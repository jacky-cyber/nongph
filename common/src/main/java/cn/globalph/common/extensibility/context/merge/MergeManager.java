package cn.globalph.common.extensibility.context.merge;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.extensibility.context.ResourceInputStream;
import cn.globalph.common.extensibility.context.merge.exceptions.MergeException;
import cn.globalph.common.extensibility.context.merge.exceptions.MergeManagerSetupException;
import cn.globalph.common.extensibility.context.merge.handlers.MergeHandler;
import cn.globalph.common.extensibility.context.merge.handlers.MergeHandlerAdapter;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * This class manages all xml merge interactions with callers. It is responsible for
 * not only loading the handler configurations, but also for cycling through the handlers
 * in a prioritized fashion and exporting the final merged document.
 *
 * @felix.wu
 *
 */
public class MergeManager {

    /**
     * Additional merge points may be added by the caller. Also default merge points
     * may be overriden to change their current behavior. This is accomplished by
     * specifying the system property denoted by the key MergeManager.MERGE_DEFINITION_SYSTEM_PROPERTY
     * with a value stating the fully qualified path of user-created property file. Please refer
     * to the default properties file located at DefaultMerge.properties
     * for more details.
     *
     */
    public static final String MERGE_DEFINITION_SYSTEM_PROPERTY = "org.broadleafcommerce.extensibility.context.merge.handlers.merge.properties";

    private static final Log LOG = LogFactory.getLog(MergeManager.class);

    private static DocumentBuilder builder;

    static {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            builder = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            LOG.error("Unable to create document builder", e);
            throw new RuntimeException(e);
        }
    }

    private MergeHandler[] handlers;

    public MergeManager() throws MergeManagerSetupException {
        try {
            Properties props = loadProperties();
            removeSkippedMergeComponents(props);
            setHandlers(props);
        } catch (IOException e) {
            throw new MergeManagerSetupException(e);
        } catch (ClassNotFoundException e) {
            throw new MergeManagerSetupException(e);
        } catch (IllegalAccessException e) {
            throw new MergeManagerSetupException(e);
        } catch (InstantiationException e) {
            throw new MergeManagerSetupException(e);
        }
    }

    private void removeSkippedMergeComponents(Properties props) {
        InputStream inputStream = this.getClass().getClassLoader()
                .getResourceAsStream("/broadleaf-commmerce/skipMergeComponents.txt");

        if (inputStream != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("mergeClassOverrides file found.");
            }

            final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            try {
                while (bufferedReader.ready())
                {
                    String line = bufferedReader.readLine();
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("mergeComponentOverrides - overridding " + line);
                    }
                    removeSkipMergeComponents(props, line);
                }
            } catch (IOException e) {
                LOG.error("Error reading resource - /broadleaf-commmerce/skipMergeComponents.txt", e);
            } finally {
                try {
                    bufferedReader.close();
                } catch (IOException ioe) {
                    LOG.error("Error closing resource - /broadleaf-commmerce/skipMergeComponents.txt", ioe);
                }
            }
        }
    }

    /**
     * Examines the properties file for an entry with an id equal to the component that we want
     * to ignore and then removes all keys that have the same number (e.g. if xpath.28 is the key
     * then handler.28, xpath.28, and priority.28 will all be removed).
     * 
     * @param props
     * @param componentName
     */
    private void removeSkipMergeComponents(Properties props, String componentName) {
        String lookupName = "@id='" + componentName.trim() + "'";
        String key = findComponentKey(lookupName, props);
        while (key  != null) {
            removeItemsMatchingKey(key, props);
            key = findComponentKey(lookupName, props);
        }
    }

    /**
     * Examines the properties file for an entry that contains the passed in component id string and returns its key
     * 
     * to ignore. 
     * 
     * @param componentName
     * @param props
     * @return
     */
    private String findComponentKey(String componentIdStr, Properties props) {
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof String) {
                String valueStr = (String) value;
                if (valueStr.contains(componentIdStr)) {
                    Object key = entry.getKey();
                    if (key instanceof String) {
                        return (String) key;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Removes all keys that share the same number.   (e.g. if xpath.28 is the key
     * then handler.28, xpath.28, and priority.28 will all be removed).
     * 
     * @param firstKey
     * @param props
     * @return
     */
    private void removeItemsMatchingKey(String firstKey, Properties props) {
        int dotPos = firstKey.indexOf(".");
        if (dotPos > 0) {
            String keyNumberToMatch = firstKey.substring(dotPos);
            
            Iterator<Object> iter = props.keySet().iterator();
            
            while (iter.hasNext()) {
                Object keyObj = iter.next();
                if (keyObj instanceof String) {
                    String keyStr = (String) keyObj;
                    dotPos = keyStr.indexOf(".");
                    String keyNumber = keyStr.substring(dotPos);
                    if (keyNumber.equals(keyNumberToMatch)) {
                        iter.remove();
                    }
                }
            }
        }
    }

    /**
     * Merge 2 xml document streams together into a final resulting stream. During
     * the merge, various merge business rules are followed based on configuration
     * defined for various merge points.
     *
     * @param stream1
     * @param stream2
     * @return the stream representing the merged document
     * @throws cn.globalph.common.extensibility.context.merge.exceptions.MergeException
     */
    public ResourceInputStream merge(ResourceInputStream stream1, ResourceInputStream stream2) throws MergeException {
        try {
            Document doc1 = builder.parse(stream1);
            Document doc2 = builder.parse(stream2);

            List<Node> exhaustedNodes = new ArrayList<Node>();

            //process any defined handlers
            for (MergeHandler handler : this.handlers) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Processing handler: " + handler.getXPath());
                }
                MergePoint point = new MergePoint(handler, doc1, doc2);
                Node[] list = point.merge(exhaustedNodes);
                if (list != null) {
                    Collections.addAll(exhaustedNodes, list);
                }
            }

            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer xmlTransformer = tFactory.newTransformer();
            xmlTransformer.setOutputProperty(OutputKeys.VERSION, "1.0");
            xmlTransformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            xmlTransformer.setOutputProperty(OutputKeys.METHOD, "xml");
            xmlTransformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc1);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(baos));
            StreamResult result = new StreamResult(writer);
            xmlTransformer.transform(source, result);

            byte[] itemArray = baos.toByteArray();

            return new ResourceInputStream(new ByteArrayInputStream(itemArray), stream2.getName(), stream1.getNames());
        } catch (Exception e) {
            throw new MergeException(e);
        }
    }

    private void setHandlers(Properties props) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ArrayList<MergeHandler> handlers = new ArrayList<MergeHandler>();
        String[] keys = props.keySet().toArray(new String[props.keySet().size()]);
        for (String key : keys) {
            if (key.startsWith("handler.")) {
                MergeHandler temp = (MergeHandler) Class.forName(props.getProperty(key)).newInstance();
                String name = key.substring(8, key.length());
                temp.setName(name);
                String priority = props.getProperty("priority." + name);
                if (priority != null) {
                    temp.setPriority(Integer.parseInt(priority));
                }
                String xpath = props.getProperty("xpath." + name);
                if (priority != null) {
                    temp.setXPath(xpath);
                }
                handlers.add(temp);
            }
        }
        MergeHandler[] explodedView = {};
        explodedView = handlers.toArray(explodedView);
        Comparator<Object> nameCompare = new Comparator<Object>() {
            public int compare(Object arg0, Object arg1) {
                return ((MergeHandler) arg0).getName().compareTo(((MergeHandler) arg1).getName());
            }
        };
        Arrays.sort(explodedView, nameCompare);
        ArrayList<MergeHandler> finalHandlers = new ArrayList<MergeHandler>();
        for (MergeHandler temp : explodedView) {
            if (temp.getName().contains(".")) {
                final String parentName = temp.getName().substring(0, temp.getName().lastIndexOf("."));
                int pos = Arrays.binarySearch(explodedView, new MergeHandlerAdapter() {
                    @Override
                    public String getName() {
                        return parentName;
                    }
                }, nameCompare);
                if (pos >= 0) {
                    MergeHandler[] parentHandlers = explodedView[pos].getChildren();
                    MergeHandler[] newHandlers = new MergeHandler[parentHandlers.length + 1];
                    System.arraycopy(parentHandlers, 0, newHandlers, 0, parentHandlers.length);
                    newHandlers[newHandlers.length - 1] = temp;
                    Arrays.sort(newHandlers);
                    explodedView[pos].setChildren(newHandlers);
                }
            } else {
                finalHandlers.add(temp);
            }
        }

        this.handlers = new MergeHandler[0];
        this.handlers = finalHandlers.toArray(this.handlers);
        Arrays.sort(this.handlers);
    }

    private Properties loadProperties() throws IOException {
        Properties defaultProperties = new Properties();
        defaultProperties.load(MergeManager.class.getResourceAsStream("/DefaultMerge.properties"));
        Properties props;
        String overrideFileClassPath = System.getProperty(MERGE_DEFINITION_SYSTEM_PROPERTY);
        if (overrideFileClassPath != null) {
            props = new Properties(defaultProperties);
            props.load(MergeManager.class.getClassLoader().getResourceAsStream(overrideFileClassPath));
        } else {
            props = defaultProperties;
        }

        return props;
    }

    public String serialize(InputStream in) {
        InputStreamReader reader = null;
        int temp;
        StringBuilder item = new StringBuilder();
        boolean eof = false;
        try {
            reader = new InputStreamReader(in);
            while (!eof) {
                temp = reader.read();
                if (temp == -1) {
                    eof = true;
                } else {
                    item.append((char) temp);
                }
            }
        } catch (IOException e) {
            LOG.error("Unable to merge source and patch locations", e);
        } finally {
            if (reader != null) {
                try{ reader.close(); } catch (Throwable e) {
                    LOG.error("Unable to merge source and patch locations", e);
                }
            }
        }

        return item.toString();
    }

}

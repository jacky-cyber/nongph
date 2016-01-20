package cn.globalph.common.web.extensibility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.extensibility.context.MergeApplicationContextXmlConfigResource;
import cn.globalph.common.extensibility.context.ResourceInputStream;
import cn.globalph.common.extensibility.context.StandardConfigLocations;
import cn.globalph.common.extensibility.context.merge.ImportProcessor;
import cn.globalph.common.extensibility.context.merge.exceptions.MergeException;

import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>In addition to standard configuration, this implementation also takes a list of
 * patch configuration files that are merged into the configuration provided above.
 * {@link cn.globalph.profile.extensibility.MergeXmlConfigResourceFactory}. The patch
 * file locations are set via the "patchConfigLocation" context-param of
 * {@link MergeContextLoader}. Patch locations
 * can either denote concrete files like "/WEB-INF/patch.xml" or Ant-style patterns
 * like "/WEB-INF/*-context.xml" (see {@link org.springframework.util.pathMatcher}
 * javadoc for pattern details).
 * </p>
 * @author felix.wu
 */
public class MergeXmlWebApplicationContext extends XmlWebApplicationContext {

    private static final Log LOG = LogFactory.getLog(MergeXmlWebApplicationContext.class);

    private String patchLocation;
    private String shutdownBean;
    private String shutdownMethod;

    /**
     * Load the bean definitions with the given XmlBeanDefinitionReader.
     * <p>The lifecycle of the bean factory is handled by the refreshBeanFactory method;
     * therefore this method is just supposed to load and/or register bean definitions.
     * <p>Delegates to a ResourcePatternResolver for resolving location patterns
     * into Resource instances.
     * @throws org.springframework.beans.BeansException in case of bean registration errors
     * @throws java.io.IOException if the required XML document isn't found
     * @see #refreshBeanFactory
     * @see #getConfigLocations
     * @see #getResources
     * @see #getResourcePatternResolver
     */
    protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws BeansException, IOException {
        String[] broadleafConfigLocations = StandardConfigLocations.retrieveAll(StandardConfigLocations.CONTEXT_TYPE_APP);
        List<ResourceInputStream> sources = new ArrayList<ResourceInputStream>(20);
        for (String location : broadleafConfigLocations) {
            InputStream source = MergeXmlWebApplicationContext.class.getClassLoader().getResourceAsStream(location);
            sources.add( new ResourceInputStream(source, location) );
        }
        ResourceInputStream[] filteredSources = new ResourceInputStream[]{};
        filteredSources = sources.toArray(filteredSources);
        
        String patchLocation = getPatchLocation();
        String[] patchLocations = StringUtils.tokenizeToStringArray(patchLocation, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
        ResourceInputStream[] patches = new ResourceInputStream[patchLocations.length];
        for (int i = 0; i < patchLocations.length; i++) {
            if (patchLocations[i].startsWith("classpath")) {
                InputStream is = MergeXmlWebApplicationContext.class.getClassLoader().getResourceAsStream(patchLocations[i].substring("classpath*:".length(), patchLocations[i].length()));
                patches[i] = new ResourceInputStream(is, patchLocations[i]);
            } else {
                Resource resource = getResourceByPath(patchLocations[i]);
                patches[i] = new ResourceInputStream(resource.getInputStream(), patchLocations[i]);
            }
            if (patches[i] == null || patches[i].available() <= 0) {
                throw new IOException("Unable to open an input stream on specified application context resource: " + patchLocations[i]);
            }
        }

        ImportProcessor importProcessor = new ImportProcessor(this);
        try {
            filteredSources = importProcessor.extract(filteredSources);
            patches = importProcessor.extract(patches);
        } catch (MergeException e) {
            throw new FatalBeanException("Unable to merge source and patch locations", e);
        }

        Resource[] resources = new MergeApplicationContextXmlConfigResource().getConfigResources(filteredSources, patches);

        reader.loadBeanDefinitions(resources);
    }

    @Override
    protected void doClose() {
        if (getShutdownBean() != null && getShutdownMethod() != null) {
            try {
                Object shutdownBean = getBean(getShutdownBean());
                Method shutdownMethod = shutdownBean.getClass().getMethod(getShutdownMethod(), new Class[]{});
                shutdownMethod.invoke(shutdownBean, new Object[]{});
            } catch (Throwable e) {
                LOG.error("Unable to execute custom shutdown call", e);
            }
        }
        super.doClose();
    }

    /**
     * @return the patchLocation
     */
    public String getPatchLocation() {
        return patchLocation;
    }

    /**
     * @param patchLocation the patchLocation to set
     */
    public void setPatchLocation(String patchLocation) {
        this.patchLocation = patchLocation;
    }

    /**
     * @return the shutdownBean
     */
    public String getShutdownBean() {
        return shutdownBean;
    }

    /**
     * @param shutdownBean the shutdownBean to set
     */
    public void setShutdownBean(String shutdownBean) {
        this.shutdownBean = shutdownBean;
    }

    /**
     * @return the shutdownMethod
     */
    public String getShutdownMethod() {
        return shutdownMethod;
    }

    /**
     * @param shutdownMethod the shutdownMethod to set
     */
    public void setShutdownMethod(String shutdownMethod) {
        this.shutdownMethod = shutdownMethod;
    }

}

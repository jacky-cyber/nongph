package cn.globalph.common.extensibility.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class StandardConfigLocations {

    private static final Log LOG = LogFactory.getLog(StandardConfigLocations.class);
    public static final String EXTRACONFIGLOCATIONSKEY = "extra.config.locations";
    
    public static final int CONTEXT_TYPE_ALL = 0;
    public static final int CONTEXT_TYPE_WEB = 1;
    public static final int CONTEXT_TYPE_SERVICE = 2;
    public static final int CONTEXT_TYPE_APP = 4;

    public static String[] retrieveAll(int contextType) throws IOException {
        String[] response;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(StandardConfigLocations.class.getResourceAsStream("/StandardConfigLocations.properties")));
            ArrayList<String> items = new ArrayList<String>();
            String line = null;
            while ( (line = reader.readLine())!=null ) {
            	addContextFile(contextType, items, line);
            }
            
            String extraConfigFiles = System.getProperty(EXTRACONFIGLOCATIONSKEY);
            if (extraConfigFiles != null) {
                String[] files = extraConfigFiles.split(" ");
                for (String file : files) {
                    addContextFile(contextType, items, file);
                }
            }
            
            response = new String[]{};
            response = items.toArray(response);
        } finally {
            if (reader != null) {
                try{ reader.close(); } catch (Throwable e) {
                    LOG.error("Unable to merge source and patch locations", e);
                }
            }
        }

        return response;
    }

    private static void addContextFile(int contextType, ArrayList<String> items, String temp) {
        if ( temp.trim().length() > 0 && 
        	!temp.startsWith("#") && StandardConfigLocations.class.getClassLoader().getResource(temp.trim()) != null ) {
        	switch(contextType){
        		case CONTEXT_TYPE_ALL : {
        			items.add(temp.trim());
        			break;
        		}
        		case CONTEXT_TYPE_WEB : {
        			if( temp.contains("-web") ) {
	    				items.add(temp.trim());
	    			}
					break;
        		}
        		case CONTEXT_TYPE_APP : {
        			if( temp.contains("-web") ) {
        				items.add(temp.trim());
        			} else if( !temp.contains("-admin") ) {
    					items.add(temp.trim());
        			}
        			break;
        		}
        		case CONTEXT_TYPE_SERVICE: {
        			if( temp.contains("-persistence-admin") ) 
        				items.add(temp.trim());
        			else if( !temp.contains("-web") && !temp.contains("-admin") )
        				items.add(temp.trim());
        		}       		
        	}
        }
    }
}

package cn.globalph.cms.file.service.operation;

import java.util.HashMap;
import java.util.Map;

/**
 * @author felix.wu
 */
public class StaticMapNamedOperationComponent implements NamedOperationComponent {
	//<名称，<参数名,参数值>>
	private Map<String, Map<String, String>> namedOperations = new HashMap<String, Map<String, String>>();
	
	public Map<String, Map<String, String>> getNamedOperations() {
        return namedOperations;
    }

    public void setNamedOperations(Map<String, Map<String, String>> namedOperations) {
        this.namedOperations = namedOperations;
    }
	    
    @Override
    public Map<String, String> setOperationValues(Map<String, String> originalParameters) {
    	Map<String, String> derivedParameters = new HashMap<String, String>(); 
        expandFulfilledMap(originalParameters, derivedParameters);
        return derivedParameters;
    }

    private void expandFulfilledMap(Map<String, String> originalParameters, Map<String, String> derivedParameters) {
        for (Map.Entry<String, String> entry : originalParameters.entrySet()) {
            if( namedOperations.containsKey( entry.getKey() ) ) {
                expandFulfilledMap( namedOperations.get(entry.getKey() ), derivedParameters );
            } else {
                derivedParameters.put( entry.getKey(), entry.getValue() );
            }
        }
    }
}

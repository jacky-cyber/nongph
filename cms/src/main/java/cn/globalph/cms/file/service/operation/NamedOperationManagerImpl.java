package cn.globalph.cms.file.service.operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NamedOperationManagerImpl implements NamedOperationManager {

    private List<NamedOperationComponent> namedOperationComponents = new ArrayList<NamedOperationComponent>();
    
    public List<NamedOperationComponent> getNamedOperationComponents() {
        return namedOperationComponents;
    }

    public void setNamedOperationComponents(List<NamedOperationComponent> namedOperationComponents) {
        this.namedOperationComponents = namedOperationComponents;
    }
    
    @Override
    public Map<String, String> applyNamedParameters(Map<String, String> parameterMap) {
        Map<String, String> derivedMap = new HashMap<String, String>();
        for (NamedOperationComponent namedOperationComponent : namedOperationComponents) {
        	derivedMap.putAll( namedOperationComponent.setOperationValues(parameterMap) );
        }
        return derivedMap;
    }
}

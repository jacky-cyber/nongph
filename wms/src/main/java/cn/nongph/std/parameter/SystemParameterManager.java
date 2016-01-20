package com.felix.std.parameter;

import com.felix.std.model.SystemParameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author felix
 */
@ApplicationScoped
@Named
public class SystemParameterManager {
    private static final Map<String, String> defaultMap = new HashMap<String, String>();
    static{
        defaultMap.put( SystemParameter.PN_VIEW_ROUND_TO_NEXT_ROUND, SystemParameter.VIEW_ROUND_TO_NEXT_ROUND.PASS.name() );
        defaultMap.put( SystemParameter.PN_VIEW_ROUND_PASS, SystemParameter.VIEW_ROUND_PASS.ALL_PASS.name() );
    }
    
    private ConcurrentMap<String, String> map = new ConcurrentHashMap();
    private boolean loaded = false;
    @Inject
    private SystemParameterDao dao;
    
    public String getParameterValue(String name){
        if( !loaded ) 
            load();
        String result = map.get( name );
        if( result==null || result.trim().isEmpty() ) {
            result = defaultMap.get( name );
        }
        return result;
    }
    
    private void load(){
        List<SystemParameter> list = dao.getAll();
        for(SystemParameter sp : list){
            map.put( sp.getName(), sp.getValue() );
        }
        loaded = true;
    }
    
    public void reload(){
        loaded = false;
    }
}

package com.felix.std.parameter;

import com.felix.nsf.CommonDao;
import com.felix.std.model.SystemParameter;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 *
 * @author felix
 */
@ApplicationScoped
@Named
public class SystemParameterDao extends CommonDao<SystemParameter>{

    @Override
    protected Class<SystemParameter> getModelClass() {
        return SystemParameter.class;
    }
    
}

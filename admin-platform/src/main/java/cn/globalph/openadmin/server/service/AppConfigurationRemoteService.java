package cn.globalph.openadmin.server.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @felix.wu
 */
@Service("blAppConfigurationRemoteService")
public class AppConfigurationRemoteService implements AppConfigurationService {

    private static final Log LOG = LogFactory.getLog(AppConfigurationRemoteService.class);

    @Resource(name = "blAppConfigurationMap")
    protected Map<String, String> propertyConfigurations = new HashMap<String, String>();

    @Override
    public Boolean getBooleanPropertyValue(String propertyName) {
        if (propertyConfigurations.get(propertyName) == null) {
            return null;
        } else {
            return Boolean.valueOf(propertyConfigurations.get(propertyName));
        }
    }

}

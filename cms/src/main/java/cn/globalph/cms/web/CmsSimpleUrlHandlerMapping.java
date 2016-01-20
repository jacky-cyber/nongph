package cn.globalph.cms.web;

import cn.globalph.common.config.RuntimeEnvironmentPropertiesConfigurer;

import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import javax.annotation.Resource;

import java.util.Properties;

public class CmsSimpleUrlHandlerMapping extends SimpleUrlHandlerMapping {

    @Resource(name="blConfiguration")
    protected RuntimeEnvironmentPropertiesConfigurer configurer;

    @Override
    public void setMappings(Properties mappings) {
        Properties clone = new Properties();
        for (Object propertyName: mappings.keySet()) {
            String newName = configurer.getStringValueResolver().resolveStringValue(propertyName.toString());
            clone.put(newName, mappings.get(propertyName));
        }
        super.setMappings(clone);
    }
}

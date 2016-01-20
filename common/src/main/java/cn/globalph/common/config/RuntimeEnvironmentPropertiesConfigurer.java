package cn.globalph.common.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.StringValueResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RuntimeEnvironmentPropertiesConfigurer extends PropertyPlaceholderConfigurer implements InitializingBean {
	
    private final static List<Resource> NPH_CONFIG_LOCATIONS = new ArrayList<Resource>();
    
    static {
    	NPH_CONFIG_LOCATIONS.add(new ClassPathResource("config/bc/"));
    	NPH_CONFIG_LOCATIONS.add(new ClassPathResource("config/bc/admin/"));
    	NPH_CONFIG_LOCATIONS.add(new ClassPathResource("config/bc/cms/"));
    	NPH_CONFIG_LOCATIONS.add(new ClassPathResource("config/bc/fw/"));
    	NPH_CONFIG_LOCATIONS.add(new ClassPathResource("config/bc/web/"));
    }
    
    private StringValueResolver stringValueResolver;

    public RuntimeEnvironmentPropertiesConfigurer() {
        super();
        setIgnoreUnresolvablePlaceholders(true); // This default will get overriden by user options if present
        setNullValue("@null");
    }

    public void afterPropertiesSet() throws IOException {
        
        ArrayList<Resource> allLocations = new ArrayList<Resource>();
        
        /**
         * 按以下顺序加载配置 (后面的配置覆写前面的配置)
         * 1. 在NPH_CONFIG_LOCATIONS常量指定的目录中加载框架内置的配置
         * 2. config-shared.properties
         * 3. config.properties
         */
        //加载框架内置配置
        for (Resource resource : createGlobalphFrameworkResource()) {
            if (resource.exists()) {
                allLocations.add(resource);
            }
        }
        allLocations.add( new ClassPathResource("config-shared.properties") );
        allLocations.add( new ClassPathResource("config.properties") );

        setLocations(allLocations.toArray(new Resource[] {}));
    }
    
    private Resource[] createGlobalphFrameworkResource() throws IOException {
        Resource[] resources = new Resource[NPH_CONFIG_LOCATIONS.size()];
        for ( int i = 0; i<NPH_CONFIG_LOCATIONS.size(); i++) {
            resources[i] = NPH_CONFIG_LOCATIONS.get(i).createRelative("framework.properties");
        }
        return resources;
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        stringValueResolver = new PlaceholderResolvingStringValueResolver(props);
    }

    private class PlaceholderResolvingStringValueResolver implements StringValueResolver {

        private final PropertyPlaceholderHelper helper;

        private final PropertyPlaceholderHelper.PlaceholderResolver resolver;

        public PlaceholderResolvingStringValueResolver(Properties props) {
            this.helper = new PropertyPlaceholderHelper("${", "}", ":", true);
            this.resolver = new PropertyPlaceholderConfigurerResolver(props);
        }

        public String resolveStringValue(String strVal) throws BeansException {
            String value = this.helper.replacePlaceholders(strVal, this.resolver);
            return (value.equals("") ? null : value);
        }
    }

    private class PropertyPlaceholderConfigurerResolver implements PropertyPlaceholderHelper.PlaceholderResolver {

        private final Properties props;

        private PropertyPlaceholderConfigurerResolver(Properties props) {
            this.props = props;
        }

        public String resolvePlaceholder(String placeholderName) {
            return RuntimeEnvironmentPropertiesConfigurer.this.resolvePlaceholder(placeholderName, props, 1);
        }
    }

    public StringValueResolver getStringValueResolver() {
        return stringValueResolver;
    }
}
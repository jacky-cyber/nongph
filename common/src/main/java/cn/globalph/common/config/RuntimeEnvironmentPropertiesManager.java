package cn.globalph.common.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

public class RuntimeEnvironmentPropertiesManager implements BeanFactoryAware {

    private static final Log LOG = LogFactory.getLog(RuntimeEnvironmentPropertiesManager.class);

    protected ConfigurableBeanFactory beanFactory;

    protected String prefix;

    public String getPrefix() {
        return prefix;
    }

    public String setPrefix(String prefix) {
        return this.prefix = prefix;
    }

    public String getProperty(String key, String suffix) {
        if(key==null) {
            return null;
        }
        String name = prefix + "." + key + "." + suffix;
        if (prefix == null) {
            name = key + "." + suffix;
        }
        String rv = beanFactory.resolveEmbeddedValue("${" + name + "}");
       
        if (rv == null ||rv.equals("${" + name + "}")) {
            LOG.warn("property ${" + name + "} not found, Reverting to property without suffix"+suffix);
            rv = getProperty(key);
        }
        return rv;

    }

    public String getProperty(String key) {
        if(key==null) {
            return null;
        }
        String name = prefix + "." + key;
        if (prefix == null) {
            name = key;
        }
        String rv = beanFactory.resolveEmbeddedValue("${" + name + "}");
        if(rv.equals("${" + name + "}")) {
            return null;
        }
        return rv;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableBeanFactory) beanFactory;
    }

}

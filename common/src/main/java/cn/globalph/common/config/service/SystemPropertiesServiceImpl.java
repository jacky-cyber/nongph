package cn.globalph.common.config.service;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.lang3.StringUtils;

import cn.globalph.common.config.RuntimeEnvironmentPropertiesManager;
import cn.globalph.common.config.dao.SystemPropertiesDao;
import cn.globalph.common.config.domain.SystemProperty;
import cn.globalph.common.config.service.type.SystemPropertyFieldType;
import cn.globalph.common.extensibility.jpa.SiteDiscriminator;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.web.WebRequestContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Service that retrieves property settings from the database.   If not set in 
 * the DB then returns the value from property files.
 */
@Service("blSystemPropertiesService")
public class SystemPropertiesServiceImpl implements SystemPropertiesService{

    protected Cache systemPropertyCache;

    @Resource(name="blSystemPropertiesDao")
    protected SystemPropertiesDao systemPropertiesDao;

    @Resource(name = "blSystemPropertyServiceExtensionManager")
    protected SystemPropertyServiceExtensionManager extensionManager;

    @Value("${system.property.cache.timeout}")
    protected int systemPropertyCacheTimeout;

    @Autowired
    protected RuntimeEnvironmentPropertiesManager propMgr;


    @Override
    public String resolveSystemProperty(String name) {
        if (extensionManager != null) {
            ExtensionResultHolder holder = new ExtensionResultHolder();
            extensionManager.getHandlerProxy().resolveProperty(name, holder);
            if (holder.getResult() != null) {
                return holder.getResult().toString();
            }
        }

        String result;
        // We don't want to utilize this cache for sandboxes
        if (WebRequestContext.getWebRequestContext().getSandBox() == null) {
            result = getPropertyFromCache(name);
        } else {
            result = null;
        }

        if (result != null) {
            return result;
        }

        SystemProperty property = systemPropertiesDao.readSystemPropertyByName(name);
        if (property == null || StringUtils.isEmpty(property.getValue())) {
            result = propMgr.getProperty(name);
        } else {
            if ("_blank_".equals(property.getValue())) {
                result = "";
            } else {
                result = property.getValue();
            }
        }

        if (result != null) {
            addPropertyToCache(name, result);
        }
        return result;
    }

    protected void addPropertyToCache(String propertyName, String propertyValue) {
        String key = buildKey(propertyName);
        if (systemPropertyCacheTimeout < 0) {
            getSystemPropertyCache().put(new Element(key, propertyValue));
        } else {
            getSystemPropertyCache().put(new Element(key, propertyValue, systemPropertyCacheTimeout, 
                    systemPropertyCacheTimeout));
        }
    }

    protected String getPropertyFromCache(String propertyName) {
        String key = buildKey(propertyName);
        Element cacheElement = getSystemPropertyCache().get(key);
        if (cacheElement != null && cacheElement.getObjectValue() != null) {
            return (String) cacheElement.getObjectValue();
        }
        return null;
    }

    /**
     * Properties can vary by site.   If a site is found on the request, use the site id as part of the
     * cache-key.
     *
     * @param propertyName
     * @return
     */
    protected String buildKey(String propertyName) {
        String key = propertyName;
        WebRequestContext brc = WebRequestContext.getWebRequestContext();
        if (brc != null) {
            if (brc.getSite() != null) {
                key = brc.getSite().getId() + "-" + key;
            }
        }
        return key;
    }

    /**
     * Properties can vary by site.   If a site is found on the request, use the site id as part of the
     * cache-key.
     * 
     * @param systemProperty
     * @return
     */
    protected String buildKey(SystemProperty systemProperty) {
        String key = systemProperty.getName();
        if (systemProperty instanceof SiteDiscriminator && ((SiteDiscriminator) systemProperty).getSiteDiscriminator() != null) {
            key = ((SiteDiscriminator) systemProperty).getSiteDiscriminator() + "-" + key;
        }
        return key;
    }

    protected Cache getSystemPropertyCache() {
        if (systemPropertyCache == null) {
            systemPropertyCache = CacheManager.getInstance().getCache("blSystemPropertyElements");
        }
        return systemPropertyCache;
    }

    @Override
    public SystemProperty findById(Long id) {
        return systemPropertiesDao.readById(id);
    }
    
    @Override
    public void removeFromCache(SystemProperty systemProperty) {
        //Could have come from a cache invalidation service that does not
        //include the site on the thread, so we should build the key
        //including the site (if applicable) from the systemProperty itself
        String key = buildKey(systemProperty);
        getSystemPropertyCache().remove(key);
        systemPropertiesDao.removeFromCache(systemProperty);
    }

    @Override
    public int resolveIntSystemProperty(String name) {
        String systemProperty = resolveSystemProperty(name);
        if (systemProperty == null) {
            return 0;
        }

        return Integer.valueOf(systemProperty).intValue();
    }

    @Override
    public boolean resolveBooleanSystemProperty(String name) {
        String systemProperty = resolveSystemProperty(name);
        if (systemProperty == null) {
            return false;
        }
        return Boolean.valueOf(systemProperty).booleanValue();
    }

    @Override
    public long resolveLongSystemProperty(String name) {
        String systemProperty = resolveSystemProperty(name);
        if (systemProperty == null) {
            return 0l;
        }

        return Long.valueOf(systemProperty).longValue();
    }
    
    @Override
    public boolean isValueValidForType(String value, SystemPropertyFieldType type) {
        if (type.equals(SystemPropertyFieldType.BOOLEAN_TYPE)) {
            value = value.toUpperCase();
            if (value != null && (value.equals("TRUE") || value.equals("FALSE"))) {
                return true;
            }
        } else if (type.equals(SystemPropertyFieldType.INT_TYPE)) {
            try {
                Integer.parseInt(value);
                return true;
            } catch (Exception e) {
                // Do nothing - we will fail on validation
            }
        } else if (type.equals(SystemPropertyFieldType.LONG_TYPE)) {
            try {
                Long.parseLong(value);
                return true;
            } catch (Exception e) {
                // Do nothing - we will fail on validation
            }
        } else if (type.equals(SystemPropertyFieldType.DOUBLE_TYPE)) {
            try {
                Double.parseDouble(value);
                return true;
            } catch (Exception e) {
                // Do nothing - we will fail on validation
            }
        } else if (type.equals(SystemPropertyFieldType.STRING_TYPE)) {
            return true;
        }

        return false;
    }
}

package cn.globalph.common.extensibility.cache.ehcache;

import net.sf.ehcache.CacheManager;
import cn.globalph.common.extensibility.context.ResourceInputStream;
import cn.globalph.common.extensibility.context.merge.MergeXmlConfigResource;

import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MergeEhCacheManagerFactoryBean extends EhCacheManagerFactoryBean implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @javax.annotation.Resource(name="blMergedCacheConfigLocations")
    private Set<String> mergedCacheConfigLocations;

    private List<Resource> configLocations;

    @Override
    public void destroy() {
        super.destroy();
        try {
            CacheManager cacheManager = getObject();
            Field cacheManagerTimer = CacheManager.class.getDeclaredField("cacheManagerTimer");
            cacheManagerTimer.setAccessible(true);
            Object failSafeTimer = cacheManagerTimer.get(cacheManager);
            Field timer = failSafeTimer.getClass().getDeclaredField("timer");
            timer.setAccessible(true);
            Object time = timer.get(failSafeTimer);
            Field thread = time.getClass().getDeclaredField("thread");
            thread.setAccessible(true);
            Thread item = (Thread) thread.get(time);
            item.setContextClassLoader(Thread.currentThread().getContextClassLoader().getParent());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void configureMergedItems() {
        List<Resource> temp = new ArrayList<Resource>();
        if (mergedCacheConfigLocations != null && !mergedCacheConfigLocations.isEmpty()) {
            for (String location : mergedCacheConfigLocations) {
                temp.add(applicationContext.getResource(location));
            }
        }
        if (configLocations != null && !configLocations.isEmpty()) {
            for (Resource resource : configLocations) {
                temp.add(resource);
            }
        }
        try {
            MergeXmlConfigResource merge = new MergeXmlConfigResource();
            ResourceInputStream[] sources = new ResourceInputStream[temp.size()];
            int j=0;
            for (Resource resource : temp) {
                sources[j] = new ResourceInputStream(resource.getInputStream(), resource.getURL().toString());
                j++;
            }
            setConfigLocation(merge.getMergedConfigResource(sources));
        } catch (Exception e) {
            throw new FatalBeanException("Unable to merge cache locations", e);
        }
    }

    public void setConfigLocations(List<Resource> configLocations) throws BeansException {
        this.configLocations = configLocations;
    }
}

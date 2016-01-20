package cn.globalph.common.extensibility.jpa;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.exception.ExceptionHelper;
import cn.globalph.common.extensibility.jpa.convert.GlobalphClassTransformer;
import cn.globalph.common.extensibility.jpa.copy.NullClassTransformer;

import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager;
import org.springframework.orm.jpa.persistenceunit.Jpa2PersistenceUnitInfoDecorator;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.SmartPersistenceUnitInfo;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.sql.DataSource;

/**
 * 将类路径中的 persistence.xml文件进行合并。 
 * MergePersistenceUnitManager跟踪合并后的持久单元名字和和数据源。
 * 可以通过单元名字请求PersistenceUnitInfo, 此时返回的PersistenceUnitInfo有被修改的 jar文件urls、类名和mapping文件名（包括all persistence.xml的合并）
 * @author felix.wu
 */
public class MergePersistenceUnitManager extends DefaultPersistenceUnitManager {

    private static final Log LOG = LogFactory.getLog(MergePersistenceUnitManager.class);

    protected HashMap<String, PersistenceUnitInfo> mergedPus = new HashMap<String, PersistenceUnitInfo>();
    protected final boolean jpa2ApiPresent = ClassUtils.hasMethod(PersistenceUnitInfo.class, "getSharedCacheMode");
    protected List<GlobalphClassTransformer> classTransformers = new ArrayList<GlobalphClassTransformer>();

    @Resource(name="blMergedPersistenceXmlLocations")
    private Set<String> mergedPersistenceXmlLocations;

    @Resource(name="blMergedDataSources")
    private Map<String, DataSource> mergedDataSources;
    
    @Resource(name="blMergedClassTransformers")
    private Set<GlobalphClassTransformer> mergedClassTransformers;

    @PostConstruct
    public void configureMergedItems() {
        String[] tempLocations;
        try {
            Field persistenceXmlLocations = DefaultPersistenceUnitManager.class.getDeclaredField("persistenceXmlLocations");
            persistenceXmlLocations.setAccessible(true);
            tempLocations = (String[]) persistenceXmlLocations.get(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for (String legacyLocation : tempLocations) {
            if ( !legacyLocation.endsWith("/persistence.xml") ) {
                //do not add the default JPA persistence location by default
                mergedPersistenceXmlLocations.add(legacyLocation);
            }
        }
        setPersistenceXmlLocations( mergedPersistenceXmlLocations.toArray(new String[mergedPersistenceXmlLocations.size()]) );

        if ( !mergedDataSources.isEmpty() ) {
            setDataSources(mergedDataSources);
        }
    }
    
    @PostConstruct
    public void configureClassTransformers() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        classTransformers.addAll(mergedClassTransformers);
    }

    protected PersistenceUnitInfo getMergedUnit(String persistenceUnitName, MutablePersistenceUnitInfo newPU) {
        if (!mergedPus.containsKey(persistenceUnitName)) {
            PersistenceUnitInfo puiToStore = newPU;
            if (jpa2ApiPresent) {
                puiToStore = (PersistenceUnitInfo) Proxy.newProxyInstance(SmartPersistenceUnitInfo.class.getClassLoader(),
                        new Class[] {SmartPersistenceUnitInfo.class}, new Jpa2PersistenceUnitInfoDecorator(newPU));
            }
            mergedPus.put(persistenceUnitName, puiToStore);
        }
        return mergedPus.get(persistenceUnitName);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void preparePersistenceUnitInfos() {
        //Need to use reflection to try and execute the logic in the DefaultPersistenceUnitManager
        //SpringSource added a block of code in version 3.1 to "protect" the user from having more than one PU with
        //the same name.  Of course, in our case, this happens before a merge occurs.  They have added
        //a block of code to throw an exception if more than one PU has the same name.  We want to
        //use the logic of the DefaultPersistenceUnitManager without the exception in the case of
        //a duplicate name. This will require reflection in order to do what we need.
        try {
        	Class<DefaultPersistenceUnitManager> supperClass = DefaultPersistenceUnitManager.class;
            
        	Set<String> persistenceUnitInfoNames = null;
            Map<String, PersistenceUnitInfo> persistenceUnitInfos = null;
            ResourcePatternResolver resourcePatternResolver = null;
            Field[] fields = supperClass.getDeclaredFields();
            for (Field field : fields) {
                if ("persistenceUnitInfoNames".equals(field.getName())) {
                    field.setAccessible(true);
                    persistenceUnitInfoNames = (Set<String>)field.get(this);
                    persistenceUnitInfoNames.clear();
                } else if ("persistenceUnitInfos".equals(field.getName())) {
                    field.setAccessible(true);
                    persistenceUnitInfos = (Map<String, PersistenceUnitInfo>)field.get(this);
                    persistenceUnitInfos.clear();
                } else if ("resourcePatternResolver".equals(field.getName())) {
                    field.setAccessible(true);
                    resourcePatternResolver = (ResourcePatternResolver)field.get(this);
                }
            }

            Method readPersistenceUnitInfos = supperClass.getDeclaredMethod("readPersistenceUnitInfos");
            readPersistenceUnitInfos.setAccessible(true);
            
            List<MutablePersistenceUnitInfo> puis = (List<MutablePersistenceUnitInfo>)readPersistenceUnitInfos.invoke(this);
            for (MutablePersistenceUnitInfo pui : puis) {
                if ( pui.getPersistenceUnitRootUrl() == null) {
                    Method determineDefaultPersistenceUnitRootUrl = supperClass.getDeclaredMethod("determineDefaultPersistenceUnitRootUrl");
                    determineDefaultPersistenceUnitRootUrl.setAccessible(true);
                    pui.setPersistenceUnitRootUrl((URL)determineDefaultPersistenceUnitRootUrl.invoke(this));
                }
                ConfigurationOnlyState state = ConfigurationOnlyState.getState();
                if ((state == null || !state.isConfigurationOnly()) && pui.getNonJtaDataSource() == null) {
                	pui.setNonJtaDataSource(getDefaultDataSource());
                }
                if (super.getLoadTimeWeaver() != null) {
                    Method puiInitMethod = pui.getClass().getDeclaredMethod("init", LoadTimeWeaver.class);
                    puiInitMethod.setAccessible(true);
                    puiInitMethod.invoke(pui, getLoadTimeWeaver());
                }
                else {
                    Method puiInitMethod = pui.getClass().getDeclaredMethod("init", ClassLoader.class);
                    puiInitMethod.setAccessible(true);
                    puiInitMethod.invoke(pui, resourcePatternResolver.getClassLoader());
                }
                postProcessPersistenceUnitInfo((MutablePersistenceUnitInfo)pui);
                String name = pui.getPersistenceUnitName();
                persistenceUnitInfoNames.add(name);

                PersistenceUnitInfo puiToStore = pui;
                if (jpa2ApiPresent) {
                    InvocationHandler jpa2PersistenceUnitInfoDecorator = null;
                    Class<?>[] classes = getClass().getSuperclass().getDeclaredClasses();
                    for (Class<?> clz : classes){
                        if ("org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager$Jpa2PersistenceUnitInfoDecorator"
                                .equals(clz.getName())) {
                            Constructor<?> constructor =
                                    clz.getConstructor(Class.forName("org.springframework.orm.jpa.persistenceunit.SpringPersistenceUnitInfo"));
                            constructor.setAccessible(true);
                            jpa2PersistenceUnitInfoDecorator = (InvocationHandler)constructor.newInstance(pui);
                            break;
                        }
                    }

                    puiToStore = (PersistenceUnitInfo) Proxy.newProxyInstance(SmartPersistenceUnitInfo.class.getClassLoader(),
                            new Class[] {SmartPersistenceUnitInfo.class}, jpa2PersistenceUnitInfoDecorator);
                }
                persistenceUnitInfos.put(name, puiToStore);
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occured reflectively invoking methods on " +
                    "class: " + getClass().getSuperclass().getName(), e);
        }

        try {
            List<String> managedClassNames = new ArrayList<String>();
            
            for (PersistenceUnitInfo pui : mergedPus.values()) {
                for (GlobalphClassTransformer transformer : classTransformers) {
                    try {
                        if (!(transformer instanceof NullClassTransformer) && pui.getPersistenceUnitName().equals("blPU")) {
                            pui.addTransformer(transformer);
                        }
                    } catch (Exception e) {
                        Exception refined = ExceptionHelper.refineException(IllegalStateException.class, RuntimeException.class, e);
                        if (refined instanceof IllegalStateException) {
                            LOG.warn("A BroadleafClassTransformer is configured for this persistence unit, but Spring " +
                                    "reported a problem (likely that a LoadTimeWeaver is not registered). As a result, " +
                                    "the Broadleaf Commerce ClassTransformer ("+transformer.getClass().getName()+") is " +
                                    "not being registered with the persistence unit.");
                        } else {
                            throw refined;
                        }
                    }
                }
            }
            
            for (PersistenceUnitInfo pui : mergedPus.values()) {
                for (String managedClassName : pui.getManagedClassNames()) {
                    if (!managedClassNames.contains(managedClassName)) {
                        // Force-load this class so that we are able to ensure our instrumentation happens globally.
                        Class.forName(managedClassName, true, getClass().getClassLoader());
                        managedClassNames.add(managedClassName);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo newPU) {
        super.postProcessPersistenceUnitInfo(newPU);
        ConfigurationOnlyState state = ConfigurationOnlyState.getState();
        String persistenceUnitName = newPU.getPersistenceUnitName();
        
        PersistenceUnitInfo pui = getMergedUnit(persistenceUnitName, newPU);
        Jpa2PersistenceUnitInfoDecorator dec = (Jpa2PersistenceUnitInfoDecorator) Proxy.getInvocationHandler(pui);
        MutablePersistenceUnitInfo temp = (MutablePersistenceUnitInfo) dec.getTarget();

        List<String> managedClassNames = newPU.getManagedClassNames();
        for (String managedClassName : managedClassNames){
            if ( !temp.getManagedClassNames().contains(managedClassName) ) {
                temp.addManagedClassName(managedClassName);
            }
        }
        
        List<String> mappingFileNames = newPU.getMappingFileNames();
        for (String mappingFileName : mappingFileNames) {
            if (!temp.getMappingFileNames().contains(mappingFileName)) {
                temp.addMappingFileName(mappingFileName);
            }
        }
        temp.setExcludeUnlistedClasses(newPU.excludeUnlistedClasses());
        for (URL url : newPU.getJarFileUrls()) {
            // Avoid duplicate class scanning by Ejb3Configuration. Do not re-add the URL to the list of jars for this
            // persistence unit or duplicate the persistence unit root URL location (both types of locations are scanned)
            if (!temp.getJarFileUrls().contains(url) && !temp.getPersistenceUnitRootUrl().equals(url)) {
                temp.addJarFileUrl(url);
            }
        }
        if (temp.getProperties() == null) {
            temp.setProperties(newPU.getProperties());
        } else {
            Properties props = newPU.getProperties();
            if (props != null) {
                for (Object key : props.keySet()) {
                    temp.getProperties().put(key, props.get(key));
                    for (GlobalphClassTransformer transformer : classTransformers) {
                        try {
                            transformer.compileJPAProperties(props, key);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        if (state == null || !state.isConfigurationOnly()) {
            if (newPU.getJtaDataSource() != null) {
                temp.setJtaDataSource(newPU.getJtaDataSource());
            }
            if (newPU.getNonJtaDataSource() != null) {
                temp.setNonJtaDataSource(newPU.getNonJtaDataSource());
            }
        } else {
            temp.getProperties().setProperty("hibernate.hbm2ddl.auto", "none");
            temp.getProperties().setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");
        }
        temp.setTransactionType(newPU.getTransactionType());
        if (newPU.getPersistenceProviderClassName() != null) {
            temp.setPersistenceProviderClassName(newPU.getPersistenceProviderClassName());
        }
        if (newPU.getPersistenceProviderPackageName() != null) {
            temp.setPersistenceProviderPackageName(newPU.getPersistenceProviderPackageName());
        }
    }

    @Override
    public PersistenceUnitInfo obtainPersistenceUnitInfo(String persistenceUnitName) {
        return mergedPus.get(persistenceUnitName);
    }

    @Override
    public PersistenceUnitInfo obtainDefaultPersistenceUnitInfo() {
        throw new IllegalStateException("Default Persistence Unit is not supported. The persistence unit name must be specified at the entity manager factory.");
    }

    public List<GlobalphClassTransformer> getClassTransformers() {
        return classTransformers;
    }

    public void setClassTransformers(List<GlobalphClassTransformer> classTransformers) {
        this.classTransformers = classTransformers;
    }

}

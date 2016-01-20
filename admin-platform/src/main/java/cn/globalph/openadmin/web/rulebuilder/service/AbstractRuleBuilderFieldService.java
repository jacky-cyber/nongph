package cn.globalph.openadmin.web.rulebuilder.service;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.web.WebRequestContext;
import cn.globalph.openadmin.server.dao.DynamicEntityDao;
import cn.globalph.openadmin.server.service.persistence.PersistenceOperationExecutor;
import cn.globalph.openadmin.server.service.persistence.PersistenceOperationExecutorFactory;
import cn.globalph.openadmin.server.service.persistence.TargetModeType;
import cn.globalph.openadmin.web.rulebuilder.dto.FieldDTO;
import cn.globalph.openadmin.web.rulebuilder.dto.FieldData;
import cn.globalph.openadmin.web.rulebuilder.dto.FieldWrapper;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

/**
 * @author Elbert Bautista (elbertbautista)
 */
public abstract class AbstractRuleBuilderFieldService implements RuleBuilderFieldService, ApplicationContextAware, InitializingBean {

    protected DynamicEntityDao dynamicEntityDao;
    protected ApplicationContext applicationContext;
    protected List<FieldData> fields = new ArrayList<FieldData>();
    protected static Boolean handlersInitialized = false;

    @Resource(name = "blRuleBuilderFieldServiceExtensionManager")
    protected RuleBuilderFieldServiceExtensionManager extensionManager;

    @Override
    public void setRuleBuilderFieldServiceExtensionManager(RuleBuilderFieldServiceExtensionManager extensionManager) {
        this.extensionManager = extensionManager;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public FieldWrapper buildFields() {
        FieldWrapper wrapper = new FieldWrapper();

        for (FieldData field : getFields()) {
            FieldDTO fieldDTO = new FieldDTO();
            fieldDTO.setLabel(field.getFieldLabel());
            
            //translate the label to display
            String label = field.getFieldLabel();
            WebRequestContext context = WebRequestContext.getWebRequestContext();
            MessageSource messages = context.getMessageSource();
            label = messages.getMessage(label, null, label, Locale.CHINA);
            fieldDTO.setLabel(label);
            
            fieldDTO.setName(field.getFieldName());
            fieldDTO.setOperators(field.getOperators());
            fieldDTO.setOptions(field.getOptions());
            wrapper.getFields().add(fieldDTO);
        }

        return wrapper;
    }

    @Override
    public SupportedFieldType getSupportedFieldType(String fieldName) {
        SupportedFieldType type = null;
        if (fieldName != null) {
            for (FieldData field : getFields()) {
                if (fieldName.equals(field.getFieldName())){
                    return field.getFieldType();
                }
            }
        }
        return type;
    }

    @Override
    public SupportedFieldType getSecondaryFieldType(String fieldName) {
        SupportedFieldType type = null;
        if (fieldName != null) {
            for (FieldData field : getFields()) {
                if (fieldName.equals(field.getFieldName())){
                    return field.getSecondaryFieldType();
                }
            }
        }
        return type;
    }

    @Override
    public FieldDTO getField(String fieldName) {
        for (FieldData field : getFields()) {
            if (field.getFieldName().equals(fieldName)) {
                FieldDTO fieldDTO = new FieldDTO();
                fieldDTO.setLabel(field.getFieldLabel());
                fieldDTO.setName(field.getFieldName());
                fieldDTO.setOperators(field.getOperators());
                fieldDTO.setOptions(field.getOptions());
                return fieldDTO;
            }
        }
        return null;
    }

    @Override
    public List<FieldData> getFields() {
        // Initialize additional static fields method for the component.  
        if (!handlersInitialized) {
            synchronized (handlersInitialized) {
                if (!handlersInitialized) {
                    if (extensionManager != null) {
                        extensionManager.getHandlerProxy().addFields(fields, getName());
                    }
                }
            }
            handlersInitialized = true;
        }

        return fields;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setFields(final List<FieldData> fields) {
        List<FieldData> proxyFields = (List<FieldData>) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{List.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getName().equals("add")) {
                    FieldData fieldData = (FieldData) args[0];
                    testFieldName(fieldData);
                }
                if (method.getName().equals("addAll")) {
                    Collection<FieldData> addCollection = (Collection<FieldData>) args[0];
                    Iterator<FieldData> itr = addCollection.iterator();
                    while (itr.hasNext()) {
                        FieldData fieldData = itr.next();
                        testFieldName(fieldData);
                    }
                }
                return method.invoke(fields, args);
            }

            private void testFieldName(FieldData fieldData) throws ClassNotFoundException {
                if (!StringUtils.isEmpty(fieldData.getFieldName()) && dynamicEntityDao != null) {
                    Class<?>[] dtos = dynamicEntityDao.getAllPolymorphicEntitiesFromCeiling(Class.forName(getDtoClassName()));
                    if (ArrayUtils.isEmpty(dtos)) {
                        dtos = new Class<?>[]{Class.forName(getDtoClassName())};
                    }
                    Field field = null;
                    for (Class<?> dto : dtos) {
                        field = dynamicEntityDao.getFieldManager().getField(dto, fieldData.getFieldName());
                        if (field != null) {
                            break;
                        }
                    }
                    if (field == null) {
                        throw new IllegalArgumentException("Unable to find the field declared in FieldData (" + fieldData.getFieldName() + ") on the target class (" + getDtoClassName() + "), or any registered entity class that derives from it.");
                    }
                }
            }
        });
        this.fields = proxyFields;
    }

    @Override
    public RuleBuilderFieldService clone() throws CloneNotSupportedException {
        try {
            RuleBuilderFieldService clone = this.getClass().newInstance();
            clone.setFields(this.fields);
            clone.setRuleBuilderFieldServiceExtensionManager(extensionManager);
            return clone;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public abstract String getDtoClassName();

    public abstract void init();

    @Override
    public void afterPropertiesSet() throws Exception {
        // This bean only is valid when the following bean is active. (admin)
        if (applicationContext.containsBean(PersistenceOperationExecutorFactory.getPersistenceManagerRef())) {
            PersistenceOperationExecutor persistenceManager = (PersistenceOperationExecutor) applicationContext.getBean(PersistenceOperationExecutorFactory.getPersistenceManagerRef());
            persistenceManager.setTargetMode(TargetModeType.SANDBOX);
            dynamicEntityDao = persistenceManager.getDynamicEntityDao();
            setFields(new ArrayList<FieldData>());
            
            // This cannot be null during startup as we do not want to remove the null safety checks in a multi-tenant env.
            boolean contextWasNull = false;
            if (WebRequestContext.getWebRequestContext() == null) {
                WebRequestContext brc = new WebRequestContext();
                brc.setIgnoreSite(true);
                WebRequestContext.setWebRequestContext(brc);
                contextWasNull = true;
            }

            try {
                init();
            } finally {
                if (contextWasNull) {
                    WebRequestContext.setWebRequestContext(null);
                }
            }
        }
    }

}

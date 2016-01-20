package cn.globalph.openadmin.audit;

import cn.globalph.common.time.SystemTime;
import cn.globalph.common.web.WebRequestContext;
import cn.globalph.openadmin.server.security.domain.AdminUser;

import java.lang.reflect.Field;
import java.util.Calendar;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class AdminAuditableListener {

    @PrePersist
    public void setAuditCreatedBy(Object entity) throws Exception {
        if (entity.getClass().isAnnotationPresent(Entity.class)) {
            Field field = getSingleField(entity.getClass(), "auditable");
            field.setAccessible(true);
            if (field.isAnnotationPresent(Embedded.class)) {
                Object auditable = field.get(entity);
                if (auditable == null) {
                    field.set(entity, new AdminAuditable());
                    auditable = field.get(entity);
                }
                Field temporalCreatedField = auditable.getClass().getDeclaredField("dateCreated");
                Field temporalUpdatedField = auditable.getClass().getDeclaredField("dateUpdated");
                Field agentField = auditable.getClass().getDeclaredField("createdBy");
                setAuditValueTemporal(temporalCreatedField, auditable);
                setAuditValueTemporal(temporalUpdatedField, auditable);
                setAuditValueAgent(agentField, auditable);
            }
        }
    }

    @PreUpdate
    public void setAuditUpdatedBy(Object entity) throws Exception {
        if (entity.getClass().isAnnotationPresent(Entity.class)) {
            Field field = getSingleField(entity.getClass(), "auditable");
            field.setAccessible(true);
            if (field.isAnnotationPresent(Embedded.class)) {
                Object auditable = field.get(entity);
                if (auditable == null) {
                    field.set(entity, new AdminAuditable());
                    auditable = field.get(entity);
                }
                Field temporalField = auditable.getClass().getDeclaredField("dateUpdated");
                Field agentField = auditable.getClass().getDeclaredField("updatedBy");
                setAuditValueTemporal(temporalField, auditable);
                setAuditValueAgent(agentField, auditable);
            }
        }
    }

    protected void setAuditValueTemporal(Field field, Object entity) throws IllegalArgumentException, IllegalAccessException {
        Calendar cal = SystemTime.asCalendar();
        field.setAccessible(true);
        field.set(entity, cal.getTime());
    }

    protected void setAuditValueAgent(Field field, Object entity) throws IllegalArgumentException, IllegalAccessException {
        try {
            WebRequestContext context = WebRequestContext.getWebRequestContext();
            if (context != null && context.getAdmin() && context.getAdditionalProperties().containsKey("adminUser")) {
                field.setAccessible(true);
                field.set(entity, ((AdminUser) context.getAdditionalProperties().get("adminUser")).getId());
            }
        } catch (IllegalStateException e) {
            //do nothing
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Field getSingleField(Class<?> clazz, String fieldName) throws IllegalStateException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException nsf) {
            // Try superclass
            if (clazz.getSuperclass() != null) {
                return getSingleField(clazz.getSuperclass(), fieldName);
            }

            return null;
        }
    }
}

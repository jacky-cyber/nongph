package cn.globalph.openadmin.web.compatibility;

import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.Property;
import cn.globalph.openadmin.server.service.JSCompatibilityHelper;
import cn.globalph.openadmin.web.form.component.RuleBuilderField;
import cn.globalph.openadmin.web.form.entity.EntityForm;
import cn.globalph.openadmin.web.form.entity.Field;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;

/**
 * @author Jeff Fischer
 */
public class JSFieldNameCompatibilityInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView
            modelAndView) throws Exception {
        if (modelAndView != null) {
            Entity entity = (Entity) modelAndView.getModelMap().get("entity");
            EntityForm entityForm = (EntityForm) modelAndView.getModelMap().get("entityForm");
    
            if (entity != null) {
                for (Property property : entity.getProperties()) {
                    if (property.getName().contains(".")) {
                        property.setName(JSCompatibilityHelper.encode(property.getName()));
                    }
                }
            }
    
            if (entityForm != null) {
                entityForm.clearFieldsMap();
                for (Map.Entry<String, Field> field : entityForm.getFields().entrySet()) {
                    if (field.getKey().contains(".")) {
                        field.getValue().setName(JSCompatibilityHelper.encode(field.getValue().getName()));
                        if (field.getValue() instanceof RuleBuilderField) {
                            ((RuleBuilderField) field.getValue()).setJsonFieldName(JSCompatibilityHelper.encode((
                                    (RuleBuilderField) field.getValue()).getJsonFieldName()));
                        }
                    }
                }
                entityForm.clearFieldsMap();
            }
        }
    }
}

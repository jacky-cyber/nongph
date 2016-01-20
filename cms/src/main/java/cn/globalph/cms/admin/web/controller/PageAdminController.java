package cn.globalph.cms.admin.web.controller;

import cn.globalph.cms.page.domain.Page;
import cn.globalph.cms.page.domain.PageTemplate;
import cn.globalph.openadmin.dto.PersistencePackageRequest;
import cn.globalph.openadmin.web.controller.entity.EntityAdminController;
import cn.globalph.openadmin.web.form.entity.DynamicEntityFormInfo;
import cn.globalph.openadmin.web.form.entity.EntityForm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles admin operations for the {@link Page} entity. This entity has fields that are 
 * dependent on the value of the {@link Page#getPageTemplate()} field, and as such,
 * it deviates from the typical {@link AdminAbstractEntityController}.
 * 
 * @author Andre Azzolini (apazzolini)
 */
@Controller("blAdminPageController")
@RequestMapping("/" + PageAdminController.SECTION_KEY)
public class PageAdminController extends EntityAdminController {
    
    protected static final String SECTION_KEY = "pages";
    
    @Override
    protected String getSectionKey(Map<String, String> pathVars) {
        //allow external links to work for ToOne items
        if (super.getSectionKey(pathVars) != null) {
            return super.getSectionKey(pathVars);
        }
        return SECTION_KEY;
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String viewEntityForm(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable  Map<String, String> pathVars,
            @PathVariable(value="id") String id) throws Exception {
        // Get the normal entity form for this item
        String returnPath = super.viewEntityForm(request, response, model, pathVars, id);
        EntityForm ef = (EntityForm) model.asMap().get("entityForm");
        
        // Attach the dynamic fields to the form
        DynamicEntityFormInfo info = new DynamicEntityFormInfo()
                .withCeilingClassName(PageTemplate.class.getName())
                .withSecurityCeilingClassName(Page.class.getName())
                .withCriteriaName("constructForm")
                .withPropertyName("pageTemplate")
                .withPropertyValue(ef.findField("pageTemplate").getValue());
        EntityForm dynamicForm = getDynamicFieldTemplateForm(info, id, null);
        ef.putDynamicFormInfo("pageTemplate", info);
        ef.putDynamicForm("pageTemplate", dynamicForm);
        
        // Mark the field that will drive this dynamic form
        ef.findField("pageTemplate").setOnChangeTrigger("dynamicForm-pageTemplate");
        
        return returnPath;
    }
    
    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String saveEntity(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable  Map<String, String> pathVars,
            @PathVariable(value="id") String id,
            @ModelAttribute(value="entityForm") EntityForm entityForm, BindingResult result,
            RedirectAttributes ra) throws Exception {
        // Attach the dynamic form info so that the update service will know how to split up the fields
        DynamicEntityFormInfo info = new DynamicEntityFormInfo()
                .withCeilingClassName(PageTemplate.class.getName())
                .withSecurityCeilingClassName(Page.class.getName())
                .withCriteriaName("constructForm")
                .withPropertyName("pageTemplate");
        entityForm.putDynamicFormInfo("pageTemplate", info);
        
        String returnPath = super.saveEntity(request, response, model, pathVars, id, entityForm, result, ra);
        if (result.hasErrors()) {
            info = entityForm.getDynamicFormInfo("pageTemplate");
            info.setPropertyValue(entityForm.findField("pageTemplate").getValue());
            
            //grab back the dynamic form that was actually put in
            EntityForm inputDynamicForm = entityForm.getDynamicForm("pageTemplate");
            
            EntityForm dynamicForm = getDynamicFieldTemplateForm(info, id, inputDynamicForm);
            entityForm.putDynamicForm("pageTemplate", dynamicForm);
        }
        
        return returnPath;
    }
    
    @RequestMapping(value = "/{propertyName}/dynamicForm", method = RequestMethod.GET)
    public String getDynamicForm(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable  Map<String, String> pathVars,
            @PathVariable("propertyName") String propertyName,
            @RequestParam("propertyTypeId") String propertyTypeId) throws Exception {
        DynamicEntityFormInfo info = new DynamicEntityFormInfo()
                .withCeilingClassName(PageTemplate.class.getName())
                .withSecurityCeilingClassName(Page.class.getName())
                .withCriteriaName("constructForm")
                .withPropertyName(propertyName)
                .withPropertyValue(propertyTypeId);
        
        return super.getDynamicForm(request, response, model, pathVars, info);
    }

    @Override
    protected void attachSectionSpecificInfo(PersistencePackageRequest ppr, Map<String, String> pathVars) {
        ppr.setSecurityRootEntityClassname(Page.class.getName());
    }
}

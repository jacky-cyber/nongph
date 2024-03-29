package cn.globalph.openadmin.web.controller.entity;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.exception.SecurityServiceException;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.presentation.client.AddMethodType;
import cn.globalph.common.sandbox.SandBoxHelper;
import cn.globalph.common.util.BLCArrayUtils;
import cn.globalph.openadmin.dto.AdornedTargetCollectionMetadata;
import cn.globalph.openadmin.dto.AdornedTargetList;
import cn.globalph.openadmin.dto.BasicCollectionMetadata;
import cn.globalph.openadmin.dto.BasicFieldMetadata;
import cn.globalph.openadmin.dto.ClassMetadata;
import cn.globalph.openadmin.dto.ClassTree;
import cn.globalph.openadmin.dto.CollectionMetadata;
import cn.globalph.openadmin.dto.DynamicResultSet;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.FilterAndSortCriteria;
import cn.globalph.openadmin.dto.MapMetadata;
import cn.globalph.openadmin.dto.PersistencePackageRequest;
import cn.globalph.openadmin.dto.Property;
import cn.globalph.openadmin.dto.SectionCrumb;
import cn.globalph.openadmin.server.security.domain.AdminSection;
import cn.globalph.openadmin.server.security.remote.EntityOperationType;
import cn.globalph.openadmin.server.service.ValidationException;
import cn.globalph.openadmin.server.service.persistence.PersistenceResponse;
import cn.globalph.openadmin.server.service.persistence.module.BasicPersistenceModule;
import cn.globalph.openadmin.web.controller.BasicAdminController;
import cn.globalph.openadmin.web.editor.NonNullBooleanEditor;
import cn.globalph.openadmin.web.form.component.DefaultListGridActions;
import cn.globalph.openadmin.web.form.component.ListGrid;
import cn.globalph.openadmin.web.form.entity.DefaultEntityFormActions;
import cn.globalph.openadmin.web.form.entity.DefaultMainActions;
import cn.globalph.openadmin.web.form.entity.EntityForm;
import cn.globalph.openadmin.web.form.entity.EntityFormAction;
import cn.globalph.openadmin.web.form.entity.Field;
import cn.globalph.openadmin.web.form.entity.Tab;

import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The default implementation of the {@link #BasicAdminController}. This delegates every call to 
 * super and does not provide any custom-tailored functionality. It is responsible for rendering the admin for every
 * entity that is not explicitly customized by its own controller.
 */
@Controller("entityAdminController")
@RequestMapping("/{sectionKey:.+}")
public class EntityAdminController extends BasicAdminController {
    protected static final Log LOG = LogFactory.getLog(EntityAdminController.class);

    @Resource(name="blSandBoxHelper")
    SandBoxHelper sandBoxHelper;

    /**
     * Renders the main entity listing for the specified class, which is based on the current sectionKey with some optional
     * criteria.
       * 
     * @param request
     * @param response
     * @param model
     * @param pathVars
     * @param criteria a Map of property name -> list critiera values
     * @return the return view path
     * @throws Exception
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String viewEntityList(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable Map<String, String> pathVars,
            @RequestParam MultiValueMap<String, String> requestParams) throws Exception {
        String sectionKey = getSectionKey(pathVars);
        String sectionClassName = getClassNameForSection(sectionKey);
        List<SectionCrumb> crumbs = getSectionCrumbs(request, null, null);
        PersistencePackageRequest ppr = getSectionPersistencePackageRequest(sectionClassName, requestParams, crumbs, pathVars);

        ClassMetadata cmd = entityAdminService.getPersistenceResponse(ppr).getDynamicResultSet().getClassMetaData();
        DynamicResultSet drs =  entityAdminService.getRecords(ppr).getDynamicResultSet();

        ListGrid listGrid = formBuilder.buildMainListGrid(drs, cmd, sectionKey, crumbs);
        List<EntityFormAction> mainActions = new ArrayList<EntityFormAction>();
        addAddActionIfAllowed( sectionClassName, cmd, mainActions );
        
        Field firstField = listGrid.getHeaderFields().iterator().next();
        if (requestParams.containsKey(firstField.getName())) {
            model.addAttribute("mainSearchTerm", requestParams.get(firstField.getName()).get(0));
        }
        
        extensionManager.getHandlerProxy().addAdditionalMainActions(sectionClassName, mainActions);
        
        model.addAttribute("entityFriendlyName", cmd.getPolymorphicEntities().getFriendlyName());
        model.addAttribute("currentUrl", request.getRequestURL().toString());
        model.addAttribute("listGrid", listGrid);
        model.addAttribute("mainActions", mainActions);
        model.addAttribute("viewType", "entityList");

        setModelAttributes(model, sectionKey);
        return "modules/defaultContainer";
    }

    /**
     * Adds the "Add" button to the main entity form if the current user has permissions to create new instances
     * of the entity and all of the fields in the entity aren't marked as read only.
     * 
     * @param sectionClassName
     * @param cmd
     * @param mainActions
     */
    protected void addAddActionIfAllowed(String sectionClassName, ClassMetadata cmd, List<EntityFormAction> mainActions) {
        if (isAddActionAllowed(sectionClassName, cmd)) {
            mainActions.add(DefaultMainActions.ADD);
        }
        
        mainEntityActionsExtensionManager.getHandlerProxy().modifyMainActions(cmd, mainActions);
    }
    
    protected boolean isAddActionAllowed(String sectionClassName, ClassMetadata cmd) {
        // If the user does not have create permissions, we will not add the "Add New" button
        boolean canCreate = true;
        try {
            adminRemoteSecurityService.securityCheck(sectionClassName, EntityOperationType.ADD);
        } catch (ServiceException e) {
            if (e instanceof SecurityServiceException) {
                canCreate = false;
            }
        }
        
        if (canCreate) {
            checkReadOnly: {
                //check if all the metadata is read only
                for (Property property : cmd.getProperties()) {
                    if (property.getFieldMetadata() instanceof BasicFieldMetadata) {
                        if (((BasicFieldMetadata) property.getFieldMetadata()).getReadOnly() == null ||
                                !((BasicFieldMetadata) property.getFieldMetadata()).getReadOnly()) {
                            break checkReadOnly;
                        }
                    }
                }
                canCreate = false;
            }
        }
        
        return canCreate;
    }

    /**
     * Renders the modal form that is used to add a new parent level entity. Note that this form cannot render any
     * subcollections as operations on those collections require the parent level entity to first be saved and have 
     * and id. Once the entity is initially saved, we will redirect the user to the normal manage entity screen where 
     * they can then perform operations on sub collections.
     * 
     * @param request
     * @param response
     * @param model
     * @param pathVars
     * @param entityType
     * @return the return view path
     * @throws Exception
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String viewAddEntityForm(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable  Map<String, String> pathVars,
            @RequestParam(defaultValue = "") String entityType) throws Exception {
        String sectionKey = getSectionKey(pathVars);
        String sectionClassName = getClassNameForSection(sectionKey);
        List<SectionCrumb> sectionCrumbs = getSectionCrumbs(request, null, null);
        ClassMetadata cmd = entityAdminService.getPersistenceResponse(getSectionPersistencePackageRequest(sectionClassName, sectionCrumbs, pathVars))
                .getDynamicResultSet().getClassMetaData();

        // If the entity type isn't specified, we need to determine if there are various polymorphic types for this entity.
        if (StringUtils.isBlank(entityType)) {
            if (cmd.getPolymorphicEntities().getChildren().length == 0) {
                entityType = cmd.getPolymorphicEntities().getClassname();
            } else {
                entityType = getDefaultEntityType();
            }
        } else {
            entityType = URLDecoder.decode(entityType, "UTF-8");
        }

        // If we still don't have a type selected, that means that there were indeed multiple possible types and we 
        // will be allowing the user to pick his desired type.
        if (StringUtils.isBlank(entityType)) {
            List<ClassTree> entityTypes = getAddEntityTypes(cmd.getPolymorphicEntities());
            model.addAttribute("entityTypes", entityTypes);
            model.addAttribute("viewType", "modal/entityTypeSelection");
            String requestUri = request.getRequestURI();
            if (!request.getContextPath().equals("/") && requestUri.startsWith(request.getContextPath())) {
                requestUri = requestUri.substring(request.getContextPath().length() + 1, requestUri.length());
            }
            model.addAttribute("currentUri", requestUri);
        } else {
            EntityForm entityForm = formBuilder.createEntityForm(cmd, sectionCrumbs);
            
            // We need to make sure that the ceiling entity is set to the interface and the specific entity type
            // is set to the type we're going to be creating.
            entityForm.setRootEntityClassname(cmd.getClassName());
            entityForm.setEntityType(entityType);
            
            // When we initially build the class metadata (and thus, the entity form), we had all of the possible
            // polymorphic fields built out. Now that we have a concrete entity type to render, we can remove the
            // fields that are not applicable for this given entity type.
            formBuilder.removeNonApplicableFields(cmd, entityForm, entityType);

            model.addAttribute("entityForm", entityForm);
            model.addAttribute("viewType", "modal/entityAdd");
        }

        model.addAttribute("entityFriendlyName", cmd.getPolymorphicEntities().getFriendlyName());
        model.addAttribute("currentUrl", request.getRequestURL().toString());
        model.addAttribute("modalHeaderType", "addEntity");
        setModelAttributes(model, sectionKey);
        return "modules/modalContainer";
    }

    /**
     * Processes the request to add a new entity. If successful, returns a redirect to the newly created entity.
     * 
     * @param request
     * @param response
     * @param model
     * @param pathVars
     * @param entityForm
     * @param result
     * @return the return view path
     * @throws Exception
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addEntity(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable  Map<String, String> pathVars,
            @ModelAttribute(value="entityForm") EntityForm entityForm, BindingResult result) throws Exception {
        String sectionKey = getSectionKey(pathVars);

        extractDynamicFormFields(entityForm);
        List<SectionCrumb> sectionCrumbs = getSectionCrumbs(request, null, null);
        Entity entity = entityAdminService.addEntity(entityForm, getSectionCustomCriteria(), sectionCrumbs).getEntity();
        entityFormValidator.validate(entityForm, entity, result);

        if (result.hasErrors()) {
            String sectionClassName = getClassNameForSection(sectionKey);
            ClassMetadata cmd = entityAdminService.getPersistenceResponse(getSectionPersistencePackageRequest(sectionClassName, sectionCrumbs, pathVars)).getDynamicResultSet().getClassMetaData();
            entityForm.clearFieldsMap();
            formBuilder.populateEntityForm(cmd, entity, entityForm, sectionCrumbs);

            formBuilder.removeNonApplicableFields(cmd, entityForm, entityForm.getEntityType());

            model.addAttribute("viewType", "modal/entityAdd");
            model.addAttribute("currentUrl", request.getRequestURL().toString());
            model.addAttribute("modalHeaderType", "addEntity");
            setModelAttributes(model, sectionKey);
            return "modules/modalContainer";
        }
        
        // Note that AJAX Redirects need the context path prepended to them
        return "ajaxredirect:" + getContextPath(request) + sectionKey + "/" + entity.getPMap().get("id").getValue();
    }

    /**
     * Renders the main entity form for the specified entity
     * 
     * @param request
     * @param response
     * @param model
     * @param pathVars
     * @param id
     * @param modal - whether or not to show the entity in a read-only modal
     * @return the return view path
     * @throws Exception
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String viewEntityForm(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable  Map<String, String> pathVars,
            @PathVariable("id") String id) throws Exception {
        String sectionKey = getSectionKey(pathVars);
        String sectionClassName = getClassNameForSection(sectionKey);
        List<SectionCrumb> crumbs = getSectionCrumbs(request, sectionKey, id);
        PersistencePackageRequest ppr = getSectionPersistencePackageRequest(sectionClassName, crumbs, pathVars);

        ClassMetadata cmd = entityAdminService.getPersistenceResponse(ppr).getDynamicResultSet().getClassMetaData();
        Entity entity = entityAdminService.getRecord(ppr, id, cmd, false).getDynamicResultSet().getRecords()[0];
        
        Map<String, DynamicResultSet> subRecordsMap = entityAdminService.getRecordsForAllSubCollections(ppr, entity, crumbs);

        EntityForm entityForm = formBuilder.createEntityForm(cmd, entity, subRecordsMap, crumbs);
        
        model.addAttribute("entity", entity);
        model.addAttribute("entityForm", entityForm);
        model.addAttribute("currentUrl", request.getRequestURL().toString());

        setModelAttributes(model, sectionKey);
        
        if (sandBoxHelper.isSandBoxable(entityForm.getEntityType())) {
            Tab auditTab = new Tab();
            auditTab.setTitle("Audit");
            auditTab.setOrder(Integer.MAX_VALUE);
            auditTab.setTabClass("audit-tab");
            entityForm.getTabs().add(auditTab);
        }

        if (isAjaxRequest(request)) {
            entityForm.setReadOnly();
            model.addAttribute("viewType", "modal/entityView");
            model.addAttribute("modalHeaderType", "viewEntity");
            return "modules/modalContainer";
        } else {
            model.addAttribute("viewType", "entityEdit");
            return "modules/defaultContainer";
        }
    }

    /**
     * Attempts to save the given entity. If validation is unsuccessful, it will re-render the entity form with
     * error fields highlighted. On a successful save, it will refresh the entity page.
     * 
     * @param request
     * @param response
     * @param model
     * @param pathVars
     * @param id
     * @param entityForm
     * @param result
     * @return the return view path
     * @throws Exception
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String saveEntity(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable  Map<String, String> pathVars,
            @PathVariable(value="id") String id,
            @ModelAttribute(value="entityForm") EntityForm entityForm, BindingResult result,
            RedirectAttributes ra) throws Exception {
        String sectionKey = getSectionKey(pathVars);
        String sectionClassName = getClassNameForSection(sectionKey);
        List<SectionCrumb> sectionCrumbs = getSectionCrumbs(request, sectionKey, id);
        PersistencePackageRequest ppr = getSectionPersistencePackageRequest(sectionClassName, sectionCrumbs, pathVars);

        extractDynamicFormFields(entityForm);
        
        Entity entity = entityAdminService.updateEntity(entityForm, getSectionCustomCriteria(), sectionCrumbs).getEntity();

        entityFormValidator.validate(entityForm, entity, result);
        if (result.hasErrors()) {
            model.addAttribute("headerFlash", "save.unsuccessful");
            model.addAttribute("headerFlashAlert", true);
            
            Map<String, DynamicResultSet> subRecordsMap = entityAdminService.getRecordsForAllSubCollections(ppr, entity, sectionCrumbs);
            ClassMetadata cmd = entityAdminService.getPersistenceResponse(ppr).getDynamicResultSet().getClassMetaData();
            entityForm.clearFieldsMap();
            formBuilder.populateEntityForm(cmd, entity, subRecordsMap, entityForm, sectionCrumbs);
            
            model.addAttribute("entity", entity);
            model.addAttribute("currentUrl", request.getRequestURL().toString());

            setModelAttributes(model, sectionKey);
            
            if (isAjaxRequest(request)) {
                entityForm.setReadOnly();
                model.addAttribute("viewType", "modal/entityView");
                model.addAttribute("modalHeaderType", "viewEntity");
                return "modules/modalContainer";
            } else {
                model.addAttribute("viewType", "entityEdit");
                return "modules/defaultContainer";
            }
        }
        
        ra.addFlashAttribute("headerFlash", "save.successful");
        
        return "redirect:/" + sectionKey + "/" + id;
    }

    /**
     * Attempts to remove the given entity.
     * 
     * @param request
     * @param response
     * @param model
     * @param pathVars
     * @param id
     * @return the return view path
     * @throws Exception
     */
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    public String removeEntity(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable  Map<String, String> pathVars,
            @PathVariable(value="id") String id,
            @ModelAttribute(value="entityForm") EntityForm entityForm, BindingResult result) throws Exception {
        String sectionKey = getSectionKey(pathVars);
        List<SectionCrumb> sectionCrumbs = getSectionCrumbs(request, sectionKey, id);

        try {
            entityAdminService.removeEntity(entityForm, getSectionCustomCriteria(), sectionCrumbs);
        } catch (ServiceException e) {
            if (e instanceof ValidationException) {
                // Create a flash attribute for the unsuccessful delete
                FlashMap fm = new FlashMap();
                fm.put("headerFlash", e.getMessage());
                fm.put("headerFlashAlert", true);
                request.setAttribute(DispatcherServlet.OUTPUT_FLASH_MAP_ATTRIBUTE, fm);

                // Make sure we have this error show up in our logs
                LOG.error("Could not delete record", e);

                // Refresh the page
                return "redirect:/" + sectionKey + "/" + id;
            }
            if (e.containsCause(ConstraintViolationException.class)) {
                // Create a flash attribute for the unsuccessful delete
                FlashMap fm = new FlashMap();
                fm.put("headerFlash", "delete.unsuccessful");
                fm.put("headerFlashAlert", true);
                request.setAttribute(DispatcherServlet.OUTPUT_FLASH_MAP_ATTRIBUTE, fm);
                
                // Make sure we have this error show up in our logs
                LOG.error("Could not delete record", e);

                // Refresh the page
                return "redirect:/" + sectionKey + "/" + id;
            }
            throw e;
        }

        return "redirect:/" + sectionKey;
    }

    @RequestMapping(value = "/{collectionField:.*}/details", method = RequestMethod.GET)
    public @ResponseBody Map<String, String> getCollectionValueDetails(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable  Map<String, String> pathVars,
            @PathVariable(value="collectionField") String collectionField,
            @RequestParam String ids,
            @RequestParam MultiValueMap<String, String> requestParams) throws Exception {
        String sectionKey = getSectionKey(pathVars);
        String sectionClassName = getClassNameForSection(sectionKey);
        List<SectionCrumb> sectionCrumbs = getSectionCrumbs(request, null, null);
        PersistencePackageRequest ppr = getSectionPersistencePackageRequest(sectionClassName, requestParams, sectionCrumbs, pathVars);
        ClassMetadata mainMetadata = entityAdminService.getPersistenceResponse(ppr).getDynamicResultSet().getClassMetaData();
        Property collectionProperty = mainMetadata.getPMap().get(collectionField);
        FieldMetadata md = collectionProperty.getFieldMetadata();

        ppr = PersistencePackageRequest.fromMetadata(md, sectionCrumbs);
        ppr.setStartIndex(getStartIndex(requestParams));
        ppr.setMaxIndex(getMaxIndex(requestParams));
        
        if (md instanceof BasicFieldMetadata) {
            String idProp = ((BasicFieldMetadata) md).getForeignKeyProperty();
            String displayProp = ((BasicFieldMetadata) md).getForeignKeyDisplayValueProperty();

            List<String> filterValues = BLCArrayUtils.asList(ids.split(FILTER_VALUE_SEPARATOR_REGEX));
            ppr.addFilterAndSortCriteria(new FilterAndSortCriteria(idProp, filterValues));
            
            DynamicResultSet drs = entityAdminService.getRecords(ppr).getDynamicResultSet();
            Map<String, String> returnMap = new HashMap<String, String>();
            
            for (Entity e : drs.getRecords()) {
                String id = e.getPMap().get(idProp).getValue();
                String disp = e.getPMap().get(displayProp).getDisplayValue();
                
                if (StringUtils.isBlank(disp)) {
                    disp = e.getPMap().get(displayProp).getValue();
                }
                
                returnMap.put(id,  disp);
            }

            return returnMap;
        }

        return null;
    }
    
    /**
     * Shows the modal popup for the current selected "to-one" field. For instance, if you are viewing a list of products
     * then this method is invoked when a user clicks on the name of the default category field.
     * 
     * @param request
     * @param response
     * @param model
     * @param pathVars
     * @param collectionField
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{collectionField:.*}/{id}/view", method = RequestMethod.GET)
    public String viewCollectionItemDetails(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable  Map<String, String> pathVars,
            @PathVariable(value="collectionField") String collectionField,
            @PathVariable(value="id") String id) throws Exception {
        String sectionKey = getSectionKey(pathVars);
        String mainClassName = getClassNameForSection(sectionKey);
        List<SectionCrumb> sectionCrumbs = getSectionCrumbs(request, sectionKey, id);
        ClassMetadata mainMetadata = entityAdminService.getPersistenceResponse(getSectionPersistencePackageRequest(mainClassName, sectionCrumbs, pathVars)).getDynamicResultSet().getClassMetaData();
        Property collectionProperty = mainMetadata.getPMap().get(collectionField);
        BasicFieldMetadata md = (BasicFieldMetadata) collectionProperty.getFieldMetadata();

        AdminSection section = adminNavigationService.findAdminSectionByClass(md.getForeignKeyClass());
        String sectionUrlKey = (section.getUrl().startsWith("/")) ? section.getUrl().substring(1) : section.getUrl();
        Map<String, String> varsForField = new HashMap<String, String>();
        varsForField.put("sectionKey", sectionUrlKey);
        return viewEntityForm(request, response, model, varsForField, id);
    }

    /**
     * Returns the records for a given collectionField filtered by a particular criteria
     * 
     * @param request
     * @param response
     * @param model
     * @param pathVars
     * @param collectionField
     * @param criteriaForm
     * @return the return view path
     * @throws Exception
     */
    @RequestMapping(value = "/{id}/{collectionField:.*}", method = RequestMethod.GET)
    public String getCollectionFieldRecords(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable  Map<String, String> pathVars,
            @PathVariable(value="id") String id,
            @PathVariable(value="collectionField") String collectionField,
            @RequestParam  MultiValueMap<String, String> requestParams) throws Exception {
        String sectionKey = getSectionKey(pathVars);
        String mainClassName = getClassNameForSection(sectionKey);
        List<SectionCrumb> sectionCrumbs = getSectionCrumbs(request, sectionKey, id);
        PersistencePackageRequest ppr = getSectionPersistencePackageRequest(mainClassName, requestParams, sectionCrumbs, pathVars);
        ClassMetadata mainMetadata = entityAdminService.getPersistenceResponse(ppr).getDynamicResultSet().getClassMetaData();
        Property collectionProperty = mainMetadata.getPMap().get(collectionField);
        
        ppr = getSectionPersistencePackageRequest(mainClassName, sectionCrumbs, pathVars);
        Entity entity = entityAdminService.getRecord(ppr, id, mainMetadata, false).getDynamicResultSet().getRecords()[0];

        // Next, we must get the new list grid that represents this collection
        ListGrid listGrid = getCollectionListGrid(mainMetadata, entity, collectionProperty, requestParams, sectionKey, sectionCrumbs);
        model.addAttribute("listGrid", listGrid);

        model.addAttribute("currentParams", new ObjectMapper().writeValueAsString(requestParams));

        // We return the new list grid so that it can replace the currently visible one
        setModelAttributes(model, sectionKey);
        return "views/standaloneListGrid";
    }

    /**
     * Shows the modal dialog that is used to add an item to a given collection. There are several possible outcomes
     * of this call depending on the type of the specified collection field.
     * 
     * <ul>
     *  <li>
     *    <b>Basic Collection (Persist)</b> - Renders a blank form for the specified target entity so that the user may
     *    enter information and associate the record with this collection. Used by fields such as ProductAttribute.
     *  </li>
     *  <li>
     *    <b>Basic Collection (Lookup)</b> - Renders a list grid that allows the user to click on an entity and select it. 
     *    Used by fields such as "allParentCategories".
     *  </li>
     *  <li>
     *    <b>Adorned Collection (without form)</b> - Renders a list grid that allows the user to click on an entity and 
     *    select it. The view rendered by this is identical to basic collection (lookup), but will perform the operation
     *    on an adorned field, which may carry extra meta-information about the created relationship, such as order.
     *  </li>
     *  <li>
     *    <b>Adorned Collection (with form)</b> - Renders a list grid that allows the user to click on an entity and 
     *    select it. Once the user selects the entity, he will be presented with an empty form based on the specified
     *    "maintainedAdornedTargetFields" for this field. Used by fields such as "crossSellProducts", which in addition
     *    to linking an entity, provide extra fields, such as a promotional message.
     *  </li>
     *  <li>
     *    <b>Map Collection</b> - Renders a form for the target entity that has an additional key field. This field is
     *    populated either from the configured map keys, or as a result of a lookup in the case of a key based on another
     *    entity. Used by fields such as the mediaMap on a Sku.
     *  </li>
     *  
     * @param request
     * @param response
     * @param model
     * @param sectionKey
     * @param id
     * @param collectionField
     * @param requestParams
     * @return the return view path
     * @throws Exception
     */
    @RequestMapping(value = "/{id}/{collectionField:.*}/add", method = RequestMethod.GET)
    public String showAddCollectionItem(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable Map<String, String> pathVars,
            @PathVariable(value = "id") String id,
            @PathVariable(value = "collectionField") String collectionField,
            @RequestParam MultiValueMap<String, String> requestParams) throws Exception {
        String sectionKey = getSectionKey(pathVars);
        String mainClassName = getClassNameForSection(sectionKey);
        List<SectionCrumb> sectionCrumbs = getSectionCrumbs(request, sectionKey, id);
        ClassMetadata mainMetadata = entityAdminService.getPersistenceResponse(getSectionPersistencePackageRequest(mainClassName,
                sectionCrumbs, pathVars)).getDynamicResultSet().getClassMetaData();
        Property collectionProperty = mainMetadata.getPMap().get(collectionField);
        FieldMetadata md = collectionProperty.getFieldMetadata();

        PersistencePackageRequest ppr = PersistencePackageRequest.fromMetadata(md, sectionCrumbs)
                .withFilterAndSortCriteria(getCriteria(requestParams))
                .withStartIndex(getStartIndex(requestParams))
                .withMaxIndex(getMaxIndex(requestParams));

        if (md instanceof BasicCollectionMetadata) {
            BasicCollectionMetadata fmd = (BasicCollectionMetadata) md;
            if (fmd.getAddMethodType().equals(AddMethodType.PERSIST)) {
                ClassMetadata cmd = entityAdminService.getPersistenceResponse(ppr).getDynamicResultSet().getClassMetaData();
                // If the entity type isn't specified, we need to determine if there are various polymorphic types
                // for this entity.
                String entityType = null;
                if (requestParams.containsKey("entityType")) {
                    entityType = requestParams.get("entityType").get(0);
                }
                if (StringUtils.isBlank(entityType)) {
                    if (cmd.getPolymorphicEntities().getChildren().length == 0) {
                        entityType = cmd.getPolymorphicEntities().getClassname();
                    } else {
                        entityType = getDefaultEntityType();
                    }
                } else {
                    entityType = URLDecoder.decode(entityType, "UTF-8");
                }

                if (StringUtils.isBlank(entityType)) {
                    List<ClassTree> entityTypes = getAddEntityTypes(cmd.getPolymorphicEntities());
                    model.addAttribute("entityTypes", entityTypes);
                    model.addAttribute("viewType", "modal/entityTypeSelection");
                    model.addAttribute("entityFriendlyName", cmd.getPolymorphicEntities().getFriendlyName());
                    String requestUri = request.getRequestURI();
                    if (!request.getContextPath().equals("/") && requestUri.startsWith(request.getContextPath())) {
                        requestUri = requestUri.substring(request.getContextPath().length() + 1, requestUri.length());
                    }
                    model.addAttribute("currentUri", requestUri);
                    model.addAttribute("modalHeaderType", "addEntity");
                    setModelAttributes(model, sectionKey);
                    return "modules/modalContainer";
                } else {
                    ppr = ppr.withRootEntityClassname(entityType);
                }
            }
        }

        //service.getContextSpecificRelationshipId(mainMetadata, entity, prefix);

        model.addAttribute("currentParams", new ObjectMapper().writeValueAsString(requestParams));

        return buildAddCollectionItemModel(request, response, model, id, collectionField, sectionKey, collectionProperty, md, ppr, null, null);
    }
    
    /**
     * Adds the requested collection item
     * 
     * @param request
     * @param response
     * @param model
     * @param pathVars
     * @param id
     * @param collectionField
     * @param entityForm
     * @return the return view path
     * @throws Exception
     */
    @RequestMapping(value = "/{id}/{collectionField:.*}/add", method = RequestMethod.POST)
    public String addCollectionItem(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable Map<String, String> pathVars,
            @PathVariable(value="id") String id,
            @PathVariable(value="collectionField") String collectionField,
            @ModelAttribute(value="entityForm") EntityForm entityForm, BindingResult result) throws Exception {
        String sectionKey = getSectionKey(pathVars);
        String mainClassName = getClassNameForSection(sectionKey);
        List<SectionCrumb> sectionCrumbs = getSectionCrumbs(request, sectionKey, id);
        ClassMetadata mainMetadata = entityAdminService.getPersistenceResponse(getSectionPersistencePackageRequest(mainClassName, sectionCrumbs, pathVars)).getDynamicResultSet().getClassMetaData();
        Property collectionProperty = mainMetadata.getPMap().get(collectionField);
        
        if (StringUtils.isBlank(entityForm.getEntityType())) {
            FieldMetadata fmd = collectionProperty.getFieldMetadata();
            if (fmd instanceof BasicCollectionMetadata) {
                entityForm.setEntityType(((BasicCollectionMetadata) fmd).getCollectionRootEntity());
            }
        }

        PersistencePackageRequest ppr = getSectionPersistencePackageRequest(mainClassName, sectionCrumbs, pathVars);
        Entity entity = entityAdminService.getRecord(ppr, id, mainMetadata, false).getDynamicResultSet().getRecords()[0];
        
        // First, we must save the collection entity
        PersistenceResponse persistenceResponse = entityAdminService.addSubCollectionEntity(entityForm, mainMetadata, collectionProperty, entity, sectionCrumbs);
        Entity savedEntity = persistenceResponse.getEntity();
        entityFormValidator.validate(entityForm, savedEntity, result);
        
        if (result.hasErrors()) {
            FieldMetadata md = collectionProperty.getFieldMetadata();
            ppr = PersistencePackageRequest.fromMetadata(md, sectionCrumbs);
            return buildAddCollectionItemModel(request, response, model, id, collectionField, sectionKey, collectionProperty,
                    md, ppr, entityForm, savedEntity);
        }

        // Next, we must get the new list grid that represents this collection
        ListGrid listGrid = getCollectionListGrid(mainMetadata, entity, collectionProperty, null, sectionKey, persistenceResponse, sectionCrumbs);
        model.addAttribute("listGrid", listGrid);

        // We return the new list grid so that it can replace the currently visible one
        setModelAttributes(model, sectionKey);
        return "views/standaloneListGrid";
    }

    /**
     * Builds out all of the model information needed for showing the add modal for collection items on both the initial GET
     * as well as after a POST with validation errors
     * 
     * @param request
     * @param model
     * @param id
     * @param collectionField
     * @param sectionKey
     * @param collectionProperty
     * @param md
     * @param ppr
     * @return the appropriate view to display for the modal
     * @see {@link #addCollectionItem(HttpServletRequest, HttpServletResponse, Model, Map, String, String, EntityForm, BindingResult)}
     * @see {@link #showAddCollectionItem(HttpServletRequest, HttpServletResponse, Model, Map, String, String, MultiValueMap)}
     * @throws ServiceException
     */
    protected String buildAddCollectionItemModel(HttpServletRequest request, HttpServletResponse response,
            Model model, String id, String collectionField, String sectionKey, Property collectionProperty,
            FieldMetadata md, PersistencePackageRequest ppr, EntityForm entityForm, Entity entity) throws ServiceException {
        
        if (entityForm != null) {
            entityForm.clearFieldsMap();
        }
        List<SectionCrumb> sectionCrumbs = getSectionCrumbs(request, sectionKey, id);
        if (md instanceof BasicCollectionMetadata) {
            BasicCollectionMetadata fmd = (BasicCollectionMetadata) md;

            // When adding items to basic collections, we will sometimes show a form to persist a new record
            // and sometimes show a list grid to allow the user to associate an existing record.
            if (fmd.getAddMethodType().equals(AddMethodType.PERSIST)) {
                ClassMetadata collectionMetadata = entityAdminService.getPersistenceResponse(ppr).getDynamicResultSet().getClassMetaData();
                if (entityForm == null) {
                    entityForm = formBuilder.createEntityForm(collectionMetadata,sectionCrumbs);
                    entityForm.setRootEntityClassname(ppr.getRootEntityClassname());
                    entityForm.setEntityType(ppr.getRootEntityClassname());
                } else {
                    formBuilder.populateEntityForm(collectionMetadata, entityForm, sectionCrumbs);
                    formBuilder.populateEntityFormFieldValues(collectionMetadata, entity, entityForm);
                }
                formBuilder.removeNonApplicableFields(collectionMetadata, entityForm, ppr.getRootEntityClassname());
                entityForm.getTabs().iterator().next().getIsVisible();

                model.addAttribute("entityForm", entityForm);
                model.addAttribute("viewType", "modal/simpleAddEntity");
            } else {
                DynamicResultSet drs = entityAdminService.getRecords(ppr).getDynamicResultSet();
                ListGrid listGrid = formBuilder.buildCollectionListGrid(id, drs, collectionProperty, sectionKey, sectionCrumbs);
                listGrid.setPathOverride(request.getRequestURL().toString());

                model.addAttribute("listGrid", listGrid);
                model.addAttribute("viewType", "modal/simpleSelectEntity");
            }
        } else if (md instanceof AdornedTargetCollectionMetadata) {
            AdornedTargetCollectionMetadata fmd = (AdornedTargetCollectionMetadata) md;

            // Even though this field represents an adorned target collection, the list we want to show in the modal
            // is the standard list grid for the target entity of this field
            ppr.setOperationTypesOverride(null);
            ppr.setType(PersistencePackageRequest.Type.STANDARD);

            ClassMetadata collectionMetadata = entityAdminService.getPersistenceResponse(ppr).getDynamicResultSet().getClassMetaData();

            DynamicResultSet drs = entityAdminService.getRecords(ppr).getDynamicResultSet();
            ListGrid listGrid = formBuilder.buildMainListGrid(drs, collectionMetadata, sectionKey, sectionCrumbs);
            listGrid.setSubCollectionFieldName(collectionField);
            listGrid.setPathOverride(request.getRequestURL().toString());
            listGrid.setFriendlyName(collectionMetadata.getPolymorphicEntities().getFriendlyName());
            if (entityForm == null) {
                entityForm = formBuilder.buildAdornedListForm(fmd, ppr.getAdornedList(), id);
            } else {
                formBuilder.buildAdornedListForm(fmd, ppr.getAdornedList(), id, entityForm);
                formBuilder.populateEntityFormFieldValues(collectionMetadata, entity, entityForm);
            }
            
            listGrid.setListGridType(ListGrid.Type.ADORNED);
            for (Entry<String, Field> entry : entityForm.getFields().entrySet()) {
                if (entry.getValue().getIsVisible()) {
                    listGrid.setListGridType(ListGrid.Type.ADORNED_WITH_FORM);
                    break;
                }
            }

            model.addAttribute("listGrid", listGrid);
            model.addAttribute("entityForm", entityForm);
            model.addAttribute("viewType", "modal/adornedSelectEntity");
        } else if (md instanceof MapMetadata) {
            MapMetadata fmd = (MapMetadata) md;
            ClassMetadata collectionMetadata = entityAdminService.getPersistenceResponse(ppr).getDynamicResultSet().getClassMetaData();
            
            if (entityForm == null) {
                entityForm = formBuilder.buildMapForm(fmd, ppr.getMapStructure(), collectionMetadata, id);
            } else {
                formBuilder.buildMapForm(fmd, ppr.getMapStructure(), collectionMetadata, id, entityForm);
                formBuilder.populateEntityFormFieldValues(collectionMetadata, entity, entityForm);
            }
            model.addAttribute("entityForm", entityForm);
            model.addAttribute("viewType", "modal/mapAddEntity");
        }

        model.addAttribute("currentUrl", request.getRequestURL().toString());
        model.addAttribute("modalHeaderType", "addCollectionItem");
        model.addAttribute("collectionProperty", collectionProperty);
        setModelAttributes(model, sectionKey);
        return "modules/modalContainer";
    }

    /**
     * Shows the appropriate modal dialog to edit the selected collection item
     * 
     * @param request
     * @param response
     * @param model
     * @param pathVars
     * @param id
     * @param collectionField
     * @param collectionItemId
     * @return the return view path
     * @throws Exception
     */
    @RequestMapping(value = "/{id}/{collectionField:.*}/{collectionItemId}", method = RequestMethod.GET)
    public String showUpdateCollectionItem(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable  Map<String, String> pathVars,
            @PathVariable(value="id") String id,
            @PathVariable(value="collectionField") String collectionField,
            @PathVariable(value="collectionItemId") String collectionItemId) throws Exception {
        return showViewUpdateCollection(request, model, pathVars, id, collectionField, collectionItemId, 
                "updateCollectionItem");
    }

    /**
     * Shows the appropriate modal dialog to view the selected collection item. This will display the modal as readonly
     *
     * @param request
     * @param response
     * @param model
     * @param pathVars
     * @param id
     * @param collectionField
     * @param collectionItemId
     * @return the return view path
     * @throws Exception
     */
    @RequestMapping(value = "/{id}/{collectionField:.*}/{collectionItemId}/view", method = RequestMethod.GET)
    public String showViewCollectionItem(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable  Map<String, String> pathVars,
            @PathVariable(value="id") String id,
            @PathVariable(value="collectionField") String collectionField,
            @PathVariable(value="collectionItemId") String collectionItemId) throws Exception {
        String returnPath = showViewUpdateCollection(request, model, pathVars, id, collectionField, collectionItemId, 
                "viewCollectionItem");
        
        // Since this is a read-only view, actions don't make sense in this context
        EntityForm ef = (EntityForm) model.asMap().get("entityForm");
        ef.removeAllActions();
        
        return returnPath;
    }
    
    protected String showViewUpdateCollection(HttpServletRequest request, Model model, Map<String, String> pathVars,
            String id, String collectionField, String collectionItemId, String modalHeaderType) throws ServiceException {
        return showViewUpdateCollection(request, model, pathVars, id, collectionField, collectionItemId, modalHeaderType, null, null);
    }

    /**
     * Shows the view and populates the model for updating a collection item. You can also pass in an entityform and entity
     * which are optional. If they are not passed in then they are automatically looked up
     * 
     * @param request
     * @param model
     * @param pathVars
     * @param id
     * @param collectionField
     * @param collectionItemId
     * @param modalHeaderType
     * @param ef
     * @param entity
     * @return
     * @throws ServiceException
     */
    protected String showViewUpdateCollection(HttpServletRequest request, Model model, Map<String, String> pathVars,
            String id, String collectionField, String collectionItemId, String modalHeaderType, EntityForm entityForm, Entity entity) throws ServiceException {
        String sectionKey = getSectionKey(pathVars);
        String mainClassName = getClassNameForSection(sectionKey);
        List<SectionCrumb> sectionCrumbs = getSectionCrumbs(request, sectionKey, id);
        ClassMetadata mainMetadata = entityAdminService.getPersistenceResponse(getSectionPersistencePackageRequest(mainClassName, sectionCrumbs, pathVars)).getDynamicResultSet().getClassMetaData();
        Property collectionProperty = mainMetadata.getPMap().get(collectionField);
        FieldMetadata md = collectionProperty.getFieldMetadata();
        SectionCrumb nextCrumb = new SectionCrumb();
        if (md instanceof MapMetadata) {
            nextCrumb.setSectionIdentifier(((MapMetadata) md).getValueClassName());
        } else {
            nextCrumb.setSectionIdentifier(((CollectionMetadata) md).getCollectionRootEntity());
        }
        nextCrumb.setSectionId(collectionItemId);
        if (!sectionCrumbs.contains(nextCrumb)) {
            sectionCrumbs.add(nextCrumb);
        }

        PersistencePackageRequest ppr = getSectionPersistencePackageRequest(mainClassName, sectionCrumbs, pathVars);
        Entity parentEntity = entityAdminService.getRecord(ppr, id, mainMetadata, false).getDynamicResultSet().getRecords()[0];

        ppr = PersistencePackageRequest.fromMetadata(md, sectionCrumbs);
        
        if (md instanceof BasicCollectionMetadata &&
                ((BasicCollectionMetadata) md).getAddMethodType().equals(AddMethodType.PERSIST)) {
            ClassMetadata collectionMetadata = entityAdminService.getPersistenceResponse(ppr).getDynamicResultSet().getClassMetaData();
            if (entity == null) {
                entity = entityAdminService.getRecord(ppr, collectionItemId, collectionMetadata, true).getDynamicResultSet().getRecords()[0];
            }

            Map<String, DynamicResultSet> subRecordsMap = entityAdminService.getRecordsForAllSubCollections(ppr, entity, sectionCrumbs);
            if (entityForm == null) {
                entityForm = formBuilder.createEntityForm(collectionMetadata, entity, subRecordsMap, sectionCrumbs);
            } else {
                entityForm.clearFieldsMap();
                formBuilder.populateEntityForm(collectionMetadata, entity, subRecordsMap, entityForm, sectionCrumbs);
                //remove all the actions since we're not trying to redisplay them on the form
                entityForm.removeAllActions();
            }
            entityForm.removeAction(DefaultEntityFormActions.DELETE);

            model.addAttribute("entityForm", entityForm);
            model.addAttribute("viewType", "modal/simpleEditEntity");
        } else if (md instanceof AdornedTargetCollectionMetadata) {
            AdornedTargetCollectionMetadata fmd = (AdornedTargetCollectionMetadata) md;

            if (entity == null) {
                entity = entityAdminService.getAdvancedCollectionRecord(mainMetadata, parentEntity, collectionProperty,
                    collectionItemId, sectionCrumbs).getDynamicResultSet().getRecords()[0];
            }
            
            boolean populateTypeAndId = true;
            if (entityForm == null) {
                entityForm = formBuilder.buildAdornedListForm(fmd, ppr.getAdornedList(), id);
            } else {
                entityForm.clearFieldsMap();
                String entityType = entityForm.getEntityType();
                formBuilder.buildAdornedListForm(fmd, ppr.getAdornedList(), id, entityForm);
                entityForm.setEntityType(entityType);
                populateTypeAndId = false;
            }

            ClassMetadata cmd = entityAdminService.getPersistenceResponse(ppr).getDynamicResultSet().getClassMetaData();
            for (String field : fmd.getMaintainedAdornedTargetFields()) {
                Property p = cmd.getPMap().get(field);
                if (p != null && p.getFieldMetadata() instanceof AdornedTargetCollectionMetadata) {
                    // Because we're dealing with a nested adorned target collection, this particular request must act
                    // directly on the first adorned target collection. Because of this, we need the actual id property
                    // from the entity that models the adorned target relationship, and not the id of the target object.
                    Property alternateIdProperty = entity.getPMap().get(BasicPersistenceModule.ALTERNATE_ID_PROPERTY);
                    DynamicResultSet drs = entityAdminService.getRecordsForCollection(cmd, entity, p, null, null, null,
                            alternateIdProperty.getValue(), sectionCrumbs).getDynamicResultSet();
                    
                    ListGrid listGrid = formBuilder.buildCollectionListGrid(alternateIdProperty.getValue(), drs, p,
                            ppr.getAdornedList().getAdornedTargetEntityClassname(), sectionCrumbs);
                    listGrid.setListGridType(ListGrid.Type.INLINE);
                    listGrid.getToolbarActions().add(DefaultListGridActions.ADD);
                    entityForm.addListGrid(listGrid, EntityForm.DEFAULT_TAB_NAME, EntityForm.DEFAULT_TAB_ORDER);
                } else if (p != null && p.getFieldMetadata() instanceof MapMetadata) {
                    // See above comment for AdornedTargetCollectionMetadata
                    MapMetadata mmd = (MapMetadata) p.getFieldMetadata();

                    Property alternateIdProperty = entity.getPMap().get(BasicPersistenceModule.ALTERNATE_ID_PROPERTY);
                    DynamicResultSet drs = entityAdminService.getRecordsForCollection(cmd, entity, p, null, null, null,
                            alternateIdProperty.getValue(), sectionCrumbs).getDynamicResultSet();

                    ListGrid listGrid = formBuilder.buildCollectionListGrid(alternateIdProperty.getValue(), drs, p,
                            mmd.getTargetClass(), sectionCrumbs);
                    listGrid.setListGridType(ListGrid.Type.INLINE);
                    listGrid.getToolbarActions().add(DefaultListGridActions.ADD);
                    entityForm.addListGrid(listGrid, EntityForm.DEFAULT_TAB_NAME, EntityForm.DEFAULT_TAB_ORDER);
                }
            }
            
            formBuilder.populateEntityFormFields(entityForm, entity, populateTypeAndId, populateTypeAndId);
            formBuilder.populateAdornedEntityFormFields(entityForm, entity, ppr.getAdornedList());
            
            boolean atLeastOneBasicField = false;
            for (Entry<String, Field> entry : entityForm.getFields().entrySet()) {
                if (entry.getValue().getIsVisible()) {
                    atLeastOneBasicField = true;
                    break;
                }
            }
            if (!atLeastOneBasicField) {
                entityForm.removeAction(DefaultEntityFormActions.SAVE);
            }

            model.addAttribute("entityForm", entityForm);
            model.addAttribute("viewType", "modal/adornedEditEntity");
        } else if (md instanceof MapMetadata) {
            MapMetadata fmd = (MapMetadata) md;

            ClassMetadata collectionMetadata = entityAdminService.getPersistenceResponse(ppr).getDynamicResultSet().getClassMetaData();
            if (entity == null) {
                entity = entityAdminService.getAdvancedCollectionRecord(mainMetadata, parentEntity, collectionProperty,
                    collectionItemId, sectionCrumbs).getEntity();
            }
            
            boolean populateTypeAndId = true;
            if (entityForm == null) {
                entityForm = formBuilder.buildMapForm(fmd, ppr.getMapStructure(), collectionMetadata, id);
            } else {
                //save off the prior key before clearing out the fields map as it will not appear
                //back on the saved entity
                String priorKey = entityForm.getFields().get("priorKey").getValue();
                entityForm.clearFieldsMap();
                formBuilder.buildMapForm(fmd, ppr.getMapStructure(), collectionMetadata, id, entityForm);
                entityForm.getFields().get("priorKey").setValue(priorKey);
                populateTypeAndId = false;
            }

            formBuilder.populateEntityFormFields(entityForm, entity, populateTypeAndId, populateTypeAndId);
            formBuilder.populateMapEntityFormFields(entityForm, entity);

            model.addAttribute("entityForm", entityForm);
            model.addAttribute("viewType", "modal/mapEditEntity");
        }

        model.addAttribute("currentUrl", request.getRequestURL().toString());
        model.addAttribute("modalHeaderType", modalHeaderType);
        model.addAttribute("collectionProperty", collectionProperty);
        setModelAttributes(model, sectionKey);
        return "modules/modalContainer";
    }



    /**
     * Updates the specified collection item
     * 
     * @param request
     * @param response
     * @param model
     * @param pathVars
     * @param id
     * @param collectionField
     * @param entityForm
     * @return the return view path
     * @throws Exception
     */
    @RequestMapping(value = "/{id}/{collectionField:.*}/{collectionItemId}", method = RequestMethod.POST)
    public String updateCollectionItem(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable  Map<String, String> pathVars,
            @PathVariable(value="id") String id,
            @PathVariable(value="collectionField") String collectionField,
            @PathVariable(value="collectionItemId") String collectionItemId,
            @ModelAttribute(value="entityForm") EntityForm entityForm, BindingResult result) throws Exception {
        String sectionKey = getSectionKey(pathVars);
        String mainClassName = getClassNameForSection(sectionKey);
        List<SectionCrumb> sectionCrumbs = getSectionCrumbs(request, sectionKey, id);
        ClassMetadata mainMetadata = entityAdminService.getPersistenceResponse(getSectionPersistencePackageRequest(mainClassName, sectionCrumbs, pathVars)).getDynamicResultSet().getClassMetaData();
        Property collectionProperty = mainMetadata.getPMap().get(collectionField);

        PersistencePackageRequest ppr = getSectionPersistencePackageRequest(mainClassName, sectionCrumbs, pathVars);
        Entity entity = entityAdminService.getRecord(ppr, id, mainMetadata, false).getDynamicResultSet().getRecords()[0];
        
        // First, we must save the collection entity
        PersistenceResponse persistenceResponse = entityAdminService.updateSubCollectionEntity(entityForm, mainMetadata, collectionProperty, entity, collectionItemId, sectionCrumbs);
        Entity savedEntity = persistenceResponse.getEntity();
        entityFormValidator.validate(entityForm, savedEntity, result);

        if (result.hasErrors()) {
            return showViewUpdateCollection(request, model, pathVars, id, collectionField, collectionItemId, 
                    "updateCollectionItem", entityForm, savedEntity); 
        }
        
        // Next, we must get the new list grid that represents this collection
        ListGrid listGrid = getCollectionListGrid(mainMetadata, entity, collectionProperty, null, sectionKey, persistenceResponse, sectionCrumbs);
        model.addAttribute("listGrid", listGrid);

        // We return the new list grid so that it can replace the currently visible one
        setModelAttributes(model, sectionKey);
        return "views/standaloneListGrid";
    }
    
    /**
     * Updates the given colleciton item's sequence. This should only be triggered for adorned target collections
     * where a sort field is specified -- any other invocation is incorrect and will result in an exception.
     * 
     * @param request
     * @param response
     * @param model
     * @param pathVars
     * @param id
     * @param collectionField
     * @param collectionItemId
     * @return an object explaining the state of the operation
     * @throws Exception
     */
    @RequestMapping(value = "/{id}/{collectionField:.*}/{collectionItemId}/sequence", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> updateCollectionItemSequence(HttpServletRequest request, 
            HttpServletResponse response, Model model,
            @PathVariable  Map<String, String> pathVars,
            @PathVariable(value="id") String id,
            @PathVariable(value="collectionField") String collectionField,
            @PathVariable(value="collectionItemId") String collectionItemId,
            @RequestParam(value="newSequence") String newSequence) throws Exception {
        String sectionKey = getSectionKey(pathVars);
        String mainClassName = getClassNameForSection(sectionKey);
        List<SectionCrumb> sectionCrumbs = getSectionCrumbs(request, sectionKey, id);
        ClassMetadata mainMetadata = entityAdminService.getPersistenceResponse(getSectionPersistencePackageRequest(mainClassName, sectionCrumbs, pathVars)).getDynamicResultSet().getClassMetaData();
        Property collectionProperty = mainMetadata.getPMap().get(collectionField);
        FieldMetadata md = collectionProperty.getFieldMetadata();
        
        PersistencePackageRequest ppr = getSectionPersistencePackageRequest(mainClassName, sectionCrumbs, pathVars);
        Entity parentEntity = entityAdminService.getRecord(ppr, id, mainMetadata, false).getDynamicResultSet().getRecords()[0];
        
        ppr = PersistencePackageRequest.fromMetadata(md, sectionCrumbs);
        
        if (md instanceof AdornedTargetCollectionMetadata) {
            AdornedTargetCollectionMetadata fmd = (AdornedTargetCollectionMetadata) md;
            AdornedTargetList atl = ppr.getAdornedList();
            
            // Get an entity form for the entity
            EntityForm entityForm = formBuilder.buildAdornedListForm(fmd, ppr.getAdornedList(), id);
            Entity entity = entityAdminService.getAdvancedCollectionRecord(mainMetadata, parentEntity, collectionProperty, 
                    collectionItemId, sectionCrumbs).getDynamicResultSet().getRecords()[0];
            formBuilder.populateEntityFormFields(entityForm, entity);
            formBuilder.populateAdornedEntityFormFields(entityForm, entity, ppr.getAdornedList());
            
            // Set the new sequence (note that it will come in 0-indexed but the persistence module expects 1-indexed)
            int sequenceValue = Integer.parseInt(newSequence) + 1;
            Field field = entityForm.findField(atl.getSortField());
            field.setValue(String.valueOf(sequenceValue));
            
            Map<String, Object> responseMap = new HashMap<String, Object>();
            entityAdminService.updateSubCollectionEntity(entityForm, mainMetadata, collectionProperty, entity, collectionItemId, sectionCrumbs);
            responseMap.put("status", "ok");
            responseMap.put("field", collectionField);
            return responseMap;
        } else {
            throw new UnsupportedOperationException("Cannot handle sequencing for non adorned target collection fields.");
        }
    }

    /**
     * Removes the requested collection item
     * 
     * Note that the request must contain a parameter called "key" when attempting to remove a collection item from a 
     * map collection.
     * 
     * @param request
     * @param response
     * @param model
     * @param pathVars
     * @param id
     * @param collectionField
     * @param collectionItemId
     * @return the return view path
     * @throws Exception
     */
    @RequestMapping(value = "/{id}/{collectionField:.*}/{collectionItemId}/delete", method = RequestMethod.POST)
    public String removeCollectionItem(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable  Map<String, String> pathVars,
            @PathVariable(value="id") String id,
            @PathVariable(value="collectionField") String collectionField,
            @PathVariable(value="collectionItemId") String collectionItemId) throws Exception {
        String sectionKey = getSectionKey(pathVars);
        String mainClassName = getClassNameForSection(sectionKey);
        List<SectionCrumb> sectionCrumbs = getSectionCrumbs(request, sectionKey, id);
        ClassMetadata mainMetadata = entityAdminService.getPersistenceResponse(getSectionPersistencePackageRequest(mainClassName, sectionCrumbs, pathVars)).getDynamicResultSet().getClassMetaData();
        Property collectionProperty = mainMetadata.getPMap().get(collectionField);

        String priorKey = request.getParameter("key");
        
        PersistencePackageRequest ppr = getSectionPersistencePackageRequest(mainClassName, sectionCrumbs, pathVars);
        Entity entity = entityAdminService.getRecord(ppr, id, mainMetadata, false).getDynamicResultSet().getRecords()[0];

        // First, we must remove the collection entity
        PersistenceResponse persistenceResponse = entityAdminService.removeSubCollectionEntity(mainMetadata, collectionProperty, entity, collectionItemId, priorKey, sectionCrumbs);

        // Next, we must get the new list grid that represents this collection
        ListGrid listGrid = getCollectionListGrid(mainMetadata, entity, collectionProperty, null, sectionKey, persistenceResponse, sectionCrumbs);
        model.addAttribute("listGrid", listGrid);

        // We return the new list grid so that it can replace the currently visible one
        setModelAttributes(model, sectionKey);
        return "views/standaloneListGrid";
    }
    
    // *********************************
    // ADDITIONAL SPRING-BOUND METHODS *
    // *********************************
    
    /**
     * Invoked on every request to provide the ability to register specific binders for Spring's binding process.
     * By default, we register a binder that treats empty Strings as null and a Boolean editor that supports either true
     * or false. If the value is passed in as null, it will treat it as false.
     * 
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(Boolean.class, new NonNullBooleanEditor());
    }
    
}

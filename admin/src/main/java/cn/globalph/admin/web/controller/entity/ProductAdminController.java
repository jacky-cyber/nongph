package cn.globalph.admin.web.controller.entity;

import org.apache.commons.lang3.StringUtils;

import cn.globalph.admin.server.service.handler.ProductCustomPersistenceHandler;
import cn.globalph.b2c.product.domain.Product;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.product.domain.SkuImpl;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.openadmin.dto.ClassMetadata;
import cn.globalph.openadmin.dto.ClassTree;
import cn.globalph.openadmin.dto.DynamicResultSet;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.PersistencePackageRequest;
import cn.globalph.openadmin.dto.Property;
import cn.globalph.openadmin.dto.SectionCrumb;
import cn.globalph.openadmin.web.controller.entity.EntityAdminController;
import cn.globalph.openadmin.web.form.component.ListGrid;
import cn.globalph.openadmin.web.form.component.ListGridAction;
import cn.globalph.openadmin.web.form.entity.DefaultEntityFormActions;
import cn.globalph.openadmin.web.form.entity.EntityForm;
import cn.globalph.openadmin.web.form.entity.Field;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles admin operations for the {@link Product} entity. Editing a product requires custom criteria in order to properly
 * invoke the {@link ProductCustomPersistenceHandler}
 * 
 * @see {@link ProductCustomPersistenceHandler}
 */
@Controller("adminProductController")
@RequestMapping("/" + ProductAdminController.SECTION_KEY)
public class ProductAdminController extends EntityAdminController {
    
    protected static final String SECTION_KEY = "product";
    
    @Override
    protected String getSectionKey(Map<String, String> pathVars) {
        //allow external links to work for ToOne items
        if (super.getSectionKey(pathVars) != null) {
            return super.getSectionKey(pathVars);
           }
        return SECTION_KEY;
     }
    
    @Override
    public String[] getSectionCustomCriteria() {
        return new String[]{"productDirectEdit"};
     }
    
    private String showAddAdditionalSku(HttpServletRequest request, HttpServletResponse response, Model model,
            String id, Map<String, String> pathVars) throws Exception {
        String collectionField = "allSkus";
        String mainClassName = getClassNameForSection(SECTION_KEY);
        List<SectionCrumb> sectionCrumbs = getSectionCrumbs(request, SECTION_KEY, id);
        ClassMetadata mainMetadata = entityAdminService.getPersistenceResponse(getSectionPersistencePackageRequest(mainClassName, sectionCrumbs, pathVars)).getDynamicResultSet().getClassMetaData();
        Property collectionProperty = mainMetadata.getPMap().get(collectionField);
        FieldMetadata md = collectionProperty.getFieldMetadata();
        
        PersistencePackageRequest ppr = PersistencePackageRequest.fromMetadata(md, sectionCrumbs)
                .withCustomCriteria(new String[] { id });
        ClassMetadata cmd = entityAdminService.getPersistenceResponse(ppr).getDynamicResultSet().getClassMetaData();
        // If the entity type isn't specified, we need to determine if there are various polymorphic types
        // for this entity.
        String entityType = null;
        if (request.getParameter("entityType") != null) {
            entityType = request.getParameter("entityType");
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
            setModelAttributes(model, SECTION_KEY);
            return "modules/modalContainer";
        } else {
            ppr = ppr.withRootEntityClassname(entityType);
        }

        ClassMetadata collectionMetadata = entityAdminService.getPersistenceResponse(ppr).getDynamicResultSet().getClassMetaData();
        EntityForm entityForm = formBuilder.createEntityForm(collectionMetadata, sectionCrumbs);
        entityForm.setRootEntityClassname(ppr.getRootEntityClassname());
        entityForm.setEntityType(ppr.getRootEntityClassname());
        formBuilder.removeNonApplicableFields(collectionMetadata, entityForm, ppr.getRootEntityClassname());

        entityForm.removeAction(DefaultEntityFormActions.DELETE);

        removeRequiredValidation(entityForm);
        
        model.addAttribute("entityForm", entityForm);
        model.addAttribute("viewType", "modal/simpleAddEntity");
                
        model.addAttribute("currentUrl", request.getRequestURL().toString());
        model.addAttribute("modalHeaderType", "addCollectionItem");
        model.addAttribute("collectionProperty", collectionProperty);
        setModelAttributes(model, SECTION_KEY);
        return "modules/modalContainer";
    }
    
    @Override
    protected String buildAddCollectionItemModel(HttpServletRequest request, HttpServletResponse response,
            Model model,
            String id,
            String collectionField,
            String sectionKey,
            Property collectionProperty,
            FieldMetadata md, PersistencePackageRequest ppr, EntityForm entityForm, Entity entity) throws ServiceException {
        if ("allSkus".equals(collectionField) && ppr.getCustomCriteria().length == 0) {
            ppr.withCustomCriteria(new String[] { id });
        }
        return super.buildAddCollectionItemModel(request, response, model, id, collectionField, sectionKey, collectionProperty, md, ppr, entityForm, entity);
    }
    
    private String showUpdateAdditionalSku(HttpServletRequest request, HttpServletResponse response, Model model,
            String id, String collectionItemId, Map<String, String> pathVars) throws Exception {
        String collectionField = "allSkus";
        
        // Find out metadata for the allSkus property
        String mainClassName = getClassNameForSection(SECTION_KEY);
        List<SectionCrumb> sectionCrumbs = getSectionCrumbs(request, SECTION_KEY, id);
        ClassMetadata mainMetadata = entityAdminService.getPersistenceResponse(getSectionPersistencePackageRequest(mainClassName, sectionCrumbs, pathVars)).getDynamicResultSet().getClassMetaData();
        Property collectionProperty = mainMetadata.getPMap().get(collectionField);
        FieldMetadata md = collectionProperty.getFieldMetadata();

        // Find the metadata and the entity for the selected sku
        PersistencePackageRequest ppr = PersistencePackageRequest.fromMetadata(md, sectionCrumbs)
                .withCustomCriteria(new String[] { id });
        ClassMetadata collectionMetadata = entityAdminService.getPersistenceResponse(ppr).getDynamicResultSet().getClassMetaData();
        if (collectionMetadata.getClassName().equals(SkuImpl.class.getName())) {
            collectionMetadata.setClassName(Sku.class.getName());
        }
        
        Entity entity = entityAdminService.getRecord(ppr, collectionItemId, collectionMetadata, true).getDynamicResultSet().getRecords()[0];
        
        // Find the records for all subcollections of Sku
        Map<String, DynamicResultSet> subRecordsMap = entityAdminService.getRecordsForAllSubCollections(ppr, entity, sectionCrumbs);
        
        // Build the entity form for the modal that includes the subcollections
        EntityForm entityForm = formBuilder.createEntityForm(collectionMetadata, entity, subRecordsMap, sectionCrumbs);
        
        entityForm.removeAction(DefaultEntityFormActions.DELETE);
        
        // Ensure that operations on the Sku subcollections go to the proper URL
        for (ListGrid lg : entityForm.getAllListGrids()) {
            lg.setSectionKey("cn.globalph.b2c.product.domain.Sku");
            lg.setSectionCrumbs(sectionCrumbs);
        }

        removeRequiredValidation(entityForm);
        
        model.addAttribute("entityForm", entityForm);
        model.addAttribute("viewType", "modal/simpleEditEntity");

        model.addAttribute("currentUrl", request.getRequestURL().toString());
        model.addAttribute("modalHeaderType", "updateCollectionItem");
        model.addAttribute("collectionProperty", collectionProperty);
        setModelAttributes(model, SECTION_KEY);
        return "modules/modalContainer";
    }

    @Override
    @RequestMapping(value = "/{id}/{collectionField:.*}/{collectionItemId}", method = RequestMethod.GET)
    public String showUpdateCollectionItem(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable  Map<String, String> pathVars,
            @PathVariable(value="id") String id,
            @PathVariable(value="collectionField") String collectionField,
            @PathVariable(value="collectionItemId") String collectionItemId) throws Exception {
        if ( "allSkus".equals(collectionField) ) {
            return showUpdateAdditionalSku(request, response, model, id, collectionItemId, pathVars);
        }
        return super.showUpdateCollectionItem(request, response, model, pathVars, id, collectionField, collectionItemId);
     }
    
    @Override
    @RequestMapping(value = "/{id}/{collectionField}/add", method = RequestMethod.GET)
    public String showAddCollectionItem(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable Map<String, String> pathVars,
            @PathVariable(value="id") String id,
            @PathVariable(value="collectionField") String collectionField,
            @RequestParam  MultiValueMap<String, String> requestParams) throws Exception {
        if ( "allSkus".equals(collectionField) ) {
            return showAddAdditionalSku(request, response, model, id, pathVars);
        } 
        return super.showAddCollectionItem(request, response, model, pathVars, id, collectionField, requestParams);
    }
    
    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String viewEntityForm(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable Map<String, String> pathVars,
            @PathVariable(value = "id") String id) throws Exception {
        String view = super.viewEntityForm(request, response, model, pathVars, id);
        
        //Skus have a specific toolbar action to generate Skus based on permutations
        EntityForm form = (EntityForm) model.asMap().get("entityForm");
        ListGridAction generateSkusAction = new ListGridAction(ListGridAction.GEN_SKUS).withDisplayText("Generate_Skus")
                                                                .withIconClass("icon-fighter-jet")
                                                                .withButtonClass("generate-skus")
                                                                .withUrlPostfix("/generate-skus");
        
        ListGrid skusGrid = form.findListGrid("allSkus");
        if (skusGrid != null) {
            skusGrid.addToolbarAction(generateSkusAction);
            skusGrid.setCanFilterAndSort(false);
        }
        
        // When we're dealing with product bundles, we don't want to render the product options and additional skus
        // list grids. Remove them from the form.
        /*
        if (ProductBundle.class.isAssignableFrom(Class.forName(form.getEntityType()))) {
            form.removeListGrid("additionalSkus");
            form.removeListGrid("productOptions");
        }
        */
        
        return view;
    }
    
    /**
     * Clears out any required validation on the fields within an entity form. Used for additional Skus since none of those
     * fields should be required.
     * 
     * @param entityForm
     */
    protected void removeRequiredValidation(EntityForm entityForm) {
        for (Field field : entityForm.getFields().values()) {
            field.setRequired(false);
        }
    }
    
}

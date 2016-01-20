package cn.globalph.openadmin.web.controller.entity;

import org.apache.commons.lang3.StringUtils;

import cn.globalph.openadmin.dto.BasicFieldMetadata;
import cn.globalph.openadmin.dto.ClassMetadata;
import cn.globalph.openadmin.dto.DynamicResultSet;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.FilterAndSortCriteria;
import cn.globalph.openadmin.dto.PersistencePackageRequest;
import cn.globalph.openadmin.dto.Property;
import cn.globalph.openadmin.dto.SectionCrumb;
import cn.globalph.openadmin.web.controller.BasicAdminController;
import cn.globalph.openadmin.web.form.component.ListGrid;
import cn.globalph.openadmin.web.service.SearchFieldResolver;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The operations in this controller are actions that do not necessarily depend on a section key being present.
 * 
 * @author Andre Azzolini (apazzolini)
 */
@Controller("blAdminBasicOperationsController")
public class AdminOperationsController extends BasicAdminController {
    
    @Resource(name = "blSearchFieldResolver")
    protected SearchFieldResolver searchFieldResolver;
    
    /**
     * Shows the modal dialog that is used to select a "to-one" collection item. For example, this could be used to show
     * a list of categories for the ManyToOne field "defaultCategory" in Product.
     * 
     * @param request
     * @param response
     * @param model
     * @param pathVars
     * @param owningClass
     * @param collectionField
     * @return the return view path
     * @throws Exception
     */
    @RequestMapping(value = "/{owningClass:.*}/{collectionField:.*}/select", method = RequestMethod.GET)
    public String showSelectCollectionItem(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable  Map<String, String> pathVars,
            @PathVariable(value = "owningClass") String owningClass,
            @PathVariable(value="collectionField") String collectionField,
            @RequestParam(required = false) String requestingEntityId,
            @RequestParam  MultiValueMap<String, String> requestParams) throws Exception {
        List<SectionCrumb> sectionCrumbs = getSectionCrumbs(request, null, null);
        PersistencePackageRequest ppr = getSectionPersistencePackageRequest(owningClass, requestParams, sectionCrumbs, pathVars);
        ClassMetadata mainMetadata = entityAdminService.getPersistenceResponse(ppr).getDynamicResultSet().getClassMetaData();
        
        // Only get collection property metadata when there is a non-structured content field that I am looking for
        Property collectionProperty = null;
        FieldMetadata md = null;
        if (!collectionField.contains("|")) {
            collectionProperty = mainMetadata.getPMap().get(collectionField);
            md = collectionProperty.getFieldMetadata();
            ppr = PersistencePackageRequest.fromMetadata(md, sectionCrumbs);
        }
        
        ppr.addFilterAndSortCriteria(getCriteria(requestParams));
        ppr.setStartIndex(getStartIndex(requestParams));
        ppr.setMaxIndex(getMaxIndex(requestParams));
        ppr.removeFilterAndSortCriteria("requestingEntityId");
        ppr.addCustomCriteria("requestingEntityId=" + requestingEntityId);
        ppr.addCustomCriteria("owningClass=" + owningClass);
        
        DynamicResultSet drs = entityAdminService.getRecords(ppr).getDynamicResultSet();
        ListGrid listGrid = null;
        // If we're dealing with a lookup from a dynamic field, we need to build the list grid differently
        if (collectionField.contains("|")) {
            listGrid = formBuilder.buildMainListGrid(drs, mainMetadata, "/" + owningClass, sectionCrumbs);
            listGrid.setListGridType(ListGrid.Type.TO_ONE);
            listGrid.setSubCollectionFieldName(collectionField);
            listGrid.setPathOverride("/" + owningClass + "/" + collectionField + "/select");
            md = new BasicFieldMetadata();
            md.setFriendlyName(mainMetadata.getPolymorphicEntities().getFriendlyName());
            collectionProperty = new Property();
            collectionProperty.setFieldMetadata(md);
        } else if (md instanceof BasicFieldMetadata) {
            listGrid = formBuilder.buildCollectionListGrid(null, drs, collectionProperty, owningClass, sectionCrumbs);
        }
        
        model.addAttribute("listGrid", listGrid);
        model.addAttribute("viewType", "modal/simpleSelectEntity");

        model.addAttribute("currentUrl", request.getRequestURL().toString());
        model.addAttribute("modalHeaderType", "selectCollectionItem");
        model.addAttribute("collectionProperty", collectionProperty);
        setModelAttributes(model, owningClass);
        return "modules/modalContainer";
    }

    @RequestMapping(value = "/{owningClass:.*}/{collectionField:.*}/typeahead", method = RequestMethod.GET)
    public @ResponseBody List<Map<String, String>> getTypeaheadResults(HttpServletRequest request, 
            HttpServletResponse response, Model model,
            @PathVariable Map<String, String> pathVars,
            @PathVariable(value = "owningClass") String owningClass,
            @PathVariable(value="collectionField") String collectionField,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String requestingEntityId,
            @RequestParam MultiValueMap<String, String> requestParams) throws Exception {
        List<SectionCrumb> sectionCrumbs = getSectionCrumbs(request, null, null);
        PersistencePackageRequest ppr = getSectionPersistencePackageRequest(owningClass, requestParams, sectionCrumbs, pathVars);
        ClassMetadata mainMetadata = entityAdminService.getPersistenceResponse(ppr).getDynamicResultSet().getClassMetaData();
        Property collectionProperty = mainMetadata.getPMap().get(collectionField);
        FieldMetadata md = collectionProperty.getFieldMetadata();

        ppr = PersistencePackageRequest.fromMetadata(md, sectionCrumbs);
        ppr.addFilterAndSortCriteria(getCriteria(requestParams));
        ppr.setStartIndex(getStartIndex(requestParams));
        ppr.setMaxIndex(getMaxIndex(requestParams));
        ppr.removeFilterAndSortCriteria("query");
        ppr.removeFilterAndSortCriteria("requestingEntityId");
        ppr.addCustomCriteria("requestingEntityId=" + requestingEntityId);
        
        // This list of datums will populate the typeahead suggestions.
        List<Map<String, String>> responses = new ArrayList<Map<String, String>>();
        if (md instanceof BasicFieldMetadata) {
            String searchField = searchFieldResolver.resolveField(((BasicFieldMetadata) md).getForeignKeyClass());
            ppr.addFilterAndSortCriteria(new FilterAndSortCriteria(searchField, query));

            DynamicResultSet drs = entityAdminService.getRecords(ppr).getDynamicResultSet();
            ClassMetadata lookupMetadata = entityAdminService.getPersistenceResponse(ppr).getDynamicResultSet().getClassMetaData();
            for (Entity e : drs.getRecords()) {
                Map<String, String> responseMap = new HashMap<String, String>();
                String idProperty = lookupMetadata.getIdProperty();
                responseMap.put("id", e.findProperty(idProperty).getValue());

                String displayKey = e.findProperty(searchField).getDisplayValue();
                if (StringUtils.isBlank(displayKey)) {
                    displayKey = e.findProperty(searchField).getValue();
                }
                responseMap.put("displayKey", displayKey);

                responses.add(responseMap);
            }
        }

        return responses;
    }

    
}

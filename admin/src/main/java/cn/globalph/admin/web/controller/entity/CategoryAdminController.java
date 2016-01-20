package cn.globalph.admin.web.controller.entity;

import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.b2c.catalog.service.CatalogService;
import cn.globalph.openadmin.web.controller.entity.EntityAdminController;
import cn.globalph.openadmin.web.form.entity.EntityFormAction;

import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * ***********************************************
 * IMPORTANT NOTE:
 * 
 * This controller is purposefully disabled. We will eventually want to bring it back when an AJAX
 * based category tree is implemented, but for now the category tree is disabled. I'm not deleting 
 * this file along with categoryTree.html because they will find their way back in the future.
 * ***********************************************
 * 
 * Handles admin operations for the {@link Category} entity.
 * 
 * @author Andre Azzolini (apazzolini)
 */
//@Controller("blAdminCategoryController")
//@RequestMapping("/" + AdminCategoryController.SECTION_KEY)
public class CategoryAdminController extends EntityAdminController {
    
    protected static final String SECTION_KEY = "category";
    
    @Resource(name = "blCatalogService")
    protected CatalogService catalogService;
    
    @Override
    protected String getSectionKey(Map<String, String> pathVars) {
        //allow external links to work for ToOne items
        if (super.getSectionKey(pathVars) != null) {
            return super.getSectionKey(pathVars);
        }
        return SECTION_KEY;
    }
    
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String viewEntityList(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable  Map<String, String> pathVars,
            @RequestParam  MultiValueMap<String, String> requestParams) throws Exception {
        super.viewEntityList(request, response, model, pathVars, requestParams);
        
        List<Category> parentCategories = catalogService.findAllParentCategories();
        model.addAttribute("parentCategories", parentCategories);
        
        List<EntityFormAction> mainActions = (List<EntityFormAction>) model.asMap().get("mainActions");
        
        mainActions.add(new EntityFormAction("CategoryTreeView")
            .withButtonClass("show-category-tree-view")
            .withDisplayText("Category_Tree_View"));
        
        mainActions.add(new EntityFormAction("CategoryListView")
            .withButtonClass("show-category-list-view active")
            .withDisplayText("Category_List_View"));
        
        model.addAttribute("viewType", "categoryTree");
        return "modules/defaultContainer";
    }
    
}

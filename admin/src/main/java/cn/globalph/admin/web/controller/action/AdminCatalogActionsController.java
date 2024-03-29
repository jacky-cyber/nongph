package cn.globalph.admin.web.controller.action;

import cn.globalph.admin.server.service.AdminCatalogService;
import cn.globalph.admin.web.controller.entity.ProductAdminController;
import cn.globalph.openadmin.web.controller.BasicAdminController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller that responds to custom catalog actions. These would normally be hooked up in customized controllers like 
 * {@link ProductAdminController}
 * 
 * @author Phillip Verheyden (phillipuniverse)
 * @see {@link ProductAdminController}
 */
@Controller("blAdminCatalogActionsController")
public class AdminCatalogActionsController extends BasicAdminController {
    
    @Resource(name = "blAdminCatalogService")
    protected AdminCatalogService adminCatalogService;

    /**
     * Invokes a separate service to generate a list of Skus for a particular {@link Product} and that {@link Product}'s
     * Product Options
     */
    @RequestMapping(value = "product/{productId}/{skusFieldName}/generate-skus",
                    method = RequestMethod.GET,
                    produces = "application/json")
    public @ResponseBody Map<String, Object> generateSkus(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable(value = "productId") Long productId,
            @PathVariable(value = "skusFieldName") String skusFieldName) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        Integer skusGenerated = adminCatalogService.generateSkusFromProduct(productId);
        
        //TODO: Externalize these messages to property files
        if (skusGenerated == 0) {
            result.put("message", "No Skus were generated. It is likely that each product option value permutation " +
            		"already has a Sku associated with it");
        } else if (skusGenerated == -1) {
            result.put("message", "This product has no Product Options configured to generate Skus from");
        } else {
            result.put("message", skusGenerated + " Skus have been generated from the configured product options");
        }
        
        String url = request.getRequestURL().toString();
        url = url.substring(0, url.indexOf("/generate-skus"));
        
        result.put("skusGenerated", skusGenerated);
        result.put("listGridUrl", url);
        return result;
    }
}

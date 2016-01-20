package cn.globalph.b2c.web.controller.catalog;

import cn.globalph.b2c.catalog.service.CatalogService;
import cn.globalph.common.web.controller.BasicController;

import javax.annotation.Resource;

/**
 * An abstract controller that provides convenience methods and resource declarations for its
 * children. Operations that are shared between controllers that deal with customer accounts belong here
 * 
 * @author apazzolini
 */
public abstract class AbstractCatalogController extends BasicController {
    
    @Resource(name = "blCatalogService")
    protected CatalogService catalogService;
    

}

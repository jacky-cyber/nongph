package cn.globalph.b2c.web.controller.cart;

import cn.globalph.b2c.catalog.service.CatalogService;
import cn.globalph.b2c.offer.service.OfferService;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.payment.service.OrderToPaymentRequestDTOService;
import cn.globalph.b2c.web.service.UpdateCartService;
import cn.globalph.common.web.controller.BasicController;

import javax.annotation.Resource;

/**
 * An abstract controller that provides convenience methods and resource declarations for its
 * children. Operations that are shared between controllers that deal with the catalog belong here.
 * 
 */
public abstract class AbstractCartController extends BasicController {
    
    @Resource(name = "blCatalogService")
    protected CatalogService catalogService;
    
    @Resource(name = "blOrderService")
    protected OrderService orderService;
    
    @Resource(name = "blOfferService")
    protected OfferService offerService;

    @Resource(name="blUpdateCartService")
    protected UpdateCartService updateCartService;

    @Resource(name = "blOrderToPaymentRequestDTOService")
    protected OrderToPaymentRequestDTOService dtoTranslationService;

}

package cn.globalph.b2c.web.service;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.service.call.UpdateCartResponse;

/**
 * Provides methods to facilitate order repricing.
 */
public interface UpdateCartService {

    /**
     * Reprices the order by removing all items and recreating the cart calling for a reprice on the new cart.
     *
     * @return
     */
    public UpdateCartResponse copyCartToCurrentContext(Order currentCart);

    /**
     * Validates the cart against the active price list and locale.
     *
     * @param cart
     * @throws IllegalArgumentException
     */
    public void validateCart (Order cart) throws IllegalArgumentException;

    /**
     * Updates the cart (locale, pricing) and performs validation.
     *
     * @param cart
     * @throws IllegalArgumentException
     */
    public void updateAndValidateCart(Order cart);

}

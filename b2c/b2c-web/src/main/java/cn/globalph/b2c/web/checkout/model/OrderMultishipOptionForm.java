package cn.globalph.b2c.web.checkout.model;

import cn.globalph.b2c.order.service.call.OrderMultishipOptionDTO;

import java.io.Serializable;
import java.util.List;

/**
 * This form is used to bind multiship options in a way that doesn't require
 * the actual objects to be instantiated -- we handle that at the controller
 * level.
 * 
 * @see OrderMultishipOptionDTO
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class OrderMultishipOptionForm implements Serializable {

    private static final long serialVersionUID = -5989681894142759293L;
    
    protected List<OrderMultishipOptionDTO> options;

    public List<OrderMultishipOptionDTO> getOptions() {
        return options;
    }

    public void setOptions(List<OrderMultishipOptionDTO> options) {
        this.options = options;
    }
    
}

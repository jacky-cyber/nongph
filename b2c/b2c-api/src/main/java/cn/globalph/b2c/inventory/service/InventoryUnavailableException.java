
package cn.globalph.b2c.inventory.service;


public class InventoryUnavailableException extends Exception {

    private static final long serialVersionUID = 1L;

    protected Long skuId;

    protected Integer quantityRequested;

    protected Integer quantityAvailable;

    public InventoryUnavailableException(String msg) {
        super(msg);
    }
    
    public InventoryUnavailableException(Long skuId, Integer quantityRequested, Integer quantityAvailable) {
        super();
        this.skuId = skuId;
        this.quantityAvailable = quantityAvailable;
        this.quantityRequested = quantityRequested;
    }

    public InventoryUnavailableException(String arg0, Long skuId, Integer quantityRequested, Integer quantityAvailable) {
        super(arg0);
        this.skuId = skuId;
        this.quantityAvailable = quantityAvailable;
        this.quantityRequested = quantityRequested;
    }
    
    public InventoryUnavailableException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public int getQuantityRequested() {
        return quantityRequested;
    }

    public void setQuantityRequested(int quantityRequested) {
        this.quantityRequested = quantityRequested;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

}

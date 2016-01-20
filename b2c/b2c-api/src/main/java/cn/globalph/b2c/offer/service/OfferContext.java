package cn.globalph.b2c.offer.service;

import cn.globalph.common.classloader.release.ThreadLocalManager;

public class OfferContext {
    
    private static final ThreadLocal<OfferContext> OFFERCONTEXT = ThreadLocalManager.createThreadLocal(OfferContext.class);
    
    public static OfferContext getOfferContext() {
        return OFFERCONTEXT.get();
     }
    
    public static void setOfferContext(OfferContext offerContext) {
        OFFERCONTEXT.set(offerContext);
     }

    protected Boolean executePromotionCalculation = true;

    public Boolean getExecutePromotionCalculation() {
        return executePromotionCalculation;
     }

    public void setExecutePromotionCalculation(Boolean executePromotionCalculation) {
        this.executePromotionCalculation = executePromotionCalculation;
     }
}

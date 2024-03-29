package cn.globalph.b2c.product.domain;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * @author felix.wu
 */
public class SkuBundleComparator implements Comparator<SkuBundle> {


    @Override
    public int compare(SkuBundle productBundle, SkuBundle productBundle1) {
        if (productBundle == null && productBundle1 == null) {
            return 0;
        }

        if (productBundle == null) {
            return 1;
        }

        if (productBundle1 == null) {
            return -1;
        }

        int priorityCompare = comparePriorities(productBundle, productBundle1);

        if (priorityCompare == 0) {
            return compareSavings(productBundle, productBundle1);
        } else {
            return priorityCompare;
        }
    }

    private int comparePriorities(SkuBundle productBundle, SkuBundle productBundle1) {
        Integer priority1 = productBundle.getPriority();
        Integer priority2 = productBundle.getPriority();

        if (priority1 == null && priority2 == null) {
            return 0;
        }

        if (priority1 == null) {
            return priority2.compareTo(0) * -1;
        }

        if (priority2 == null) {
            return priority1.compareTo(0);
        }

        return priority1.compareTo(priority2);
    }

    private int compareSavings(SkuBundle productBundle, SkuBundle productBundle1) {
        BigDecimal savings1 = productBundle.getPotentialSavings();
        BigDecimal savings2 = productBundle1.getPotentialSavings();


        if (savings1 == null && savings2 == null) {
            return 0;
        }

        if (savings1 == null) {
            return savings2.compareTo(BigDecimal.ZERO) * -1;
        }

        if (savings2 == null) {
            return savings1.compareTo(BigDecimal.ZERO);
        }

        return savings1.compareTo(savings2);
    }

}

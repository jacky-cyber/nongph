package cn.globalph.common.currency.util;

import cn.globalph.common.money.Money;

import org.codehaus.jackson.map.util.LRUMap;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;


/**
 * Utility methods for common currency operations
 * 
 * @see {@link BroadleafCurrency}
 */
public class BroadleafCurrencyUtils {

    protected static final Map<String,NumberFormat> FORMAT_CACHE = new LRUMap<String, NumberFormat>(20, 100);

    public static final MathContext ROUND_FLOOR_MATH_CONTEXT = new MathContext(0, RoundingMode.FLOOR);

    /**
     * Returns the unit amount (e.g. .01 for US and all other 2 decimal currencies)
     * @param currency
     * @return
     */
    public static Money getUnitAmount(Money difference) {
        Currency currency = Money.currency;
        BigDecimal divisor = new BigDecimal(Math.pow(10, currency.getDefaultFractionDigits()));
        BigDecimal unitAmount = new BigDecimal("1").divide(divisor);

        if (difference.lessThan(BigDecimal.ZERO)) {
            unitAmount = unitAmount.negate();
           }
        return new Money(unitAmount);
    }

    /**
     * Returns the unit amount (e.g. .01 for US and all other 2 decimal currencies)
     * @param currency
     * @return
     */
    public static Money getUnitAmount() {
        Currency currency = Money.currency;
        BigDecimal divisor = new BigDecimal(Math.pow(10, currency.getDefaultFractionDigits()));
        BigDecimal unitAmount = new BigDecimal("1").divide(divisor);
        return new Money(unitAmount);
    }

    /**
     * Returns the remainder amount if the passed in totalAmount was divided by the
     * quantity taking into account the normal unit of the currency (e.g. .01 for US).
     * @param currency
     * @return
     */
    public static int calculateRemainder(Money totalAmount, int quantity) {
        if (totalAmount == null || totalAmount.isZero() || quantity == 0) {
            return 0;
        }

        // Use this to convert to a whole number (e.g. 1.05 becomes 105 in US currency).
        BigDecimal multiplier = new BigDecimal(10).pow(totalAmount.getAmount().scale());
        BigDecimal amount = totalAmount.getAmount().multiply(multiplier);

        BigDecimal remainder = amount.remainder(new BigDecimal(quantity), ROUND_FLOOR_MATH_CONTEXT);
        return remainder.toBigInteger().intValue();
    }

    /**
     * Provides a cached approach for creating NumberFormat instances. More performant
     * than creating a new one each time.
     *
     * @param locale the Locale
     * @param currency the Currency
     * @return either a new NumberFormat instance, or one taken from the cache
     */
    public static NumberFormat getNumberFormatFromCache() {
        String key = Locale.CHINA.toString() + Money.currency.getCurrencyCode();
        if (!FORMAT_CACHE.containsKey(key)) {
            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CHINA);
            format.setCurrency( Money.currency );
            FORMAT_CACHE.put(key, format);
        }
        return FORMAT_CACHE.get(key);
    }
}

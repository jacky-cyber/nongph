package cn.globalph.common.money;

import java.util.Currency;

public interface CurrencyConversionService {
    
    /**
     * Converts the given Money into the destination. The starting currency is determined by {@code source.getCurrency()}
     * 
     * @param source - the Money to convert
     * @param destinationCurrency - which Currency to convert to
     * @param destinationScale - the scale that the result will be in. If zero, this defaults to the scale of <b>source</b> 
     * and if that is zero, defaults to {@code BankersRounding.DEFAULT_SCALE}
     * @return a new Money in <b>destinationCurrency</b>. If the source and destination are the same currency, the original
     * source is returned unchanged
     */
    public Money convertCurrency(Money source, Currency destinationCurrency, int destinationScale);
    
}

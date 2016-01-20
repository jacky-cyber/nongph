package cn.globalph.openadmin.server.service.persistence.module;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * 
 * @author Jeff Fischer
 * @see {@link BasicPersistenceModule}
 */
public interface DataFormatProvider {

    public SimpleDateFormat getSimpleDateFormatter();

    public DecimalFormat getDecimalFormatter();
    
    /**
     * Formats a aw value from an entity into its string representation used by the system. For instance, this might use
     * the {@link #getDecimalFormatter()} to ensure that BigDecimals only show 2 decimal places or dates are formatted
     * a certain way.
     * 
     * @param value
     * @return
     */
    public String formatValue(Object value);

}

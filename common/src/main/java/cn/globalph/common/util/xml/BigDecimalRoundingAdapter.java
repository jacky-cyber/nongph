package cn.globalph.common.util.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *  This class allows us to round Big Decimals to 2 decimal places, generally for
 *  marshalling purposes.  This is to be used with <code>javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters</code>.
 */
public class BigDecimalRoundingAdapter extends XmlAdapter<String, BigDecimal> {

    @Override
    public BigDecimal unmarshal(String s) throws Exception {
        return new BigDecimal(s);
    }

    @Override
    public String marshal(BigDecimal bigDecimal) throws Exception {
        return bigDecimal.setScale(2, RoundingMode.UP).toString();
    }
}

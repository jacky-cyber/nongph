package cn.globalph.common.util.xml;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ISO8601DateAdapter extends XmlAdapter<String, Date> {

    protected SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    @Override
    public String marshal(Date arg0) throws Exception {
        SimpleDateFormat fmt = (SimpleDateFormat) isoFormat.clone();
        return fmt.format(arg0);
    }

    @Override
    public Date unmarshal(String arg0) throws Exception {
        SimpleDateFormat fmt = (SimpleDateFormat) isoFormat.clone();
        return fmt.parse(arg0);
    }

}

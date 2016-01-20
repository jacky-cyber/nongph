package cn.globalph.b2c.offer.domain;

import java.io.Serializable;
import java.util.Map;

public interface OfferInfo extends Serializable{

    public Long getId();

    public void setId(Long id);

    public Map<String, String> getFieldValues();

    public void setFieldValues(Map<String, String> fieldValues);

}

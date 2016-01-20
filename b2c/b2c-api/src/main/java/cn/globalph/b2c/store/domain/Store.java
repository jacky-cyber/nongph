package cn.globalph.b2c.store.domain;

import java.io.Serializable;

public interface Store extends Serializable{

    public Long getId();
    public void setId(Long id);

    public String getName();
    public void setName(String name);

    public String getAddress1();
    public void setAddress1(String address1);

    public String getAddress2();
    public void setAddress2(String address2);

    public String getCity();
    public void setCity(String city);

    public String getZip();
    public void setZip(String zip);

    public String getCountry();
    public void setCountry(String country);

    public String getPhone();
    public void setPhone(String phone);

    public Double getLongitude();
    public void setLongitude(Double longitude);

    public Double getLatitude();
    public void setLatitude(Double latitude);

    public void setState(String state);
    public String getState();

}

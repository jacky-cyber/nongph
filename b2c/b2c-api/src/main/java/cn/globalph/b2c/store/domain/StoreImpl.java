package cn.globalph.b2c.store.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;
import cn.globalph.common.presentation.RequiredOverride;
import cn.globalph.common.presentation.client.VisibilityEnum;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "NPH_STORE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blStandardElements")
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "StoreImpl_baseStore")
@Inheritance(strategy = InheritanceType.JOINED)
public class StoreImpl implements Store {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator= "StoreId")
    @GenericGenerator(
            name="StoreId",
            strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
            parameters = {
                    @Parameter(name="segment_value", value="StoreImpl"),
                    @Parameter(name="entity_name", value="cn.globalph.b2c.store.domain.StoreImpl")
            }
    )
    @Column(name = "STORE_ID", nullable = false)
    @AdminPresentation(friendlyName = "StoreImpl_Store_ID", visibility = VisibilityEnum.HIDDEN_ALL)
    private Long id;

    @Column(name = "STORE_NAME", nullable = false)
    @AdminPresentation(friendlyName = "StoreImpl_Store_Name", order = Presentation.FieldOrder.NAME,
            group = Presentation.Group.Name.General, groupOrder = Presentation.Group.Order.General,
            prominent = true, gridOrder = 1, columnWidth = "200px",
            requiredOverride = RequiredOverride.REQUIRED)
    private String name;


    @Column(name = "ADDRESS_1")
    @AdminPresentation(friendlyName = "StoreImpl_address1", order = Presentation.FieldOrder.ADDRESS_1,
            group = Presentation.Group.Name.Location, groupOrder = Presentation.Group.Order.Location,
            gridOrder = 2, columnWidth = "200px")
    private String address1;

    @Column(name = "ADDRESS_2")
    @AdminPresentation(friendlyName = "StoreImpl_address2", order = Presentation.FieldOrder.ADDRESS_2,
            group = Presentation.Group.Name.Location, groupOrder = Presentation.Group.Order.Location,
            gridOrder = 3, columnWidth = "200px")
    private String address2;

    @Column(name = "STORE_CITY")
    @AdminPresentation(friendlyName = "StoreImpl_city", order = Presentation.FieldOrder.CITY,
            group = Presentation.Group.Name.Location, groupOrder = Presentation.Group.Order.Location,
            prominent = true, gridOrder = 4)
    private String city;

    @Column(name = "STORE_STATE")
    @AdminPresentation(friendlyName = "StoreImpl_State", order = Presentation.FieldOrder.STATE,
            group = Presentation.Group.Name.Location, groupOrder = Presentation.Group.Order.Location,
            prominent = true, gridOrder = 5)
    private String state;

    @Column(name = "STORE_ZIP")
    @AdminPresentation(friendlyName = "StoreImpl_Zip", order = Presentation.FieldOrder.ZIP,
            group = Presentation.Group.Name.Location, groupOrder = Presentation.Group.Order.Location,
            prominent = true, gridOrder = 6)
    private String zip;

    @Column(name = "STORE_COUNTRY")
    @AdminPresentation(friendlyName = "StoreImpl_Country", order = Presentation.FieldOrder.COUNTRY,
            group = Presentation.Group.Name.Location, groupOrder = Presentation.Group.Order.Location,
            gridOrder = 7, columnWidth = "200px")
    private String country;

    @Column(name = "STORE_PHONE")
    @AdminPresentation(friendlyName = "StoreImpl_Phone", order = Presentation.FieldOrder.PHONE,
            group = Presentation.Group.Name.Location, groupOrder = Presentation.Group.Order.Location,
            gridOrder = 8, columnWidth = "200px")
    private String phone;

    @Column(name = "LATITUDE")
    @AdminPresentation(friendlyName = "StoreImpl_lat", order = Presentation.FieldOrder.LATITUDE,
            tab = Presentation.Tab.Name.Advanced, tabOrder = Presentation.Tab.Order.Advanced,
            group = Presentation.Group.Name.Geocoding, groupOrder = Presentation.Group.Order.Geocoding,
            gridOrder = 9, columnWidth = "200px")
    private Double latitude;

    @Column(name = "LONGITUDE")
    @AdminPresentation(friendlyName = "StoreImpl_lng", order = Presentation.FieldOrder.LONGITUDE,
            tab = Presentation.Tab.Name.Advanced, tabOrder = Presentation.Tab.Order.Advanced,
            group = Presentation.Group.Name.Geocoding, groupOrder = Presentation.Group.Order.Geocoding,
            gridOrder = 10, columnWidth = "200px")
    private Double longitude;

    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#getId()
     */
    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#getId()
     */
    public Long getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#setId(java.lang.Long)
     */
    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#setId(java.lang.Long)
     */
    public void setId(Long id) {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#getName()
     */
    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#getName()
     */
    public String getName() {
        return name;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#setName(java.lang.String)
     */
    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#setName(java.lang.String)
     */
    public void setName(String name) {
        this.name = name;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#getAddress1()
     */
    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#getAddress1()
     */
    public String getAddress1() {
        return address1;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#setAddress1(java.lang.String)
     */
    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#setAddress1(java.lang.String)
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#getAddress2()
     */
    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#getAddress2()
     */
    public String getAddress2() {
        return address2;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#setAddress2(java.lang.String)
     */
    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#setAddress2(java.lang.String)
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#getCity()
     */
    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#getCity()
     */
    public String getCity() {
        return city;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#setCity(java.lang.String)
     */
    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#setCity(java.lang.String)
     */
    public void setCity(String city) {
        this.city = city;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#getZip()
     */
    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#getZip()
     */
    public String getZip() {
        return zip;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#setZip(java.lang.String)
     */
    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#setZip(java.lang.String)
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#getCountry()
     */
    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#getCountry()
     */
    public String getCountry() {
        return country;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#setCountry(java.lang.String)
     */
    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#setCountry(java.lang.String)
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#getPhone()
     */
    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#getPhone()
     */
    public String getPhone() {
        return phone;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#setPhone(java.lang.String)
     */
    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#setPhone(java.lang.String)
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#getLongitude()
     */
    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#getLongitude()
     */
    public Double getLongitude() {
        return longitude;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#setLongitude(java.lang.Float)
     */
    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#setLongitude(java.lang.Float)
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#getLatitude()
     */
    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#getLatitude()
     */
    public Double getLatitude() {
        return latitude;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#setLatitude(java.lang.Float)
     */
    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#setLatitude(java.lang.Float)
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#setState(java.lang.String)
     */
    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#setState(java.lang.String)
     */
    public void setState(String state) {
        this.state = state;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#getState()
     */
    /* (non-Javadoc)
     * @see cn.globalph.b2c.store.domain.Store#getState()
     */
    public String getState() {
        return state;
    }

    public static class Presentation {

        public static class Tab {
            public static class Name {
                public static final String Advanced = "StoreImpl_Advanced_Tab";

            }

            public static class Order {
                public static final int Advanced = 7000;
            }
        }

        public static class Group {
            public static class Name {
                public static final String General = "StoreImpl_Store_General";
                public static final String Location = "StoreImpl_Store_Location";
                public static final String Geocoding = "StoreImpl_Store_Geocoding";
            }

            public static class Order {
                public static final int General = 1000;
                public static final int Location = 2000;
                public static final int Geocoding = 3000;
            }
        }

        public static class FieldOrder {
            public static final int NAME = 1000;
            public static final int ADDRESS_1 = 2000;
            public static final int ADDRESS_2 = 3000;
            public static final int CITY = 4000;
            public static final int STATE = 5000;
            public static final int ZIP = 6000;
            public static final int COUNTRY = 7000;
            public static final int PHONE = 8000;
            public static final int LATITUDE = 9000;
            public static final int LONGITUDE = 10000;
        }
    }

}

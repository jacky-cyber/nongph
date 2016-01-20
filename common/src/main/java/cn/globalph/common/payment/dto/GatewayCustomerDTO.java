package cn.globalph.common.payment.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * @author felix.wu
 */
public class GatewayCustomerDTO<T> {

    protected T parent;

    protected Map<String, Object> additionalFields;
    protected String customerId;
    protected String name;
    protected String companyName;
    protected String phone;
    protected String mobile;
    protected String fax;
    protected String website;
    protected String email;

    public GatewayCustomerDTO() {
        this.additionalFields = new HashMap<String, Object>();
    }

    public GatewayCustomerDTO(T parent) {
        this.additionalFields = new HashMap<String, Object>();
        this.parent = parent;
    }

    public T done() {
        return parent;
    }

    public GatewayCustomerDTO<T> additionalFields(String key, Object value) {
        additionalFields.put(key, value);
        return this;
    }

    public GatewayCustomerDTO<T> customerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public GatewayCustomerDTO<T> name(String name) {
        this.name = name;
        return this;
    }

    public GatewayCustomerDTO<T> companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public GatewayCustomerDTO<T> phone(String phone) {
        this.phone = phone;
        return this;
    }

    public GatewayCustomerDTO<T> mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public GatewayCustomerDTO<T> fax(String fax) {
        this.fax = fax;
        return this;
    }

    public GatewayCustomerDTO<T> website(String website) {
        this.website = website;
        return this;
    }

    public GatewayCustomerDTO<T> email(String email) {
        this.email = email;
        return this;
    }

    public Map<String, Object> getAdditionalFields() {
        return additionalFields;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getPhone() {
        return phone;
    }

    public String getMobile() {
        return mobile;
    }

    public String getFax() {
        return fax;
    }

    public String getWebsite() {
        return website;
    }

    public String getEmail() {
        return email;
    }
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean customerPopulated() {
        return ((getAdditionalFields() != null && !getAdditionalFields().isEmpty()) ||
                getCustomerId() != null ||
                getName() != null ||
                getCompanyName() != null ||
                getPhone() != null ||
                getMobile() != null ||
                getFax() != null ||
                getWebsite() != null ||
                getEmail() != null);
    }
}

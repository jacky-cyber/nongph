package cn.globalph.passport.domain;

import cn.globalph.common.audit.Auditable;
import cn.globalph.common.audit.AuditableListener;
import org.apache.commons.lang3.StringUtils;

import cn.globalph.common.admin.domain.AdminMainEntity;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.persistence.PreviewStatus;
import cn.globalph.common.persistence.Previewable;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.AdminPresentationCollection;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;
import cn.globalph.common.presentation.client.AddMethodType;
import cn.globalph.common.presentation.client.VisibilityEnum;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@EntityListeners(value = { AuditableListener.class, CustomerPersistedEntityListener.class })
//@EntityListeners(CustomerPersistedEntityListener.class)
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "NPH_CUSTOMER")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "blCustomerElements")
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "CustomerImpl_baseCustomer")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.PREVIEW, skipOverlaps=true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE)
})
public class CustomerImpl implements Customer, AdminMainEntity, Previewable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CUSTOMER_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @AdminPresentation(friendlyName = "CustomerImpl_Customer_Id", group = "CustomerImpl_Primary_Key",
            visibility = VisibilityEnum.HIDDEN_ALL)
    protected Long id;
    
    /**
     * 组件、嵌入
    @Embedded
    protected Auditable auditable = new Auditable();
	*/

    @Embedded
    protected Auditable auditable = new Auditable();
    
    @Embedded
    protected PreviewStatus previewable = new PreviewStatus();
    
    @Column(name = "NAME")
    @AdminPresentation(friendlyName = "CustomerImpl_First_Name", order = 2, group = "CustomerImpl_Customer",
            prominent = true, gridOrder = 2)
    protected String name;
    
    @Column(name = "LOGIN_NAME")
    @AdminPresentation(friendlyName = "CustomerImpl_UserName", order = 4000, group = "CustomerImpl_Customer",
            prominent = true, gridOrder = 1)
    protected String loginName;

    @Column(name = "PASSWORD")
    @AdminPresentation(excluded = true)
    protected String password;

    @Column(name = "EMAIL_ADDRESS")
    @Index(name = "CUSTOMER_EMAIL_INDEX", columnNames = { "EMAIL_ADDRESS" })
    @AdminPresentation(friendlyName = "CustomerImpl_Email_Address", order = 1000, group = "CustomerImpl_Customer", gridOrder = 1000)
    protected String emailAddress;

    @Column(name = "PHONE")
    @AdminPresentation(friendlyName = "CustomerImpl_Phone", order = 1, group = "CustomerImpl_Customer",
            prominent = false, gridOrder = 3)
    protected String phone;
    
    @Column(name = "VALIDATION_CODE")
    @AdminPresentation(friendlyName = "短信验证码", order = 1000, group = "CustomerImpl_Customer",
            prominent = false, gridOrder = 1000)
    protected String validationCode;
    
    @Column(name = "EMAIL_TOKEN")
    @AdminPresentation(friendlyName = "邮箱验证口令", order = 1000, group = "CustomerImpl_Customer",
            prominent = false, gridOrder = 1000)
    protected String emailToken;
    
    @Column(name = "VALIDATION_STATUS")
    @AdminPresentation(friendlyName = "认证状态", order = 1000, group = "CustomerImpl_Customer",
            prominent = false, gridOrder = 1000)
    protected Integer validationStatus = 0;

    @ManyToOne(targetEntity = ChallengeQuestionImpl.class)
    @JoinColumn(name = "CHALLENGE_QUESTION_ID")
    @Index(name="CUSTOMER_CHALLENGE_INDEX", columnNames={"CHALLENGE_QUESTION_ID"})
    @AdminPresentation(friendlyName = "CustomerImpl_Challenge_Question", order = 4000,
            tab = Presentation.Tab.Name.Advanced, tabOrder = Presentation.Tab.Order.Advanced,
            excluded = true)
    protected ChallengeQuestion challengeQuestion;

    @Column(name = "CHALLENGE_ANSWER")
    @AdminPresentation(friendlyName = "CustomerImpl_Challenge_Answer", order = 5000,
            tab = Presentation.Tab.Name.Advanced, tabOrder = Presentation.Tab.Order.Advanced,
            excluded = true)
    protected String challengeAnswer;

    @Column(name = "PASSWORD_CHANGE_REQUIRED")
    @AdminPresentation(excluded = true)
    protected Boolean passwordChangeRequired = false;

    @Column(name = "RECEIVE_EMAIL")
    @AdminPresentation(friendlyName = "CustomerImpl_Customer_Receive_Email",order=1000, 
            tab = Presentation.Tab.Name.Advanced, tabOrder = Presentation.Tab.Order.Advanced)
    protected Boolean receiveEmail = true;

    @Column(name = "IS_REGISTERED")
    @AdminPresentation(friendlyName = "CustomerImpl_Customer_Registered", order = 4000,
            prominent = true, gridOrder = 4000)
    protected Boolean registered = false;
    
    @Column(name = "DEACTIVATED")
    @AdminPresentation(friendlyName = "CustomerImpl_Customer_Deactivated", order=3000,
        tab = Presentation.Tab.Name.Advanced, tabOrder = Presentation.Tab.Order.Advanced)
    protected Boolean deactivated = false;
    
    @OneToMany(mappedBy = "customer", targetEntity = AddressImpl.class, cascade = {CascadeType.ALL}, orphanRemoval=true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blStandardElements")
    @AdminPresentationCollection(friendlyName = "CustomerImpl_Customer_Addresses", order = 1000,
            addType = AddMethodType.PERSIST,
            tab = Presentation.Tab.Name.Contact, tabOrder = Presentation.Tab.Order.Contact)
    protected List<Address> consigneeAddresses = new ArrayList<Address>();
    
    @OneToMany(mappedBy = "customer", targetEntity = CustomerMessageImpl.class, cascade = {CascadeType.ALL}, orphanRemoval=true)
    @AdminPresentationCollection(friendlyName = "用户消息", order = 1000,
            addType = AddMethodType.PERSIST,
            tab = "用户消息")
    protected List<CustomerMessage> customerMessages = new ArrayList<CustomerMessage>();

//    @OneToMany(mappedBy = "customer", targetEntity = CustomerPaymentImpl.class, cascade = {CascadeType.ALL}, orphanRemoval=true)
//    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blStandardElements")
//    @BatchSize(size = 50)
//    @AdminPresentationCollection(excluded = true)
//    protected List<CustomerPayment> customerPayments  = new ArrayList<CustomerPayment>();

    @Column(name = "BONUS_POINT")
    @AdminPresentation(friendlyName = "消费积分", order = 4000, group = "CustomerImpl_Customer",
    prominent = true)
    protected Integer bonusPoint = 0;
    
    @Column(name = "USED_BONUS_POINT")
    @AdminPresentation(friendlyName = "已使用积分", order = 5000, group = "CustomerImpl_Customer",
    prominent = true)
    protected Integer usedBonusPoint = 0;
    
    @Transient
    protected String unencodedPassword;

    @Transient
    protected String unencodedChallengeAnswer;
    
    @Transient
    protected boolean anonymous;

    @Transient
    protected boolean cookied;

    @Transient
    protected boolean loggedIn;
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Auditable getAuditable() {
        return auditable;
    }

    @Override
    public void setAuditable(Auditable auditable) {
        this.auditable = auditable;
    }

    @Override
    public String getLoginName() {
        return loginName;
    }

    @Override
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean isPasswordChangeRequired() {
        if (passwordChangeRequired == null) {
            return false;
        } else {
            return passwordChangeRequired.booleanValue();
        }
    }

    @Override
    public void setPasswordChangeRequired(boolean passwordChangeRequired) {
        this.passwordChangeRequired = Boolean.valueOf(passwordChangeRequired);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public ChallengeQuestion getChallengeQuestion() {
        return challengeQuestion;
    }

    @Override
    public void setChallengeQuestion(ChallengeQuestion challengeQuestion) {
        this.challengeQuestion = challengeQuestion;
    }

    @Override
    public String getChallengeAnswer() {
        return challengeAnswer;
    }

    @Override
    public void setChallengeAnswer(String challengeAnswer) {
        this.challengeAnswer = challengeAnswer;
    }

    @Override
    public String getUnencodedPassword() {
        return unencodedPassword;
    }

    @Override
    public void setUnencodedPassword(String unencodedPassword) {
        this.unencodedPassword = unencodedPassword;
    }

    @Override
    public boolean isReceiveEmail() {
        if (receiveEmail == null) {
            return false;
        } else {
            return receiveEmail.booleanValue();
        }
    }

    @Override
    public void setReceiveEmail(boolean receiveEmail) {
        this.receiveEmail = Boolean.valueOf(receiveEmail);
    }

    @Override
    public boolean isRegistered() {
        if (registered == null) {
            return true;
        } else {
            return registered.booleanValue();
        }
    }

    @Override
    public void setRegistered(boolean registered) {
        this.registered = Boolean.valueOf(registered);
    }

    @Override
    public String getUnencodedChallengeAnswer() {
        return unencodedChallengeAnswer;
    }

    @Override
    public void setUnencodedChallengeAnswer(String unencodedChallengeAnswer) {
        this.unencodedChallengeAnswer = unencodedChallengeAnswer;
    }
    
    /*
    @Override
    public Auditable getAuditable() {
        return auditable;
    }

    @Override
    public void setAuditable(Auditable auditable) {
        this.auditable = auditable;
    }
    */
    
    @Override
    public boolean isAnonymous() {
        return anonymous;
    }
    
    public boolean isPersisted(){
    	return id!=null;
    }
    
    @Override
    public boolean isCookied() {
        return cookied;
    }

    @Override
    public boolean isLoggedIn() {
        return loggedIn;
    }

    @Override
    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
        if (anonymous) {
            cookied = false;
            loggedIn = false;
        }
    }

    @Override
    public void setCookied(boolean cookied) {
        this.cookied = cookied;
        if (cookied) {
            anonymous = false;
            loggedIn = false;
        }
    }

    @Override
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
        if (loggedIn) {
            anonymous = false;
            cookied = false;
        }
    }

    @Override
    public boolean isDeactivated() {
        if (deactivated == null) {
            return false;
        } else {
            return deactivated.booleanValue();
        }
    }
    
    @Override
    public void setDeactivated(boolean deactivated) {
        this.deactivated = Boolean.valueOf(deactivated);
    }

    public List<Address> getConsigneeAddresses() {
		return consigneeAddresses;
	}

	public void setConsigneeAddresses(List<Address> consigneeAddresses) {
		this.consigneeAddresses = consigneeAddresses;
	}

	@Override
    public String getPhone() {
        return phone;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getValidationCode() {
		return validationCode;
	}

	public void setValidationCode(String validationCode) {
		this.validationCode = validationCode;
	}

	public String getEmailToken() {
		return emailToken;
	}

	public void setEmailToken(String emailToken) {
		this.emailToken = emailToken;
	}

	public Integer getValidationStatus() {
		return validationStatus;
	}

	public void setValidationStatus(Integer validationStatus) {
		this.validationStatus = validationStatus;
	}

//	@Override
//    public List<CustomerPayment> getCustomerPayments() {
//        return customerPayments;
//    }

	@Override
	public String getMaskName(){
		if(StringUtils.isNotEmpty(name)){
			return name.charAt(0) + "***" + name.charAt(name.length() - 1);
		}else{
			return "***";
		}
	}
	
//    @Override
//    public void setCustomerPayments(List<CustomerPayment> customerPayments) {
//        this.customerPayments = customerPayments;
//    }

    @Override
    public String getMainEntityName() {
        if ( !StringUtils.isEmpty(getName()) ) {
            return getName();
        }
        return String.valueOf(getId());
    }

    @Override
    public Boolean getPreview() {
        if (previewable == null) {
            previewable = new PreviewStatus();
        }
        return previewable.getPreview();
    }

    @Override
    public void setPreview(Boolean preview) {
        if (previewable == null) {
            previewable = new PreviewStatus();
        }
        previewable.setPreview(preview);
    }
    
    @Override
	public void setBonusPoint(Integer bonusPoint) {
		this.bonusPoint = bonusPoint;
	}

	@Override
	public Integer getBonusPoint() {
		return this.bonusPoint;
	}
	
	@Override
	public void setUsedBonusPoint(Integer usedBonusPoint) {
		this.usedBonusPoint = usedBonusPoint;
	}

	@Override
	public Integer getUsedBonusPoint() {
		return this.usedBonusPoint;
	}
	
    @Override
	public List<CustomerMessage> getCustomerMessages() {
		return this.customerMessages;
	}

	@Override
	public void setCustomerMessages(List<CustomerMessage> customerMessages) {
		this.customerMessages = customerMessages;
	}

	@Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CustomerImpl other = (CustomerImpl) obj;

        if (id != null && other.id != null) {
            return id.equals(other.id);
        }

        if (loginName == null) {
            if (other.loginName != null) {
                return false;
            }
        } else if (!loginName.equals(other.loginName)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((loginName == null) ? 0 : loginName.hashCode());
        return result;
    }

    public static class Presentation {

        public static class Tab {

            public static class Name {

                public static final String Contact = "CustomerImpl_Contact_Tab";
                public static final String Advanced = "CustomerImpl_Advanced_Tab";
            }

            public static class Order {

                public static final int Contact = 2000;
                public static final int Advanced = 3000;
            }
        }
    }
}

package cn.globalph.common.email.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @felix.wu
 *
 */
@Entity
@Table(name = "NPH_EMAIL_TRACKING_CLICKS")
public class EmailTrackingClicksImpl implements EmailTrackingClicks {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "ClickId")
    @GenericGenerator(
        name="ClickId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="EmailTrackingClicksImpl"),
            @Parameter(name="entity_name", value="cn.globalph.common.email.domain.EmailTrackingClicksImpl")
        }
    )
    @Column(name = "CLICK_ID")
    protected Long id;

    @ManyToOne(optional=false, targetEntity = EmailTrackingImpl.class)
    @JoinColumn(name = "EMAIL_TRACKING_ID")
    @Index(name="TRACKINGCLICKS_TRACKING_INDEX", columnNames={"EMAIL_TRACKING_ID"})
    protected EmailTracking emailTracking;

    @Column(nullable=false, name = "DATE_CLICKED")
    protected Date dateClicked;

    @Column(name = "CUSTOMER_ID")
    @Index(name="TRACKINGCLICKS_CUSTOMER_INDEX", columnNames={"CUSTOMER_ID"})
    protected String customerId;

    @Column(name = "DESTINATION_URI")
    protected String destinationUri;

    @Column(name = "QUERY_STRING")
    protected String queryString;

    /* (non-Javadoc)
     * @see cn.globalph.common.email.domain.EmailTrackingClicks#getId()
     */
    @Override
    public Long getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see cn.globalph.common.email.domain.EmailTrackingClicks#setId(java.lang.Long)
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see cn.globalph.common.email.domain.EmailTrackingClicks#getDateClicked()
     */
    @Override
    public Date getDateClicked() {
        return dateClicked;
    }

    /* (non-Javadoc)
     * @see cn.globalph.common.email.domain.EmailTrackingClicks#setDateClicked(java.util.Date)
     */
    @Override
    public void setDateClicked(Date dateClicked) {
        this.dateClicked = dateClicked;
    }

    /* (non-Javadoc)
     * @see cn.globalph.common.email.domain.EmailTrackingClicks#getDestinationUri()
     */
    @Override
    public String getDestinationUri() {
        return destinationUri;
    }

    /* (non-Javadoc)
     * @see cn.globalph.common.email.domain.EmailTrackingClicks#setDestinationUri(java.lang.String)
     */
    @Override
    public void setDestinationUri(String destinationUri) {
        this.destinationUri = destinationUri;
    }

    /* (non-Javadoc)
     * @see cn.globalph.common.email.domain.EmailTrackingClicks#getQueryString()
     */
    @Override
    public String getQueryString() {
        return queryString;
    }

    /* (non-Javadoc)
     * @see cn.globalph.common.email.domain.EmailTrackingClicks#setQueryString(java.lang.String)
     */
    @Override
    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    /* (non-Javadoc)
     * @see cn.globalph.common.email.domain.EmailTrackingClicks#getEmailTracking()
     */
    @Override
    public EmailTracking getEmailTracking() {
        return emailTracking;
    }

    /* (non-Javadoc)
     * @see cn.globalph.common.email.domain.EmailTrackingClicks#setEmailTracking(cn.globalph.common.email.domain.EmailTrackingImpl)
     */
    @Override
    public void setEmailTracking(EmailTracking emailTracking) {
        this.emailTracking = emailTracking;
    }

    /**
     * @return the customer
     */
    @Override
    public String getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customer to set
     */
    @Override
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
        result = prime * result + ((dateClicked == null) ? 0 : dateClicked.hashCode());
        result = prime * result + ((destinationUri == null) ? 0 : destinationUri.hashCode());
        result = prime * result + ((emailTracking == null) ? 0 : emailTracking.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EmailTrackingClicksImpl other = (EmailTrackingClicksImpl) obj;

        if (id != null && other.id != null) {
            return id.equals(other.id);
        }

        if (customerId == null) {
            if (other.customerId != null)
                return false;
        } else if (!customerId.equals(other.customerId))
            return false;
        if (dateClicked == null) {
            if (other.dateClicked != null)
                return false;
        } else if (!dateClicked.equals(other.dateClicked))
            return false;
        if (destinationUri == null) {
            if (other.destinationUri != null)
                return false;
        } else if (!destinationUri.equals(other.destinationUri))
            return false;
        if (emailTracking == null) {
            if (other.emailTracking != null)
                return false;
        } else if (!emailTracking.equals(other.emailTracking))
            return false;
        return true;
    }

}

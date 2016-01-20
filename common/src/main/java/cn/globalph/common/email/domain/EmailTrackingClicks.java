package cn.globalph.common.email.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @felix.wu
 *
 */
public interface EmailTrackingClicks extends Serializable {

    /**
     * @return the emailId
     */
    public abstract Long getId();

    /**
     * @param id the i to set
     */
    public abstract void setId(Long id);

    /**
     * @return the dateClicked
     */
    public abstract Date getDateClicked();

    /**
     * @param dateClicked the dateClicked to set
     */
    public abstract void setDateClicked(Date dateClicked);

    /**
     * @return the destinationUri
     */
    public abstract String getDestinationUri();

    /**
     * @param destinationUri the destinationUri to set
     */
    public abstract void setDestinationUri(String destinationUri);

    /**
     * @return the queryString
     */
    public abstract String getQueryString();

    /**
     * @param queryString the queryString to set
     */
    public abstract void setQueryString(String queryString);

    /**
     * @return the emailTracking
     */
    public abstract EmailTracking getEmailTracking();

    /**
     * @param emailTracking the emailTracking to set
     */
    public abstract void setEmailTracking(EmailTracking emailTracking);

    public abstract String getCustomerId();

    /**
     * @param customerId the customer to set
     */
    public abstract void setCustomerId(String customerId);

}

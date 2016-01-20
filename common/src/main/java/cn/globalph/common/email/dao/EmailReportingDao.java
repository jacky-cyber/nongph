package cn.globalph.common.email.dao;

import cn.globalph.common.email.domain.EmailTarget;
import cn.globalph.common.email.domain.EmailTracking;

/**
 * @felix.wu
 *
 */
public interface EmailReportingDao {

    public Long createTracking(String emailAddress, String type, String extraValue) ;
    public void recordOpen(Long emailId, String userAgent);
    public void recordClick(Long emailId, String customerId, String destinationUri, String queryString);
    public EmailTracking retrieveTracking(Long emailId);
    public EmailTarget createTarget();

}

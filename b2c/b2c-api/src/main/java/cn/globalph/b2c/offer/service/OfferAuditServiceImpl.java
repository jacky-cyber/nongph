package cn.globalph.b2c.offer.service;

import cn.globalph.b2c.offer.dao.OfferAuditDao;
import cn.globalph.b2c.offer.domain.OfferAudit;
import cn.globalph.common.util.TransactionUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 *
 * @author Phillip Verheyden (phillipuniverse)
 */
@Service("blOfferAuditService")
public class OfferAuditServiceImpl implements OfferAuditService {
    
    @Resource(name = "blOfferAuditDao")
    protected OfferAuditDao offerAuditDao;
    
    @Override
    public OfferAudit readAuditById(Long offerAuditId) {
        return offerAuditDao.readAuditById(offerAuditId);
    }

    @Override
    @Transactional(TransactionUtils.DEFAULT_TRANSACTION_MANAGER)
    public OfferAudit save(OfferAudit offerAudit) {
        return offerAuditDao.save(offerAudit);
    }
    
    @Override
    @Transactional(TransactionUtils.DEFAULT_TRANSACTION_MANAGER)
    public void delete(OfferAudit offerAudit) {
        offerAuditDao.delete(offerAudit);
    }

    @Override
    public OfferAudit create() {
        return offerAuditDao.create();
    }
    
    @Override
    public Long countUsesByCustomer(Long customerId, Long offerId) {
        return offerAuditDao.countUsesByCustomer(customerId, offerId);
    }
}

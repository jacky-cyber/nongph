package cn.globalph.b2c.rating.dao;

import cn.globalph.b2c.rating.domain.ReviewDetail;
import cn.globalph.b2c.rating.domain.ReviewDetailImpl;
import cn.globalph.b2c.rating.domain.ReviewFeedback;
import cn.globalph.common.persistence.EntityConfiguration;
import cn.globalph.passport.domain.Customer;

import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository("blReviewDetailDao")
public class ReviewDetailDaoImpl implements ReviewDetailDao {

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Resource(name="blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    public ReviewDetail readReviewDetailById(Long reviewId) {
        return em.find(ReviewDetailImpl.class, reviewId);
    }

    public ReviewDetail saveReviewDetail(ReviewDetail reviewDetail) {
        return em.merge(reviewDetail);
    }
    
    @Override
    public ReviewDetail readReviewByCustomerAndItem(Customer customer, String itemId) {
        final Query query = em.createNamedQuery("BC_READ_REVIEW_DETAIL_BY_CUSTOMER_ID_AND_ITEM_ID");
        query.setParameter("customerId", customer.getId());
        query.setParameter("itemId", itemId);
        ReviewDetail reviewDetail = null;
        try {
            reviewDetail = (ReviewDetail) query.getSingleResult();
        } catch (NoResultException nre) {
            //ignore
        }
        return reviewDetail;
    }

    public ReviewDetail create() {
        return (ReviewDetail) entityConfiguration.createEntityInstance(ReviewDetail.class.getName());
    }

    public ReviewFeedback createFeedback() {
        return (ReviewFeedback) entityConfiguration.createEntityInstance(ReviewFeedback.class.getName());
    }
}

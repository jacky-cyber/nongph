package cn.globalph.b2c.rating.dao;

import cn.globalph.b2c.rating.domain.ReviewDetail;
import cn.globalph.b2c.rating.domain.ReviewFeedback;
import cn.globalph.passport.domain.Customer;

public interface ReviewDetailDao {

    ReviewDetail readReviewDetailById(Long reviewId);
    ReviewDetail saveReviewDetail(ReviewDetail reviewDetail);
    ReviewDetail create();
    ReviewFeedback createFeedback();
    ReviewDetail readReviewByCustomerAndItem(Customer customer, String itemId);

}

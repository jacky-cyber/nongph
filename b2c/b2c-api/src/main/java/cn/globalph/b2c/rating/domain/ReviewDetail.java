package cn.globalph.b2c.rating.domain;

import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.rating.service.type.ReviewStatusType;
import cn.globalph.passport.domain.Customer;

import java.util.Date;
import java.util.List;

public interface ReviewDetail {

    Long getId();
    Customer getCustomer();
    String getReviewText();
    void setReviewText(String reviewText);
    Date getReviewSubmittedDate();
    Integer helpfulCount();
    Integer notHelpfulCount();
    ReviewStatusType getStatus();
    RatingSummary getRatingSummary();
    RatingDetail getRatingDetail();
    List<ReviewFeedback> getReviewFeedback();
    OrderItem getOrderItem();

}

package cn.globalph.b2c.rating.service;

import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.rating.domain.RatingSummary;
import cn.globalph.b2c.rating.domain.ReviewDetail;
import cn.globalph.b2c.rating.service.type.RatingSortType;
import cn.globalph.b2c.rating.service.type.RatingType;
import cn.globalph.passport.domain.Customer;

import java.util.List;
import java.util.Map;

public interface RatingService {

    public RatingSummary saveRatingSummary(RatingSummary rating);
    public void deleteRatingSummary(RatingSummary rating);
    public RatingSummary readRatingSummary(String itemId, RatingType type);
    public Map<String, RatingSummary> readRatingSummaries(List<String> itemIds, RatingType type);
    public void rateItem(String itemId, RatingType type, Customer customer, Double rating);

    public List<ReviewDetail> readReviews(String itemId, RatingType type, int start, int finish, RatingSortType sortBy);
    public void reviewItem(String itemId, RatingType type, Customer customer, Double rating, String reviewText);
    void reviewItem(String itemId, RatingType type, Customer customer,String[] ratingList, String reviewText, OrderItem orderItem);
    public void markReviewHelpful(Long reviewId, Customer customer, Boolean helpful);
    
    /**
     * Reads a ReviewDetail by the given customer and the itemId
     * @param itemId
     * @param customer
     * @return review, or null if review is not found
     */
    public ReviewDetail readReviewByCustomerAndItem(Customer customer, String itemId);

}

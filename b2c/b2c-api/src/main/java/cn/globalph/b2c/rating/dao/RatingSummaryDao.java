package cn.globalph.b2c.rating.dao;

import cn.globalph.b2c.rating.domain.RatingDetail;
import cn.globalph.b2c.rating.domain.RatingSummary;
import cn.globalph.b2c.rating.domain.ReviewDetail;
import cn.globalph.b2c.rating.service.type.RatingType;
import cn.globalph.passport.domain.Customer;

import java.util.Date;
import java.util.List;

public interface RatingSummaryDao {

    public RatingSummary createSummary();
    
    public RatingSummary createSummary(String itemId, RatingType type);
    
    public RatingDetail createDetail();

    public RatingDetail createDetail(RatingSummary ratingSummary,Double rating, Date submittedDate, Customer customer);

    RatingDetail createDetail(RatingSummary ratingSummary, String[] ratingArray, Date submittedDate, Customer customer);
    
    RatingSummary readRatingSummary(String itemId, RatingType type);
    List<RatingSummary> readRatingSummaries(List<String> itemIds, RatingType type);
    RatingSummary saveRatingSummary(RatingSummary summary);
    void deleteRatingSummary(RatingSummary summary);

    RatingDetail readRating(Long customerId, Long ratingSummaryId);
    ReviewDetail readReview(Long customerId, Long ratingSummaryId);
    ReviewDetail readReview(Long customerId, Long ratingSummaryId, Long orderItemId);
}

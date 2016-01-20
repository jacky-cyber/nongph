package cn.globalph.b2c.rating.domain;

import cn.globalph.b2c.rating.service.type.RatingType;

import java.util.List;

public interface RatingSummary {
    
    public Long getId();
    
    public void setId(Long id);
    
    public RatingType getRatingType();
    
    public void setRatingType(RatingType ratingType);
    
    public String getItemId();
    
    public void setItemId(String itemId);
    
    public Integer getNumberOfRatings();
    
    public Integer getNumberOfReviews();
    
    public Double getAverageRating();
    
    public void resetAverageRating();

    public List<ReviewDetail> getReviews();
    
    public void setReviews(List<ReviewDetail> reviews);
    
    public List<RatingDetail> getRatings();
    
    public void setRatings(List<RatingDetail> ratings);

}

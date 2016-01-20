package cn.globalph.b2c.rating.domain;

import cn.globalph.passport.domain.Customer;

import java.util.Date;

public interface RatingDetail {

    public Long getId();
    
    public void setId(Long id);
    
    public Double getRating();
    
    public void setRating(Double newRating);
    /*add by jenny start*/
    public Double getLogisticsRating();
    public void setLogisticsRating(Double newRating);
    public Double getServeRating();
    public void setServeRating(Double newRating);
    /*add by jenny end*/
    public Customer getCustomer();
    
    public void setCustomer(Customer customer);
    
    public Date getRatingSubmittedDate();
    
    public void setRatingSubmittedDate(Date ratingSubmittedDate);
    
    public RatingSummary getRatingSummary();
    
    public void setRatingSummary(RatingSummary ratingSummary);

}

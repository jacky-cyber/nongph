package cn.globalph.b2c.rating.domain;


import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.domain.CustomerImpl;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import java.util.Date;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "NPH_RATING_DETAIL")
public class RatingDetailImpl implements RatingDetail {

    @Id
    @GeneratedValue(generator = "RatingDetailId")
    @GenericGenerator(
        name="RatingDetailId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="RatingDetailImpl"),
            @Parameter(name="entity_name", value="cn.globalph.b2c.rating.domain.RatingDetailImpl")
        }
    )

    @Column(name = "RATING_DETAIL_ID")
    private Long id;

    @Column(name = "RATING", nullable = false)
    protected Double rating;

    @Column(name = "RATING_SUBMITTED_DATE", nullable = false)
    protected Date ratingSubmittedDate;

    /*add by jenny start*/
    @Column(name = "LOGISTICS_RATING", nullable = false)
    protected Double logisticsRating;

    @Column(name = "SERVE_RATING", nullable = false)
    protected Double serveRating;
    /*add by jenny end*/

    @ManyToOne(targetEntity = CustomerImpl.class, optional = false)
    @JoinColumn(name = "CUSTOMER_ID")
    @Index(name="RATING_CUSTOMER_INDEX", columnNames={"CUSTOMER_ID"})
    protected Customer customer;

    @ManyToOne(optional = false, targetEntity = RatingSummaryImpl.class)
    @JoinColumn(name = "RATING_SUMMARY_ID")
    protected RatingSummary ratingSummary;

    @Override
    public Long getId() {
        return id;
    }
    
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Double getRating() {
        return rating;
    }
    
    @Override
    public void setRating(Double newRating) {
        this.rating = newRating;
    }
    /*add by jenny start*/
    @Override
    public Double getLogisticsRating() {
        return this.logisticsRating;
    }

    @Override
    public void setLogisticsRating(Double newRating) {
        this.logisticsRating = newRating;
    }

    @Override
    public Double getServeRating() {
        return serveRating;
    }

    @Override
    public void setServeRating(Double newRating) {
        this.serveRating = newRating;
    }
    /*add by jenny end*/
    @Override
    public Date getRatingSubmittedDate() {
        return ratingSubmittedDate;
    }
    
    @Override
    public void setRatingSubmittedDate(Date ratingSubmittedDate) {
        this.ratingSubmittedDate = ratingSubmittedDate;
    }

    @Override
    public Customer getCustomer() {
        return customer;
    }
    
    @Override
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public RatingSummary getRatingSummary() {
        return ratingSummary;
    }
    
    @Override
    public void setRatingSummary(RatingSummary ratingSummary) {
        this.ratingSummary = ratingSummary;
    }
    
    
}

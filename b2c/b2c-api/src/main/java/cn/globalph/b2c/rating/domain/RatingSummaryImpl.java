package cn.globalph.b2c.rating.domain;


import cn.globalph.b2c.rating.service.type.RatingType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "NPH_RATING_SUMMARY")
public class RatingSummaryImpl implements RatingSummary {

    @Id
    @GeneratedValue(generator = "RatingSummaryId")
    @GenericGenerator(
        name="RatingSummaryId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="RatingSummaryImpl"),
            @Parameter(name="entity_name", value="cn.globalph.b2c.rating.domain.RatingSummaryImpl")
        }
    )
    @Column(name = "RATING_SUMMARY_ID")
    protected Long id;

    @Column(name = "ITEM_ID", nullable = false)
    @Index(name="RATINGSUMM_ITEM_INDEX", columnNames={"ITEM_ID"})
    protected String itemId;

    @Column(name = "RATING_TYPE", nullable = false)
    @Index(name="RATINGSUMM_TYPE_INDEX", columnNames={"RATING_TYPE"})
    protected String ratingTypeStr;

    @Column(name = "AVERAGE_RATING", nullable = false)
    protected Double averageRating = new Double(0);

    @OneToMany(mappedBy = "ratingSummary", targetEntity = RatingDetailImpl.class, cascade = {CascadeType.ALL})
    protected List<RatingDetail> ratings = new ArrayList<RatingDetail>();

    @OneToMany(mappedBy = "ratingSummary", targetEntity = ReviewDetailImpl.class, cascade = {CascadeType.ALL})
    @OrderBy(value = "REVIEW_SUBMITTED_DATE desc")
    protected List<ReviewDetail> reviews = new ArrayList<ReviewDetail>();

    @Override
    public Long getId() {
        return id;
    }
    
    /**
     * @param id the id to set
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Double getAverageRating() {
        return averageRating;
    }
    
    @Override
    public void resetAverageRating() {
        if (ratings == null || ratings.isEmpty()) {
            this.averageRating = 0.0;
        } else {
            double sum = 0;
            for (RatingDetail detail : ratings) {
                sum += detail.getRating();
            }
            Double realAverageRating = sum / ratings.size();
            BigDecimal bigDecimal = new BigDecimal(realAverageRating);
            bigDecimal = bigDecimal.setScale(1,BigDecimal.ROUND_HALF_UP);
            this.averageRating =bigDecimal.doubleValue();
        }
    }

    @Override
    public String getItemId() {
        return itemId;
    }
    
    @Override
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public Integer getNumberOfRatings() {
        return getRatings().size();
    }

    @Override
    public Integer getNumberOfReviews() {
        return getReviews().size();
    }

    @Override
    public RatingType getRatingType() {
        return new RatingType(ratingTypeStr);
    }
    
    @Override
    public void setRatingType(RatingType type) {
        ratingTypeStr = (type == null) ? null : type.getType();
    }

    @Override
    public List<RatingDetail> getRatings() {
        return ratings == null ? new ArrayList<RatingDetail>() : ratings;
    }
    
    @Override
    public void setRatings(List<RatingDetail> ratings) {
        this.ratings = ratings;
    }

    @Override
    public List<ReviewDetail> getReviews() {
        return reviews == null ? new ArrayList<ReviewDetail>() : reviews;
    }
    
    @Override
    public void setReviews(List<ReviewDetail> reviews) {
        this.reviews = reviews;
    }

}

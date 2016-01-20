package cn.globalph.b2c.rating.domain;

import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.domain.CustomerImpl;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "NPH_REVIEW_FEEDBACK")
public class ReviewFeedbackImpl implements ReviewFeedback {

    @Id
    @GeneratedValue(generator = "ReviewFeedbackId")
    @GenericGenerator(
        name="ReviewFeedbackId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="ReviewFeedbackImpl"),
            @Parameter(name="entity_name", value="cn.globalph.b2c.rating.domain.ReviewFeedbackImpl")
        }
    )
    @Column(name = "REVIEW_FEEDBACK_ID")
    protected Long id;

    @ManyToOne(targetEntity = CustomerImpl.class, optional = false)
    @JoinColumn(name = "CUSTOMER_ID")
    @Index(name="REVIEWFEED_CUSTOMER_INDEX", columnNames={"CUSTOMER_ID"})
    protected Customer customer;

    @Column(name = "IS_HELPFUL", nullable = false)
    protected Boolean isHelpful = false;

    @ManyToOne(optional = false, targetEntity = ReviewDetailImpl.class)
    @JoinColumn(name = "REVIEW_DETAIL_ID")
    @Index(name="REVIEWFEED_DETAIL_INDEX", columnNames={"REVIEW_DETAIL_ID"})
    protected ReviewDetail reviewDetail;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Customer getCustomer() {
        return customer;
    }

    @Override
    public ReviewDetail getReviewDetail() {
        return reviewDetail;
    }

    @Override
    public Boolean getIsHelpful() {
        return isHelpful;
    }

    @Override
    public void setIsHelpful(Boolean isHelpful) {
        this.isHelpful = isHelpful;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public void setReviewDetail(ReviewDetail reviewDetail) {
        this.reviewDetail = reviewDetail;
    }

}

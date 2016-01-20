package cn.globalph.b2c.web.processor;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.globalph.b2c.rating.domain.RatingSummary;
import cn.globalph.b2c.rating.domain.ReviewDetail;
import cn.globalph.b2c.rating.service.RatingService;
import cn.globalph.b2c.rating.service.type.RatingSortType;
import cn.globalph.b2c.rating.service.type.RatingType;
import cn.globalph.common.web.dialect.AbstractModelVariableModifierProcessor;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.web.core.CustomerState;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.standard.expression.Expression;
import org.thymeleaf.standard.expression.StandardExpressions;

import javax.annotation.Resource;

/**
 * A Thymeleaf processor that will add the product ratings and reviews to the model
 *
 * @author jfridye
 */
public class RatingsProcessor extends AbstractModelVariableModifierProcessor {
    
    @Resource(name = "blRatingService")
    protected RatingService ratingService;

    /**
     * Sets the name of this processor to be used in Thymeleaf template
     *
     * NOTE: Thymeleaf normalizes the attribute names by converting all to lower-case
     * we will use the underscore instead of camel case to avoid confusion
     *
     */
    public RatingsProcessor() {
        super("ratings");
    }

    @Override
    public int getPrecedence() {
        return 10000;
    }

    @Override
    protected void modifyModelAttributes(Arguments arguments, Element element) {
        Expression expression = (Expression) StandardExpressions.getExpressionParser(arguments.getConfiguration())
                .parseExpression(arguments.getConfiguration(), arguments, element.getAttributeValue("itemId"));
        String itemId = String.valueOf(expression.execute(arguments.getConfiguration(), arguments));
        RatingSummary ratingSummary = ratingService.readRatingSummary(itemId, RatingType.PRODUCT);
        if (ratingSummary != null) {
            addToModel(arguments, getRatingsVar(element), ratingSummary);
        }
        
        Integer maxResults = null; 
        try{
        	maxResults = Integer.parseInt((String)element.getAttributeValue("maxResults"));
        }catch(Exception e){
        	maxResults = null;
        }
        
        if(maxResults != null){
	        List<ReviewDetail> reviewDetails =  ratingService.readReviews(itemId,RatingType.PRODUCT,0,maxResults - 1,RatingSortType.DEFAULT);
	        if(reviewDetails != null && reviewDetails.size() > 0){
	        	addToModel(arguments, "reviewDetails", reviewDetails);
	        }
        }
//        Customer customer = CustomerState.getCustomer();
//        ReviewDetail reviewDetail = null;
//        if (!customer.isAnonymous()) {
//            reviewDetail = ratingService.readReviewByCustomerAndItem(customer, itemId);
//        }
//        if (reviewDetail != null) {
//            addToModel(arguments, "currentCustomerReview", reviewDetail);
//        }
        
    }
    
    private String getRatingsVar(Element element) {
        String ratingsVar = element.getAttributeValue("ratingsVar");
        if (StringUtils.isNotEmpty(ratingsVar)) {
            return ratingsVar;
        } 
        return "ratingSummary";
    }

}

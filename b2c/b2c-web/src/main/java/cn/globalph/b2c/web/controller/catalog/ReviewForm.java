package cn.globalph.b2c.web.controller.catalog;

import cn.globalph.b2c.product.domain.Product;

import java.io.Serializable;

public class ReviewForm implements Serializable {

    private static final long serialVersionUID = 1L;

    protected Product product;
    protected Double rating;
    protected String reviewText;
    protected String orderItemId;

    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    public Double getRating() {
        return rating;
    }
    public void setRating(Double rating) {
        this.rating = rating;
    }
    public String getReviewText() {
        return reviewText;
    }
    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
	public String getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}   
}

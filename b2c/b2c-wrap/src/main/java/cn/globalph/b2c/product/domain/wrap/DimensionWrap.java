package cn.globalph.b2c.product.domain.wrap;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;

public class DimensionWrap{

    @XmlElement
    protected BigDecimal width;

    @XmlElement
    protected BigDecimal height;

    @XmlElement
    protected BigDecimal depth;

    @XmlElement
    protected BigDecimal girth;

    @XmlElement
    protected String container;

    @XmlElement
    protected String size;
    
    @XmlElement
    protected String dimensionUnitOfMeasure;

	public BigDecimal getWidth() {
		return width;
	}

	public void setWidth(BigDecimal width) {
		this.width = width;
	}

	public BigDecimal getHeight() {
		return height;
	}

	public void setHeight(BigDecimal height) {
		this.height = height;
	}

	public BigDecimal getDepth() {
		return depth;
	}

	public void setDepth(BigDecimal depth) {
		this.depth = depth;
	}

	public BigDecimal getGirth() {
		return girth;
	}

	public void setGirth(BigDecimal girth) {
		this.girth = girth;
	}

	public String getContainer() {
		return container;
	}

	public void setContainer(String container) {
		this.container = container;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getDimensionUnitOfMeasure() {
		return dimensionUnitOfMeasure;
	}

	public void setDimensionUnitOfMeasure(String dimensionUnitOfMeasure) {
		this.dimensionUnitOfMeasure = dimensionUnitOfMeasure;
	}
}

package cn.globalph.b2c.catalog.domain.wrap;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is a JAXB wrapper class for wrapping a collection of categories.
 */
@XmlRootElement(name = "categories")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class CategoriesWrap {

    @XmlElement(name = "category")
    protected List<CategoryWrap> categories = new ArrayList<CategoryWrap>();

	public List<CategoryWrap> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryWrap> categories) {
		this.categories = categories;
	}
}

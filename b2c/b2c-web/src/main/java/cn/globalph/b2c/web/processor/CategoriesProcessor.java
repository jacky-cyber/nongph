package cn.globalph.b2c.web.processor;

import org.apache.commons.lang.StringUtils;

import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.b2c.catalog.domain.CategoryXref;
import cn.globalph.b2c.catalog.service.CatalogService;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;
import cn.globalph.common.web.dialect.AbstractModelVariableModifierProcessor;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.standard.expression.Expression;
import org.thymeleaf.standard.expression.StandardExpressions;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

/**
 * A Thymeleaf processor that will add the desired categories to the model. It does this by
 * searching for the parentCategory name and adding up to maxResults subcategories under
 * the model attribute specified by resultVar
 * 
 */
public class CategoriesProcessor extends AbstractModelVariableModifierProcessor {
    
    @Resource(name = "blCatalogService")
    protected CatalogService catalogService;

    @Resource(name = "blCategoriesProcessorExtensionManager")
    protected CategoriesProcessorExtensionManager extensionManager;

    /**
     * Sets the name of this processor to be used in Thymeleaf template
     */
    public CategoriesProcessor() {
        super("categories");
    }
    
    @Override
    public int getPrecedence() {
        return 10000;
    }

    @Override
    protected void modifyModelAttributes(Arguments arguments, Element element) {
        String resultVar = element.getAttributeValue("resultVar");
        String parentCategory = element.getAttributeValue("parentCategory");
        if(parentCategory.startsWith("$")){
	        Expression expression = (Expression) StandardExpressions.getExpressionParser(arguments.getConfiguration())
	                .parseExpression(arguments.getConfiguration(), arguments, parentCategory);
	        parentCategory  = (String) expression.execute(arguments.getConfiguration(), arguments);
        }
        String unparsedMaxResults = element.getAttributeValue("maxResults");

        if (extensionManager != null) {
            ExtensionResultHolder holder = new ExtensionResultHolder();
            ExtensionResultStatusType result = extensionManager.getHandlerProxy().findAllPossibleChildCategories(parentCategory, unparsedMaxResults, holder);
            if (ExtensionResultStatusType.HANDLED.equals(result)) {
                addToModel(arguments, resultVar, holder.getResult());
                return;
            }
        }

        // TODO: Potentially write an algorithm that will pick the minimum depth category
        // instead of the first category in the list
        List<Category> categories = catalogService.findCategoriesByName(parentCategory);
        if (categories != null && categories.size() > 0) {
            // gets child categories in order ONLY if they are in the xref table and active
            List<CategoryXref> subcategories = categories.get(0).getChildCategoryXrefs();
            if (subcategories != null && !subcategories.isEmpty()) {
                if (StringUtils.isNotEmpty(unparsedMaxResults)) {
                    int maxResults = Integer.parseInt(unparsedMaxResults);
                    if (subcategories.size() > maxResults) {
                        subcategories = subcategories.subList(0, maxResults);
                    }
                }
            }
            List<Category> results = new ArrayList<Category>(subcategories.size());
            for (CategoryXref xref : subcategories) {
                results.add(xref.getSubCategory());
            }
            
            addToModel(arguments, resultVar, results);
        }
    }
}

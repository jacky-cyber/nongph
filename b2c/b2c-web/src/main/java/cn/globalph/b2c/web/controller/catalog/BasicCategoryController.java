package cn.globalph.b2c.web.controller.catalog;

import org.apache.commons.lang.StringUtils;

import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.search.domain.SkuSearchCriteria;
import cn.globalph.b2c.search.domain.SkuSearchResult;
import cn.globalph.b2c.search.service.SearchFacetDTOService;
import cn.globalph.b2c.search.service.SearchService;
import cn.globalph.b2c.web.catalog.CategoryHandlerMapping;
import cn.globalph.b2c.web.util.ProcessorUtils;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.template.TemplateOverrideExtensionManager;
import cn.globalph.common.template.TemplateType;
import cn.globalph.common.web.TemplateTypeAware;
import cn.globalph.common.web.WebRequestContext;
import cn.globalph.common.web.controller.BasicController;
import cn.globalph.common.web.deeplink.DeepLinkService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class works in combination with the CategoryHandlerMapping which finds a
 * category based upon the passed in URL.
 *
 * @author felix.wu
 */
public class BasicCategoryController extends BasicController implements
		Controller, TemplateTypeAware {

	protected static String defaultCategoryView = "catalog/category";
	protected static String CATEGORY_ATTRIBUTE_NAME = "category";
	protected static String SKUS_ATTRIBUTE_NAME = "skus";
	protected static String FACETS_ATTRIBUTE_NAME = "facets";
	protected static String PRODUCT_SEARCH_RESULT_ATTRIBUTE_NAME = "result";
	protected static String ACTIVE_FACETS_ATTRIBUTE_NAME = "activeFacets";
	protected static String ALL_SKUS_ATTRIBUTE_NAME = "blcAllDisplayedSkus";

	@Resource(name = "blSearchService")
	protected SearchService searchService;

	@Resource(name = "blSearchFacetDTOService")
	protected SearchFacetDTOService facetService;

	@Autowired(required = false)
	@Qualifier("blCategoryDeepLinkService")
	protected DeepLinkService<Category> deepLinkService;

	@Resource(name = "blTemplateOverrideExtensionManager")
	protected TemplateOverrideExtensionManager templateOverrideManager;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView model = new ModelAndView();

		if (request.getParameterMap().containsKey("facetField")) {
			// If we receive a facetField parameter, we need to convert the
			// field to the
			// product search criteria expected format. This is used in
			// multi-facet selection. We
			// will send a redirect to the appropriate URL to maintain canonical
			// URLs

			String fieldName = request.getParameter("facetField");
			List<String> activeFieldFilters = new ArrayList<String>();
			Map<String, String[]> parameters = new HashMap<String, String[]>(
					request.getParameterMap());

			for (Iterator<Entry<String, String[]>> iter = parameters.entrySet()
					.iterator(); iter.hasNext();) {
				Map.Entry<String, String[]> entry = iter.next();
				String key = entry.getKey();
				if (key.startsWith(fieldName + "-")) {
					activeFieldFilters.add(key.substring(key.indexOf('-') + 1));
					iter.remove();
				}
			}

			parameters.remove(SkuSearchCriteria.PAGE_NUMBER);
			parameters.put(fieldName, activeFieldFilters
					.toArray(new String[activeFieldFilters.size()]));
			parameters.remove("facetField");

			String newUrl = ProcessorUtils.getUrl(request.getRequestURL()
					.toString(), parameters);
			model.setViewName("redirect:" + newUrl);
		} else {
			// Else, if we received a GET to the category URL (either the user
			// clicked this link or we redirected
			// from the POST method, we can actually process the results

			Category category = (Category) request
					.getAttribute(CategoryHandlerMapping.CURRENT_CATEGORY_ATTRIBUTE_NAME);
			assert (category != null);

			SkuSearchCriteria searchCriteria = facetService
					.buildSearchCriteria(request);

			String searchTerm = request
					.getParameter(SkuSearchCriteria.QUERY_STRING);
			SkuSearchResult result;
			if (StringUtils.isNotBlank(searchTerm)) {
				result = searchService.findProductsByCategoryAndQuery(category,
						searchTerm, searchCriteria);
			} else {
				result = searchService.findSkusByCategory(category,
						searchCriteria);
			}

			facetService.setActiveFacetResults(result.getFacets(), request);

			model.addObject(CATEGORY_ATTRIBUTE_NAME, category);
			model.addObject(SKUS_ATTRIBUTE_NAME, result.getSkus());
			model.addObject(FACETS_ATTRIBUTE_NAME, result.getFacets());
			model.addObject(PRODUCT_SEARCH_RESULT_ATTRIBUTE_NAME, result);
			if (result.getSkus() != null) {
				model.addObject(ALL_SKUS_ATTRIBUTE_NAME, new HashSet<Sku>(
						result.getSkus()));
			}

			addDeepLink(model, deepLinkService, category);

			ExtensionResultHolder<String> erh = new ExtensionResultHolder<String>();
			templateOverrideManager.getHandlerProxy().getOverrideTemplate(erh,
					category);

			if (StringUtils.isNotBlank(erh.getResult())) {
				model.setViewName(erh.getResult());
			} else if (StringUtils.isNotEmpty(category.getDisplayTemplate())) {
				model.setViewName(category.getDisplayTemplate());
			} else {
				model.setViewName(getDefaultCategoryView());
			}
		}
		return model;
	}

	public String getDefaultCategoryView() {
		return defaultCategoryView;
	}

	@Override
	public String getExpectedTemplateName(HttpServletRequest request) {
		WebRequestContext context = WebRequestContext.getWebRequestContext();
		if (context != null) {
			Category category = (Category) context.getRequest().getAttribute(
					CATEGORY_ATTRIBUTE_NAME);
			if (category != null && category.getDisplayTemplate() != null) {
				return category.getDisplayTemplate();
			}
		}
		return getDefaultCategoryView();
	}

	@Override
	public TemplateType getTemplateType(HttpServletRequest request) {
		return TemplateType.CATEGORY;
	}

}

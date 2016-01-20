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

import com.google.gson.Gson;

import java.io.PrintWriter;
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


public class BasicAjaxCategoryController extends BasicController implements
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
			result = searchService.findSkusByCategory(category, searchCriteria);
		}

		facetService.setActiveFacetResults(result.getFacets(), request);

		List<Sku> skus = result.getSkus();
		List<Map<String, Object>> returnSkus = new ArrayList<Map<String, Object>>();
		for (Sku sku : skus) {
			Map<String, Object> map = new HashMap<String, Object>();
			if (sku.getProduct() != null && sku.getProduct().getUrl() != null) {
				map.put("href", sku.getProduct().getUrl() + '/' + sku.getId());
			}
			if (sku.getSkuMedia() != null
					&& sku.getSkuMedia().get("primary") != null
					&& sku.getSkuMedia().get("primary").getUrl() != null) {
				map.put("media", sku.getSkuMedia().get("primary").getUrl()
						+ "?browse");
			}
			if (sku.getName() != null) {
				map.put("alt", sku.getName());
			}
			if (sku.getDescription() != null) {
				map.put("description", sku.getDescription());
			}
			if (sku.getRetailPrice() != null) {
				map.put("retailPrice", sku.getRetailPrice().toString());
			}
			map.put("onSale", sku.isOnSale());
			if (sku.getSalePrice() != null) {
				map.put("salePrice", sku.getSalePrice());
			}
			returnSkus.add(map);
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(returnSkus);
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		out.print(json);
		return null;
	
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

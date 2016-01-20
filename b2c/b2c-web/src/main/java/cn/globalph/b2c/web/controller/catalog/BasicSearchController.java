package cn.globalph.b2c.web.controller.catalog;

import org.apache.commons.lang.StringUtils;

import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.search.domain.SkuSearchCriteria;
import cn.globalph.b2c.search.domain.SkuSearchResult;
import cn.globalph.b2c.search.redirect.domain.SearchRedirect;
import cn.globalph.b2c.search.redirect.service.SearchRedirectService;
import cn.globalph.b2c.search.service.SearchFacetDTOService;
import cn.globalph.b2c.search.service.SearchService;
import cn.globalph.b2c.web.util.ProcessorUtils;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.security.service.ExploitProtectionService;
import cn.globalph.common.util.UrlUtil;

import org.springframework.ui.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles searching the catalog for a given search term. Will apply product
 * search criteria such as filters, sorts, and pagination if applicable
 * 
 * @author felix.wu
 */
public class BasicSearchController extends AbstractCatalogController {

	private static String SKUS_ATTRIBUTE_NAME = "skus";
	private static String FACETS_ATTRIBUTE_NAME = "facets";
	private static String PRODUCT_SEARCH_RESULT_ATTRIBUTE_NAME = "result";
	// private static String ACTIVE_FACETS_ATTRIBUTE_NAME = "activeFacets";
	private static String ORIGINAL_QUERY_ATTRIBUTE_NAME = "originalQuery";
	private static String ALL_PRODUCTS_ATTRIBUTE_NAME = "blcAllDisplayedProducts";

	@Resource(name = "blSearchService")
	private SearchService searchService;

	@Resource(name = "blExploitProtectionService")
	private ExploitProtectionService exploitProtectionService;

	@Resource(name = "blSearchFacetDTOService")
	private SearchFacetDTOService facetService;
	@Resource(name = "blSearchRedirectService")
	private SearchRedirectService searchRedirectService;
	private static String searchView = "catalog/search";

	public String getSearchView() {
		return searchView;
	}

	protected SearchService getSearchService() {
		return searchService;
	}

	public String search(Model model, HttpServletRequest request,
			HttpServletResponse response, String query)
			throws ServletException, IOException, ServiceException {
		try {
			if (StringUtils.isNotEmpty(query)) {
				query = StringUtils.trim(query);
				query = exploitProtectionService.cleanString(query);
			}
		} catch (ServiceException e) {
			query = null;
		}

		if (query == null || query.length() == 0) {
			return "redirect:/";
		}

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
			return "redirect:" + newUrl;
		} else {
			// Else, if we received a GET to the category URL (either the user
			// performed a search or we redirected
			// from the POST method, we can actually process the results
			SearchRedirect handler = searchRedirectService
					.findSearchRedirectBySearchTerm(query);

			if (handler != null) {
				String contextPath = request.getContextPath();
				String url = UrlUtil.fixRedirectUrl(contextPath,
						handler.getUrl());
				response.sendRedirect(url);
				return null;
			}

			if (StringUtils.isNotEmpty(query)) {
				SkuSearchCriteria searchCriteria = facetService
						.buildSearchCriteria(request);
				SkuSearchResult result = getSearchService().findSkusByQuery(
						query, searchCriteria);

				facetService.setActiveFacetResults(result.getFacets(), request);

				model.addAttribute(SKUS_ATTRIBUTE_NAME, result.getSkus());
				model.addAttribute(FACETS_ATTRIBUTE_NAME, result.getFacets());
				model.addAttribute(PRODUCT_SEARCH_RESULT_ATTRIBUTE_NAME, result);
				model.addAttribute(ORIGINAL_QUERY_ATTRIBUTE_NAME, query);
				if (result.getSkus() != null) {
					model.addAttribute(ALL_PRODUCTS_ATTRIBUTE_NAME,
							new HashSet<Sku>(result.getSkus()));
				}
			}

		}
		return getSearchView();
	}

	public List<Map<String, Object>> ajaxSearch(Model model,
			HttpServletRequest request, HttpServletResponse response,
			String query) throws ServletException,
			IOException, ServiceException {
		try {
			if (StringUtils.isNotEmpty(query)) {
				query = StringUtils.trim(query);
				query = exploitProtectionService.cleanString(query);
			}
		} catch (ServiceException e) {
			query = null;
		}

		if (query == null || query.length() == 0) {
			return null;
		}

		// Else, if we received a GET to the category URL (either the user
		// performed a search or we redirected
		// from the POST method, we can actually process the results
		SearchRedirect handler = searchRedirectService
				.findSearchRedirectBySearchTerm(query);

		if (handler != null) {
			String contextPath = request.getContextPath();
			String url = UrlUtil.fixRedirectUrl(contextPath, handler.getUrl());
			response.sendRedirect(url);
			return null;
		}

		if (StringUtils.isNotEmpty(query)) {
			SkuSearchCriteria searchCriteria = facetService
					.buildSearchCriteria(request);
			SkuSearchResult result = getSearchService().findSkusByQuery(query,
					searchCriteria);

			facetService.setActiveFacetResults(result.getFacets(), request);

			List<Sku> skus = result.getSkus();
			List<Map<String, Object>> returnSkus = new ArrayList<Map<String,Object>>();
			for(Sku sku : skus){
				Map<String, Object> map = new HashMap<String, Object>();
				if(sku.getProduct() != null && sku.getProduct().getUrl() != null){
					map.put("href",sku.getProduct().getUrl() + '/' + sku.getId());
				}
				if(sku.getSkuMedia() != null && sku.getSkuMedia().get("primary") != null && sku.getSkuMedia().get("primary").getUrl() != null){
					map.put("media", sku.getSkuMedia().get("primary").getUrl()+"?browse");
				}
				if(sku.getName() != null){
					map.put("alt", sku.getName());
				}
				if(sku.getDescription() != null){
					map.put("description", sku.getDescription());
				}
				if(sku.getRetailPrice() != null){
					map.put("retailPrice", sku.getRetailPrice().toString());
				}
				map.put("onSale", sku.isOnSale());
				if(sku.getSalePrice() != null){
					map.put("salePrice", sku.getSalePrice());
				}
				returnSkus.add(map);
			}
			return returnSkus;
		}else{
			return null;
		}

	}
}
package cn.globalph.controller.catalog;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.globalph.b2c.product.dao.SkuDao;
import cn.globalph.b2c.product.domain.Sku;

@Controller
public class SkuController {
	@Resource(name = "blSkuDao")
	SkuDao skuDao;
	
	@RequestMapping(value = "/sku/info/{skuId}")
	public String viewSkuInfo(@PathVariable(value="skuId") Long skuId, Model model){
		Sku sku = skuDao.readSkuById(skuId);
		model.addAttribute("sku", sku);
		return "catalog/skuInfo";
	}
	
	@RequestMapping(value = "/sku/comment/{skuId}")
	public String viewComment(@PathVariable(value="skuId") Long skuId, Model model){
		Sku sku = skuDao.readSkuById(skuId);
		model.addAttribute("sku", sku);
		return "catalog/skuComment";
	}
}

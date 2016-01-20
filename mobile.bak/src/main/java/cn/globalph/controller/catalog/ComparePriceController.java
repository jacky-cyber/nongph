package cn.globalph.controller.catalog;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.globalph.b2c.product.dao.SkuDao;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.common.web.controller.BasicController;

@Controller
public class ComparePriceController extends BasicController {
	@Resource(name = "blSkuDao")
	private SkuDao skuDao;
	
	@RequestMapping(value = "/{skuId}/comparePrice")
	public String showComparePrice(@PathVariable(value = "skuId") Long skuId, Model model){
		if(skuId == null){
			return "redirect:/";
		}
		Sku sku = skuDao.readSkuById(skuId);
		
		if(sku == null){
			return "redirec:/";
		}else{
			model.addAttribute("sku", sku);
			return "catalog/skuComparePrice";
		}
	}
}

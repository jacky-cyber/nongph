package cn.globalph.controller.catalog;

import cn.globalph.common.exception.ServiceException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController extends cn.globalph.b2c.web.controller.catalog.BasicSearchController {

    @Override
    @RequestMapping("/search")
    public String search(Model model, HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(value = "q") String q) throws ServletException, IOException, ServiceException {
        return super.search(model, request,response, q);
    }
    
    @Override
    @RequestMapping(value="/ajaxSearch", produces="application/json")
    public @ResponseBody List<Map<String,Object>> ajaxSearch(Model model, HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(value = "q") String q) throws ServletException, IOException, ServiceException {
        return super.ajaxSearch(model, request,response, q);
    }

}
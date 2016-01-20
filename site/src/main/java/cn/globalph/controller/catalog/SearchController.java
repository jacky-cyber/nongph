package cn.globalph.controller.catalog;

import cn.globalph.b2c.web.controller.catalog.BasicSearchController;
import cn.globalph.common.exception.ServiceException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@Controller
@RequestMapping("/search")
public class SearchController extends BasicSearchController {

    @Override
    @RequestMapping("")
    public String search(Model model, HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(value = "q") String q) throws ServletException, IOException, ServiceException {
        return super.search(model, request,response, q);
    }

}
package com.itheima.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itheima.pojo.ResultModel;
import com.itheima.service.ProductService;

@Controller
public class ProductController {
	@Autowired
	private ProductService productService;
	@RequestMapping("list")
	public String search(String queryString,String catalog_name,String price , @RequestParam(defaultValue="1")int page,int sort,Model model ) throws Exception{
		
		ResultModel resultModel = productService.search(queryString, catalog_name, price, page, sort);
		model.addAttribute("result", resultModel);
		model.addAttribute("queryString", queryString);
		model.addAttribute("catalog_name", catalog_name);
		model.addAttribute("price", price);
		model.addAttribute("page", page);
		model.addAttribute("sort", sort);
		return "product_list";
	}
}

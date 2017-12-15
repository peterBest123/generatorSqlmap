package com.itheima.service;

import com.itheima.pojo.ResultModel;

public interface ProductService {
	
	ResultModel search(String queryString,String catalog_name,String PriceStr,int currentPage,int sort) throws Exception;
}

package com.itheima.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itheima.dao.ProductDao;
import com.itheima.pojo.ProductModel;
import com.itheima.pojo.ResultModel;
@Repository
public class ProductDaoImpl implements ProductDao {
	@Autowired
	private HttpSolrServer server;
	
	@Override
	public ResultModel search(SolrQuery query)throws Exception {
		//查询
		QueryResponse queryResponse = server.query(query);
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		//高亮显示
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		List<ProductModel> productList = new ArrayList<>();
		//取总页数
		long numFound = solrDocumentList.getNumFound();
		//取查询结果list
		for (SolrDocument solrDocument : solrDocumentList) {
			ProductModel productModel = new ProductModel();
			productModel.setCatalog_name((String) solrDocument.getFieldValue("product_catalog_name"));
			productModel.setPicture((String) solrDocument.getFieldValue("product_picture"));
			List<String> list = highlighting.get(solrDocument.getFieldValue("id")).get("product_name");
			String productName = null;
			if (list!= null && list.size()>0) {
				productName = list.get(0);
			}else {
				productName = (String) solrDocument.getFieldValue("product_name");
			}
			productModel.setName(productName);
			productModel.setPid((String) solrDocument.getFieldValue("id"));
			productModel.setPrice((float) solrDocument.getFieldValue("product_price"));
			productList.add(productModel);
		}
		ResultModel resultModel = new ResultModel();
		resultModel.setProductList(productList);
		resultModel.setRecordCount(numFound);
		//返回结果
		return resultModel;
	}

}

package com.itheima.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.itheima.dao.ProductDao;
import com.itheima.pojo.ResultModel;
import com.itheima.service.ProductService;
@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductDao productDao;
	
	private static final int ROWS = 60;
	@Override
	public ResultModel search(String queryString, String catalog_name, String PriceStr,int currentPage,
			int sort) throws Exception {
		
		SolrQuery solrQuery = new SolrQuery();
		//默认搜索域
		solrQuery.set("df", "product_keywords");
		if (queryString != null && !"".equals(queryString)) {
			solrQuery.setQuery(queryString);
		}else{
			solrQuery.setQuery("*:*");
		}
		if (catalog_name != null && !"".equals(catalog_name)) {
			solrQuery.addFilterQuery("product_catalog_name:" + catalog_name);
		}
		//价格区间过滤
		if (PriceStr != null && !"".equals(PriceStr)) {
			String[] split = PriceStr.split("-");
			solrQuery.addFilterQuery("product_price:[" + split[0] + " TO " + split[1] + "]" );
		}
		if (currentPage<1) {
			currentPage = 1;
		}
		//分页
		int star = (currentPage - 1)*ROWS;
		solrQuery.setStart(star);
		solrQuery.setRows(ROWS);
		
		//价格排序
		if (sort == 0) {
			solrQuery.setSort("product_price", ORDER.asc);
		}else if(sort == 1){
			solrQuery.setSort("product_price", ORDER.desc);
		}
		//查询高亮
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("product_name");
		solrQuery.setHighlightSimplePre("<em style=\"color:red\">");
		solrQuery.setHighlightSimplePost("</em>");
		//执行查询
		ResultModel resultModel = productDao.search(solrQuery);
		Long recordCount = resultModel.getRecordCount();
		int pageCount = (int) (recordCount / ROWS);
		if (recordCount % ROWS != 0) {
			pageCount ++;
		}
		//分页数据
		resultModel.setCurPage(currentPage);
		resultModel.setPageCount(pageCount);
		return resultModel;
	}

}

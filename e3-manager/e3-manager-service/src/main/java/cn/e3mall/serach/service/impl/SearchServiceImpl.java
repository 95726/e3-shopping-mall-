package cn.e3mall.serach.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.serach.SearchDao;
import cn.e3mall.serach.service.SearchService;
/*
 * 商品搜索service
 */
@Service
public class SearchServiceImpl implements SearchService {
	@Autowired
	private SearchDao searchDao;
	
	@Override
	public SearchResult search(String keyword, int page, int rows) throws Exception {
		//创建一个solrquery对象
		SolrQuery solrQuery=new SolrQuery();
		//设置查询条件
		solrQuery.setQuery(keyword);
		//设置分页条件
		if(page<=0) page=1;
		//设置默认搜索域
		solrQuery.set("df","item_title");
		//开启高亮显示
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("item_title");
		solrQuery.setHighlightSimplePost("<em style=\'color:red\'>");
		solrQuery.setHighlightSimplePre("</em>");
		//调用daotrue
		SearchResult search = searchDao.search(solrQuery);
		long count = search.getRecordCount();
		//计算总页数
		int totalpage=(int) (count/rows);
		if(totalpage%rows!=0)
			totalpage++;
		search.setTotalPages(totalpage);
		//返回结果solrQuery
		return search;
	}

}

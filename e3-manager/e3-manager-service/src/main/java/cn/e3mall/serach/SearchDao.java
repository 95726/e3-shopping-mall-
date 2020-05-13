package cn.e3mall.serach;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;

@Repository
public class SearchDao {
	@Autowired
	private SolrServer solrServer;
	
	
	//根据查询条件查询索引库
	public SearchResult search(SolrQuery solrQuery) throws Exception {
		//根据solrquery查询索引库
		QueryResponse queryResponse=solrServer.query(solrQuery);
		//取查询结果
		SolrDocumentList results = queryResponse.getResults();		
		//取查询结果总记录数
		long numFound = results.getNumFound();
		SearchResult result=new SearchResult();
		result.setRecordCount(numFound);
		//取商品列表，以及高亮显示
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		
		
		List<SearchItem> list=new ArrayList();
		for (SolrDocument solrDocument : results) {
			SearchItem item = new SearchItem();
			item.setId((String) solrDocument.get("id"));
			item.setCategory_name((String) solrDocument.get("item_category_name"));
			item.setImage((String) solrDocument.get("item_image"));
			item.setPrice((long) solrDocument.get("item_price"));
			item.setSell_point((String) solrDocument.get("item_sell_point"));
			// 取高亮显示
			List<String> lists = highlighting.get(solrDocument.get("id")).get("item_title");
			String title = "";
			if (list != null && list.size() > 0) {
				title = lists.get(0);
			} else {
				title = (String) solrDocument.get("item_title");
			}
			item.setTitle(title);
			list.add(item);
		}
		result.setItemList(list);
		//返回结果
		return result;
		
	}
}

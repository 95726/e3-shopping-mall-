package cn.e3mall.search.solrj;
//测试方法

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrJ {

	@Test
	public void solrclreat() throws SolrServerException, IOException {
		//创建SolrService对象，创建连接
		SolrServer  solrServer=new HttpSolrServer("http://192.168.25.132:8080/solr/collection1");
		//创建一个SolrInputDocument
		SolrInputDocument document = new SolrInputDocument();
		//向文档对象当中添加域。文档当中必须包含一个id域，所有域的名称必须在schema.xml当中定义
		document.addField("id", "dox01");
		document.addField("item_title", "测试商品01！");
		document.addField("item_price", 1000);
		//把文档写入索引库
		solrServer.add(document);
		//提交
		solrServer.commit();
	}
	
	@Test
	public void deletesolr() throws SolrServerException, IOException {
		//创建SolrService对象，创建连接
		SolrServer  solrServer=new HttpSolrServer("http://192.168.25.132:8080/solr/collection1");
		//删除文档
		solrServer.deleteByQuery("id:dox01");
		//提交
		solrServer.commit();
	}
}

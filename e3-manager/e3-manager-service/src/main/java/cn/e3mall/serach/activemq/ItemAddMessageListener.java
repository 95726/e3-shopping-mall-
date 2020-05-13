package cn.e3mall.serach.activemq;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.mapper.ItemMapper;

/*
 * 监听商品添加商品消息，监听到消息后，将对应的商品信息同步到索引库	
 */
public class ItemAddMessageListener implements MessageListener {
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer solrServer;
	
	
	@Override
	public void onMessage(Message message) {
		try {
			//1.从消息当中获取ID
			TextMessage textmessage=(TextMessage) message;
			String text = textmessage.getText();
			Long itemId = new Long(text);
			//等待事务提交
			Thread.sleep(1000);
			//根据商品Id查询商品信息
			SearchItem searchItem = itemMapper.getItemById(itemId);
			//3.创建一个文档对象
			SolrInputDocument document = new SolrInputDocument();
			//4.向文档对象当中添加域
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			//5.把文档对象写入索引库
			solrServer.add(document);
			//6.提交
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

}

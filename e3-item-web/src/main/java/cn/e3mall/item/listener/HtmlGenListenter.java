package cn.e3mall.item.listener;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
/*
 * 监听商品添加信息，生成静态页面
 */
public class HtmlGenListenter implements MessageListener{

	@Autowired
	private ItemService itemService;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Value("${HTML_GEN_HTML}")
	private String HTML_GEN_HTML;
	
	
	@Override
	public void onMessage(Message message) {
		
		try {
			//创建一个模板Jsp
			//从消息当中取id
			TextMessage textMessage=(TextMessage) message;
			String id=textMessage.getText();
			Long itemId=new Long(id);
			//等待事务提交
			Thread.sleep(1000);
			//根据商品Id查询，商品基本信息和描述信息
			//调用商品基本信息
			TbItem tbitem= itemService.getItemById(itemId);
			Item item = new Item(tbitem);
			//调用商品详情信息
			TbItemDesc itemDesc = itemService.getItemDesc(itemId);
			//创建一个数据集，封装商品数据
			Map data=new HashMap<>();
			data.put("item", item);
			data.put("itemDesc", itemDesc);
			//加载模板对象
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			//创建一个输出流，指定输出的目录及文件名
			Writer out=new FileWriter(HTML_GEN_HTML+itemId+".html");
			//生成静态页面
			template.process(data, out);
			//关闭流
			out.close();
		} catch (Exception e) {
			
		}

		
	}

}

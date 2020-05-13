package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.jedis.JedisClient;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;
import cn.e3mall.untils.E3Result;
import cn.e3mall.untils.IDUtils;
import cn.e3mall.untils.JsonUtils;
@Service
public class ItemServiceImpl implements ItemService {
	
	@Value("${REDIS_TIEM_PRE}")
	private String REDIS_TIEM_PRE;
	@Value("${ITEM_CACHE_EXPIRE}")
	private Integer ITEM_CACHE_EXPIRE;
	
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper  itemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private Destination topicDestination;
	@Autowired
	private JedisClient jedisClient;
	
	
	//根据ID查询数据
	@Override
	public TbItem getItemById(long itemId) {
		//查询缓存
		try {
			String json = jedisClient.get(REDIS_TIEM_PRE+":"+itemId+":BASE");
			if(StringUtils.isNotBlank(json)) {
				TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
				return item;
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//根据主键查询
		//TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
//				设置查询条件
		criteria.andIdEqualTo(itemId);
//				执行查询
		List<TbItem> list = itemMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			//把结果添加到缓存
			try {
				jedisClient.set(REDIS_TIEM_PRE+":"+itemId+":BASE",JsonUtils.objectToJson(list.get(0)));
				//设置过期时间
				jedisClient.expire(REDIS_TIEM_PRE+":"+itemId+":BASE", ITEM_CACHE_EXPIRE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list.get(0);
		}
		return null;
	}
	//分页功能
	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example=new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//创建一个返回值对象
		EasyUIDataGridResult result=new EasyUIDataGridResult();
		result.setRows(list);
		//获取分页结果
		PageInfo<TbItem> pageinfo=new PageInfo<>(list);
		result.setTotal(pageinfo.getTotal());
		//取总记录数
		return result;
	}
	
	//商品添加
	@Override
	public E3Result addItem(TbItem item, String desc) {
		 //生成商品ID
		final long id = IDUtils.genItemId();
		item.setId(id);
		//补全item商品属性
		item.setUpdated(new Date());
		item.setCreated(new Date());
		//'商品状态，1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		//向商品表当中插入数据
		itemMapper.insert(item);
		//创建一个商品描述pojo
		TbItemDesc itemdesc=new TbItemDesc();
		//补全属性
		itemdesc.setItemId(id);
		itemdesc.setCreated(new Date());
		itemdesc.setItemDesc(desc);
		itemdesc.setUpdated(new Date());
		//向商品描述表当中插入属性
		itemDescMapper.insert(itemdesc);
		//发送消息
		jmsTemplate.send(topicDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(id+"");
				return textMessage;
			}
		});
		//返回成功
		return E3Result.ok();
	}
	@Override
	public E3Result deleteList(Long[] ids) {
		for (Long id : ids) {
			itemMapper.deleteByPrimaryKey(id);
			itemDescMapper.deleteByPrimaryKey(id);
		}
		return E3Result.ok();
	}
	//下架
	@Override
	public E3Result updateInstock(Long[] ids) {
		
		for (Long id : ids) {
			TbItem tbItem=new TbItem();
			tbItem.setId(id);
			//'商品状态，1-正常，2-下架，3-删除',
			tbItem.setStatus((byte) 2);
			itemMapper.updateByPrimaryKeySelective(tbItem);
		}
		return E3Result.ok();
	}
	//上架
	@Override
	public E3Result updateReshelf(Long[] ids) {
		for (Long id : ids) {
			TbItem tbItem=new TbItem();
			tbItem.setId(id);
			//'商品状态，1-正常，2-下架，3-删除',
			tbItem.setStatus((byte) 1);
			itemMapper.updateByPrimaryKeySelective(tbItem);
		}
		return E3Result.ok();
	}
	//回显商品描述数据
	@Override
	public TbItemDesc echoItemDesc(Long id) {
		TbItemDesc desc = itemDescMapper.selectByPrimaryKey(id);
		return desc;
	}
	
	//获取商品详情页面
	@Override
	public TbItemDesc getItemDesc(long itemId) {
		//查询缓存
		try {
			String json = jedisClient.get(REDIS_TIEM_PRE+":"+itemId+":DESC");
			if(StringUtils.isNotBlank(json)) {
				TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return itemDesc;
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemDesc desc = itemDescMapper.selectByPrimaryKey(itemId);
		//把结果添加到缓存
		try {
			jedisClient.set(REDIS_TIEM_PRE+":"+itemId+":DESC",JsonUtils.objectToJson(desc));
			//设置过期时间
			jedisClient.expire(REDIS_TIEM_PRE+":"+itemId+":DESC", ITEM_CACHE_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

}

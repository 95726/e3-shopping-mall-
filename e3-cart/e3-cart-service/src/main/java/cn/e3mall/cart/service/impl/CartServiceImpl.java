package cn.e3mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.jedis.JedisClient;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.untils.E3Result;
import cn.e3mall.untils.JsonUtils;
/*
 * 登陆时购物车的处理
 */
@Service
public class CartServiceImpl implements CartService {
	
	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;
	
	@Autowired
	private TbItemMapper tbItemMapper;
	@Autowired
	private JedisClient jedisClient;
	@Override
	public E3Result addCart(long userId, long itemId,int num) {
		//向redis中添加购物车
		//数据类型是hash key :用户id field:商品id value:商品信息
		//判断商品是否存在
		Boolean hexists = jedisClient.hexists(REDIS_CART_PRE+":"+userId,itemId+"");
		//如果存在数量相加
		if(hexists) {
			String json = jedisClient.hget(REDIS_CART_PRE+":"+userId,itemId+"");
			//把json转换成Tbitem
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			item.setNum(item.getNum()+num);
			//写回redis
			jedisClient.hset(REDIS_CART_PRE+":"+userId,itemId+"", JsonUtils.objectToJson(item));
			return E3Result.ok();
		}
		//不存在，根据商品Id取商品信息
		TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
		tbItem.setNum(num);
		//取一张图片
		String image=tbItem.getImage();
		if(StringUtils.isNotBlank(image)) {
			tbItem.setImage(image.split(",")[0]);
		}
		//添加到购物车列表
		jedisClient.hset(REDIS_CART_PRE+":"+userId,itemId+"", JsonUtils.objectToJson(tbItem));
		//返回成功
		return E3Result.ok();
	}
	
	
	@Override
	public E3Result mergeCart(long userId, List<TbItem> itemList) {
		//遍历商品列表
		//把列表添加到购物车。
		//判断购物车中是否有此商品
		//如果有，数量相加
		//如果没有添加新的商品
		for (TbItem tbItem : itemList) {
			addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		//返回成功
		return E3Result.ok();
	}


	@Override
	public List<TbItem> getCartList(long userId) {
		//根据用户id查询购物车列表
		List<String> list = jedisClient.hvals(REDIS_CART_PRE+":"+userId);
		List<TbItem> itemlist=new ArrayList<>();
		for (String tbItem : list) {
			TbItem item = JsonUtils.jsonToPojo(tbItem, TbItem.class);
			itemlist.add(item);
		}
		return itemlist;
	}


	@Override
	public E3Result updateCartNum(long userId, long itemId, int num) {
		//从redis当中取数据
		String json = jedisClient.hget(REDIS_CART_PRE+":"+userId,itemId+"");
		TbItem tbitem = JsonUtils.jsonToPojo(json, TbItem.class);
		tbitem.setNum(num);
		//写入redis
		jedisClient.hset(REDIS_CART_PRE+":"+userId,itemId+"", JsonUtils.objectToJson(tbitem));
		return E3Result.ok();
	}


	@Override
	public E3Result deleteCartItem(long userId, long itemId) {
		//删除购物车商品
		jedisClient.hdel(REDIS_CART_PRE+":"+userId,itemId+"");
		jedisClient.del("ITEM_INFO:"+itemId+":DESC");
		jedisClient.del("ITEM_INFO:"+itemId+":BASE");
		return E3Result.ok();
	}

	/*
	 * 删除用户购物车
	 * (non-Javadoc)
	 * @see cn.e3mall.cart.service.CartService#clearCartItem(long)
	 */
	@Override
	public E3Result clearCartItem(long userId) {
		jedisClient.del(REDIS_CART_PRE+":"+userId);
		return E3Result.ok();
	}

}

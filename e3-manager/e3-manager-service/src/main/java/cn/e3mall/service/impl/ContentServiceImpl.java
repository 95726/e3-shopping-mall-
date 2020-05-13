package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.jedis.JedisClient;
import cn.e3mall.jedis.JedisClientCluster;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;
import cn.e3mall.service.ContentService;
import cn.e3mall.untils.E3Result;
import cn.e3mall.untils.JsonUtils;
import redis.clients.jedis.JedisPool;
@Service
public class ContentServiceImpl implements ContentService {
	//插入content数据
	@Autowired
	private TbContentMapper tbContentMapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;
	@Override
	public E3Result addContent(TbContent tbContent) {
		//数据添加完整
		tbContent.setUpdated(new Date());
		tbContent.setCreated(new Date());
		//插入到数据库
		tbContentMapper.insert(tbContent);
		//缓存同步
		jedisClient.hdel(CONTENT_LIST,tbContent.getCategoryId().toString());
		return E3Result.ok();
	}
	//根据类容分类id获取数据列表
	@Override
	public List<TbContent> getContentListByCid(long cid) {
		//查缓存
		try {
			//如果缓存当中存在
			String json = jedisClient.hget(CONTENT_LIST, cid+"");
			if (StringUtils.isNotBlank(json)) {
				List<TbContent> list = JsonUtils.jsonToList(json,TbContent.class);
				return list;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		TbContentExample example=new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(cid);
		//获取数据
		List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(example);
		//把结果添加到缓存
		try {
			jedisClient.hset(CONTENT_LIST, cid+"", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

}

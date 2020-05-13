package cn.e3mall.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.jedis.JedisClient;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;
import cn.e3mall.untils.E3Result;
import cn.e3mall.untils.JsonUtils;
@Service
public class TokenServiceImpl implements TokenService {
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_TIME}")
	private Integer SESSION_TIME;
	@Override
	public E3Result getUserByToken(String token) {
		//根据token到redis当中查找用户信息
		String json = jedisClient.get("SESSION:"+token);
		//取不到用户信息，登录已经过期，返回登录过期
		if(StringUtils.isBlank(json)) {
			return E3Result.build(201, "登录已过期,重新登录！");
		}
		//取到数据更新token过期时间
		jedisClient.expire("SESSION:"+token, SESSION_TIME);
		//返回结果对象
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		return E3Result.ok(user);
	}

}

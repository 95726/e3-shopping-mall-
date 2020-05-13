package cn.e3mall.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.jedis.JedisClientPool;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.LoginService;
import cn.e3mall.untils.E3Result;
import cn.e3mall.untils.JsonUtils;

/*
 * 用户登录服务
 */
@Service
public class LoginServiceImpl implements LoginService{
	@Autowired
	private TbUserMapper tbUserMapper;
	@Autowired
	private JedisClientPool jedisClientPool;
	
	@Value("${SESSION_TIME}")
	private Integer SESSION_TIME;

	@Override
	public E3Result login(String username, String password) {
//		1、判断用户名密码是否正确。
		TbUserExample example=new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = tbUserMapper.selectByExample(example);
		if(list==null || list.size()==0) {
			//登录失败
			return E3Result.build(400, "用户名或密码错误！");
		}
		//取用户信息
		TbUser tbUser = list.get(0);
		//判断用户密码是否正确
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(tbUser.getPassword())) {
			//登录失败
			return E3Result.build(400, "用户名或密码错误！");
		}
		
		
//		2、登录成功后生成token。Token相当于原来的jsessionid，字符串，可以使用uuid。
		String uuid = UUID.randomUUID().toString();
//		3、把用户信息保存到redis。Key就是token，value就是TbUser对象转换成json。
		tbUser.setPassword(null);
		jedisClientPool.set("SESSION:"+uuid,JsonUtils.objectToJson(tbUser));	
//		5、设置key的过期时间。模拟Session的过期时间。一般半个小时。
		jedisClientPool.expire("SESSION:"+uuid, SESSION_TIME);
//		6、返回e3Result包装token。
		return E3Result.ok(uuid);
	}

}

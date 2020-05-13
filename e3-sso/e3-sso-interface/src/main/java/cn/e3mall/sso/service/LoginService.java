package cn.e3mall.sso.service;

import cn.e3mall.untils.E3Result;

/*
 * 用户登录服务
 */
public interface LoginService {
	/*
	 *1.、判断用户名密码是否正确。
	 *2、登录成功后生成token。Token相当于原来的jsessionid，字符串，可以使用uuid。
	 *3、把用户信息保存到redis。Key就是token，value就是TbUser对象转换成json。
	 *4、使用String类型保存Session信息。可以使用“前缀:token”为key
	 *5、设置key的过期时间。模拟Session的过期时间。一般半个小时。
	 *6、返回e3Result包装token。
	 */
	public E3Result login(String username, String password);
}

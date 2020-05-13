package cn.e3mall.jdestest;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.jedis.JedisClientCluster;
import cn.e3mall.jedis.JedisClientPool;

public class JedisClientTest {
	@Test
	public void jedisClientTest1() {
		//初始化Spring容器
		ApplicationContext applicationContext=new  ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		//获取bean对象
		JedisClientPool jedisClientPool = applicationContext.getBean(JedisClientPool.class);
		jedisClientPool.set("jedis", "kakdad");
		String jedis = jedisClientPool.get("jedis");
		System.out.println(jedis);
	}
	
	public void jedisClientTest2() {
		//初始化Spring容器
		ApplicationContext applicationContext=new  ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		//获取bean对象
		 JedisClientCluster jedisClientCluster = applicationContext.getBean(JedisClientCluster.class);
		 jedisClientCluster.set("jedis", "kakdad");
		String jedis = jedisClientCluster.get("jedis");
		System.out.println(jedis);
	}
}

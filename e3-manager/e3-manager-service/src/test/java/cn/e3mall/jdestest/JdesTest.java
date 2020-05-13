package cn.e3mall.jdestest;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

public class JdesTest {
	@Test
	public void jdestest1() {
		//创建jds
		Jedis jedis=new Jedis("192.168.25.128", 6379);
		//使用Jedis方法操做数据库
		jedis.set("test1", "ahsajsjakka");
		String test = jedis.get("test1");
		System.out.println(test);
		//释放资源
		jedis.close();
	}
	@Test
	public void testjedisCluster() {
		//创建一个jedisCluster对象。有一个参数是set类型set当中包含HostandPort
		Set<HostAndPort> nodes=new HashSet();
		nodes.add(new HostAndPort("192.168.25.128", 7001));
		nodes.add(new HostAndPort("192.168.25.128", 7002));
		nodes.add(new HostAndPort("192.168.25.128", 7003));
		nodes.add(new HostAndPort("192.168.25.128", 7004));
		nodes.add(new HostAndPort("192.168.25.128", 7005));
		nodes.add(new HostAndPort("192.168.25.128", 7006));
		//使用JedisCluster对象操作redis
		JedisCluster jedisCluster = new JedisCluster(nodes);
		jedisCluster.set("test1","123");
		String string = jedisCluster.get("test1");
		System.out.println(string);
		//关闭JedisCluster
		jedisCluster.close();
		
	}
	
}

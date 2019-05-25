package com.jd.order.springconfig;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@PropertySource(value = "classpath:redis.properties")
public class RedisSpringConfig {

	@Value("${spring.redis.host}")
	private String host;
	@Value("${spring.redis.port}")
	private int port;
	@Value("${spring.redis.host1}")
	private String host1;
	@Value("${spring.redis.port1}")
	private int port1;
	@Value("${spring.redis.host2}")
	private String host2;
	@Value("${spring.redis.port2}")
	private int port2;
	@Value("${spring.redis.host3}")
	private String host3;
	@Value("${spring.redis.port3}")
	private int port3;
	@Value("${spring.redis.host4}")
	private String host4;
	@Value("${spring.redis.port4}")
	private int port4;
	@Value("${spring.redis.host5}")
	private String host5;
	@Value("${spring.redis.port5}")
	private int port5;
	@Value("${spring.redis.host6}")
	private String host6;
	@Value("${spring.redis.port6}")
	private int port6;

	// 使用默认配置
	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		return jedisPoolConfig;
	}

	/* jedis客户端单机版 */
	@Bean
	public JedisPool redisClient() {
		JedisPool redisClient = new JedisPool(jedisPoolConfig(), host, port);
		return redisClient;
	}
	
	/*集群版整合*/
/*	@Bean
	public JedisCluster redisClient() {
		HostAndPort node1=new HostAndPort(host1, port1);
		HostAndPort node2=new HostAndPort(host2, port2);
		HostAndPort node3=new HostAndPort(host3, port3);
		HostAndPort node4=new HostAndPort(host4, port4);
		HostAndPort node5=new HostAndPort(host5, port5);
		HostAndPort node6=new HostAndPort(host6, port6);
		Set<HostAndPort> nodes=new HashSet<>();
		nodes.add(node1);
		nodes.add(node2);
		nodes.add(node3);
		nodes.add(node4);
		nodes.add(node5);
		nodes.add(node6);
		JedisCluster jedisCluster=new JedisCluster(nodes,jedisPoolConfig());
		return jedisCluster;
	}*/
}

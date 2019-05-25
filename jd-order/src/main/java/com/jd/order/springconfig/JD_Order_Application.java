package com.jd.order.springconfig;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
@PropertySource(value = { "classpath:jdbc.properties", "classpath:resource.properties" })
@ComponentScan(basePackages = "com.jd.order")
@SpringBootApplication
public class JD_Order_Application {
	@Value("${jdbc.url}")
	private String url;

	@Value("${jdbc.driver}")
	private String driver;

	@Value("${jdbc.username}")
	private String username;

	@Value("${jdbc.password}")
	private String password;

	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		// 数据库驱动
		dataSource.setDriverClassName(driver);
		// 相应的jdbcurl
		dataSource.setUrl(url);
		// 数据库用户名
		dataSource.setUsername(username);
		// 数据库密码
		dataSource.setPassword(password);

		dataSource.setMaxActive(10);

		dataSource.setMinIdle(5);

		return dataSource;
	}

	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(JD_Order_Application.class);
	}
}

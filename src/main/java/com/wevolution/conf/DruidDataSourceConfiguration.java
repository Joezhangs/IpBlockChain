package com.wevolution.conf;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.pool.DruidDataSource;
/**
* @ClassName: DruidDataSourceConfiguration 
* @Description: Druid数据源配置
* @author jiangjian
* @date 2017年8月15日 下午8:55:05 
*
 */
@Configuration
public class DruidDataSourceConfiguration {
	private Logger logger = LoggerFactory.getLogger(DruidDataSourceConfiguration.class);
	
    @Value("${spring.datasource.url}")
    private String dbUrl;
    
    @Value("${spring.datasource.username}")
    private String username;
    
    @Value("${spring.datasource.password}")
    private String password;
    
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;
    
    @Value("${spring.datasource.initialSize}")
    private int initialSize;
    
    @Value("${spring.datasource.minIdle}")
    private int minIdle;
    
    @Value("${spring.datasource.maxActive}")
    private int maxActive;
    
    @Value("${spring.datasource.maxWait}")
    private int maxWait;
    
    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;
    
    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;
    
    @Value("${spring.datasource.validationQuery}")
    private String validationQuery;
    
    @Value("${spring.datasource.testWhileIdle}")
    private boolean testWhileIdle;
    
    @Value("${spring.datasource.testOnBorrow}")
    private boolean testOnBorrow;
    
    @Value("${spring.datasource.testOnReturn}")
    private boolean testOnReturn;
    
    @Value("${spring.datasource.poolPreparedStatements}")
    private boolean poolPreparedStatements;
    
    @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;
    
    @Value("${spring.datasource.filters}")
    private String filters;
    
    @Value("{spring.datasource.connectionProperties}")
    private String connectionProperties;
    
    @Bean(name="dataSource",initMethod="init",destroyMethod="close")     //声明其为Bean实例
    @Primary  //在同样的DataSource中，首先使用被标注的DataSource
    public DataSource dataSource(){
    	DruidDataSource datasource = new DruidDataSource();
    	
    	datasource.setUrl(this.dbUrl);
    	datasource.setUsername(username);
    	datasource.setPassword(password);
    	datasource.setDriverClassName(driverClassName);
    	
    	//configuration
    	datasource.setInitialSize(initialSize);
    	datasource.setMinIdle(minIdle);
    	datasource.setMaxActive(maxActive);
    	datasource.setMaxWait(maxWait);
    	datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
    	datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
    	datasource.setValidationQuery(validationQuery);
    	datasource.setTestWhileIdle(testWhileIdle);
    	datasource.setTestOnBorrow(testOnBorrow);
    	datasource.setTestOnReturn(testOnReturn);
    	datasource.setPoolPreparedStatements(poolPreparedStatements);
    	datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
    	try {
			datasource.setFilters(filters);
		} catch (SQLException e) {
			logger.error("druid configuration initialization filter", e);
		}
    	datasource.setConnectionProperties(connectionProperties);
    	
    	return datasource;
    }
//    @Bean
//	public ServletRegistrationBean druidServlet() {
//		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
//		servletRegistrationBean.setServlet(new StatViewServlet());
//		servletRegistrationBean.addUrlMappings("/druid/*");
//		Map<String, String> initParameters = new HashMap<String, String>();
//		// initParameters.put("loginUsername", "druid");// 用户名
//		// initParameters.put("loginPassword", "druid");// 密码
//		initParameters.put("resetEnable", "false");// 禁用HTML页面上的“Reset All”功能
//		initParameters.put("allow", "127.0.0.1"); // IP白名单 (没有配置或者为空，则允许所有访问)
//		// initParameters.put("deny", "192.168.20.38");// IP黑名单
//		// (存在共同时，deny优先于allow)
//		servletRegistrationBean.setInitParameters(initParameters);
//		return servletRegistrationBean;
//	}
//
//	@Bean
//	public FilterRegistrationBean filterRegistrationBean() {
//		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
//		filterRegistrationBean.setFilter(new WebStatFilter());
//		filterRegistrationBean.addUrlPatterns("/*");
//		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
//		return filterRegistrationBean;
//	}

}

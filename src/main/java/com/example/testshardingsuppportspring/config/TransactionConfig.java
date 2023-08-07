package com.example.testshardingsuppportspring.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TransactionConfig {
	@Bean
	@Primary
	public PlatformTransactionManager transactionManager(
			@Qualifier("catalogDataSource") DataSource proxyDataSource) {
		return new JdbcTransactionManager(proxyDataSource);
	}

	@Bean
	public PlatformTransactionManager directRoutingTransactionManager(
			@Qualifier("shardingKeyDataSourceAdapter") DataSource gsmDataSource) {
		return new JdbcTransactionManager(gsmDataSource);
	}
}

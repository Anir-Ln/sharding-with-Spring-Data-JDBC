package com.example.testshardingsuppportspring.config;

import java.sql.SQLException;
import java.sql.ShardingKey;

import javax.sql.DataSource;

import oracle.jdbc.OracleType;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.ShardingKeyProvider;
import org.springframework.jdbc.datasource.ShardingKeyDataSourceAdapter;

@Configuration
public class DaoConfig {
	final String USERNAME = "testuser1";
	final String PASSWORD = "testuser1";


	String makeUrl(String port, String service) {
		return "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(HOST=" + "phoenix254768.dev3sub3phx.databasede3phx.oraclevcn.com" + ")(PORT=" + port + ")(PROTOCOL=tcp))(CONNECT_DATA=(SERVICE_NAME=" + service + ")(REGION=east)))";
	}

	@Bean
	@Primary
	public DataSource catalogDataSource() throws SQLException {
		PoolDataSource catalog = PoolDataSourceFactory.getPoolDataSource();
		catalog.setURL(makeUrl("5225", "GDS$CATALOG.oradbcloud"));
		catalog.setUser(USERNAME);
		catalog.setPassword(PASSWORD);
		catalog.setConnectionPoolName("catalog_pool");
		catalog.setInitialPoolSize(7);
		catalog.setMinPoolSize(5);
		catalog.setMaxPoolSize(13);
		catalog.setMaxConnectionsPerShard(10);
		catalog.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");

		return catalog;
	}
	@Bean
	public DataSource gsmDataSource() throws SQLException {
		final String service = "wsvc.shpool.oradbcloud";
		final String port = "1921";

		PoolDataSource gsm = PoolDataSourceFactory.getPoolDataSource();
		gsm.setURL(makeUrl(port, service));
		gsm.setUser(USERNAME);
		gsm.setPassword(PASSWORD);
		gsm.setConnectionPoolName("gsm_pool");
		gsm.setInitialPoolSize(7);
		gsm.setMinPoolSize(5);
		gsm.setMaxPoolSize(13);
		gsm.setMaxConnectionsPerShard(10);
		gsm.setShardingMode(true);
		gsm.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");

		return gsm;
	}


	@Bean
	public ShardingKeyProvider getShardingKeyProvider() {
		return new ShardingKeyProvider() {
			@Override
			public ShardingKey getShardingKey() throws SQLException {
				// return the department_id of the authenticated employee
				// for testing let's say the authenticated employee's department_id is 20;
				Long department_id = 20L;
				return gsmDataSource().createShardingKeyBuilder().subkey(department_id, OracleType.NUMBER).build();
			}

			@Override
			public ShardingKey getSuperShardingKey() {
				return null;
			}
		};
	}

	@Bean
	public DataSource shardingKeyDataSourceAdapter() throws SQLException {
		ShardingKeyDataSourceAdapter adapter = new ShardingKeyDataSourceAdapter(gsmDataSource());
		adapter.setShardingKeyProvider(getShardingKeyProvider());
		return adapter;
	}

}

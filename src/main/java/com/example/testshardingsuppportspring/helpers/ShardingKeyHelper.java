package com.example.testshardingsuppportspring.helpers;

import java.sql.SQLException;
import java.sql.ShardingKey;
import java.sql.ShardingKeyBuilder;

import javax.sql.DataSource;

import oracle.jdbc.OracleType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ShardingKeyHelper {
	@Autowired
	@Qualifier("gsmDataSource")
	DataSource dataSource;

	public ShardingKey create(ShardingSubKey... subKeys) {
		try {
			ShardingKeyBuilder builder = dataSource.createShardingKeyBuilder();
			for(ShardingSubKey subKey: subKeys) {
				builder.subkey(subKey.subKey, subKey.subKeyType);
			}
			return builder.build();
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public ShardingKey create(Object subKey, OracleType subKeyType) {
		try {
			return dataSource.createShardingKeyBuilder().subkey(subKey, subKeyType).build();
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private class ShardingSubKey {
		Object subKey;
		OracleType subKeyType;
	}
}

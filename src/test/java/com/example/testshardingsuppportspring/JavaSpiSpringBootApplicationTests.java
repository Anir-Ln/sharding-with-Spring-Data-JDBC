package com.example.testshardingsuppportspring;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.example.testshardingsuppportspring.Domain.Department;
import com.example.testshardingsuppportspring.helpers.ShardingKeyHelper;
import com.example.testshardingsuppportspring.repositories.DepartmentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JavaSpiSpringBootApplicationTests {
	@Autowired
	DepartmentRepository departmentRepository;
	@Autowired
	ShardingKeyHelper shardingKeyHelper;
	@Autowired
	@Qualifier("gsmDataSource")
	DataSource dataSource;
	Department expected;

	@Autowired
	@Qualifier("catalogDataSource")
	DataSource catalogDataSource;

	@BeforeEach
	void setup() {
		expected = new Department();
		expected.setDepartmentId(20L);
		expected.setDepartmentName("Marketing");
		expected.setManagerId(201L);
		expected.setLocationId(1800L);
	}

	@Test
	void testConnection() throws SQLException {
		Connection con = catalogDataSource.createConnectionBuilder().build();
		System.out.println(con);
	}

	@Test
	void testCatalogConnection() throws SQLException {
		Connection con = catalogDataSource.createConnectionBuilder().build();
		System.out.println(con);
	}

	@Test
	void test1() {
		Department department = departmentRepository.findByDepartmentId(20L);
		Assertions.assertEquals(expected, department);
	}

	@Test
	void test2() {
		List<Department> departments = departmentRepository.findAll();
		assert departments.size() > 5;
	}
}

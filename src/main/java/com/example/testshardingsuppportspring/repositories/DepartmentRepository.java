package com.example.testshardingsuppportspring.repositories;

import java.util.List;

import com.example.testshardingsuppportspring.Domain.Department;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DepartmentRepository extends CrudRepository<Department, Long> {
	@Transactional(transactionManager = "directRoutingTransactionManager")
	@Query("select * from departments where department_id = :department_id")
	Department findByDepartmentId(@Param("department_id") Long id);

	@Query("select * from departments")
	List<Department> findAll();
}


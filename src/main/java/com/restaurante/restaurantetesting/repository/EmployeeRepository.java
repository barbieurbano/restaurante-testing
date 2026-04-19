package com.restaurante.restaurantetesting.repository;

import com.restaurante.restaurantetesting.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
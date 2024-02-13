package com.user.usermgmt.repository;

import com.user.usermgmt.model.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("employeeRepository")
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    List<Employee> findAllBy(Pageable pageable);
}

package com.user.usermgmt.service;

import com.user.usermgmt.model.Employee;
import com.user.usermgmt.model.dto.CreateEmployeeDto;
import com.user.usermgmt.model.dto.UpdateEmployeeDto;

import java.util.List;

public interface IEmployee {
    Employee createNew(CreateEmployeeDto employee);
    List<Employee> findAll(int page);
    Employee findById(String id);
    Employee update(UpdateEmployeeDto updateEmployeeDto);
    Boolean deleteById(String id);
}

package com.user.usermgmt.service;

import com.user.usermgmt.model.Employee;
import com.user.usermgmt.model.dto.CreateEmployeeDto;
import com.user.usermgmt.model.dto.UpdateEmployeeDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IEmployee {
    Employee createNew(CreateEmployeeDto employee);
    CompletableFuture<List<Employee>> findAll(int page);
    Employee findById(String id);
    Employee update(UpdateEmployeeDto updateEmployeeDto);
    Boolean deleteById(String id);
}

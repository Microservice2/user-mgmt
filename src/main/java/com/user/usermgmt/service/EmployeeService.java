package com.user.usermgmt.service;

import com.user.usermgmt.model.Employee;
import com.user.usermgmt.model.dto.CreateEmployeeDto;
import com.user.usermgmt.model.dto.UpdateEmployeeDto;
import com.user.usermgmt.repository.EmployeeRepository;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class EmployeeService implements IEmployee {

    private static final int SIZE = 10;
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee createNew(CreateEmployeeDto employeeDto) {

        RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange(33, 45)
                .build();
        final int PASS_LENGTH = 32;
        Employee newEmployee = new Employee();
        newEmployee.setDepartmentId(employeeDto.getDepartmentId());
        newEmployee.setFirstName(employeeDto.getFirstName());
        newEmployee.setLastName(employeeDto.getLastName());
        newEmployee.setFullName(employeeDto.getFirstName()+" "+employeeDto.getLastName());
        newEmployee.setPosition(employeeDto.getPosition());
        newEmployee.setAge(employeeDto.getAge());
        newEmployee.setUsername(employeeDto.getFirstName()+"_"+employeeDto.getLastName());
        newEmployee.setPassword(pwdGenerator.generate(PASS_LENGTH));

        return employeeRepository.save(newEmployee);
    }

    @Override
    @Async("userTaskExecutor")
    public CompletableFuture<List<Employee>> findAll(int page) {
        Pageable paging = PageRequest.of(page, SIZE);
        CompletableFuture<List<Employee>> employees = CompletableFuture.supplyAsync(() -> employeeRepository.findAllBy(paging));
        try {
            return CompletableFuture.completedFuture(employees.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Employee findById(String id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public Employee update(UpdateEmployeeDto updateEmployeeDto) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(updateEmployeeDto.getId());
        if(optionalEmployee.isPresent()){
            Employee employee = optionalEmployee.get();
            employee.setAge(updateEmployeeDto.getAge());
            employee.setFirstName(updateEmployeeDto.getFirstName());
            employee.setLastName(updateEmployeeDto.getLastName());
            employee.setFullName(updateEmployeeDto.getFullName());

            return employeeRepository.save(employee);
        }
        return null;
    }

    @Override
    public Boolean deleteById(String id) {
        if(employeeRepository.existsById(id)){
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

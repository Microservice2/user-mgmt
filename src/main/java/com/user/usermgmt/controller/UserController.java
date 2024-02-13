package com.user.usermgmt.controller;

import com.user.usermgmt.model.Employee;
import com.user.usermgmt.model.dto.CreateEmployeeDto;
import com.user.usermgmt.model.dto.ResponseDto;
import com.user.usermgmt.model.dto.UpdateEmployeeDto;
import com.user.usermgmt.service.IEmployee;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final IEmployee employeeService;

    public UserController(IEmployee employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/createNew")
    public ResponseEntity<ResponseDto> createNewUser(CreateEmployeeDto data) {
        Employee newEmployee = employeeService.createNew(data);
        if (newEmployee == null) {
            return new ResponseEntity<>(new ResponseDto("Failed insert employee", HttpStatus.INTERNAL_SERVER_ERROR.toString()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseDto("Successful insert employee", HttpStatus.OK.toString()),
                HttpStatus.OK);
    }

    @GetMapping("/getEmployees")
    public ResponseEntity<ResponseDto> getEmployees(@RequestParam(defaultValue = "0") int page) {
        List<Employee> employees = employeeService.findAll(page);
        if (employees == null) {
            return new ResponseEntity<>(new ResponseDto("Employee data is empty", HttpStatus.INTERNAL_SERVER_ERROR.toString()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseDto("Success", employees),
                HttpStatus.OK);
    }

    @GetMapping("/getDetailEmployeeById/{id}")
    public ResponseEntity<ResponseDto> getDetailEmployeeById(@PathVariable("id") String id) {
        Employee employee = employeeService.findById(id);
        if (employee == null) {
            return new ResponseEntity<>(new ResponseDto("Employee not found", HttpStatus.INTERNAL_SERVER_ERROR.toString()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseDto("Success", employee),
                HttpStatus.OK);
    }

    @DeleteMapping("/deleteEmployeeById/{id}")
    public ResponseEntity<ResponseDto> deleteEmployeeById(@PathVariable("id") String id) {
        if(!employeeService.deleteById(id)) {
            return new ResponseEntity<>(new ResponseDto("Failed delete employee", HttpStatus.INTERNAL_SERVER_ERROR.toString()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseDto("Successfully deleted employee"),
                HttpStatus.OK);
    }

    @PutMapping("/updateEmployee")
    public ResponseEntity<ResponseDto> updateEmployee(@RequestBody UpdateEmployeeDto updateDepartmentDto) {
        Employee employee = employeeService.update(updateDepartmentDto);
        if(employee == null) {
            return new ResponseEntity<>(new ResponseDto("Failed update employee", HttpStatus.INTERNAL_SERVER_ERROR.toString()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseDto("Successfully deleted employee", employee),
                HttpStatus.OK);
    }
}

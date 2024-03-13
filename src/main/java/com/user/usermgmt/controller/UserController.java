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
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final IEmployee employeeService;

    public UserController(IEmployee employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/createNew")
    @PreAuthorize("hasRole('admin') and hasAuthority('scope_admin_write')")
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
    @PreAuthorize("hasAnyRole('normal', 'admin') and hasAnyAuthority('scope_admin_read', 'scope_normal_read')")
    public ResponseEntity<ResponseDto> getEmployees(@RequestParam(defaultValue = "0") int page) {
        List<Employee> employees = null;
        try {
            employees = employeeService.findAll(page).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        if (employees == null) {
            return new ResponseEntity<>(new ResponseDto("Employee data is empty", HttpStatus.INTERNAL_SERVER_ERROR.toString()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseDto("Success", employees),
                HttpStatus.OK);
    }

    @GetMapping("/getEmployeeById/{id}")
    @PreAuthorize("hasAnyRole('normal', 'admin') and hasAnyAuthority('scope_admin_read', 'scope_normal_read')")
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
    @PreAuthorize("hasRole('admin') and hasAuthority('scope_admin_delete')")
    public ResponseEntity<ResponseDto> deleteEmployeeById(@PathVariable("id") String id) {
        if(!employeeService.deleteById(id)) {
            return new ResponseEntity<>(new ResponseDto("Failed delete employee", HttpStatus.INTERNAL_SERVER_ERROR.toString()),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return new ResponseEntity<>(new ResponseDto("Successfully deleted employee"),
                HttpStatus.OK);
    }

    @PutMapping("/updateEmployee")
    @PreAuthorize("hasAnyRole('normal', 'admin') and hasAnyAuthority('scope_admin_update', 'scope_normal_update')")
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

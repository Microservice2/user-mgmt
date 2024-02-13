package com.user.usermgmt.model.dto;

import com.user.usermgmt.model.Employee;
import lombok.Data;

import java.util.List;

@Data
public class ResponseDto {
    private String message;
    private String status;
    private Employee employee;
    private List<Employee> employees;

    public ResponseDto(String message) {
        this.message = message;
    }

    public ResponseDto(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public ResponseDto(String status, Employee employee) {
        this.status = status;
        this.employee = employee;
    }

    public ResponseDto(String status, List<Employee> employees) {
        this.status = status;
        this.employees = employees;
    }
}

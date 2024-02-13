package com.user.usermgmt.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeDto {
    private String departmentId;
    private String firstName;
    private String lastName;
    private int age;
    private String position;
}

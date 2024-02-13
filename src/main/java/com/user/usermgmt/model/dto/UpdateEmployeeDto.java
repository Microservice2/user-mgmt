package com.user.usermgmt.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeDto {
    private String id;
    private String firstName;
    private String lastName;
    private String fullName;
    private int age;
    private String position;
}

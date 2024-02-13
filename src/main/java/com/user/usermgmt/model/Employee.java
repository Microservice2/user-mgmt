package com.user.usermgmt.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "tbl_employee")
public class Employee {

    @Id
    @UuidGenerator
    private String id;

    @Column(name = "department_id")
    private String departmentId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "age")
    private int age;

    @Column(name = "position")
    private String position;

    @Column(name = "user_name")
    private String username;

    @Column(name = "password")
    private String password;
}


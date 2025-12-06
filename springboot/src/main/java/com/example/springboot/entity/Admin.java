package com.example.springboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin extends Account {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String role;
}

package com.example.springboot.service;

import com.example.springboot.entity.Account;
import com.example.springboot.entity.Employee;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface EmployeeService {

    List<Employee> selectAll(Employee employee);

    Employee selectById(Integer id);

    PageInfo<Employee> selectPage(Employee employee ,Integer pageNum, Integer pageSize);

    void add(Employee employee);

    void update(Employee employee);


    void deleteById(Integer id);

    void deleteBatch(List<Integer> ids);

    Employee login(Account account);

    void register(Employee employee);

    void updatePassword(Account account);
}

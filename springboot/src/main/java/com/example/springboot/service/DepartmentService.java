package com.example.springboot.service;

import com.example.springboot.entity.Department;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface DepartmentService {

    List<Department> selectAll(Department department);

    Department selectById(Integer id);

    PageInfo<Department> selectPage(Department department ,Integer pageNum, Integer pageSize);

    void add(Department department);

    void update(Department department);


    void deleteById(Integer id);

    void deleteBatch(List<Integer> ids);
}

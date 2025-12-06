package com.example.springboot.mapper;

import com.example.springboot.entity.Department;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DepartmentMapper {

    // 通过在mapper.xml文件中编写sql语句
    List<Department> selectAll(Department department);

    // 通过注解的形式编写sql语句。只适合比较简单的sql语句
    @Select("select * from department where id = #{id}")
    Department selectById(Integer id);

    void insert(Department department);

    void updateById(Department department);

    @Delete("DELETE FROM `department` WHERE id = #{id}")
    void deleteById(Integer id);
}

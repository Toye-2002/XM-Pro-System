package com.example.springboot.mapper;

import com.example.springboot.entity.Employee;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface EmployeeMapper {

    // 通过在mapper.xml文件中编写sql语句
    List<Employee> selectAll(Employee employee);

    // 通过注解的形式编写sql语句。只适合比较简单的sql语句
    @Select("select * from employee where id = #{id}")
    Employee selectById(Integer id);

    void insert(Employee employee);

    void updateById(Employee employee);

    @Delete("DELETE FROM `employee` WHERE id = #{id}")
    void deleteById(Integer id);

    @Select("select * from `employee` where username = #{username}")
    Employee selectByUsername(String username);

    @Select("select * from `employee` where no = #{no}")
    Employee selectByNo(String no);
}

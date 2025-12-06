package com.example.springboot.mapper;

import com.example.springboot.entity.Admin;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AdminMapper {

    // 通过在mapper.xml文件中编写sql语句
    List<Admin> selectAll(Admin admin);

    // 通过注解的形式编写sql语句。只适合比较简单的sql语句
    @Select("select * from `admin` where id = #{id}")
    Admin selectById(Integer id);

    void insert(Admin admin);

    void updateById(Admin admin);

    @Delete("DELETE FROM `admin` WHERE id = #{id}")
    void deleteById(Integer id);

    @Select("select * from `admin` where username = #{username}")
    Admin selectByUsername(String username);
}

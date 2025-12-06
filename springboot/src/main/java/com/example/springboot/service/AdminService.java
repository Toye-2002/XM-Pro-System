package com.example.springboot.service;

import com.example.springboot.entity.Account;
import com.example.springboot.entity.Admin;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AdminService {

    List<Admin> selectAll(Admin admin);

    Admin selectById(Integer id);

    PageInfo<Admin> selectPage(Admin admin ,Integer pageNum, Integer pageSize);

    void add(Admin admin);

    void update(Admin admin);


    void deleteById(Integer id);

    void deleteBatch(List<Integer> ids);

    Admin login(Account account);

    void updatePassword(Account account);
}

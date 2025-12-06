package com.example.springboot.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Admin;
import com.example.springboot.exception.CustomException;
import com.example.springboot.mapper.AdminMapper;
import com.example.springboot.service.AdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    // 查询全部管理员信息
    @Override
    public List<Admin> selectAll(Admin admin) {
        return adminMapper.selectAll(admin);
    }

    // 根据ID查询管理员
    @Override
    public Admin selectById(Integer id) {
        return adminMapper.selectById(id);
    }

    // 分页查询
    @Override
    public PageInfo<Admin> selectPage(Admin admin,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> list = adminMapper.selectAll(admin);
        return PageInfo.of(list);
    }

    // 添加员工
    @Override
    public void add(Admin admin) {
        String username = admin.getUsername();
        Admin dbArticle = adminMapper.selectByUsername(username);
        if(dbArticle != null) {
            throw new CustomException("500","账号已存在，请更换其它账号");
        }
        if(StrUtil.isBlank(admin.getPassword())) {   // 密码没填
            admin.setPassword("668330");             // 默认密码 668330
        }
        if(StrUtil.isBlank(admin.getName())) {       // 名字没填
            admin.setName(admin.getUsername());   // 默认名字就是账号名
        }
        // 一定要设置角色
        admin.setRole("ADMIN");
        adminMapper.insert(admin);
    }

    // 更新员工信息
    @Override
    public void update(Admin admin) {
        adminMapper.updateById(admin);
    }

    // 根据ID删除员工
    @Override
    public void deleteById(Integer id) {
        adminMapper.deleteById(id);
    }

    // 批量删除
    @Override
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    // 登录
    @Override
    public Admin login(Account account) {
        String username = account.getUsername();
        Admin dbAdmin = adminMapper.selectByUsername(username);
        if(dbAdmin == null){  // 没有查询到用户
            throw new CustomException("500","账号不存在");
        }
        // 数据库存在这个账号
        String password = account.getPassword();
        if(!dbAdmin.getPassword().equals(password)){   // 用户输入的密码跟数据库账号的密码不匹配
            throw new CustomException("500","账号或密码错误");
        }
        return dbAdmin;
    }

    // 修改密码
    @Override
    public void updatePassword(Account account) {
        Integer id = account.getId();
        Admin admin = this.selectById(id);
        if(!admin.getPassword().equals(account.getPassword())) {
            throw new CustomException("500","输入的原密码错误");
        }
        admin.setPassword(account.getNewPassword());  // 设置新密码
        this.update(admin);
    }


}

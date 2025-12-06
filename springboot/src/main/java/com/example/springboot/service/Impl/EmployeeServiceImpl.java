package com.example.springboot.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Admin;
import com.example.springboot.entity.Employee;
import com.example.springboot.exception.CustomException;
import com.example.springboot.mapper.EmployeeMapper;
import com.example.springboot.service.EmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    // 查询全部员工信息
    @Override
    public List<Employee> selectAll(Employee employee) {
        return employeeMapper.selectAll(employee);
    }

    // 根据ID查询员工
    @Override
    public Employee selectById(Integer id) {
        return employeeMapper.selectById(id);
    }

    // 分页查询
    @Override
    public PageInfo<Employee> selectPage(Employee employee,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Employee> list = employeeMapper.selectAll(employee);
        return PageInfo.of(list);
    }

    // 添加员工
    @Override
    public void add(Employee employee) {
        String username = employee.getUsername();
        Employee dbEmployee = employeeMapper.selectByUsername(username);
        if(dbEmployee != null) {
            throw new CustomException("500","账号已存在，请更换其它账号");
        }
        Employee dbEmployeeNo = employeeMapper.selectByNo(employee.getNo());
        if(dbEmployeeNo != null) {  // 工号是唯一的
            throw new CustomException("500","工号已存在，请确认输入是否正确");
        }

        if(StrUtil.isBlank(employee.getPassword())) {   // 密码没填
            employee.setPassword("668660");             // 默认密码 668660
        }
        if(StrUtil.isBlank(employee.getName())) {       // 名字没填
            employee.setName(employee.getUsername());   // 默认名字就是账号名
        }
        // 一定要设置角色
        employee.setRole("EMP");
        employeeMapper.insert(employee);
    }

    // 更新员工信息
    @Override
    public void update(Employee employee) {
        employeeMapper.updateById(employee);
    }

    // 根据ID删除员工
    @Override
    public void deleteById(Integer id) {
        employeeMapper.deleteById(id);
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
    public Employee login(Account account) {
        String username = account.getUsername();
        Employee dbEmployee = employeeMapper.selectByUsername(username);
        if(dbEmployee == null){  // 没有查询到用户
            throw new CustomException("500","账号不存在");
        }
        // 数据库存在这个账号
        String password = account.getPassword();
        if(!dbEmployee.getPassword().equals(password)){   // 用户输入的密码跟数据库账号的密码不匹配
            throw new CustomException("500","账号或密码错误");
        }
        return dbEmployee;
    }

    // 注册
    @Override
    public void register(Employee employee) {
        this.add(employee);
    }

    // 修改密码
    @Override
    public void updatePassword(Account account) {
        Integer id = account.getId();
        Employee employee = this.selectById(id);
        if(!employee.getPassword().equals(account.getPassword())) {
            throw new CustomException("500","输入的原密码错误");
        }
        employee.setPassword(account.getNewPassword());  // 设置新密码
        this.update(employee);
    }

}

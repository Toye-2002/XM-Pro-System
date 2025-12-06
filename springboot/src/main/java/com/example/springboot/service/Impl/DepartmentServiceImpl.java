package com.example.springboot.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Department;
import com.example.springboot.exception.CustomException;
import com.example.springboot.mapper.DepartmentMapper;
import com.example.springboot.service.DepartmentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    // 查询全部部门信息
    @Override
    public List<Department> selectAll(Department department) {
        return departmentMapper.selectAll(department);
    }

    // 根据ID查询部门
    @Override
    public Department selectById(Integer id) {
        return departmentMapper.selectById(id);
    }

    // 分页查询
    @Override
    public PageInfo<Department> selectPage(Department department,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Department> list = departmentMapper.selectAll(department);
        return PageInfo.of(list);
    }

    // 添加部门
    @Override
    public void add(Department department) {
        departmentMapper.insert(department);
    }

    // 更新部门信息
    @Override
    public void update(Department department) {
        departmentMapper.updateById(department);
    }

    // 根据ID删除部门
    @Override
    public void deleteById(Integer id) {
        departmentMapper.deleteById(id);
    }

    // 批量删除
    @Override
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }
}
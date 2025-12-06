package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Department;
import com.example.springboot.service.DepartmentService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 部门的增删改查接口
@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Resource
    private DepartmentService departmentService;

    // 查询所有
    @GetMapping("/selectAll")
    public Result selectAll(Department department) {
        List<Department> list = departmentService.selectAll(department);
        return Result.success(list);
    }

    // 查询单个.路径参数（@PathVariable），参数作为URL路径的一部分
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Department department = departmentService.selectById(id);
        return Result.success(department);
    }

    // 查询单个。查询参数（@RequestParam），参数在URL问号后面
    @GetMapping("/selectByIdd")
    public Result selectByIdd(@RequestParam Integer id) {
        Department department = departmentService.selectById(id);
        return Result.success(department);
    }

    // 分页查询， 借助pagehelper依赖
    @GetMapping("/selectPage")
    public Result selectPage(Department department,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Department> departmentPageInfo = departmentService.selectPage(department,pageNum, pageSize);
        return Result.success(departmentPageInfo);
    }

    // 新增数据
    @PostMapping("/add")
    public Result add(@RequestBody Department department) {
        departmentService.add(department);
        return Result.success();
    }

    // 更新数据
    @PutMapping("/update")
    public Result update(@RequestBody Department department) {
        departmentService.update(department);
        return Result.success();
    }

    // 删除数据
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        departmentService.deleteById(id);
        return Result.success();
    }

    // 批量删除
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        departmentService.deleteBatch(ids);
        return Result.success();
    }

}

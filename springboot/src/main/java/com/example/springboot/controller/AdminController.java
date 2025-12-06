package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Admin;
import com.example.springboot.service.AdminService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// 员工的增删改查接口
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    // 查询所有
    @GetMapping("/selectAll")
    public Result selectAll(Admin admin) {
        List<Admin> list = adminService.selectAll(admin);
        return Result.success(list);
    }

    // 查询单个.路径参数（@PathVariable），参数作为URL路径的一部分
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Admin admin = adminService.selectById(id);
        return Result.success(admin);
    }

    // 查询单个。查询参数（@RequestParam），参数在URL问号后面
    @GetMapping("/selectByIdd")
    public Result selectByIdd(@RequestParam Integer id) {
        Admin admin = adminService.selectById(id);
        return Result.success(admin);
    }

    // 分页查询， 借助pagehelper依赖
    @GetMapping("/selectPage")
    public Result selectPage(Admin admin,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Admin> adminPageInfo = adminService.selectPage(admin,pageNum, pageSize);
        return Result.success(adminPageInfo);
    }

    // 新增数据
    @PostMapping("/add")
    public Result add(@RequestBody Admin admin) {
        adminService.add(admin);
        return Result.success();
    }

    // 更新数据
    @PutMapping("/update")
    public Result update(@RequestBody Admin admin) {
        adminService.update(admin);
        return Result.success();
    }

    // 删除数据
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        adminService.deleteById(id);
        return Result.success();
    }

    // 批量删除
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        adminService.deleteBatch(ids);
        return Result.success();
    }

}

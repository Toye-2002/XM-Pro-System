package com.example.springboot.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Employee;
import com.example.springboot.service.EmployeeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

// 员工的增删改查接口
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    // 查询所有
    @GetMapping("/selectAll")
    public Result selectAll(Employee employee) {
        List<Employee> list = employeeService.selectAll(employee);
        return Result.success(list);
    }

    // 查询单个.路径参数（@PathVariable），参数作为URL路径的一部分
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Employee employee = employeeService.selectById(id);
        return Result.success(employee);
    }

    // 查询单个。查询参数（@RequestParam），参数在URL问号后面
    @GetMapping("/selectByIdd")
    public Result selectByIdd(@RequestParam Integer id) {
        Employee employee = employeeService.selectById(id);
        return Result.success(employee);
    }

    // 分页查询， 借助pagehelper依赖
    @GetMapping("/selectPage")
    public Result selectPage(Employee employee,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Employee> employeePageInfo = employeeService.selectPage(employee,pageNum, pageSize);
        return Result.success(employeePageInfo);
    }

    // 新增数据
    @PostMapping("/add")
    public Result add(@RequestBody Employee employee) {
        employeeService.add(employee);
        return Result.success();
    }

    // 更新数据
    @PutMapping("/update")
    public Result update(@RequestBody Employee employee) {
        employeeService.update(employee);
        return Result.success();
    }

    // 删除数据
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        employeeService.deleteById(id);
        return Result.success();
    }

    // 批量删除
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        employeeService.deleteBatch(ids);
        return Result.success();
    }

    // 导出数据
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 1.从数据库查询出所有的数据
        List<Employee> employeeList = employeeService.selectAll(null);
        // 2.构建 ExcelWriter,  在内存操作，写到浏览器中
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 3.设置中文表头
        writer.addHeaderAlias("username","账号");
        writer.addHeaderAlias("name","姓名");
        writer.addHeaderAlias("sex","性别");
        writer.addHeaderAlias("no","工号");
        writer.addHeaderAlias("age","年龄");
        writer.addHeaderAlias("descr","个人介绍");
        writer.addHeaderAlias("departmentName","部门");
        // 默认的，未添加alias的属性也会写出，如果只想写出加了别名的字段，可以调用此方法排除之
        writer.setOnlyAlias(true);
        // 4.写出数据到writer
        writer.write(employeeList,true);
        // 5.设置输出文件的名称，以及输出流的头信息
        // 设置浏览器响应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("员工信息", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        // 6.写出到输出流  并关闭writer
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out);
        writer.close();
    }

    // 导入数据
    @PostMapping("/import")
    public Result importData(MultipartFile file) throws Exception {
        // 1.拿到输入流，构建reader
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 2.读取 excel里面的数据
        reader.addHeaderAlias("账号","username");
        reader.addHeaderAlias("姓名","name");
        reader.addHeaderAlias("性别","sex");
        reader.addHeaderAlias("工号","no");
        reader.addHeaderAlias("年龄","age");
        reader.addHeaderAlias("个人介绍","descr");
        reader.addHeaderAlias("部门","departmentName");
        List<Employee> employeeList = reader.readAll(Employee.class);
        // 3.将employeeList遍历写入到数据库中
        for (Employee employee : employeeList) {
            employeeService.add(employee);
        }
        return Result.success();
    }

}




















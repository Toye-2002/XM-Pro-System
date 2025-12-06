package com.example.springboot.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Employee;
import com.example.springboot.exception.CustomException;
import com.example.springboot.service.AdminService;
import com.example.springboot.service.ArticleService;
import com.example.springboot.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@RestController
public class WebController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private ArticleService articleService;

    // 简单的测试接口
    @GetMapping("/hello")
    public String webController() {
        return "hello";
    }

    // 登录接口
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account result = null;
        if("ADMIN".equals(account.getRole())){  // 管理员登录
            result = adminService.login(account);
        }else if ("EMP".equals(account.getRole())){
            result = employeeService.login(account);
        }else {
            throw new CustomException("500","非法输入");
        }
        return Result.success(result);
    }

    // 注册接口
    @PostMapping("/register")
    public Result register(@RequestBody Employee employee) {
        employeeService.register(employee);
        return Result.success();
    }

    // 修改密码
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
       if("ADMIN".equals(account.getRole())){    // 管理员修改密码
           adminService.updatePassword(account);
       }else if("EMP".equals(account.getRole())){
           employeeService.updatePassword(account);
       }else {
           throw new CustomException("500","非法输入");
       }
        return Result.success();
    }

    // 柱状图
    @GetMapping("/barData")
    public Result getBarData() {
        Map<String, Object> map = new HashMap<>();
        List<Employee> employeeList = employeeService.selectAll(null);
        Set<String> departmentNameSet = employeeList.stream().map(Employee::getDepartmentName).collect(Collectors.toSet());
        map.put("department", departmentNameSet);  // x轴数据
        List<Long> countList = new ArrayList<>();
        for(String departmentName : departmentNameSet){
            // 统计这个部门下面的员工数量
            long count = employeeList.stream().filter(employee -> employee.getDepartmentName().equals(departmentName)).count();
            countList.add(count);
        }
        map.put("count", countList);     // y轴数据
        return Result.success(map);
    }

    // 折线图
    @GetMapping("/lineData")
    public Result getLineData() {
        Map<String, Object> map = new HashMap<>();

        Date date = new Date();
        DateTime start = DateUtil.offsetDay(date, -7);      // 从7天开始算起
        List<DateTime> dateTimeList = DateUtil.rangeToList(start, date, DateField.DAY_OF_YEAR);
        // 把DateTime类型的日期转换成 字符串类型的日期。
        List<String> dateStrList = dateTimeList.stream().map(dateTime -> DateUtil.format(dateTime, "MM月dd日"))
                .sorted(Comparator.naturalOrder()).toList();

        map.put("date", dateStrList);  // x轴数据

        List<Integer> countList = new ArrayList<>();
        for(DateTime day : dateTimeList){
            String dayFormat = DateUtil.formatDate(day);
            // 获得当天所有发布的文章数量
            Integer count = articleService.selectCountByDate(dayFormat);
            countList.add(count);
        }
        map.put("count", countList);  // y轴发布的数据
        return Result.success(map);
    }

    // 饼图
    @GetMapping("/pieData")
    public Result getPieData() {
       List<Map<String,Object>> list = new ArrayList<>();
        List<Employee> employeeList = employeeService.selectAll(null);
        Set<String> departmentNameSet = employeeList.stream().map(Employee::getDepartmentName).collect(Collectors.toSet());

        for(String departmentName : departmentNameSet){
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", departmentName);
            // 统计这个部门下面的员工数量
            long count = employeeList.stream().filter(employee -> employee.getDepartmentName().equals(departmentName)).count();
            map.put("value", count);
            list.add(map);
        }

        return Result.success(list);
    }


}






















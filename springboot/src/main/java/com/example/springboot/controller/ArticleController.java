package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Article;
import com.example.springboot.entity.Article;
import com.example.springboot.service.ArticleService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 员工的增删改查接口
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    // 查询所有
    @GetMapping("/selectAll")
    public Result selectAll(Article article) {
        List<Article> list = articleService.selectAll(article);
        return Result.success(list);
    }

    // 查询单个.路径参数（@PathVariable），参数作为URL路径的一部分
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Article article = articleService.selectById(id);
        return Result.success(article);
    }

    // 查询单个。查询参数（@RequestParam），参数在URL问号后面
    @GetMapping("/selectByIdd")
    public Result selectByIdd(@RequestParam Integer id) {
        Article article = articleService.selectById(id);
        return Result.success(article);
    }

    // 分页查询， 借助pagehelper依赖
    @GetMapping("/selectPage")
    public Result selectPage(Article article,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Article> articlePageInfo = articleService.selectPage(article,pageNum, pageSize);
        return Result.success(articlePageInfo);
    }

    // 新增数据
    @PostMapping("/add")
    public Result add(@RequestBody Article article) {
        articleService.add(article);
        return Result.success();
    }

    // 更新数据
    @PutMapping("/update")
    public Result update(@RequestBody Article article) {
        articleService.update(article);
        return Result.success();
    }

    // 删除数据
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        articleService.deleteById(id);
        return Result.success();
    }

    // 批量删除
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        articleService.deleteBatch(ids);
        return Result.success();
    }

}

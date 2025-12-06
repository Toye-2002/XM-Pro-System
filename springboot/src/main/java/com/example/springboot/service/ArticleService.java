package com.example.springboot.service;
import com.example.springboot.entity.Article;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ArticleService {

    List<Article> selectAll(Article article);

    Article selectById(Integer id);

    PageInfo<Article> selectPage(Article article ,Integer pageNum, Integer pageSize);

    void add(Article article);

    void update(Article article);


    void deleteById(Integer id);

    void deleteBatch(List<Integer> ids);

    Integer selectCountByDate(String date);
}

package com.example.springboot.service.Impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Article;
import com.example.springboot.exception.CustomException;
import com.example.springboot.mapper.ArticleMapper;
import com.example.springboot.service.ArticleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper ArticleMapper;
    @Autowired
    private ArticleMapper articleMapper;

    // 查询全部员工信息
    @Override
    public List<Article> selectAll(Article article) {
        return ArticleMapper.selectAll(article);
    }

    // 根据ID查询员工
    @Override
    public Article selectById(Integer id) {
        return ArticleMapper.selectById(id);
    }

    // 分页查询
    @Override
    public PageInfo<Article> selectPage(Article article,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Article> list = ArticleMapper.selectAll(article);
        return PageInfo.of(list);
    }

    // 添加员工
    @Override
    public void add(Article article) {
        article.setTime(DateUtil.now());
        ArticleMapper.insert(article);
    }

    // 更新员工信息
    @Override
    public void update(Article article) {
        ArticleMapper.updateById(article);
    }

    // 根据ID删除员工
    @Override
    public void deleteById(Integer id) {
        ArticleMapper.deleteById(id);
    }

    // 批量删除
    @Override
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    @Override
    public Integer selectCountByDate(String date) {
        return articleMapper.selectCountByDate(date);
    }


}

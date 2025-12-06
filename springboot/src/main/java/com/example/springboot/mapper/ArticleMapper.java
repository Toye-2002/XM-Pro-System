package com.example.springboot.mapper;

import com.example.springboot.entity.Article;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ArticleMapper {

    // 通过在mapper.xml文件中编写sql语句
    List<Article> selectAll(Article article);

    // 通过注解的形式编写sql语句。只适合比较简单的sql语句
    @Select("select * from `article` where id = #{id}")
    Article selectById(Integer id);

    void insert(Article article);

    void updateById(Article article);

    @Delete("DELETE FROM `article` WHERE id = #{id}")
    void deleteById(Integer id);

    @Select("select count(*) from `article` where time like '%${date}%'")
    Integer selectCountByDate(String date);

}

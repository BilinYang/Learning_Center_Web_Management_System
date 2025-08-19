package com.bilin.mapper;

import com.bilin.entity.CourseQueryParam;
import com.bilin.entity.Course;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseMapper {
    List<Course> page(CourseQueryParam courseQueryParam);

    @Delete("delete from course where id = #{id}")
    void delete(Integer id);

    void save(Course course);

    @Select("select * from course where id = #{id}")
    Course getById(Integer id);

    void update(Course course);

    @Select("select * from course")
    List<Course> list();
}

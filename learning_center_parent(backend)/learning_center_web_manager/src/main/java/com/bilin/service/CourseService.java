package com.bilin.service;

import com.bilin.entity.CourseQueryParam;
import com.bilin.entity.Course;
import com.bilin.entity.PageBean;

import java.util.List;

public interface CourseService {
    PageBean page(CourseQueryParam courseQueryParam);

    void delete(Integer id);

    void save(Course course);

    Course getById(Integer id);

    void update(Course course);

    List<Course> list();
}

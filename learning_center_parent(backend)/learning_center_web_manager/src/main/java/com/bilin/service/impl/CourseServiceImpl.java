package com.bilin.service.impl;
import com.bilin.entity.CourseQueryParam;
import com.bilin.entity.Course;
import com.bilin.entity.PageBean;
import com.bilin.mapper.CourseMapper;
import com.bilin.service.CourseService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseMapper courseMapper;

    @Override
    public PageBean page(CourseQueryParam courseQueryParam){
        PageHelper.startPage(courseQueryParam.getPage(), courseQueryParam.getPageSize());
        List<Course> courseList = courseMapper.page(courseQueryParam);
        Page p = (Page) courseList;
        return new PageBean(p.getTotal(), p.getResult());
    }

    @Override
    public void delete(Integer id){
        courseMapper.delete(id);
    }

    @Override
    public void save(Course course){
        course.setCreateTime(LocalDateTime.now());
        course.setUpdateTime(LocalDateTime.now());

        LocalDate curDate = LocalDate.now();
        if (course.getBeginDate().isAfter(curDate)) {
            course.setCourseStatus("Not started");
        }
        else if (course.getEndDate().isBefore(curDate)) {
            course.setCourseStatus("Completed");
        }
        else{
            course.setCourseStatus("In progress");
        }
        courseMapper.save(course);
    }

    @Override
    public Course getById(Integer id){
        return courseMapper.getById(id);
    }

    @Override
    @Transactional
    public void update(Course course){
        course.setUpdateTime(LocalDateTime.now());
        courseMapper.update(course);
    }

    @Override
    public List<Course> list(){
        return courseMapper.list();
    }
}

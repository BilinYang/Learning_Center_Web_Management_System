package com.bilin.controller;

import com.bilin.entity.Course;
import com.bilin.entity.CourseQueryParam;
import com.bilin.entity.PageBean;
import com.bilin.entity.Result;
import com.bilin.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping
    public Result page(CourseQueryParam courseQueryParam) {
        log.info("Running query on courses: courseQueryParam={}", courseQueryParam);
        PageBean pageBean = courseService.page(courseQueryParam);
        return Result.success(pageBean);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public Result delete(@PathVariable Integer id) {
        log.info("Delete course with id={}", id);
        courseService.delete(id);
        return Result.success();
    }

    @PostMapping
    public Result save(@RequestBody Course course) {
        log.info("Save course: {}", course);
        courseService.save(course);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("Get course with id={}", id);
        Course course = courseService.getById(id);
        return Result.success(course);
    }

    @PutMapping
    public Result update(@RequestBody Course course) {
        log.info("Update course: {}", course);
        courseService.update(course);
        return Result.success();
    }

    @GetMapping("/list")
    public Result list() {
        log.info("Get all courses");
        List<Course> allCourses = courseService.list();
        return Result.success(allCourses);
    }
}

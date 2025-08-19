package com.bilin.controller;

import com.bilin.entity.PageBean;
import com.bilin.entity.Result;
import com.bilin.entity.Student;
import com.bilin.entity.StudentQueryParam;
import com.bilin.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public Result page(StudentQueryParam studentQueryParam){
        log.info("Running query on courses: courseQueryParam={}", studentQueryParam);
        PageBean pageBean = studentService.page(studentQueryParam);
        return Result.success(pageBean);
    }

    @DeleteMapping("/{ids}")
    public Result deleteBatch(@PathVariable List<Integer> ids){
        log.info("Deleting courses with ids: {}", ids);
        studentService.deleteBatch(ids);
        return Result.success();
    }

    @PostMapping
    public Result save(@RequestBody Student student){
        log.info("Save student: {}", student);
        studentService.save(student);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        log.info("Get student with id: {}", id);
        Student student = studentService.getById(id);
        return Result.success(student);
    }

    @PutMapping
    public Result update(@RequestBody Student student){
        log.info("Update student: {}", student);
        studentService.update(student);
        return Result.success();
    }

    @PutMapping("/violation/{id}/{score}")
    public Result applyViolationPenalty(@PathVariable Integer id, @PathVariable Integer score){
        log.info("Apply a penalty of {} points to the student with id {}", id, score);
        studentService.applyViolationPenalty(id, score);
        return Result.success();
    }

}

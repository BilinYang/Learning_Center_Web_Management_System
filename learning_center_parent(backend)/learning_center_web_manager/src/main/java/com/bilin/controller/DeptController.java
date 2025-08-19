package com.bilin.controller;

import com.bilin.annotation.Log;
import com.bilin.entity.Dept;
import com.bilin.entity.Result;
import com.bilin.service.impl.DeptServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/dept")
@RestController // equivalent to @Controller + @ResponseBody
public class DeptController {

    @Autowired
    private DeptServiceImpl deptService;

    //Get department data as a list from the database
    @GetMapping
    public Result list(){
        List<Dept> depts = deptService.list();
        return Result.success(depts);
    }

    @Log
    @DeleteMapping
    public Result delete(Integer id){
        log.info("delete id = {}", id);
        deptService.delete(id);
        return Result.success();
    }

    @Log
    @PostMapping
    public Result save(@RequestBody Dept dept){
        log.info("dept = {}", dept);
        deptService.save(dept);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") Integer id){
        log.info("department ID to search = {}", id);
        Dept dept = deptService.getById(id);
        return Result.success(dept);
    }

    @Log
    @PutMapping
    public Result update(@RequestBody Dept dept){
       log.info("update dept = {}", dept);
        deptService.update(dept);
        return Result.success();
    }
}

package com.bilin.controller;

import com.bilin.annotation.Log;
import com.bilin.entity.Emp;
import com.bilin.entity.EmpQueryParam;
import com.bilin.entity.PageBean;
import com.bilin.entity.Result;
import com.bilin.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequestMapping("/emp")
@RestController
public class EmpController {

    @Autowired
    private EmpService empService;

//    @GetMapping("/emps")
//    public Result page(String name, Integer gender,
//                       @DateTimeFormat(pattern="yyyy-mm-dd") LocalDate begin,
//                       @DateTimeFormat(pattern="yyyy-mm-dd") LocalDate end,
//                       @RequestParam(defaultValue = "1") Integer page,
//                       @RequestParam(defaultValue = "10") Integer pageSize){
//        log.info("Pagination query- name:{}, gender:{}, entry_date:{}, end_date:{}, page:{}, pageSize:{}", name, gender, begin, end, page, pageSize);
//        PageBean pageBean =  empService.page(name, gender, begin, end, page, pageSize);
//        return Result.success(pageBean);
//    }

    @GetMapping
    public Result page(EmpQueryParam param){
        log.info("Pagination query- name:{}, gender:{}, entry_date:{}, end_date:{}, page:{}, pageSize:{}", param.getName(),
                param.getGender(), param.getBegin(), param.getEnd(), param.getPage(), param.getPageSize());
        PageBean pageBean =  empService.page(param);
        return Result.success(pageBean);
    }

    @Log
    @PostMapping
    public Result save (@RequestBody Emp emp) throws Exception{
        log.info("Add new employee: {}", emp);
        empService.save(emp);
        return Result.success();
    }

    @Log
    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids){
        log.info("Delete employees with ids: {}", ids);
        empService.delete(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        // Method 1: use Mapper method to get employee information and their work experiences
        log.info("Get employee by id: {}", id);
        Emp emp = empService.getById(id);
        return Result.success(emp);
    }

    @Log
    @PutMapping
    public Result update(@RequestBody Emp emp){
        log.info("Update employee (by id): {}", emp);
        log.debug("Updating employee with id={}", emp.getId());
        empService.update(emp);
        return Result.success();
    }

    @GetMapping("/list")
    public Result list(){
        log.info("Get all employees");
        List<Emp> allEmps = empService.list();
        return Result.success(allEmps);
    }
}

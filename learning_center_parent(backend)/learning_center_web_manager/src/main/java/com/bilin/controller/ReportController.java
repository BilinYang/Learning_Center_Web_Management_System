package com.bilin.controller;

import com.bilin.entity.CourseCountOption;
import com.bilin.entity.JobOption;
import com.bilin.entity.Result;
import com.bilin.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/empJobData")
    public Result getEmpJobData(){
        log.info("Count the number of employees by job position.");
        JobOption jobOption = reportService.getEmpJobData();
        // List<Map> jobList =  reportService.getEmpJobData();
        // return Result.success(jobList);
        return Result.success(jobOption);
    }

    @GetMapping("/empGenderData")
    public Result getEmpGenderData(){
        log.info("Count the number of employees by gender.");
        List<Map> genderList = reportService.getEmpGenderData();
        return Result.success(genderList);
    }

    @GetMapping("/studentDegreeData")
    public Result getStudentDegreeData(){
        log.info("Count the number of students by highest degree/education level earned");
        List<Map> degreeList = reportService.getStudentDegreeData();
        return Result.success(degreeList);
    }

    @GetMapping("/studentCountData")
    public Result getStudentCountData(){
        log.info("Count the number of students by class");
        CourseCountOption courseCountOption = reportService.getStudentCountData();
        return Result.success(courseCountOption);
    }
}

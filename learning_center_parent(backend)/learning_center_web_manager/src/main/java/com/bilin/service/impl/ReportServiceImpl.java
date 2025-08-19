package com.bilin.service.impl;

import com.bilin.entity.CourseCountOption;
import com.bilin.entity.JobOption;
import com.bilin.mapper.EmpMapper;
import com.bilin.mapper.StudentMapper;
import com.bilin.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public JobOption getEmpJobData(){
        List<Map<String, Object>> list = empMapper.countEmpJobData();
        List<Object> jobList = list.stream().map(dataMap -> dataMap.get("pos")).toList();
        List<Object> dataList = list.stream().map(dataMap -> dataMap.get("total")).toList();
        return new JobOption(jobList, dataList);
    }

//    @Override
//    public List<Map> getEmpJobData(){
//        return empMapper.countEmpJobData();
//    }

    @Override
    public List<Map> getEmpGenderData(){
        return empMapper.countEmpGenderData();
    }

    @Override
    public List<Map> getStudentDegreeData(){
        return studentMapper.countStudentDegreeData();
    }

    @Override
    public CourseCountOption getStudentCountData(){
        List<Map<String, Object>> list = studentMapper.getStudentCount();
        List<Object> clazzList = list.stream().map(dataMap -> dataMap.get("course_name")).toList();
        List<Object> dataList = list.stream().map(dataMap -> dataMap.get("student_count")).toList();
        return new CourseCountOption(clazzList, dataList);
    }
}

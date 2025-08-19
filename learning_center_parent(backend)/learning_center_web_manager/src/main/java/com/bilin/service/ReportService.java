package com.bilin.service;

import com.bilin.entity.CourseCountOption;
import com.bilin.entity.JobOption;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ReportService {
    JobOption getEmpJobData();
    // List<Map> getEmpJobData();

    List<Map> getEmpGenderData();

    List<Map> getStudentDegreeData();

    CourseCountOption getStudentCountData();
}

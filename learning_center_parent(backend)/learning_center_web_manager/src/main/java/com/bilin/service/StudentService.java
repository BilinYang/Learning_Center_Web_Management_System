package com.bilin.service;

import com.bilin.entity.PageBean;
import com.bilin.entity.Student;
import com.bilin.entity.StudentQueryParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentService {

    PageBean page(StudentQueryParam studentQueryParam);

    void deleteBatch(List<Integer> ids);

    void save(Student student);

    Student getById(Integer id);

    void update(Student student);

    void applyViolationPenalty(Integer id, Integer score);
}

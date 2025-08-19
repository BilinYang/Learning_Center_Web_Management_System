package com.bilin.service.impl;

import com.bilin.entity.PageBean;
import com.bilin.entity.Student;
import com.bilin.entity.StudentQueryParam;
import com.bilin.mapper.StudentMapper;
import com.bilin.service.StudentService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public PageBean page(StudentQueryParam studentQueryParam){
        PageHelper.startPage(studentQueryParam.getPage(), studentQueryParam.getPageSize());
        List<Student> studentList = studentMapper.page(studentQueryParam);
        Page p = (Page) studentList;
        return new PageBean(p.getTotal(), p.getResult());
    }

    @Override
    public void deleteBatch(List<Integer> ids){
        studentMapper.deleteBatch(ids);
    }

    @Override
    public void save(Student student){
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.save(student);
    }

    @Override
    public Student getById(Integer id){
        return studentMapper.getById(id);
    }

    @Override
    public void update(Student student){
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.update(student);
    }

    @Override
    public void applyViolationPenalty(Integer id, Integer score){
        studentMapper.applyViolationPenalty(id, score);
    }
}

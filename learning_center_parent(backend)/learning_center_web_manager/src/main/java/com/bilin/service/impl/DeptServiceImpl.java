package com.bilin.service.impl;

import com.bilin.entity.Dept;
import com.bilin.entity.Result;
import com.bilin.mapper.DeptMapper;
import com.bilin.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// @Primary
@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptMapper deptMapper;

    public List<Dept> list(){
        //1. Use Mapper method to get data from database
        List<Dept> depts = deptMapper.list();
        //2. Return data
        return depts;
    }

    @Override
    public void delete(Integer id) {
        deptMapper.delete(id);
    }

    public void save(Dept dept) {
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());

        deptMapper.insert(dept);
    }

    @Override
    public Dept getById(Integer id) {
        return deptMapper.getById(id);
    }

    public void update(Dept dept) {
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.update(dept);
    }
}

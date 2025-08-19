package com.bilin.service;

import com.bilin.entity.Dept;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DeptService {
    List<Dept> list();
    void delete(Integer id);

    Dept getById(Integer id);
}
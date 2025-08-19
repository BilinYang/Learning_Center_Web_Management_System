package com.bilin.service;

import com.bilin.entity.Emp;
import com.bilin.entity.EmpLoginInfo;
import com.bilin.entity.EmpQueryParam;
import com.bilin.entity.PageBean;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

public interface EmpService {
    public PageBean page(EmpQueryParam param);

    void save(Emp emp) throws Exception;

    void delete(List<Integer> ids);

    Emp getById(Integer id);

    void update(Emp emp);

    List<Emp> list();

    EmpLoginInfo login(Emp emp);
}

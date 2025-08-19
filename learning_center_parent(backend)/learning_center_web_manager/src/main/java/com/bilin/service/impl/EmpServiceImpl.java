package com.bilin.service.impl;

import com.bilin.entity.*;
import com.bilin.mapper.DeptMapper;
import com.bilin.mapper.EmpExprMapper;
import com.bilin.mapper.EmpMapper;
import com.bilin.service.EmpLogService;
import com.bilin.service.EmpService;
import com.bilin.utils.JwtUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private EmpExprMapper empExprMapper;

    @Autowired
    private EmpLogService empLogService;

//    @Override
//    public PageBean page(Integer page, Integer pageSize){
//         Long total = empMapper.count();
//
//         Integer start = (page - 1) * pageSize;
//         List<Emp> empList = empMapper.page(start, pageSize);
//
//         return new PageBean(total, empList);
//    }

    @Override
    public PageBean page(EmpQueryParam param){
        PageHelper.startPage(param.getPage(), param.getPageSize());
        List<Emp> empList = empMapper.page(param);
        Page p = (Page) empList;

        return new PageBean(p.getTotal(), p.getResult());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Emp emp) throws Exception{
        try{
            emp.setPassword("123456");
            emp.setCreateTime(LocalDateTime.now());
            emp.setUpdateTime(LocalDateTime.now());
            empMapper.insert(emp);

            Integer id = emp.getId();
            log.info("id = {}", id);

            List<EmpExpr> exprList = emp.getExprList();
            if (!CollectionUtils.isEmpty(exprList)) {
                exprList.forEach((expr)->{
                    expr.setEmpId(id);
                });
                empExprMapper.insertBatch(exprList);
            }

        } finally{
            // write log regardless of if the employee was successfully saved
            EmpLog empLog = new EmpLog();
            empLog.setOperateTime(LocalDateTime.now());
            empLog.setInfo("Successfully inserted employee into dataset: " + emp);
            empLogService.insertLog(empLog);
        }
    }

    @Transactional
    @Override
    public void delete(List<Integer> ids){
        try{
            empMapper.deleteBatch(ids);
            empExprMapper.deleteBatch(ids);

        } finally{
            EmpLog empLog = new EmpLog();
            empLog.setInfo("Successfully deleted employees with ids from dataset: " + ids);
            empLogService.insertLog(empLog);
        }

    }

    public Emp getById(Integer id){
        // Method 1: use Mapper method to get employee information and their work experiences
        // return empMapper.getById(id);

        // Method 2:
        // 1. Search employee info and wrap it in an Emp object
        Emp emp = empMapper.getById2(id);
        // 2. Retrieve the employee’s work experience list and wrap it into the Emp object
        List<EmpExpr> empExprList = empExprMapper.getByEmpId(id);
        emp.setExprList(empExprList);
        // 3. return the Emp object
        return emp;
    }

    @Transactional
    public void update(Emp emp){
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.update(emp);

        empExprMapper.deleteByEmpId(emp.getId());
        List<EmpExpr> exprList = emp.getExprList();

        if (!CollectionUtils.isEmpty(exprList)) {
            exprList.forEach((expr)->{
                expr.setEmpId(emp.getId());
            });
            empExprMapper.insertBatch(exprList);
        }
    }

    @Override
    public List<Emp> list(){
        return empMapper.list();
    }

    @Override
    public EmpLoginInfo login(Emp emp){
        Emp empFromDB = empMapper.selectUsernameAndPassword(emp);

        // if username and password are correct, create and return new EmpLoginInfo object
        if (empFromDB != null){
            Map<String,Object> claims = new HashMap<>();
            claims.put("id",empFromDB.getId());
            claims.put("username", empFromDB.getUsername());

            String jwt = JwtUtils.generateJwt(claims);
            return new EmpLoginInfo(empFromDB.getId(), empFromDB.getUsername(),
                    empFromDB.getName(), jwt);
        }
        // if username or password is not correct (the search returns an empty result), return null
        return null;
    }

}

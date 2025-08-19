package com.bilin.mapper;

import com.bilin.entity.Emp;
import com.bilin.entity.EmpExpr;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface  EmpExprMapper {

    void insertBatch(List<EmpExpr> exprList);

    void deleteBatch(List<Integer> empIds);

    List<EmpExpr> getByEmpId(Integer empId);

    void deleteByEmpId(Integer empId);

}

package com.bilin.mapper;

import com.bilin.entity.Emp;
import com.bilin.entity.EmpQueryParam;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface EmpMapper {

//    @Select("select count(*) from emp")
//    Long count();
//
//    @Select("select e.*, d.name deptName from emp e left join dept d " +
//            "on e.dept_id = d.id limit #{start}, #{pageSize}")
//    List<Emp> page(Integer start, Integer pageSize);


    List<Emp> page(EmpQueryParam param);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into emp values (null, #{username}, #{password}, #{name}, #{gender}, #{phone}, #{job}, #{salary}, " +
            "#{image}, #{entryDate}, #{deptId}, #{createTime}, #{updateTime})")
    void insert(Emp emp);

    void deleteBatch(List<Integer> ids);

    // Method 1
    // Emp getById(Integer id);

    // Method 2
    @Select("select * from emp where id=#{id}")
    Emp getById2(Integer id);

    void update(Emp emp);

    @Select("select * from emp")
    List<Emp> list();

    @Select("select * from emp where username=#{username} and password=#{password}")
    Emp selectUsernameAndPassword(Emp emp);

    @MapKey("pos")
    List<Map<String, Object>> countEmpJobData();
//    @MapKey("pos")
//    List<Map> countEmpJobData();

    @MapKey("name")
    List<Map> countEmpGenderData();
}

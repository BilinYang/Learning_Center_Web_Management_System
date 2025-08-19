package com.bilin.mapper;

import com.bilin.entity.Student;
import com.bilin.entity.StudentQueryParam;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {

    List<Student> page(StudentQueryParam studentQueryParam);

    void deleteBatch(List<Integer> ids);

    void save(Student student);

    @Select("select * from student where id=#{id}")
    Student getById(Integer id);

    void update(Student student);

    @Update("update student set violation_count = violation_count+1, " +
        "penalty_points = penalty_points + #{score} where id=#{id}")
    void applyViolationPenalty(Integer id, Integer score);

    @MapKey("name")
    List<Map> countStudentDegreeData();

    @MapKey("name")
    List<Map<String, Object>> getStudentCount();
}

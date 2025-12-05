package com.chy.mapper;

import com.chy.pojo.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {
    @Select("select count(*) from student where clazz_id = #{id}")
    Integer countByClazzId(Integer id);

    List<Student> list(String name, Integer degree, Integer clazzId);

    void delete(List<Integer> ids);

    @Insert("insert into student(name,no, gender, phone,id_card, is_college, address, degree, graduation_date,clazz_id, create_time, update_time) VALUES" +
            "(#{name},#{no},#{gender},#{phone},#{idCard},#{isCollege},#{address},#{degree},#{graduationDate},#{clazzId},#{createTime},#{updateTime})")
    void insert(Student student);

    @Select("select * from student where id = #{id}")
    Student getById(Integer id);

    void update(Student student);

    @Update("update student set violation_count = violation_count + 1 , violation_score = violation_score + #{score} , update_time = now() where id = #{id}")
    void updateViolation(Integer id, Integer score);
    @MapKey("name")
    List<Map> countStudentDegreeData();
    @Select("select c.name cname , count(s.id) scount from clazz c  left join student s on s.clazz_id = c.id group by c.name order by count(s.id) desc ")
    List<Map<String, Object>> getStudentCount();
}

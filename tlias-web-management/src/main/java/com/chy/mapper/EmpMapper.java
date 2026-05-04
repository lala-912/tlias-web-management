package com.chy.mapper;

import com.chy.pojo.Emp;
import com.chy.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface EmpMapper {
//    查询总记录数
    public List<Emp> list(EmpQueryParam empQueryParam);
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into emp(username,name,gender,phone,job,salary,image,entry_date,dept_id,create_time,update_time)" +
            "values (#{username}, #{name}, #{gender}, #{phone}, #{job}, #{salary}, #{image}, #{entryDate}, #{deptId}, #{createTime}, #{updateTime})")
    void insert(Emp emp);

    void deleteByIds(List<Integer> ids);

    Emp getById(Integer id);

    void updateById(Emp emp);
    @MapKey("pos")
    List<Map<String, Object>> countEmpJobData();
    @MapKey("name")
    List<Map<String, Object>> countEmpGenderData();

    @Select("select id, username, password, name, role from emp where username = #{username}")
    Emp selectByUsername(String username);

    @Update("update emp set password = #{password} where id = #{id}")
    void updatePassword(@Param("id") Integer id, @Param("password") String password);
}

package com.chy.mapper;

import com.chy.pojo.Clazz;
import com.chy.pojo.ClazzQueryParam;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ClazzMapper {
    @Select("select * from clazz")
    List<Clazz> findAll();

    public List<Clazz> list(ClazzQueryParam clazzQueryParam);

    @Delete("delete from clazz where id = #{id}")
    void deleteById(Integer id);

    @Insert("insert into clazz VALUES (null,#{name},#{room},#{beginDate},#{endDate},#{masterId}, #{subject},#{createTime},#{updateTime})")
    void insert(Clazz clazz);

    @Select("select * from clazz where id = #{id}")
    Clazz getInfo(Integer id);

    void update(Clazz clazz);
}

package com.chy.service;

import com.chy.pojo.PageResult;
import com.chy.pojo.Student;

import java.util.List;

public interface StudentService {

    PageResult page(String name, Integer degree, Integer clazzId, Integer page, Integer pageSize);

    void delete(List<Integer> ids);

    void save(Student student);

    Student getInfo(Integer id);

    void update(Student student);

    void violationHandle(Integer id, Integer score);
}

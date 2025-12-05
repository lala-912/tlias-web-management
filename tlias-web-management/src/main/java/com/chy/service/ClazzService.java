package com.chy.service;

import com.chy.pojo.Clazz;
import com.chy.pojo.ClazzQueryParam;
import com.chy.pojo.PageResult;

import java.time.LocalDateTime;
import java.util.List;

public interface ClazzService {
    PageResult page(ClazzQueryParam clazzQueryParam);

    void deleteById(Integer id);

    void save(Clazz clazz);

    Clazz getInfo(Integer id);

    void update(Clazz clazz);

    List<Clazz> findAll();
}

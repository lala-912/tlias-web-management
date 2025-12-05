package com.chy.service;

import com.chy.pojo.Emp;
import com.chy.pojo.EmpQueryParam;
import com.chy.pojo.LoginInfo;
import com.chy.pojo.PageResult;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public interface EmpService {


    PageResult<Emp> page(EmpQueryParam empQueryParam);

    void save(Emp emp) throws Exception;

    void delete(List<Integer> ids);

    Emp getInfo(Integer id);

    void update(Emp emp);

    LoginInfo login(Emp emp);
}

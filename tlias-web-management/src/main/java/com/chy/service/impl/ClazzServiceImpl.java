package com.chy.service.impl;

import com.chy.exception.BusinessException;
import com.chy.mapper.ClazzMapper;
import com.chy.mapper.StudentMapper;
import com.chy.pojo.Clazz;
import com.chy.pojo.ClazzQueryParam;
import com.chy.pojo.Emp;
import com.chy.pojo.PageResult;
import com.chy.service.ClazzService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClazzServiceImpl implements ClazzService {
    @Autowired
    private ClazzMapper clazzMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Override
    public PageResult<Clazz> page(ClazzQueryParam clazzQueryParam) {
        PageHelper.startPage(clazzQueryParam.getPage(), clazzQueryParam.getPageSize());
        //执行查询
        List<Clazz> clazzList = clazzMapper.list(clazzQueryParam);
        //解析查询结果并封装
        Page<Clazz> p = (Page<Clazz>)clazzList;
        return new PageResult<Clazz>(p.getTotal(), p.getResult());
    }

    @Override
    public void deleteById(Integer id) {
        Integer count = studentMapper.countByClazzId(id);
        if(count>0){
            throw new BusinessException("班级下有学员, 不能直接删除~");
        }
        clazzMapper.deleteById(id);
    }

    @Override
    public void save(Clazz clazz) {
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.insert(clazz);
    }

    @Override
    public Clazz getInfo(Integer id) {
        return clazzMapper.getInfo(id);
    }

    @Override
    public void update(Clazz clazz) {
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.update(clazz);
    }

    @Override
    public List<Clazz> findAll() {
        return clazzMapper.findAll();
    }
}

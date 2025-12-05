package com.chy.controller;

import com.chy.anno.Log;
import com.chy.pojo.Clazz;
import com.chy.pojo.ClazzQueryParam;
import com.chy.pojo.PageResult;
import com.chy.pojo.Result;
import com.chy.service.ClazzService;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/clazzs")
@RestController
public class ClazzController{
    @Autowired
    private ClazzService clazzService;

    //条件分页查询班级
    @GetMapping
    public Result page(ClazzQueryParam clazzQueryParam){
        PageResult<Clazz> pageResult = clazzService.page(clazzQueryParam);
        return Result.success(pageResult);
    }
    @Log
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        clazzService.deleteById(id);
        return Result.success();
    }
    @Log
    @PostMapping
    public Result save(@RequestBody Clazz clazz){
        clazzService.save(clazz);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        Clazz clazz = clazzService.getInfo(id);
        return Result.success(clazz);
    }
    @Log
    @PutMapping
    public Result update(@RequestBody Clazz clazz){
        clazzService.update(clazz);
        return Result.success();
    }
    @GetMapping("/lists")
    public Result findAll(){
        List<Clazz> clazzList = clazzService.findAll();
        return Result.success(clazzList);
    }
}

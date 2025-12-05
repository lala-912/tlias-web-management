package com.chy.mapper;

import com.chy.pojo.EmpExpr;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
//员工工作经历
public interface EmpExprMapper {
    void insertBatch(List<EmpExpr> exprList);

    void deleteByEmpIds(List<Integer> empIds);
}

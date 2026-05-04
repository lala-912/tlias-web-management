package com.chy.service.impl;

import com.chy.mapper.EmpExprMapper;
import com.chy.mapper.EmpLogMapper;
import com.chy.mapper.EmpMapper;
import com.chy.pojo.*;
import com.chy.service.EmpLogService;
import com.chy.service.EmpService;
import com.chy.mapper.RefreshTokenMapper;
import com.chy.utils.JwtUtils;
import com.chy.utils.Sha256Utils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private EmpExprMapper empExprMapper;
    @Autowired
    private EmpLogService empLogService;
    @Autowired
    private RefreshTokenMapper refreshTokenMapper;
    @Override
    public PageResult<Emp> page(EmpQueryParam empQueryParam) {
//        设置分页参数
        PageHelper.startPage(empQueryParam.getPage(), empQueryParam.getPageSize());
        //执行查询
        List<Emp> empList = empMapper.list(empQueryParam);
        //解析查询结果并封装
        Page<Emp> p = (Page<Emp>)empList;
        return new PageResult<Emp>(p.getTotal(), p.getResult());
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Emp emp) throws Exception{
        try {
            emp.setCreateTime(LocalDateTime.now());
            emp.setUpdateTime(LocalDateTime.now());
            empMapper.insert(emp);

            List<EmpExpr> exprList = emp.getExprList();
            if(!CollectionUtils.isEmpty(exprList)){
                exprList.forEach(empExpr -> {
                    empExpr.setEmpId(emp.getId());
                });
                empExprMapper.insertBatch(exprList);
            }
        } finally {
            EmpLog empLog = new EmpLog(null, LocalDateTime.now(), "新增员工" + emp);
            empLogService.insertLog(empLog);
        }

    }
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Integer> ids) {
        empMapper.deleteByIds(ids);
        empExprMapper.deleteByEmpIds(ids);
    }

    @Override
    public Emp getInfo(Integer id) {
        return empMapper.getById(id);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Emp emp) {
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.updateById(emp);

        //根据ID修改员工的工作经历信息
        //2.1现根据员工ID删除拥有工作经历
        empExprMapper.deleteByEmpIds(Arrays.asList(emp.getId()));
        //2.2添加
        List<EmpExpr> exprList = emp.getExprList();
        if(!CollectionUtils.isEmpty(exprList)){
            exprList.forEach(empExpr -> empExpr.setEmpId(emp.getId()));
            empExprMapper.insertBatch(exprList);
        }
    }

    @Override
    public LoginInfo login(Emp emp) {
        Emp e = empMapper.selectByUsernameAndPassword(emp);
        if(e != null){
            log.info("登陆成功，员工信息：{}", e);
            // 生成 access token（15分钟）
            Map<String,Object> claims = new HashMap<>();
            claims.put("id",e.getId());
            claims.put("Username",e.getUsername());
            String accessToken = JwtUtils.generateToken(claims);

            // 生成 refresh token（7天）
            String rawRefreshToken = UUID.randomUUID().toString().replace("-", "");
            String refreshTokenHash = Sha256Utils.hash(rawRefreshToken);
            RefreshToken rt = new RefreshToken();
            rt.setUserId(e.getId());
            rt.setTokenHash(refreshTokenHash);
            rt.setExpiresAt(LocalDateTime.now().plusDays(7));
            refreshTokenMapper.insert(rt);

            return new LoginInfo(e.getId(), e.getUsername(), e.getName(), accessToken, rawRefreshToken);
        }
        return null;
    }

    public String refreshAccessToken(String rawRefreshToken) {
        String hash = Sha256Utils.hash(rawRefreshToken);
        // 检查黑名单
        if (refreshTokenMapper.countInBlacklist(hash) > 0) {
            log.warn("refresh token 已被列入黑名单");
            return null;
        }
        RefreshToken rt = refreshTokenMapper.findValidToken(hash);
        if (rt == null) {
            log.warn("refresh token 无效或已过期");
            return null;
        }
        log.info("refresh token 有效，刷新 access token, userId={}", rt.getUserId());
        Map<String,Object> claims = new HashMap<>();
        claims.put("id", rt.getUserId());
        claims.put("Username", ""); //重新生成时从 refresh_token 表无法得知 username，可以扩展表字段
        return JwtUtils.generateToken(claims);
    }

    public void logout(String rawRefreshToken) {
        String hash = Sha256Utils.hash(rawRefreshToken);
        refreshTokenMapper.addToBlacklist(hash);
        log.info("refresh token 已加入黑名单");
    }
}

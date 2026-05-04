package com.chy.service.impl;

import com.chy.mapper.EmpExprMapper;
import com.chy.mapper.EmpLogMapper;
import com.chy.mapper.EmpMapper;
import com.chy.mapper.RefreshTokenMapper;
import com.chy.pojo.*;
import com.chy.service.EmpLogService;
import com.chy.service.EmpService;
import com.chy.utils.JwtUtils;
import com.chy.utils.Sha256Utils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;

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
    @Autowired
    private JwtUtils jwtUtils;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public PageResult<Emp> page(EmpQueryParam empQueryParam) {
        PageHelper.startPage(empQueryParam.getPage(), empQueryParam.getPageSize());
        List<Emp> empList = empMapper.list(empQueryParam);
        Page<Emp> p = (Page<Emp>) empList;
        return new PageResult<Emp>(p.getTotal(), p.getResult());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Emp emp) throws Exception {
        try {
            emp.setCreateTime(LocalDateTime.now());
            emp.setUpdateTime(LocalDateTime.now());
            // 密码BCrypt哈希
            if (emp.getPassword() != null && !emp.getPassword().isEmpty()) {
                emp.setPassword(passwordEncoder.encode(emp.getPassword()));
            }
            empMapper.insert(emp);

            List<EmpExpr> exprList = emp.getExprList();
            if (!CollectionUtils.isEmpty(exprList)) {
                exprList.forEach(empExpr -> empExpr.setEmpId(emp.getId()));
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
        // 密码变更时重新哈希
        if (emp.getPassword() != null && !emp.getPassword().isEmpty()) {
            emp.setPassword(passwordEncoder.encode(emp.getPassword()));
        }
        empMapper.updateById(emp);

        empExprMapper.deleteByEmpIds(Arrays.asList(emp.getId()));
        List<EmpExpr> exprList = emp.getExprList();
        if (!CollectionUtils.isEmpty(exprList)) {
            exprList.forEach(empExpr -> empExpr.setEmpId(emp.getId()));
            empExprMapper.insertBatch(exprList);
        }
    }

    @Override
    public LoginInfo login(Emp emp) {
        Emp e = empMapper.selectByUsername(emp.getUsername());
        if (e == null) {
            log.warn("用户不存在: {}", emp.getUsername());
            return null;
        }

        String storedPwd = e.getPassword();
        // 兼容旧数据：明文密码迁移到BCrypt
        if (!storedPwd.startsWith("$2a$")) {
            if (!storedPwd.equals(emp.getPassword())) {
                return null;
            }
            storedPwd = passwordEncoder.encode(storedPwd);
            empMapper.updatePassword(e.getId(), storedPwd);
            e.setPassword(storedPwd);
        } else {
            if (!passwordEncoder.matches(emp.getPassword(), storedPwd)) {
                return null;
            }
        }

        log.info("登陆成功，员工信息：{}", e);

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", e.getId());
        claims.put("Username", e.getUsername());
        claims.put("role", e.getRole() != null ? e.getRole() : "user");
        String accessToken = jwtUtils.generateToken(claims);

        String rawRefreshToken = UUID.randomUUID().toString().replace("-", "");
        String refreshTokenHash = Sha256Utils.hash(rawRefreshToken);
        RefreshToken rt = new RefreshToken();
        rt.setUserId(e.getId());
        rt.setTokenHash(refreshTokenHash);
        rt.setExpiresAt(LocalDateTime.now().plusDays(7));
        refreshTokenMapper.insert(rt);

        return new LoginInfo(e.getId(), e.getUsername(), e.getName(), accessToken, rawRefreshToken);
    }

    @Override
    public String refreshAccessToken(String rawRefreshToken) {
        String hash = Sha256Utils.hash(rawRefreshToken);
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
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", rt.getUserId());
        return jwtUtils.generateToken(claims);
    }

    @Override
    public void logout(String rawRefreshToken) {
        String hash = Sha256Utils.hash(rawRefreshToken);
        refreshTokenMapper.addToBlacklist(hash);
        log.info("refresh token 已加入黑名单");
    }
}
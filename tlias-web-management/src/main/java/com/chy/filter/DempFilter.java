//package com.chy.filter;
//
//import jakarta.servlet.*;
//import jakarta.servlet.annotation.WebFilter;
//import lombok.extern.slf4j.Slf4j;
//
//import java.io.IOException;
//@WebFilter(urlPatterns = "/*")
//@Slf4j
//public class DempFilter implements Filter {
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        log.info("init初始化方法");
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        log.info("拦截到了请求...");
//        filterChain.doFilter(servletRequest,servletResponse);
//    }
//
//    @Override
//    public void destroy() {
//        log.info("destroy销毁方法");
//    }
//}

package com.springboot_activiti.Filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName ProjectFilter.java
 * @Description TODO
 * @createTime 2020年05月16日 15:05:00
 */
@Component
@WebFilter(urlPatterns ={"/business/*","/workflow/*","/node/*"},filterName = "ssoFilter")
public class ProjectFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        Object account = session.getAttribute("account");
        //得到端口号后面拼接的路径
        String url = request.getRequestURI();
        //放行静态资源
        if(url.endsWith(".docx")||url.endsWith(".css")||url.endsWith(".js")||url.endsWith(".jpg") ||url.endsWith(".gif")||url.endsWith(".png")||url.endsWith(".ico")){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if (url.equals("/business/loginSubmit")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;

        }else if(url.startsWith("/node")){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }else if (account == null) {
            request.getRequestDispatcher("/business/login").forward(request, response);
            return;


        }else{
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
    }
}

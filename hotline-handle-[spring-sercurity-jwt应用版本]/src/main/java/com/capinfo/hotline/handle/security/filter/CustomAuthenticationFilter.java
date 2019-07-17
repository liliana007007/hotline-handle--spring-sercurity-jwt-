package com.capinfo.hotline.handle.security.filter;

import com.capinfo.hotline.handle.security.config.CustomUserDetails;
import com.capinfo.hotline.handle.security.model.LoginUser;
import com.capinfo.hotline.handle.utils.JwtTokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * @author longliying
 * @title: 用户账号验证类
 * CustomAuthenticationFilter 继承UsernamePasswordAuthenticationFilter,该拦截器用于获取用户登录的信息，
 * 获取用户登录信息，只需创建一个token并调用authenticationManager.authenticate()让spring-security去进行验证就可以了，
 * 不用自己查数据库再对比密码了，这一步交给spring去操作
 * @date 2019/7/1711:16
 */
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private ThreadLocal<Boolean> rememberMe = new ThreadLocal<>();

    Logger logger = LoggerFactory.getLogger(CustomAuthenticationFilter.class);

    private AuthenticationManager authenticationManager;
    public CustomAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    /**
     * 验证用户
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {
        //从输入流中获得登录的信息
        try{
            LoginUser loginUser =  new ObjectMapper().readValue(request.getInputStream(),LoginUser.class);
            rememberMe.set(loginUser.isRememberMe());
            return authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(loginUser.getUsername(),loginUser.getPassword()));
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 成功验证后调用的方法
     * @param request
     * @param response
     * @param chain
     * @param authResult //验证结果
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {
        //1、getPrincipal()方法返回一个实现了UserDetails接口的对象，就是CustomUserDetails
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
        logger.info("customUserDetails:"+customUserDetails.toString());
        //2、创建token
        String role = "";
        Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();
        for(GrantedAuthority authority:authorities){
            role = authority.getAuthority();
        }
        String token = JwtTokenUtils.createToken(customUserDetails.getUsername(),role,rememberMe.get());
        //3、将token写入响应头Header中
        // 但是这里创建的token只是单纯的token
        // 按照jwt的规定，最后请求的格式应该是 `Bearer token`
        response.setHeader("token",JwtTokenUtils.TOKEN_PREFIX+token);
    }

    /**
     * 验证失败时候调用的方法
     * @param request
     * @param response
     * @param failed 验证失败
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, ServletException {
        response.getWriter().write("authentication failed, reason: " + failed.getMessage());
    }
}

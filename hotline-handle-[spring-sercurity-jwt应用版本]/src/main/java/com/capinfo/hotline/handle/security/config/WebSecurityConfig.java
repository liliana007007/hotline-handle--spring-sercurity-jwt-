package com.capinfo.hotline.handle.security.config;

import com.capinfo.hotline.handle.security.filter.CustomAuthenticationFilter;
import com.capinfo.hotline.handle.security.filter.CustomAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @author longliying
 * @title:  Spring Security 的配置类
 * @date 2019/7/1620:00
 */
@Configuration
@EnableWebSecurity  //开启Security服务
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启全局Security注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    //加密密码的，安全第一嘛
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                //开启密码加密
                .passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                //关闭CSRF跨域
                .and().csrf().disable()
                .authorizeRequests()
                //测试用资源，需要验证了的用户才能访问
                .antMatchers("/tasks/**").authenticated()
//                .antMatchers(HttpMethod.DELETE,"/tasks/**").hasRole("ROLE_ADMIN")
//                .antMatchers(HttpMethod.DELETE,"/tasks/**").hasRole("ADMIN")
                //其他都放行了
                .anyRequest().permitAll()
                .and()
                .addFilter(new CustomAuthenticationFilter(authenticationManager()))
                .addFilter(new CustomAuthorizationFilter(authenticationManager()))
                //不要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        //设置拦截忽略文件夹，可以对静态资源放行
//        web.ignoring().antMatchers("/css/**","/js/**");
//    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

}

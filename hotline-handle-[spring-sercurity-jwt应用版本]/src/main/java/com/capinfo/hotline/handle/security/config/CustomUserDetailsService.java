package com.capinfo.hotline.handle.security.config;

import com.capinfo.hotline.handle.sys.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
/**
 * @author longliying
 * @title: 自定义 UserDetailsService ，将用户信息和权限注入进来
 * @date 2019/7/1619:51
 */
@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    //临时注入下，对密码进行加密
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 重写loadUserByUsername 方法
     * @param username
     * @return UserDetails信息
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //此处应该从数据库中获得用户及密码,此处简单弄
//        username = "long";
        String  password=null;
        String  role=null;
        if("long".equals(username)){
            password = bCryptPasswordEncoder.encode("123456");
//        String  password = "123456";
            role = "ROLE_ADMIN";
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        //返回UserDetails实现类
        return new CustomUserDetails(user);
    }
}

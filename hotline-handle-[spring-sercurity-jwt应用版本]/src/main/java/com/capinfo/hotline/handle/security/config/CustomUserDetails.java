package com.capinfo.hotline.handle.security.config;

import com.capinfo.hotline.handle.sys.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * @author longliying
 * @title: CustomUserDetails UserDetails的实现类，用于接受实际用户实体信息，转换为UserDetails实体信息
 * @date 2019/7/17 10:42
 */
public class CustomUserDetails implements UserDetails {
    private String id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(){

    }

    //写一个能直接使用实际实体user创建jwtUser的构造器
    public CustomUserDetails(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        authorities = Collections.singleton(new SimpleGrantedAuthority(user.getRole()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    //账号是否未过期，默认是false，记得要改一下
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //账号是否未锁定，默认是false，记得要改一下
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //账号凭证是否未过期，默认是false，记得还要改一下
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //默认也是false，记得改一下
    @Override
    public boolean isEnabled() {
        return true;
    }

    // 我自己重写打印下信息看的
    @Override
    public String toString() {
        return "CustomUserDetails{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}

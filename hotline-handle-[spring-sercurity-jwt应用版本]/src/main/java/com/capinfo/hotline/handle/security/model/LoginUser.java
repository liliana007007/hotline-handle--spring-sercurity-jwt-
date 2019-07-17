package com.capinfo.hotline.handle.security.model;

import lombok.Data;

/**
 * @author longliying
 * @title: LoginUser 登录用户信息类
 * @date 2019/7/1711:28
 */
@Data
public class LoginUser {
    private String username;
    private String password;
    private boolean rememberMe;
}

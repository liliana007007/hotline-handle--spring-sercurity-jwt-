package com.capinfo.hotline.handle.sys.entity;

import lombok.Data;

/**
 * @author longliying
 * @title: User 用户类
 * @date 2019/7/1710:54
 */
@Data
public class User {
    private String id;
    private String username;
    private String password;
    private String role;
}

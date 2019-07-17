package com.capinfo.hotline.handle.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;

/**
 * @author longliying
 * @title: JwtTokenUtils，jwt工具类
 * @date 2019/7/17 10:29
 */
public class JwtTokenUtils {
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private static final String SECRET = "jwtsecretdemo";
    private static final String ISS = "echisan";

    // 角色的key
    private static final String ROLE_CLAIMS = "rol";

    // 过期时间是3600秒，既是1个小时
    private static final long EXPIRATION = 3600L;

    // 选择了记住我之后的过期时间为7天
    private static final long EXPIRATION_REMEMBER = 604800L;

    /**
     * 创建token
     * @param username 传入用户名
     * @param role 传入用户角色
     * @param isRememberMe 是否记住我，记住我token保存时间长
     * @return 返回创建的token
     */
    public static String createToken(String username,String role, boolean isRememberMe) {
        long expiration = isRememberMe ? EXPIRATION_REMEMBER : EXPIRATION;
        HashMap<String,Object> map = new HashMap<>();
        map.put(ROLE_CLAIMS,role);
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setClaims(map)
                .setIssuer(ISS)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();
    }

    /**
     * 通过token中获取用户名
     * @param token
     * @return 返回token中保存的用户名
     */
    public static String getUsername(String token){
        return getTokenBody(token).getSubject();
    }

    /**
     * 通过token获得用户角色
     * @param token
     * @return
     */
    public static String getUserRole(String token){
        return (String)getTokenBody(token).get(ROLE_CLAIMS);
    }
    /**
     * token是否已过期
     * @param token
     * @return true 已过期， false 未过期
     */
    public static boolean isExpiration(String token){
        return getTokenBody(token).getExpiration().before(new Date());
    }

    /**
     * 根据token获得存储的Token Body
     * @param token
     * @return
     */
    private static Claims getTokenBody(String token){
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}

package com.atguigu.gmall.passport.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.Base64UrlCodec;

import java.util.Map;

public class JwtUtil {

    /**
     *  生成Token
     * @param key 公共部分
     * @param param 私有部分
     * @param salt 盐值
     * @return Token
     */
    public static String encode(String key,Map<String,Object> param,String salt){
        if(salt!=null){
            key+=salt;
        }
        Base64UrlCodec base64UrlCodec = new Base64UrlCodec();
        key = base64UrlCodec.encode(key.getBytes());
        JwtBuilder jwtBuilder = Jwts.builder().signWith(SignatureAlgorithm.HS256,key);

        jwtBuilder = jwtBuilder.setClaims(param);

        return jwtBuilder.compact();

    }


    public  static Map<String,Object> decode(String token , String key, String salt){
        Claims claims;
        if (salt!=null){
            key+=salt;
        }
        Base64UrlCodec base64UrlCodec = new Base64UrlCodec();
        key = base64UrlCodec.encode(key.getBytes());
        try {
            claims= Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch ( JwtException e) {
            return null;
        }
        return  claims;
    }

}

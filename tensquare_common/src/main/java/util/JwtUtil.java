package util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Date;

/**
 * jwt工具类
 * 创建token
 * 解析token
 */
@ConfigurationProperties("jwt.config")
public class JwtUtil {

    private String key ;  //秘钥 尽量不要将一些非固定值写死在代码中

    private long ttl ;//提取到配置文件设置一个小时过期 毫秒级别 60*60*1000

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    /**
     * 生成JWT
     *
     * @param id
     * @param subject
     * @return
     */
    public String createJWT(String id, String subject, String roles) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder().setId(id)  //用户id
                .setSubject(subject)  //用户名
                .setIssuedAt(now)  //创建时间
                .signWith(SignatureAlgorithm.HS256, key).claim("roles", roles);  //签名加密算法 以及角色设置 roles：user  admin
        if (ttl > 0) { //如果过期时间小于0 设置就没有意义了
            builder.setExpiration( new Date( nowMillis + ttl));  //过期时间
        }
        return builder.compact();
    }

    /**
     * 解析JWT
     * @param jwtStr
     * @return
     */
    public Claims parseJWT(String jwtStr){
        return  Jwts.parser()
                .setSigningKey(key)  //key就是秘钥
                .parseClaimsJws(jwtStr) //需要解析的token
                .getBody(); //获取载荷信息
    }

}
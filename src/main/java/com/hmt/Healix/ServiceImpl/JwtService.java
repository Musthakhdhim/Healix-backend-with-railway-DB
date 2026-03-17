package com.hmt.Healix.ServiceImpl;

import com.hmt.Healix.Entities.Role;
import com.hmt.Healix.Entities.Users;
import com.hmt.Healix.config.JwtConfig;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
@AllArgsConstructor
public class JwtService {

    private final JwtConfig jwtConfig;

    public String generateToken(Users user){
        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("username",user.getUsername())
                .claim("email",user.getEmail())
                .claim("role",user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*jwtConfig.getAccessTokenExpiration()))
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes()))
                .compact();
    }

    public String generateRefreshToken(Users user){
        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("username",user.getUsername())
                .claim("email",user.getEmail())
                .claim("role",user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*jwtConfig.getRefreshTokenExpiration()))
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes()))
                .compact();
    }

    public boolean validateToken(String token){
        try{
            var claims=Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getExpiration().after(new Date());
        }
        catch(JwtException ex){
            return false;
        }
    }

    public Long getIdFromToken(String token){
        var claims=Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Long.valueOf(claims.getSubject());
    }

    public Role getRoleFromToken(String token){
        var claims=Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Role.valueOf(claims.get("role",String.class));
    }

    public String getTokenFromAuthorization(HttpServletRequest request){
        String authHeader= request.getHeader("Authorization");
        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            throw new RuntimeException("token not found in the request");
        }
        String token=authHeader.replace("Bearer ","");

        return token;
    }
}

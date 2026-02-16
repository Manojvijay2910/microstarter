package com.starter.security;

import java.util.Date;
import java.security.Key;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;

import com.starter.properties.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final JwtProperties jwtProperties;

    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    // 🔐 Generate Keys
    private Key getAccessKey() {
        return Keys.hmacShaKeyFor(
                jwtProperties.getAccess().getSecret()
                        .getBytes(StandardCharsets.UTF_8)
        );
    }

    private Key getRefreshKey() {
        return Keys.hmacShaKeyFor(
                jwtProperties.getRefresh().getSecret()
                        .getBytes(StandardCharsets.UTF_8)
        );
    }

    // ✅ Generate Access Token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(
                        System.currentTimeMillis() +
                        jwtProperties.getAccess().getExpiration().toMillis()
                ))
                .signWith(getAccessKey()) // no algorithm needed
                .compact();
    }

    // ✅ Generate Refresh Token
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(
                        System.currentTimeMillis() +
                        jwtProperties.getRefresh().getExpiration().toMillis()
                ))
                .signWith(getRefreshKey())
                .compact();
    }

    // ✅ Extract User name
    public String extractUsername(String token) {
        return getClaims(token, getAccessKey()).getSubject();
    }

    // ✅ Validate Access Token
    public boolean validateToken(String token, String username) {
        return username.equals(extractUsername(token)) &&
                !isTokenExpired(token, getAccessKey());
    }

    // ✅ Validate Refresh Token
    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getRefreshKey())
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 🔍 Extract Claims
    private Claims getClaims(String token, Key key) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token, Key key) {
        return getClaims(token, key)
                .getExpiration()
                .before(new Date());
    }
}

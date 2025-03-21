package com.example.learn_spring_boot.sercurity.token;

import com.example.learn_spring_boot.entities.Users;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.Role;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    public static final String SECRET = "QHMBQfsViR66wU3Yx/MOdkKcHdmJeRy4JdbDbrjmZdfu35Q7yzH6b3vJCrQcNgoOEFfsGyhOeF5Pby7R+YzG0w==";

    public String generateToken(Users user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("roles", user.getRoles().stream()
                .map(role -> role.getName())
                .toList());

        claims.put("userName", user.getUsername());
        //claims.put("avatar", user.getAvatar());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 60 * 1000))
                .signWith(getSiginKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Map<String, Object> extractClaims, Users user) {
        extractClaims.put("id", user.getId());
        extractClaims.put("email", user.getEmail());
        extractClaims.put("roles", user.getRoles().stream()
                .map(role -> role.getName())
                .toList());
        extractClaims.put("userName", user.getUsername());
        //extractClaims.put("avatar", user.getAvatar());
        return Jwts.builder()
                .setClaims(extractClaims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 30 * 24 * 60 * 60 * 60 * 1000))
                .signWith(getSiginKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // todo xác nhận quyền sở hữu
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSiginKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSiginKey() {
        byte[] key = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(key);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // todo kiểm tra hết hạn
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }


//    public void decodeTheToken(String token, HttpServletRequest request) {
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(getSiginKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        String fullName = claims.get("fullName", String.class);
//        String email = claims.get("email", String.class);
//        Long id = claims.get("id", Long.class);
//        String role = claims.get("role", String.class);
//
//        HttpSession session = request.getSession();
//        var user = UserDetailToken.builder().id(id).role(role).fullName(fullName).email(email).build();
//        if (user.getRole().equals("ROLE_USER")) {
//            session.setAttribute("customer", user);
//        } else {
//            session.setAttribute("employee", user);
//        }
//    }

}

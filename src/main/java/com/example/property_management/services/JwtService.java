package com.example.property_management.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "76520129db3872d5a5519085e1d598c95df1c903aa748ab6880cc06fbdebadb7e06c0989dca4eb206b57a2f090d0fa6dc99077e21689a5454dad2bf4e1164f5b1a5370c5a55ac60be28e8ff69482a6a273bb22daf7fa4f5c20c03119127b02c0c10c979f064315b910ee3a54e6a650150fc33c7641949393ddbd5cd496b9952336f80a6d3bbc77f9d156ef79b77c164ba55f9f5feebe6b260294eadb73b0ed4e0b27e1f8850393c14ee25b9309831350de9852f940d99c93411a2e12a0f0318b5c38efcee0ee586e849c8c311c528fb48015b9580ca50fa2af4030b4fd7f66e6138cd8f72948fd1f34893b4ac1acb9be59f95fa981e0eabef32f1263b21f0228";

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails){
        return generateTokens(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String userName = extractUserName(token);
        return  (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public  boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateTokens(Map<String, Object> extractClaims, UserDetails userDetails){
        return Jwts
                .builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+10000*60*24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

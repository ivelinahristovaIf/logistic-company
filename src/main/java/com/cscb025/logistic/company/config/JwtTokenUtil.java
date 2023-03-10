package com.cscb025.logistic.company.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import com.cscb025.logistic.company.enums.UserRole;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@Component
public class JwtTokenUtil {

    private static final int FIRST_ELEMENT_INDEX = 0;

    private static final int ROLE_INDEX = 1;

    @Value("${jwt.expiration}")
    private long jwtTokenValidity;

    @Value("${jwt.secret}")
    private String secret;

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles",
                userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        final SignatureAlgorithm sa = HS256;
        final SecretKeySpec secretKeySpec = new SecretKeySpec(getSigningKey().getEncoded(), sa.getJcaName());
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtTokenValidity))
                .signWith(secretKeySpec, sa)
                .compact();
    }

    private Key getSigningKey() {
        final byte[] keyBytes = Decoders.BASE64.decode(this.secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(final String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractFirstName(final String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get("firstName").toString();
    }

    public String extractUUID(final String token) {
        final Claims claims = extractAllClaims(token);
        return UUID.fromString(String.valueOf(claims.get("uuid"))).toString();
    }

    public String extractLastName(final String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get("lastName").toString();
    }

    public UserRole extractRole(final String token) {
        final Claims claims = extractAllClaims(token);
        return UserRole.valueOf(
                ((ArrayList<?>) claims.get("roles")).get(FIRST_ELEMENT_INDEX).toString().split("_")[ROLE_INDEX]);
    }

    public Date extractExpiration(final String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(final String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean isTokenValid(final String token) {
        return (!isTokenExpired(token) && checkIfSecretKeyValid(token));
    }

    private boolean checkIfSecretKeyValid(final String jwtToken) {
        final SignatureAlgorithm sa = HS256;
        final SecretKeySpec secretKeySpec = new SecretKeySpec(getSigningKey().getEncoded(), sa.getJcaName());
        final String[] chunks = jwtToken.split("\\.");
        final String tokenWithoutSignature = chunks[0] + "." + chunks[1];
        final String signature = chunks[2];
        final DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(sa, secretKeySpec,
                Decoders.BASE64URL);
        return validator.isValid(tokenWithoutSignature, signature);
    }

}
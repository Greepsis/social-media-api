package com.socialmedia.rest.security.jwt;

import com.socialmedia.serviceimpl.service.security.CustomUserDetailsService;
import io.jsonwebtoken.*;
import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtToken {
    @Value("${spring.auth.jwt.secure_key}")
    private String secret;
    @Value("${spring.auth.jwt.validity-in-milliseconds}")
    private long validityInMilliseconds;

    private final CustomUserDetailsService customUserDetailsService;

    public String createToken(String email) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(signingKey, signatureAlgorithm)
                .compact();
    }
    public String getEmail(String token){
        return Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter
                        .parseBase64Binary(secret))
                .build().parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(DatatypeConverter
                            .parseBase64Binary(secret))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expiredJwtException) {
            log.error("Token expired");
        } catch (UnsupportedJwtException unsupportedJwtException) {
            log.error("Unsupported jwt");
        } catch (MalformedJwtException malformedJwtException) {
            log.error("Malformed jwt");
        } catch (Exception exception) {
            log.error("Invalid token");
        }
        return false;
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(getEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails,"", userDetails.getAuthorities());
    }
}

package com.example.capstone.security.jwt;


import com.example.capstone.security.service.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtsecret;

    @Value("${jwt.expiration}")
    private int jwtExpiration;

    //creazione del JWT

    public String createJwtToken(Authentication authentication){

        //recupero principal
        UserDetailsImpl userPrincipal= (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles=userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+jwtExpiration))
                .claim("roles",roles)
                .signWith(getkey(),SignatureAlgorithm.HS256)
                .compact();

    }


    // recupero della chiave
    public Key getkey(){
        byte[] decodedKey = Base64.getDecoder().decode(jwtsecret);
        Key key = Keys.hmacShaKeyFor(decodedKey);

        // Log della chiave per verificare
        System.out.println("Chiave segreta decodificata: " + Base64.getEncoder().encodeToString(decodedKey));

        return key;
    }

    // recupero username dal JWT
    public String getUsernameFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(getkey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    //recupero scadenza token
    public Date getExpireDateFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(getkey()).build().parseClaimsJws(token).getBody().getExpiration();
    }


    //validazione del token
    public boolean jwtTokenValidation(String token){
        try {
            Jwts.parserBuilder().setSigningKey(getkey()).build().parseClaimsJws(token);
            return true;
        }catch (Exception e) {
            System.out.println("Errore di validazione JWT: " + e.getMessage());
            return false;
        }
    }





    public List<String> getRolesFromToken(String token){
       Claims claim= Jwts.parserBuilder().setSigningKey(getkey()).build().parseClaimsJws(token).getBody();
       return claim.get("roles", List.class);
    }
}

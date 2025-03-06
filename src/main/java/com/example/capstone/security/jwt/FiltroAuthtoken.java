package com.example.capstone.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FiltroAuthtoken extends OncePerRequestFilter {

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserDetailsService userDetailsService;


    private String analizeJwt(HttpServletRequest request){
        String headAutentication=request.getHeader("Authorization");

        if(StringUtils.hasText(headAutentication) && (headAutentication.startsWith("Bearer "))){
            return headAutentication.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String token = analizeJwt(request);
        if (token != null) {
            System.out.println("Token ricevuto: " + token);

            if (jwtUtils.jwtTokenValidation(token)) {
                String username = jwtUtils.getUsernameFromToken(token);
                System.out.println("Utente autenticato: " + username);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    List<String> roles=jwtUtils.getRolesFromToken(token);

                    List<GrantedAuthority> authorities=roles.stream()
                            .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } else {
                System.out.println("Token non valido");
            }
        } else {
            System.out.println("Nessun token trovato nella richiesta");
        }

        filterChain.doFilter(request, response);

    }
}

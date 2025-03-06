package com.example.capstone.security.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

    // rilevamento di eventuali errori di autenticazione
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {


        //ritorno al client con status

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // e ora il contenuto


        final Map<String,Object> errors= new HashMap<>();
        errors.put("stato",HttpServletResponse.SC_UNAUTHORIZED);
        errors.put("errore","Autorizzazione non valida");
        errors.put("messaggio",authException.getMessage());
        errors.put("path",request.getServletPath());

        //conversione da un Map Java e Json
        final ObjectMapper errorsMapper=new ObjectMapper();
        errorsMapper.writeValue(response.getOutputStream(),errors);


    }
}

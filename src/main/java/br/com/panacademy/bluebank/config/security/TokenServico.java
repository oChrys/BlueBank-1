package br.com.panacademy.bluebank.config.security;

import br.com.panacademy.bluebank.modelo.Cliente;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenServico {

    @Value("${forum.jwt.expiration}")
    private String expiracao;

    @Value("${forum.jwt.secret}")
    private String secret;

    public String gerarToken(Authentication authenticate) {
        Date hoje = new Date();

        Cliente logado = (Cliente) authenticate.getPrincipal();

        return Jwts.builder()
                .setIssuer("API Blue Bank Turma 2 Squad 2")
                .setSubject(logado.getId().toString())
                .setIssuedAt(hoje)
                .setExpiration(new Date(hoje.getTime() + Long.parseLong(expiracao)))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

    }
}
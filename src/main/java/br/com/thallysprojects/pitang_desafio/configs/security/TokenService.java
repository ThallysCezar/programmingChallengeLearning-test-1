package br.com.thallysprojects.pitang_desafio.configs.security;

import br.com.thallysprojects.pitang_desafio.entities.Users;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${chave.secreta.artigo}")
    private String chaveSecreta;

    private Algorithm algorithm(String chave){
        return Algorithm.HMAC256(chave);
    }

    public String gerarTokenJwt(Users users) throws JWTCreationException {
        return JWT.create()
                .withIssuer("auth-artigo")
                .withSubject(users.getLogin()) // Gera o token com o login, não com o email
                .withExpiresAt(genExpirantionDate())
                .sign(algorithm(chaveSecreta));
    }

    public String validarToken(String token) throws JWTVerificationException {
        return JWT.require(algorithm(chaveSecreta))
                .withIssuer("auth-artigo")
                .build()
                .verify(token)
                .getSubject();
    }

    private Instant genExpirantionDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-02:00"));
    }

    public String recuperarToken (HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(header == null){
            return null;
        }
        return header.replace("Bearer ", "");
    }

}

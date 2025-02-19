package br.com.thallysprojects.pitang_desafio.configs.security;

import br.com.thallysprojects.pitang_desafio.entities.Users;
import br.com.thallysprojects.pitang_desafio.exceptions.UsersNotFoundException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${chave.secreta.artigo}")
    private String chaveSecreta;

    private Algorithm algorithm(String chave) {
        return Algorithm.HMAC256(chave);
    }

    public String gerarTokenJwt(Users users) throws JWTCreationException {
        return JWT.create()
                .withIssuer("auth-artigo")
                .withSubject(users.getLogin())
                .withExpiresAt(genExpirantionDate())
                .sign(algorithm(chaveSecreta));
    }

    public String validarToken(String token) throws JWTVerificationException, TokenExpiredException {
        try{
            return JWT.require(algorithm(chaveSecreta))
                    .withIssuer("auth-artigo")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (TokenExpiredException e) {
            throw new TokenExpiredException("Unauthorized - invalid session", e.getExpiredOn());
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("Unauthorized");
        }
    }

    private Instant genExpirantionDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-02:00"));
    }

    public String recuperarToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null) {
            return null;
        }
        return header.replace("Bearer ", "");
    }

}

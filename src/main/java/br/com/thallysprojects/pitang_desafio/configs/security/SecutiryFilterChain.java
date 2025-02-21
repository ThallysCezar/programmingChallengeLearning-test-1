package br.com.thallysprojects.pitang_desafio.configs.security;

import br.com.thallysprojects.pitang_desafio.entities.Users;
import br.com.thallysprojects.pitang_desafio.repositories.UsersRepository;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecutiryFilterChain extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Autowired
    UsersRepository usersRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Se a rota estiver liberada pelo SecurityConfig, não bloqueamos aqui
        String requestURI = request.getRequestURI();

        // Permitir que certas rotas passem sem autenticação
        if (requestURI.contains("/h2-console") ||
                requestURI.contains("/swagger") ||
                requestURI.contains("/v3/api-docs") ||
                requestURI.contains("/swagger-ui") ||
                requestURI.contains("/api/users") ||
                requestURI.contains("/api/signin")) {

            filterChain.doFilter(request, response);
            return;
        }


        String token = tokenService.recuperarToken(request);

        if (token == null || token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String login = tokenService.validarToken(token);
            Users user = usersRepository.findByLogin(login);

            if (user != null) {
                UserDetails userDetails = user;
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        } catch (TokenExpiredException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized - Token expired");
            response.getWriter().flush();
            return;
        } catch (JWTVerificationException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized - Invalid token");
            response.getWriter().flush();
            return;
        }

        filterChain.doFilter(request, response);
    }

}
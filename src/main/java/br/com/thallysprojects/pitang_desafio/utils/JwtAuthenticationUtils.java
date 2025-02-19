//package br.com.thallysprojects.pitang_desafio.utils;
//
//import br.com.thallysprojects.pitang_desafio.configs.security.TokenService;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationUtils extends OncePerRequestFilter {
//
//    private final TokenService tokenService;
//
//    public JwtAuthenticationUtils(TokenService tokenService) {
//        this.tokenService = tokenService;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        String token = extractToken(request);
//
//        if (token == null || token.isEmpty()) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//            return;
//        }
//
//        try {
//            DecodedJWT decodedJWT = tokenService.validateToken(token);
//            request.setAttribute("userId", decodedJWT.getSubject());
//        } catch (TokenExpiredException e) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized - invalid session");
//            return;
//        } catch (JWTVerificationException e) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//            return;
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private String extractToken(HttpServletRequest request) {
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            return authHeader.substring(7);
//        }
//        return null;
//    }
//}

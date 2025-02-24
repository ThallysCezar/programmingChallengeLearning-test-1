package br.com.thallysprojects.pitang_desafio.services;

import br.com.thallysprojects.pitang_desafio.configs.security.TokenService;
import br.com.thallysprojects.pitang_desafio.dtos.AuthenticationUserDTO;
import br.com.thallysprojects.pitang_desafio.entities.Users;
import br.com.thallysprojects.pitang_desafio.exceptions.users.UsersNotFoundException;
import br.com.thallysprojects.pitang_desafio.repositories.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("Service de Autentication")
class AuthenticationServiceTest {

    AuthenticationService service;

    AuthenticationManager manager;

    TokenService tokenService;

    UsersRepository usersRepository;

    BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.manager = Mockito.mock(AuthenticationManager.class);
        this.tokenService = Mockito.mock(TokenService.class);
        this.usersRepository = Mockito.mock(UsersRepository.class);
        this.passwordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        this.service = new AuthenticationService(manager, tokenService, usersRepository, passwordEncoder);
    }

    @Test
    @DisplayName("Deve retornar login com sucesso")
    void testLoginSuccess() {
        AuthenticationUserDTO dto = new AuthenticationUserDTO("user", "password");
        Users user = new Users();
        user.setLogin("user");
        user.setPassword("encodedPassword");
        user.setLastLogin(LocalDate.now());

        when(usersRepository.findByLogin("user")).thenReturn(user);
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(tokenService.gerarTokenJwt(user)).thenReturn("token");

        String token = service.login(dto);

        assertNotNull(token);
        assertEquals("token", token);
        verify(usersRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Deve lançar UsersNotFoundException quando houver erro ao singin com o usuário")
    void testLoginUserNotFound() {
        AuthenticationUserDTO dto = new AuthenticationUserDTO("user", "password");
        when(usersRepository.findByLogin("user")).thenReturn(null);

        UsersNotFoundException exception = assertThrows(UsersNotFoundException.class, () -> {
            service.login(dto);
        });

        assertEquals("Invalid login or password", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar UsersNotFoundException quando houver erro com as credencias do usuário")
    void testLoginInvalidPassword() {
        AuthenticationUserDTO dto = new AuthenticationUserDTO("user", "wrongPassword");
        Users user = new Users();
        user.setLogin("user");
        user.setPassword("encodedPassword");

        when(usersRepository.findByLogin("user")).thenReturn(user);
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        UsersNotFoundException exception = assertThrows(UsersNotFoundException.class, () -> {
            service.login(dto);
        });

        assertEquals("Credenciais inválidas", exception.getMessage());
    }

}
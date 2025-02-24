package br.com.thallysprojects.pitang_desafio.services;

import br.com.thallysprojects.pitang_desafio.configs.security.TokenService;
import br.com.thallysprojects.pitang_desafio.dtos.AuthenticationUserDTO;
import br.com.thallysprojects.pitang_desafio.entities.Users;
import br.com.thallysprojects.pitang_desafio.exceptions.users.UsersNotFoundException;
import br.com.thallysprojects.pitang_desafio.mappers.CarsMapper;
import br.com.thallysprojects.pitang_desafio.repositories.CarsRepository;
import br.com.thallysprojects.pitang_desafio.repositories.UsersRepository;
import br.com.thallysprojects.pitang_desafio.utils.ValidationsCars;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

//    @InjectMocks
    AuthenticationService service;

//    @Mock
    AuthenticationManager manager;

//    @Mock
    TokenService tokenService;

//    @Mock
    UsersRepository usersRepository;

//    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.manager = Mockito.mock(AuthenticationManager.class);
        this.tokenService = Mockito.mock(TokenService.class);
        this.usersRepository = Mockito.mock(UsersRepository.class);
        this.passwordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        this.service = new AuthenticationService(manager, tokenService, usersRepository, passwordEncoder);
    }

    @Test
    void testLoginSuccess() {
        // Arrange
        AuthenticationUserDTO dto = new AuthenticationUserDTO("user", "password");
        Users user = new Users();
        user.setLogin("user");
        user.setPassword("encodedPassword");
        user.setLastLogin(LocalDate.now());

        when(usersRepository.findByLogin("user")).thenReturn(user);
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(tokenService.gerarTokenJwt(user)).thenReturn("token");

        // Act
        String token = service.login(dto);

        // Assert
        assertNotNull(token);
        assertEquals("token", token);
        verify(usersRepository, times(1)).save(user);
    }

    @Test
    void testLoginUserNotFound() {
        // Arrange
        AuthenticationUserDTO dto = new AuthenticationUserDTO("user", "password");
        when(usersRepository.findByLogin("user")).thenReturn(null);

        // Act & Assert
        UsersNotFoundException exception = assertThrows(UsersNotFoundException.class, () -> {
            service.login(dto);
        });

        assertEquals("Invalid login or password", exception.getMessage());
    }

    @Test
    void testLoginInvalidPassword() {
        // Arrange
        AuthenticationUserDTO dto = new AuthenticationUserDTO("user", "wrongPassword");
        Users user = new Users();
        user.setLogin("user");
        user.setPassword("encodedPassword");

        when(usersRepository.findByLogin("user")).thenReturn(user);
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        // Act & Assert
        UsersNotFoundException exception = assertThrows(UsersNotFoundException.class, () -> {
            service.login(dto);
        });

        assertEquals("Credenciais inválidas", exception.getMessage());
    }

}
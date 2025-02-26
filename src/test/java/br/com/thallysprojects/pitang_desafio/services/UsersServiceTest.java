package br.com.thallysprojects.pitang_desafio.services;

import br.com.thallysprojects.pitang_desafio.dtos.MeDTO;
import br.com.thallysprojects.pitang_desafio.dtos.UsersDTO;
import br.com.thallysprojects.pitang_desafio.entities.UserRole;
import br.com.thallysprojects.pitang_desafio.entities.Users;
import br.com.thallysprojects.pitang_desafio.exceptions.users.UsersBadRequestException;
import br.com.thallysprojects.pitang_desafio.exceptions.users.UsersGeneralException;
import br.com.thallysprojects.pitang_desafio.exceptions.users.UsersNotFoundException;
import br.com.thallysprojects.pitang_desafio.mappers.UsersMapper;
import br.com.thallysprojects.pitang_desafio.repositories.UsersRepository;
import br.com.thallysprojects.pitang_desafio.utils.ValidationsUsers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("Service de Usuários")
class UsersServiceTest {

    private UsersService service;

    private UsersRepository repository;

    private UsersMapper mapper;

    private ValidationsUsers validationsUsers;


    @BeforeEach
    void setUp() {
        this.repository = Mockito.mock(UsersRepository.class);
        this.mapper = Mockito.mock(UsersMapper.class);
        this.validationsUsers = Mockito.mock(ValidationsUsers.class);
        this.service = new UsersService(repository, mapper, validationsUsers);
    }

    @Test
    @DisplayName("Deve retornar todos os usuários com sucesso")
    void testFindAllSuccess() {
        Users user = new Users();
        UsersDTO userDTO = new UsersDTO();

        when(repository.findAll()).thenReturn(Collections.singletonList(user));
        when(mapper.toListDTO(Collections.singletonList(user))).thenReturn(Collections.singletonList(userDTO));

        List<UsersDTO> result = service.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve lançar UsersNotFoundException quando houver erro ao procurar todos os usuários")
    void testFindAllNoUsersFound() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        UsersNotFoundException exception = assertThrows(UsersNotFoundException.class, () -> {
            service.findAll();
        });

        assertEquals("Nenhum usuário encontrado", exception.getMessage());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar um usuário, pelo id, com sucesso")
    void testFindByIdSuccess() {
        Long id = 1L;
        Users user = new Users();
        UsersDTO userDTO = new UsersDTO();

        when(repository.findById(id)).thenReturn(Optional.of(user));
        when(mapper.toDTO(user)).thenReturn(userDTO);

        UsersDTO result = service.findById(id);

        assertNotNull(result);
        verify(repository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve lançar UsersNotFoundException quando houver erro ao procurar um usuário pelo seu id")
    void testFindByIdUserNotFound() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UsersNotFoundException.class, () -> {
            service.findById(id);
        });

        verify(repository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve atualizar um usuário, pelo id, com sucesso")
    void testUpdateUserByIdSuccess() {
        Long id = 1L;
        UsersDTO dto = new UsersDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setLogin("johndoe");
        dto.setPassword("password123");
        dto.setBirthday(LocalDate.now());
        dto.setPhone("123456789");
        dto.setRole(UserRole.ADMIN);

        Users existingUser = new Users();
        existingUser.setId(id);

        when(validationsUsers.validateUserExists(id)).thenReturn(existingUser);
        doNothing().when(validationsUsers).validateEmailChange(existingUser, dto.getEmail());
        doNothing().when(validationsUsers).validateLoginChange(existingUser, dto.getLogin());
        when(validationsUsers.isValidPassword(dto.getPassword())).thenReturn(true);
        when(repository.saveAndFlush(existingUser)).thenReturn(existingUser);

        service.updateUserById(id, dto);

        verify(validationsUsers, times(1)).validateUserExists(id);
        verify(validationsUsers, times(1)).validateEmailChange(existingUser, dto.getEmail());
        verify(validationsUsers, times(1)).validateLoginChange(existingUser, dto.getLogin());
        verify(repository, times(1)).saveAndFlush(existingUser);
    }


    @Test
    @DisplayName("Deve lançar UsersNotFoundException quando houver erro ao atualizar um usuário pelo seu id")
    void testUpdatedNotFound() {
        Long id = 1L;
        UsersDTO dto = new UsersDTO();

        when(validationsUsers.validateUserExists(id)).thenThrow(new UsersNotFoundException("Usuário não encontrado", HttpStatus.NOT_FOUND.value()));

        UsersNotFoundException exception = assertThrows(UsersNotFoundException.class, () -> {
            service.updateUserById(id, dto);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(validationsUsers, times(1)).validateUserExists(id);
        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    @DisplayName("Deve lançar UsersGeneralException quando houver erro ao atualizar um usuário pelo seu id com email invalido")
    void testUpdateUserByIdInvalidEmailChange() {
        Long id = 1L;
        UsersDTO dto = new UsersDTO();
        dto.setEmail("invalid-email");

        Users existingUser = new Users();
        existingUser.setId(id);

        when(validationsUsers.validateUserExists(id)).thenReturn(existingUser);
        doThrow(new UsersGeneralException("Mudança de email inválida", HttpStatus.BAD_REQUEST.value()))
                .when(validationsUsers).validateEmailChange(existingUser, dto.getEmail());

        UsersGeneralException exception = assertThrows(UsersGeneralException.class, () -> {
            service.updateUserById(id, dto);
        });

        assertEquals("Mudança de email inválida", exception.getMessage());
        verify(validationsUsers, times(1)).validateUserExists(id);
        verify(validationsUsers, times(1)).validateEmailChange(existingUser, dto.getEmail());
        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    @DisplayName("Deve lançar UsersGeneralException quando houver erro ao atualizar um usuário pelo seu id com login invalido")
    void testUpdateUserByIdInvalidLoginChange() {
        Long id = 1L;
        UsersDTO dto = new UsersDTO();
        dto.setLogin("invalid login");

        Users existingUser = new Users();
        existingUser.setId(id);

        when(validationsUsers.validateUserExists(id)).thenReturn(existingUser);
        doThrow(new UsersGeneralException("Mudança de login inválida", HttpStatus.BAD_REQUEST.value()))
                .when(validationsUsers).validateLoginChange(existingUser, dto.getLogin());

        UsersGeneralException exception = assertThrows(UsersGeneralException.class, () -> {
            service.updateUserById(id, dto);
        });

        assertEquals("Mudança de login inválida", exception.getMessage());
        verify(validationsUsers, times(1)).validateUserExists(id);
        verify(validationsUsers, times(1)).validateLoginChange(existingUser, dto.getLogin());
        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    @DisplayName("Deve lançar UsersGeneralException quando houver erro desconhecido ao atualizar um usuário pelo seu id")
    void testUpdateUserByIdUnknownError() {
        Long id = 1L;
        UsersDTO dto = new UsersDTO();

        Users existingUser = new Users();
        existingUser.setId(id);

        when(validationsUsers.validateUserExists(id)).thenReturn(existingUser);
        when(repository.saveAndFlush(existingUser)).thenThrow(new RuntimeException("Erro desconhecido"));

        UsersGeneralException exception = assertThrows(UsersGeneralException.class, () -> {
            service.updateUserById(id, dto);
        });

        assertEquals("Erro desconhecido ao atualizar o usuário.", exception.getMessage());
        verify(validationsUsers, times(1)).validateUserExists(id);
        verify(repository, times(1)).saveAndFlush(existingUser);
    }

    @Test
    @DisplayName("Deve salvar um usuário com sucesso")
    void testSaveSuccess() {
        UsersDTO dto = new UsersDTO();
        dto.setEmail("john.doe@example.com");
        dto.setLogin("johndoe");
        dto.setPassword("password123");

        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.getPassword());

        Users user = new Users();
        user.setPassword(encryptedPassword);
        when(mapper.toEntity(dto)).thenReturn(user);
        when(repository.save(user)).thenReturn(user);
        when(mapper.toDTO(user)).thenReturn(dto);

        doNothing().when(validationsUsers).validatePasswordFormat(Mockito.anyString());

        service.save(dto);

        verify(validationsUsers, times(1)).validateEmailExists(dto.getEmail(), repository);
        verify(validationsUsers, times(1)).validateLoginExists(dto.getLogin(), repository);
        verify(validationsUsers, times(1)).validateEmailFormat(dto.getEmail());
        verify(validationsUsers, times(1)).validateLoginFormat(dto.getLogin());
        verify(validationsUsers, times(1)).validatePasswordFormat(Mockito.anyString());
        verify(validationsUsers, times(1)).validateMissingFields(dto);
        verify(repository, times(1)).save(user);

        verify(repository, times(1)).save(argThat(u -> u.getPassword().equals(encryptedPassword)));
    }

    @Test
    @DisplayName("Deve lançar UsersBadRequestException quando houver erro ao salvar um usuário com um email já existente")
    void testSaveEmailAlreadyExists() {
        UsersDTO dto = new UsersDTO();
        dto.setEmail("john.doe@example.com");

        doThrow(new UsersBadRequestException("Email already exists", HttpStatus.BAD_REQUEST.value()))
                .when(validationsUsers).validateEmailExists(dto.getEmail(), repository);


        UsersGeneralException exception = assertThrows(UsersGeneralException.class, () -> {
            service.save(dto);
        });

        assertEquals("Email already exists", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getErrorCode());
        verify(validationsUsers, times(1)).validateEmailExists(dto.getEmail(), repository);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar UsersBadRequestException quando houver erro ao salvar um usuário com um login já existente")
    void testSaveLoginAlreadyExists() {
        UsersDTO dto = new UsersDTO();
        dto.setLogin("johndoe");

        doThrow(new UsersBadRequestException("Login already exists", HttpStatus.BAD_REQUEST.value()))
                .when(validationsUsers).validateLoginExists(dto.getLogin(), repository);

        UsersGeneralException exception = assertThrows(UsersGeneralException.class, () -> {
            service.save(dto);
        });

        assertEquals("Login already exists", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getErrorCode());
        verify(validationsUsers, times(1)).validateLoginExists(dto.getLogin(), repository);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar UsersBadRequestException quando houver erro ao salvar um usuário com um email no formato inesperado")
    void testSaveInvalidEmailFormat() {
        UsersDTO dto = new UsersDTO();
        dto.setEmail("invalid-email");

        doThrow(new UsersBadRequestException("Invalid fields, email", HttpStatus.BAD_REQUEST.value()))
                .when(validationsUsers).validateEmailFormat(dto.getEmail());

        UsersGeneralException exception = assertThrows(UsersGeneralException.class, () -> {
            service.save(dto);
        });

        assertEquals("Invalid fields, email", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getErrorCode());
        verify(validationsUsers, times(1)).validateEmailFormat(dto.getEmail());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar UsersBadRequestException quando houver erro ao salvar um usuário com um login no formato inesperado")
    void testSaveInvalidLoginFormat() {
        UsersDTO dto = new UsersDTO();
        dto.setLogin("invalid login");

        doThrow(new UsersBadRequestException("Invalid fields, login", HttpStatus.BAD_REQUEST.value()))
                .when(validationsUsers).validateLoginFormat(dto.getLogin());

        UsersGeneralException exception = assertThrows(UsersGeneralException.class, () -> {
            service.save(dto);
        });

        assertEquals("Invalid fields, login", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getErrorCode());
        verify(validationsUsers, times(1)).validateLoginFormat(dto.getLogin());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar UsersBadRequestException quando houver erro ao salvar um usuário com um password no formato inesperado")
    void testSaveInvalidPasswordFormat() {
        UsersDTO dto = new UsersDTO();
        dto.setPassword("weak");

        doThrow(new UsersBadRequestException("Invalid fields, password", HttpStatus.BAD_REQUEST.value()))
                .when(validationsUsers).validatePasswordFormat(dto.getPassword());

        UsersGeneralException exception = assertThrows(UsersGeneralException.class, () -> {
            service.save(dto);
        });

        assertEquals("Invalid fields, password", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getErrorCode());
        verify(validationsUsers, times(1)).validatePasswordFormat(dto.getPassword());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar UsersBadRequestException quando houver erro ao salvar um usuário quando os campos não estiverem preenchidos")
    void testSaveMissingFields() {
        UsersDTO dto = new UsersDTO();

        doThrow(new UsersBadRequestException("Missing fields", HttpStatus.BAD_REQUEST.value()))
                .when(validationsUsers).validateMissingFields(dto);

        UsersGeneralException exception = assertThrows(UsersGeneralException.class, () -> {
            service.save(dto);
        });

        assertEquals("Missing fields", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getErrorCode());
        verify(validationsUsers, times(1)).validateMissingFields(dto);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar UsersGeneralException quando houver um erro desconhecido ao salvar um usuário")
    void testSaveUnknownError() {
        UsersDTO dto = new UsersDTO();
        dto.setEmail("john.doe@example.com");
        dto.setLogin("johndoe");
        dto.setPassword("password123");

        when(mapper.toEntity(dto)).thenThrow(new UsersGeneralException());

        UsersGeneralException exception = assertThrows(UsersGeneralException.class, () -> {
            service.save(dto);
        });

        assertEquals("Erro desconhecido ao salvar o carro", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve deletar um usuário com sucesso")
    void testDeleteUsersSuccess() {
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(true);

        service.deleteUsers(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar um UsersNotFoundException ao tentar deletar um usuário pelo seu id")
    void testDeleteUsersNotFound() {
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(false);

        UsersNotFoundException exception = assertThrows(UsersNotFoundException.class, () -> {
            service.deleteUsers(id);
        });

        assertEquals(String.format("Usuário não encontrado para o ID '%s'.", id), exception.getMessage());
        verify(repository, times(1)).existsById(id);
    }

    @Test
    @DisplayName("Deve logar com Username um usuário com sucesso")
    void testLoadUserByUsernameSuccess() {
        String email = "john.doe@example.com";
        Users user = new Users();
        user.setEmail(email);
        user.setLogin("johndoe");
        user.setPassword("encodedPassword");
        user.setRole(UserRole.ADMIN);

        when(repository.findByEmail(email)).thenReturn(user);

        UserDetails userDetails = service.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(user.getLogin(), userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        verify(repository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Deve lançar um UsernameNotFoundException com Username um usuário")
    void testLoadUserByUsernameUserNotFound() {
        String email = "nonexistent@example.com";

        when(repository.findByEmail(email)).thenReturn(null);

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername(email);
        });

        assertEquals("Usuário não encontrado com o email: " + email, exception.getMessage());
        verify(repository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Deve retornar um MeDTO com sucesso")
    void testGetLoggerDTOSuccess() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        Users user = new Users();
        MeDTO meDTO = new MeDTO();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("johndoe");
        when(repository.findByLogin("johndoe")).thenReturn(user);
        when(mapper.toMeDTO(user)).thenReturn(meDTO);

        MeDTO result = service.getLoggerDTO();

        assertNotNull(result);
        verify(repository, times(1)).findByLogin("johndoe");
        verify(mapper, times(1)).toMeDTO(user);
    }

    @Test
    @DisplayName("Deve lançar um UsersNotFoundException ao tentar retornar um MeDTO com um usuário não autenticado")
    void testGetLoggerDTONoAuthentication() {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(null);

        UsersNotFoundException exception = assertThrows(UsersNotFoundException.class, () -> {
            service.getLoggerDTO();
        });

        assertEquals("Nenhum usuário autenticado encontrado.", exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), exception.getErrorCode());
        verify(repository, never()).findByLogin(any());
    }

    @Test
    @DisplayName("Deve lançar um UsersNotFoundException ao tentar retornar um MeDTO com um usuário não encontrado")
    void testGetLoggerDTOUserNotFound() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("johndoe");
        when(repository.findByLogin("johndoe")).thenReturn(null);

        UsersNotFoundException exception = assertThrows(UsersNotFoundException.class, () -> {
            service.getLoggerDTO();
        });

        assertEquals("Usuário não encontrado para o login: johndoe", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getErrorCode());
        verify(repository, times(1)).findByLogin("johndoe");
    }

    @Test
    @DisplayName("Deve lançar um UsersGeneralException com erro desconhecido ao tentar retornar um MeDTO")
    void testGetLoggerDTOUnknownError() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("johndoe");
        when(repository.findByLogin("johndoe")).thenThrow(new RuntimeException("Erro desconhecido"));

        UsersGeneralException exception = assertThrows(UsersGeneralException.class, () -> {
            service.getLoggerDTO();
        });

        assertEquals("Erro ao obter dados do usuário logado.", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getErrorCode());
        verify(repository, times(1)).findByLogin("johndoe");
    }

}

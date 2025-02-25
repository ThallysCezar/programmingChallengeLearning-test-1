package br.com.thallysprojects.pitang_desafio.services;

import br.com.thallysprojects.pitang_desafio.dtos.MeDTO;
import br.com.thallysprojects.pitang_desafio.dtos.UsersDTO;
import br.com.thallysprojects.pitang_desafio.entities.UserRole;
import br.com.thallysprojects.pitang_desafio.entities.Users;
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
import org.springframework.security.core.context.SecurityContextHolder;

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

        assertThrows(UsersNotFoundException.class, () -> {
            service.updateUserById(id, dto);
        });

        verify(validationsUsers, times(1)).validateUserExists(id);
        verify(repository, never()).saveAndFlush(any());
    }
//
//    @Test
//    @DisplayName("Deve lançar CarsBadRequestException quando houver erro ao atualizar um carro pelo seu id")
//    void testUpdateCarsByIdInvalidLicenseChange() {
//        Long id = 1L;
//        CarsDTO dto = new CarsDTO();
//        dto.setLicensePlate("ABC1234");
//
//        Cars existingCar = new Cars();
//        existingCar.setId(id);
//        existingCar.setLicensePlate("XYZ5678");
//
//        when(validationsCars.validateCarsExists(id)).thenReturn(existingCar);
//
//        doThrow(new CarsGeneralException("A licensa já está em uso.", HttpStatus.BAD_REQUEST.value())).when(validationsCars).validateLicenseChange(existingCar, dto.getLicensePlate());
//
//        assertThrows(CarsGeneralException.class, () -> {
//            service.updateCarsById(id, dto);
//        });
//
//        verify(validationsCars, times(1)).validateCarsExists(id);
//        verify(validationsCars, times(1)).validateLicenseChange(existingCar, dto.getLicensePlate());
//        verify(repository, never()).saveAndFlush(any());
//    }
//
//    @Test
//    @DisplayName("Deve lançar CarsGeneralException quando houver erro ao atualizar um carro pelo seu id")
//    void testUpdateCarsByIdUnknownError() {
//        Long id = 1L;
//        CarsDTO dto = new CarsDTO();
//
//        Cars existingCar = new Cars();
//        existingCar.setId(id);
//
//        when(validationsCars.validateCarsExists(id)).thenReturn(existingCar);
//
//        when(repository.saveAndFlush(existingCar)).thenThrow(new RuntimeException("Erro desconhecido"));
//
//        assertThrows(CarsGeneralException.class, () -> {
//            service.updateCarsById(id, dto);
//        });
//
//        verify(validationsCars, times(1)).validateCarsExists(id);
//        verify(repository, times(1)).saveAndFlush(existingCar);
//    }

    // testes para a parte que não foram bem sucecidas
    @Test
    @DisplayName("Deve salvar um usuário com sucesso")
    void testSaveSuccess() {
        // Arrange
        UsersDTO dto = new UsersDTO();
        dto.setEmail("john.doe@example.com");
        dto.setLogin("johndoe");
        dto.setPassword("password123");

        Users user = new Users();
        when(mapper.toEntity(dto)).thenReturn(user);
        when(repository.save(user)).thenReturn(user);
        when(mapper.toDTO(user)).thenReturn(dto);

        // Act
        service.save(dto);

        // Assert
        verify(repository, times(1)).save(user);
    }

//    @Test
//    @DisplayName("Deve lançar CarsBadRequestException ao tentar salvar um carro")
//    void testSaveLicensePlateAlreadyExists() {
//        CarsDTO dto = new CarsDTO();
//        dto.setLicensePlate("ABC1234");
//
//        when(repository.findByLicensePlate("ABC1234")).thenReturn(new Cars());
//
//        CarsBadRequestException exception = assertThrows(CarsBadRequestException.class, () -> {
//            service.save(dto);
//        });
//
//        assertEquals("License plate already exists", exception.getMessage());
//    }

    @Test
    @DisplayName("Deve deletar um usuário com sucesso")
    void testDeleteUsersSuccess() {
        // Arrange
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(true);

        // Act
        service.deleteUsers(id);

        // Assert
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar um UsersNotFoundException ao tentar deletar um usuário pelo seu id")
    void testDeleteUsersNotFound() {
        // Arrange
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(false);

        // Act & Assert
        UsersNotFoundException exception = assertThrows(UsersNotFoundException.class, () -> {
            service.deleteUsers(id);
        });

        assertEquals(String.format("Usuário não encontrado para o ID '%s'.", id), exception.getMessage());
        verify(repository, times(1)).existsById(id);
    }

//    Ta faltando a parte de teste para loadUserByUsername, erro e teste
    //    @Test
//    @DisplayName("Deve lançar um UsersNotFoundException ao tentar deletar um usuário pelo seu id")
//    void testDeleteUsersNotFound() {
//        // Arrange
//        Long id = 1L;
//        when(repository.existsById(id)).thenReturn(false);
//
//        // Act & Assert
//        UsersNotFoundException exception = assertThrows(UsersNotFoundException.class, () -> {
//            service.deleteUsers(id);
//        });
//
//        assertEquals(String.format("Usuário não encontrado para o ID '%s'.", id), exception.getMessage());
//        verify(repository, times(1)).existsById(id);
//    }

    @Test
    @DisplayName("Deve retornar um MeDTO com sucesso")
    void testGetLoggerDTOSuccess() {
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Users user = new Users();
        MeDTO meDTO = new MeDTO();

        when(authentication.getName()).thenReturn("johndoe");
        when(repository.findByLogin("johndoe")).thenReturn(user);
        when(mapper.toMeDTO(user)).thenReturn(meDTO);

        // Act
        MeDTO result = service.getLoggerDTO();

        // Assert
        assertNotNull(result);
        verify(repository, times(1)).findByLogin("johndoe");
    }

    //Faltando tratamento para essa parte de getLoggerDTO, pois não tem tratamento algum para isso.
//    @Test
//    @DisplayName("Deve lançar um UsersNotFoundException ao tentar deletar um usuário pelo seu id")
//    void testDeleteUsersNotFound() {
//        // Arrange
//        Long id = 1L;
//        when(repository.existsById(id)).thenReturn(false);
//
//        // Act & Assert
//        UsersNotFoundException exception = assertThrows(UsersNotFoundException.class, () -> {
//            service.deleteUsers(id);
//        });
//
//        assertEquals(String.format("Usuário não encontrado para o ID '%s'.", id), exception.getMessage());
//        verify(repository, times(1)).existsById(id);
//    }

}

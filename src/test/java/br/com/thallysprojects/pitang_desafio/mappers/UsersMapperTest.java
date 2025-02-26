package br.com.thallysprojects.pitang_desafio.mappers;

import br.com.thallysprojects.pitang_desafio.dtos.CarsDTO;
import br.com.thallysprojects.pitang_desafio.dtos.MeDTO;
import br.com.thallysprojects.pitang_desafio.dtos.UsersDTO;
import br.com.thallysprojects.pitang_desafio.entities.Cars;
import br.com.thallysprojects.pitang_desafio.entities.UserRole;
import br.com.thallysprojects.pitang_desafio.entities.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DisplayName("Mapper users")
class UsersMapperTest {

    private UsersMapper usersMapper;

    private CarsMapper carsMapper;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        this.modelMapper = Mockito.mock(ModelMapper.class);
        this.carsMapper = Mockito.mock(CarsMapper.class);
        this.usersMapper = new UsersMapper(modelMapper, carsMapper);
    }

    @Test
    @DisplayName("Deve retornar um DTO recebendo uma entidade")
    void testToDTO() {
        Users user = new Users();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setBirthday(LocalDate.now());
        user.setLogin("johndoe");
        user.setPassword("password123");
        user.setPhone("123456789");
        user.setRole(UserRole.ADMIN);

        Cars car = new Cars();
        car.setId(1L);
        car.setYears("2023");
        user.setCars(List.of(car));

        CarsDTO carDTO = new CarsDTO();
        carDTO.setId(car.getId());
        carDTO.setYears(car.getYears());
        when(carsMapper.toDTO(car)).thenReturn(carDTO);

        UsersDTO dto = usersMapper.toDTO(user);

        assertNotNull(dto);
        assertEquals(user.getFirstName(), dto.getFirstName());
        assertEquals(user.getLastName(), dto.getLastName());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getBirthday(), dto.getBirthday());
        assertEquals(user.getLogin(), dto.getLogin());
        assertEquals(user.getPassword(), dto.getPassword());
        assertEquals(user.getPhone(), dto.getPhone());
        assertEquals(user.getRole(), dto.getRole());
        assertNotNull(dto.getCars());
        assertEquals(1, dto.getCars().size());
        assertEquals(car.getId(), dto.getCars().get(0).getId());
    }

    @Test
    @DisplayName("Deve retornar uma entidade recebendo um DTO")
    void testToEntity() {
        UsersDTO dto = new UsersDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setBirthday(LocalDate.now());
        dto.setLogin("johndoe");
        dto.setPassword("password123");
        dto.setPhone("123456789");
        dto.setRole(UserRole.ADMIN);

        CarsDTO carDTO = new CarsDTO();
        carDTO.setId(1L);
        carDTO.setYears("2023");
        dto.setCars(List.of(carDTO));

        Users userMock = new Users();
        userMock.setFirstName(dto.getFirstName());
        userMock.setLastName(dto.getLastName());
        userMock.setEmail(dto.getEmail());
        userMock.setBirthday(dto.getBirthday());
        userMock.setLogin(dto.getLogin());
        userMock.setPassword(dto.getPassword());
        userMock.setPhone(dto.getPhone());
        userMock.setRole(dto.getRole());

        when(modelMapper.map(dto, Users.class)).thenReturn(userMock);

        Cars carMock = new Cars();
        carMock.setId(carDTO.getId());
        carMock.setYears(carDTO.getYears());
        when(carsMapper.toEntity(carDTO)).thenReturn(carMock);

        Users user = usersMapper.toEntity(dto);

        assertNotNull(user);
        assertEquals(dto.getFirstName(), user.getFirstName());
        assertEquals(dto.getLastName(), user.getLastName());
        assertEquals(dto.getEmail(), user.getEmail());
        assertEquals(dto.getBirthday(), user.getBirthday());
        assertEquals(dto.getLogin(), user.getLogin());
        assertEquals(dto.getPassword(), user.getPassword());
        assertEquals(dto.getPhone(), user.getPhone());
        assertEquals(dto.getRole(), user.getRole());
        assertNotNull(user.getCars());
        assertEquals(1, user.getCars().size());
        assertEquals(carDTO.getId(), user.getCars().get(0).getId());
    }

    @Test
    void testToMeDTO() {
        Users user = new Users();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setBirthday(LocalDate.now());
        user.setLogin("johndoe");
        user.setPhone("123456789");
        user.setCreatedAt(LocalDate.now());
        user.setLastLogin(LocalDate.now());

        Cars car = new Cars();
        car.setId(1L);
        car.setYears("2023");
        car.setLicensePlate("ABC1234");
        car.setModel("Model X");
        car.setColor("Red");
        user.setCars(List.of(car));

        MeDTO meDTO = usersMapper.toMeDTO(user);

        assertNotNull(meDTO);
        assertEquals(user.getFirstName(), meDTO.getFirstName());
        assertEquals(user.getLastName(), meDTO.getLastName());
        assertEquals(user.getEmail(), meDTO.getEmail());
        assertEquals(user.getBirthday(), meDTO.getBirthday());
        assertEquals(user.getLogin(), meDTO.getLogin());
        assertEquals(user.getPhone(), meDTO.getPhone());
        assertEquals(user.getCreatedAt(), meDTO.getCreatedAt());
        assertEquals(user.getLastLogin(), meDTO.getLastLogin());
        assertNotNull(meDTO.getCars());
        assertEquals(1, meDTO.getCars().size());
        assertEquals(car.getId(), meDTO.getCars().get(0).getId());
    }

    @Test
    @DisplayName("Deve retornar uma lista de DTOs recebendo uma lista de entidades")
    void testToListDTO() {
        Users user1 = new Users();
        user1.setId(1L);
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setBirthday(LocalDate.now());
        user1.setLogin("johndoe");
        user1.setPassword("password123");
        user1.setPhone("123456789");
        user1.setRole(UserRole.ADMIN);

        Users user2 = new Users();
        user2.setId(2L);
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setEmail("jane.smith@example.com");
        user2.setBirthday(LocalDate.now());
        user2.setLogin("janesmith");
        user2.setPassword("password456");
        user2.setPhone("987654321");
        user2.setRole(UserRole.ADMIN);

        List<Users> usersList = List.of(user1, user2);

        UsersDTO dto1 = new UsersDTO();
        dto1.setFirstName(user1.getFirstName());
        dto1.setLastName(user1.getLastName());
        dto1.setEmail(user1.getEmail());
        dto1.setBirthday(user1.getBirthday());
        dto1.setLogin(user1.getLogin());
        dto1.setPassword(user1.getPassword());
        dto1.setPhone(user1.getPhone());
        dto1.setRole(user1.getRole());

        UsersDTO dto2 = new UsersDTO();
        dto2.setFirstName(user2.getFirstName());
        dto2.setLastName(user2.getLastName());
        dto2.setEmail(user2.getEmail());
        dto2.setBirthday(user2.getBirthday());
        dto2.setLogin(user2.getLogin());
        dto2.setPassword(user2.getPassword());
        dto2.setPhone(user2.getPhone());
        dto2.setRole(user2.getRole());

        when(modelMapper.map(user1, UsersDTO.class)).thenReturn(dto1);
        when(modelMapper.map(user2, UsersDTO.class)).thenReturn(dto2);

        List<UsersDTO> dtosList = usersMapper.toListDTO(usersList);

        assertNotNull(dtosList, "A lista de DTOs não deve ser nula");
        assertEquals(2, dtosList.size(), "O tamanho da lista de DTOs deve ser 2");

        assertNotNull(dtosList.get(0), "O primeiro DTO na lista é nulo");
        assertEquals(user1.getFirstName(), dtosList.get(0).getFirstName(), "Nome do primeiro DTO não corresponde");
        assertEquals(user1.getLastName(), dtosList.get(0).getLastName(), "Sobrenome do primeiro DTO não corresponde");
        assertEquals(user1.getEmail(), dtosList.get(0).getEmail(), "Email do primeiro DTO não corresponde");
        assertEquals(user1.getBirthday(), dtosList.get(0).getBirthday(), "Data de nascimento do primeiro DTO não corresponde");
        assertEquals(user1.getLogin(), dtosList.get(0).getLogin(), "Login do primeiro DTO não corresponde");
        assertEquals(user1.getPassword(), dtosList.get(0).getPassword(), "Senha do primeiro DTO não corresponde");
        assertEquals(user1.getPhone(), dtosList.get(0).getPhone(), "Telefone do primeiro DTO não corresponde");
        assertEquals(user1.getRole(), dtosList.get(0).getRole(), "Role do primeiro DTO não corresponde");

        assertNotNull(dtosList.get(1), "O segundo DTO na lista é nulo");
        assertEquals(user2.getFirstName(), dtosList.get(1).getFirstName(), "Nome do segundo DTO não corresponde");
        assertEquals(user2.getLastName(), dtosList.get(1).getLastName(), "Sobrenome do segundo DTO não corresponde");
        assertEquals(user2.getEmail(), dtosList.get(1).getEmail(), "Email do segundo DTO não corresponde");
        assertEquals(user2.getBirthday(), dtosList.get(1).getBirthday(), "Data de nascimento do segundo DTO não corresponde");
        assertEquals(user2.getLogin(), dtosList.get(1).getLogin(), "Login do segundo DTO não corresponde");
        assertEquals(user2.getPassword(), dtosList.get(1).getPassword(), "Senha do segundo DTO não corresponde");
        assertEquals(user2.getPhone(), dtosList.get(1).getPhone(), "Telefone do segundo DTO não corresponde");
        assertEquals(user2.getRole(), dtosList.get(1).getRole(), "Role do segundo DTO não corresponde");
    }

    @Test
    @DisplayName("Deve retornar uma lista de entidades recebendo uma lista de DTOs")
    void testToList() {
        UsersDTO dto1 = new UsersDTO();
        dto1.setFirstName("John");
        dto1.setLastName("Doe");
        dto1.setEmail("john.doe@example.com");
        dto1.setBirthday(LocalDate.of(1990, 2, 12));
        dto1.setLogin("johndoe");
        dto1.setPassword("password123");
        dto1.setPhone("123456789");
        dto1.setRole(UserRole.ADMIN);

        UsersDTO dto2 = new UsersDTO();
        dto2.setFirstName("Jane");
        dto2.setLastName("Doe");
        dto2.setEmail("jane.doe@example.com");
        dto2.setBirthday(LocalDate.of(1995, 2, 12));
        dto2.setLogin("janedoe");
        dto2.setPassword("password456");
        dto2.setPhone("987654321");
        dto2.setRole(UserRole.ADMIN);

        List<UsersDTO> dtosList = Arrays.asList(dto1, dto2);

        Users user1 = new Users();
        user1.setFirstName(dto1.getFirstName());
        user1.setLastName(dto1.getLastName());
        user1.setEmail(dto1.getEmail());
        user1.setBirthday(dto1.getBirthday());
        user1.setLogin(dto1.getLogin());
        user1.setPassword(dto1.getPassword());
        user1.setPhone(dto1.getPhone());
        user1.setRole(dto1.getRole());

        Users user2 = new Users();
        user2.setFirstName(dto2.getFirstName());
        user2.setLastName(dto2.getLastName());
        user2.setEmail(dto2.getEmail());
        user2.setBirthday(dto2.getBirthday());
        user2.setLogin(dto2.getLogin());
        user2.setPassword(dto2.getPassword());
        user2.setPhone(dto2.getPhone());
        user2.setRole(dto2.getRole());

        when(modelMapper.map(dto1, Users.class)).thenReturn(user1);
        when(modelMapper.map(dto2, Users.class)).thenReturn(user2);

        CarsDTO carDTO1 = new CarsDTO();
        carDTO1.setId(1L);
        carDTO1.setYears("2023");

        CarsDTO carDTO2 = new CarsDTO();
        carDTO2.setId(2L);
        carDTO2.setYears("2022");

        Cars car1 = new Cars();
        car1.setId(carDTO1.getId());
        car1.setYears(carDTO1.getYears());

        Cars car2 = new Cars();
        car2.setId(carDTO2.getId());
        car2.setYears(carDTO2.getYears());

        when(carsMapper.toEntity(carDTO1)).thenReturn(car1);
        when(carsMapper.toEntity(carDTO2)).thenReturn(car2);

        List<Users> usersList = usersMapper.toList(dtosList);

        assertNotNull(usersList);
        assertEquals(2, usersList.size());

        assertEquals(dto1.getFirstName(), usersList.get(0).getFirstName());
        assertEquals(dto1.getLastName(), usersList.get(0).getLastName());
        assertEquals(dto1.getEmail(), usersList.get(0).getEmail());
        assertEquals(dto1.getBirthday(), usersList.get(0).getBirthday());
        assertEquals(dto1.getLogin(), usersList.get(0).getLogin());
        assertEquals(dto1.getPassword(), usersList.get(0).getPassword());
        assertEquals(dto1.getPhone(), usersList.get(0).getPhone());
        assertEquals(dto1.getRole(), usersList.get(0).getRole());

        assertEquals(dto2.getFirstName(), usersList.get(1).getFirstName());
        assertEquals(dto2.getLastName(), usersList.get(1).getLastName());
        assertEquals(dto2.getEmail(), usersList.get(1).getEmail());
        assertEquals(dto2.getBirthday(), usersList.get(1).getBirthday());
        assertEquals(dto2.getLogin(), usersList.get(1).getLogin());
        assertEquals(dto2.getPassword(), usersList.get(1).getPassword());
        assertEquals(dto2.getPhone(), usersList.get(1).getPhone());
        assertEquals(dto2.getRole(), usersList.get(1).getRole());
    }

}
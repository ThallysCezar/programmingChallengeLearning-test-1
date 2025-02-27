package br.com.thallysprojects.pitang_desafio.mappers;

import br.com.thallysprojects.pitang_desafio.dtos.CarsDTO;
import br.com.thallysprojects.pitang_desafio.dtos.MeDTO;
import br.com.thallysprojects.pitang_desafio.dtos.UsersDTO;
import br.com.thallysprojects.pitang_desafio.entities.Cars;
import br.com.thallysprojects.pitang_desafio.entities.UserRole;
import br.com.thallysprojects.pitang_desafio.entities.Users;
import br.com.thallysprojects.pitang_desafio.factorios.CarsFactory;
import br.com.thallysprojects.pitang_desafio.factorios.UsersFactory;
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
        Users user = UsersFactory.createDefaultUserWithListCar();
        UsersDTO userMock = UsersFactory.createDefaultUserDTOwithListCar();
        when(carsMapper.toDTO(user.getCars().get(0))).thenReturn(userMock.getCars().get(0));

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
        assertEquals(dto.getCars().get(0).getId(), user.getCars().get(0).getId());
    }

    @Test
    @DisplayName("Deve retornar uma entidade recebendo um DTO")
    void testToEntity() {
        UsersDTO dto = UsersFactory.createDefaultUserDTOwithListCar();

        Users userMock = UsersFactory.createDefaultUserWithListCar();
        when(modelMapper.map(dto, Users.class)).thenReturn(userMock);

        when(carsMapper.toEntity(dto.getCars().get(0))).thenReturn(userMock.getCars().get(0));

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
        assertEquals(dto.getCars().get(0).getId(), user.getCars().get(0).getId());
    }

    @Test
    void testToMeDTO() {
        Users user = UsersFactory.createDefaultUserWithListCar();

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
        assertEquals(user.getCars().get(0).getId(), meDTO.getCars().get(0).getId());
    }

    @Test
    @DisplayName("Deve retornar uma lista de DTOs recebendo uma lista de entidades")
    void testToListDTO() {
        Users user1 = UsersFactory.createDefaultUser();
        Users user2 = UsersFactory.createDefaultUserTwo();

        UsersDTO dto1 = UsersFactory.createDefaultUserDTO();
        UsersDTO dto2 = UsersFactory.createDefaultUserDTOTwo();

        when(modelMapper.map(user1, UsersDTO.class)).thenReturn(dto1);
        when(modelMapper.map(user2, UsersDTO.class)).thenReturn(dto2);

        List<UsersDTO> dtosList = UsersFactory.createUsersDTOList();

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
        UsersDTO dto1 = UsersFactory.createDefaultUserDTO();
        UsersDTO dto2 = UsersFactory.createDefaultUserDTOTwo();

        Users user1 = UsersFactory.createDefaultUser();
        Users user2 = UsersFactory.createDefaultUserTwo();

        when(modelMapper.map(dto1, Users.class)).thenReturn(user1);
        when(modelMapper.map(dto2, Users.class)).thenReturn(user2);

        CarsDTO carDTO1 = CarsFactory.createDefaultCarDTO();
        CarsDTO carDTO2 = CarsFactory.createDefaultCarDTOTwo();

        Cars car1 = CarsFactory.createDefaultCar();
        Cars car2 = CarsFactory.createDefaultCarTwo();

        when(carsMapper.toEntity(carDTO1)).thenReturn(car1);
        when(carsMapper.toEntity(carDTO2)).thenReturn(car2);

        List<Users> usersList = UsersFactory.createUsersList();

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
package br.com.thallysprojects.pitang_desafio.factorios;

import br.com.thallysprojects.pitang_desafio.dtos.CarsDTO;
import br.com.thallysprojects.pitang_desafio.dtos.UsersDTO;
import br.com.thallysprojects.pitang_desafio.entities.Cars;
import br.com.thallysprojects.pitang_desafio.entities.UserRole;
import br.com.thallysprojects.pitang_desafio.entities.Users;

import java.time.LocalDate;
import java.util.List;

public class UsersFactory {

    public static Users createUser(Long id, String firstName, String lastName, String email, LocalDate birthday,
                                   String login, String password, String phone, UserRole role) {
        Users user = new Users();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setBirthday(birthday);
        user.setLogin(login);
        user.setPassword(password);
        user.setPhone(phone);
        user.setRole(role);
        return user;
    }

    public static Users createUserWithListCar(Long id, String firstName, String lastName, String email, LocalDate birthday,
                                   String login, String password, String phone, UserRole role, List<Cars> cars) {
        Users user = new Users();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setBirthday(birthday);
        user.setLogin(login);
        user.setPassword(password);
        user.setPhone(phone);
        user.setRole(role);

        Cars car = new Cars();
        car.setId(1L);
        car.setYears("2023");
        car.setLicensePlate("ABC1234");
        car.setModel("Model X");
        car.setColor("Red");
        user.setCars(List.of(car));

        return user;
    }

    public static UsersDTO createUserDTO(Long id, String firstName, String lastName, String email, LocalDate birthday,
                                         String login, String password, String phone, UserRole role) {
        UsersDTO dto = new UsersDTO();
//        dto.setId(id);
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setEmail(email);
        dto.setBirthday(birthday);
        dto.setLogin(login);
        dto.setPassword(password);
        dto.setPhone(phone);
        dto.setRole(role);
        return dto;
    }

    public static UsersDTO createUserDTOWithList(Long id, String firstName, String lastName, String email, LocalDate birthday,
                                                 String login, String password, String phone, UserRole role, List<CarsDTO> carsDTO) {
        UsersDTO dto = new UsersDTO();
//        dto.setId(id);
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setEmail(email);
        dto.setBirthday(birthday);
        dto.setLogin(login);
        dto.setPassword(password);
        dto.setPhone(phone);
        dto.setRole(role);

        CarsDTO carDTO = new CarsDTO();
        carDTO.setId(1L);
        carDTO.setYears("2023");
        carDTO.setLicensePlate("ABC1234");
        carDTO.setModel("Model X");
        carDTO.setColor("Red");
        dto.setCars(List.of(carDTO));

        return dto;
    }

    // Métodos para criar objetos com valores padrão
    public static Users createDefaultUser() {
        return createUser(1L, "John", "Doe", "john.doe@example.com", LocalDate.now(),
                "johndoe", "password123", "123456789", UserRole.ADMIN);
    }

    public static Users createDefaultUserTwo() {
        return createUser(2L, "Jane", "Smith", "jane.smith@example.com", LocalDate.now(), "janesmith", "password456", "987654321", UserRole.ADMIN);
    }


    public static Users createDefaultUserWithListCar() {
        return createUserWithListCar(1L, "Jane", "Smith", "jane.smith@example.com", LocalDate.now(), "janesmith", "password456", "987654321", UserRole.ADMIN, CarsFactory.createCarsList());
    }

    public static UsersDTO createDefaultUserDTO() {
        return createUserDTO(1L, "John", "Doe", "john.doe@example.com", LocalDate.now(),
                "johndoe", "password123", "123456789", UserRole.ADMIN);
    }

    public static UsersDTO createDefaultUserDTOTwo() {
        return createUserDTO(2L, "Jane", "Smith", "jane.smith@example.com", LocalDate.now(), "janesmith", "password456", "987654321", UserRole.ADMIN);
    }

    public static UsersDTO createDefaultUserDTOwithListCar() {
        return createUserDTOWithList(2L, "Jane", "Smith", "jane.smith@example.com", LocalDate.now(), "janesmith", "password456", "987654321", UserRole.ADMIN, CarsFactory.createCarsDTOList());
    }

    public static List<Users> createUsersList() {
        return List.of(
                createDefaultUser(),
                createDefaultUserTwo()
        );
    }

    public static List<UsersDTO> createUsersDTOList() {
        return List.of(
                createDefaultUserDTO(),
                createDefaultUserDTOTwo()
        );
    }

}
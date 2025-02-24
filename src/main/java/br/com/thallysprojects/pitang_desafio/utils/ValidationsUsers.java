package br.com.thallysprojects.pitang_desafio.utils;

import br.com.thallysprojects.pitang_desafio.dtos.CarsDTO;
import br.com.thallysprojects.pitang_desafio.dtos.UsersDTO;
import br.com.thallysprojects.pitang_desafio.entities.Cars;
import br.com.thallysprojects.pitang_desafio.entities.Users;
import br.com.thallysprojects.pitang_desafio.exceptions.users.UsersBadRequestException;
import br.com.thallysprojects.pitang_desafio.exceptions.users.UsersGeneralException;
import br.com.thallysprojects.pitang_desafio.exceptions.users.UsersNotFoundException;
import br.com.thallysprojects.pitang_desafio.mappers.CarsMapper;
import br.com.thallysprojects.pitang_desafio.repositories.CarsRepository;
import br.com.thallysprojects.pitang_desafio.repositories.UsersRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ValidationsUsers {

    private final UsersRepository repository;
    private final CarsRepository carsRepository;
    private final CarsMapper carsMapper;

    public Users validateUserExists(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UsersNotFoundException(
                        String.format("Usuário não encontrado com o id '%s'.", id),
                        HttpStatus.NOT_FOUND.value()
                ));
    }

    public void validateEmailChange(Users existingUser, String newEmail) {
        if (!existingUser.getEmail().equals(newEmail) && repository.existsByEmail(newEmail)) {
            throw new UsersGeneralException("O e-mail informado já está em uso.", HttpStatus.BAD_REQUEST.value());
        }

        existingUser.setEmail(newEmail);

    }

    public void validateLoginChange(Users existingUser, String newLogin) {
        if (!existingUser.getLogin().equals(newLogin) && repository.existsByLogin(newLogin)) {
            throw new UsersGeneralException("O login informado já está em uso.", HttpStatus.BAD_REQUEST.value());
        }

        existingUser.setLogin(newLogin);
    }

    public boolean isValidPassword(String password) {
        return password != null && !password.isBlank();
    }

    public List<Cars> validateAndUpdateUserCars(Users existingUser, List<CarsDTO> carsDTOList) {
        if (carsDTOList == null) return existingUser.getCars();

        Map<Long, Cars> existingCarsMap = existingUser.getCars().stream()
                .collect(Collectors.toMap(Cars::getId, car -> car));

        List<Cars> updatedCars = new ArrayList<>();
        Set<Long> updatedCarIds = new HashSet<>();

        for (CarsDTO carDTO : carsDTOList) {
            Cars car;
            if (carDTO.getId() != null && existingCarsMap.containsKey(carDTO.getId())) {
                car = existingCarsMap.get(carDTO.getId());
            } else {
                car = carsRepository.findById(carDTO.getId()).orElse(null);
                if (car == null) {
                    car = carsMapper.toEntity(carDTO);
                }
                car.setUsers(existingUser);
            }

            car.setYears(carDTO.getYears());
            car.setLicensePlate(carDTO.getLicensePlate());
            car.setModel(carDTO.getModel());
            car.setColor(carDTO.getColor());

            updatedCars.add(car);
            if (car.getId() != null) {
                updatedCarIds.add(car.getId());
            }
        }

        existingUser.getCars().removeIf(car -> car.getId() != null && !updatedCarIds.contains(car.getId()));

        existingUser.getCars().clear();
        existingUser.getCars().addAll(updatedCars);

        return updatedCars;
    }

    public void validateEmailFormat(String email) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new UsersBadRequestException("Invalid fields, email", HttpStatus.BAD_REQUEST.value());
        }
    }

    public void validateLoginFormat(String login) {
        if (!login.matches("^[a-zA-Z0-9_]{5,}$")) {
            throw new UsersBadRequestException("Invalid fields, login", HttpStatus.BAD_REQUEST.value());
        }
    }

    public void validatePasswordFormat(String password) {
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$")) {
            throw new UsersBadRequestException("Invalid fields, password", HttpStatus.BAD_REQUEST.value());
        }
    }

    public void validateMissingFields(UsersDTO dto) {
        if (StringUtils.isBlank(dto.getEmail()) ||
                StringUtils.isBlank(dto.getLogin()) ||
                StringUtils.isBlank(dto.getPassword()) ||
                StringUtils.isBlank(dto.getFirstName()) ||
                StringUtils.isBlank(dto.getLastName())) {

            throw new UsersBadRequestException("Missing fields", HttpStatus.BAD_REQUEST.value());
        }
    }

    public void validateEmailExists(String email, UsersRepository repository) {
        if (repository.findByEmail(email) != null) {
            throw new UsersBadRequestException("Email already exists", HttpStatus.BAD_REQUEST.value());
        }
    }

    public void validateLoginExists(String login, UsersRepository repository) {
        if (repository.findByLogin(login) != null) {
            throw new UsersBadRequestException("Login already exists", HttpStatus.BAD_REQUEST.value());
        }
    }

}
package br.com.thallysprojects.pitang_desafio.mappers;

import br.com.thallysprojects.pitang_desafio.dtos.CarsDTO;
import br.com.thallysprojects.pitang_desafio.dtos.UsersDTO;
import br.com.thallysprojects.pitang_desafio.entities.Cars;
import br.com.thallysprojects.pitang_desafio.entities.Users;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsersMapper {

    private final ModelMapper modelMapper;

    private final CarsMapper carsMapper;

    public UsersMapper(ModelMapper modelMapper, CarsMapper carsMapper) {
        this.modelMapper = modelMapper;
        this.carsMapper = carsMapper;
    }

//    public UsersDTO toDTO(Users entity) {
//        UsersDTO dto = modelMapper.map(entity, UsersDTO.class);
//        List<Cars> carsList = entity.getCars() != null ? entity.getCars() : new ArrayList<>();
//
//        dto.setCars(carsMapper.toListDTO(carsList));
//        return dto;
//    }

    public UsersDTO toDTO(Users model) {
        return new UsersDTO(
                model.getFirstName(),
                model.getLastName(),
                model.getEmail(),
                model.getBirthday(),
                model.getLogin(),
                model.getPassword(),
                model.getPhone(),
                model.getCars() != null ? model.getCars().stream().map(carsMapper::toDTO).toList() : null
        );
    }

    public Users toEntity(UsersDTO dto) {
        Users user = modelMapper.map(dto, Users.class);

        if (dto.getCars() != null) {
            List<Cars> cars = dto.getCars().stream()
                    .map(carDTO -> {
                        Cars car = carsMapper.toEntity(carDTO);
                        car.setUsers(user); // 🔴 IMPORTANTE: garante a relação
                        return car;
                    })
                    .toList();
            user.setCars(cars);
        }

        return user;
    }

    public List<UsersDTO> toListDTO(List<Users> modelList) {
        return modelList.stream()
                .map(this::toDTO).toList();
    }

    public List<Users> toList(List<UsersDTO> dtosList) {
        return dtosList.stream()
                .map(this::toEntity).collect(Collectors.toList());
    }

}
package br.com.thallysprojects.pitang_desafio.mappers;

import br.com.thallysprojects.pitang_desafio.dtos.CarsDTO;
import br.com.thallysprojects.pitang_desafio.dtos.MeDTO;
import br.com.thallysprojects.pitang_desafio.dtos.UsersDTO;
import br.com.thallysprojects.pitang_desafio.entities.Cars;
import br.com.thallysprojects.pitang_desafio.entities.Users;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

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

    public UsersDTO toDTO(Users model) {
        return new UsersDTO(
                model.getFirstName(),
                model.getLastName(),
                model.getEmail(),
                model.getBirthday(),
                model.getLogin(),
                model.getPassword(),
                model.getPhone(),
                model.getRole(),
                model.getCars() != null ? model.getCars().stream().map(carsMapper::toDTO).toList() : null
        );
    }

    public Users toEntity(UsersDTO dto) {
        Users user = modelMapper.map(dto, Users.class);

        if (dto.getCars() != null) {
            List<Cars> cars = dto.getCars().stream()
                    .map(carDTO -> {
                        Cars car = carsMapper.toEntity(carDTO);
                        car.setUsers(user);
                        return car;
                    })
                    .toList();
            user.setCars(cars);
        }

        return user;
    }

    public MeDTO toMeDTO(Users users) {
        MeDTO meDTO = new MeDTO();
        meDTO.setFirstName(users.getFirstName());
        meDTO.setLastName(users.getLastName());
        meDTO.setEmail(users.getEmail());
        meDTO.setBirthday(users.getBirthday());
        meDTO.setLogin(users.getLogin());
        meDTO.setPhone(users.getPhone());
        meDTO.setCreatedAt(users.getCreatedAt());
        meDTO.setLastLogin(users.getLastLogin());

        List<CarsDTO> carsDTO = users.getCars().stream()
                .map(car -> new CarsDTO(car.getId(), car.getYears(), car.getLicensePlate(), car.getModel(), car.getColor()))
                .collect(Collectors.toList());
        meDTO.setCars(carsDTO);

        return meDTO;
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
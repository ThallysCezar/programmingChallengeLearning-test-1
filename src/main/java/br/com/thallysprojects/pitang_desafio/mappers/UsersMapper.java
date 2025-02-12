package br.com.thallysprojects.pitang_desafio.mappers;

import br.com.thallysprojects.pitang_desafio.dtos.UsersDTO;
import br.com.thallysprojects.pitang_desafio.entities.Users;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsersMapper {

    private final ModelMapper modelMapper;

    public UsersMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UsersDTO toDTO(Users entity) {
        return modelMapper.map(entity, UsersDTO.class);
    }

    public Users toEntity(UsersDTO dto) {
        return modelMapper.map(dto, Users.class);
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
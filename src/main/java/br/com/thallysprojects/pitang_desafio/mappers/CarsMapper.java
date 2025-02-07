package br.com.thallysprojects.pitang_desafio.mappers;

import br.com.thallysprojects.pitang_desafio.dtos.CarsDTO;
import br.com.thallysprojects.pitang_desafio.entities.Cars;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CarsMapper {

    private final ModelMapper modelMapper;

    public CarsMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CarsDTO toDTO(Cars entity) {
        return modelMapper.map(entity, CarsDTO.class);
    }

    public Cars toEntity(CarsDTO dto) {
        return modelMapper.map(dto, Cars.class);
    }

    public List<CarsDTO> toListDTO(List<Cars> modelList) {
        return modelList.stream()
                .map(this::toDTO).toList();
    }

    public List<Cars> toList(List<CarsDTO> dtosList) {
        return dtosList.stream()
                .map(this::toEntity).collect(Collectors.toList());
    }
    
}

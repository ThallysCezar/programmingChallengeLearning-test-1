package br.com.thallysprojects.pitang_desafio.configs.mapper;

import br.com.thallysprojects.pitang_desafio.dtos.UsersDTO;
import br.com.thallysprojects.pitang_desafio.entities.Users;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.OffsetDateTime;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Mapeamento explícito para a lista de carros
        modelMapper.typeMap(UsersDTO.class, Users.class).addMappings(mapper -> {
            mapper.skip(Users::setCars); // Evita sobrescrever a lista de carros
        });

        return modelMapper;
    }

}

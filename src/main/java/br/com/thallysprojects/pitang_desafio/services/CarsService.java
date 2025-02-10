package br.com.thallysprojects.pitang_desafio.services;

import br.com.thallysprojects.pitang_desafio.dtos.CarsDTO;
import br.com.thallysprojects.pitang_desafio.entities.Cars;
import br.com.thallysprojects.pitang_desafio.exceptions.CarsGeneralException;
import br.com.thallysprojects.pitang_desafio.exceptions.CarsNotFoundException;
import br.com.thallysprojects.pitang_desafio.mappers.CarsMapper;
import br.com.thallysprojects.pitang_desafio.repositories.CarsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarsService {

    private final CarsRepository repository;

    private final CarsMapper mapper;

    public List<CarsDTO> findAll() {
        List<Cars> cars = repository.findAll();
        if (cars.isEmpty()) {
            throw new CarsNotFoundException("Nenhum carro encontrado");
        }
        return mapper.toListDTO(cars);
    }


    public CarsDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(CarsNotFoundException::new);
    }

    public void updateCarsById(Long id) {
        try {
            Cars existingUser = repository.findById(id).orElseThrow(() -> new CarsNotFoundException(String.format("Carro não encontrado com o id '%s'.", id)));
            mapper.toDTO(repository.saveAndFlush(existingUser));

        } catch (Exception e) {
            log.error("Erro desconhecido ao atualizar o carro: {}", e.getMessage(), e);
            throw new CarsGeneralException();
        }
    }

    public CarsDTO save(CarsDTO dto) {
        try {
            return mapper.toDTO(repository.save(mapper.toEntity(dto)));
        } catch (Exception e) {
            log.error("Erro desconhecido ao salvar o carro: {}", e.getMessage(), e);
            throw new CarsGeneralException();
        }
    }

    public void deleteCars(Long id) throws Exception {
        if (!repository.existsById(id)) {
            throw new CarsNotFoundException(String.format("Carro não encontrado com o id '%s'.", id));
        }
        repository.deleteById(id);
    }

}

package br.com.thallysprojects.pitang_desafio.services;

import br.com.thallysprojects.pitang_desafio.dtos.CarsDTO;
import br.com.thallysprojects.pitang_desafio.entities.Cars;
import br.com.thallysprojects.pitang_desafio.mappers.CarsMapper;
import br.com.thallysprojects.pitang_desafio.repositories.CarsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CarsService {

    private final CarsRepository repository;

    private final CarsMapper mapper;

    public List<CarsDTO> findAll() throws Exception {
        List<Cars> cars = repository.findAll();
        if (cars.isEmpty()) {
            throw new Exception("Nenhum carro encontrado");
        }
        return mapper.toListDTO(cars);
    }


    public CarsDTO findById(Long id) throws Exception {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(Exception::new);
    }

    public CarsDTO updateCarsById(Long id) {
        try {
            Cars existingUser = repository.findById(id).orElseThrow(() -> new Exception("Carro não com encontrando com o ID: " + id));
            return mapper.toDTO(repository.saveAndFlush(existingUser));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public CarsDTO save(CarsDTO dto) throws Exception {
        try {
            return mapper.toDTO(repository.save(mapper.toEntity(dto)));
        } catch (Exception ex) {
            throw new Exception();
        }
    }

    public void deleteCars(Long id) throws Exception {
        if (!repository.existsById(id)) {
            throw new Exception(String.format("Carro não encontrado com o id '%s'.", id));
        }
        repository.deleteById(id);
    }

}

package br.com.thallysprojects.pitang_desafio.services;

import br.com.thallysprojects.pitang_desafio.dtos.CarsDTO;
import br.com.thallysprojects.pitang_desafio.entities.Cars;
import br.com.thallysprojects.pitang_desafio.exceptions.cars.CarsBadRequestException;
import br.com.thallysprojects.pitang_desafio.exceptions.cars.CarsGeneralException;
import br.com.thallysprojects.pitang_desafio.exceptions.cars.CarsNotFoundException;
import br.com.thallysprojects.pitang_desafio.exceptions.users.UsersNotFoundException;
import br.com.thallysprojects.pitang_desafio.mappers.CarsMapper;
import br.com.thallysprojects.pitang_desafio.repositories.CarsRepository;
import br.com.thallysprojects.pitang_desafio.utils.ValidationsCars;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarsService {

    private final CarsRepository repository;

    private final CarsMapper mapper;

    private final ValidationsCars validationsCars;

    public List<CarsDTO> findAll() {
        List<Cars> cars = repository.findAll();
        if (cars.isEmpty()) {
            throw new CarsNotFoundException("Nenhum carro encontrado", HttpStatus.NOT_FOUND.value());
        }
        return mapper.toListDTO(cars);
    }

    public CarsDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(CarsNotFoundException::new);
    }

    public void updateCarsById(Long id, CarsDTO dto) {
        try {
            Cars existingCars = validationsCars.validateCarsExists(id);
            existingCars.setYears(dto.getYears());

            validationsCars.validateLicenseChange(existingCars, dto.getLicensePlate());
            existingCars.setModel(dto.getModel());
            existingCars.setColor(dto.getColor());

            repository.saveAndFlush(existingCars);
        } catch (CarsNotFoundException | CarsBadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro desconhecido ao atualizar o carro: {}", e.getMessage(), e);
            throw new CarsGeneralException("Erro desconhecido ao atualizar o carro", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public void save(CarsDTO dto) {
        try {
            if (repository.findByLicensePlate(dto.getLicensePlate()) != null) {
                throw new CarsBadRequestException("License plate already exists", HttpStatus.BAD_REQUEST.value());
            }

            if (StringUtils.isBlank(dto.getYears()) ||
                    StringUtils.isBlank(dto.getLicensePlate()) ||
                    StringUtils.isBlank(dto.getModel()) ||
                    StringUtils.isBlank(dto.getColor())) {
                throw new CarsBadRequestException("Missing fields", HttpStatus.BAD_REQUEST.value());
            }

            mapper.toDTO(repository.save(mapper.toEntity(dto)));
        } catch (CarsGeneralException e) {
            log.error("Erro desconhecido ao salvar o carro: {}", e.getMessage(), e);
            throw new CarsGeneralException("Erro desconhecido ao salvar o carro", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public void deleteCars(Long id) {
        if (!repository.existsById(id)) {
            throw new CarsNotFoundException(String.format("Carro não encontrado com o id '%s'.", id), HttpStatus.NOT_FOUND.value());
        }
        repository.deleteById(id);
    }

}
package br.com.thallysprojects.pitang_desafio.services;

import br.com.thallysprojects.pitang_desafio.dtos.CarsDTO;
import br.com.thallysprojects.pitang_desafio.entities.Cars;
import br.com.thallysprojects.pitang_desafio.exceptions.cars.CarsBadRequestException;
import br.com.thallysprojects.pitang_desafio.exceptions.cars.CarsGeneralException;
import br.com.thallysprojects.pitang_desafio.exceptions.cars.CarsNotFoundException;
import br.com.thallysprojects.pitang_desafio.mappers.CarsMapper;
import br.com.thallysprojects.pitang_desafio.repositories.CarsRepository;
import br.com.thallysprojects.pitang_desafio.utils.ValidationsCars;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("Service de Carros")
class CarsServiceTest {

    private CarsService service;

    private CarsRepository repository;

    private CarsMapper mapper;

    private ValidationsCars validationsCars;


    @BeforeEach
    void setUp() {
        this.repository = Mockito.mock(CarsRepository.class);
        this.mapper = Mockito.mock(CarsMapper.class);
        this.validationsCars = Mockito.mock(ValidationsCars.class);
        this.service = new CarsService(repository, mapper, validationsCars);
    }

    @Test
    @DisplayName("Deve retornar todos os carros com sucesso")
    void testFindAllSuccess() {
        Cars car = new Cars();
        when(repository.findAll()).thenReturn(Collections.singletonList(car));
        when(mapper.toListDTO(Collections.singletonList(car))).thenReturn(Collections.singletonList(new CarsDTO()));

        List<CarsDTO> result = service.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Deve lançar CarsNotFoundException quando houver erro ao procurar todos os carros")
    void testFindAllNoCarsFound() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        CarsNotFoundException exception = assertThrows(CarsNotFoundException.class, () -> {
            service.findAll();
        });

        assertEquals("Nenhum carro encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar um carro, pelo id, com sucesso")
    void testFindByIdSuccess() {
        Cars car = new Cars();
        when(repository.findById(1L)).thenReturn(Optional.of(car));
        when(mapper.toDTO(car)).thenReturn(new CarsDTO());

        CarsDTO result = service.findById(1L);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Deve lançar CarsNotFoundException quando houver erro ao procurar um carro pelo seu id")
    void testFindByIdNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CarsNotFoundException.class, () -> {
            service.findById(1L);
        });
    }

    @Test
    @DisplayName("Deve atualizar um carro, pelo id, com sucesso")
    void testUpdateCarsByIdSuccess() {
        CarsDTO dto = new CarsDTO();
        dto.setYears("2023");
        dto.setLicensePlate("ABC1234");
        dto.setModel("Model X");
        dto.setColor("Red");

        Cars existingCar = new Cars();
        when(validationsCars.validateCarsExists(1L)).thenReturn(existingCar);

        service.updateCarsById(1L, dto);

        verify(repository, times(1)).saveAndFlush(existingCar);
    }

    @Test
    @DisplayName("Deve lançar CarsNotFoundException quando houver erro ao atualizar um carro pelo seu id")
    void testUpdatedNotFound() {
        Long id = 1L;
        CarsDTO dto = new CarsDTO();

        when(validationsCars.validateCarsExists(id)).thenThrow(new CarsNotFoundException("Carro não encontrado", HttpStatus.NOT_FOUND.value()));

        assertThrows(CarsNotFoundException.class, () -> {
            service.updateCarsById(id, dto);
        });

        verify(validationsCars, times(1)).validateCarsExists(id);
        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    @DisplayName("Deve lançar CarsBadRequestException quando houver erro ao atualizar um carro pelo seu id")
    void testUpdateCarsByIdInvalidLicenseChange() {
        Long id = 1L;
        CarsDTO dto = new CarsDTO();
        dto.setLicensePlate("ABC1234");

        Cars existingCar = new Cars();
        existingCar.setId(id);
        existingCar.setLicensePlate("XYZ5678");

        when(validationsCars.validateCarsExists(id)).thenReturn(existingCar);

        doThrow(new CarsGeneralException("A licensa já está em uso.", HttpStatus.BAD_REQUEST.value())).when(validationsCars).validateLicenseChange(existingCar, dto.getLicensePlate());

        assertThrows(CarsGeneralException.class, () -> {
            service.updateCarsById(id, dto);
        });

        verify(validationsCars, times(1)).validateCarsExists(id);
        verify(validationsCars, times(1)).validateLicenseChange(existingCar, dto.getLicensePlate());
        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    @DisplayName("Deve lançar CarsGeneralException quando houver erro ao atualizar um carro pelo seu id")
    void testUpdateCarsByIdUnknownError() {
        Long id = 1L;
        CarsDTO dto = new CarsDTO();

        Cars existingCar = new Cars();
        existingCar.setId(id);

        when(validationsCars.validateCarsExists(id)).thenReturn(existingCar);

        when(repository.saveAndFlush(existingCar)).thenThrow(new RuntimeException("Erro desconhecido"));

        assertThrows(CarsGeneralException.class, () -> {
            service.updateCarsById(id, dto);
        });

        verify(validationsCars, times(1)).validateCarsExists(id);
        verify(repository, times(1)).saveAndFlush(existingCar);
    }

    @Test
    @DisplayName("Deve salvar um carro com sucesso")
    void testSaveSuccess() {
        CarsDTO dto = new CarsDTO();
        dto.setYears("2023");
        dto.setLicensePlate("ABC1234");
        dto.setModel("Model X");
        dto.setColor("Red");

        when(repository.findByLicensePlate("ABC1234")).thenReturn(null);

        Cars carEntity = new Cars();
        carEntity.setId(1L);

        when(mapper.toEntity(dto)).thenReturn(carEntity);
        when(repository.save(carEntity)).thenReturn(carEntity);

        when(mapper.toDTO(any(Cars.class))).thenReturn(dto);

        service.save(dto);

        verify(repository, times(1)).save(carEntity);
        verify(mapper, times(1)).toDTO(carEntity);
    }

    @Test
    @DisplayName("Deve lançar CarsBadRequestException ao tentar salvar um carro")
    void testSaveLicensePlateAlreadyExists() {
        CarsDTO dto = new CarsDTO();
        dto.setLicensePlate("ABC1234");

        when(repository.findByLicensePlate("ABC1234")).thenReturn(new Cars());

        CarsBadRequestException exception = assertThrows(CarsBadRequestException.class, () -> {
            service.save(dto);
        });

        assertEquals("License plate already exists", exception.getMessage());
    }

    @Test
    @DisplayName("Deve deletar um carro com sucesso")
    void testDeleteCarsSuccess() {
        final var id = 1L;
        final var carEntity = new Cars();
        carEntity.setId(id);

        final var carDTO = new CarsDTO();
        carDTO.setId(id);

        when(repository.existsById(id)).thenReturn(true);

        doNothing().when(repository).deleteById(id);

        service.deleteCars(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar um CarsNotFoundException ao tentar deletar um carro pelo seu id")
    void testDeleteCarsNotFound() {
        when(repository.existsById(1L)).thenReturn(false);

        CarsNotFoundException exception = assertThrows(CarsNotFoundException.class, () -> {
            service.deleteCars(1L);
        });

        assertEquals("Carro não encontrado com o id '1'.", exception.getMessage());
    }

}
package br.com.thallysprojects.pitang_desafio.services;

import br.com.thallysprojects.pitang_desafio.dtos.CarsDTO;
import br.com.thallysprojects.pitang_desafio.entities.Cars;
import br.com.thallysprojects.pitang_desafio.exceptions.CarsNotFoundException;
import br.com.thallysprojects.pitang_desafio.exceptions.UsersNotFoundException;
import br.com.thallysprojects.pitang_desafio.mappers.CarsMapper;
import br.com.thallysprojects.pitang_desafio.repositories.CarsRepository;
import br.com.thallysprojects.pitang_desafio.utils.ValidationsCars;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


//@ExtendWith(MockitoExtension.class)
class CarsServiceTest {

    //    @InjectMocks
    private CarsService service;

    //    @Mock
    private CarsRepository repository;

    //    @Mock
    private CarsMapper mapper;

    //    @Mock
    private ValidationsCars validationsCars;


    @BeforeEach
    void setUp() {
        this.repository = Mockito.mock(CarsRepository.class);
        this.mapper = Mockito.mock(CarsMapper.class);
        this.validationsCars = Mockito.mock(ValidationsCars.class);
        this.service = new CarsService(repository, mapper, validationsCars);
    }

    @Test
    void testFindAllSuccess() {
        // Arrange
        Cars car = new Cars();
        when(repository.findAll()).thenReturn(Collections.singletonList(car));
        when(mapper.toListDTO(Collections.singletonList(car))).thenReturn(Collections.singletonList(new CarsDTO()));

        // Act
        List<CarsDTO> result = service.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindAllNoCarsFound() {
        // Arrange
        when(repository.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        CarsNotFoundException exception = assertThrows(CarsNotFoundException.class, () -> {
            service.findAll();
        });

        assertEquals("Nenhum carro encontrado", exception.getMessage());
    }

    @Test
    void testFindByIdSuccess() {
        // Arrange
        Cars car = new Cars();
        when(repository.findById(1L)).thenReturn(Optional.of(car));
        when(mapper.toDTO(car)).thenReturn(new CarsDTO());

        // Act
        CarsDTO result = service.findById(1L);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testFindByIdNotFound() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CarsNotFoundException.class, () -> {
            service.findById(1L);
        });
    }

    @Test
    void testUpdateCarsByIdSuccess() {
        // Arrange
        CarsDTO dto = new CarsDTO();
        dto.setYears("2023");
        dto.setLicensePlate("ABC1234");
        dto.setModel("Model X");
        dto.setColor("Red");

        Cars existingCar = new Cars();
        when(validationsCars.validateCarsExists(1L)).thenReturn(existingCar);

        // Act
        service.updateCarsById(1L, dto);

        // Assert
        verify(repository, times(1)).saveAndFlush(existingCar);
    }

    @Test
    void testSaveSuccess() {
        // Arrange
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

        // Act
        service.save(dto);

        // Assert
        verify(repository, times(1)).save(carEntity); // Verifica se o save foi chamado com o carEntity
        verify(mapper, times(1)).toDTO(carEntity);
    }

    @Test
    void testSaveLicensePlateAlreadyExists() {
        // Arrange
        CarsDTO dto = new CarsDTO();
        dto.setLicensePlate("ABC1234");

        when(repository.findByLicensePlate("ABC1234")).thenReturn(new Cars());

        // Act & Assert
        UsersNotFoundException exception = assertThrows(UsersNotFoundException.class, () -> {
            service.save(dto);
        });

        assertEquals("License plate already exists", exception.getMessage());
    }

    @Test
    void testDeleteCarsSuccess() {
        // Criando um carro simulado
        final var id = 1L;
        final var carEntity = new Cars();
        carEntity.setId(id);

        final var carDTO = new CarsDTO();
        carDTO.setId(id);

        // Mockando a busca pelo carro antes de deletar
        when(repository.existsById(id)).thenReturn(true);

        // Mockando a exclusão
        doNothing().when(repository).deleteById(id);

        // Act
        service.deleteCars(id);

        // Assert
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteCarsNotFound() {
        // Arrange
        when(repository.existsById(1L)).thenReturn(false);

        // Act & Assert
        CarsNotFoundException exception = assertThrows(CarsNotFoundException.class, () -> {
            service.deleteCars(1L);
        });

        assertEquals("Carro não encontrado com o id '1'.", exception.getMessage());
    }

}

package br.com.thallysprojects.pitang_desafio.mappers;

import br.com.thallysprojects.pitang_desafio.dtos.CarsDTO;
import br.com.thallysprojects.pitang_desafio.entities.Cars;
import br.com.thallysprojects.pitang_desafio.factorios.CarsFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DisplayName("Mapper cars")
class CarsMapperTest {

    private CarsMapper carsMapper;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        this.modelMapper = Mockito.mock(ModelMapper.class);
        this.carsMapper = new CarsMapper(modelMapper);
    }

    @Test
    @DisplayName("Deve retornar um DTO recebendo uma entidade")
    void testToDTO() {
        Cars car = CarsFactory.createDefaultCar();
        CarsDTO dtoMock = CarsFactory.createDefaultCarDTO();

        when(modelMapper.map(car, CarsDTO.class)).thenReturn(dtoMock);

        CarsDTO dto = carsMapper.toDTO(car);

        assertNotNull(dto);
        assertEquals(car.getId(), dto.getId());
        assertEquals(car.getYears(), dto.getYears());
        assertEquals(car.getLicensePlate(), dto.getLicensePlate());
        assertEquals(car.getModel(), dto.getModel());
        assertEquals(car.getColor(), dto.getColor());
    }

    @Test
    @DisplayName("Deve retornar uma entidade recebendo um DTO")
    void testToEntity() {
        CarsDTO dto = CarsFactory.createDefaultCarDTO();
        Cars carMock = CarsFactory.createDefaultCar();

        when(modelMapper.map(dto, Cars.class)).thenReturn(carMock);

        Cars car = carsMapper.toEntity(dto);

        assertNotNull(car);
        assertEquals(dto.getId(), car.getId());
        assertEquals(dto.getYears(), car.getYears());
        assertEquals(dto.getLicensePlate(), car.getLicensePlate());
        assertEquals(dto.getModel(), car.getModel());
        assertEquals(dto.getColor(), car.getColor());
    }

    @Test
    @DisplayName("Deve retornar uma lista de DTOs recebendo uma lista de entidades")
    void testToListDTO() {
        Cars car1 = CarsFactory.createDefaultCar();
        Cars car2 = CarsFactory.createDefaultCarTwo();

        CarsDTO dto1 = CarsFactory.createDefaultCarDTO();
        CarsDTO dto2 = CarsFactory.createDefaultCarDTOTwo();

        when(modelMapper.map(car1, CarsDTO.class)).thenReturn(dto1);
        when(modelMapper.map(car2, CarsDTO.class)).thenReturn(dto2);

        List<Cars> carsList = List.of(car1, car2);

        List<CarsDTO> dtosList = carsMapper.toListDTO(carsList);

        assertNotNull(dtosList);
        assertEquals(2, dtosList.size());

        assertNotNull(dtosList.get(0), "O primeiro DTO na lista é nulo");
        assertNotNull(dtosList.get(1), "O segundo DTO na lista é nulo");

        assertEquals(car1.getId(), dtosList.get(0).getId(), "ID do primeiro DTO não corresponde");
        assertEquals(car1.getYears(), dtosList.get(0).getYears(), "Ano do primeiro DTO não corresponde");
        assertEquals(car1.getLicensePlate(), dtosList.get(0).getLicensePlate(), "Placa do primeiro DTO não corresponde");
        assertEquals(car1.getModel(), dtosList.get(0).getModel(), "Modelo do primeiro DTO não corresponde");
        assertEquals(car1.getColor(), dtosList.get(0).getColor(), "Cor do primeiro DTO não corresponde");

        assertEquals(car2.getId(), dtosList.get(1).getId(), "ID do segundo DTO não corresponde");
        assertEquals(car2.getYears(), dtosList.get(1).getYears(), "Ano do segundo DTO não corresponde");
        assertEquals(car2.getLicensePlate(), dtosList.get(1).getLicensePlate(), "Placa do segundo DTO não corresponde");
        assertEquals(car2.getModel(), dtosList.get(1).getModel(), "Modelo do segundo DTO não corresponde");
        assertEquals(car2.getColor(), dtosList.get(1).getColor(), "Cor do segundo DTO não corresponde");
    }

    @Test
    @DisplayName("Deve retornar uma lista de entidades recebendo uma lista de DTOs")
    void testToList() {
        CarsDTO dto1 = CarsFactory.createDefaultCarDTO();
        CarsDTO dto2 = CarsFactory.createDefaultCarDTOTwo();

        List<CarsDTO> dtosList = Arrays.asList(dto1, dto2);

        Cars car1 = CarsFactory.createDefaultCar();
        Cars car2 = CarsFactory.createDefaultCarTwo();

        when(modelMapper.map(dto1, Cars.class)).thenReturn(car1);
        when(modelMapper.map(dto2, Cars.class)).thenReturn(car2);

        List<Cars> carsList = carsMapper.toList(dtosList);

        assertNotNull(carsList);
        assertEquals(2, carsList.size());

        assertNotNull(carsList.get(0), "O primeiro carro na lista é nulo");
        assertNotNull(carsList.get(1), "O segundo carro na lista é nulo");

        assertEquals(dto1.getId(), carsList.get(0).getId(), "ID do primeiro carro não corresponde");
        assertEquals(dto1.getYears(), carsList.get(0).getYears(), "Ano do primeiro carro não corresponde");
        assertEquals(dto1.getLicensePlate(), carsList.get(0).getLicensePlate(), "Placa do primeiro carro não corresponde");
        assertEquals(dto1.getModel(), carsList.get(0).getModel(), "Modelo do primeiro carro não corresponde");
        assertEquals(dto1.getColor(), carsList.get(0).getColor(), "Cor do primeiro carro não corresponde");

        assertEquals(dto2.getId(), carsList.get(1).getId(), "ID do segundo carro não corresponde");
        assertEquals(dto2.getYears(), carsList.get(1).getYears(), "Ano do segundo carro não corresponde");
        assertEquals(dto2.getLicensePlate(), carsList.get(1).getLicensePlate(), "Placa do segundo carro não corresponde");
        assertEquals(dto2.getModel(), carsList.get(1).getModel(), "Modelo do segundo carro não corresponde");
        assertEquals(dto2.getColor(), carsList.get(1).getColor(), "Cor do segundo carro não corresponde");
    }

}
package br.com.thallysprojects.pitang_desafio.mappers;

import br.com.thallysprojects.pitang_desafio.dtos.CarsDTO;
import br.com.thallysprojects.pitang_desafio.entities.Cars;
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
        Cars car = new Cars();
        car.setId(1L);
        car.setYears("2023");
        car.setLicensePlate("ABC1234");
        car.setModel("Model X");
        car.setColor("Red");

        CarsDTO dtoMock = new CarsDTO();
        dtoMock.setId(car.getId());
        dtoMock.setYears(car.getYears());
        dtoMock.setLicensePlate(car.getLicensePlate());
        dtoMock.setModel(car.getModel());
        dtoMock.setColor(car.getColor());

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
        CarsDTO dto = new CarsDTO();
        dto.setId(1L);
        dto.setYears("2023");
        dto.setLicensePlate("ABC1234");
        dto.setModel("Model X");
        dto.setColor("Red");

        Cars carMock = new Cars();
        carMock.setId(dto.getId());
        carMock.setYears(dto.getYears());
        carMock.setLicensePlate(dto.getLicensePlate());
        carMock.setModel(dto.getModel());
        carMock.setColor(dto.getColor());

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
        Cars car1 = new Cars();
        car1.setId(1L);
        car1.setYears("2023");
        car1.setLicensePlate("ABC1234");
        car1.setModel("Model X");
        car1.setColor("Red");

        Cars car2 = new Cars();
        car2.setId(2L);
        car2.setYears("2022");
        car2.setLicensePlate("XYZ5678");
        car2.setModel("Model Y");
        car2.setColor("Blue");

        List<Cars> carsList = List.of(car1, car2);

        CarsDTO dto1 = new CarsDTO();
        dto1.setId(car1.getId());
        dto1.setYears(car1.getYears());
        dto1.setLicensePlate(car1.getLicensePlate());
        dto1.setModel(car1.getModel());
        dto1.setColor(car1.getColor());

        CarsDTO dto2 = new CarsDTO();
        dto2.setId(car2.getId());
        dto2.setYears(car2.getYears());
        dto2.setLicensePlate(car2.getLicensePlate());
        dto2.setModel(car2.getModel());
        dto2.setColor(car2.getColor());

        when(modelMapper.map(car1, CarsDTO.class)).thenReturn(dto1);
        when(modelMapper.map(car2, CarsDTO.class)).thenReturn(dto2);


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
        CarsDTO dto1 = new CarsDTO();
        dto1.setId(1L);
        dto1.setYears("2023");
        dto1.setLicensePlate("ABC1234");
        dto1.setModel("Model X");
        dto1.setColor("Red");

        CarsDTO dto2 = new CarsDTO();
        dto2.setId(2L);
        dto2.setYears("2022");
        dto2.setLicensePlate("XYZ5678");
        dto2.setModel("Model Y");
        dto2.setColor("Blue");

        List<CarsDTO> dtosList = Arrays.asList(dto1, dto2);

        Cars car1 = new Cars();
        car1.setId(dto1.getId());
        car1.setYears(dto1.getYears());
        car1.setLicensePlate(dto1.getLicensePlate());
        car1.setModel(dto1.getModel());
        car1.setColor(dto1.getColor());

        Cars car2 = new Cars();
        car2.setId(dto2.getId());
        car2.setYears(dto2.getYears());
        car2.setLicensePlate(dto2.getLicensePlate());
        car2.setModel(dto2.getModel());
        car2.setColor(dto2.getColor());

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
package br.com.thallysprojects.pitang_desafio.factorios;

import br.com.thallysprojects.pitang_desafio.dtos.CarsDTO;
import br.com.thallysprojects.pitang_desafio.entities.Cars;

import java.util.List;

public class CarsFactory {

    public static Cars createCar(Long id, String years, String licensePlate, String model, String color) {
        Cars car = new Cars();
        car.setId(id);
        car.setYears(years);
        car.setLicensePlate(licensePlate);
        car.setModel(model);
        car.setColor(color);
        return car;
    }

    public static CarsDTO createCarDTO(Long id, String years, String licensePlate, String model, String color) {
        CarsDTO dto = new CarsDTO();
        dto.setId(id);
        dto.setYears(years);
        dto.setLicensePlate(licensePlate);
        dto.setModel(model);
        dto.setColor(color);
        return dto;
    }

    public static Cars createDefaultCar() {
        return createCar(1L, "2023", "ABC1234", "Model X", "Red");
    }

    public static Cars createDefaultCarTwo() {
        return createCar(2L, "2022", "XYZ5678", "Model Y", "Blue");
    }

    public static CarsDTO createDefaultCarDTO() {
        return createCarDTO(1L, "2023", "ABC1234", "Model X", "Red");
    }

    public static CarsDTO createDefaultCarDTOTwo() {
        return createCarDTO(2L, "2022", "XYZ5678", "Model Y", "Blue");
    }

    public static List<Cars> createCarsList() {
        return List.of(
                createDefaultCar(),
                createDefaultCarTwo()
        );
    }

    public static List<CarsDTO> createCarsDTOList() {
        return List.of(
                createDefaultCarDTO(),
                createDefaultCarDTOTwo()
        );
    }

}
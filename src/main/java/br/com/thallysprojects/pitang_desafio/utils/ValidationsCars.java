package br.com.thallysprojects.pitang_desafio.utils;

import br.com.thallysprojects.pitang_desafio.entities.Cars;
import br.com.thallysprojects.pitang_desafio.exceptions.cars.CarsBadRequestException;
import br.com.thallysprojects.pitang_desafio.exceptions.cars.CarsGeneralException;
import br.com.thallysprojects.pitang_desafio.exceptions.cars.CarsNotFoundException;
import br.com.thallysprojects.pitang_desafio.mappers.CarsMapper;
import br.com.thallysprojects.pitang_desafio.repositories.CarsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidationsCars {

    private final CarsRepository carsRepository;
    private final CarsMapper carsMapper;

    public Cars validateCarsExists(Long id) {
        return carsRepository.findById(id)
                .orElseThrow(() -> new CarsNotFoundException(
                        String.format("Cars não encontrado com o id '%s'.", id),
                        HttpStatus.NOT_FOUND.value()
                ));
    }


    public void validateLicenseChange(Cars existingCar, String newLicense) {
        if (!existingCar.getLicensePlate().equals(newLicense) && carsRepository.existsByLicensePlate(newLicense)) {
            throw new CarsBadRequestException("A licensa já está em uso.", HttpStatus.BAD_REQUEST.value());
        }

        existingCar.setLicensePlate(newLicense);
    }

}
package br.com.thallysprojects.pitang_desafio.repositories;

import br.com.thallysprojects.pitang_desafio.entities.Cars;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarsRepository extends JpaRepository<Cars, Long> {

    boolean existsByLicensePlate(String license);

    Cars findByLicensePlate(String license);

}
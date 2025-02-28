package br.com.thallysprojects.pitang_desafio.repositories;

import br.com.thallysprojects.pitang_desafio.entities.Cars;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface CarsRepository extends JpaRepository<Cars, Long> {

    boolean existsByLicensePlate(String license);

    Cars findByLicensePlate(String license);

    @Modifying
    @Transactional
    @Query("DELETE FROM Cars c WHERE c.users.id IN (SELECT u.id FROM Users u WHERE u.lastLogin < :cutoffDate)")
    void deleteCarsOfInactiveUsers(LocalDate cutoffDate);

}
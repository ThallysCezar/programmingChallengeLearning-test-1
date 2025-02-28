package br.com.thallysprojects.pitang_desafio.repositories;

import br.com.thallysprojects.pitang_desafio.entities.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface UsersRepository extends JpaRepository<Users, Long> {

    boolean existsByEmail(String email);

    boolean existsByLogin(String login);

    Users findByEmail(String email);

    Users findByLogin(String login);

    @Modifying
    @Transactional
    @Query("DELETE FROM Users u WHERE u.lastLogin < :cutoffDate")
    void deleteByLastLoginBefore(LocalDate cutoffDate);

}
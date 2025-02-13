package br.com.thallysprojects.pitang_desafio.repositories;

import br.com.thallysprojects.pitang_desafio.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    boolean existsByEmail(String email);

    boolean existsByLogin(String login);
}
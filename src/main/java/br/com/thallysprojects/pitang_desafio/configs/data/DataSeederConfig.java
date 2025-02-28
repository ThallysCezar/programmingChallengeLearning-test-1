package br.com.thallysprojects.pitang_desafio.configs.data;

import br.com.thallysprojects.pitang_desafio.entities.Cars;
import br.com.thallysprojects.pitang_desafio.entities.UserRole;
import br.com.thallysprojects.pitang_desafio.entities.Users;
import br.com.thallysprojects.pitang_desafio.repositories.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class DataSeederConfig implements CommandLineRunner {

    private final UsersRepository usersRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public DataSeederConfig(UsersRepository usersRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (usersRepository.count() == 0) {

            Users user1 = new Users();
            user1.setFirstName("Joao");
            user1.setLastName("Silva");
            user1.setEmail("joao@email.com");
            user1.setBirthday(LocalDate.now().minusYears(25));
            user1.setLogin("joaosilva");

            user1.setPassword(passwordEncoder.encode("senha123"));
            user1.setPhone("11999999999");
            user1.setRole(UserRole.ADMIN);

            Cars car1 = new Cars();
            car1.setYears("2020");
            car1.setLicensePlate("ABC-1234");
            car1.setModel("Honda Civic");
            car1.setColor("Preto");
            car1.setUsers(user1);

            Cars car2 = new Cars();
            car2.setYears("2018");
            car2.setLicensePlate("XYZ-5678");
            car2.setModel("Toyota Corolla");
            car2.setColor("Branco");
            car2.setUsers(user1);

            user1.setCars(List.of(car1, car2));

            Users user2 = new Users();
            user2.setFirstName("Maria");
            user2.setLastName("Santos");
            user2.setEmail("maria@email.com");
            user2.setBirthday(LocalDate.now().minusYears(30));
            user2.setLogin("mariasantos");
            user2.setPassword(passwordEncoder.encode("senha456"));
            user2.setPhone("11888888888");
            user2.setRole(UserRole.LOJISTA);
            user2.setLastLogin(LocalDate.now().minusDays(31));

            Cars car3 = new Cars();
            car3.setYears("2015");
            car3.setLicensePlate("DEF-9012");
            car3.setModel("Ford Fusion");
            car3.setColor("Prata");
            car3.setUsers(user2);

            user2.setCars(List.of(car3));
            usersRepository.saveAll(List.of(user1, user2));

            log.info("Dados iniciais inseridos no banco!");
        } else {
            log.info("Os dados já existem no banco.");
        }
    }

}
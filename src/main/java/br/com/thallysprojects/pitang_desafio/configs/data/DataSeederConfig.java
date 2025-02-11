package br.com.thallysprojects.pitang_desafio.configs.data;

import br.com.thallysprojects.pitang_desafio.entities.Cars;
import br.com.thallysprojects.pitang_desafio.entities.Users;
import br.com.thallysprojects.pitang_desafio.repositories.CarsRepository;
import br.com.thallysprojects.pitang_desafio.repositories.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@Slf4j
//@RequiredArgsConstructor
public class DataSeederConfig implements CommandLineRunner{

    private final UsersRepository usersRepository;

    public DataSeederConfig(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

//    @Bean
//    CommandLineRunner initDatabase(UsersRepository usersRepository, CarsRepository carsRepository) {
//        return args -> {
//
//            // Criando usuários e garantindo que estão gerenciados pelo Hibernate
//            Users user1 = usersRepository.findByEmail("joao.silva@email.com")
//                    .orElseGet(() -> usersRepository.saveAndFlush(
//                            new Users(null, "João", "Silva", "joao.silva@email.com",
//                                    OffsetDateTime.now().minusYears(30), "joaosilva", "123456", "11987654321", null)
//                    ));
//
//            Users user2 = usersRepository.findByEmail("maria.oliveira@email.com")
//                    .orElseGet(() -> usersRepository.saveAndFlush(
//                            new Users(null, "Maria", "Oliveira", "maria.oliveira@email.com",
//                                    OffsetDateTime.now().minusYears(25), "mariaoliveira", "654321", "11912345678", null)
//                    ));
//
//            // Recarregando os usuários do banco para garantir que estão gerenciados
//            user1 = usersRepository.findByEmail(user1.getEmail()).orElseThrow();
//            user2 = usersRepository.findByEmail(user2.getEmail()).orElseThrow();
//
//            // Criando carros apenas se ainda não existirem
//            if (carsRepository.findByLicensePlate("ABC-1234").isEmpty()) {
//                Cars car1 = new Cars();
//                car1.setYears("2020");
//                car1.setLicensePlate("ABC-1234");
//                car1.setModel("Toyota Corolla");
//                car1.setColor("Prata");
//                car1.setUsers(user1);
//                carsRepository.saveAndFlush(car1);
//            }
//
//            if (carsRepository.findByLicensePlate("XYZ-5678").isEmpty()) {
//                Cars car2 = new Cars();
//                car2.setYears("2022");
//                car2.setLicensePlate("XYZ-5678");
//                car2.setModel("Honda Civic");
//                car2.setColor("Preto");
//                car2.setUsers(user2);
//                carsRepository.saveAndFlush(car2);
//            }
//
//            System.out.println("✅ Dados iniciais inseridos com sucesso!");
//        };
//    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (usersRepository.count() == 0) { // Evita duplicação de dados

            // Criando um usuário
            Users user1 = new Users();
            user1.setFirstName("Joao");
            user1.setLastName("Silva");
            user1.setEmail("joao@email.com");
            user1.setBrithDay(OffsetDateTime.now().minusYears(25)); // Exemplo de data de nascimento
            user1.setLogin("joaosilva");
            user1.setPassword("senha123");
            user1.setPhone("11999999999");

            // Criando um carro e associando ao usuário
            Cars car1 = new Cars();
            car1.setYears("2020");
            car1.setLicensePlate("ABC-1234");
            car1.setModel("Honda Civic");
            car1.setColor("Preto");
            car1.setUsers(user1); // Associação com o usuário

            Cars car2 = new Cars();
            car2.setYears("2018");
            car2.setLicensePlate("XYZ-5678");
            car2.setModel("Toyota Corolla");
            car2.setColor("Branco");
            car2.setUsers(user1); // Associação com o mesmo usuário

            // Associando os carros ao usuário
            user1.setCars(List.of(car1, car2));

            // Salvando tudo no banco
            usersRepository.save(user1);

            log.info("Dados iniciais inseridos no banco!");
//            System.out.println("Dados iniciais inseridos no banco!");
        } else {
            log.info("Os dados já existem no banco.");
//            System.out.println("Os dados já existem no banco.");
        }
    }

}
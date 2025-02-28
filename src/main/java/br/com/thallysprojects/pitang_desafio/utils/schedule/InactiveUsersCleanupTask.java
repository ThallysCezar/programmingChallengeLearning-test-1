package br.com.thallysprojects.pitang_desafio.utils.schedule;

import br.com.thallysprojects.pitang_desafio.repositories.CarsRepository;
import br.com.thallysprojects.pitang_desafio.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class InactiveUsersCleanupTask {

    private final UsersRepository usersRepository;
    private final CarsRepository carsRepository;

//    @Scheduled(initialDelay = 1 * 60 * 1000, fixedRate = Long.MAX_VALUE) // delay de 1 minuto, apenas para testar
    @Scheduled(initialDelay = 5 * 60 * 1000, fixedRate = Long.MAX_VALUE) // delay de 5 minutos
    public void limparRegistrosAntigos() {
        LocalDate cutoffDate = LocalDate.now().minusDays(30);

        carsRepository.deleteCarsOfInactiveUsers(cutoffDate);
        usersRepository.deleteByLastLoginBefore(cutoffDate);
        log.info("Usuarios inativos removidos com sucesso.");
    }

}
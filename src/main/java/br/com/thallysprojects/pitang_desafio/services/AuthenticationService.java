package br.com.thallysprojects.pitang_desafio.services;

import br.com.thallysprojects.pitang_desafio.configs.security.TokenService;
import br.com.thallysprojects.pitang_desafio.dtos.AuthenticationRegisterDTO;
import br.com.thallysprojects.pitang_desafio.dtos.AuthenticationUserDTO;
import br.com.thallysprojects.pitang_desafio.entities.Users;
import br.com.thallysprojects.pitang_desafio.exceptions.UsersGeneralException;
import br.com.thallysprojects.pitang_desafio.exceptions.UsersNotFoundException;
import br.com.thallysprojects.pitang_desafio.repositories.UsersRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager manager;
    private final TokenService tokenService;
    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public String login(AuthenticationUserDTO dto) {
        Users user = usersRepository.findByLogin(dto.getLogin());
        if(user == null){
            throw new UsersNotFoundException("Invalid login or password", HttpStatus.BAD_REQUEST.value());
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new UsersNotFoundException("Credenciais inválidas", HttpStatus.BAD_REQUEST.value());
        }

        user.setLastLogin(LocalDate.now());
        usersRepository.save(user);

        return tokenService.gerarTokenJwt(user);
    }

}
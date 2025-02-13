package br.com.thallysprojects.pitang_desafio.services;

import br.com.thallysprojects.pitang_desafio.configs.security.TokenService;
import br.com.thallysprojects.pitang_desafio.dtos.AuthenticationRegisterDTO;
import br.com.thallysprojects.pitang_desafio.dtos.AuthenticationUserDTO;
import br.com.thallysprojects.pitang_desafio.entities.Users;
import br.com.thallysprojects.pitang_desafio.repositories.UsersRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager manager;
    private final TokenService tokenService;
    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder passwordEncoder;

//    @Transactional
//    public String login(AuthenticationUserDTO dto){
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getPassword());
//
//        Authentication authentication = manager.authenticate(authenticationToken);
//        Users user = (Users) authentication.getPrincipal();
//
//        return tokenService.gerarTokenJwt(user);
//    }

    @Transactional
    public String login(AuthenticationUserDTO dto) {
        Users user = usersRepository.findByLogin(dto.getLogin());

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        return tokenService.gerarTokenJwt(user);
    }


    @Transactional
    public ResponseEntity<String> register(@RequestBody @Valid AuthenticationRegisterDTO data) {
        if (usersRepository.findByEmail(data.getEmail()) != null || usersRepository.findByLogin(data.getLogin()) != null) {
            return ResponseEntity.badRequest().body("Usuário já cadastrado");
        }

        String encryptedPassword = passwordEncoder.encode(data.getPassword());

        Users newUser = new Users();
        newUser.setEmail(data.getEmail());
        newUser.setLogin(data.getLogin());
        newUser.setPassword(encryptedPassword);
        newUser.setRole(data.getUserRole());

        usersRepository.save(newUser);

        return ResponseEntity.ok("Usuário cadastrado com sucesso");
    }

}
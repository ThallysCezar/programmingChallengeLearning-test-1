package br.com.thallysprojects.pitang_desafio.controllers;

import br.com.thallysprojects.pitang_desafio.dtos.AuthenticationRegisterDTO;
import br.com.thallysprojects.pitang_desafio.dtos.AuthenticationUserDTO;
import br.com.thallysprojects.pitang_desafio.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/signin")
    public ResponseEntity<String> login(@RequestBody @Valid AuthenticationUserDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(service.login(dto));
    }

//    @PostMapping("/register")
//    public ResponseEntity register(@RequestBody @Valid AuthenticationRegisterDTO dto) {
//        return ResponseEntity.status(HttpStatus.OK).body(service.register(dto));
//    }
//
//    @GetMapping
//    public ResponseEntity<String> cliente() {
//        return ResponseEntity.status(HttpStatus.OK).body("Olá, essa é a rota de cliente! você está autenticado.");
//    }

}

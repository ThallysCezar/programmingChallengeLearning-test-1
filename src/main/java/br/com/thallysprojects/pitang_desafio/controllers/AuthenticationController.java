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
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid AuthenticationUserDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(service.login(dto));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid AuthenticationRegisterDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(service.register(dto));
    }

    //    @Operation(summary = "Retornar o token de acesso")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "User created successfully"),
//            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
//            @ApiResponse(responseCode = "400", description = "Invalid input provided")
//    })
//    @GetMapping("/me")
////    @ResponseStatus(HttpStatus.CREATED)
//    // retorna o token de acesso
//    public void retornMe(
//            @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                    description = "User to create", required = true,
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = UsersDTO.class),
//                            examples = @ExampleObject(value = """
//                                    {
//                                        "firstName": "John",
//                                        "lastName": "Doe",
//                                        "email": "john.doe@example.com",
//                                        "birthday": "1990-05-15",
//                                        "login": "johndoe",
//                                        "password": "securePassword123",
//                                        "phone": "123-456-7890",
//                                        "cars": [
//                                            {
//                                                "id": 1,
//                                                "model": "Toyota Corolla",
//                                                "color": "Black",
//                                                "licensePlate": "ABC-1234",
//                                                "years": 2020
//                                            }
//                                        ]
//                                    }""")))
//            @Valid @RequestBody AuthenticationUserDTO dto) {
////        return ResponseEntity.ok().body(service.findAll());
//    }

    @GetMapping
    public ResponseEntity<String> cliente() {
        return ResponseEntity.status(HttpStatus.OK).body("Olá, essa é a rota de cliente! você está autenticado.");
    }

}

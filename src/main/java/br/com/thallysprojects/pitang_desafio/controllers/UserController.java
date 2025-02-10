package br.com.thallysprojects.pitang_desafio.controllers;

import br.com.thallysprojects.pitang_desafio.configs.security.TokenService;
import br.com.thallysprojects.pitang_desafio.dtos.AuthenticationDTO;
import br.com.thallysprojects.pitang_desafio.dtos.LoginResponseDTO;
import br.com.thallysprojects.pitang_desafio.dtos.UsersDTO;
import br.com.thallysprojects.pitang_desafio.entities.Users;
import br.com.thallysprojects.pitang_desafio.services.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
//@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    @Autowired
    UsersService service;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenService tokenService;

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Users",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsersDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "There not found users",
                    content = @Content)})
    @GetMapping
    public ResponseEntity<List<UsersDTO>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

//    @GetMapping("/filterToUsersWithPageable")
//    //Colocar paginação para dar uma melhorada
//    public Page<UsersDTO> filterBooks(@ParameterObject Pageable pageable) {
//        return service.findAllWithPageable(pageable);
//    }

    @Operation(summary = "Get a users by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the User",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsersDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<UsersDTO> findUserById(@Valid @PathVariable
                                                 @Parameter(description = "id of user to be searched") Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @Operation(summary = "Update a user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Updated the User successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsersDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})
    @PutMapping("/{id}")
    public ResponseEntity<UsersDTO> updateUser(@Valid @PathVariable
                                               @Parameter(description = "id of user to be searched") Long id) {
        service.updateUserById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsersDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input provided")})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity save(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User to create", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsersDTO.class),
                            examples = @ExampleObject(value = "{ \"title\": \"New User\", \"author\": \"Author Name\" }")))
            @Valid @RequestBody UsersDTO dto) {
        if (service.loadUserByUsername(dto.getLogin()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.getPassword());

        UsersDTO newUser = new UsersDTO(dto.getLogin(), encryptedPassword, dto.getRole());

        service.save(newUser);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete a user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User deleted successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsersDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@Valid @PathVariable @Parameter(description = "id of user to be searched")
                           Long id) throws Exception {
        service.deleteUsers(id);
    }


    @PostMapping("signin")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        final var userNamePassword = new UsernamePasswordAuthenticationToken(data.getLogin(), data.getPassword());
        final var auth = this.authenticationManager.authenticate(userNamePassword);

        final var token = tokenService.generateToken((Users) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

}

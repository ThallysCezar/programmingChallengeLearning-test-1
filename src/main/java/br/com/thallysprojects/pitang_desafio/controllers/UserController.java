package br.com.thallysprojects.pitang_desafio.controllers;

import br.com.thallysprojects.pitang_desafio.dtos.UsersDTO;
import br.com.thallysprojects.pitang_desafio.services.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UsersService service;

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "404", description = "No users found")
    })
    @GetMapping
    public ResponseEntity<List<UsersDTO>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @Operation(summary = "Get a user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsersDTO> findUserById(@Valid @PathVariable
                                                 @Parameter(description = "id of user to be searched") Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @Operation(summary = "Update a user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsersDTO> updateUser(
            @PathVariable @Parameter(description = "ID of the user") Long id,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User data to update",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsersDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "firstName": "John",
                                        "lastName": "Doe",
                                        "email": "john.doe@example.com",
                                        "birthday": "1990-05-15",
                                        "login": "johndoe",
                                        "password": "securePassword123",
                                        "phone": "123-456-7890",
                                        "cars": [
                                            {
                                                "id": 1,
                                                "model": "Toyota Corolla",
                                                "color": "Black",
                                                "licensePlate": "ABC-1234",
                                                "years": 2020
                                            }
                                        ]
                                    }""")))
            UsersDTO dto) {
        service.updateUserById(id, dto);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User to create", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsersDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "firstName": "John",
                                        "lastName": "Doe",
                                        "email": "john.doe@example.com",
                                        "birthday": "1990-05-15",
                                        "login": "johndoe",
                                        "password": "securePassword123",
                                        "phone": "123-456-7890",
                                        "cars": [
                                            {
                                                "id": 1,
                                                "model": "Toyota Corolla",
                                                "color": "Black",
                                                "licensePlate": "ABC-1234",
                                                "years": 2020
                                            }
                                        ]
                                    }""")))
            @Valid @RequestBody UsersDTO dto) {
        service.save(dto);
    }

    @Operation(summary = "Delete a user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Error Intern in User"),
            @ApiResponse(responseCode = "404", description = "User not found")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@Valid @PathVariable @Parameter(description = "id of user to be searched")
                           Long id) {
        service.deleteUsers(id);
    }

}

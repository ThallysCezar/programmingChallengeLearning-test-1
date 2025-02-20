package br.com.thallysprojects.pitang_desafio.controllers;

import br.com.thallysprojects.pitang_desafio.dtos.CarsDTO;
import br.com.thallysprojects.pitang_desafio.dtos.UsersDTO;
import br.com.thallysprojects.pitang_desafio.services.CarsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarsController {

    private final CarsService service;

    @Operation(summary = "Get all cars")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Cars"),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "There not found cars")})
    @GetMapping
    //Colocar paginação para dar uma melhorada
    public ResponseEntity<List<CarsDTO>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @Operation(summary = "Get a cars by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Cars"),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Car not found")})
    @GetMapping("/{id}")
    public ResponseEntity<CarsDTO> findCarById(@Valid @PathVariable
                                               @Parameter(description = "id of car to be searched") Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

//    @Operation(summary = "Update a car by its id")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Updated the Car successfully"),
//            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
//            @ApiResponse(responseCode = "404", description = "Car not found")})
//    @PutMapping("/{id}")
//    public ResponseEntity<CarsDTO> updateCars(
//            @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                    description = "User data to update",
//                    required = true,
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = CarsDTO.class),
//                            examples = @ExampleObject(value = """
//                                    {
//                                     "year": 2018,
//                                     "licensePlate": "PDV-0625",
//                                     "model": "Audi",
//                                     "color": "White"
//                                     }"""))) @Valid @PathVariable
//            @Parameter(description = "id of user to be searched") Long id, CarsDTO dto) {
//        service.updateCarsById(id, dto);
//        return ResponseEntity.ok().build();
//    }

    @Operation(summary = "Update a user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CarsDTO> updateUser(
            @PathVariable @Parameter(description = "ID of the car") Long id,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Car data to update",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarsDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                     "year": 2018,
                                     "licensePlate": "PDV-0625",
                                     "model": "Audi",
                                     "color": "White"
                                     }""")))
            CarsDTO dto) {
        service.updateCarsById(id, dto);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Create a new car")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Car created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided")})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User data to update",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarsDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                     "year": 2018,
                                     "licensePlate": "PDV-0625",
                                     "model": "Audi",
                                     "color": "White"
                                     }""")))
            @Valid @RequestBody CarsDTO dto) {
        service.save(dto);
    }

    @Operation(summary = "Delete a car by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Car deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Car not found")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCars(@Valid @PathVariable @Parameter(description = "id of car to be searched") Long id) throws Exception {
        service.deleteCars(id);
    }

}

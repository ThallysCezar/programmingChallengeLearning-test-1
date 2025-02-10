package br.com.thallysprojects.pitang_desafio.controllers;

import br.com.thallysprojects.pitang_desafio.dtos.CarsDTO;
import br.com.thallysprojects.pitang_desafio.services.CarsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@AllArgsConstructor
public class CarsController {

    private final CarsService service;

    @Operation(summary = "Get all cars")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Cars",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarsDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "There not found cars",
                    content = @Content)})
    @GetMapping
    //Colocar paginação para dar uma melhorada
    public ResponseEntity<List<CarsDTO>> findAll() throws Exception {
        return ResponseEntity.ok().body(service.findAll());
    }

    @Operation(summary = "Get a cars by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Cars",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarsDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Car not found",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<CarsDTO> findCarById(@Valid @PathVariable
                                               @Parameter(description = "id of car to be searched") Long id) throws Exception {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @Operation(summary = "Update a car by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Updated the Car successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarsDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Car not found",
                    content = @Content)})
    @PutMapping("/{id}")
    public ResponseEntity<CarsDTO> updateCars(@Valid @PathVariable
                                              @Parameter(description = "id of user to be searched") Long id) {
        service.updateCarsById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create a new car")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Car created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarsDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input provided")})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Car to create", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarsDTO.class),
                            examples = @ExampleObject(value = "{ \"title\": \"New User\", \"author\": \"Author Name\" }")))
            @Valid @RequestBody CarsDTO dto) throws Exception {
        service.save(dto);
    }

    @Operation(summary = "Delete a car by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Car deleted successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarsDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Car not found",
                    content = @Content)})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCars(@Valid @PathVariable @Parameter(description = "id of car to be searched") Long id) throws Exception {
        service.deleteCars(id);
    }

}

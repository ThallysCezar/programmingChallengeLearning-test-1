package br.com.thallysprojects.pitang_desafio.controllers;

import br.com.thallysprojects.pitang_desafio.dtos.CarsDTO;
import br.com.thallysprojects.pitang_desafio.services.CarsService;
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

    @GetMapping
    public ResponseEntity<List<CarsDTO>> findAll() throws Exception {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarsDTO> findCarById(@Valid @PathVariable Long id) throws Exception {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarsDTO> updateCars(@Valid @PathVariable Long id) {
        service.updateCarsById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@Valid @RequestBody CarsDTO dto) throws Exception {
        service.save(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCars(@Valid @PathVariable Long id) throws Exception {
        service.deleteCars(id);
    }
    
}

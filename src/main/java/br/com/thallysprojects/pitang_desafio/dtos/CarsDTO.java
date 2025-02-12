package br.com.thallysprojects.pitang_desafio.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarsDTO {

    private Long id;
    private String years;
    private String licensePlate;
    private String model;
    private String color;
//    private Long userId;

}
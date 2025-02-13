package br.com.thallysprojects.pitang_desafio.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsersDTO {

    private String firstName;
    private String lastName;
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private String login;
    private String password;
    private String phone;

    private List<CarsDTO> cars = new ArrayList<>();

}
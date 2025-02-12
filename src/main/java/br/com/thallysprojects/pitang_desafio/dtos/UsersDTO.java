package br.com.thallysprojects.pitang_desafio.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsersDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private OffsetDateTime brithDay;
    private String login;
    private String password;
    private String phone;
    private List<CarsDTO> carsDTOList;

}
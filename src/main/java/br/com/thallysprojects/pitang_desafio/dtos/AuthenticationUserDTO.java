package br.com.thallysprojects.pitang_desafio.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationUserDTO {

    private String login;
    private String password;

}
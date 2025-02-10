package br.com.thallysprojects.pitang_desafio.dtos;

import lombok.Data;

@Data
public class AuthenticationDTO {

    private String login;
    private String password;

}
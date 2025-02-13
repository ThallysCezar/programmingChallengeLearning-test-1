package br.com.thallysprojects.pitang_desafio.dtos;

import br.com.thallysprojects.pitang_desafio.entities.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRegisterDTO {

    private String login;
    private String email;
    private String password;
    private UserRole userRole;

}

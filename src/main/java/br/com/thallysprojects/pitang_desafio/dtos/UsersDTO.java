package br.com.thallysprojects.pitang_desafio.dtos;

import br.com.thallysprojects.pitang_desafio.entities.Users;
import br.com.thallysprojects.pitang_desafio.entities.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.time.OffsetDateTime;

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
    private UserRole role;

    public UsersDTO(String login, String encryptedPassword, UserRole role) {
        this.login = login;
        this.password = encryptedPassword;
        this.role = role;
    }


    public UsersDTO(Users entity) {
        BeanUtils.copyProperties(entity, this);
    }
}

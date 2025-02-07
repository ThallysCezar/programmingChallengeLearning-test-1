package br.com.thallysprojects.pitang_desafio.dtos;

import br.com.thallysprojects.pitang_desafio.entities.Users;
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
    private String brithDay;
    private OffsetDateTime login;
    private String password;
    private String phone;


    public UsersDTO(Users entity) {
        BeanUtils.copyProperties(entity, this);
    }
}

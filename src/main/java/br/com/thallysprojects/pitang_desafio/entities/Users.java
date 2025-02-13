package br.com.thallysprojects.pitang_desafio.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tb_users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private String login;
    private String password;
    private String phone;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Cars> cars;

}
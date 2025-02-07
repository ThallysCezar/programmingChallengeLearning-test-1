package br.com.thallysprojects.pitang_desafio.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_cars")
@Getter
@Setter
public class Cars {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String years;
    private String licensePlate;
    private String model;
    private String color;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_users", referencedColumnName = "id")
    private Users users;

}

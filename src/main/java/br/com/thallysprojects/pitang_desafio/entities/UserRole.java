package br.com.thallysprojects.pitang_desafio.entities;

import lombok.Getter;

@Getter
public enum UserRole {

    ADMIN("ADMIN"),
    LOJISTA("LOJISTA"),
    CLIENTE("CLIENTE");

    private String tipo;

    UserRole(String tipo) {
        this.tipo = tipo;
    }



}
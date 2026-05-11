package com.entrevista.api_concessionaria.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PerfilFuncionario {
    ATENDENTE,
    ANALISTA, 
    GERENTE;

    @JsonCreator
    public static PerfilFuncionario fromString(String value) {
        return value == null ? null : PerfilFuncionario.valueOf(value.toUpperCase());
    }
}

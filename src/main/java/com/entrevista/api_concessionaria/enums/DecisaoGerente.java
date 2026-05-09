package com.entrevista.api_concessionaria.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum DecisaoGerente {    
    APROVADA,
    REPROVADA;

    @JsonCreator
    public static DecisaoGerente fromString(String value) {
        return value == null ? null : DecisaoGerente.valueOf(value.toUpperCase());
    }
}

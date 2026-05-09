package com.entrevista.api_concessionaria.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum StatusSolicitacao {
    ABERTA,
    EM_ANALISE,
    AGUARDANDO_APROVACAO,
    CONCLUIDA;

    @JsonCreator
    public static StatusSolicitacao fromString(String value) {
        return value == null ? null : StatusSolicitacao.valueOf(value.toUpperCase());
    }
}

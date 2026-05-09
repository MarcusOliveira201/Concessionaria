package com.entrevista.api_concessionaria.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoSolicitacao {
    RECLAMACAO,
    REVISAO_META;

    @JsonCreator
    public static TipoSolicitacao fromString(String value) {
        return value == null ? null : TipoSolicitacao.valueOf(value.toUpperCase());
    }
}

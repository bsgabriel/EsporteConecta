package com.ucs.esporteconecta.model.enums;

public enum DiaSemana {
    DOMINGO("domingo"),
    SEGUNDA_FEIRA("segunda-feira"),
    TERCA_FEIRA("terça-feira"),
    QUARTA_FEIRA("quarta-feira"),
    QUINTA_FEIRA("quinta-feira"),
    SEXTA_FEIRA("sexta-feira"),
    SABADO("sábado");

    private final String valor;

    DiaSemana(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return getValor();
    }
}

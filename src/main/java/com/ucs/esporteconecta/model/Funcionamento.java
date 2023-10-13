package com.ucs.esporteconecta.model;

import com.ucs.esporteconecta.model.enums.DiaSemana;
import jakarta.persistence.*;

@Entity
@Table(name = "funcionamentos")
public class Funcionamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Instalacao instalacao;

    @Enumerated(EnumType.STRING)
    private DiaSemana diaSemana;

    @OneToOne(cascade = CascadeType.ALL)
    private Horario horario;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instalacao getInstalacao() {
        return instalacao;
    }

    public void setInstalacao(Instalacao instalacao) {
        this.instalacao = instalacao;
    }

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    @Override
    public String toString() {
        return diaSemana.getValor() + ": " + horario.toString();
    }
}

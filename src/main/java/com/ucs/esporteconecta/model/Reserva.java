package com.ucs.esporteconecta.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate data;

    @ManyToOne
    private Horario horario;

    @ManyToOne
    private Instalacao instalacao;

    @ManyToOne
    private Esportista esportista;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public Instalacao getInstalacao() {
        return instalacao;
    }

    public void setInstalacao(Instalacao instalacao) {
        this.instalacao = instalacao;
    }

    public Esportista getEsportista() {
        return esportista;
    }

    public void setEsportista(Esportista esportista) {
        this.esportista = esportista;
    }
}

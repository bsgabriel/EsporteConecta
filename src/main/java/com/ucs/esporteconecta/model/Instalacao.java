package com.ucs.esporteconecta.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "instalacoes")
public class Instalacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer capacidadeMaxima;

    @Column
    private String descricao;

    @OneToMany(mappedBy = "instalacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> reservas;

    @OneToMany(mappedBy = "instalacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Funcionamento> funcionamentos;

    @OneToMany(mappedBy = "instalacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Avaliacao> avaliacoes;

    @ManyToOne
    private Instituicao instituicao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public void setCapacidadeMaxima(Integer capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Instituicao getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(Instituicao instituicao) {
        this.instituicao = instituicao;
    }

    public List<Reserva> getReservas() {
        if (reservas == null)
            reservas = new ArrayList<>();
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public List<Funcionamento> getFuncionamentos() {
        if (funcionamentos == null)
            funcionamentos = new ArrayList<>();
        return funcionamentos;
    }

    public void setFuncionamentos(List<Funcionamento> funcionamentos) {
        this.funcionamentos = funcionamentos;
    }

    public List<Avaliacao> getAvaliacoes() {
        if (avaliacoes == null)
            avaliacoes = new ArrayList<>();
        return avaliacoes;
    }

    public void setAvaliacoes(List<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }
}

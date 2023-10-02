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
    private String nome;

    @Column
    private String descricao;

    @Column
    private Integer capacidadeMaxima;

    @Column
    private Double valor;

    @Column
    private String bairro;

    @Column
    private String cidade;

    @Column
    private String estado;

    @OneToOne(cascade = CascadeType.ALL)
    private Modalidade modalidade;

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() { return descricao; }

    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Integer getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public void setCapacidadeMaxima(Integer capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public Double getValor() { return valor; }

    public void setValor(Double valor) { this.valor = valor;}

    public String getBairro() { return bairro; }

    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }

    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }

    public Modalidade getModalidade() {
        return modalidade;
    }

    public void setModalidade(Modalidade modalidade) {
        this.modalidade = modalidade;
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

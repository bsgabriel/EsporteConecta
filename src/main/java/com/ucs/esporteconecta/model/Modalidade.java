package com.ucs.esporteconecta.model;

import jakarta.persistence.*;

@Entity
@Table(name = "modalidades")
public class Modalidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nome;

    @Column
    private String urlImagem;

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

    public String getUrlImagem() {return urlImagem;}
    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }
    @Override
    public String toString() {
        return getId() + "-" + getNome();
    }
}

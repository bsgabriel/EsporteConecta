package com.ucs.esporteconecta.model;

import jakarta.persistence.*;

@Entity
@Table(name = "avaliacoes")
public class Avaliacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String comentario;

    @Column
    private int nota;

    @ManyToOne
    private Esportista esportista;

    @ManyToOne
    private Instalacao instalacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public Esportista getEsportista() {
        return esportista;
    }

    public void setEsportista(Esportista esportista) {
        this.esportista = esportista;
    }

    public Instalacao getInstalacao() {
        return instalacao;
    }

    public void setInstalacao(Instalacao instalacao) {
        this.instalacao = instalacao;
    }
}

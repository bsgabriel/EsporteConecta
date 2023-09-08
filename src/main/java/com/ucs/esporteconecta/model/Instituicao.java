package com.ucs.esporteconecta.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "instituicoes")
public class Instituicao extends Usuario {

    @Column
    private String cnpj;

    @Column
    private String razaoSocial;

    @Column
    private String nomeFantasia;

    @OneToMany(mappedBy = "instituicao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Instalacao> instalacoes;

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public List<Instalacao> getInstalacoes() {
        if (instalacoes == null)
            instalacoes = new ArrayList<>();
        return instalacoes;
    }

    public void setInstalacoes(List<Instalacao> instalacoes) {
        this.instalacoes = instalacoes;
    }
}

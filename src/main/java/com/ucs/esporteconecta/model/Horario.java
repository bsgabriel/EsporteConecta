package com.ucs.esporteconecta.model;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "horarios")
public class Horario {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private LocalTime inicio;

    @Column()
    private LocalTime fim;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalTime inicio) {
        this.inicio = inicio;
    }

    public LocalTime getFim() {
        return fim;
    }

    public void setFim(LocalTime fim) {
        this.fim = fim;
    }

    @Override
    public String toString() {
        return inicio + " - " + fim;
    }
}

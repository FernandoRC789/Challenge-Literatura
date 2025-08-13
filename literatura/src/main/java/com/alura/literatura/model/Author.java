package com.alura.literatura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "authors")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonAlias("name")
    @Column(length = 500)
    private String nombre;
    @JsonAlias("birth_year") private Integer birthYear;
    @JsonAlias("death_year") private Integer deathYear;

    @ManyToOne
    @JoinColumn(name = "libro_id") // referencia al libro al que pertenece
    @JsonBackReference
    private Libro libro;

    public Author(String nombre, Integer birthYear, Integer deathYear, Libro libro) {
        this.nombre = nombre;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
        this.libro = libro;
    }


}

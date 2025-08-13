package com.alura.literatura.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "idiomas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Idioma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false, unique = true)
    private String codigo;

    @JsonBackReference
    @ManyToMany(mappedBy = "idiomas")
    private List<Libro> libros = new ArrayList<>();

    // Constructor personalizado
    public Idioma(String codigo) {
        this.codigo = codigo;
    }
}


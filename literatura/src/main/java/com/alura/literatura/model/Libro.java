package com.alura.literatura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.alura.literatura.model.Author;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "libros")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonAlias("title")
    @Column(length = 1000)
    private String titulo;

    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonAlias("authors")
    @JsonBackReference
    private List<Author> autores = new ArrayList<>();

    @JsonAlias("download_count")
    private Integer numDescarga;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "libro_idioma",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "idioma_id")
    )
    private List<Idioma> idiomas =  new ArrayList<>();

    // Método para mapear JSON de Gutendex a Libro
    public static Libro fromMap(Map<String, Object> json) {
        Libro libro = new Libro();

        libro.setTitulo((String) json.get("title"));

        // Mapear autores
        List<Map<String, Object>> authorsJson = (List<Map<String, Object>>) json.get("authors");
        if (authorsJson != null) {
            for (Map<String, Object> a : authorsJson) {
                String nombre = (String) a.get("name");
                Integer birthYear = a.get("birth_year") != null ? (Integer) a.get("birth_year") : null;
                Integer deathYear = a.get("death_year") != null ? (Integer) a.get("death_year") : null;

                Author autor = new Author(nombre, birthYear, deathYear, libro);
                libro.getAutores().add(autor);
            }
        }

        libro.setNumDescarga((Integer) json.get("download_count"));

        return libro;
    }
}

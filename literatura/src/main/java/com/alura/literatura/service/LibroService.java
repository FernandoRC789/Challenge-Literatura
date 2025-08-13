package com.alura.literatura.service;

import com.alura.literatura.conexionAPI.ConsumoAPI;
import com.alura.literatura.model.Author;
import com.alura.literatura.model.Idioma;
import com.alura.literatura.model.Libro;
import com.alura.literatura.repository.IdiomaRepository;
import com.alura.literatura.repository.LibroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LibroService {

    private final LibroRepository libroRepository;
    private final ConsumoAPI consumoAPI;
    private final IdiomaRepository idiomaRepository;

    public LibroService(LibroRepository libroRepository,IdiomaRepository idiomaRepository, ConsumoAPI consumoAPI) {
        this.libroRepository = libroRepository;
        this.idiomaRepository = idiomaRepository;
        this.consumoAPI = consumoAPI;
    }


    // Guardar libros desde la API según título
    public void guardarLibrosPorTitulo(String tituloBusqueda) {
        List<Map<String, Object>> results = consumoAPI.buscarLibrosPorTitulo(tituloBusqueda);

        for (Map<String, Object> r : results) {
            Libro libro = Libro.fromMap(r);

            // ...dentro del for que recorre cada resultado del API:
            List<String> idiomasJson = (List<String>) r.get("languages");
            if (idiomasJson != null) {
                for (String cod : idiomasJson) {
                    // Busca el idioma en la base de datos por su código
                    Idioma idioma = idiomaRepository.findByCodigo(cod)
                            .orElseGet(() -> {
                                // Si no existe, lo crea y lo guarda
                                Idioma nuevoIdioma = new Idioma(cod);
                                return idiomaRepository.save(nuevoIdioma);
                            });

                    // Lo añade al libro
                    libro.getIdiomas().add(idioma);
                }
            }
            boolean yaExiste = libroRepository.findByTituloIgnoreCase(libro.getTitulo()).isPresent();

            if (!yaExiste) {
                libroRepository.save(libro);
            }
        }
    }

    // Listar todos los libros
    public List<Libro> listarTodos() {
        return libroRepository.findAll();
    }

    // Listar todos los autores únicos por nombre
    public List<String> listarAutores() {
        return libroRepository.findAll()
                .stream()
                .flatMap(libro -> libro.getAutores().stream())
                .map(Author::getNombre)
                .distinct()
                .collect(Collectors.toList());
    }

    // Listar autores vivos en un año determinado
    public List<String> listarAutoresVivos(int anio) {
        return libroRepository.findAll()
                .stream()
                .flatMap(libro -> libro.getAutores().stream())
                .filter(autor -> (autor.getBirthYear() == null || autor.getBirthYear() <= anio) &&
                        (autor.getDeathYear() == null || autor.getDeathYear() >= anio))
                .map(Author::getNombre)
                .distinct()
                .collect(Collectors.toList());
    }

    // Listar libros por idioma (ej: "en", "es", "fr")
    public List<Libro> listarLibrosPorIdioma(String idioma) {
        return libroRepository.findAll()
                .stream()
                .filter(libro -> libro.getIdiomas() != null &&
                        libro.getIdiomas().stream()
                                .anyMatch(i -> i.getCodigo().equalsIgnoreCase(idioma)))
                .collect(Collectors.toList());
    }
}

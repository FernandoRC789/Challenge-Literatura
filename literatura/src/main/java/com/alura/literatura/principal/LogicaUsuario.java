package com.alura.literatura.principal;

import com.alura.literatura.conexionAPI.ConsumoAPI;
import com.alura.literatura.model.Idioma;
import com.alura.literatura.model.Libro;
import com.alura.literatura.service.LibroService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class LogicaUsuario {
    private final String URL_BASE = "https://gutendex.com/books?search=";
    private Scanner teclado = new Scanner(System.in);
    private final LibroService libroService;
    private ConsumoAPI api = new ConsumoAPI();


    public LogicaUsuario(LibroService libroService) {
        this.libroService = libroService;
    }

    public void menu(){
        var opcion = -1;
        while(opcion !=0){
            var menu = """
                    Elija la opción que desea consultar:
                    1.- Buscar Libro por Titulo.
                    2.- Listar Libros Registrados
                    3.- Listar Autores Registrados
                    4.- Listar Autores vivos en  un determinado año
                    5.- Listar Libros por Idioma
                    6.- Listar TODOS los Libros

                    0.- SALIR
                    """;
            System.out.println(menu);
            try {
                opcion = Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número válido.");
                continue;
            }

            switch (opcion){
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivos();
                    break;
                case 5:
                    listarLibrosPorIdiomas();
                    break;
                case 6:
                    mostrarTodosLosLibros();
                    break;
                case 0:
                    System.out.println("////Cerrando Aplicación...////");
                    break;
                default:
                    System.out.println("Opción Invalida...");
            }
        }
    }

    private String nombresAutores(Libro libro) {
        return libro.getAutores().stream()
                .map(a -> a.getNombre())
                .reduce((a1, a2) -> a1 + ", " + a2)
                .orElse("Sin autores");
    }


    private void buscarLibroPorTitulo() {
        System.out.println("Ingrese el título a buscar:");
        String titulo = teclado.nextLine();
        titulo= titulo.replace(" ","+");
        libroService.guardarLibrosPorTitulo(titulo);
        System.out.println("Libros guardados en la base de datos.");
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroService.listarTodos();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
            return;
        }

        for (Libro libro : libros) {
            System.out.println(libro.getTitulo() + " - " + nombresAutores(libro));
        }
    }

    private void listarAutoresRegistrados() {
        List<String> autores = libroService.listarAutores();
        for (String autor : autores) {
            System.out.println(autor);
        }
    }

    private void listarAutoresVivos() {
        System.out.print("Ingrese el año: ");
        int anio;

        try {
            anio = Integer.parseInt(teclado.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Año inválido. Intente de nuevo.");
            return;
        }

        List<String> autoresVivos = libroService.listarAutoresVivos(anio);

        if (autoresVivos.isEmpty()) {
            System.out.println("❌ No se encontraron autores vivos en el año " + anio + ".");
        } else {
            System.out.println("\n📜 Autores vivos en el año " + anio + ":");
            System.out.println("----------------------------------------");
            autoresVivos.forEach(autor -> System.out.println("✔ " + autor));
        }

        System.out.println(); // línea en blanco al final
    }


    private void listarLibrosPorIdiomas() {
        System.out.print("Ingrese el idioma (ej: en, es, fr): ");
        String idioma = teclado.nextLine().trim().toLowerCase();

        List<Libro> libros = libroService.listarLibrosPorIdioma(idioma);

        if (libros.isEmpty()) {
            System.out.println("❌ No se encontraron libros en el idioma '" + idioma + "'.");
            return;
        }

        System.out.println("\n📚 Libros en idioma '" + idioma + "':");
        System.out.println("-----------------------------------------------------");

        for (Libro libro : libros) {
            System.out.println("📖 Título: " + libro.getTitulo());
            System.out.println("👨‍💼 Autor(es): " + nombresAutores(libro));
            System.out.println("⬇️  Descargas: " + libro.getNumDescarga());
            System.out.println("-----------------------------------------------------");
        }

        System.out.println(); // Línea en blanco para mejorar separación visual
    }


    private void mostrarTodosLosLibros() {
        List<Libro> libros = libroService.listarTodos();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
            return;
        }

        System.out.println("📚 Libros registrados en la base de datos:");
        System.out.println("-------------------------------------------");

        for (Libro libro : libros) {
            System.out.println("Título       : " + libro.getTitulo());
            System.out.println("Autores      : " + nombresAutores(libro));
            System.out.println("Descargas    : " + libro.getNumDescarga());

            String idiomas = libro.getIdiomas().stream()
                    .map(Idioma::getCodigo)
                    .collect(Collectors.joining(", "));
            System.out.println("Idiomas      : " + idiomas);

            System.out.println("-------------------------------------------");
        }
    }


}

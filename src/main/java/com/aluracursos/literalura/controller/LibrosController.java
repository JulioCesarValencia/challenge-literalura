package com.aluracursos.literalura.controller;


import com.aluracursos.literalura.consumoapi.Autor;
import com.aluracursos.literalura.consumoapi.ClienteApiLibros;
import com.aluracursos.literalura.consumoapi.DatosRespuesta;
import com.aluracursos.literalura.consumoapi.Libro;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class LibrosController implements CommandLineRunner {
    private final Scanner teclado = new Scanner(System.in);
    private final ClienteApiLibros clienteApiLibros = new ClienteApiLibros(new RestTemplate());
    @Override
    public void run(String... args) {
        mostrarMenu();
    }

    private void mostrarMenu() {
        int opcion = -1;

        while (opcion != 0) {

            System.out.println("""
                    ********************
                    1 - Buscar libro por titulo
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un anio
                    5 - Listar libros por idioma
                    0 - Salir
                    ********************
                    """);
            opcion = teclado.nextInt();
            //aqui limpio
            teclado.nextLine();

            switch (opcion) {
                case 1 -> buscarLibroPorTitulo();
                case 2 -> listarLibros();
                case 3 -> listarAutores();
                case 4 -> listarAutoresVivosPorAnio();
                case 5 -> listarLibrosPorIdioma();
                case 0 -> System.out.println("Bye");
                default -> System.out.println("Opcion invalida");
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Ingresa el nombre del libro que deseas buscar:");
        String titulo = teclado.nextLine();

        //evito errores de acento
        String query = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
        // Llamada a la API
        DatosRespuesta datos = clienteApiLibros.obtenerDatos("?search=" + query);

        if (datos.results().isEmpty()) {
            System.out.println("No se encontraron libros con ese título.");
            return;
        }

        // Mapea Autor -> Libros
        Map<String, List<String>> librosPorAutor = new HashMap<>();

        for (Libro libro : datos.results()) {
            if (libro.titulo().toLowerCase().contains(titulo.toLowerCase())) {
                for (Autor autor : libro.autores()) {
                    librosPorAutor
                            //busco la "clave"(key) y si no existe,ejecuta la funcion "k -> .."para crea un "valor inicial"(vacio)
                            .computeIfAbsent(autor.nombre(), k -> new ArrayList<>())
                            .add(libro.titulo());
                }
            }
        }

        // muestro los resultados
        librosPorAutor.forEach((autor, libros) -> {
            System.out.println("\nAutor: " + autor);
            System.out.println("Libros:");
            libros.forEach(libro -> System.out.println(" - " + libro));
        });

    }

    private void listarLibros() {
        System.out.println("2");
    }

    private void listarAutores() {
        System.out.println("3");
    }

    private void listarAutoresVivosPorAnio() {
        System.out.println("4");
    }

    private void listarLibrosPorIdioma() {
        System.out.println("5");
    }
}

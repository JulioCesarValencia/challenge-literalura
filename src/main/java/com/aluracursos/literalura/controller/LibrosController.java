package com.aluracursos.literalura.controller;


import com.aluracursos.literalura.consumoapi.Autor;
import com.aluracursos.literalura.consumoapi.ClienteApiLibros;
import com.aluracursos.literalura.consumoapi.DatosRespuesta;
import com.aluracursos.literalura.consumoapi.Libro;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibrosController {
    public static void main(String[] args) {

        RestTemplate restTemplate = new RestTemplate();
        ClienteApiLibros clienteApiLibros = new ClienteApiLibros(restTemplate);

        // Llamada a la API
        DatosRespuesta datos = clienteApiLibros.obtenerDatos("?search=java");

        // Mapea Autor -> Libros
        Map<String, List<String>> librosPorAutor = new HashMap<>();

        for (Libro libro : datos.results()) {
            for (Autor autor : libro.autores()) {
                librosPorAutor
                        .computeIfAbsent(autor.nombre(), k -> new ArrayList<>())
                        .add(libro.titulo());
            }
        }

        // muestro los resultados
        librosPorAutor.forEach((autor, libros) -> {
            System.out.println("\nAutor: " + autor);
            System.out.println("Libros:");
            libros.forEach(libro -> System.out.println(" - " + libro));
        });
    }
}

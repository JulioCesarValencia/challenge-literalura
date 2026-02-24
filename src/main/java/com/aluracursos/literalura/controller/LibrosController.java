package com.aluracursos.literalura.controller;


import com.aluracursos.literalura.consumoapi.Autor;
import com.aluracursos.literalura.consumoapi.ClienteApiLibros;
import com.aluracursos.literalura.consumoapi.DatosRespuesta;
import com.aluracursos.literalura.consumoapi.Libro;
import com.aluracursos.literalura.model.AutorEntity;
import com.aluracursos.literalura.model.LibroEntity;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import jakarta.transaction.Transactional;
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

    private final AutorRepository autorRepository;
    private final LibroRepository libroRepository;

    public LibrosController(AutorRepository autorRepository, LibroRepository libroRepository) {
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
    }

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
                case 0 -> System.out.println("Gracias por tu visita!");
                default -> System.out.println("Opcion invalida");
            }
        }
    }

    @Transactional
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


        for (Libro libroApi : datos.results()) {
            List<AutorEntity> autoresEntity = new ArrayList<>();

            for (Autor autorApi : libroApi.autores()) {
                AutorEntity autorEntity = autorRepository.findByNombre(autorApi.nombre())
                .orElseGet(() -> {
                    AutorEntity a = new AutorEntity();
                    a.setNombre(autorApi.nombre());
                    a.setFechaNacimiento(autorApi.fecha_nacimiento() != null
                            ? Integer.valueOf(autorApi.fecha_nacimiento()) : null);
                    a.setFechaFallecimiento(autorApi.fecha_fallecimiento() != null
                            ? Integer.valueOf(autorApi.fecha_fallecimiento()) : null);
                    return autorRepository.save(a); // save nuevo autor
                });
                autoresEntity.add(autorEntity);
                }


                //verifico dupliado
                Optional<LibroEntity> libroExistenteOpt = libroRepository.findByTitulo(libroApi.titulo());
                if (libroExistenteOpt.isPresent()) {
                    // si existe, podríamos actualizar descargas/idiomas y relaciones si hace falta
                    LibroEntity existente = libroExistenteOpt.get();
                    existente.setDescargas(libroApi.descargas());
                    // actualizar idiomas (simplificamos guardando el primero como cadena)
                    existente.setIdioma(String.join(",", libroApi.idiomas()));
                    // asegurar relaciones autor-libro
                    List<AutorEntity> actuales = existente.getAutores() == null ? new ArrayList<>() : existente.getAutores();
                    for (AutorEntity a : autoresEntity) {
                        if (actuales.stream().noneMatch(x -> x.getId().equals(a.getId()))) {
                            actuales.add(a);
                        }
                    }
                    existente.setAutores(actuales);
                    libroRepository.save(existente);
                } else {
                    // crear nuevo libroEntity
                    LibroEntity nuevo = new LibroEntity();
                    nuevo.setTitulo(libroApi.titulo());
                    nuevo.setDescargas(libroApi.descargas());
                    nuevo.setIdioma(String.join(",", libroApi.idiomas())); // guardamos los idiomas como CSV
                    nuevo.setAutores(autoresEntity);
                    libroRepository.save(nuevo);
                }
//            System.out.println("Título: " + libro.titulo());
//            System.out.println("Autor(es): " +
//                    libro.autores().stream()
//                            .map(Autor::nombre)
//                            .toList());
//            System.out.println("Idiomas: " + libro.idiomas());
//            System.out.println("Descargas: " + libro.descargas());
//            System.out.println("********************");
            }

        System.out.println("Guardado(s) en la base de datos (si no existían). Mostrando resultados:");
        //datos.results().forEach(libro -> System.out.println(libro)); // usa toString() del record Libro

        int count = 0;
        for (Libro libro : datos.results()) {
            if(count >= 4) break; // solo los primeros 4 libros

            // Primer autor
            Autor autor = libro.autores().isEmpty() ? null : libro.autores().get(0);
            String nombreAutor = autor == null ? "Desconocido" : autor.nombre();

            // Primer idioma
            String idioma = libro.idiomas().isEmpty() ? "-" : libro.idiomas().get(0);

            System.out.println("Título: " + libro.titulo());
            System.out.println("Autor: " + nombreAutor);
            System.out.println("Idioma: " + idioma);
            System.out.println("Numero de Descargas: " + libro.descargas());
            System.out.println("------------------------");

            count++;
        }

    }

    private void listarLibros() {
        List<LibroEntity> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos.");
            return;
        }

        for (LibroEntity le : libros) {
            System.out.println("Título: " + le.getTitulo());
            System.out.println("Autores: " + (le.getAutores() == null ? List.of() :
                    le.getAutores().stream().map(AutorEntity::getNombre).toList()));
            System.out.println("Idiomas: " + le.getIdioma());
            System.out.println("Descargas: " + le.getDescargas());
            System.out.println("----------------------");
        }
    }

    private void listarAutores() {
        List<AutorEntity> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados en la base de datos.");
            return;
        }

        for (AutorEntity a : autores) {
            System.out.println("Autor: " + a.getNombre());
            System.out.println("Fecha de Nacimiento: " + a.getFechaNacimiento());
            System.out.println("Fecha de Fallecimiento: " + a.getFechaFallecimiento());
            // títulos de los libros
            List<String> titulos = a.getLibros() == null ? List.of() :
                    a.getLibros().stream().map(LibroEntity::getTitulo).toList();
            System.out.println("Libros: " + titulos);
            System.out.println("----------------------");
        }
    }



    private void listarAutoresVivosPorAnio() {
        System.out.println("Ingrese el año para filtrar autores vivos en ese año:");
        int anio = Integer.parseInt(teclado.nextLine());

        List<AutorEntity> autores = autorRepository.findAll();
        List<AutorEntity> vivos = autores.stream()
                .filter(a -> {
                    Integer birth = a.getFechaNacimiento();
                    Integer death = a.getFechaFallecimiento();

                    if (birth == null) return false;
                    if (death == null) return birth <= anio;
                    return birth <= anio && death >= anio;
                })
                .toList();

        if (vivos.isEmpty()) {
            System.out.println("No hay autores vivos en ese año en la BD.");
            return;
        }
        System.out.println("Autores vivos en " + anio + ":");
        //vivos.forEach(a -> System.out.println(a.getNombre() + " (" + a.getFechaNacimiento() + " - " + a.getFechaFallecimiento() + ")"));


        for (AutorEntity a : vivos) {
            System.out.println("Autor: " + a.getNombre());
            System.out.println("Fecha de Nacimiento: " + a.getFechaNacimiento());
            System.out.println("Fecha de Fallecimiento: " + (a.getFechaFallecimiento() == null ? "Vivo" : a.getFechaFallecimiento()));
            List<String> titulos = a.getLibros() == null ? List.of() :
                    a.getLibros().stream().map(LibroEntity::getTitulo).toList();
            System.out.println("Libros: " + titulos);
            System.out.println("----------------------");
        }

    }

    private void listarLibrosPorIdioma() {
        System.out.println("Ingresa el idioma: ");
        System.out.println("es - Español");
        System.out.println("en - Inglés");
        System.out.println("fr - Francés");
        System.out.println("pt - Portugués");

        String idioma = teclado.nextLine().toLowerCase();

        List<LibroEntity> libros = libroRepository.buscarLibrosPorIdioma(idioma);
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos para el idioma '" + idioma + "'.");
            return;
        }

        System.out.println("Libros en idioma '" + idioma + "':");

        for (LibroEntity le : libros) {
            System.out.println("Título: " + le.getTitulo());
            System.out.println("Autor(es): " + (le.getAutores() == null ? List.of() :
                    le.getAutores().stream().map(AutorEntity::getNombre).toList()));
            System.out.println("Idioma: " + le.getIdioma());
            System.out.println("Descargas: " + le.getDescargas());
            System.out.println("----------------------");
        }


//
//        long total = libroRepository.contarLibrosPorIdioma(idioma);
//
//        System.out.println("Total de libros en '" + idioma + "': " + total);
//
//        libroRepository.buscarLibrosPorIdioma(idioma)
//                .stream()
//                .map(LibroEntity::getTitulo)
//                .forEach(t -> System.out.println(" - " + t));

    }
}

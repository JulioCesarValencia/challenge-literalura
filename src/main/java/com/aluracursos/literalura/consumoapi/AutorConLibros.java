package com.aluracursos.literalura.consumoapi;

import java.util.List;

public record AutorConLibros(
        String nombre, Integer fecha_nacimiento, Integer fecha_fallecimiento, List<String> libros
) {
}

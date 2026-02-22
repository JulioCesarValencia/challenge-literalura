package com.aluracursos.literalura.consumoapi;

public record Libro(int id,
                    String title,
                    List<Autor> authors,
                    List<String> languages,
                    int download_count
) {
}

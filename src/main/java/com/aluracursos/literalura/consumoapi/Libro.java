package com.aluracursos.literalura.consumoapi;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public record Libro(int id,
                    @JsonAlias("title") String titulo,
                    @JsonAlias("authors") List<Autor> autores,
                    @JsonAlias("languages") List<String> idiomas,
                    @JsonAlias("download_count") int descargas
) {

    @Override
    public String toString() {
        return "Libro{" +
                "titulo='" + titulo + '\'' +
                ", autores=" + autores +
                ", idiomas=" + idiomas +
                ", descargas=" + descargas +
                '}';
    }
}



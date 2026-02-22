package com.aluracursos.literalura.consumoapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosRespuesta(
        int count,
        List<Libro> results
) {
}

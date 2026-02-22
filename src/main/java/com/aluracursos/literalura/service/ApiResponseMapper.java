package com.aluracursos.literalura.service;

import com.aluracursos.literalura.consumoapi.DatosRespuesta;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ApiResponseMapper {
    private final ObjectMapper mapper = new ObjectMapper();

    public DatosRespuesta parsear(String json) {
        try {
            return mapper.readValue(json, DatosRespuesta.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al parsear JSON", e);
        }
    }
}

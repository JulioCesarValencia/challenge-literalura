package com.aluracursos.literalura.consumoapi;

import com.aluracursos.literalura.service.ApiResponseMapper;
import org.springframework.web.client.RestTemplate;


public class ClienteApiLibros {

    private final RestTemplate restTemplate;
    private static final String URL_BASE = "https://gutendex.com/books";
        private final ApiResponseMapper apiResponseMapper;

    public ClienteApiLibros(RestTemplate restTemplate) {
        // Inicializamos el cliente HTTP una sola vez
        this.restTemplate = restTemplate;
        this.apiResponseMapper = new ApiResponseMapper();
    }

    public DatosRespuesta obtenerDatos(String url) {
        try {
            String urlCompleta = URL_BASE + url;
            //obtengo json
            String json = restTemplate.getForObject(urlCompleta, String.class);
            return apiResponseMapper.parsear(json);
        }  catch (Exception e){
            String mensajeError = "Error al obtener datos de la API: " + e.getMessage();
            System.err.println(mensajeError);
            throw new RuntimeException("No se pudo conectar con la API", e);
        }
    }

}


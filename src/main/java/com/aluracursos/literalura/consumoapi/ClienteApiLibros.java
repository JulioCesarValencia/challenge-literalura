package com.aluracursos.literalura.consumoapi;

import org.springframework.web.client.RestTemplate;


public class ClienteApiLibros {

    private final RestTemplate restTemplate;
    private static final String URL_BASE = "https://gutendex.com/books";

    public ClienteApiLibros(RestTemplate restTemplate) {
        // Inicializamos el cliente HTTP una sola vez
        this.restTemplate = restTemplate;
    }

    public String obtenerDatos(String url) {
        try {
            String urlCompleta = URL_BASE + url;
            return restTemplate.getForObject(urlCompleta, String.class);
        }  catch (Exception e){
            String mensajeError = "Error al obtener datos de la API: " + e.getMessage();
            System.err.println(mensajeError);
            throw new RuntimeException("No se pudo conectar con la API", e);
        }
    }

}


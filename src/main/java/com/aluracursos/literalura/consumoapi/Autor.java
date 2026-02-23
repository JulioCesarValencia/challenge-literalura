package com.aluracursos.literalura.consumoapi;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public record Autor(
        @JsonAlias({"name","Name"}) String nombre,
        @JsonAlias({"birth_year","birth_date"}) String fecha_nacimiento,
        @JsonAlias({"death_year","death_date"}) String fecha_fallecimiento
){}









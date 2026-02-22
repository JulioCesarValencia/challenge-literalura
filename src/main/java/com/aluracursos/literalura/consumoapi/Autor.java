package com.aluracursos.literalura.consumoapi;

public record Autor(String name,
                    String birth_date,
                    String death_date,
                    List<String> books) {
}

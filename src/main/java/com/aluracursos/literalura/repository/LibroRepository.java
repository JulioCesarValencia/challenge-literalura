package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.LibroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<LibroEntity, Long> {
    Optional<LibroEntity> findByTitulo(String titulo);

    @Query("SELECT COUNT(l) FROM LibroEntity l WHERE LOWER(l.idioma) = LOWER(:idioma)")
    long contarLibrosPorIdioma(@Param("idioma") String idioma);

    @Query("SELECT l FROM LibroEntity l WHERE LOWER(l.idioma) = LOWER(:idioma)")
    List<LibroEntity> buscarLibrosPorIdioma(@Param("idioma") String idioma);

}

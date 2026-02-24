package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.AutorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<AutorEntity, Long> {
    Optional<AutorEntity> findByNombre(String nombre);

    @Query("""
SELECT a FROM AutorEntity a
WHERE a.fechaNacimiento <= :anio
AND (a.fechaFallecimiento IS NULL OR a.fechaFallecimiento >= :anio)
""")
    List<AutorEntity> buscarAutoresVivosEnAnio(@Param("anio") Integer anio);
}

package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.LibroEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepository extends JpaRepository<LibroEntity, Long> {
}

package com.aluracursos.literalura.model;

import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "libros")
public class LibroEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(length = 1000)
        private String titulo;
        @Column(length = 1000)
        private String idioma;
        private Integer descargas;

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "libro_autor",
                joinColumns = @JoinColumn(name = "libro_id"),
                inverseJoinColumns = @JoinColumn(name = "autor_id")
        )
        private List<AutorEntity> autores;

        public LibroEntity() {
        }

        public LibroEntity(String titulo, String idioma, Integer descargas, List<AutorEntity> autores) {
                this.titulo = titulo;
                this.idioma = idioma;
                this.descargas = descargas;
                this.autores = autores;
        }


        // getters y setters

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getTitulo() {
                return titulo;
        }

        public void setTitulo(String titulo) {
                this.titulo = titulo;
        }

        public String getIdioma() {
                return idioma;
        }

        public void setIdioma(String idioma) {
                this.idioma = idioma;
        }

        public Integer getDescargas() {
                return descargas;
        }

        public void setDescargas(Integer descargas) {
                this.descargas = descargas;
        }

        public List<AutorEntity> getAutores() {
                return autores;
        }

        public void setAutores(List<AutorEntity> autores) {
                this.autores = autores;
        }
}

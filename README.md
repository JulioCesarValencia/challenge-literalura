# Literalura — Cliente y persistencia de libros 📚

Literalura es una aplicación de consola construida con **Spring Boot** que permite:

- Buscar libros desde la API pública **Gutendex**.  
- Guardar libros y autores en tu **base de datos PostgreSQL** local.  
- Consultar y listar libros y autores en múltiples formas (por título, por idioma, autores vivos en un año, etc.)  
- Mostrar toda la información en consola de forma clara y vertical.  

---

## Características

1. **Buscar libro por título**: consulta la API, guarda en la base de datos si no existía y muestra título, autor(es), idioma y descargas en formato vertical.
2. **Listar libros registrados**: listado vertical desde la base de datos.
3. **Listar autores registrados**: listado vertical con fecha de nacimiento/fallecimiento y los libros asociados.
4. **Listar autores vivos en un año**: filtra autores según año, mostrando la información vertical.
5. **Listar libros por idioma**: permite seleccionar un idioma de forma vertical (`es`, `en`, `fr`, `pt`) y listar los libros correspondientes.
6. **To 10 mas desacrgados**: lista los 10 libros con mayor número de descargas almacenados en la base de datos, mostrando título, autor(es), idioma y cantidad de descargas en formato vertical y ordenados de mayor a menor.
7. 
---

## Tecnologías

- :contentReference[oaicite:0]{index=0}  
- :contentReference[oaicite:1]{index=1}  
- Maven  
- Java 17+  
- JPA / Hibernate  
- RestTemplate para consumo de la API :contentReference[oaicite:2]{index=2}  

---

## Requisitos

- Java 17+  
- Maven  
- PostgreSQL (local o remoto)  
- Git (opcional)  

---

## Configuración de la base de datos

Ejemplo usando `psql`:

```sql
CREATE DATABASE literalura_db;
CREATE USER literalura_user WITH ENCRYPTED PASSWORD 'tu_password_segura';
GRANT ALL PRIVILEGES ON DATABASE literalura_db TO literalura_user;

## Configuracion:
<> application.properties

spring.datasource.url=jdbc:postgresql://localhost:5432/literalura_db
spring.datasource.username=literalura_user
spring.datasource.password=tu_password_segura

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

## Ejecucion:
Maven:

spring.datasource.url=jdbc:postgresql://localhost:5432/literalura_db
spring.datasource.username=literalura_user
spring.datasource.password=tu_password_segura

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

o ejecutando el JAR:

java -jar target/literalura-0.0.1-SNAPSHOT.jar


## Uso del menú
Al iniciar la app se verá:

********************
1 - Buscar libro por titulo
2 - Listar libros registrados
3 - Listar autores registrados
4 - Listar autores vivos en determinado anio
5 - Listar libros por idioma
6 - Top 10 mas descargados
0 - Salir
********************

## Formato de salida (vertical y clara):

Libros:
Título: Don Quijote
Autor: Miguel de Cervantes Saavedra
Idioma: es
Descargas: 10926
------------------------

Autores:
Autor: Mary Shelley
Nacimiento: 1797
Fallecimiento: 1851
Libros: [Frankenstein, ...]
------------------------

## Troubleshooting:
- Datasource no configurado: revisa spring.datasource.url
- Duplicados: la app evita duplicar autores por nombre

## Licencia:
MIT License — libre para usar, modificar y compartir.

## Créditos:
Construido con Spring Boot
API de libros: Gutendex
Persistencia en PostgreSQL


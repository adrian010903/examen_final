# Sistema de Gestion de Caballeriza

Proyecto de examen para administrar caballos, personal, alimentacion, historial medico, reservas, alertas y usuarios.

## Tecnologias

- Backend: Spring Boot, Spring Security, JWT, Spring Data JPA
- Base de datos: MariaDB/MySQL
- Frontend: React + Vite
- Pruebas: JUnit, H2, Vitest, Testing Library
- Documentacion API: Swagger/OpenAPI

## Requisitos

- Java 17
- Maven Wrapper incluido
- Node.js 18 o superior
- MariaDB/MySQL en `localhost:3306`

## Base de datos

Crear la base:

```sql
CREATE DATABASE IF NOT EXISTS caballeriza_db;
```

La configuracion por defecto usa variables de entorno con valores basicos:

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/caballeriza_db
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:}
server.port=8080
```

Si tu MariaDB usa otro usuario o contrasena, sobrescribe con variables de entorno:

```bash
DB_USERNAME=usuario
DB_PASSWORD=contrasena
DB_URL=jdbc:mariadb://localhost:3306/caballeriza_db
```

Ejemplo Windows PowerShell:

```powershell
$env:DB_USERNAME="tu_usuario"
$env:DB_PASSWORD="tu_contrasena"
.\mvnw.cmd spring-boot:run
```

Hibernate crea/actualiza las tablas automaticamente con:

```properties
spring.jpa.hibernate.ddl-auto=update
```

## Ejecutar backend

```bash
.\mvnw.cmd spring-boot:run
```

API:

```text
http://localhost:8080
```

Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

## Ejecutar frontend

```bash
cd frontend
npm.cmd install
npm.cmd run dev
```

Frontend:

```text
http://127.0.0.1:5173
```

El frontend usa proxy hacia el backend en `localhost:8080`.

## Usuarios

Desde la pantalla de login se puede registrar un usuario inicial. Roles disponibles:

- `ADMINISTRADOR`
- `CUIDADOR`
- `VETERINARIO`
- `CLIENTE`

Ejemplo:

```text
Nombre: Admin
Correo: admin@caballeriza.com
Contrasena: 123456
Rol: ADMINISTRADOR
```

## Modulos principales

- Caballos: CRUD, foto, raza, peso, identificador.
- Historial medico: vacunas, tratamientos, alergias, observaciones y proximo control.
- Personal: CRUD de empleados y roles operativos.
- Turnos y tareas: asignacion por empleado y cambio de estado pendiente/completada.
- Reservas: citas veterinarias, montas, paseos y entrenamientos con crear/cancelar.
- Alimentacion: planes por caballo y suministros.
- Inventario: insumos con alerta de stock bajo.
- Alertas: stock bajo, vacunaciones proximas y tratamientos vencidos/proximos.
- Seguridad: registro, login, JWT, roles y permisos por endpoint.

## Permisos principales

- Administrador: acceso general.
- Veterinario: caballos, historial medico y alertas.
- Cuidador: caballos, tareas, alimentacion, inventario y alertas.
- Cliente: reservas.

## Pruebas

Backend:

```bash
.\mvnw.cmd test
```

Frontend:

```bash
cd frontend
npm.cmd test
```

Build frontend:

```bash
cd frontend
npm.cmd run build
```

## Endpoints principales

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET/POST/PUT/DELETE /api/caballos`
- `GET/POST/PUT/DELETE /api/empleados`
- `GET/POST/PUT/DELETE /api/historial-medico`
- `GET/POST/PUT/DELETE /api/tareas`
- `GET/POST/PUT/DELETE /api/turnos`
- `GET/POST/PUT/DELETE /api/planes-alimentacion`
- `GET/POST/PUT/DELETE /api/suministros`
- `GET/POST/PUT/DELETE /api/inventario`
- `GET/POST/PUT/DELETE /api/reservas`
- `PATCH /api/reservas/{id}/cancelar`
- `GET /api/alertas`

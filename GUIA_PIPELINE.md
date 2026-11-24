# Guía de instalación, configuración y ejecución del Pipeline

Esta guía permite descargar el proyecto, ejecutar las pruebas unitarias en local y ejecutar el pipeline de CI/CD en GitHub Actions.

## 1. Descripción del proyecto

API RESTful para gestionar farmacias y compras de medicamentos.

| Elemento | Tecnología |
|---|---|
| Lenguaje | Java 8 |
| Build | Maven (empaqueta un `.war`) |
| API REST | Jersey 2.27 (JAX-RS) |
| Persistencia | JPA con Hibernate 5.4 |
| Servidor de aplicaciones | Apache Tomcat 9 |
| Pruebas unitarias | JUnit 5 (Jupiter) con Maven Surefire 2.22.2 |
| Pipeline | GitHub Actions (`.github/workflows/ci.yml`) |
| Contenedores | Docker (imagen Tomcat 9 + Java 8) |

Las pruebas unitarias están en `src/test/java` y validan la lógica de las entidades (`hasValidAttributes()` de Pharmacy, Product, Department, PharmacyProduct, Customer, Manager y Administrator). **No requieren base de datos.**

## 2. Requisitos del ambiente local

| Herramienta | Versión | Para qué |
|---|---|---|
| Git | cualquiera reciente | Descargar el repositorio |
| JDK | 8 (recomendado). También compila con 11, 17 o 21 | Compilar y probar |
| Maven | 3.6 o superior | Compilar, probar y empaquetar |
| Docker | opcional | Construir y ejecutar la imagen en local |

Verificar la instalación:

```bash
git --version
java -version
mvn -version
```

### Instalación en macOS (Homebrew)

```bash
brew install git maven
brew install --cask temurin@8     # JDK 8 (opcional si ya hay otro JDK)
```

### Instalación en Ubuntu/Debian

```bash
sudo apt update
sudo apt install -y git maven openjdk-8-jdk
```

### Instalación en Windows

Instalar Git (https://git-scm.com), un JDK 8 como Temurin (https://adoptium.net) y Maven (https://maven.apache.org/download.cgi). Agregar `JAVA_HOME` y el directorio `bin` de Maven al `PATH`.

## 3. Descargar el proyecto

```bash
git clone https://github.com/Kevin-Rengifo0124/farmacia-pipeline.git
cd farmacia-pipeline
```

## 4. Ejecutar las pruebas en local

```bash
mvn test
```

Resultado esperado:

```
Tests run: 29, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Los reportes quedan en `target/surefire-reports/`.

Para generar el WAR:

```bash
mvn clean package
```

El artefacto queda en `target/DSS_P4.war`.

### Ejecutar con Docker (opcional)

```bash
mvn clean package -DskipTests
docker build -t farmacia .
docker run -p 8080:8080 farmacia
```

La aplicación queda en `http://localhost:8080/DSS_P4/webapi/`. Nota: los endpoints que consultan datos fallan si no hay una base de datos accesible; el `persistence.xml` apunta a un servidor MariaDB original que ya no está disponible. Para comprobar que la aplicación arrancó, abrir `http://localhost:8080/DSS_P4/webapi/application.wadl`.

## 5. El pipeline

El flujo está definido en `.github/workflows/ci.yml` y sigue los seis pasos de `PIPELINE_DEVOPS.md`.

| Paso del plan | Dónde está en el pipeline |
|---|---|
| 1. Trigger por push | Bloque `on:` (push a `main` y `develop`, pull request hacia `main`) |
| 2. Infraestructura / Docker | Runner `ubuntu-latest`, `setup-java` (JDK 8), imagen Docker del job `docker-build` |
| 3. Checkout del código | `actions/checkout` |
| 4. Compilación | `mvn dependency:go-offline`, `mvn clean compile`, `mvn package` |
| 5. Pruebas unitarias | `mvn test` (con reportes subidos como artefacto) |
| 6. Despliegue al runtime | Job `smoke-test` (contenedor en GitHub) y job `deploy` (SSH, opcional) |

### Jobs y orden de ejecución

```
build-and-test  →  docker-build  →  smoke-test  →  deploy (opcional)
```

1. **build-and-test**: compila, ejecuta las pruebas unitarias y empaqueta el WAR. Si una prueba falla, el job termina en rojo y **los siguientes jobs no se ejecutan**.
2. **docker-build**: solo en push. Construye la imagen y la publica en GitHub Container Registry (ghcr.io).
3. **smoke-test**: levanta la imagen como contenedor y comprueba que Tomcat y Jersey responden. Falla si no arrancan.
4. **deploy**: despliegue por SSH a un servidor propio. Está **desactivado** salvo que se configure (ver sección 7).

En un pull request solo se ejecuta `build-and-test`.

## 6. Configurar y ejecutar el pipeline en GitHub

1. Crear un repositorio en GitHub (público) y subir el proyecto, o hacer **Fork** del repositorio original.
2. En el repositorio, ir a **Settings → Actions → General → Workflow permissions** y seleccionar **Read and write permissions**. Guardar. Esto permite publicar la imagen en ghcr.io.
3. Hacer un push a la rama `main`:

   ```bash
   git add .
   git commit -m "Prueba del pipeline"
   git push origin main
   ```

4. Abrir la pestaña **Actions** del repositorio y seleccionar la ejecución **Pipeline CI/CD**. Se ven los jobs y el detalle de cada paso.
5. Al terminar, en el resumen de la ejecución están los artefactos:
   - `surefire-reports`: reportes de las pruebas unitarias.
   - `war`: el archivo `DSS_P4.war`.
6. La imagen publicada aparece en la sección **Packages** del repositorio.

No se requiere ningún secret para el flujo principal. El `GITHUB_TOKEN` lo entrega GitHub automáticamente.

## 7. Despliegue opcional por SSH (paso 6 en un servidor propio)

El job `deploy` solo se ejecuta en `main`, después del smoke test y si existe la variable `DEPLOY_ENABLED=true`.

1. Preparar un servidor Linux con Docker instalado y el puerto 8080 abierto.
2. Si el paquete de ghcr.io es privado, ejecutar en el servidor `docker login ghcr.io` con un token con permiso `read:packages`, o hacer público el paquete desde **Packages**.
3. En el repositorio: **Settings → Secrets and variables → Actions**.
   - Secrets: `SSH_HOST`, `SSH_USER`, `SSH_PRIVATE_KEY`.
   - Variable: `DEPLOY_ENABLED` con valor `true`.
4. Hacer un push a `main`.

## 8. Comprobar que el pipeline falla cuando una prueba falla

Este es el procedimiento para validar el comportamiento ante pruebas rotas.

1. Abrir `src/test/java/jmv74211/DSS_P4/models/DepartmentTest.java`.
2. Cambiar, por ejemplo:

   ```java
   assertTrue(new Department("HERBALIST").hasValidAttributes());
   ```

   por:

   ```java
   assertFalse(new Department("HERBALIST").hasValidAttributes());
   ```

   (`assertFalse` ya está importado en ese archivo).
3. Ejecutar en local `mvn test`: debe terminar en `BUILD FAILURE` con `Failures: 1`.
4. Hacer commit y push:

   ```bash
   git commit -am "Prueba intencionalmente rota"
   git push origin main
   ```

5. En **Actions**: el job **Build & Test** termina en rojo en el paso *Ejecutar pruebas unitarias*. Los jobs `docker-build`, `smoke-test` y `deploy` **no se ejecutan**. El reporte del fallo está en el artefacto `surefire-reports`.
6. Revertir el cambio y hacer push de nuevo: el pipeline vuelve a verde.

## 9. Solución de problemas

| Síntoma | Causa y solución |
|---|---|
| `docker-build` falla con `permission denied` al publicar | Activar **Read and write permissions** (sección 6, paso 2) |
| `smoke-test` no puede descargar la imagen | Comprobar que `docker-build` terminó bien y que el repositorio tiene permisos de paquetes |
| `smoke-test` falla con "no respondió a tiempo" | Revisar los logs de Tomcat impresos en el paso para ver el error de arranque |
| El pipeline no se dispara | El push debe ser a `main` o `develop`. El workflow debe estar en `.github/workflows/` |
| `mvn test` no encuentra pruebas | Las clases de prueba deben estar en `src/test/java` y terminar en `Test` |
| Error de compilación con JDK moderno | Usar JDK 8, o revisar que `source/target` en el `pom.xml` sigan en 1.8 |

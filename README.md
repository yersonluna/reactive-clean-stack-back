# Franchise API - Reactive Clean Stack

Una API RESTful reactiva para gestionar una red de franquicias, sucursales e inventario de productos. Implementada con **Spring Boot 3.2**, **WebFlux**, **MongoDB** y **Clean Architecture**.

## 🎯 Características

- ✅ API RESTful completamente reactiva (Spring WebFlux)
- ✅ Persistencia NoSQL (MongoDB)
- ✅ Clean Architecture con separación clara de capas
- ✅ Programación reactiva con Project Reactor
- ✅ Pruebas unitarias con JUnit 5 y Mockito
- ✅ Contenerización con Docker y Docker Compose
- ✅ Cobertura de código con JaCoCo
- ✅ Java 21 (LTS)

## 📋 Requisitos Funcionales

| Código | Descripción |
|--------|-------------|
| RF-01 | Agregar Franquicia |
| RF-02 | Agregar Sucursal |
| RF-03 | Agregar Producto |
| RF-04 | Eliminar Producto |
| RF-05 | Modificar Stock |
| RF-06 | Top Stock por Sucursal (Complejo) |
| RF-07 | Actualización Pro (Renombrar) |

## 🚀 Inicio Rápido

### Opción 1: Docker Compose (Recomendado)

```bash
# Clonar repositorio
git clone https://github.com/tu-usuario/reactive-clean-stack-back.git
cd reactive-clean-stack-back

# Ejecutar con Docker Compose
docker-compose up --build

# La API estará disponible en: http://localhost:8080
```

### Opción 2: Ejecución Local

**Requisitos**:
- JDK 21+
- Maven 3.8+
- MongoDB 7.0 (corriendo en localhost:27017)

```bash
# Compilar
mvn clean package

# Ejecutar
java -jar target/franchise-api-1.0.0.jar

# O usar Maven directamente
mvn spring-boot:run
```

## 📚 Documentación

Para ver el detalle técnico del backend, consulta el [SRS de Backend](docs/BACKEND_SRS.md).

## 🔌 Ejemplos de API

### Crear Franquicia
```bash
curl -X POST http://localhost:8080/api/franchises \
  -H "Content-Type: application/json" \
  -d '{"name":"Franquicia XYZ"}'
```

### Listar Franquicias
```bash
curl http://localhost:8080/api/franchises
```

### Agregar Sucursal
```bash
curl -X POST http://localhost:8080/api/franchises/{franchiseId}/branches \
  -H "Content-Type: application/json" \
  -d '{"name":"Sucursal Centro"}'
```

### Agregar Producto
```bash
curl -X POST http://localhost:8080/api/franchises/{franchiseId}/branches/{branchId}/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Laptop","stock":50}'
```

### Actualizar Stock
```bash
curl -X PUT http://localhost:8080/api/franchises/{franchiseId}/branches/{branchId}/products/{productId}/stock \
  -H "Content-Type: application/json" \
  -d '{"stock":75}'
```

### Top Stock por Sucursal (RF-06)
```bash
curl http://localhost:8080/api/franchises/{franchiseId}/top-stock
```

Response:
```json
[
  {
    "branchId": "b1",
    "branchName": "Sucursal Centro",
    "productId": "p1",
    "productName": "Laptop",
    "stock": 150
  }
]
```

### Renombrar Franquicia
```bash
curl -X PUT http://localhost:8080/api/franchises/{franchiseId}/rename \
  -H "Content-Type: application/json" \
  -d '{"newName":"Nueva Franquicia"}'
```

## 🧪 Testing

### Ejecutar Tests
```bash
mvn clean test
```

### Generar Reporte de Cobertura
```bash
mvn clean test jacoco:report
# Abrir: target/site/jacoco/index.html
```

### Tests Implementados
- **FranchiseServiceTest**: 7 tests unitarios
- **FranchiseControllerTest**: 6 tests de integración

Cobertura mínima esperada: >80%

## 🏗️ Arquitectura

```
src/main/java/com/franchise/
├── domain/           # Entidades y lógica de negocio
│   └── entity/
├── application/      # Casos de uso (servicios reactivos)
│   └── service/
└── infrastructure/   # Controladores, repositorios, DTOs
    ├── controller/
    ├── persistence/
    └── dto/
```

### Flujo Reactivo

```
HttpRequest
    ↓
Controller
    ↓
FranchiseService (Mono/Flux)
    ↓
FranchiseRepository (Reactive)
    ↓
MongoDB
    ↓
HttpResponse
```

## 🐳 Docker

### Construcción Manual
```bash
docker build -t franchise-api:latest .
```

### Ejecución con Docker
```bash
docker run -d \
  --name franchise-api \
  -p 8080:8080 \
  -e SPRING_DATA_MONGODB_URI=mongodb://mongodb:27017/franchise_db \
  franchise-api:latest
```

## 🔧 Configuración

### application.yml (Local)
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/franchise_db

server:
  port: 8080
```

### application-docker.yml (Docker)
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://mongodb:27017/franchise_db
```

## 📊 Endpoints Resumen

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/franchises` | Listar todas |
| GET | `/api/franchises/{id}` | Obtener por ID |
| POST | `/api/franchises` | Crear franquicia |
| POST | `/api/franchises/{id}/branches` | Crear sucursal |
| POST | `/api/franchises/{id}/branches/{bid}/products` | Crear producto |
| DELETE | `/api/franchises/{id}/branches/{bid}/products/{pid}` | Eliminar producto |
| PUT | `/api/franchises/{id}/branches/{bid}/products/{pid}/stock` | Actualizar stock |
| GET | `/api/franchises/{id}/top-stock` | Producto top por sucursal |
| PUT | `/api/franchises/{id}/rename` | Renombrar franquicia |
| PUT | `/api/franchises/{id}/branches/{bid}/rename` | Renombrar sucursal |
| PUT | `/api/franchises/{id}/branches/{bid}/products/{pid}/rename` | Renombrar producto |

## ⚠️ Manejo de Errores

La API retorna códigos HTTP estándar:

- `201 Created`: Recurso creado exitosamente
- `204 No Content`: Operación exitosa sin contenido
- `400 Bad Request`: Solicitud inválida
- `404 Not Found`: Recurso no encontrado
- `500 Internal Server Error`: Error del servidor

## 🚀 Producción (Azure)

*Ver carpeta `terraform/` para provisionar recursos en Azure*

Recursos a desplegar:
- Azure Container Registry
- Azure Container Instances o App Service
- Azure Cosmos DB (MongoDB API)
- Application Insights
- Virtual Networks y Security Groups

## 📝 Logs

```bash
# Ver logs en tiempo real
docker-compose logs -f franchise-api

# Ver logs de MongoDB
docker-compose logs -f mongodb
```

## 🧹 Limpieza

```bash
# Detener contenedores
docker-compose down

# Detener y eliminar volúmenes
docker-compose down -v

# Eliminar imágenes
docker rmi franchise-api:latest
```

## 🤝 Contribuir

1. Fork el repositorio
2. Crea una rama para tu feature: `git checkout -b feature/AmazingFeature`
3. Commit tus cambios: `git commit -m 'Add some AmazingFeature'`
4. Push a la rama: `git push origin feature/AmazingFeature`
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo licencia MIT.

## 📞 Soporte

Para preguntas o problemas, abre un issue en el repositorio.

---

**Última actualización**: 20 de Abril de 2026

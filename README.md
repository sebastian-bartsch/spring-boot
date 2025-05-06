# 📊 Sistema de Monitoreo de Sensores

Este sistema permite gestionar empresas, sensores, ubicaciones y recolectar datos en tiempo real mediante una arquitectura desacoplada basada en eventos.
Está construido con Spring Boot 3.4, Java 17, JPA, PostgreSQL y RabbitMQ.

---

## 🚀 Funcionalidad Principal

- Registrar empresas, ubicaciones y sensores.
- Asociar sensores a ubicaciones físicas.
- Guardar lecturas de sensores con su métrica correspondiente.
- Visualizar y consultar datos recolectados.
- Control de acceso por usuarios, roles y permisos.

---

## 🧱 Estructura de la Base de Datos

**Tablas principales:**
- `company`: Empresas que registran sensores.
- `location`: Ubicaciones físicas de sensores, vinculadas a una empresa y ciudad.
- `sensor`: Sensores físicos con una clave única (API key).
- `sensor_data`: Valores recolectados por sensores (valor, métrica, fecha).
- `measurement`: Registro de eventos de medición por sensor.
- `metric`: Tipo de métrica registrada (temperatura, humedad, iluminación, etc).

**Tablas de apoyo:**
- `city`: Ciudad, vinculada a una región.
- `region`: Región, vinculada a un país.
- `country`: País.
- `category`: Clasificación de sensor (ESP32, Zigbee, etc).

> Puedes ver el diagrama completo en `/docs/db-diagram.png` (si tienes uno).

---

## 📦 Requisitos del Sistema

- Java 17
- Maven 3.8+
- PostgreSQL 16
- RabbitMQ 3.x

---

## 🛠️ Instalación

1. Clona este repositorio.
2. Instala Java 17 o superior.
3. Instala Lombok.
4. Instala o accede a una base de datos PostgreSQL.
   - Crea una base de datos llamada `iot-api` (las tablas se generarán automáticamente con JPA-Hibernate).
5. Instala o accede a un servidor RabbitMQ.
   - Crear cola: `sensor_cola`
   - Crear exchange: `sensor_exchange`
   - Crear ruta: `sensor_ruta` y asociarla con el exchange.
   - *Si se desea cambiar nombres, modificar la clase:* `iot-api/src/main/java/com/tld/configuration/RabbitMQConfig.java`
6. Configura las variables de entorno requeridas en `application.properties`. Ejemplo en Linux:

```bash
export DB_HOST="localhost o ip"
export DB_PORT="5432"
export DB_NAME="iot-api"
export DB_USER="usuario"
export DB_PASSWORD="contraseña"
export JWT_PRIVATE_KEY="fade5fd33efa966698314310e74c3bf9827a04b875d1e4f8a088b389c54ab7a1"
export JWT_USER_KEY="AUTH0JWT"

export RB_HOST="localhost o ip"
export RB_PORT="5672"
export RB_USER="usuario rabbit"
export RB_PASSWORD="clave_rabbit"
```

> También se puede modificar directamente en `application.properties` (no recomendado para producción).

7. Compila y ejecuta el proyecto:
```bash
mvn clean install
mvn spring-boot:run
```

---

## 🛠️ Endpoints REST

Cada endpoint tiene las siguientes operaciones:

- `POST`: Ingreso de datos.
- `PUT`: Actualización de datos (principalmente usado para activar registros inactivos).
- `GET`: Obtención de múltiples datos en formato JSON.
- `DELETE`: Borrado lógico de registros.

Los métodos están agrupados por entidad. Para más detalle, revisar la documentación de endpoints y la colección de Postman adjunta.

- `/api/v1/company`
- `/api/v1/location`
- `/api/v1/sensor`
- `/api/v1/measurement`
- `/api/v1/rabbit`

---

## ▶️ Forma de uso

### Requisitos

- Tener Postman instalado.
- Descargar la colección de Postman adjunta.
- Configurar una variable de entorno en Postman con la IP del servidor o `localhost`.

### Flujo sugerido

1. Obtener token de autenticación:
   - `user > loginUser`
   - `POST /api/auth/login?username=USER&password=PASS`

2. Usar los siguientes endpoints en orden:

- `company > addCompany`
- `location > addLocation`
- `sensor > addSensor` (con API key de empresa en header)
- `measurement > addSensorData` (con API key de sensor en header o JSON body)

---

## 📄 Licencia

Este proyecto es de uso privado/educativo. Ajusta este apartado según tu caso.

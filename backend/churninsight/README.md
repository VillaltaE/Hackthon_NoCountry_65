# 🎬 ChurnInsight
## Backend

### 🧠 Descripción

Este módulo corresponde al **backend del proyecto ChurnInsight**, desarrollado en **Java con Spring Boot**.

Su responsabilidad es exponer una **API REST** que reciba datos de clientes y devuelva una **predicción de churn**, consumiendo un servicio externo de Machine Learning.

El backend actúa como **punto de entrada principal del sistema**.

---

### 🎯 Objetivo del MVP (Backend)

- Exponer endpoint `/predict`
- Validar datos de entrada
- Consumir API de predicción
- Retornar:
    - resultado de churn
    - probabilidad asociada

---

### 🏗️ Ubicación dentro del repositorio
```
Hackthon_NoCountry_65/
├── backend/
    └── churninsight/ ← ESTE es el proyecto Spring Boot
```
#### ⚠️ Importante
El proyecto ejecutable es `backend/churninsight`.  
El repositorio raíz **no es un proyecto Java** y no se ejecuta directamente.

---

### 🛠️ Stack tecnológico

- Java 21
- Maven
- Spring Boot 4.0.1
- Spring Web (API REST)
- Spring Validation (validación de requests)
- Jackson (serialización JSON)
- JUnit / Spring Test (tests – futuras iteraciones)

---

### ▶️ Cómo ejecutar el backend

#### 🔹 Requisitos
- Java 21
- Acceso a una terminal (Windows, Linux o macOS)

> El proyecto utiliza **Maven Wrapper**, por lo que **no es necesario tener Maven instalado**.

---

#### 🔹 Pasos para ejecutar

1. Posicionarse en la carpeta del proyecto backend:
```
cd backend/churninsight
```
2. Ejecutar la aplicación:

- Windows
```
mvnw spring-boot:run
```

- Linux / macOS
```
./mvnw spring-boot:run
```

3. Esperar a que el proyecto compile y se levante correctamente.

🔹 **Acceso a la aplicación**

Por defecto, el backend se ejecuta en:
```
http://localhost:8080
```
🔹 **Verificación rápida**

Si la aplicación está corriendo correctamente, se puede verificar accediendo a:
```
http://localhost:8080
```

o consumiendo los endpoints expuestos mediante una herramienta de pruebas HTTP
(Postman, Insomnia, curl, etc.).

> **Nota:** El proyecto puede ejecutarse desde cualquier IDE Java compatible, siempre que se abra la carpeta backend/churninsight como proyecto Maven.


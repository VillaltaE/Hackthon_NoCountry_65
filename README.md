
---

<div style="display:flex; justify-content: center; align-items: center;">
        <img src="https://i.ibb.co/BV3Y2y80/ascii-text-art-removebg-preview-removebg-preview.png" 
         width=1000>  
</div>

---

# 🎬 ChurnInsight - Hackathon NoCountry – Equipo 65

![Python](https://img.shields.io/badge/Python-3.10%2B-blue?logo=python)
![FastAPI](https://img.shields.io/badge/FastAPI-green?logo=fastapi)
![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-brightgreen?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue?logo=postgresql)
![Pandas](https://img.shields.io/badge/Pandas-2.2.2-violet?logo=pandas)
![Seaborn](https://img.shields.io/badge/seaborn-0.13.2-blue?logo=seaborn)
![Status](https://img.shields.io/badge/status-active-success)

El Hackathon ONE – No Country 2025 es una iniciativa que tiene como objetivo ofrecer una experiencia práctica de simulación laboral, fomentando la colaboración entre estudiantes, el desarrollo de soluciones tecnológicas y el fortalecimiento de habilidades de trabajo en equipo, comunicación y resolución de problemas.

### 🧠 Descripción del proyecto

ChurnInsight es una solución de analítica predictiva cuyo objetivo es anticipar la cancelación (churn) de clientes mediante técnicas de Machine Learning.

El proyecto se basa en un dataset de clientes de Netflix, a partir del cual se entrena un modelo capaz de identificar patrones de comportamiento asociados a la cancelación del servicio.
La predicción se expone mediante una API, permitiendo a sistemas externos consultar la probabilidad de churn de un cliente.

---
### 🎯 Problema que resuelve

La pérdida de clientes impacta directamente en los ingresos de las empresas de suscripción.
Detectar clientes con alta probabilidad de churn permite:

- Aplicar estrategias de retención tempranas
- Reducir pérdidas económicas
- Mejorar la toma de decisiones basada en datos

---

### 🤖 Funcionalidades

- Generación de predicciones de churn por cliente utilizando un modelo de Machine Learning externo.
- Exposición de una API REST para el consumo de predicciones y datos de churn.
- Interfaz web (frontend) con formulario para el ingreso de datos y visualización de resultados de predicción.
- Validación de campos obligatorios en las solicitudes.
- Respuesta estructurada que incluye predicción, probabilidad de churn y mensaje interpretativo.
- Persistencia del historial de predicciones en una base de datos PostgreSQL (Supabase).
- Visualización y descarga del historial de predicciones.
- Cálculo de KPIs básicos de churn (total evaluados, niveles de riesgo alto/medio/bajo y tasa de churn) y visualización en el frontend.
- Obtención de factores de riesgo y sugerencias de acción personalizadas por cliente.
- Pruebas unitarias.

---

### 📺 Live Demo 

La interfaz web tiene varios componentes:
 
- En la parte superior encontramos indicadores básicos de churn, como número de clientes evaluados, cantidad de clientes en riesgo alto, medio y bajo, así como también la tasa de churn.

![demo_1](https://i.ibb.co/HfcZGFgb/ezgif-1ce0a1e4657b3ac7.gif)

- Debajo de los indicadores encontramos dos paneles, en el lado izquierdo cargamos los datos sobre el cliente que queremos analizar y luego seleccionamos el botón predecir

![demo_2](https://i.ibb.co/fd3fr5W8/ezgif-1c2cea7233168f64.gif)

-  Podemos pedirle al sistema que nos genere una predicción de carácter aleatorio para observar como funciona la aplicación.

![demo_3](https://i.ibb.co/LzrTFJn5/ezgif-1128632c906a20c3.gif)

- En el panel derecho encontramos el resultado de la predicción: la etiqueta va a continuar / va cancelar junto con la probabilidad de deserción. También, podemos hace click en el botón ver detalles de la predicción para obtener más información sobre el comportamiento del usuario así como también acciones sugeridas.

![demo_4](https://i.ibb.co/SwP4WZSB/ezgif-5ea4ac3960582d49.gif)
 
- Abajo de los dos paneles encontramos el historial de predicciones, donde podemos acceder a predicciones ya realizadas anteriormente. Tenemos una función de filtro, se puede buscar una predicción o predicciones por id de cliente o por fechas. Además, hay un botón para descargar el historial en formato csv.

![demo_5](https://i.ibb.co/39BZfBmH/demo-5.gif)

Consulta el siguiente video para ver la live demo completa: 

[Live Demo](https://youtu.be/nOfDL6W6_QA)


---

### 📝 Ejemplos de petición y respuesta

La API recibe la siguiente petición con los datos del cliente:

```
{
  "customer_id": "user-123",
  "features": {
    "subscription_type": "Premium",
    "watch_hours": 120.5,
    "last_login_days": 3,
    "monthly_fee": 17.99,
    "number_of_profiles": 4,
    "avg_watch_time_per_day": 2.5,
    "payment_method": "Credit Card"
  }
}

```

y retorna la respuesta de la petición en el siguiente formato:

- ✅ Si la predicción se generó correctamente

```
{
  "timestamp": "2026-01-12T15:45:30",
  "status": 200,
  "message": "Predicción generada correctamente",
  "data": {
    "customer_id": "user-123",
    "prediction": {
      "label": "will_churn",
      "probability": 0.82
    },
    "prevision": "Va a cancelar"
  },
  "path": "/api/predict"
}

```
- ❌ Si hubo algún error en la petición

```

{
  "timestamp": "2026-01-12T15:45:30",
  "status": 400,
  "error": "Error de Validación",
  "details": {
    "features.numberOfProfiles": "el valor debe estar entre 1 y 5"
  },
  "path": "/api/predict"
}

```

- ❌ Si el modelo Machine Learning está fuera de servicio

```

{
  "timestamp": "2026-01-12T15:45:30",
  "status": 503,
  "error": "Servicio ML no disponible",
  "details": {
    "ml_service": "timeout al intentar obtener predicción"
  },
  "path": "/api/predict"
}

```
---

### 🧠 Arquitectura del proyecto

Construimos una arquitectura multi-backend orientada a la predicción de churn. A continuación, un esquema de la estructura del proyecto:

```
Frontend
   ↓
Spring Boot (Java) ──────────► PostgreSQL (Supabase)
   ↓
FastAPI (Python) ──► Modelo ML (scikit-learn)

```

---

### 🧩 Componentes

- 🐍 API Python (FastAPI)

Predicción utilizando un modelo de Machine Learning (scikit-learn).

-  Backend Java (Spring Boot)

Lógica de negocio, orquestación y persistencia de datos.

- 🗄️ PostgreSQL (Supabase)

Almacenamiento del historial de predicciones

---

### 📁 Estructura del repositorio

```
Hackthon_NoCountry_65/
├── README.md                 # Documentación general del proyecto
│
├── backend/
│   └── churninsight/         # Proyecto Spring Boot (Java)
│
├── data-science/             # Dataset, notebooks y entrenamiento del modelo
│
└── api-python/               # API en Python para servir el modelo de predicción
|
└── frontend/                 # Formulario y resultados de las predicciones

```
---

#### ⚠️ Nota sobre la ejecución

Este repositorio contiene un sistema compuesto por varios proyectos independientes (frontend, backend y servicio de Machine Learning).

Cada componente se ejecuta de forma separada y cumple un rol específico dentro de la arquitectura del sistema.

Para levantar el sistema completo, se recomienda ejecutar los servicios en el siguiente orden:

1. API Python (FastAPI – modelo de Machine Learning)
2. Backend Java (Spring Boot – orquestación y persistencia)
3. Frontend (interfaz web)

Las instrucciones detalladas de ejecución se encuentran en los README de cada componente.

---

### 📦 Requisitos

#### 🐈‍⬛ General

+ Git
+ Conexión a Internet
+ Terminal (Windows, Linux o macOS)

#### 🐍 Python

+ Python 3.10+ (recomendado 3.11 o 3.12)
+ pip

#### Java

+ Java 21
+ Maven NO requerido (se usa mvnw)

---

### 🚀 Instalación

#### 1️⃣ Clonar el repositorio

```bash
git clone https://github.com/VillaltaE/Hackthon_NoCountry_65.git
cd Hackthon_NoCountry_65
```
---

#### 🐍 API Python – FastAPI (Puerto 8000)

#### 📂 Ir a la carpeta

```bash
cd api-python
```

#### 🧱 Crear entorno virtual (solo la primera vez)

#### 🪟🐧Windows/Linux/MacOS

```bash
python -m venv .venv
```

#### ▶️ Activar entorno virtual

#### 🪟 En Windows (PowerShell)

```powershell
.\.venv\Scripts\Activate.ps1
```

> ⚠️ Si PowerShell bloquea scripts:

```powershell
Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy RemoteSigned
```

Luego vuelve a activar:

```powershell
.\.venv\Scripts\Activate.ps1
```
#### En Linux/MacOS

```bash
source .venv/bin/activate
```

#### 📥 Instalar dependencias

#### 🪟🐧Windows/Linux/MacOS

```bash
pip install -r requirements.txt
```

> Si aparece `ModuleNotFoundError: No module named 'pydantic_settings'`:

```bash
pip install pydantic-settings
```

> Nota: Si el modelo avisa versiones distintas de scikit-learn (warning), el servicio puede funcionar igual. Para igualar versión:

```bash
pip install scikit-learn==1.6.1
```

#### ▶️ Levantar API Python

```bash
uvicorn main:app --reload
```

✅ Verifica:
- API: `http://127.0.0.1:8000`
- Health: `http://127.0.0.1:8000/health`

---

#### ☕ Backend Java – Spring Boot (Puerto 8080)

#### 📂 Ir a la carpeta

```bash
cd backend/churninsight
```

#### ▶️ Levantar backend

```powershell
.\mvnw.cmd spring-boot:run
```

> (Opcional: más rápido, sin tests)

```powershell
.\mvnw.cmd spring-boot:run -DskipTests
```
#### 🐧Linux/MacOS

```bash
chmod +x mvnw
```

```bash
./mvnw spring-boot:run
```

✅ Verifica:
- Backend: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

---

#### 🔁 Orden recomendado de ejecución

1. Levanta **API Python** (puerto **8000**)
2. Levanta **Spring Boot** (puerto **8080**)

El backend Java consulta `http://localhost:8000/health` y llama `POST /predict`.

---

#### 🌐 Levantar el Frontend (index.html)

##### 📂 Ir a la carpeta **frontend**

En el **directorio de frontend** con `index.html` \Hackthon_NoCountry_65-main\Hackthon_NoCountry_65_1501\frontend, sigue estos pasos para levantarlo:

1. Abre el archivo **`index.html`** en tu navegador.
   - Si tienes un **servidor web** como **Apache**, **Nginx**, o algo similar, puedes levantarlo con ese servidor. Por ejemplo:

   🪟 En Windows/ 🐧Linux
   ```bash
      python -m http.server 8001
    ```

2. **Si es un proyecto estático**, simplemente abre `index.html` directamente en tu navegador. Esto debería permitirte interactuar con el backend a través de la API expuesta.

> Si tienes algún problema con CORS (Cross-Origin Resource Sharing), asegúrate de que el **Backend Java (Spring Boot)** permita peticiones desde el frontend.

---

### 🆙 Proyecto en funcionamiento

#### 🏁 Estado esperado (cuando todo funciona)

- FastAPI arriba en `8000` (health OK).
- Spring Boot arriba en `8080`.
- Spring Boot llama a FastAPI `/predict` (200 OK).
- Se guardan predicciones en PostgreSQL (Supabase).

```
Modelo cargado exitosamente
Application startup complete
Started ChurninsightApplication
Predicción exitosa
BUILD SUCCESS
```
---

### 🧯 Problemas comunes

#### ❌ No se puede activar `.venv`

- Asegúrate de haber creado el entorno:
  ```bash
  python -m venv .venv
  ```
- Si PowerShell bloquea scripts:
  ```powershell
  Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy RemoteSigned
  ```

#### ❌ `ModuleNotFoundError`

Ejecuta:
```bash
pip install -r requirements.txt
```
o instala el paquete faltante:
```bash
pip install pydantic-settings
```

#### ❌ Spring Boot no conecta a Python

Verifica que Python esté arriba:
- `http://127.0.0.1:8000/health` → debe responder 200

#### ❌ Puerto ocupado (8000/8080)

Cierra procesos previos o cambia el puerto.
Ejemplo FastAPI:
```bash
uvicorn main:app --reload --port 8001
```
---

###  🪛 Stack Tecnológico
Hemos dividido nuestras tecnologías en herramientas de entorno (runtime/infraestructura) y dependencias de software (frameworks/librerías).

#### Infrastructura y herramientas

- Java 21.
- Maven Wrapper.
- Python 3.12.
- Uvicorn 0.32.1.
- PostgreSQL (v17 via Supabase).
- Jupyter Notebooks.

####  Librerías y Frameworks (dependencias)

|     **Ámbito**    |    **Tecnología**    |   **Versión**  |
|:-----------------:|:--------------------:|:--------------:|
| Backend Core      | Spring Boot          | 4.0.1          |
| Web / API REST    | Spring Web           | 4.0.1          |
| ML Serving        | FastAPI              | 0.115.5        |
| Data Science      | Scikit-learn         | 1.5.2          |
| Data Analysis     | Pandas / Numpy       | 2.2.2/2.0.2    |
| Data Analysis     | Statsmodels / Scipy  | 0.14.6/1.16.3  |
| Visualización     | Seaborn / Matplotlib | 0.13.2         |
| Serialización(Py) | Joblib               | 1.4.2          |
| Serialización     | Jackson              | 2.x (Internal) |
| Validación (Java) | Spring Validation    | 4.0.1          |
| Validación (Py)   | Pydantic             | 2.10.5         |
| Testing           | JUnit / Spring Test  | 5.x / 4.0.1    |
| Frontend UI       | Bootstrap            | 5.x            |
| HTTP Client       | Httpx                | 0.28.1         |
| Configuration     | Python-dotenv        | 1.0.1          |
| Testing           | JUnit / Pytest       | 8.3.4          |

---

### 👥 Organización del equipo

- **Backend:** desarrollo de frontend ([consulta aquí](https://github.com/VillaltaE/Hackthon_NoCountry_65/blob/main/frontend/README.md)), API principal ([ver más](https://github.com/VillaltaE/Hackthon_NoCountry_65/blob/main/api-python/README.md)) y consumo del servicio de predicción ([más información](https://github.com/VillaltaE/Hackthon_NoCountry_65/blob/main/backend/churninsight/README.md)).
- **Data Science:** análisis del dataset de Netflix, entrenamiento y evaluación del modelo ([ver más](https://github.com/VillaltaE/Hackthon_NoCountry_65/blob/main/data-science/README_DATA_SCIENCE.md)).

---

### 👪 Integrantes del equipo

- José Bartra (Backend Developer).
- Edgardo Encina (Data Scientist).
- Jimena Garcia ( Data Scientist).
- Clarivel Jeldres (Backend Developer).
- Edgar Jose Villalta Martinez (Data Scientist).




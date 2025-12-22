# 🎬 ChurnInsight
## Hackathon NoCountry – Equipo 65
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
```
---
### ⚠️ Nota importante sobre la ejecución

El repositorio contiene varios proyectos independientes.

El proyecto ejecutable es:
```
backend/churninsight
```

El repositorio raíz no es un proyecto Java y no se ejecuta directamente.

---
### 🛠️ Tecnologías utilizadas

- Java 21 + Spring Boot
- Python (Machine Learning)
- Jupyter Notebook
- FastAPI (servicio de predicción)
- Git & GitHub

---
### 👥 Organización del equipo

- **Backend:** desarrollo de la API principal y consumo del servicio de predicción
- **Data Science:** análisis del dataset de Netflix, entrenamiento y evaluación del modelo

---
### 📌 Estado del proyecto
🚧 **MVP en desarrollo**




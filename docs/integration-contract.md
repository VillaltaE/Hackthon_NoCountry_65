# 📜 Contrato de Integración – Predicción de Churn
### 🎯 Objetivo

Definir el formato de intercambio de datos entre el Backend (Spring Boot) y el servicio de Machine Learning (FastAPI) para la predicción de churn.

El backend es responsable de:

- recibir solicitudes del cliente
- validar datos
- gestionar identificadores
- consumir el servicio de ML

El servicio de ML:

- recibe únicamente las features
- retorna la predicción y su probabilidad

---

### 🔗 Servicio de predicción (Data Science)
**Endpoint**
```
POST /predict
```
---

### 📥 Request (Backend → Data Science)
**JSON de entrada (features del modelo)**
```json
{
"subscription_type": "premium",
"watch_hours": 120.5,
"last_login_days": 3,
"monthly_fee": 15.99,
"number_of_profiles": 4,
"avg_watch_time_per_day": 2.8
}
```

---

### 📤 Response (Data Science → Backend)
**JSON de salida**
```json
{
  "prediction": "Va a cancelar",
  "probability": 0.91
}
```
#### 📌 Definiciones

**prediction:** resultado de la clasificación del modelo

- "Va a cancelar"
- "Va a continuar"

**probability:** probabilidad asociada a la predicción (valor entre 0 y 1)  

---
### 🔁 Response final (Backend → Cliente)

El backend agrega información de negocio:
```json
{
  "customer_id": "C012",
  "prediction": "Va a cancelar",
  "probability": 0.91
}
```
#### 📌 Nota sobre identificadores
> El identificador del cliente (customer_id) es gestionado exclusivamente por el backend.
El servicio de Data Science no recibe ni retorna IDs, solo procesa features del modelo.




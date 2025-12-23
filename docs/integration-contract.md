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
"churn_prediction": true,
"churn_probability": 0.76
}
```
---
### 🔁 Response final (Backend → Cliente)

El backend agrega información de negocio:
```json
{
"customer_id": "C012",
"churn_prediction": true,
"churn_probability": 0.76
}
```




# 🎬 ChurnInsight
## Hackathon NoCountry – Equipo 65
### 🧠 Descripción del proyecto

ChurnInsight es una solución de analítica predictiva cuyo objetivo es anticipar la cancelación (churn) de clientes mediante técnicas de Machine Learning.

El proyecto se basa en un dataset de clientes de Netflix, a partir del cual se entrena un modelo capaz de identificar patrones de comportamiento asociados a la cancelación del servicio.
La predicción se expone mediante una API, permitiendo a sistemas externos consultar la probabilidad de churn de un cliente.

---

### 💾 Descripción del dataSet

El dataset consta de 14 columnas y 5000 filas.
 Las columnas son las siguientes:
 - customer_id: cadena de caracteres
 - age, años
 - gender: female, male y  other
 - subscription_type: Basic, Premium y Standard
 - watch_hours: horas
 - last_login_days: dias
 - region: Africa, Asia, Europa, North América, Oceanía y  South América
 - device: Desktop,TV, Mobile, Laptop y Tablet
 - monthly_fee: Doláres de USA
 - churned: 1 si y 0 no
 - payment_method: Credit Card, Debit Card, Crypto, Gift Card y Paypal
 - number_of_profiles: Entre 1 y 5
 - avg_watch_time_per_day: minutos
 - favorite_genre: Action, Sci-Fi, Drama,Horror,Romance, Comedy y Documentary





### 🎯 Problema que resuelve

La pérdida de clientes impacta directamente en los ingresos de las empresas de suscripción.
Detectar clientes con alta probabilidad de churn permite:

- Aplicar estrategias de retención tempranas
- Reducir pérdidas económicas
- Mejorar la toma de decisiones basada en datos

### 🛠️ Tecnologías utilizadas

## EDA

- matplotlib.pyplot
- seaborn 
- numpy
- pandas

## Modelo predictivo

-librerias de sklearn

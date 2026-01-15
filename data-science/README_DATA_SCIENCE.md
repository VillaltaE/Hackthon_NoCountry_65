# 🎬 ChurnInsight
## Hackathon NoCountry – Equipo 65
### 🧠 Descripción del proyecto

ChurnInsight es una solución de analítica predictiva cuyo objetivo es anticipar la cancelación (churn) de clientes mediante técnicas de Machine Learning.

El proyecto se basa en un dataset de clientes de Netflix, a partir del cual se entrena un modelo capaz de identificar patrones de comportamiento asociados a la cancelación del servicio.
La predicción se expone mediante una API, permitiendo a sistemas externos consultar la probabilidad de churn de un cliente.

---

### 💾 Descripción del dataset

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
 - avg_watch_time_per_day: horas (watch_hours/last_login_days + 1)
 - favorite_genre: Action, Sci-Fi, Drama,Horror,Romance, Comedy y Documentary


### 🎯 Problema que resuelve

La pérdida de clientes impacta directamente en los ingresos de las empresas de suscripción.
Detectar clientes con alta probabilidad de churn permite:

- Aplicar estrategias de retención tempranas
- Reducir pérdidas económicas
- Mejorar la toma de decisiones basada en datos

### 📊 Tecnologías utilizadas

## EDA

- Matplotlib.pyplot.
- Seaborn. 
- Numpy.
- Pandas.
- StatsModels.

## Modelo predictivo

- Librerias de scikitlearn.
- Librería Statsmodels.

## Análisis estadístico

El análisis estadístico con ***Chi Cuadrado*** y la ***V de Cramer*** se usa para determinar
si hay asociación significativa entre dos variables categóricas y qué tan fuerte
es esa relación.

El ***coeficiente de correlación de Pearson*** utilizado con el objetivo de medir la dependencia lineal entre dos variables continuas.

Análisis de multicolinealidad a través del método ***Factor de Inflación de la Varianza (VIF)*** entre las variables del modelo de Regresión Logística para evaluar la independiencia de las mismas. 

## Modelo seleccionado

Se hicieron pruebas con los modelos Random Forest y Logistic Regression, ambos modelos funcionan, sin embargo, se seleccionó el modelo de Logistic Regression porque es más sencillo de explicar a los tomadores de decision, por medio de una ecuación.

Aplicando el modelo de Logistic Regression a nuestro problema , la probabilidad de cancelación del servicio de Netflix es la siguiente:

$$P(Y=1|X) = \frac{1}{1 + e^{-(\beta_0 + \beta_1X_1 + \beta_2X_2 + \dots + \beta_n X_n)}}$$

donde  β0  es el intercepto de la regresión y  β1 ,  β2  ...  βn  son los coeficientes de las variables predictoras.


from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import pickle
import pandas as pd
import uvicorn

app = FastAPI(title="Netflix Churn Prediction Service")

# 1. Cargar el modelo al iniciar la aplicación
try:
    with open('modelo.pkl', 'rb') as f:
        model = pickle.load(f)
    print("Modelo cargado exitosamente")
except Exception as e:
    print(f"Error al cargar el modelo: {e}")

# 2. Definir el orden exacto de las columnas que espera el modelo
MODEL_COLUMNS = [
    "subscription_type", 
    "watch_hours", 
    "last_login_days", 
    "monthly_fee", 
    "number_of_profiles", 
    "avg_watch_time_per_day", 
    "payment_method"
]

# 3. Modelos de datos (Pydantic) para validación de entrada
class CustomerFeatures(BaseModel):
    subscription_type: str
    watch_hours: float
    last_login_days: int
    monthly_fee: float
    number_of_profiles: int
    avg_watch_time_per_day: float
    payment_method: str

class PredictRequest(BaseModel):
    customer_id: str
    features: CustomerFeatures

# 4. Endpoint de predicción
@app.post("/predict")
def predict(request: PredictRequest):
    try:
        # Convertir los datos recibidos a un diccionario
        input_dict = request.features.model_dump()
        
        # Crear DataFrame y forzar el orden de las columnas
        data_df = pd.DataFrame([input_dict])[MODEL_COLUMNS]
        
        # Realizar la predicción
        proba = model.predict_proba(data_df)[0, 1]
        
        # Determinar la etiqueta según el umbral de 0.5 
        label = "will_churn" if proba >= 0.5 else "will_continue"
        
        # Debug en consola de Python
        print(f"Predicción para ID {request.customer_id}: Prob={proba:.4f} Label={label}")
        
        return {
            "customer_id": request.customer_id,
            "prediction": {
                "label": label,
                "probability": round(float(proba), 3)
            }
        }
        
    except Exception as e:
        print(f"Error durante la inferencia: {e}")
        raise HTTPException(status_code=400, detail=f"Error procesando la predicción: {str(e)}")

# 5. Punto de entrada para ejecutar el script directamente
if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
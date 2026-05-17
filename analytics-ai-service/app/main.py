from fastapi import FastAPI
from app.routes.prediction_routes import router as prediction_router

app = FastAPI(
    title="Analytics AI Service"
)

app.include_router(prediction_router)

@app.get("/health")
def health():
    return {
        "status": "UP"
    }
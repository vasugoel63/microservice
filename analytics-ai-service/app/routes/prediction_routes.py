from fastapi import APIRouter
from app.schemas.prediction_request import RevenuePredictionRequest
from app.schemas.prediction_response import RevenuePredictionResponse
from app.services.prediction_service import PredictionService

router = APIRouter()

@router.post("/api/ai/predict/revenue",
             response_model=RevenuePredictionResponse)
def predict_revenue(request: RevenuePredictionRequest):

    prediction = PredictionService.predict_revenue(request.day)

    return RevenuePredictionResponse(
        predictedRevenue=prediction
    )
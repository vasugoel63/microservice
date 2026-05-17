from pydantic import BaseModel

class RevenuePredictionResponse(BaseModel):
    predictedRevenue: float
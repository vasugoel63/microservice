from pydantic import BaseModel;

class RevenuePredictionRequest(BaseModel):
    day: int
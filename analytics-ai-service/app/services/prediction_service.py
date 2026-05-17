from app.ml.model_loader import model

class PredictionService:

    @staticmethod
    def predict_revenue(day: int):
        prediction = model.predict([[day]])
        return float(prediction[0])
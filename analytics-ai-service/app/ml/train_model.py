import pandas as pd
from sklearn.linear_model import LinearRegression;
import joblib;
import os;

from app.config.database import engine

query = """
SELECT
    DATE(created_at) AS payment_day,
    SUM(amount) AS revenue
FROM transaction
WHERE status = 'SUCCESS'
GROUP BY payment_day
ORDER BY payment_day
"""

df = pd.read_sql(query, engine)
# data={
#     "day": [1,2,3,4,5,6,7],
#     "revenue":[10000,12000,15000,18000,20000,23000,25000]
# }
print(df)
df["payment_day"]
# df = pd.DataFrame(data);

print(df)

df["payment_day"] = pd.to_datetime(df["payment_day"])

df["day_number"] = (
    df["payment_day"] - df["payment_day"].min()
).dt.days

X = df[["day_number"]]
y = df["revenue"]

model = LinearRegression()

model.fit(X, y)

os.makedirs("saved_models", exist_ok=True)

joblib.dump(model, "saved_models/revenue_model.pkl")

print("Model trained successfully")
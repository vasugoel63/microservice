from sqlalchemy import create_engine;

DATABASE_URL = "postgresql://postgres:password@postgres-db:5432/billing-db"


engine = create_engine(DATABASE_URL)
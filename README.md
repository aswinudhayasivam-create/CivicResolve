# CivicResolve — Public Complaint & Grievance Management System

React/Vite frontend + Spring Boot/Java API + PostgreSQL + JWT + C/C++ native modules.

## Local

Frontend:
```
cd frontend
npm install
npm run dev
```

Backend:
```
cd backend
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Duser.timezone=UTC"
```

Frontend API URL is configurable with `VITE_API_URL`.

## Public deployment

Use Vercel for the React frontend, Render Docker for the Java backend, and Neon/managed PostgreSQL for the database. See `DEPLOYMENT-GUIDE.md`.

This is the important difference from local development: after deployment, the public URL is hosted in the cloud and your laptop/VS Code does not need to remain running.

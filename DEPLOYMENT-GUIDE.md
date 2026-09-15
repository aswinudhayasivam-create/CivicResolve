# CivicResolve — Public URL Deployment

This version is prepared so the frontend does **not** depend on `localhost:8080`.
The frontend reads `VITE_API_URL`, while Spring Boot reads `DB_URL`, `DB_USER`, `DB_PASSWORD`, `JWT_SECRET`, `CORS_ORIGIN`, and `PORT`.

## Recommended architecture

```text
Phone / PC / Tablet
        │
        ▼
Vercel (React frontend)
        │ HTTPS
        ▼
Render (Spring Boot API)
        │
        ▼
Neon / managed PostgreSQL
```

Your laptop does **not** need to stay on after deployment.

## 1. Put the project on GitHub

Create a GitHub repository and upload this entire project. Do not upload real `.env` files or passwords.

## 2. Create PostgreSQL

Create a PostgreSQL database on Neon or another managed PostgreSQL provider. Copy its connection details.

For Neon, the connection string normally looks like:
`postgres://USER:PASSWORD@HOST/DATABASE?sslmode=require`

For Spring Boot, set `DB_URL` to the JDBC form:
`jdbc:postgresql://HOST/DATABASE?sslmode=require`

Set:
- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`

Then run the SQL in `database/schema.sql` against the production database and seed the categories.

## 3. Deploy the Java backend on Render

Render does not provide a native Java runtime, so this project includes `backend/Dockerfile`; Render recommends Docker for JVM languages.

Create **New → Web Service** and connect the GitHub repository.

Set:
- Root Directory: `backend`
- Runtime/Language: `Docker`
- Dockerfile Path: `backend/Dockerfile` (if Render asks for a repo-relative path)

Environment variables:
- `DB_URL=jdbc:postgresql://YOUR_HOST/YOUR_DB?sslmode=require`
- `DB_USER=YOUR_DB_USER`
- `DB_PASSWORD=YOUR_DB_PASSWORD`
- `JWT_SECRET=` a long random secret
- `CORS_ORIGIN=https://YOUR-FRONTEND.vercel.app`

Render supplies `PORT`; the application is configured to use it.

After deployment, Render gives you a URL similar to:
`https://civicresolve-api.onrender.com`

Test:
`https://civicresolve-api.onrender.com/api/categories`

## 4. Deploy the React frontend on Vercel

Import the same GitHub repository into Vercel. Set the project root to `frontend`.

Build command:
`npm run build`

Output directory:
`dist`

Add this environment variable:
`VITE_API_URL=https://YOUR-RENDER-BACKEND.onrender.com/api`

Deploy. Vercel will give you a public URL like:
`https://civicresolve.vercel.app`

## 5. Important CORS step

After you know the Vercel URL, set Render's:
`CORS_ORIGIN=https://YOUR-FRONTEND.vercel.app`

Redeploy the backend.

## 6. Test from your phone

Open the Vercel URL on mobile data or Wi-Fi.

The phone talks directly to the hosted frontend and hosted backend. VS Code and your laptop do not need to be running.

## Local development

For local use, copy `frontend/.env.example` to `.env` and keep:
`VITE_API_URL=http://localhost:8080/api`

Then run the frontend normally with `npm run dev`.

## Production warning

Do not use the development JWT secret, local PostgreSQL credentials, or localhost API URL in production.

Also note that the current project still stores uploaded files locally; for a real production deployment, use S3-compatible object storage for attachments.

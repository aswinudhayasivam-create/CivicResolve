# CivicResolve — Professional Dashboard Edition

CivicResolve is a public complaint and grievance management system with a dark glassmorphism interface, role-based authentication, citizen workflows, authority operations, PostgreSQL persistence, and optional native C/C++ modules.

## Included working features

### Citizen
- Account registration and login
- JWT session restore
- Citizen dashboard with database-backed complaint metrics
- Structured complaint submission
- Category and priority selection
- Location/landmark capture
- Complaint tracking by tracking number
- Status timeline
- Account profile editing
- Notification preference storage
- Password change

### Authority / Admin
- Role-aware dashboard
- Live complaint metrics
- Resolution pipeline chart
- Priority distribution chart
- Search by title/tracking number
- Status filtering
- Complaint detail modal
- Citizen contact information in the protected authority view
- Full status history
- Status updates with timeline comments
- Authority self-assignment for complaint ownership
- CSV export of the currently loaded complaint dataset

### Public
- Database-backed service overview statistics
- Public complaint tracking using tracking number
- Sanitized public tracking response
- Responsive mobile navigation
- Dark glassmorphism UI with the existing monochrome/mint visual direction

## Security/data fixes

- `/api/auth/me` supports all authenticated roles instead of only citizens.
- Complaint APIs no longer serialize the JPA `User` entity, preventing password-hash exposure.
- Public tracking returns a safe DTO-style map rather than the full complaint entity.
- Complaint creation derives the citizen from the JWT identity.
- Status updates are validated server-side.
- Error responses preserve HTTP status codes and return safe messages.
- CORS is controlled through `CORS_ORIGIN`.
- Production startup rejects a missing/short JWT secret instead of silently using a weak default.
- Tracking numbers use cryptographically strong random characters rather than predictable timestamps.
- Account updates are authenticated and scoped to the JWT identity.

## Native modules

The repository keeps the C audit logger and C++ duplicate-similarity library. They compile independently with CMake. They are intentionally not wired into the live complaint request path because doing so safely would require a deployment-specific native integration layer.

## Local run

### Backend
```bash
cd backend
mvn spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm run dev
```

Set `frontend/.env` to:
```text
VITE_API_URL=http://localhost:8080/api
```

## Production

Use Vercel for the frontend, Render (or another Java host) for Spring Boot, and Neon/managed PostgreSQL for the database. Set:

Backend:
```text
DB_URL=...
DB_USER=...
DB_PASSWORD=...
JWT_SECRET=<long random production secret>
CORS_ORIGIN=https://civic-resolve-bay.vercel.app,https://civicresolve.vercel.app
```

Frontend:
```text
VITE_API_URL=https://civicresolve-uyct.onrender.com/api
```

Never commit real `.env` files or production credentials.

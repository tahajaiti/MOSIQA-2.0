# MOSIQA - Music Stream Application

A full-stack music streaming application with Angular frontend and Spring Boot backend.

## 🏗️ Architecture

```
MOSIQA-2/
├── frontend/          # Angular 21 application
│   ├── src/app/
│   │   ├── core/      # Services, models, API
│   │   ├── features/  # Library, Track pages
│   │   ├── layout/    # Main layout components
│   │   ├── shared/    # Reusable components
│   │   └── store/     # NgRx state management
│   └── Dockerfile
├── backend/           # Spring Boot 4.0.2 API
│   ├── src/main/java/com/kyojin/mosiqa/
│   │   ├── config/    # CORS configuration
│   │   ├── controller/# REST endpoints
│   │   ├── dto/       # Data Transfer Objects
│   │   ├── entity/    # JPA entities
│   │   ├── exception/ # Error handling
│   │   ├── mapper/    # MapStruct mappers
│   │   ├── repository/# JPA repositories
│   │   └── service/   # Business logic
│   └── Dockerfile
└── docker-compose.yml # Container orchestration
```

## 🚀 Quick Start

### Prerequisites
- Java 21+
- Node.js 22+
- Docker & Docker Compose (optional)

### Development Mode

**Backend:**
```bash
cd backend
./gradlew bootRun --args='--spring.profiles.active=dev'
```

**Frontend:**
```bash
cd frontend
npm install
npm start
```

Access:
- Frontend: http://localhost:4200
- Backend API: http://localhost:8080/api
- H2 Console: http://localhost:8080/h2-console

### Docker Mode

```bash
docker-compose up --build
```

Access:
- Frontend: http://localhost:4200
- Backend API: http://localhost:8080/api

## 📚 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/tracks | List all tracks |
| GET | /api/tracks/{id} | Get track by ID |
| POST | /api/tracks | Create track (multipart) |
| PUT | /api/tracks/{id} | Update track |
| DELETE | /api/tracks/{id} | Delete track |
| GET | /api/tracks/search?q= | Search tracks |
| GET | /api/tracks/category/{cat} | Filter by category |
| GET | /api/files/audio/{id} | Stream audio |
| GET | /api/files/cover/{id} | Get cover image |

## 🧪 Testing

**Backend:**
```bash
cd backend
./gradlew test
```

**Frontend:**
```bash
cd frontend
npm test
```

## 🛠️ Tech Stack

### Frontend
- Angular 21
- NgRx (State Management)
- TailwindCSS
- ng-icons/lucide

### Backend
- Spring Boot 4.0.2
- Java 21
- H2 Database (file-based)
- MapStruct
- Lombok

## 📁 Database

H2 file-based database stored in `./data/mosiqa-db`

Profiles:
- `dev`: File-based H2, debug logging
- `test`: In-memory H2, reduced logging

## 🔒 CORS

Configured to allow requests from:
- http://localhost:4200
- http://127.0.0.1:4200

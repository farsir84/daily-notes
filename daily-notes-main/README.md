# Daily Notes Application

A full-stack daily notes application with calendar view, featuring Angular frontend, Spring Boot backend, and PostgreSQL database.

## Features

- 📅 **Calendar View** - Interactive calendar with FullCalendar
- 📝 **Note Management** - Create, edit, and delete daily notes
- 🔐 **Authentication** - Secure JWT-based authentication
- 🎨 **Modern UI** - Beautiful Material Design interface
- 🐳 **Docker Ready** - Complete Docker Compose setup

## Tech Stack

### Frontend
- Angular 19 (latest stable version)
- Angular Material
- FullCalendar
- TypeScript
- SCSS

### Backend
- Spring Boot 3.2+
- Java 21
- Spring Security with JWT
- Spring Data JPA
- PostgreSQL 16

### Deployment
- Docker
- Docker Compose
- Nginx

## Prerequisites

- Docker and Docker Compose installed on your system

## Quick Start

1. **Clone or navigate to the project directory:**
   ```bash
   cd daily-notes
   ```

2. **Build and start all services:**
   ```bash
   docker-compose up --build
   ```

3. **Access the application:**
   - Open your browser and navigate to: `http://localhost:8088`

4. **Login with default credentials:**
   - Username: `denis`
   - Password: `denis`

## Project Structure

```
daily-notes/
├── backend/              # Spring Boot application
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── frontend/             # Angular application
│   ├── src/
│   ├── package.json
│   ├── nginx.conf
│   └── Dockerfile
└── docker-compose.yml    # Docker Compose configuration
```

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login

### Notes
- `GET /api/notes` - Get all notes for authenticated user
- `GET /api/notes/range?startDate={date}&endDate={date}` - Get notes by date range
- `POST /api/notes` - Create a new note
- `PUT /api/notes/{id}` - Update a note
- `DELETE /api/notes/{id}` - Delete a note

## Development

### Backend Development
```bash
cd backend
mvn spring-boot:run
```

### Frontend Development
```bash
cd frontend
npm install
npm start
```

## Docker Services

- **PostgreSQL** - Database (internal port 5432)
- **Backend** - Spring Boot API (internal port 8080)
- **Frontend** - Angular + Nginx (exposed on port 8088)

## Stopping the Application

```bash
docker-compose down
```

To remove volumes as well:
```bash
docker-compose down -v
```

## Notes

- All services are connected via a custom Docker network
- PostgreSQL data is persisted in a Docker volume
- The frontend uses Nginx as a reverse proxy to route API calls to the backend
- Default user is created automatically on first startup

## License

This project is provided as-is for educational purposes.

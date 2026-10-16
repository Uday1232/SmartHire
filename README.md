# 🧠 SmartHire — Interview Intelligence System

SmartHire is a full-stack web application developed to streamline and digitize the end-to-end hiring process. The project focuses on managing interview workflows through a centralized platform, reducing manual effort, and improving hiring efficiency using AI-powered tools.

Built as a hands-on software development project, SmartHire demonstrates practical implementation of frontend development, backend API development, database integration, authentication, and AI integration.

---

## 🎯 Project Objective

The objective of SmartHire is to provide a centralized system for managing essential hiring activities, including candidate management, hiring drives, interview rounds, resume analysis, question generation, and candidate ranking.

The project emphasizes modular development, RESTful API design, database management, AI integration with Google Gemini, and secure role-based access to application resources.

---

## ✨ Key Features

- **Hiring Drive Management:** Create and manage hiring drives with configurable interview rounds.
- **Candidate Management:** Bulk-import candidates via Excel upload and track their status through the pipeline.
- **AI Resume Intelligence:** Extract structured candidate profile data from PDF and DOCX resumes using Google Gemini.
- **AI Question Generation:** Automatically generate interview questions tailored to round type and difficulty level.
- **Live Interview Sessions:** Conduct interviews with real-time question evaluation and verdicts.
- **Candidate Ranking:** AI-assisted ranking of candidates at the end of a hiring drive.
- **Multi-Role Access:** Super Admin, Company Admin, and Interviewer roles with role-based access control.
- **Authentication:** JWT-based authentication with optional Google OAuth2 sign-in.
- **Company Management:** Super Admin can manage multiple companies and their admins.

---

## 🛠️ Technology Stack

| Category | Technologies |
|---|---|
| Frontend | React.js, JavaScript, HTML, CSS |
| UI Library | Material UI (MUI v5) |
| Backend | Java 21, Spring Boot 4.x |
| AI Integration | Google Gemini (via Spring AI 2.0) |
| Database | PostgreSQL 16 |
| Database Migrations | Flyway |
| Authentication | JSON Web Token (JWT), Google OAuth2 |
| API Development | REST APIs |
| File Parsing | Apache POI (Excel), Apache PDFBox (PDF) |
| Development Tools | Git, GitHub, Postman |
| Containerization | Docker, Docker Compose |

---

## 🏗️ Application Architecture

SmartHire follows a client-server architecture:

- **Frontend Layer:** Handles the user interface and user interactions, organized by role-specific pages and layouts.
- **Backend Layer:** Processes API requests, applies business logic, manages application workflows, and communicates with Google Gemini for AI operations.
- **Database Layer:** Stores and retrieves hiring-related data using PostgreSQL, with schema managed by Flyway migrations.
- **Authentication Layer:** Protects applicable routes and resources through JWT token-based authentication and Google OAuth2.
- **AI Layer:** Integrates Google Gemini to power resume extraction, question generation, and candidate ranking.

---

## 📂 Project Structure

```
SmartHire/
├── smarthire/                        # Spring Boot backend
│   ├── src/main/java/com/smarthire/
│   │   ├── controller/               # REST controllers
│   │   ├── service/                  # Business logic and AI services
│   │   ├── entity/                   # JPA entities
│   │   ├── repository/               # Spring Data JPA repositories
│   │   ├── security/                 # JWT, OAuth2, filters
│   │   ├── dto/                      # Request and response DTOs
│   │   ├── enums/                    # Status, role, round-type enums
│   │   └── parser/                   # Excel candidate file parser
│   ├── Dockerfile
│   └── pom.xml
├── smarthire-frontend/               # React frontend
│   ├── src/
│   │   ├── pages/                    # admin / interviewer / superadmin / auth pages
│   │   ├── components/               # Role-based layouts
│   │   ├── api/                      # Axios API modules
│   │   ├── context/                  # Auth context
│   │   └── routes/                   # Protected route wrapper
│   ├── Dockerfile
│   └── package.json
├── docker-compose.yml                # Orchestrates DB, backend, and frontend
├── .env.example                      # Environment variable template
└── README.md
```

---

## ⚙️ Local Setup

### Prerequisites

- Java 21 JDK
- Node.js 20 or later and npm
- PostgreSQL 16
- Git
- Docker Desktop (optional, for containerized setup)
- Postman (optional, for API testing)

---

### 1. Clone the Repository

```bash
git clone https://github.com/TechRitesh27/SmartHire.git
cd SmartHire
```

---

### 2. Set Up the Backend

Navigate to the backend folder:

```powershell
cd smarthire
```

Set the required environment variables in your terminal:

```powershell
$env:DB_URL         = "jdbc:postgresql://localhost:5432/smarthire"
$env:DB_USER        = "your_db_user"
$env:DB_PASSWORD    = "your_db_password"
$env:JWT_SECRET     = "replace_with_a_long_random_secret_min_32_chars"
$env:JWT_EXPIRATION = "86400000"
$env:GEMINI_API_KEY = "your_gemini_api_key"
```

Create the `smarthire` database in PostgreSQL. Flyway will automatically apply the schema on first startup.

---

### 3. Run the Backend

```powershell
.\mvnw.cmd spring-boot:run
```

Wait for Spring Boot to report that it has started. Keep this terminal open.

---

### 4. Set Up the Frontend

Open a second terminal and navigate to the frontend directory:

```powershell
cd smarthire-frontend
```

Create your local environment file from the example:

```powershell
Copy-Item .env.example .env
```

Configure the `.env` file:

```dotenv
REACT_APP_API_BASE_URL=http://localhost:8080/api/v1
```

Install dependencies and start the frontend:

```powershell
npm install
npm start
```

Open [http://localhost:3000](http://localhost:3000) in your browser.

---

### 5. First-Time Setup

Open [http://localhost:3000/setup](http://localhost:3000/setup) to create the initial **Super Admin** account. This route is only available while no Super Admin exists.

After that, use `/login` for all subsequent sign-ins.

---

## 🐳 Docker Compose Setup (Recommended)

The simplest way to run SmartHire is with Docker Compose, which starts PostgreSQL, the backend, and the frontend together.

```powershell
Copy-Item .env.example .env
notepad .env
docker compose up --build
```

| Service | URL |
|---|---|
| Frontend | http://localhost:3000 |
| Backend API | http://localhost:8080/api/v1 |
| PostgreSQL | localhost:5432 |

To stop the services:

```powershell
docker compose down
```

---

## 🔐 Environment Configuration

The application uses environment variables for configuration, including:

| Variable | Description |
|---|---|
| `DB_HOST` | Database hostname (`postgres` for Docker, `localhost` for local dev) |
| `DB_PORT` | PostgreSQL port (default `5432`) |
| `DB_NAME` | Database name |
| `DB_USER` | Database username |
| `DB_PASSWORD` | Database password |
| `JWT_SECRET` | Signing key — minimum 32 characters |
| `JWT_EXPIRATION` | Token lifetime in milliseconds (`86400000` = 24 hours) |
| `GEMINI_API_KEY` | Google Gemini API key — required for AI features |
| `GOOGLE_CLIENT_ID` | Google OAuth2 client ID — optional, for Google sign-in |
| `GOOGLE_CLIENT_SECRET` | Google OAuth2 client secret — optional, for Google sign-in |
| `REACT_APP_API_BASE_URL` | Backend API base URL visible from the browser |

The `.env.example` file provides a template for local setup. Actual credentials and secrets should remain in the ignored `.env` file and must not be committed to GitHub.

---

## 🧪 Testing and Validation

During development, the application can be tested by validating:

- Backend API request and response handling
- Database connectivity and Flyway migration execution
- Authentication flows (JWT and Google OAuth2)
- Hiring drive and round creation workflows
- Candidate import via Excel upload
- AI resume extraction and question generation
- Interview session execution and evaluation
- Frontend-backend integration

Use Postman or the testing tools configured in the project to validate the implemented functionality.

---

## 📚 Learning Outcomes

Through this project, I have worked on practical full-stack development concepts, including:

- Building RESTful APIs with Java, Spring Boot, and Spring Security
- Integrating PostgreSQL with JPA and managing schema migrations with Flyway
- Connecting frontend components to backend APIs using Axios
- Implementing JWT-based and OAuth2 token authentication
- Integrating Google Gemini AI for real-world AI-powered features
- Parsing PDF, DOCX, and Excel files for data extraction
- Organizing application code into maintainable, modular layers
- Using Docker and Docker Compose for containerized deployment
- Using Git and GitHub for version control
- Managing environment variables and development configuration

---

## 🚀 Future Improvements

Potential improvements include:

- Enhanced analytics and hiring dashboards
- Email notifications for interview scheduling
- QR-based candidate check-in
- Improved role-based access granularity
- Support for video interview integration
- Automated hiring reports and exports

These enhancements can be introduced incrementally as the project evolves.

---

## 👨‍💻 Developer

**Uday Badhe**
Aspiring Full-Stack Developer | Java | Spring Boot | React.js | PostgreSQL | AI Integration

GitHub: [@TechRitesh27](https://github.com/TechRitesh27)

---

*SmartHire — A practical full-stack development project focused on simplifying and digitizing the interview process.*

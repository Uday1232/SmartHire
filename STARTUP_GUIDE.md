# SmartHire Startup Guide

This guide explains how to configure and run SmartHire on a Windows development machine. It covers the recommended Docker setup and a separate local development setup, including optional Google Gemini and Google sign-in credentials.

## 1. What you need

Install the tools required for your chosen setup:

| Tool | Docker setup | Local development |
|---|---:|---:|
| Git | Required | Required |
| Docker Desktop with Docker Compose | Required | Optional |
| Java 21 JDK | Not required on the host | Required |
| Node.js 20 or later and npm | Not required on the host | Required |
| PostgreSQL 16 | Started by Docker | Required, unless you use Docker just for the database |

For local development, confirm the command-line tools are available in PowerShell:

```powershell
java -version
node --version
npm --version
```

The backend includes the Maven Wrapper, so a separate Maven installation is not necessary.

## 2. Get the project

Open PowerShell and clone the repository, or open a PowerShell terminal in your existing checkout:

```powershell
git clone https://github.com/TechRitesh27/SmartHire.git
Set-Location SmartHire
```

Run the remaining commands from the repository root unless the instructions say otherwise.

## 3. Choose how to run SmartHire

- **Docker Compose** is the simplest option. It builds and runs PostgreSQL, the backend, and the frontend together.
- **Local development** runs the backend and frontend directly on your computer. You can use an existing PostgreSQL installation or start only the database through Docker.

## 4. Optional: create Google credentials

You can run the application with email and password without configuring Google sign-in. A Gemini API key is needed for AI-powered resume extraction and question generation. Google OAuth credentials are needed only for the **Continue with Google** login button.

### 4.1 Create a Gemini API key

1. Sign in to [Google AI Studio](https://aistudio.google.com/).
2. Open the API key page: [Get API key](https://aistudio.google.com/app/apikey).
3. Select or create a Google Cloud project if prompted.
4. Choose **Create API key** and copy the key.
5. Store it in your local environment configuration as `GEMINI_API_KEY`.

Keep this key private. Do not put it in frontend code, screenshots, or a committed file. Without it, application startup and non-AI features can still work, but AI features that call Gemini will fail.

### 4.2 Create Google OAuth credentials

This is optional and is only required for Google sign-in.

1. Sign in to the [Google Cloud Console](https://console.cloud.google.com/).
2. Select or create a Google Cloud project.
3. Open **Google Auth Platform** (or **APIs & Services → OAuth consent screen**) and configure the consent screen. Choose the appropriate audience and provide the application name and contact details requested by Google.
4. If the app is in testing mode, add the Google accounts you will use under **Test users**.
5. Open **APIs & Services → Credentials**, choose **Create credentials → OAuth client ID**, and select **Web application**.
6. Add this authorized redirect URI for local development:

   ```text
   http://localhost:8080/login/oauth2/code/google
   ```

7. Create the client and copy its **Client ID** and **Client secret** into `GOOGLE_CLIENT_ID` and `GOOGLE_CLIENT_SECRET`.
8. If you use a different host or port, register the matching redirect URI in Google Cloud and configure the application with the same URL.

Google sign-in does **not** automatically create a SmartHire account. The email address returned by Google must already exist as a SmartHire user. First create that user in SmartHire with the same email address; then the matching Google account can sign in.

Never commit the client secret. For a deployed environment, register that environment's exact callback URL and use its own credentials and secret storage.

## 5. Option A — Run everything with Docker Compose

### 5.1 Configure the root environment file

If you do not already have a root `.env` file, copy the template. This command leaves an existing `.env` untouched:

```powershell
if (-not (Test-Path .env)) { Copy-Item .env.example .env }
notepad .env
```

Set the values in `.env`. For a local Docker installation, the configuration should follow this pattern:

```dotenv
DB_HOST=postgres
DB_PORT=5432
DB_NAME=smarthire
DB_USER=smarthire
DB_PASSWORD=replace_with_a_private_database_password

JWT_SECRET=replace_with_a_long_random_secret_at_least_32_characters
JWT_EXPIRATION=86400000

GEMINI_API_KEY=
GOOGLE_CLIENT_ID=
GOOGLE_CLIENT_SECRET=

REACT_APP_API_BASE_URL=http://localhost:8080/api/v1
```

Replace the database password and JWT secret with private values. Add the Gemini and OAuth credentials if you want those integrations. `JWT_EXPIRATION=86400000` means the token lasts 24 hours, in milliseconds.

`DB_HOST=postgres` is important for Docker: it is the database service name on Docker's internal network. Do not change it to `localhost` for the Compose setup.

### 5.2 Build and start the services

From the repository root:

```powershell
docker compose up --build
```

The first startup may take several minutes while Docker downloads base images and builds the applications. Leave this terminal open to see logs. Use `Ctrl+C` to stop the attached services.

To start the services in the background instead:

```powershell
docker compose up --build -d
docker compose ps
```

### 5.3 Open the application

- SmartHire frontend: [http://localhost:3000](http://localhost:3000)
- Backend API base URL: `http://localhost:8080/api/v1`
- PostgreSQL port on your computer: `5432` by default

Flyway applies the database migrations when the backend connects to the database for the first time.

### 5.4 Stop Docker services

```powershell
docker compose down
```

This stops and removes the containers but preserves the database volume. **Do not use `docker compose down -v` unless you intentionally want to permanently delete the Compose database and all data stored in it.**

## 6. Option B — Run the backend and frontend locally

In this option, PostgreSQL must be available at `localhost:5432`. You can install PostgreSQL 16, or use Docker just for PostgreSQL as described below.

### 6.1 Start PostgreSQL with Docker (optional)

If Docker is installed and you do not already have a PostgreSQL database, start a database container from the repository root:

```powershell
docker run --name smarthire-postgres `
  -e POSTGRES_DB=smarthire `
  -e POSTGRES_USER=smarthire `
  -e POSTGRES_PASSWORD=replace_with_a_private_database_password `
  -p 5432:5432 `
  -v smarthire-postgres-data:/var/lib/postgresql/data `
  -d postgres:16-alpine
```

Use the same password in the backend environment in the next step. The named Docker volume preserves the database between container restarts.

For an installed PostgreSQL server, create a database and user using your normal PostgreSQL administration tool (for example, pgAdmin). The database name, username, and password must match the backend settings below.

### 6.2 Configure and start the backend

Open a PowerShell terminal, move into the backend directory, and set the database and JWT environment variables. Replace the password and secret with your own values:

```powershell
Set-Location .\smarthire

$env:DB_URL = "jdbc:postgresql://localhost:5432/smarthire"
$env:DB_USER = "smarthire"
$env:DB_PASSWORD = "replace_with_a_private_database_password"
$env:JWT_SECRET = "replace_with_a_long_random_secret_at_least_32_characters"
$env:JWT_EXPIRATION = "86400000"
```

To enable Gemini and Google sign-in, also set the corresponding variables in this same terminal before starting the backend:

```powershell
$env:GEMINI_API_KEY = "your_gemini_api_key"
$env:SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID = "your_google_client_id"
$env:SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_SECRET = "your_google_client_secret"
$env:APP_FRONTEND_URL = "http://localhost:3000"
```

The OAuth variables are optional for password-based login. Keep them set in the terminal that launches the backend; environment variables set in one PowerShell process are not automatically shared with another.

Start the backend:

```powershell
.\mvnw.cmd spring-boot:run
```

Wait for the Spring Boot application to report that it has started. Flyway will create or update the database schema during startup. Keep this terminal open.

### 6.3 Configure and start the frontend

Open a **second** PowerShell terminal at the repository root:

```powershell
Set-Location .\smarthire-frontend
if (-not (Test-Path .env)) { Copy-Item .env.example .env }
notepad .env
```

Make sure the frontend `.env` contains:

```dotenv
REACT_APP_API_BASE_URL=http://localhost:8080/api/v1
```

Install the frontend dependencies and start the development server:

```powershell
npm install
npm start
```

The frontend should open at [http://localhost:3000](http://localhost:3000). Keep this terminal open while using the application.

The root `.env` file is used by Docker Compose. It is **not** automatically loaded when starting the backend with the Maven Wrapper. The frontend's `.env` is separate and is read by the React development server when it starts.

## 7. First-time account and company setup

1. Open [http://localhost:3000/setup](http://localhost:3000/setup).
2. Create the initial **Super Admin** account. This is a one-time setup: the backend permits registration through this setup flow only while no Super Admin exists.
3. After registration, you should be signed in and taken to the company administration area.
4. Create a company.
5. Create a **Company Admin** for that company.
6. Sign out and sign in as the Company Admin to manage hiring drives and create interviewer accounts.
7. Create interviewers using the email addresses they will use for login. To use Google sign-in, the Google account email must match the already-created SmartHire user email.

For later visits, open [http://localhost:3000/login](http://localhost:3000/login) and use the account created for you.

## 8. Quick verification

- The frontend loads at `http://localhost:3000`.
- The backend starts without database connection or migration errors.
- You can create the first Super Admin at `/setup`.
- The Super Admin can create a company and Company Admin.
- AI operations work only if a valid Gemini API key is configured.
- Google sign-in works only if OAuth is configured in both Google Cloud and the backend, and the Google email already belongs to a SmartHire user.

## 9. Troubleshooting

### Backend cannot connect to PostgreSQL

- Confirm PostgreSQL is running and listening on port `5432`.
- For local backend startup, verify `DB_URL` points to `localhost`, and that `DB_USER` and `DB_PASSWORD` match the database.
- For Docker Compose, verify `DB_HOST=postgres`; Compose services use the service name rather than `localhost`.
- Check backend logs with `docker compose logs -f backend` when using Docker.

### Frontend cannot reach the backend

- Confirm the backend is listening on port `8080`.
- Confirm the frontend API base URL is `http://localhost:8080/api/v1`.
- If you changed the frontend `.env`, restart the frontend development server. React reads this setting when it starts.

### AI request fails

- Confirm `GEMINI_API_KEY` is a valid API key and is set in the environment of the backend.
- Restart the backend after changing environment variables.
- AI services are not required for basic login and application startup, but resume extraction and question generation depend on them.

### Google login fails

- Confirm the Google OAuth client ID and secret are configured on the backend.
- Confirm Google Cloud has the exact callback URI `http://localhost:8080/login/oauth2/code/google`.
- If the OAuth app is in testing mode, add the Google account as a test user.
- Confirm a SmartHire user already exists with the same email address as the Google account.
- When running locally, ensure `APP_FRONTEND_URL` is `http://localhost:3000`.

### Setup says a Super Admin already exists

The initial setup has already been completed for the connected database. Use the login page rather than registering again. Do not delete the database to bypass this check unless it is disposable development data.

### Docker reports the backend as unhealthy

The Compose file checks `http://localhost:8080/actuator/health`. If Docker reports the backend as unhealthy, inspect the backend logs first:

```powershell
docker compose logs --tail 200 backend
```

The project may not expose the Actuator health endpoint in the current build, so this health-check status alone does not prove that the application failed to start. Check the logs and test the frontend/API before diagnosing it as a startup failure.

## 10. Keep credentials and data safe

- Never commit `.env` files or paste API keys, passwords, JWT secrets, or OAuth client secrets into public issue reports.
- Use different credentials for development and production.
- Do not use placeholder JWT secrets or database passwords for a shared or deployed environment.
- Treat candidate information, resumes, and interview evaluations as sensitive data.
- Be careful before deleting Docker volumes: they may contain your local database and candidate data.

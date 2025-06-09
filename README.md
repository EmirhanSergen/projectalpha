# Project Alpha

This project consists of a Spring Boot backend and a React/Vite frontend. Both parts rely on environment variables for configuration.

## Backend environment variables
Set the following variables so the backend can connect to Supabase and the database:

- `SUPABASE_URL` – Base URL of your Supabase project.
- `SUPABASE_API_KEY` – The Supabase anon or service API key.
- `SUPABASE_SECRET_KEY` – The Supabase service role key.
- `ADMIN_USERNAME` – Username for the default admin account.
- `ADMIN_PASSWORD` – Password for the default admin account.
- `DB_USERNAME` – Database username.
- `DB_PASSWORD` – Database password.

The Spring Boot application reads these values from the environment. You can export them in your shell before running the application, or place them in a `.env` file and use a tool such as [`dotenv`](https://github.com/cdimascio/dotenv-java) or your IDE to load them.

## Frontend environment variables
The Vite frontend expects the following variables (all must start with the `VITE_` prefix):

- `VITE_SUPABASE_URL` – Same as `SUPABASE_URL` but for the frontend.
- `VITE_SUPABASE_ANON_KEY` – Supabase anon key used by the frontend.
- `VITE_API_BASE_URL` – Base URL of the backend API.

Create a `frontend/.env` file or export the variables in your shell so that Vite can load them when you run `npm run dev` or build the project.

## Example `.env` files
```bash
# backend/.env or system environment
SUPABASE_URL=https://<your-supabase-url>
SUPABASE_API_KEY=...
SUPABASE_SECRET_KEY=...
ADMIN_USERNAME=admin
ADMIN_PASSWORD=secret
DB_USERNAME=postgres
DB_PASSWORD=password
```

```bash
# frontend/.env
VITE_SUPABASE_URL=https://<your-supabase-url>
VITE_SUPABASE_ANON_KEY=...
VITE_API_BASE_URL=http://localhost:8080
```

After setting the variables, start the backend and frontend normally. The applications will read the configuration from the environment.

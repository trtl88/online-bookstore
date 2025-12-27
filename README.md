<div align="center">

# Online Bookstore

**Spring Boot 3 (Java 17) + MySQL** — REST APIs + built-in static web UI.

![Java](https://img.shields.io/badge/Java-17-informational)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.x-brightgreen)
![Database](https://img.shields.io/badge/Database-MySQL-blue)
![Platform](https://img.shields.io/badge/Platform-Windows%20friendly-9cf)

</div>

A full-stack (server-served UI + REST) online bookstore implemented with **Spring Boot 3**, a **MySQL** schema auto-initialized on startup, and static HTML/CSS/JS pages hosted directly by the backend.

> This repository currently contains the backend application under `backend/`.

---

## Quick links

- **Run (Windows / PowerShell):** `powershell -ExecutionPolicy Bypass -File backend\run-site.ps1`
- **Smoke test:** `powershell -ExecutionPolicy Bypass -File backend\run-smoke.ps1`
- **Open:** http://localhost:8080/

---

## Contents

- [Highlights](#what-you-get)
- [Tech stack](#tech-stack)
- [Architecture overview](#architecture-overview)
- [Quick start (Windows)](#quick-start-windows)
- [Configuration](#configuration)
- [Scripts](#scripts)
- [Web UI pages](#web-ui-pages)
- [REST API overview](#rest-api-overview)
- [Database schema & seed data](#database-schema--seed-data)
- [Testing](#testing)
- [Project layout](#project-layout)
- [Troubleshooting](#troubleshooting)
- [Security notes](#security-notes)

---

## What you get

### Storefront

- Browse books, view details, search by query, filter by category
- Shopping cart: add, view, remove, clear
- Checkout flow (mock credit-card validation)
- Order history + order details

### Admin / Manager capabilities

- Add / update books (validation + ISBN normalization)
- Publisher support (including creation if missing during add-book)
- Automatic **restock workflow** using a MySQL trigger:
  - When `stock_quantity` drops below `threshold`, a **PENDING** record is inserted into `publisher_orders`
  - Manager can confirm restock orders (increments stock)
- Reporting endpoints:
  - last month sales
  - sales by date
  - top customers
  - top books
  - restock count

### Seeded demo data

The project includes realistic demo data for:
- publishers, authors, books, author↔book relationships
- multiple users (including an admin)
- historical orders + order items
- some shopping-cart items
- sample publisher restock orders

---

## Tech stack

| Area | Tech |
|------|------|
| Language | Java 17 |
| Framework | Spring Boot 3.5.x |
| Web | Spring Web (REST controllers) |
| Data access | SQL-centric repositories (with Spring Data JPA dependency present) |
| UI | Static HTML/CSS/JS served from `src/main/resources/static/` |
| Database | MySQL (`mysql-connector-j`) |
| Build | Maven Wrapper (`backend/mvnw.cmd`) |

---

## Architecture overview

```
Browser (static UI)
   |
   |  fetch('/api/...')
   v
Spring Boot app
  controllers/  -> services/  -> repositories/  -> MySQL
  static/ pages -> REST API   -> SQL queries    -> schema.sql + data.sql
```

- **Web UI** is served as static assets from Spring Boot: `backend/src/main/resources/static/`
- Those HTML pages call backend REST endpoints under `/api/...`
- API layer: `controllers/` → business logic: `services/` → SQL repositories: `repositories/`
- Database initialization runs automatically on startup:
  - `backend/src/main/resources/schema.sql`
  - `backend/src/main/resources/data.sql`

---

## Quick start (Windows)

### 1) Prerequisites

Install / ensure you have:

| Requirement | Notes |
|------------|-------|
| Java 17 (JDK) | Required to run Spring Boot |
| MySQL Server | Expected on `localhost:3306` (XAMPP works great) |

### 2) Start MySQL

- Start the MySQL service (XAMPP Control Panel → **Start** MySQL)

### 3) Configure DB credentials (if needed)

Default configuration expects:

- URL: `jdbc:mysql://localhost:3306/online_bookstore?createDatabaseIfNotExist=true`
- User: `root`
- Password: *(empty)*

If your MySQL setup differs, update:
- `backend/src/main/resources/application.properties`

### 4) Run the backend

Option A — run with the provided PowerShell script:

```powershell
powershell -ExecutionPolicy Bypass -File backend\run-site.ps1
```

Option B — run from the backend folder using Maven Wrapper:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

### 5) Open the app

- Storefront: http://localhost:8080/ (or `http://localhost:8080/index.html`)

---

## Configuration

Key settings live in:
- `backend/src/main/resources/application.properties`

Highlights:
- **MySQL** connection details (URL, username, password)
- SQL init mode:
  - `spring.sql.init.mode=always`
  - Statement separator configured to `;;` to match provided SQL files
- Hibernate DDL generation is disabled:
  - `spring.jpa.hibernate.ddl-auto=none`

To run on a different port, add:

```properties
server.port=9090
```

---

## Scripts

### Run the site

- `backend/run-site.ps1`
  - Stops existing backend processes if found
  - Builds with `mvn package -DskipTests`
  - Starts the produced JAR (detached)

### Smoke test (API)

- `backend/run-smoke.ps1`
  - Calls a minimal API flow:
    - login → add-to-cart → get-cart → checkout → order history/details

Run it after the server is up:

```powershell
powershell -ExecutionPolicy Bypass -File backend\run-smoke.ps1
```

---

## Web UI pages

These are served directly from Spring Boot under `/`:

- `index.html` — storefront / browse
- `book_details.html` — book detail page
- `cart.html` — shopping cart
- `checkout.html` — checkout flow
- `login.html`, `signup.html`, `edit_profile.html`
- Admin/manager pages:
  - `admin_dashboard.html`
  - `add_book.html`, `edit_book.html`
  - `manage_users.html`
  - `confirm_orders.html`
  - `reports.html`

Static assets:
- `assets/css/` (styles)
- `assets/js/` (API calls + UI logic)
- `assets/img/` (images)

---

## REST API overview

Base URL (local): `http://localhost:8080`

### Books (`/api/books`)

| Method | Path | Purpose |
|--------|------|---------|
| GET | `/api/books` | List all books |
| GET | `/api/books/{isbn}` | Book details |
| GET | `/api/books/search?query=...&category=...` | Search (+ optional category) |
| GET | `/api/books/category/{category}` | List by category |
| GET | `/api/books/publishers` | Publisher names |
| POST | `/api/books/add` | Add a book (admin) |
| PUT | `/api/books/update` | Update a book (admin) |

### Cart (`/api/cart`)

| Method | Path | Purpose |
|--------|------|---------|
| POST | `/api/cart/add?username=...&isbn=...&quantity=...` | Add item |
| GET | `/api/cart/{username}` | View cart |
| DELETE | `/api/cart/remove?username=...&isbn=...` | Remove item |
| DELETE | `/api/cart/clear?username=...` | Clear cart |

### Orders (`/api/orders`)

| Method | Path | Purpose |
|--------|------|---------|
| POST | `/api/orders/checkout?username=...&cc=...&expiry=...` | Checkout |
| GET | `/api/orders/history/{username}` | Order history |
| GET | `/api/orders/details/{orderId}` | Order line-items |

### Users (`/api/users`)

| Method | Path | Purpose |
|--------|------|---------|
| POST | `/api/users/signup` | Register |
| POST | `/api/users/login` | Login (returns a `User` object or `null`) |
| PUT | `/api/users/profile` | Update profile |

Manager features:

| Method | Path | Purpose |
|--------|------|---------|
| GET | `/api/users/customers` | List customers |
| PUT | `/api/users/promote/{username}` | Promote user to manager |

### Manager — publisher restock orders (`/api/manager/orders`)

| Method | Path | Purpose |
|--------|------|---------|
| GET | `/api/manager/orders/pending` | View pending restock orders |
| POST | `/api/manager/orders/confirm/{orderId}` | Confirm restock (increments stock) |

### Reports (`/api/reports`)

| Method | Path | Returns |
|--------|------|---------|
| GET | `/api/reports/sales/last-month` | Currency-formatted string |
| GET | `/api/reports/sales/date?date=YYYY-MM-DD` | Currency-formatted string |
| GET | `/api/reports/top-customers` | List |
| GET | `/api/reports/top-books` | List |
| GET | `/api/reports/restock-count?isbn=...` | Integer |

### Server info

| Method | Path | Purpose |
|--------|------|---------|
| GET | `/api/server/info` | Simple uptime metadata |

---

## Database schema & seed data

- Schema: `backend/src/main/resources/schema.sql`
  - Creates core tables: `book`, `author`, `book_authors`, `users`, `orders`, `order_items`, `shopping_cart`, `publisher_orders`
  - Defines triggers:
    - `before_book_update` prevents negative stock
    - `after_book_update` auto-creates restock orders when stock drops below threshold

- Seed data: `backend/src/main/resources/data.sql`

Demo accounts included:

| Username | Password | Notes |
|----------|----------|-------|
| `admin` | `adminpass` | Admin user |
| `john` | `pass` | Used by `run-smoke.ps1` |

---

## Testing

From `backend/`:

```powershell
.\mvnw.cmd test
```

Or build a runnable JAR:

```powershell
.\mvnw.cmd package
java -jar target\backend-0.0.1-SNAPSHOT.jar
```

---

## Project layout

- `backend/` — Spring Boot application
  - `src/main/java/com/trtl88/backend/`
    - `controllers/` — REST endpoints
    - `services/` — business logic
    - `repositories/` — SQL repositories
    - `models/` — domain models / DTOs
  - `src/main/resources/`
    - `application.properties` — runtime config
    - `schema.sql` / `data.sql` — DB initialization
    - `static/` — UI (HTML/CSS/JS)

---

## Troubleshooting

### App starts but pages don’t load data

- Ensure MySQL is running and reachable at `localhost:3306`
- Confirm credentials in `backend/src/main/resources/application.properties`
- Check that the DB `online_bookstore` exists (or can be created)

### SQL init fails on startup

- Verify the configured statement separator (`spring.sql.init.separator=;;`) matches the SQL files
- Ensure MySQL user has permission to create databases/tables

### Port already in use

- Stop any existing process bound to `8080`, or set `server.port` in `application.properties`

---

## Security notes

This project is a learning/demo-style application.

- Passwords in seed data are **plain text** and the login flow compares plain strings.
- Most endpoints are not protected by authentication/authorization.
- Credit card validation is intentionally minimal (length check only) and should not be used as-is for real payments.

---

## License

No license file is currently included in this repository. If you plan to publish or distribute this project, consider adding an explicit license.

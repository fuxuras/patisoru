# Project State & Developer Handover

**ATTENTION NEXT AGENT/DEVELOPER:** This document is not a log. It is a living document that must be updated to reflect the **current state** of the project after any session that introduces significant changes.

**Your Responsibility:**
-   Before ending your session, update this file if you have:
    -   Changed the core architecture (e.g., added a new layer, changed DI).
    -   Added a major new feature or endpoint.
    -   Added or changed a key dependency (e.g., a new database, a new library).
    -   Established a new coding pattern or convention.
-   Do **not** append changes like a log. Edit the relevant sections to describe how the project looks **now**.

---

## 1. Project Overview

This is a full-stack social media application called "Patisoru". It features a Java/Spring Boot backend providing a REST API and a modern Astro/React frontend. The project is containerized for consistent deployment.

-   **Backend:** Java 17, Spring Boot 3
-   **Frontend:** Astro, React, Tailwind CSS
-   **Database:** PostgreSQL
-   **Cache:** Redis (for JWTs or other caching purposes)
-   **Deployment:** Docker

## 2. Backend Architecture: Controller -> Service -> Repository

The backend (`core/`) follows a classic three-layer architecture to ensure a clean separation of concerns.

-   **`controllers`:** Handles all HTTP request/response logic. It is responsible for receiving requests, validating input (via DTOs), calling the appropriate service method, and formatting the HTTP response. It is the entry point to the API.

-   **`services`:** Contains all business logic. This layer is completely unaware of the HTTP context. It orchestrates data from repositories and other services to perform application-specific operations (e.g., creating a post, verifying a user).

-   **`repositories`:** Manages all database interactions using Spring Data JPA. These are interfaces that extend `JpaRepository`, providing standard CRUD operations and the ability to define custom queries.

-   **`entities`:** Defines the JPA entities that are mapped to the PostgreSQL database tables. Key entities include `User`, `Post`, `Comment`, and `Like`. A soft-delete pattern is implemented on the `Post` entity.

-   **`dto` (Data Transfer Objects):** Simple objects used to transfer data between the client and the controllers. This prevents exposing internal entity structures directly to the API. Mapping between DTOs and entities is handled by MapStruct (`DtoMapper.java`).

## 3. Frontend Architecture

The frontend (`frontend/`) is built with [Astro](https://astro.build/) and uses [React](https://react.dev/) for interactive UI components (islands).

-   **Astro Islands Architecture:** Interactive components (React components) are explicitly loaded on the client-side using Astro's `client:*` directives (e.g., `client:load`), allowing the majority of the page to remain static and fast.
-   **Pages (`src/pages/`):** Astro components that define the routes of the application (e.g., `index.astro`, `login.astro`, `profile.astro`).
-   **Components (`src/components/`):**
    -   **React (`src/components/react/`):** Interactive components, such as `AuthForm.jsx` for login/registration and `AuthStatus.jsx`/`AuthStatusMobile.jsx` for displaying authentication-dependent UI in the header.
    -   **UI (`src/components/ui/`):** Core UI components built using [shadcn/ui](https://ui.shadcn.com/). This provides a set of accessible and composable components like `Button`, `Card`, and `Input`, styled with Tailwind CSS.
-   **Styling:** Primarily handled by [Tailwind CSS](https://tailwindcss.com/).
-   **State Management (`src/store/`):** Client-side state is managed using [Nano Stores](https://github.com/nanostores/nanostores), a lightweight state manager. `auth.ts` specifically handles user authentication state.
-   **API Communication (`src/lib/api.ts`):** An `axios`-based client is configured to communicate with the backend REST API. It handles setting the JWT authorization header for authenticated requests.

## 4. Key Features & State

-   **Responsive Header & Footer:** The application now includes a responsive header with navigation links and dynamic authentication status display, and a simple footer.
-   **Authentication:**
    -   JWT-based authentication is fully implemented. The flow consists of:
        1.  User registers (`/api/auth/register`) or logs in (`/api/auth/login`).
        2.  Backend returns a JWT.
        3.  Frontend stores the JWT in local storage and in the `$jwtToken` Nano Store atom.
        4.  The `api.ts` client attaches the JWT as a Bearer token to the `Authorization` header for all subsequent requests.
    -   The backend's `JwtAuthenticationFilter` validates this token on every protected request.
    -   Public routes are restricted to `/api/auth/**`. All other API routes require authentication.

-   **Core Features:**
    -   **Posts:** Users can create, view, and delete their own posts.
    -   **Comments:** Users can comment on posts.
    -   **Likes:** Users can like and dislike posts.

-   **Database Seeding:**
    -   The `DataInitializer.java` class runs on application startup.
    -   It seeds the database with default roles (`ROLE_USER`, `ROLE_ADMIN`) if they do not already exist, ensuring the application has a valid state on its first run.

## 5. Coding Conventions & Best Practices

-   **Backend:**
    -   **Immutability & Boilerplate:** [Lombok](https://projectlombok.org/) is used extensively (`@Getter`, `@Setter`, `@NoArgsConstructor`, etc.) to reduce boilerplate code in entities and DTOs.
    -   **DTO Mapping:** [MapStruct](https://mapstruct.org/) is the convention for mapping between `Entity` objects and `DTOs`. See `DtoMapper.java`.
    -   **Configuration:** Use `application.properties` for external configuration. Secrets and environment-specific values should be managed via environment variables, which Spring Boot automatically picks up.
    -   **Soft Deletes:** Critical entities like `Post` should use the `@SQLDelete` and `@SQLRestriction` annotations for soft deletion rather than permanent deletion.

-   **Frontend:**
    -   **TypeScript:** All new frontend logic should be written in TypeScript (`.ts`, `.tsx`). Existing `.js` files are being migrated.
    -   **UI Components:** Build UI using the established `shadcn/ui` components in `src/components/ui`. For new, complex, and stateful UI, create React components in `src/components/react` and use Astro's `client:*` directives for hydration.
    -   **Styling:** Use Tailwind CSS utility classes directly in the markup. Avoid writing custom CSS files where possible.
    -   **API Calls:** All backend communication must go through the centralized `axios` instance in `src/lib/api.ts`.
    -   **State:** Use Nano Stores (e.g., `$isLoggedIn`, `$jwtToken`, `$user` atoms in `src/store/auth.ts`) as the single source of truth for authentication status and user information.
    -   **Path Aliases:** Use `@/` for absolute imports from the `src` directory (e.g., `@/store/auth`). This is configured in `frontend/tsconfig.json` with `baseUrl` and `paths`.

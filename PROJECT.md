# Patisoru Project Plan

> **Note:** This document must be kept up-to-date. Any changes to the project's architecture, development plan, or dependencies must be reflected here.

This document outlines the architecture of the backend service and the development plan for the frontend application.

## 1. Backend Architecture & Analysis

The backend is a monolithic service built with Java and Spring Boot. It serves a JSON REST API for a social-like platform where users can create posts, comment on them, and like them.

### 1.1. Core Technologies

-   **Java 17**
-   **Spring Boot 3:** Core framework for the application.
-   **Spring Security:** For authentication and authorization.
-   **Spring Data JPA:** For database interaction.
-   **PostgreSQL:** The likely database, given the use of `UUID` as a primary key.
-   **Maven:** For dependency management.
-   **Lombok:** To reduce boilerplate code.
-   **MapStruct:** For mapping between DTOs and Entities.
-   **Redis:** For caching, specifically for the "featured posts" endpoint.
-   **JWT (JSON Web Tokens):** For securing the API.

### 1.2. Project Structure

The code is organized into standard Spring Boot packages:

-   `configuration`: Contains configuration beans for Security, Redis, JWT, and DTO mapping.
-   `controllers`: Defines the public REST API endpoints.
-   `dto`: Data Transfer Objects for API requests and responses.
-   `entities`: JPA entities that model the database schema.
-   `exceptions`: Custom exception classes and a global exception handler.
-   `repositories`: Spring Data JPA repositories for database access.
-   `security`: Contains security-related services and handlers.
-   `services`: Contains the core business logic.

### 1.3. Database Schema (Inferred from Entities)

-   **`users`**: Stores user information.
    -   `id` (UUID, PK)
    -   `email` (String, Unique)
    -   `password` (String, Hashed)
    -   `full_name` (String)
    -   `status` (Enum: `ACTIVE`, `INACTIVE`, `PENDING`)
    -   `created_at`, `updated_at`
-   **`roles`**: Stores user roles (e.g., `ROLE_USER`, `ROLE_ADMIN`).
    -   `id` (Long, PK)
    -   `name` (String, Unique)
-   **`user_roles`**: A join table for the many-to-many relationship between users and roles.
-   **`posts`**: Stores posts created by users.
    -   `id` (UUID, PK)
    -   `title` (String)
    -   `text` (String)
    -   `post_type` (Enum: `USER_POST`, `VET_POST`, `VERIFIED_POST`)
    -   `like_count` (Long)
    -   `deleted` (Boolean, for soft deletes)
    -   `user_id` (FK to `users`)
    -   `created_at`, `updated_at`
-   **`comments`**: Stores comments on posts.
    -   `id` (UUID, PK)
    -   `text` (String)
    -   `user_id` (FK to `users`)
    -   `post_id` (FK to `posts`)
    -   `created_at`, `updated_at`
-   **`likes`**: A composite key entity to track likes/dislikes on posts.
    -   `post_id` (UUID, PK)
    -   `user_id` (UUID, PK)
    -   `is_like` (Boolean)
-   **`verification_tokens`**: Stores tokens for email verification.
    -   `id` (UUID, PK)
    -   `token` (String)
    -   `expiry_date` (LocalDateTime)
    -   `user_id` (FK to `users`)

### 1.4. API Endpoints

#### Auth Controller (`/api/auth`)

-   `POST /register`: Registers a new user. Sends a verification email.
-   `POST /login`: Authenticates a user and returns a JWT.
-   `POST /email-verification`: Verifies a user's email using a token.

#### Post Controller (`/api/posts`)

-   `GET /{id}`: Gets a single post by its ID.
-   `POST /`: Creates a new post.
-   `DELETE /{id}`: Deletes a post (soft delete). Requires ownership.

#### Comment Controller (`/api/posts/{postId}/comments`)

-   `POST /`: Creates a new comment on a post.
-   `GET /`: Gets all comments for a specific post.

#### Like Controller (`/api/posts/{postId}/likes`)

-   `POST /like`: Adds a "like" to a post.
-   `POST /dislike`: Adds a "dislike" to a post.
-   `POST /remove`: Removes a like/dislike from a post.
-   `GET /status`: Gets the current user's like status for a post.

#### Discover Controller (`/api/discover`)

-   `GET /`: Returns a paginated list of all posts, sorted by creation date.

### 1.5. Authentication & Authorization

-   **Authentication:** Handled via JWT. The `JwtAuthenticationFilter` intercepts requests, validates the token, and sets the `SecurityContext`.
-   **Authorization:**
    -   Public endpoints are configured in `SecurityConfig` (`/api/auth/**`).
    -   All other endpoints require authentication.
    -   Method-level authorization is used for post deletion (`@PreAuthorize("@postSecurityService.isOwner(authentication,#postId)")`), ensuring only the post's owner can delete it.
-   **User Status:** Users must have an `ACTIVE` status to be enabled (`isEnabled()` in `User` entity). Registration sets the status to `PENDING`, and email verification sets it to `ACTIVE`.

### 1.6. Analysis & Improvement Suggestions

-   **`LikeService` Refactoring:** The `LikeService` contains a `TODO` comment indicating that the like/dislike logic is "completely bullshit" and needs refactoring. The current implementation uses three separate methods (`like`, `dislike`, `remove`) which all call a single `saveLike` method. This can be simplified into a single `setLikeStatus(postId, userId, status)` endpoint where `status` could be `LIKE`, `DISLIKE`, or `NONE`. This would be more RESTful and cleaner.
-   **Hardcoded Role Strings:** Role checks in `PostService` use hardcoded strings (`"ROLE_VET"`, `"ROLE_VERIFIED"`). It would be better to use an Enum or constants for roles to avoid typos and improve maintainability.
-   **Error Handling:** The `GlobalExceptionHandler` provides a good baseline. However, it could be improved by handling more specific Spring exceptions, like `MethodArgumentNotValidException` for validation failures, to provide more detailed error messages to the client.
-   **`ResponseMessage` DTO:** The `ResponseMessage` DTO with a numeric code (`1` for success, `-1` for failure) is not very descriptive. Standard HTTP status codes are usually sufficient and more conventional for a REST API. This DTO is used in the `LikeController` but the controller returns a `long` instead. This DTO seems to be unused and could be removed.
-   **Soft Deletes:** The `Post` entity uses a `deleted` flag for soft deletes. This is good. We should ensure that all queries for posts properly filter out the deleted ones. The `@SQLRestriction("deleted = false")` annotation handles this at the entity level, which is a great implementation.
-   **Email Service:** The `EmailService` constructs a verification URL that is hardcoded to `https://patisoru.com.tr`. This should be externalized into the `application.properties` file to be configurable across different environments (development, staging, production).
-   **Missing User Profile Features:** The backend has a `UserEditRequest` DTO but no corresponding endpoint to update user information. There's also no endpoint to view a user's profile or their posts.

## 2. Frontend Development Plan

The frontend will be built using Astro, React, and `shadcn/ui` for the component library.

> **A Note on TypeScript vs. JavaScript:** While Astro and `shadcn/ui` are built with TypeScript and the project will be configured to support `.ts`/`.tsx` files, our primary development for custom components and application logic will be done using JavaScript (`.js` and `.jsx` files). This allows us to benefit from the strong typing of the underlying tools without enforcing TypeScript for our own application code.

### 2.1. Setup & Initialization (As Discovered)

Our setup process is now simplified and confirmed based on the latest tool behaviors.

> **Why is there no `tailwind.config.mjs`?**
> Our project uses Tailwind CSS v4 with its `@tailwindcss/vite` plugin. Unlike v3, this new version doesn't require a separate `tailwind.config.mjs` file for a basic setup. The `shadcn` CLI is smart enough to work with this modern setup by injecting styles directly into `globals.css`.

1.  **Create a New Astro Project:** Start with a clean project, including React and Tailwind CSS integrations.
    ```bash
    npm create astro@latest frontend -- --template minimal --install --add react --add tailwind --git
    ```

2.  **Configure `tsconfig.json`:** Manually add the `paths` configuration for import aliases. This is a required step before initializing `shadcn`.
    ```json
    // In tsconfig.json
    {
      "compilerOptions": {
        "paths": {
          "@/*": [
            "./src/*"
          ]
        }
      }
    }
    ```

3.  **Initialize `shadcn`:** Run the `init` command within the `frontend` directory. The CLI will automatically detect the Astro+Vite setup and inject styles directly into `src/styles/globals.css`.
    ```bash
    npx shadcn@latest init
    ```

4.  **Add Core Components:** Add the components we anticipate needing for the project.
    ```bash
    npx shadcn@latest add button input card label sonner form avatar dropdown-menu
    ```

### 2.2. Project Structure

-   `src/pages/`: Contains the main pages of the application (e.g., `index.astro`, `login.astro`, `post/[id].astro`).
-   `src/components/`: Contains reusable Astro components (`.astro`).
-   `src/components/ui/`: This is where `shadcn/ui` components are stored (`.tsx`).
-   `src/components/react/`: Contains our custom React components (`.tsx`) that use `shadcn/ui` components. These will be the interactive "islands".
-   `src/layouts/`: Contains layout components, like `Layout.astro`.
-   `src/lib/`: Contains utility functions, API service, and state management.
-   `src/store/`: For managing global state (e.g., authentication status). We will use `nanostores`.

### 2.3. Development Phases & Tasks

#### Phase 1: Authentication

-   **Task 1.1: Create Auth Store:** Use `nanostores` to create a simple store to manage the JWT and user authentication state.
-   **Task 1.2: Create API Service:** Create a utility file (`src/lib/api.ts`) to handle `fetch` requests to the backend, automatically adding the `Authorization` header.
-   **Task 1.3: Build Login/Register Pages:**
    -   Create `src/pages/login.astro` and `src/pages/register.astro`.
    -   Create a React component `src/components/react/AuthForm.tsx` that uses `shadcn/ui`'s `Form`, `Card`, `Input`, `Button`, and `Label` components.
    -   The form will handle both login and registration logic, making API calls and updating the auth store on success.
    -   Use `client:load` on the `<AuthForm />` component in the Astro pages.
-   **Task 1.4: Build Email Verification Page:**
    -   Create `src/pages/email-verification.astro`.
    -   This page will read the `email` and `token` from the URL query parameters.
    -   It will have a simple React component to show a loading state, call the verification API, and then display a success or error message using `sonner`.

#### Phase 2: Core Features (Posts & Comments)

-   **Task 2.1: Build Home/Discover Page:**
    -   Modify `src/pages/index.astro`.
    -   Fetch posts from the `/api/discover` endpoint.
    -   Create a `PostCard.astro` component to display a summary of each post.
    -   The page will use server-side rendering for the initial post list.
-   **Task 2.2: Build Post Detail Page:**
    -   Create `src/pages/post/[id].astro`.
    -   Fetch the specific post and its comments using the post `id` from the URL.
    -   Display the post content.
    -   Create a `CommentSection.tsx` React component to display comments and allow users to add new comments. This component will be interactive.
    -   Create a `LikeButton.tsx` React component to handle liking/disliking the post. It will manage its own state and update the like count.
-   **Task 2.3: Build "Create Post" Feature:**
    -   Create a `src/pages/post/create.astro` page.
    -   Build a `CreatePostForm.tsx` React component using `shadcn/ui` `Form` components to submit a new post.

#### Phase 3: User Profile

-   **Task 3.1: Build User Profile Page:**
    -   Create `src/pages/user/[id].astro`.
    -   This page will display user information and a list of their posts.
    -   Requires a new backend endpoint: `GET /api/users/{id}`.
-   **Task 3.2: Build User Edit Page:**
    -   Create `src/pages/settings/profile.astro`.
    -   Build a `UserEditForm.tsx` React component to allow users to update their `fullName`.
    -   Requires a new backend endpoint: `PUT /api/users/me`.

# Blogging Application

This is a comprehensive blogging application built using Spring Boot, Java, Swagger, Spring Security, MySQL, and JWT for authentication. The application allows users to create, read, update, and delete blog posts, categories, comments, and users.

## Features

- **User authentication** using JWT
- **CRUD operations** for Users, Categories, Posts, and Comments
- **Role-based authorization** for secure actions (e.g., delete operations)
- **Search functionality** for blog posts by title
- **Image upload and retrieval** for blog posts
- **API documentation** available via Swagger UI
- **XML and JSON** data format support

## Technologies Used

- **Java 17**
- **Spring Boot 3.x**
- **Spring Security 6.x**
- **MySQL**
- **Swagger 2.3.0**
- **JWT (JSON Web Token)**
- **Maven** for build management
- **Jackson** for JSON and XML data formats

## Directory Structure
```
  C:.
  ├───.idea
  ├───.mvn
  │   └───wrapper
  ├───images
  ├───src
  │   ├───main
  │   │   ├───java
  │   │   │   └───com
  │   │   │       └───blog
  │   │   │           └───app
  │   │   │               ├───config
  │   │   │               ├───controllers
  │   │   │               ├───entities
  │   │   │               ├───exceptions
  │   │   │               ├───payloads
  │   │   │               ├───repositories
  │   │   │               ├───security
  │   │   │               └───services
  │   │   │                   └───impl
  │   │   └───resources
  │   │       ├───static
  │   │       └───templates
  │   └───test
  │       ├───java
  │       │   └───com
  │       │       └───blog
  │       │           └───app
  │       │               ├───controllers
  │       │               └───services
  │       └───resources
  ├───target
  │   ├───classes
  │   │   └───com
  │   │       └───blog
  │   │           └───app
  │   │               ├───config
  │   │               ├───controllers
  │   │               ├───entities
  │   │               ├───exceptions
  │   │               ├───payloads
  │   │               ├───repositories
  │   │               ├───security
  │   │               └───services
  │   │                   └───impl
  │   ├───generated-sources
  │   │   └───annotations
  │   ├───generated-test-sources
  │   │   └───test-annotations
  │   └───test-classes
  │       └───com
  │           └───blog
  │               └───app
  │                   ├───controllers
  │                   └───services
  └───testPath
```

## Getting Started

### Prerequisites

- **Java 17** installed on your machine.
- **MySQL** database setup.
- **Maven** installed for building the project.

### Installation

1. **Clone the repository:**
```bash
     git clone https://github.com/BloggingApplication/blogging-app.git
```
```bash
     cd blogging-app
  ```
2. **Build the project**:
  ```bash
    mvn clean install
  ```
3. **Run the application**:
  ```bash
    mvn spring-boot:run
  ```
4. **Access the application**:
The application will be running at `http://localhost:8080`.

### API Documentation

- Access the Swagger UI for API documentation at `http://localhost:8080/swagger-ui.html`.

### JWT Authentication

This application uses JWT for authentication. Below are the steps to use JWT:

#### Obtain a JWT Token:

- Send a POST request to `/api/auth/login` with valid user credentials.
- The response will contain the JWT token.

**Example request body:**

  ```json
    {
        "username": "your-username",
        "password": "your-password"
    }
  ```

### Use the JWT Token:

Include the token in the Authorization header of requests to secured endpoints.

Example:

  ```bash
    Authorization: Bearer <your-jwt-token>
  ```

### API Endpoints

#### User API

- `GET /api/users`: Retrieve all users.
- `GET /api/users/{user_id}`: Retrieve a user by ID.
- `PUT /api/users/{user_id}`: Update an existing user.
- `DELETE /api/users/{user_id}`: Delete a user (requires ADMIN role).

#### Category API

- `GET /api/categories`: Retrieve all categories.
- `GET /api/categories/{category_id}`: Retrieve a category by ID.
- `POST /api/categories`: Create a new category.
- `PUT /api/categories/{category_id}`: Update an existing category.
- `DELETE /api/categories/{category_id}`: Delete a category.

#### Post API

- `GET /api/posts`: Retrieve all posts.
- `GET /api/posts/{post_id}`: Retrieve a post by ID.
- `GET /api/users/{user_id}/posts`: Retrieve posts by user ID.
- `GET /api/categories/{category_id}/posts`: Retrieve posts by category ID.
- `GET /api/posts/search/{keyword}`: Search posts by title.
- `POST /api/users/{user_id}/categories/{category_id}/posts`: Create a new post.
- `PUT /api/posts/{post_id}`: Update an existing post.
- `DELETE /api/posts/{post_id}`: Delete a post (requires ADMIN role).
- `POST /api/posts/images/upload/{post_id}`: Upload an image for a post.
- `GET /api/posts/images/{imageName}`: Retrieve an image by its name.

#### Comment API

- `GET /api/comments`: Retrieve all comments.
- `POST /api/posts/{post_id}/comments`: Create a new comment on a post.
- `DELETE /api/comments/{comment_id}`: Delete a comment.

## Security Configuration

The application uses Spring Security for securing the endpoints. The security configuration is set up using the `SecurityFilterChain` bean method in the `SecurityConfig` class.

### Secured Endpoints
- `/api/posts/**`, `/api/comments/**`, `/api/categories/**`, `/api/users/**` - Requires authentication.
- `DELETE` endpoints require the `ADMIN` role.

### Open Endpoints
- `/api/auth/login` - Open for authentication.
- `/api/auth/register` - Open for user registration.


### Data Format Support

This application supports both JSON and XML data formats. The `jackson-dataformat-xml` dependency is included to handle XML data. You can request and receive data in either format by specifying the appropriate `Content-Type` and `Accept` headers in your HTTP requests.
  ```bash
    localhost:8080/api/users?media=xml
  ```
  ```bash
    localhost:8080/api/users?media=json
  ```

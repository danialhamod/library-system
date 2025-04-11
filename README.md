# Library Management System

A Spring Boot application for managing books, patrons, and borrowing books in a library.

## Features

- RESTful API endpoints for books, patrons, and borrowing operations
- Basic authentication
- In-memory H2 database
- Caching for improved performance
- AOP logging to file
- Comprehensive unit and integration tests

## Technologies

- Java 17
- Spring Boot 3.4.4
- Spring Data JPA
- H2 Database (in-memory)
- Spring Security
- Spring Cache
- AspectJ (AOP)
- Lombok
- JUnit 5 + Mockito

## Getting Started

### Prerequisites

- Java 17 JDK
- Maven 3.8+
- (Optional) Postman for API testing

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/danialhamod/library-system.git
   cd library-system
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

### Running the Application

Start the application with:
```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

### Testing

Run unit tests:
```bash
mvn test
```

## Configuration

### Database
- **H2 in-memory database** is used by default
- Access console at `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:librarydb`
  - Username: `sa`
  - Password: (leave empty)

### Authentication
Basic authentication credentials:
- Username: `admin`
- Password: `admin123`

### Caching
- In-memory caching is enabled for:
  - Book details
  - Patron information
- Cache TTL: 10 minutes

### Logging
- AOP logs method executions to file
- Log location: `logs/library-app.log`
- Log rotation: Daily or when reaching 10MB
- Keeps 7 days of logs

## API Documentation

A Postman collection is included in the repository (`Library Management System.postman_collection.json` file) with:
- All API endpoints
- Example requests
- Pagination GET parameters (disabled)
- Authentication setup

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/library/
│   │       ├── aop/          # AOP logging classes
│   │       ├── config/       # Configuration classes with auth config
│   │       ├── constants/    # Constants classes
│   │       ├── controller/   # REST controllers
│   │       ├── dto/          # DTO classes
│   │       ├── exception/    # Exceptions and exception handling classes
│   │       ├── model/        # Entities
│   │       ├── repository/   # JPA repositories
│   │       ├── service/      # Business logic
│   │       └── LibraryApplication.java
│   └── resources/
│       └── application.properties
└── test/                     # Unit and integration tests
```

## Endpoints

### Books
- `GET /api/books` - List all books
- `POST /api/books` - Create new book
- `GET /api/books/{id}` - Get book details
- `PUT /api/books/{id}` - Update book
- `DELETE /api/books/{id}` - Delete book

### Patrons
- `GET /api/patrons` - List all patrons
- `POST /api/patrons` - Create new patron
- `GET /api/patrons/{id}` - Get patron details
- `PUT /api/patrons/{id}` - Update patron
- `DELETE /api/patrons/{id}` - Delete patron

### Borrowing
- `POST /api/borrow/{bookId}/patron/{patronId}` - Borrow a book
- `PUT /api/return/{bookId}/patron/{patronId}` - Return a book
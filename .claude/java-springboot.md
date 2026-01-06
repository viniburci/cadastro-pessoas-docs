You are an expert in Java, Spring Boot, and scalable enterprise application development. You write maintainable, performant, and secure code following Spring Boot and Java best practices.

## Java Best Practices

- Use Java 17+ features when available
- Follow naming conventions: classes (PascalCase), methods/variables (camelCase), constants (UPPER_SNAKE_CASE)
- Prefer immutability when possible
- Use `var` for local variables when type is obvious
- Avoid raw types; always use generics
- Use Optional instead of null for uncertain values
- Leverage streams and lambda expressions for collection operations

## Spring Boot Best Practices

- Use constructor injection over field injection
- Avoid `@Autowired` on fields; use constructor injection instead
- Keep Spring Boot version up to date
- Use `@RestController` for REST APIs instead of `@Controller` + `@ResponseBody`
- Implement proper exception handling with `@ControllerAdvice`
- Use `application.properties` or `application.yml` for configuration
- Leverage Spring Boot profiles for different environments
- Use `@Transactional` at service layer, not controller layer

## Project Structure

```
src/main/java/com/company/project/
├── domain/           # Business entities and domain logic
│   ├── entity/       # JPA entities
│   ├── dto/          # Data Transfer Objects
│   ├── service/      # Business logic services
│   ├── repository/   # Data access layer
│   └── controller/   # REST controllers
├── config/           # Configuration classes
├── exception/        # Custom exceptions and handlers
├── util/             # Utility classes
└── Application.java  # Main application class
```

## Entities and DTOs

- Keep entities focused on database representation
- Use DTOs for API requests/responses
- Never expose entities directly in REST APIs
- Use Lombok annotations to reduce boilerplate: `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`
- Use `@Entity` with `@Table` for JPA entities
- Always define proper relationships: `@OneToMany`, `@ManyToOne`, `@ManyToMany`
- Use `@JsonManagedReference` and `@JsonBackReference` to avoid circular dependencies

## Controllers

- Keep controllers thin; delegate logic to services
- Use proper HTTP methods: GET, POST, PUT, DELETE, PATCH
- Use `@PathVariable` for resource identifiers
- Use `@RequestParam` for query parameters
- Use `@RequestBody` for request payloads
- Return `ResponseEntity<T>` for full control over HTTP response
- Use proper HTTP status codes (200, 201, 204, 400, 404, 500)
- Implement CORS configuration when needed with `@CrossOrigin`

## Services

- Design services around business capabilities
- Use `@Service` annotation
- Keep services transactional with `@Transactional`
- Handle business logic validation in services
- Throw custom exceptions for business rule violations
- Never catch generic `Exception`; catch specific exceptions

## Repositories

- Use Spring Data JPA for data access
- Extend `JpaRepository<Entity, ID>` for CRUD operations
- Use query methods following Spring Data naming conventions
- Use `@Query` for complex queries
- Prefer JPQL over native SQL when possible
- Use projections for read-only operations

## Exception Handling

- Create custom exception classes extending `RuntimeException`
- Use `@ControllerAdvice` for global exception handling
- Return proper error responses with meaningful messages
- Use `@ExceptionHandler` to handle specific exceptions
- Log exceptions appropriately (ERROR for system issues, WARN for business violations)

## Validation

- Use Bean Validation annotations: `@NotNull`, `@NotBlank`, `@Size`, `@Min`, `@Max`, `@Email`
- Add `@Valid` or `@Validated` to method parameters
- Create custom validators when needed
- Handle validation errors in `@ControllerAdvice`

## Security

- Never log sensitive information (passwords, tokens, personal data)
- Use parameterized queries to prevent SQL injection
- Validate all user inputs
- Use BCrypt for password hashing
- Implement proper authentication and authorization with Spring Security
- Use HTTPS in production
- Never commit secrets (.env, credentials, API keys)

## Database

- Use migrations (Flyway or Liquibase) for schema management
- Avoid `ddl-auto=create-drop` in production
- Use indexes for frequently queried columns
- Use database-specific types when needed (e.g., `columnDefinition = "BYTEA"` for PostgreSQL)
- Use proper cascade types: `CascadeType.ALL`, `CascadeType.PERSIST`, etc.

## Testing

- Write unit tests for services using JUnit 5 and Mockito
- Use `@SpringBootTest` for integration tests
- Use `@WebMvcTest` for controller tests
- Mock external dependencies
- Test edge cases and error scenarios
- Aim for high code coverage (70%+)

## Performance

- Use lazy loading for collections when appropriate
- Implement pagination for large datasets
- Use caching with `@Cacheable` when appropriate
- Optimize database queries (avoid N+1 problem)
- Use connection pooling (HikariCP is default)
- Profile application for bottlenecks

## Logging

- Use SLF4J with Logback (Spring Boot default)
- Use appropriate log levels: TRACE, DEBUG, INFO, WARN, ERROR
- Log meaningful information with context
- Avoid logging in loops
- Use parameterized logging: `log.info("User {} logged in", username)`

## Dependencies

- Keep dependencies up to date
- Use Spring Boot Starter dependencies
- Avoid unnecessary dependencies
- Use Maven or Gradle for dependency management
- Prefer Spring Boot managed versions

## Configuration

- Use environment variables for sensitive data
- Use profiles for environment-specific configuration
- Externalize configuration from code
- Use `@ConfigurationProperties` for type-safe configuration
- Document all configuration properties

## API Design

- Follow RESTful principles
- Use plural nouns for resource names (`/users`, `/products`)
- Use HTTP methods semantically
- Version your APIs (`/api/v1/...`)
- Return consistent response structures
- Document APIs with Swagger/OpenAPI

## Git Commits

- NEVER use Co-Authored-By in commit messages
- Do NOT add "Generated with Claude Code" or similar footers
- Keep commit messages clean and professional
- Follow conventional commits format: `feat:`, `fix:`, `refactor:`, `docs:`, `test:`
- Write descriptive commit messages explaining the "why"

## Code Quality

- Follow SOLID principles
- Keep methods small and focused
- Use meaningful names for classes, methods, and variables
- Write self-documenting code
- Add comments only when necessary to explain "why", not "what"
- Use static code analysis tools (SonarQube, Checkstyle)
- Follow DRY (Don't Repeat Yourself) principle

# Testing Guide for Productivity App

## Overview

This project includes a comprehensive test suite covering unit tests, integration tests, and API tests for all major components.

## Test Structure

```
src/test/java/com/example/productivity_app/
├── ProductivityAppApplicationTests.java          # Basic context loading test
├── ProductivityAppIntegrationTest.java          # Integration test with test profile
├── controller/
│   └── UserControllerTest.java                  # Controller layer tests using MockMvc
├── service/
│   ├── UserServiceTest.java                     # User service unit tests
│   ├── GoalServiceTest.java                     # Goal service unit tests
│   ├── BuddyRequestServiceTest.java             # Buddy request service unit tests
│   └── CheckpointServiceTest.java               # Checkpoint service unit tests
└── util/
    └── JwtUtilTest.java                         # JWT utility unit tests
```

## Test Categories

### 1. Unit Tests
- **Service Layer Tests**: Test business logic in isolation using Mockito
- **Utility Tests**: Test helper classes and utilities
- **Entity Tests**: Test entity behavior and business rules

### 2. Integration Tests
- **Controller Tests**: Test REST endpoints using MockMvc
- **Service Integration**: Test how services interact with repositories
- **Context Loading**: Verify Spring context loads correctly

### 3. Test Coverage
- **Happy Path**: Successful operations
- **Error Cases**: Exception handling and edge cases
- **Validation**: Input validation and business rule enforcement
- **Security**: Authentication and authorization scenarios

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=UserServiceTest
```

### Run Tests with Coverage Report
```bash
mvn test jacoco:report
```

### Run Tests in Specific Package
```bash
mvn test -Dtest="com.example.productivity_app.service.*"
```

## Test Configuration

### Test Profile
Tests use the `test` profile with the following configuration:
- **Database**: H2 in-memory database
- **Security**: Disabled for easier testing
- **Logging**: DEBUG level for troubleshooting

### Test Properties
```properties
# H2 Database for testing
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop

# JWT Configuration
jwt.secret=test-secret-key-for-unit-testing-only
jwt.expiration=3600000
```

## Test Patterns Used

### 1. Arrange-Act-Assert (AAA)
```java
@Test
void shouldCreateUserSuccessfully() {
    // Arrange
    when(userRepository.save(any())).thenReturn(testUser);
    
    // Act
    UsersDto result = userService.createUser(registerDto);
    
    // Assert
    assertNotNull(result);
    assertEquals("test@example.com", result.getEmail());
}
```

### 2. Mocking with Mockito
```java
@Mock
private UserRepository userRepository;

@InjectMocks
private UserService userService;
```

### 3. Exception Testing
```java
@Test
void shouldThrowExceptionWhenUserNotFound() {
    when(userRepository.findById(999L)).thenReturn(Optional.empty());
    
    assertThrows(RuntimeException.class, 
        () -> userService.getUsersById(999L));
}
```

### 4. MockMvc for Controller Testing
```java
mockMvc.perform(post("/users/register")
    .contentType(MediaType.APPLICATION_JSON)
    .content(jsonContent))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.email").value("test@example.com"));
```

## Test Data Management

### Test Fixtures
- **@BeforeEach**: Set up test data for each test method
- **Test Constants**: Reusable test values
- **Builder Pattern**: Create complex test objects

### Data Cleanup
- **@AfterEach**: Clean up after each test
- **H2 Database**: Automatically cleaned between tests
- **Mockito**: Automatically resets mocks

## Best Practices

### 1. Test Naming
- Use descriptive names: `shouldCreateUserSuccessfully()`
- Follow the pattern: `should[ExpectedBehavior]When[Condition]()`

### 2. Test Isolation
- Each test should be independent
- Use fresh mocks and test data for each test
- Avoid test dependencies

### 3. Assertions
- Test one concept per test method
- Use specific assertions over generic ones
- Verify both positive and negative scenarios

### 4. Mocking Strategy
- Mock external dependencies (databases, APIs)
- Don't mock the class under test
- Use `@InjectMocks` for automatic dependency injection

## Common Test Scenarios

### 1. CRUD Operations
- Create, Read, Update, Delete operations
- Success and failure cases
- Validation errors

### 2. Business Logic
- Progress calculation
- Status transitions
- Business rule enforcement

### 3. Security
- JWT token generation and validation
- Authentication flows
- Authorization checks

### 4. Error Handling
- Resource not found
- Invalid input data
- Business rule violations

## Troubleshooting

### Common Issues

1. **Context Loading Failures**
   - Check test profile configuration
   - Verify component scanning
   - Check for missing beans

2. **Mockito Errors**
   - Ensure proper `@ExtendWith(MockitoExtension.class)`
   - Check mock setup in `@BeforeEach`
   - Verify argument matchers

3. **Database Issues**
   - Ensure H2 dependency is included
   - Check test profile properties
   - Verify entity scanning

### Debug Tips

1. **Enable Debug Logging**
   ```properties
   logging.level.com.example.productivity_app=DEBUG
   ```

2. **Use Test Profiles**
   ```java
   @ActiveProfiles("test")
   ```

3. **Check Test Output**
   - Review test execution logs
   - Check for exception stack traces
   - Verify mock interactions

## Future Enhancements

### Planned Test Improvements
1. **TestContainers**: Use real PostgreSQL for integration tests
2. **Performance Tests**: Load testing for critical endpoints
3. **Security Tests**: Penetration testing for authentication flows
4. **API Contract Tests**: Verify API compatibility

### Test Coverage Goals
- **Unit Tests**: 90%+ coverage
- **Integration Tests**: 80%+ coverage
- **API Tests**: 70%+ coverage

## Contributing to Tests

### Adding New Tests
1. Follow existing naming conventions
2. Use appropriate test categories
3. Include both positive and negative scenarios
4. Add proper documentation

### Test Maintenance
1. Keep tests up to date with code changes
2. Refactor tests when business logic changes
3. Remove obsolete tests
4. Update test documentation

---

For more information about testing strategies, see:
- [Spring Boot Testing Guide](https://spring.io/guides/gs/testing-web/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)

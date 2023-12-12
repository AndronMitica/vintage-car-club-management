## VCC - Vintage Car Club Management

The vintage car enthusiast app is a comprehensive tool for managing club memberships, scheduling events, and showcasing member-to-member vintage vehicle collections. Enthusiasts can create profiles, add multiple vehicles, and track memberships. The platform's unique showcase feature fosters a vibrant community where members share stories and photos of their vintage cars, creating a dynamic hub for enthusiasts to connect and celebrate their shared passion.

The application uses:
-  Spring Boot
-  Java 17
-  Maven
-  PostgreSQL
-  H2 in-memory database
-  MockMVC (for integration tests)
-  JUnit (for unit tests)
-  Mockito (to mock dependencies for unit tests)


## Database connection properties
- spring.datasource.url = jdbc:postgresql://localhost:5432/your_database
- spring.datasource.username=pass
- spring.datasource.password=pass
- spring.datasource.driver-class-name=org.postgresql.Driver

# JPA properties
- spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
- spring.jpa.open-in-view=false
- spring.jpa.hibernate.ddl-auto=update

# Thymeleaf
- spring.thymeleaf.prefix=classpath:/templates/
- spring.thymeleaf.suffix=.html

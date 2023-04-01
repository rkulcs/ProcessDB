# Requirements
- Java 17 or later
- A database management system (tested with PostgreSQL)
- npm (tested with version 9.3.1)

# Running the Application
## Backend and DB
1. Create a new database and user in a DBMS (the application is configured to use PostgreSQL by default)
2. Edit backend/src/main/resources/application.properties as follows:
  - spring.datasource.url=\<JDBC URL to database\>
  - spring.datasource.username=\<DB username\>
  - spring.datasource.password=\<DB password\>
  - spring.datasource.driver-class-name=\<JDBC driver class name\> (if a different DBMS is used)
  - app.jwt.secret=<custom 32-character secret string>
3. Navigate to the backend directory, then run the Spring Boot application: `./mvnw spring-boot:run`

## Frontend
1. Edit the backend URL in frontend/.env if needed; this step can be skipped if Spring Boot is running on http://localhost:8080
2. Navigate to the frontend directory, then run the following:
```
npm install
npm run css-build
npm run dev
```

# CooksCorner
Inspiring culinary experiences start with CooksCorner

This project was developed in collaboration with a frontend developer.

## Description
CooksCorner is an innovative app designed for a convenient and inspiring culinary experience. Offering a range of categories, including a comprehensive list of recipes, CooksCorner provides a convenient platform for culinary enthusiasts. Whether exploring recipes through captivating photos, delving into detailed descriptions, or managing your culinary journey by saving, liking, and even creating your own dishes, CooksCorner is the way to seamless and inspiring culinary adventures.

## Build with
- [Spring Boot](https://spring.io/projects/spring-boot) - Server framework
- [Spring Security](https://spring.io/projects/spring-security) - Security
- [Maven](https://maven.apache.org/) - Build and dependency management
- [PostgreSQL](https://www.postgresql.org/) - Database
- [Cloudinary](https://cloudinary.com/) - Image Storege
- [Swagger](https://swagger.io/) - Documentation

## Entity Relationship Diagram
![ERD](https://github.com/nastenka-ooops/RecipeHub/blob/main/diagrams/ERD.png)

## Installation
To install the application, follow these steps:

1. Clone the repository:
   ```sh
   git clone https://github.com/nastenka-ooops/RecipeHub.git
   cd CooksCorner
   ```

2. If you have Maven installed locally, run:
   ```sh
   mvn clean install
   ```

3. If you do not have Maven installed, run:
   ```sh
   ./mvnw clean install
   ```

## Testing
To run tests, open a terminal in the root directory and type:
```sh
mvn clean test
```
All test cases can be found in the test directory of the project.

## How To Run
After a successful installation, set the following environment variables and run the application:
```sh
java -jar *.jar --DB_HOST=your-database-host --DB_NAME=your-database-name --DB_PASSWORD=your-database-password --DB_PORT=your-database-port --DB_USERNAME=your-database-username --MAIL_PASSWORD=your-email-password --MAIL_USERNAME=your-email-username --API_KEY=your-api-key --API_SECRET=your-api-secret --CLOUD_NAME=your-cloud-name
```

## How To Use
Visit the CooksCorner website to explore the site developed in collaboration with the frontend developer.

## Documentation
[Documentation]([link_to_swagger_documentation](https://cookscorner-production-9502.up.railway.app/swagger-ui/index.html))

## Authors
- *Backend developer* - [Brytskaya Anastasia](https://github.com/nastenka-ooops)
- *Frontend developer* - [Karataeva Aigerim](https://github.com/KarataevaAigerim)

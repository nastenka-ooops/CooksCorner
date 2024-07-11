FROM openjdk:20-jdk-slim-buster
COPY ./CooksCorner-0.0.1-SNAPSHOT.jar ./CooksCorner.jar

ENTRYPOINT ["java", "-jar", "./CooksCorner.jar"]

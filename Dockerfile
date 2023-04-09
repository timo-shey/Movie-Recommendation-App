FROM openjdk:18
ARG JAR_FILE=target/*.jar
ADD ./target/movieSearchAPI-0.0.1.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8080
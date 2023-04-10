FROM openjdk:8-jre-alpine
COPY target/my-operator-*.jar /app/my-operator.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/my-operator.jar"]

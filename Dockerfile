FROM eclipse-temurin:27-jre
EXPOSE 8080
ADD backend-target/app.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
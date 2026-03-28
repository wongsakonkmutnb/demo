# 1. ใช้ Maven และ Java 21 ในการ Build โปรเจกต์ (ตามเวอร์ชันที่คุณใช้ในเครื่อง)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# 2. จำลองเครื่อง Server ที่มี Java 21 เพื่อรันโปรเจกต์
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
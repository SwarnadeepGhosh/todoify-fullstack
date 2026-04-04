# JDK 11: maven:3.8.2-jdk-11-slim (Build) and adoptopenjdk/openjdk11:alpine-jre (Runtime)
# JDK 17: maven:3.8.4-openjdk-17 (Build) and openjdk:17-jdk-slim (Runtime)
# JDK 21: maven:3.9.9-eclipse-temurin-21 (Build) and eclipse-temurin:21-jre-alpine (Runtime)

# Build a JAR File
# FROM maven:3.8.2-jdk-11-slim AS stage1 # for jdk 1.8

# WORKDIR /home/app
# COPY . /home/app/
# RUN mvn -f /home/app/pom.xml clean package
#
#
# # Create an Image
# # FROM openjdk:8-jdk-alpine # For Java 8, try this
#
# # For Java 11, try this
# FROM adoptopenjdk/openjdk11:alpine-jre
# EXPOSE 8080
# COPY --from=stage1 /home/app/target/todoify-fullstack-0.0.1-SNAPSHOT.jar app.jar
# ENTRYPOINT ["sh", "-c", "java -jar /app.jar"]
# maven:3.9.4-eclipse-temurin-21-alpine

###############################################################################################
# -------- Stage 1: Build --------
# FROM maven:3.9.9-eclipse-temurin-21 AS build
# WORKDIR /app
# COPY . .
# RUN mvn clean package -DskipTests
#
# # -------- Stage 2: Run --------
# FROM eclipse-temurin:21-jre-alpine
# WORKDIR /app
#
# # Copy jar from build stage
# COPY --from=build /app/target/todoify-fullstack-*.jar app.jar
# EXPOSE 8080
# ENTRYPOINT ["java", "-jar", "app.jar"]

###############################################################################################
# # Use an official Maven image to build the Spring Boot app
# FROM maven:3.8.4-openjdk-17 AS build
#
# # Set the working directory
# WORKDIR /app
#
# # Copy the pom.xml and install dependencies
# COPY pom.xml .
# RUN mvn dependency:go-offline
#
# # Copy the source code and build the application
# COPY src ./src
# RUN mvn clean package -DskipTests
#
# # Use an official OpenJDK image to run the application
# FROM openjdk:17-jdk-slim
#
# # Set the working directory
# WORKDIR /app
#
# # Copy the built JAR file from the build stage
# COPY --from=build /app/target/sb-deployment-0.0.1-SNAPSHOT.jar .
#
# # Expose port 8080
# EXPOSE 8080
#
# # Specify the command to run the application
# ENTRYPOINT ["java", "-jar", "/app/sb-deployment-0.0.1-SNAPSHOT.jar"]


###############################################################################################
# Use an official Maven image to build the Spring Boot app
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and install dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Use an official OpenJDK image to run the application
FROM eclipse-temurin:21-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/todoify-fullstack-1.0.jar app.jar

# Expose port 8080
EXPOSE 8080

# Specify the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
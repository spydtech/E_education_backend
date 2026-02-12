# Use an official Maven image to build the application
FROM maven:3.8.6-eclipse-temurin-17 AS build
 
# Set the working directory inside the container
WORKDIR /EonlineCource/Backend/RegisterLogin/RegisterLogin
 
# Copy the Maven project files
COPY ./Backend/RegisterLogin/RegisterLogin /EonlineCource/Backend/RegisterLogin/RegisterLogin
#COPY src ./src
 
# Build the application
RUN mvn clean package -DskipTests
 
# Use a lightweight JDK runtime image for running the application
FROM eclipse-temurin:17-jre
 
# Set the working directory inside the container
WORKDIR /EonlineCource/Backend/RegisterLogin/RegisterLogin
 
# Copy the built JAR file from the build stage
COPY --from=build /EonlineCource/Backend/RegisterLogin/RegisterLogin/target/*.jar education.jar
 
# Expose the application port (change if needed)
EXPOSE 80
EXPOSE 3000
EXPOSE 8082
 
# Command to run the application
CMD ["java", "-jar", "education.jar"]

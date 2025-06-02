# Use Maven with OpenJDK 11 as the base image
FROM maven:3.6.3-openjdk-11

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files to the container
COPY pom.xml .
COPY src ./src

# Download project dependencies
RUN mvn dependency:go-offline

# Package the application
RUN mvn clean package -DskipTests=true

# Set the default command to run tests
CMD ["mvn", "test"]

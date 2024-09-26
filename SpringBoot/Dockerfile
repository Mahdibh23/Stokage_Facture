# Step 1: Use Maven with JDK 21 for building the application
FROM openjdk:21-slim AS build

# Installer Maven
RUN apt-get update && apt-get install -y curl \
  && curl -o /tmp/apache-maven-3.9.8-bin.tar.gz http://mirror.olnevhost.net/pub/apache/maven/maven-3/3.9.8/binaries/apache-maven-3.9.8-bin.tar.gz \
  && tar xzvf /tmp/apache-maven-3.9.8-bin.tar.gz -C /opt \
  && ln -s /opt/apache-maven-3.9.8 /opt/maven

# Mettre à jour le PATH pour Maven
ENV MAVEN_HOME=/opt/maven
ENV PATH=${MAVEN_HOME}/bin:${PATH}

# Set the working directory
WORKDIR /app

# Copy the POM and source code
COPY pom.xml ./ 
COPY src ./src

# Download the dependencies and build the project
RUN mvn clean package -DskipTests -Dmaven.compiler.release=17

# Step 2: Use JRE for running the application
FROM openjdk:21-slim

# Copy the built jar file from the build stage
COPY --from=build /app/target/SpringBoot-0.0.1-SNAPSHOT.jar /app/SpringBoot-0.0.1-SNAPSHOT.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/SpringBoot-0.0.1-SNAPSHOT.jar"]

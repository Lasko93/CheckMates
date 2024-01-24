FROM maven:3.9.1-amazoncorretto-19-debian-bullseye AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean install -DskipTests

# Use an Ubuntu base image
FROM ubuntu:20.04

# Avoid prompts from apt
ARG DEBIAN_FRONTEND=noninteractive

# Install wget and necessary dependencies
RUN apt-get update && \
    apt-get install -y wget && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Download and install OpenJDK 19
RUN wget https://download.java.net/java/GA/jdk19.0.2/fdb695a9d9064ad6b064dc6df578380c/7/GPL/openjdk-19.0.2_linux-x64_bin.tar.gz && \
    tar -xvf openjdk-19.0.2_linux-x64_bin.tar.gz && \
    mv jdk-19.0.2 /opt/ && \
    rm openjdk-19.0.2_linux-x64_bin.tar.gz

# Set environment variables for Java
ENV JAVA_HOME=/opt/jdk-19.0.2
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Set the working directory
WORKDIR /usr/app

# Copy the compiled JAR file from the build stage
COPY --from=build /usr/src/app/target/chess-app-group-d-0.0.1-SNAPSHOT.jar .

# Copy the Stockfish binary
COPY src/main/java/com/example/chessappgroupd/stockfishlinux/stockfish-ubuntu-x86-64-modern .
# Ensure the Stockfish binary is executable
RUN chmod +x stockfish-ubuntu-x86-64-modern

# Expose the port the application runs on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "chess-app-group-d-0.0.1-SNAPSHOT.jar"]
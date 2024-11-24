FROM openjdk:21
WORKDIR /app
COPY target/projectNIC-0.0.1-SNAPSHOT.jar /app/projectNIC.jar
ENTRYPOINT ["java", "-jar", "/app/projectNIC.jar"]

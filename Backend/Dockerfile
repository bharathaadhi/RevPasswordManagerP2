FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/revpasswordmanagerP2-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","app.jar","--spring.profiles.active=docker"]
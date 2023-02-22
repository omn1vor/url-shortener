FROM openjdk:17-alpine
EXPOSE 8080
COPY target/url-shortener-0.0.1-SNAPSHOT.jar /usr/app/app.jar
WORKDIR /usr/app
CMD ["java", "-jar", "app.jar"]

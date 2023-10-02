FROM gradle:latest

WORKDIR /app

COPY src ./src

COPY build.gradle.kts settings.gradle.kts gradle.properties ./

EXPOSE 8080

CMD ["gradle","build", "run"]
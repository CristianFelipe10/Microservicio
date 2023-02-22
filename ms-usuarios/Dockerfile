FROM openjdk:11.0.12 as builder

WORKDIR /app/ms-usuarios

COPY ./pom.xml /app
COPY ./ms-usuarios/.mvn ./.mvn
COPY ./ms-usuarios/mvnw .
COPY ./ms-usuarios/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./ms-usuarios/src ./src

RUN ./mvnw clean package -DskipTests

#Se crea una imagen a partir de otra para optimizar el dockerfile
FROM openjdk:11.0.12

WORKDIR /app

COPY --from=builder /app/ms-usuarios/target/ms-usuarios-0.0.1-SNAPSHOT.jar .

EXPOSE 8002

ENTRYPOINT ["java", "-jar", "ms-usuarios-0.0.1-SNAPSHOT.jar"]
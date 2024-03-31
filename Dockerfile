FROM openjdk:17-jdk
WORKDIR /server
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} goguma_bookstore_server.jar
CMD ["java", "-jar", "/server/goguma_bookstore_server.jar"]
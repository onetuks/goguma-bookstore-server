FROM openjdk:17-jdk
WORKDIR /server
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} happyparking.jar
CMD ["java", "-jar", "/server/happyparking.jar"]
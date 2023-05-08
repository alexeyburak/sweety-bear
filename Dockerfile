FROM openjdk:17

ARG JAR_FILE=/target/*dependencies.jar

COPY ${JAR_FILE} application.jar

ENTRYPOINT ["java","-jar","application.jar"]
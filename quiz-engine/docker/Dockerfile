FROM openjdk:15-slim

RUN mkdir /quiz-engine
COPY ./../target/*.jar /quiz-engine/quiz-engine.jar
WORKDIR /quiz-engine
CMD "java" "-jar" "quiz-engine.jar"
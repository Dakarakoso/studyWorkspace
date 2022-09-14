FROM openjdk:11-slim as build

LABEL TEAM=Willian.com

COPY ./target/cards-0.0.1-SNAPSHOT.jar /cards-0.0.1-SNAPSHOT.jar

ENTRYPOINT [ "java", "-jar", "/cards-0.0.1-SNAPSHOT.jar" ]
FROM openjdk:11-slim as build

LABEL TEAM=Willian.com

COPY ./target/loans-0.0.1-SNAPSHOT.jar /loans-0.0.1-SNAPSHOT.jar

ENTRYPOINT [ "java", "-jar", "/loans-0.0.1-SNAPSHOT.jar" ]
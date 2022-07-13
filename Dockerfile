FROM ubuntu
FROM java:8

COPY target/practice-0.0.1-SNAPSHOT.jar /demo.jar

CMD ["java", "-jar", "/demo.jar"]
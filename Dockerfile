FROM ubuntu
FROM java:8

COPY target/practice-0.0.1-SNAPSHOT.jar /demo.jar
COPY students.json /students.json

CMD ["java", "-jar", "/demo.jar"]
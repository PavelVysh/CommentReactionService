FROM amazoncorretto:17
COPY target/comments-0.0.2-SNAPSHOT.jar /comments.jar
ENTRYPOINT ["java","-jar","/comments.jar"]
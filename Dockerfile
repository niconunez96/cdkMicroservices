FROM amazoncorretto:17
ARG buildId=latest
ENV buildId=${buildId}
COPY build/libs/*-SNAPSHOT.jar app.jar
CMD java -jar -Dspring.profiles.active=prod /app.jar
EXPOSE 80

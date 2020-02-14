FROM openjdk:8-jdk-alpine
VOLUME /tmp
ENV JAVA_OPTS="-Xms200m -Xmx200m -Dfile.encoding=UTF-8 -agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=n"
ADD target/discovery-1.0.0.jar app.jar
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar

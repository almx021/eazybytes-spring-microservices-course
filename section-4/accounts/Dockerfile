# Import image openjdk:21-jdk-oracle
FROM openjdk:21-jdk-oracle

LABEL maintainer="andré lucas"

# Copy local jar and past in the container
COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

# Run this command when executing the container
ENTRYPOINT ["java", "-jar", "accounts-0.0.1-SNAPSHOT.jar"]
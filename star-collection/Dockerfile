FROM amazoncorretto:22

# Install necessary packages
RUN yum install -y tar which gzip openssh-clients findutils

# Set up Maven environment
ENV MAVEN_HOME=/usr/share/maven
COPY --from=maven:3.9.8-eclipse-temurin-11 ${MAVEN_HOME} ${MAVEN_HOME}
COPY --from=maven:3.9.8-eclipse-temurin-11 /usr/local/bin/mvn-entrypoint.sh /usr/local/bin/mvn-entrypoint.sh
COPY --from=maven:3.9.8-eclipse-temurin-11 /usr/share/maven/ref/settings-docker.xml /usr/share/maven/ref/settings-docker.xml

RUN ln -s ${MAVEN_HOME}/bin/mvn /usr/bin/mvn

# Set Maven environment variables
ARG MAVEN_VERSION=3.9.8
ARG USER_HOME_DIR="/root"
ENV MAVEN_CONFIG="$USER_HOME_DIR/.m2"

# Copy the application code
WORKDIR /app
COPY . /app

# Build the application
RUN /usr/local/bin/mvn-entrypoint.sh mvn clean package -DskipTests

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/star-collection-0.0.3.jar"]
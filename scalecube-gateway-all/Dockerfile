FROM anapsix/alpine-java:8_jdk
MAINTAINER info@scalecube.io

# Add Maven dependencies (not shaded into the artifact; Docker-cached)
COPY target/lib                                             /usr/dev/root/lib

# copy package information.
COPY target/maven-archiver/pom.properties                   /usr/dev/root/lib/package.properties

# Add the service itself
COPY target/server.jar                                      /usr/dev/root/server.jar

EXPOSE 8080

ENV REDIS_PORT 6379
ENV REDIS_HOST 172.17.0.6
ENV GATEWAY_PORT 8080
ENV SC_HOME=/usr/dev/root/

ENTRYPOINT ["java", "-jar", "/usr/dev/root/server.jar"]

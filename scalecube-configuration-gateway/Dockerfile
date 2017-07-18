FROM anapsix/alpine-java:8_jdk
MAINTAINER info@scalecube.io

# Add Maven dependencies (not shaded into the artifact; Docker-cached)
COPY target/lib                                             /usr/dev/root/lib

# copy package information.
COPY target/maven-archiver/pom.properties                   /usr/dev/root/lib/package.properties

# Add the service itself
COPY target/server.jar                                      /usr/dev/root/server.jar

EXPOSE 8081
EXPOSE 4801
EXPOSE 4802

ENV API_GATEWAY_PORT 8081
ENV SC_HOME=/usr/dev/root/

ENTRYPOINT ["java", "-jar", "/usr/dev/root/server.jar"]

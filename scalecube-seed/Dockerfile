FROM anapsix/alpine-java:8_jdk
MAINTAINER info@scalecube.io

ENV SC_USER_HOME=/usr/dev/root

# Add Maven dependencies (not shaded into the artifact; Docker-cached)
COPY target/lib                                             /usr/dev/root/lib

# copy package information.
COPY target/maven-archiver/pom.properties                   /usr/dev/root/lib/package.properties

# Add the service itself
COPY target/server.jar                                      /usr/dev/root/server.jar

# Cluster control port.
EXPOSE 4801  

# Service communication port.
EXPOSE 4802  

ENV SC_HOME=/usr/dev/root/lib/
ENV SC_SEED_ADDRESS=172.17.0.2

ENTRYPOINT ["java", "-jar", "/usr/dev/root/server.jar"]

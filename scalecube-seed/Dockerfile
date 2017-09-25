FROM anapsix/alpine-java:8_jdk

MAINTAINER info@scalecube.io

# copy package information.
ADD target/maven-archiver/pom.properties                   /usr/dev/root/package.properties

# Add the service itself
ADD target/server-jar-with-dependencies.jar                                      /usr/dev/root/server.jar

# Cluster control port and communication port.
EXPOSE 4801 4802

###################################################################
# Environment Variables:
# -----------------------------------------------------------------
# SC_HOME:         the root folder for a scalecube node.
# SC_SEED_ADDRESS: the list of seed nodes to join a cluster
###################################################################

ENV SC_HOME="/usr/dev/root/" \
    SC_SEED_ADDRESS="172.17.0.2:4802, 172.17.0.3:4802, 172.17.0.4:4802"

ENTRYPOINT ["java", "-jar", "/usr/dev/root/server.jar"]

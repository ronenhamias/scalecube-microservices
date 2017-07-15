FROM java

# Add Maven dependencies (not shaded into the artifact; Docker-cached)
COPY target/lib                                             /usr/dev/root/lib

# Add index html as demo file.
COPY src/main/java/io/scalecube/gateway/all/index.html      /usr/dev/root/lib/index.html

# copy package information.
COPY target/maven-archiver/pom.properties                   /usr/dev/root/lib/package.properties

# Add the service itself
COPY target/server.jar                                      /usr/dev/root/server.jar

EXPOSE 8080

ENV REDIS_PORT 6379
ENV REDIS_HOST 172.17.0.6
ENV GATEWAY_PORT 8080
ENV SC_HOME=/usr/dev/root/lib/

ENTRYPOINT ["java", "-jar", "/usr/dev/root/server.jar", "/usr/dev/root/lib/"]

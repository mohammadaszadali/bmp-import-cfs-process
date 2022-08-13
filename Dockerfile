FROM adoptopenjdk/openjdk8:latest
ADD target/import-cfs-process-1.0.jar import-cfs-process.jar
RUN sh -c 'touch /import-cfs-process.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/import-cfs-process.jar"]
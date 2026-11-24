# Imagen de runtime: Tomcat 9 + Java 8 (según el readme del proyecto).
# El WAR se genera antes con: mvn clean package
# (en el pipeline, el WAR se descarga como artefacto del job build-and-test).
FROM tomcat:9.0-jdk8-temurin

# Elimina las aplicaciones de ejemplo de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

COPY target/DSS_P4.war /usr/local/tomcat/webapps/DSS_P4.war

EXPOSE 8080

CMD ["catalina.sh", "run"]

FROM tomcat:10.1-jdk17-temurin

RUN rm -rf /usr/local/tomcat/webapps/*
COPY DenCli/target/*.war /usr/local/tomcat/webapps/DenCli.war

EXPOSE 8080
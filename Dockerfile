FROM openjdk:21
EXPOSE 8080
ADD target/*.jar  kron-app.jar
ENTRYPOINT [ "java","-jar","kron-app.jar" ]

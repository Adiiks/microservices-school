
# Microservices Application - School System API

I created that project to learn and test my skills in developing microservices application. I am using Java and Spring to write that application also I'm using docker and docker-compose to run necessary services such as MySQL (database for all services) and Keycloak (authorization server). Below I describe every service inside my architecture.






## Configserver

This is configuration server which contains all configurations for other services. The config files are located in github under this link: https://github.com/Adiiks/microservices-school-configs.
## Eureka Server

This is a service dicovery. It contains locations of services and also check from time to time if particular service is up or down. Every service during start register itself with eureka server.
## Gateway Server

This is service which acts as an entry point to the application. Every request come first to this service and after authentication and authorization success it passes request to particular service.
## Docker Compose

Docker compose file for running services. Currently only contains Configserver, Eureka server, MySQL and Keycloak.
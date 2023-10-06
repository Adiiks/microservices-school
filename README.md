
# Microservices Application - School System API

I created that project to learn and test my skills in developing microservices application. I am using Java and Spring to write that application also I'm using docker and docker-compose to run necessary services such as MySQL (database for all services) and Keycloak (authorization server). Below I describe every service inside my architecture.






## Configserver (Spring Cloud Config)

This is configuration server which contains all configurations for other services. The config files are located in github under this link: https://github.com/Adiiks/microservices-school-configs.
## Eureka Server (Spring Cloud Netflix)

This is a service dicovery. It contains locations of services and also check from time to time if particular service is up or down. Every service during start register itself with eureka server.
## Gateway Server (Spring Cloud Gateway)

This is service which acts as an entry point to the application. Every request come first to this service and after authentication and authorization success it passes request to particular service.
## User Service

Service for managing users. We have three types of user: Admin, Teacher and Student. Service offers following features:

- creating students and teachers (saving them in Keycloak Server)
- searching users (pagination) by names, lastname
- getting list of users (pagination) based on faculty name
- checking if teacher exists based on ID
- getting list of teachers based on list of IDs
- checking if student exists based on username
## Faculty Service

Service for managing faculties (example: Faculty of Computer Science). Service offers following features:

- creating new faculty
- checking if faculty with particular id exists
- getting list of faculties based on passed list of ids
- getting ID of faculty based on faculty name
- getting list of faculties (pagination)
## Course Service

Service for managing courses. Service offers following features:

- creating new course
- checking if course exists based on ID
- getting list of courses based on list of IDs
## Lesson Service

Service for managing lessons. Service offers following features:

- creating new lesson
- opening registration for lesson
- getting list of registrations (pagination)
- student can register or unregister to lesson
- checking if lesson exists based on ID
## Attendance Service

Service for managing attendacies. Service offers following features:

- creating list of attendacies of students
## Docker Compose

Docker compose file for running services. Currently only contains Configserver, Eureka server, MySQL and Keycloak.
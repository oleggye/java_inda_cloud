### Technologies overview:
- `Service Discovery` using Netflix Eureka.
- `Client side load balancing` using Netflix Ribbon.
- `API Gateway ` using Netflix Zuul. Configured routes to the both `pc` and `po` services using 
service discovery information and prevented other routing. Implemented simple fallback strategy
and verified using integration tests.
- `Circuit Breaker pattern` using Netflix Hystrix.
- `Open Feign` - an http client that has already integrated with Ribbon and Eureka. 
- `Metrics` using `Spring Actuator`.
- `Distributed tracing` using Spring Cloud Sleuth to implement tracing and Open Zipkin server 
to collect and display it.
- `Containerization`. Docker images and Docker compose for automating deployment.

### Pros & Cons of the current project

#### Pros
- loose coupling and high cohesion
- environment isolation
- great scalability and resilience potential
- can control codebase and using technologies per service due to unified communication interface

#### Cons
- automation of environments scaling and management (need an Orchestrator)
- services have to support Eureka discovery (technology restriction)
- latency increased due to division on services
- the gateway become the point of failure
- difficult to organize and support CI/CD with the growth
- need to spend more time for integration and system testing
- resource consumption
- security issues
- fixed configuration           
                             
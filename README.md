# Banking-Ledger-With-Event-Driven-Architecture

## Bootstrap instructions
*To run this server locally, do the following:*

1. Update application.properties or application.yml for your specific database settings.

2. You can run the application using: mvn spring-boot:run

3. Once the server is running, the API can be accessed at:
Base URL: http://localhost:8080

4. All APIs use the 'PUT' method


## Design considerations

I decided to build the Simple Bank Ledger System using Spring Boot for the following reasons:

1. Ease of Development and Testing: Spring Boot provides a comprehensive set of tools and features that simplify the development process. It has a minimalistic configuration and helps speed up development with various built-in components.
2. Microservices Architecture: With the rise of microservices, Spring Boot is a natural choice due to its lightweight and modular design. It provides an easy way to build standalone microservices that can be deployed in various environments.
3. Spring Ecosystem Integration: Being part of the Spring ecosystem, Spring Boot integrates seamlessly with Spring's wide array of libraries, which include Spring Data for database operations and Spring Security for securing the application.
4. Scalability and Performance: Spring Boot allows for the creation of scalable applications that can handle high volumes of transactions efficiently. Its embedded server options like Tomcat or Jetty enable quick startup times, ideal for cloud deployments.

I chose MySQL to store the transactions for these reasons:

1. Relational Database Capabilities: MySQL provides robust support for ACID transactions, which ensures the integrity of the transaction history stored in the database.
2. Reliability and Scalability: MySQL has proven reliability and is known for its scalability, making it a suitable choice for applications handling potentially high volumes of banking transactions.
3. ORM Compatibility: MySQL integrates seamlessly with Spring Data JPA, allowing for simplified data access and management through object-relational mapping (ORM).

## Assumptions

While designing this service, I have made the assumption that:

1. If a load request is made to a non-existent account, a new account is created with that specific userId. 

2. If an authorization request is made to a non-existent account, the transaction is declined and is saved in the database.

## Bonus: Deployment considerations

If I were to deploy this Simple Bank Ledger System application, I would choose a cloud provider like AWS, Azure, or Google Cloud for scalable infrastructure. The application would be packaged using Docker to ensure consistent deployment across environments. I would use Kubernetes for orchestration to facilitate easy scaling, self-healing, and rolling deployments. A continuous integration and delivery (CI/CD) pipeline, such as Jenkins or GitHub Actions, would automate testing and deployment.

To manage traffic and ensure high availability, a load balancer would distribute traffic across multiple instances of the application, with auto-scaling to adjust the number of running instances based on demand. 

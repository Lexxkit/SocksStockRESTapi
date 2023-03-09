### SocksStockREST App
Test task from SKYPRO. Simple RESTful app for automating socks warehouse via dedicated endpoints.

#### Run
Project uses containerization with Docker Compose for PostgreSQL.
```
In IDE:
  1. clone git repository https://github.com/Lexxkit/SocksStockRESTapi.git
  2. run Docker on your machine
  3. open 'docker-compose.yaml'  in project and run it OR type 'docker-compose up' in a terminal
  4. run 'SocksStockAutApplication'
```

#### Documentation
- JavaDoc available at: https://lexxkit.github.io/SocksStockRESTapi/
- SwaggerUI available after launching the app at: http://localhost:8080/api/swagger-ui/index.html
- DB schema image available at: https://github.com/Lexxkit/SocksStockRESTapi/blob/feat/create-app/DB_schema/App_DB_schema.png

#### Stack
```
Java 17
Spring Boot 2
Maven
SpringDoc OpenAPI UI
Mapstruct
Lombok
Liquibase
PostreSQL
```

#### Functionality
Create an application to automate the accounting of socks in the store. The storekeeper should be able to:

- record the income and outcome of socks;
- find out the total amount of socks of a certain color and composition at a given time.

The application's external interface is represented as an HTTP API (REST).
##### List of HTTP URL methods
###### POST /api/socks/income
Records socks income
###### POST /api/socks/income
Records socks outcome
###### GET /api/socks?color=red&operation=moreThan&cottonPart=90
Returns the total number of socks in stock that match the query criteria passed in the parameters.

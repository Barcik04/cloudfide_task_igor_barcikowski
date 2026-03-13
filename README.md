### ABOUT
    E-commerce platform task by Igor Barcikowski

    Note: Integration tests cover the most important methods.


### DATA RELATIONS DIAGRAM
<img width="1343" height="371" alt="image" src="https://github.com/user-attachments/assets/fc3db70e-8e46-4813-ac20-d084ff7b61ef" />

    
### List of functions
    ## product-controller
        --Adds an attribute to the product identified by the given productId
        --Deletes the product identified by the given productId
        --Partially updates product fields by productId, excluding attributes
        --Creates a product for the given producer with the provided list of attributes
        --Partially updates attribute fields for the specified productId and attributeId
        --Retrieves all products with dynamic filtering
        --Deletes an attribute from the specified product using productId and attributeId
    
    ## producer-controller
        --Retrieves all producers in a paginated format
        --Creates a new producer
        --Deletes the producer identified by the given producerId
        --Partially updates the producer identified by the given producerId
    
### STACK
    ## Core 
    - Java 21
    - Spring Boot 3.4.13
    - Maven

    ## Data
    - H2
    - Spring Data JPA (Hibernate)
    - Liquibase

    ## Validation & API
    - Spring Validation
    - REST API
    - Springdoc OpenAPI (Swagger)

    ## Tests
    - Junit5
    - Spring Boot Test

### ACCESS TO
    1. Swagger API: http://localhost:8080/swagger-ui/index.html#
    2. H2 console: http://localhost:8080/h2-console

### FILTERING
    System supports dynamic filtering feature with pagination.
    Here are a couple of examples of how to use fitlering:
        -- GET /api/v1/products/get-all?q=phone
        -- GET /api/v1/products/get-all?minPrice=100&maxPrice=500
        -- GET /api/v1/products/get-all?producerName=samsung&sort=productName,asc

### VALIDATION AND ERROR HANDLING
    -- request validation is handled by Bean Validtaion 
    -- Invalid input or illegal actions return custom error messages handled in GlobalExceptionHandler class


### HOW TO RUN
    ## IntelliJ:
        1. clone project from github
        2. Open the project
        3. make sure maven project is loaded
        4. run application
        
    ### VS Code / Other
        1. Clone the repository from GitHub
        2. Open the project
        3. Make sure Java 21 and Maven are installed
        4. Run the application using:
        
        ```bash
        mvn spring-boot:run

### USAGE NOTES
    - To create a product, an existing producer is required.
    - Product attributes can be passed during product creation or added later.
    - Swagger contains default example values for easier testing.
    - Products endpoint supports dynamic filtering, pagination, and sorting.
    - To create a productAttribute, an existing product is required.


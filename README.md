### ABOUT
    This project is a URL shortening platform built as a REST API.
    
    It allows users to:
        - register and verify accounts via email,
        - create short URLs with optional expiration dates,
        - track click statistics for each link,
        - manage their own URL collection.
    
    The system focuses on security, observability and asynchronous processing.


### SYSTEM
    --Register/Login Jwt tokenized authentication
    --Managing users links
    --Creating clickable shorted links


### TECH STACK
    ## Core
    - Java 21
    - Spring Boot 4.0.2
    - Maven

    ## Data & Persistence
    - PostgreSQL 17
    - Sping Data JPA (Hibernate)
    - Fly (db migrations)

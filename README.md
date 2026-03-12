# URL Shortener Service

A Spring Boot backend service that converts long URLs into short links, with Redis caching and PostgreSQL persistence.

---

## Prerequisites

- Java 17+
- Maven
- Docker Desktop

---

## Getting Started

### 1. Clone and open the project in IntelliJ

### 2. Start PostgreSQL and Redis with Docker

I have added docker-compose.yml file in this project

Then run:

```bash
docker-compose up -d
```

Verify both containers are running:

```bash
docker ps
```

### 3. Run the Spring Boot App

```bash
./mvnw spring-boot:run
```

---

## Testing with Postman

A Postman collection is included in the project root:

```
url-shortener.postman_collection.json
```

### Import the Collection

1. Open Postman
2. Click Import
3. Upload `url-shortener.postman_collection`
4. All requests can be seen

### Testing Order

```
Step 1 → POST   /api/shorten          Create a short URL and note the shortCode in response
Step 2 → GET    /{shortCode}          Paste in browser. It should redirect automatically
Step 3 → GET    /api/stats/{code}     Check click count increased after redirect
Step 4 → DELETE /api/links/{code}     Delete link
```

---

## Checking the Database

```bash
docker exec -it urlshortener-postgres-1 psql -U postgres -d urlshortener

SELECT * FROM urls;


```

---

## Checking Redis Cache

```bash
docker exec -it urlshortener-redis-1 redis-cli

KEYS *

```

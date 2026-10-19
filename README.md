# sample-crm

Sample CRM repository for AI testing.

A small CRM back end for companies, contacts, and deals, with an HTTP JSON API. Java 25, Spring Boot 4, PostgreSQL. See `PRODUCT_INTENT.md` for what it is and `specs/` for how work is done.

## Requirements

- Java 25
- Docker, running (tests and the local run start PostgreSQL in a container)

## Build and test

```sh
./mvnw verify
```

## Run

```sh
./mvnw spring-boot:test-run
```

This starts the API on `http://localhost:8080` with a throwaway PostgreSQL in a container. To use your own database instead, set `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` and run `./mvnw spring-boot:run`.

## API

| Method and path | Does |
| --- | --- |
| `POST /companies`, `GET /companies` | Create a company, list all companies |
| `GET`, `PUT`, `DELETE /companies/{id}` | Read, replace, delete a company |
| `POST /contacts` | Create a contact |
| `GET /contacts?page=1&size=20&companyId=&name=` | List contacts a page at a time, optionally for one company or by name |
| `GET`, `PUT`, `DELETE /contacts/{id}` | Read, replace, delete a contact |
| `POST /deals`, `GET /deals?companyId=` | Create a deal, list all deals or one company's |
| `GET`, `PUT`, `DELETE /deals/{id}` | Read, replace, delete a deal |
| `GET /v3/api-docs` | OpenAPI description of the API |

Bad input gets a `400` problem response that names each wrong field. A missing record gets a `404`. Deleting a company or contact keeps the records that refer to it and clears the reference.

```sh
curl -X POST localhost:8080/companies -H 'Content-Type: application/json' \
  -d '{"name": "Acme", "website": "https://acme.example"}'
```

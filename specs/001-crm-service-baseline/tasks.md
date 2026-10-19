# 001 · Tasks: CRM service baseline

## Tasks

- [x] **T1** Scaffold the project: `pom.xml` (Boot 4.1.1 parent, Java 25, every dependency in the plan), Maven Wrapper, `SampleCrmApplication`, `application.yml`, empty Flyway location, `TestcontainersConfiguration`, `TestSampleCrmApplication`, `ApiTestBase`, and a smoke test that starts the app against PostgreSQL · `pom.xml`, `mvnw`, `.mvn/`, `src/main/...`, `src/test/.../SmokeTest.java` · check: `./mvnw verify` passes with Docker running → AC-22
- [x] **T2** [P] Add the CI workflow that runs `./mvnw -B verify` on every push and pull request · `.github/workflows/ci.yml` · check: workflow file is valid YAML and the command runs locally; the CI run itself is checked in `/sdd-verify` → AC-23
- [x] **T3** [P] Error format and companies, test first: `CompanyApiTest` (create, read, update, delete, list, unknown id, bad input), then `V1__companies.sql`, the `company` package, and the `api` package (`ApiExceptionHandler`, `BadInputException`, `NotFoundException`, `FieldErrorItem`) · `src/main/.../company/`, `src/main/.../api/`, `db/migration/V1__companies.sql`, `CompanyApiTest` · check: `./mvnw verify` → AC-1, AC-5, AC-6, AC-7, AC-8, AC-16
- [x] **T4** Contacts CRUD, test first: `ContactApiTest` (create with and without company, read, update, delete, unknown id, bad input incl. unknown company, every wrong field, delete company keeps contacts), then `V2__contacts.sql` with `on delete set null` and the `contact` package · `src/main/.../contact/`, `db/migration/V2__contacts.sql`, `ContactApiTest` · check: `./mvnw verify` → AC-2, AC-3, AC-5, AC-6, AC-7, AC-16, AC-17, AC-18
- [x] **T5** [P] Contact list, test first: `ContactListApiTest` (page and stable order, defaults, invalid paging, by company, by name, past last page), then `PageResponse`, paging parameters with method validation, and the contact `Specification` · `src/main/.../api/PageResponse.java`, `src/main/.../contact/ContactController.java`, `ContactService.java`, `ContactRepository.java`, `ContactListApiTest` · check: `./mvnw verify` → AC-10, AC-11, AC-12, AC-13, AC-14, AC-15
- [x] **T6** [P] Deals, test first: `DealApiTest` (create, read, update, delete, unknown id, list all and by company, bad input incl. negative amount and unknown company or contact, delete company keeps deals, delete contact keeps deals, update after company deleted), then `V3__deals.sql` and the `deal` package · `src/main/.../deal/`, `db/migration/V3__deals.sql`, `DealApiTest` · check: `./mvnw verify` → AC-4, AC-5, AC-6, AC-7, AC-9, AC-16, AC-19, AC-20, AC-25
- [x] **T7** API description: `ApiDescriptionTest` fetches `/v3/api-docs` and checks every operation of R1–R15; set the API title and version · `src/main/.../SampleCrmApplication.java` or `api/OpenApiConfig.java`, `ApiDescriptionTest` · check: `./mvnw verify` → AC-21
- [x] **T8** Document the build, test, and run commands and update the current state · `CLAUDE.md`, `README.md` · check: commands in the docs run as written from a clean checkout → AC-22

## Verification

- [ ] **V1** Every acceptance criterion verified, with evidence below
- [ ] **V2** PLAN.md and BACKLOG.md updated for this spec

## Evidence

| Acceptance criterion | Result | Evidence (test, command output, manual check) |
| --- | --- | --- |
| AC-1 | | |
| AC-2 | | |
| AC-3 | | |
| AC-4 | | |
| AC-5 | | |
| AC-6 | | |
| AC-7 | | |
| AC-8 | | |
| AC-9 | | |
| AC-10 | | |
| AC-11 | | |
| AC-12 | | |
| AC-13 | | |
| AC-14 | | |
| AC-15 | | |
| AC-16 | | |
| AC-17 | | |
| AC-18 | | |
| AC-19 | | |
| AC-20 | | |
| AC-21 | | |
| AC-22 | | |
| AC-23 | | |
| AC-24 | | |
| AC-25 | | |

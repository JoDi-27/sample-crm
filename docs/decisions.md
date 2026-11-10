# Decisions

The register of decisions. Settled choices get a `DEC-nn` row; open ones keep their `ND-nn` row under "Still open", with a one-line item in [BACKLOG.md](../BACKLOG.md). A spike's Recommendation, or a plan's cross-cutting Decisions row, settles an ND into a DEC here with the same reasoning, and the BACKLOG item is ticked in the same change. Plans cite the `DEC-nn` they rely on.

## Decided

All of these were made before the kit moved to the plugin. Most come from spec 001's plan (its `Dn` rows and Deviations); the reasons there are summarized here.

| ID | Decision | Choice | Why | Date |
|---|---|---|---|---|
| DEC-01 | Technology | Java with Spring Boot and a REST API, PostgreSQL, Testcontainers for tests, GitHub Actions for CI | Decided by the owner before spec 001 (old `PRODUCT_INTENT.md` "Technology") | 2026-10-18 |
| DEC-02 | Build tool | Maven with the Maven Wrapper (Maven 3.9.16); `distributionUrl` set to Maven Central by hand | Maven was installed and the wrapper pins the version for CI; the generated URL pointed at a private mirror (001 D1) | 2026-10-18 |
| DEC-03 | Java version | Java 25 | Current LTS (001 D2) | 2026-10-18 |
| DEC-04 | Framework | Spring Boot 4.1.1 with Spring MVC and Jackson 3 | Latest stable release (001 D3) | 2026-10-18 |
| DEC-05 | Persistence | Spring Data JPA with plain `Long` id references, no JPA associations; JPA `Specification` for contact filters | Most common choice, with paging and filters built in; plain ids leave reference clearing to the database (001 D4) | 2026-10-18 |
| DEC-06 | Schema migrations | Flyway, with Hibernate `ddl-auto: validate` | Versioned SQL later specs can extend; `ddl-auto` cannot express `ON DELETE SET NULL` (001 D5) | 2026-10-18 |
| DEC-07 | Deleting referenced records | Records that point at a deleted company or contact stay, and the reference is cleared by `ON DELETE SET NULL`. Nothing cascades and no delete is refused. A deal needs `companyId` on create but not on update. | The database guarantees P3 even under concurrent requests (001 D6, R17–R19) | 2026-10-18 |
| DEC-08 | Error format | RFC 9457 problem details with an `errors` list of `{field, message}`; the default `type` is left out | Built into Spring and a standard (001 D7 and Deviation) | 2026-10-18 |
| DEC-09 | Where validation lives | Written out by hand in each controller's create and update handler, collecting every problem (references included) before one `BadInputException`; `@Min`/`@Max` only on paging parameters. Email uses a hand-written regex. | Each operation's rules can be read where the request is handled, and every wrong field is still named in one answer. Supersedes 001 D8. (001 Deviation) | 2026-10-19 |
| DEC-10 | Ids | `bigint` identity values, not UUIDs | Simpler and enough for one service (001 D9) | 2026-10-18 |
| DEC-11 | Contact paging | `page` from 1, `size` 1–100 (default 20), `items/page/size/total`, sorted by id; the offset is computed in code (`OffsetPage`) | Matches the spec directly; id order is stable and cheap (001 D10 and Deviation) | 2026-10-19 |
| DEC-12 | Field length limits | Names and titles 200, first and last names 100, email 254, phone 50, website 500; amounts `numeric(15,2)`, 0 or more | Fit real data; 254 is the practical email maximum (001 D15) | 2026-10-18 |
| DEC-13 | API description | springdoc-openapi 3.1.1, JSON at `/v3/api-docs`, no Swagger UI | Generated from the code so it cannot drift; no UI is needed (001 D11) | 2026-10-18 |
| DEC-14 | Test style | HTTP-level `@SpringBootTest(RANDOM_PORT)` tests against one shared Testcontainers PostgreSQL, tables truncated before each test | Proves each criterion end to end against the real database, and stays fast (001 D12) | 2026-10-18 |
| DEC-15 | Test database switch | Testcontainers starts only when `DB_URL` is empty; CI passes in a `postgres:18-alpine` service container through `DB_URL` | One switch, using the variables `application.yml` already reads (001 D16) | 2026-10-18 |
| DEC-16 | CI | GitHub Actions on every push and pull request, Ubuntu, `./mvnw -B verify` | Covers every pushed commit; Ubuntu runners have Docker (001 D13) | 2026-10-18 |
| DEC-17 | Local run | `./mvnw spring-boot:test-run` with Testcontainers, no compose file | No extra file to keep in sync, and the same database setup as the tests (001 D14) | 2026-10-18 |

## Still open

| ID | Decision | Options | Current leaning | Source |
|---|---|---|---|---|

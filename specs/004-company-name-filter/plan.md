# 004 · Plan: Company name filter

## Approach

Add an optional `name` query parameter to `GET /companies`. The controller passes it to the service. The service uses a repository query that matches names containing the text, ignoring case. A missing or empty `name` keeps today's call.

## Constitution check

| Article | How the plan complies |
| --- | --- |
| P1 API only | The filter is a query parameter on the HTTP list. |
| P4 Easy to run | Tests run on real PostgreSQL through `ApiTestBase`. |
| P5 Small and conventional | Spring Data derived query. No new dependency. |
| Eng. 1 Happy path first | No paging, no other filters (out of scope). |
| Eng. 2 Tests prove ACs | One test per AC in a new test class. |
| Eng. 3 Small increments | One task, one commit. |
| Eng. 6 Verify current docs | No new library. Spring Data JPA comes from the Spring Boot version already in `pom.xml`; the derived `ContainingIgnoreCase` keyword is long-standing. |
| Architecture checklist | Controller stays package-private, the service keeps `@Transactional(readOnly = true)` on the read, and the list stays sorted by id. |

## Design

- `CompanyRepository`: add `List<Company> findByNameContainingIgnoreCase(String name, Sort sort)`.
- `CompanyService.list(String name)`: when `name` is null or empty, `findAll(Sort.by("id"))` as now. Otherwise the new query with `Sort.by("id")`.
- `CompanyController.list(@RequestParam(required = false) String name)`: passes it on.
- Wildcard characters in `name` are not specified by the spec, so the plan promises nothing about them.
- No schema change, no migration.

## Requirement coverage

| Requirement | Covered by |
| --- | --- |
| R1 | Repository query, service branch for a non-empty name, controller parameter |
| R2 | Service branch for a null or empty name, which keeps the existing call |

## Decisions

| # | Decision | Alternatives | Why |
| --- | --- | --- | --- |
| 1 | Derived query `findByNameContainingIgnoreCase` | A JPQL `@Query` with `lower(...) like`; Specifications | Smallest code and mainstream. |
| 2 | Empty `name` is treated as no filter in the service | A 400 for empty | R2 says empty returns every company. |

## Test strategy

| Acceptance criterion | Verified by |
| --- | --- |
| AC-1 | `CompanyListApiTest.filterIgnoresCase`: integration, three companies, `name=acme` |
| AC-2 | `CompanyListApiTest.filterWithNoMatch`: integration, `name=zzz` returns `[]` |
| AC-3 | `CompanyListApiTest.noFilterReturnsAll`: integration, no `name`, then `name=`. R2 keeps today's behavior, so this is a regression guard that passes on the current code too. AC-1 and AC-2 are the tests that fail first. |

## Risks and unknowns

- None known. `ApiTestBase` truncates all tables before each test, so the tests can assert on exact lists.

## Deviations

| # | Deviation |
| --- | --- |
| 1 | auto: implement.stuck: T1's check used `+` to join two test classes, which Surefire does not accept ("No tests matching pattern"). It now uses `,`. The same two classes run, so the check is not weakened. |

## Amendments

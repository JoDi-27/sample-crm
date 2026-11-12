# 005 · Plan: Deal list contact filter

## Approach

Add an optional `mainContactId` query parameter to `GET /deals`, next to `companyId`. The controller passes both to the service. The service picks a repository query by which filters are present. Spring already rejects a non-numeric `Long` parameter with a 400 that names it, which covers R5.

## Constitution check

| Article | How the plan complies |
| --- | --- |
| P1 API only | The filter is a query parameter on the HTTP list. |
| P2 Clear errors | A non-numeric value gives a 400 naming `mainContactId` through the existing type-mismatch handler. |
| P4 Easy to run | Tests run on real PostgreSQL through `ApiTestBase`. |
| P5 Small and conventional | Spring Data derived queries. No new dependency. |
| Eng. 1 Happy path first | No paging, no other filters (out of scope). |
| Eng. 2 Tests prove ACs | One test per AC in a new test class. |
| Eng. 3 Small increments | One task, one commit. |
| Eng. 6 Verify current docs | No new library. Spring Data JPA comes from the Spring Boot version already in `pom.xml`. |
| Architecture checklist | Controller stays package-private, the service keeps `@Transactional(readOnly = true)` on the read, the list stays sorted by id, no schema change. |

## Design

- Parameter name: `mainContactId`, as the deal's field and the create/update request already call it.
- `DealRepository`: add `findByMainContactId(long, Sort)` and `findByCompanyIdAndMainContactId(long, long, Sort)`.
- `DealService.list(Long companyId, Long mainContactId)`: both null uses `findAll`; only company uses the existing query; only contact uses the new one; both uses the combined one. An unknown contact matches no row, so the list is empty (R3). No existence check.
- `DealController.list`: adds `@RequestParam(required = false) Long mainContactId`.
- No schema change, no migration.

## Requirement coverage

| Requirement | Covered by |
| --- | --- |
| R1 | Repository `findByMainContactId`, service branch, controller parameter |
| R2 | Repository `findByCompanyIdAndMainContactId`, service branch |
| R3 | No existence check, so an unknown id returns an empty list |
| R4 | Service keeps the existing calls when no contact filter is given |
| R5 | `Long` parameter type and the existing type-mismatch handler |

## Decisions

| # | Decision | Alternatives | Why |
| --- | --- | --- | --- |
| 1 | Name the parameter `mainContactId` | `contactId` | Matches the deal field and request. |
| 2 | Derived queries, one per filter combination | Specifications; one JPQL query with optional filters | Smallest code and mainstream. DEC-05 chose Specifications for contact filters; deals differ because there are only two combinations. |

## Test strategy

| Acceptance criterion | Verified by |
| --- | --- |
| AC-1 | `DealListApiTest.filterByContact`: integration, deals D1, D3 for A and D2 for B |
| AC-2 | `DealListApiTest.filterByCompanyAndContact`: integration |
| AC-3 | `DealListApiTest.unknownContactIsEmpty`: integration, id 999 returns `[]` |
| AC-4 | `DealListApiTest.noContactFilterUnchanged`: integration: deals with different contacts and companies; no filter returns all in id order, company-only returns its deals in id order. A regression guard on purpose: it passes on the current code, while AC-1, AC-2, AC-3, and AC-5 in the same task fail first, so the task check fails before the change |
| AC-5 | `DealListApiTest.invalidContactFilter`: integration, parameterized over `abc` and `1.5`: 400 with `errors[0].field == mainContactId`, deal count unchanged (a GET changes nothing) |

## Risks and unknowns

- None known. `ApiTestBase` truncates all tables before each test, so the tests can assert on exact lists.

## Deviations

| # | Deviation |
| --- | --- |

## Amendments

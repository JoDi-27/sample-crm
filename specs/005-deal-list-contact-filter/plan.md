# 005 · Plan: Deal list contact filter

## Approach

Add an optional `mainContactId` query parameter to `GET /deals`, next to `companyId`. The controller passes both to the service. The service picks a repository query by which filters are present. A bad value is rejected by Spring's type conversion; the existing `ApiExceptionHandler` already turns that into a 400 that names the parameter.

## Constitution check

| Article | How the plan complies |
| --- | --- |
| P1 API only | The filter is a query parameter on the HTTP list. |
| P2 Clear errors | A non-numeric value gives a 400 naming `mainContactId` (existing handler for type mismatch). |
| P3 No dangling references | Nothing is written or deleted. |
| P4 Easy to run | Tests run on real PostgreSQL through `ApiTestBase`. |
| P5 Small and conventional | Spring Data derived queries. No new dependency. |
| Eng. 1 Happy path first | No paging, title, or amount filters (out of scope). |
| Eng. 2 Tests prove ACs | One test per AC in a new test class. |
| Eng. 3 Small increments | One task, one commit. |
| Eng. 6 Verify current docs | No new library. Derived queries come from the Spring Data JPA in the Spring Boot version already in `pom.xml`. |
| Architecture checklist | Controller stays package-private; the service read stays `@Transactional(readOnly = true)`; every list is sorted by id; the contact id is a plain `Long`. |

## Design

- `DealRepository`: add `List<Deal> findByMainContactId(long mainContactId, Sort sort)` and `List<Deal> findByCompanyIdAndMainContactId(long companyId, long mainContactId, Sort sort)`.
- `DealService.list(Long companyId, Long mainContactId)`: no filters, `findAll(BY_ID)`; company only, `findByCompanyId` as now; contact only, the new query; both, the combined query. All sorted by id.
- `DealController.list(@RequestParam(required = false) Long companyId, @RequestParam(required = false) Long mainContactId)`.
- Unknown contact ids need no existence check: the query finds no rows and the list is empty (R3). This differs from create, where an unknown reference is a 400, because the spec asks for an empty list.
- No schema change. The existing `main_contact_id` column already has an index (checked in task T1 against `V3__deals.sql`).

## Requirement coverage

| Requirement | Covered by |
| --- | --- |
| R1 | Contact-only repository query and service branch |
| R2 | Combined repository query and service branch |
| R3 | No existence check; the query returns no rows |
| R4 | The no-filter and company-only branches keep the existing calls |
| R5 | `Long` parameter type; existing type-mismatch handler gives a 400 naming `mainContactId` |

## Decisions

| # | Decision | Alternatives | Why |
| --- | --- | --- | --- |
| 1 | Parameter is named `mainContactId` | `contactId` | It matches the deal's field name in requests and responses. The spec says only "contact filter". |
| 2 | Four service branches with derived queries | A JPA Specification, or one query with nullable parameters | Smallest code, mainstream, matches how `companyId` is done. DEC-05 (Specification for contact filters) covers the contact list; it is not changed here. |

## Test strategy

| Acceptance criterion | Verified by |
| --- | --- |
| AC-1 | `DealListApiTest.filterByContact`: integration, deals D1, D3 for contact A, D2 for B; `mainContactId=A` returns D1, D3 in order |
| AC-2 | `DealListApiTest.filterByCompanyAndContact`: integration, two companies, three deals; both filters return one deal |
| AC-3 | `DealListApiTest.unknownContactReturnsEmpty`: integration, a very large id that no contact has (999999) returns `[]` |
| AC-4 | `DealListApiTest.noContactFilterKeepsResults`: no filter returns all three; `companyId` only returns the company's deals. A regression guard that passes on the current code too; AC-1 to AC-3 and AC-5 fail first. |
| AC-5 | `DealListApiTest.badContactFilter`: `mainContactId=abc` is a 400 whose fields include `mainContactId` |

## Risks and unknowns

- None known. `ApiTestBase` truncates all tables before each test, so the tests can assert on exact lists.

## Deviations

| # | Deviation |
| --- | --- |

## Amendments

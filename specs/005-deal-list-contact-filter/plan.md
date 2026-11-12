# 005 · Plan: Deal list contact filter

## Approach

Add an optional `contactId` query parameter to `GET /deals`, next to `companyId`. The controller passes both to the service. The service picks one of four repository calls. A contact id that no contact has simply matches no deal, so the list is empty (R3). A value that is not a whole number is rejected by Spring's type conversion, which the existing `ApiExceptionHandler.handleTypeMismatch` turns into a 400 that names the parameter (R5), as `page=abc` already is.

## Constitution check

| Article | How the plan complies |
| --- | --- |
| P1 API only | The filter is a query parameter on the HTTP list. |
| P2 Clear errors | `contactId=abc` gives a 400 naming `contactId`; nothing is changed. |
| P3 No dangling references | Read-only change; no references are written. |
| P4 Easy to run | Tests run on real PostgreSQL through `ApiTestBase`. |
| P5 Small and conventional | Spring Data derived queries. No new dependency. |
| Eng. 1 Happy path first | No paging, no other filters (out of scope). |
| Eng. 2 Tests prove ACs | One test per AC. |
| Eng. 3 Small increments | One task, one commit. |
| Eng. 6 Verify current docs | No new library. Derived queries come from the Spring Boot version already in `pom.xml`. |
| Architecture checklist | Controller stays package-private, the read keeps `@Transactional(readOnly = true)`, the list is sorted by id. The controller does not check that the contact exists, because R3 asks for an empty list, not an error. |

## Design

- `DealRepository`: add `List<Deal> findByMainContactId(long mainContactId, Sort sort)` and `List<Deal> findByCompanyIdAndMainContactId(long companyId, long mainContactId, Sort sort)`.
- `DealService.list(Long companyId, Long contactId)`: both null uses `findAll(BY_ID)`; company only uses `findByCompanyId`; contact only uses `findByMainContactId`; both uses the combined query. All sorted by id.
- `DealController.list(@RequestParam(required = false) Long companyId, @RequestParam(required = false) Long contactId)`.
- No schema change, no migration.

## Requirement coverage

| Requirement | Covered by |
| --- | --- |
| R1 | `findByMainContactId`, service branch, controller parameter |
| R2 | `findByCompanyIdAndMainContactId` |
| R3 | A derived query on an unknown id returns no rows; no existence check |
| R4 | The null and company-only branches keep today's calls |
| R5 | `Long` request parameter and the existing type-mismatch handler |

## Decisions

| # | Decision | Alternatives | Why |
| --- | --- | --- | --- |
| 1 | Query parameter is named `contactId`, like `companyId` | `mainContactId`, as the deal field is named | The spec does not fix a name. It filters by "the contact", and it mirrors `companyId`. |
| 2 | Four derived repository methods behind one service branch | Specifications or a JPQL query with optional parameters | Smallest code, mainstream; only two filters exist. DEC-05 (Specification) covers contacts only. |
| 3 | No existence check on the contact | 400 for an unknown contact | R3 requires an empty list. |

## Test strategy

| Acceptance criterion | Verified by |
| --- | --- |
| AC-1 | `DealListApiTest.filterByContact`: integration, `contactId=A` returns D1 and D3 in order |
| AC-2 | `DealListApiTest.filterByCompanyAndContact`: integration |
| AC-3 | `DealListApiTest.unknownContactIsEmpty`: integration, `contactId=999999` |
| AC-4 | `DealListApiTest.noContactFilterUnchanged`: no filter returns three; `companyId` only returns the company's deals. A guard that passes on the current code; AC-1 to AC-3 and AC-5 are the tests that fail first. |
| AC-5 | `DealListApiTest.invalidContactFilter`: parameterized over `abc` and `1.5`: a 400 naming `contactId`, via `badInputFields` |

## Risks and unknowns

- `ApiTestBase` truncates all tables before each test, so tests can assert exact lists. If `badInputFields` does not name a query parameter for `contactId`, the handler is extended in the same task.

## Deviations

| # | Deviation |
| --- | --- |

## Amendments

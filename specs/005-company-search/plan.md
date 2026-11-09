# 005 · Plan: Company search

## Approach

Add an optional `name` query parameter to `GET /companies`. When it is given and not empty, the service returns only companies whose name contains it, ignoring case, in id order. Without it the list is unchanged: every company in id order, as a plain JSON array, not paged (AS-01). The matching copies the contact list's name filter: a lower-cased `LIKE` with `%` and `_` escaped, through a JPA `Specification`.

## Constitution check

| Article | How the plan complies |
| --- | --- |
| 1 API only | The filter is a query parameter on the existing HTTP endpoint, and shows in `/v3/api-docs`. |
| 2 Clear errors | A name text cannot be invalid: any text, including an empty one, is accepted. Nothing changes on a read. |
| 4 Easy to run | The tests are API tests against PostgreSQL in a container, run by `./mvnw verify`. |
| 5 Small and conventional | No new dependency; uses Spring Data JPA's `JpaSpecificationExecutor`, already used by the contact list. |
| 6 Happy path first | Only the name filter. Paging the company and deal lists is left for a BACKLOG item (see AS-01). |
| 7 Tests prove ACs | AC-1 to AC-6 each have an API test (see Test strategy). |
| 8 Green main | T1's check runs the whole build (`./mvnw -B verify`). |
| 9 Small increments | One task, one commit. |
| 11 Decisions written down | D1 to D4 below; D1 rests on AS-01 in `docs/decisions.md`. |
| 12 Verify current docs | No new library. `JpaSpecificationExecutor.findAll(Specification, Sort)` and `CriteriaBuilder.like(Expression, String, char)` are already used in this code base with the Spring Boot 4.1.1 / Spring Data JPA versions in `pom.xml`. |

Architecture checklist: the controller takes the parameter and calls its service; the query stays in the repository through a `Specification`. The list is not paged, so the paging rule does not apply.

## Design

- `CompanyController.list(@RequestParam(required = false) String name)` passes `name` to `CompanyService.list(name)`.
- `CompanyService.list(String name)`: `companies.findAll(nameContains(name), Sort.by("id"))`, mapped to `CompanyResponse`. `nameContains` returns no predicate when `name` is null or empty, so the result is every company, as today. Otherwise it is `lower(name) LIKE '%' || escape(lower(text)) || '%'` with `\` as the escape character.
- `CompanyRepository` also extends `JpaSpecificationExecutor<Company>`.
- No schema change. No index: the company list is small and not paged (see Risks).
- The response stays `List<CompanyResponse>`.

## Requirement coverage

| Requirement | Covered by |
| --- | --- |
| R1 | `name` parameter on `GET /companies`; `CompanyService.list(name)` with the case-insensitive contains `Specification`, escaping `\`, `%`, and `_`; no match gives an empty array with 200 |
| R2 | `CompanyService.list(name)` with no predicate when `name` is absent or empty; the response stays a plain array of every company |

## Decisions

| # | Decision | Alternatives | Why |
| --- | --- | --- | --- |
| D1 | The company list is not paged, with or without a name text | Page it like the contact list | AS-01 (sdd-product, inferred): keeps the public contract of spec 001 R8; paging companies and deals together is its own item |
| D2 | Filter with a JPA `Specification` and lower-cased `LIKE`, escaping `%` and `_` | A derived query `findByNameContainingIgnoreCase`; PostgreSQL `ILIKE` in a native query | Same code shape and same escaping as the contact name filter, so "Lists, filters, and errors work the same way" (intent, Consistent API); a derived query does not escape `%` and `_` |
| D3 | An empty name text (`?name=`) counts as no name text | Reject with 400 | Spec Scope: no name text is rejected, and the empty text narrows nothing; the contact filter does the same |
| D4 | Case folding: `toLowerCase(Locale.ROOT)` on the text and SQL `lower()` on the column | `ILIKE`; `upper()` on both | Same as the contact filter. PostgreSQL's `lower()` folds non-ASCII letters (É to é) under the database's UTF-8 ctype, so "café" finds "CAFÉ" there; this is not part of any AC |

## Test strategy

| Acceptance criterion | Verified by (unit, integration, e2e, manual) |
| --- | --- |
| AC-1 | Integration: `CompanyApiTest.searchByNameIgnoringCase`: Acme Corp, ACME Labs, Big Acme Ltd, Globex; `GET /companies?name=acme` returns exactly the first three |
| AC-2 | Integration: `CompanyApiTest.listWithoutNameReturnsAll`: three companies, `GET /companies` returns all three as a plain array; `CompanyApiTest.listAll` from spec 001 still passes |
| AC-3 | Integration: `CompanyApiTest.searchWithNoMatchIsEmpty`: `GET /companies?name=zzz` is 200 with an empty array |
| AC-4 | Integration: `CompanyApiTest.nameTextMatchesWildcardsLiterally`: `a_b` returns only A_B Corp; `50%` returns only 50% Off; and, for the escape character itself, `a\b` returns only "A\B Corp", not "AB Corp". The text is sent as a URI variable so `%` and `\` arrive encoded |
| AC-5 | Integration: `CompanyApiTest.listsAreNotPaged`: 25 companies "Acme 1" to "Acme 25" and "Globex"; `GET /companies?name=acme` returns exactly the 25 Acme companies; then, with Globex deleted, `GET /companies` returns all 25 |
| AC-6 | Integration: `CompanyApiTest.emptyNameTextNarrowsNothing`: `GET /companies?name=` returns Acme Corp and Globex |

Also checked (NFR-API-02, not an AC of this spec): `ApiDescriptionTest.companyListTakesName` checks that `GET /companies` lists the `name` parameter. AC-2 and AC-6 guard behavior that does not change, so their tests pass before T1; T1's check still fails first through AC-1, AC-3, AC-4, and AC-5.

## Risks and unknowns

- A contains match cannot use a normal B-tree index, so search is a full scan. With an unpaged company list this is acceptable now; a trigram index can come with paging later.
- Folding non-ASCII letters (D4) depends on the database's ctype. The test container and the default setup use UTF-8; a database created with the `C` locale would not fold É. No AC covers it.

## Deviations

## Amendments

# 005 · Tasks: Company search

## Tasks

- [x] **T1** Company name filter, test first: add `searchByNameIgnoringCase`, `listWithoutNameReturnsAll`, `searchWithNoMatchIsEmpty`, `nameTextMatchesWildcardsLiterally`, `listsAreNotPaged`, and `emptyNameTextNarrowsNothing` to `CompanyApiTest`, and `companyListTakesName` to `ApiDescriptionTest` (NFR-API-02); then the `name` parameter in `CompanyController`, `CompanyService.list(name)` with the `Specification`, and `JpaSpecificationExecutor` on `CompanyRepository`; then run the whole build · `src/main/.../company/`, `CompanyApiTest`, `ApiDescriptionTest`, `ApiTestBase` (a list helper with URI variables) · check: `./mvnw -B test -Dtest='CompanyApiTest#searchByNameIgnoringCase+listWithoutNameReturnsAll+searchWithNoMatchIsEmpty+nameTextMatchesWildcardsLiterally+listsAreNotPaged+emptyNameTextNarrowsNothing+listAll,ApiDescriptionTest#companyListTakesName' && ./mvnw -B verify` → AC-1, AC-2, AC-3, AC-4, AC-5, AC-6

## Evidence

| Acceptance criterion | Result | Evidence (test, command output, manual check) |
| --- | --- | --- |
| AC-1 | Pass | Integration: `CompanyApiTest.searchByNameIgnoringCase`; passed in `./mvnw -B verify` at `83d98cf` (91 tests, 0 failures) |
| AC-2 | Pass | Integration: `CompanyApiTest.listWithoutNameReturnsAll` and `CompanyApiTest.listAll` (spec 001 AC-8); passed in `./mvnw -B verify` at `83d98cf` |
| AC-3 | Pass | Integration: `CompanyApiTest.searchWithNoMatchIsEmpty` (200, empty array); passed in `./mvnw -B verify` at `83d98cf` |
| AC-4 | Pass | Integration: `CompanyApiTest.nameTextMatchesWildcardsLiterally` (`a_b`, `50%`, and `a\b`); passed in `./mvnw -B verify` at `83d98cf` |
| AC-5 | Pass | Integration: `CompanyApiTest.listsAreNotPaged` (exactly 25 of 26 with `name=acme`; 25 without a name text); passed in `./mvnw -B verify` at `83d98cf` |
| AC-6 | Pass | Integration: `CompanyApiTest.emptyNameTextNarrowsNothing`; passed in `./mvnw -B verify` at `83d98cf` |

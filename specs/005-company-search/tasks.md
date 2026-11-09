# 005 · Tasks: Company search

## Tasks

- [x] **T1** Company name filter, test first: add `searchByNameIgnoringCase`, `listWithoutNameReturnsAll`, `searchWithNoMatchIsEmpty`, `nameTextMatchesWildcardsLiterally`, `listsAreNotPaged`, and `emptyNameTextNarrowsNothing` to `CompanyApiTest`, and `companyListTakesName` to `ApiDescriptionTest` (NFR-API-02); then the `name` parameter in `CompanyController`, `CompanyService.list(name)` with the `Specification`, and `JpaSpecificationExecutor` on `CompanyRepository`; then run the whole build · `src/main/.../company/`, `CompanyApiTest`, `ApiDescriptionTest`, `ApiTestBase` (a list helper with URI variables) · check: `./mvnw -B test -Dtest='CompanyApiTest#searchByNameIgnoringCase+listWithoutNameReturnsAll+searchWithNoMatchIsEmpty+nameTextMatchesWildcardsLiterally+listsAreNotPaged+emptyNameTextNarrowsNothing+listAll,ApiDescriptionTest#companyListTakesName' && ./mvnw -B verify` → AC-1, AC-2, AC-3, AC-4, AC-5, AC-6

## Evidence

| Acceptance criterion | Result | Evidence (test, command output, manual check) |
| --- | --- | --- |
| AC-1 | | |
| AC-2 | | |
| AC-3 | | |
| AC-4 | | |
| AC-5 | | |
| AC-6 | | |

# 004 · Tasks: Company name filter

## Tasks

- [x] **T1** Add the `name` filter to the company list, with its tests first · `company/CompanyRepository.java`, `company/CompanyService.java`, `company/CompanyController.java`, `src/test/java/com/example/samplecrm/CompanyListApiTest.java` · check: `./mvnw test -Dtest=CompanyListApiTest,CompanyApiTest` → AC-1, AC-2, AC-3

## Evidence

| Acceptance criterion | Result | Evidence (test, command output, manual check) |
| --- | --- | --- |
| AC-1 | pass | CompanyListApiTest.filterIgnoresCase: name=acme returns exactly Acme Ltd and ACME Labs; ./mvnw verify, 87 tests, 0 failures |
| AC-2 | pass | CompanyListApiTest.filterWithNoMatch: name=zzz returns an empty list; ./mvnw verify, 87 tests, 0 failures |
| AC-3 | pass | CompanyListApiTest.noFilterReturnsAll: no filter and name= both return 3 (guard on existing behavior); ./mvnw verify, 87 tests, 0 failures |

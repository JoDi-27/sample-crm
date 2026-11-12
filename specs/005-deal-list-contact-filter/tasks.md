# 005 · Tasks: Deal list contact filter

## Tasks

- [x] **T1** Add the `contactId` filter to the deal list, with its tests first · `deal/DealRepository.java`, `deal/DealService.java`, `deal/DealController.java`, `src/test/java/com/example/samplecrm/DealListApiTest.java` · check: `./mvnw test -Dtest=DealListApiTest,DealApiTest` → AC-1, AC-2, AC-3, AC-4, AC-5

## Evidence

| Acceptance criterion | Result | Evidence (test, command output, manual check) |
| --- | --- | --- |
| AC-1 | pass | DealListApiTest.filterByContact: contactId=A returns D1, D3 in order; `./mvnw test -Dtest=DealListApiTest,DealApiTest`, 35 tests, 0 failures |
| AC-2 | pass | DealListApiTest.filterByCompanyAndContact: companyId=X&contactId=A returns D1 only; `./mvnw test -Dtest=DealListApiTest,DealApiTest`, 35 tests, 0 failures |
| AC-3 | pass | DealListApiTest.unknownContactIsEmpty: contactId=999999 returns []; `./mvnw test -Dtest=DealListApiTest,DealApiTest`, 35 tests, 0 failures |
| AC-4 | pass | DealListApiTest.noContactFilterUnchanged: no filter returns 3; companyId only returns its deals; `./mvnw test -Dtest=DealListApiTest,DealApiTest`, 35 tests, 0 failures |
| AC-5 | pass | DealListApiTest.invalidContactFilter (abc, 1.5): 400 naming contactId, deals unchanged; `./mvnw test -Dtest=DealListApiTest,DealApiTest`, 35 tests, 0 failures |

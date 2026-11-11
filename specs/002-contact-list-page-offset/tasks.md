# 002 · Tasks: Contact list page offset

## Tasks

- [x] **T1** Add the paging tests, see them fail, then fix the offset · `src/test/java/com/example/samplecrm/ContactListApiTest.java`, `src/main/java/com/example/samplecrm/contact/ContactService.java` · check: `./mvnw -q test -Dtest=ContactListApiTest` → AC-1, AC-2

## Evidence

| Acceptance criterion | Result | Evidence (test, command output, manual check) |
| --- | --- | --- |
| AC-1 | pass | `ContactListApiTest.pagesDoNotOverlap`: red on the old code, green after T1; `./mvnw verify` passes |
| AC-2 | pass | `ContactListApiTest.secondPageOfCompanyFilter`: red on the old code, green after T1; `./mvnw verify` passes |

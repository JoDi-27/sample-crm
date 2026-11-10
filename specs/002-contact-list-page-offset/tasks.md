# 002 · Tasks: Contact list page offset

## Tasks

- [x] **T1** Add the page 2 and 3 tests and fix the offset · `src/test/java/com/example/samplecrm/ContactListApiTest.java`, `src/main/java/com/example/samplecrm/contact/ContactService.java` · check: `./mvnw -q test -Dtest='ContactListApiTest#pagesDoNotOverlap+laterPageWithCompanyFilter'` → AC-1, AC-2

## Evidence

| Acceptance criterion | Result | Evidence (test, command output, manual check) |
| --- | --- | --- |
| AC-1 | pass | `ContactListApiTest.pagesDoNotOverlap`; `./mvnw -q test -Dtest='ContactListApiTest#pagesDoNotOverlap+laterPageWithCompanyFilter'` passed (`jsdd done 002 T1`), and `./mvnw verify` passed |
| AC-2 | pass | `ContactListApiTest.laterPageWithCompanyFilter`, same command |

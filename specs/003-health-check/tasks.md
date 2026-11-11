# 003 · Tasks: Health check

## Tasks

- [ ] **T1** Add `GET /health`: write `HealthApiTest` first (asserts status 200 and `status` == `UP`), then the controller · `src/main/java/com/example/samplecrm/health/HealthController.java`, `src/test/java/com/example/samplecrm/HealthApiTest.java` · check: `./mvnw -q test -Dtest=HealthApiTest` → AC-1
- [ ] **T2** Add the README "Health check" section with a `curl` example and the `{"status":"UP"}` answer · `README.md` · check: `grep -q '^## Health check' README.md && grep -q '/health' README.md && grep -q '"status"' README.md` → AC-2

## Evidence

| Acceptance criterion | Result | Evidence (test, command output, manual check) |
| --- | --- | --- |
| AC-1 | | |
| AC-2 | | T2 proves only that the section exists. Whether it is clear needs the owner (parked at task.manual_check). |

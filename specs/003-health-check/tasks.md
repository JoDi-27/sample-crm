# 003 · Tasks: Health check

## Tasks

- [ ] **T1** Add `HealthApiTest` and `HealthController` for `GET /health` · `src/test/java/com/example/samplecrm/HealthApiTest.java`, `src/main/java/com/example/samplecrm/health/HealthController.java` · check: `./mvnw test -Dtest=HealthApiTest` → AC-1
- [ ] **T2** Add the README "Health check" section and API table row · `README.md` · check: `grep -q 'GET /health' README.md && grep -q '"status"' README.md` → AC-2 (precondition: the section exists)
- [ ] **T3** The owner reads the README health check section · `README.md` · check: manual: the owner reads the README health check section and confirms it is clear → AC-2

## Evidence

| Acceptance criterion | Result | Evidence (test, command output, manual check) |
| --- | --- | --- |
| AC-1 | | |
| AC-2 | | |

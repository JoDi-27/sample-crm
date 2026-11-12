# 005 · Verify report: Deal list contact filter

**Round:** 2 · **Date:** 2026-11-12 · **Git range:** main..HEAD
**Outcome:** converged
**Decision:** none needed
**Planned as:** A1 in plan.md

## Acceptance criteria

| AC | Result | Evidence | Gap |
| --- | --- | --- | --- |
| AC-1 | pass | DealListApiTest.filterByContact | |
| AC-2 | pass | DealListApiTest.filterByCompanyAndContact | |
| AC-3 | pass | DealListApiTest.unknownContactIsEmpty | |
| AC-4 | pass | DealListApiTest.noContactFilterUnchanged (a guard, stated in the plan) | |
| AC-5 | pass | DealListApiTest.invalidContactFilter (abc, 1.5) | |

## Requirements

| R | Verdict | Where | Gap |
| --- | --- | --- | --- |
| R1 | delivered | DealService.java list, DealRepository.findByMainContactId | |
| R2 | delivered | DealRepository.findByCompanyIdAndMainContactId | |
| R3 | delivered | no existence check | |
| R4 | delivered | service keeps the existing calls | |
| R5 | delivered | `Long` parameter, existing type-mismatch handler | |

## Unrequested behavior

None.

## Plan drift

The plan missed `ApiDescriptionTest`, which pins the deal list's parameters (G1).

## Proposed actions

- **G1** The full suite (`jsdd suite 005`, run alone) fails one test: `ApiDescriptionTest.listsEveryOperation` expects `GET /deals` to have only `companyId`, and it now also has `mainContactId`. Fix the test, since the spec changed the behavior it pins: expect `companyId` and `mainContactId` · `src/test/java/com/example/samplecrm/ApiDescriptionTest.java` · check: `./mvnw test -Dtest=ApiDescriptionTest` → AC-1 (R1). Options: (a) update the test to the two parameters (recommended, the reviewer-style fix for a pinned test). Chosen: a (auto: verify.gap_option, the recommended option; the first full-suite run also failed because the reviewer ran tests on the shared database at the same time, which is not a gap).

## History

| Round | Date | Outcome | Decision |
| --- | --- | --- | --- |
| 1 | 2026-11-12 | gaps found | approved (auto: verify.gaps) |
| 2 | 2026-11-12 | converged | none needed (T2 fixed G1; full suite 95 tests, 0 failures) |

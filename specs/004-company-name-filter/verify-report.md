# 004 · Verify report: Company name filter


**Round:** 1 · **Date:** 2026-11-10 · **Git range:** main..HEAD
**Outcome:** converged
**Decision:** none needed

## Acceptance criteria

| AC | Result | Evidence | Gap |
| --- | --- | --- | --- |
| AC-1 | pass | CompanyListApiTest.filterIgnoresCase | |
| AC-2 | pass | CompanyListApiTest.filterWithNoMatch | |
| AC-3 | pass | CompanyListApiTest.noFilterReturnsAll (guard: passes on old code too, as the plan records) | |

## Requirements

| R | Verdict | Where | Gap |
| --- | --- | --- | --- |
| R1 | delivered | CompanyService.java:28-33, CompanyRepository.java:8, CompanyController.java:47 | |
| R2 | delivered | CompanyService.java:29-31 | |

## Unrequested behavior

None. A whitespace-only name is treated as a filter; the spec does not cover it.

## Plan drift

None. Deviation 1 (check syntax) is recorded.

## Proposed actions

None.


## History

| Round | Date | Outcome | Decision |
| --- | --- | --- | --- |
| 1 | 2026-11-10 | converged | none needed |

# 005 · Verify report: Deal list contact filter

**Round:** 1 · **Date:** 2026-11-12 · **Git range:** main..HEAD
**Outcome:** converged
**Decision:** none needed
**Planned as:**

## Acceptance criteria

| AC | Result | Evidence | Gap |
| --- | --- | --- | --- |
| AC-1 | pass | DealListApiTest.filterByContact | none |
| AC-2 | pass | DealListApiTest.filterByCompanyAndContact | none |
| AC-3 | pass | DealListApiTest.unknownContactIsEmpty | none |
| AC-4 | pass | DealListApiTest.noContactFilterUnchanged (a guard; passes on old code too, as planned) | none |
| AC-5 | pass | DealListApiTest.invalidContactFilter (abc, 1.5) | none |

## Requirements

| R | Verdict | Where | Gap |
| --- | --- | --- | --- |
| R1 | delivered | deal/DealService.java list, DealRepository.findByMainContactId | none |
| R2 | delivered | DealService.list, DealRepository.findByCompanyIdAndMainContactId | none |
| R3 | delivered | derived query, no existence check | none |
| R4 | delivered | null and company-only branches unchanged | none |
| R5 | delivered | DealController.list takes Long contactId; existing type-mismatch handler | none |

## Unrequested behavior

None.

## Plan drift

None.

## Proposed actions

None. Reviewer notes on blank Evidence and the R5 line were checked: Evidence is filled and `docs/capabilities/deal.md` states R5.

## History

| Round | Date | Outcome | Decision |
| --- | --- | --- | --- |
| 1 | 2026-11-12 | converged | none needed |

# 006 · Verify report: Deal stages

**Round:** 1 · **Date:** 2026-11-10 · **Git range:** autonomous-test..3834c63 (the loop's base branch; `main` lacks the Phase 2 specs)
**Outcome:** converged
**Decision:** none needed
**Planned as:**

## Acceptance criteria

| AC | Result | Evidence | Gap |
| --- | --- | --- | --- |
| AC-1 | pass | `DealStageApiTest.newDealStartsOpen` | |
| AC-2 | pass | `DealStageApiTest.updateToWon` | |
| AC-3 | pass | `DealStageApiTest.unknownStageOnUpdateIsBadInput`: 400 naming `stage`; the title sent with it was not stored | |
| AC-4 | pass | `DealStageApiTest.wonDealCanBeReopened` | |
| AC-5 | pass | `DealStageListApiTest.listByStageAndCompany` | |
| AC-6 | pass | `DealStageApiTest.createWithStage` | |
| AC-7 | pass | `DealStageApiTest.updateWithoutStageKeepsIt` | |
| AC-8 | pass | `DealStageListApiTest.unknownStageOnListIsBadInput` | |
| AC-9 | pass | `DealStageMigrationTest.existingDealsBecomeOpen` (reads the migrated row with SQL; the API read of a stored stage is proven by AC-1) | |
| AC-10 | pass | `DealStageApiTest.unknownStageOnCreateIsBadInput` | |
| AC-11 | pass | `DealStageApiTest.updateWithNullStageKeepsIt` (explicit JSON null) | |
| AC-12 | pass | `DealStageListApiTest.noMatchIsEmpty` | |
| AC-13 | pass | `DealStageApiTest.everyResponseShowsStage` | |

All in `./mvnw -B verify` at `3834c63`: 97 tests, 0 failures.

## Requirements

| R | Verdict | Where | Gap |
| --- | --- | --- | --- |
| R1 | delivered | `V4__deal_stage.sql:2-3`; `Deal.java:32`; `DealResponse.java:10` | |
| R2 | delivered | `DealService.java:51-53`; `Deal.java:32,37-38,47-49` | |
| R3 | delivered | `Deal.java:42-49` | |
| R4 | delivered | `DealController.java:58,102,109-112` | |
| R5 | delivered | `Deal.java:47-49` (no transition rule) | |
| R6 | delivered | `DealController.java:66-72`; `DealService.java:32-40`; `DealRepository.java:11,13` | |

## Unrequested behavior

None.

## Plan drift

None.

## Proposed actions

None.

## History

| Round | Date | Outcome | Decision |
| --- | --- | --- | --- |
| 1 | 2026-11-10 | converged | none needed |

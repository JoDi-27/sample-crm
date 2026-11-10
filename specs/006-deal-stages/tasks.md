# 006 · Tasks: Deal stages

## Tasks

- [x] **T1** Stage on every deal, test first: `DealStageApiTest` (`newDealStartsOpen`, `createWithStage`, `updateToWon`, `wonDealCanBeReopened`, `updateWithoutStageKeepsIt`, `updateWithNullStageKeepsIt`, `unknownStageOnCreateIsBadInput`, `unknownStageOnUpdateIsBadInput`, `everyResponseShowsStage`) and `DealStageMigrationTest.existingDealsBecomeOpen`; then `V4__deal_stage.sql`, `DealStage`, `DealStageConverter`, and the stage in `Deal`, `DealRequest`, `DealResponse`, `DealController` (create and update validation), and `DealService` · `db/migration/V4__deal_stage.sql`, `src/main/.../deal/`, `DealStageApiTest`, `DealStageMigrationTest` · check: `./mvnw -B test -Dtest=DealStageApiTest && ./mvnw -B test -Dtest=DealStageMigrationTest && ./mvnw -B verify` → AC-1, AC-2, AC-3, AC-4, AC-6, AC-7, AC-9, AC-10, AC-11, AC-13
- [x] **T2** List deals by stage, test first: `DealStageListApiTest` (`listByStageAndCompany`, `noMatchIsEmpty`, `unknownStageOnListIsBadInput`), and `stage` added to the deal list parameters in `ApiDescriptionTest.listsEveryOperation`; then the `stage` parameter in `DealController.list`, `DealService.list(companyId, stage)`, and `findByStage` and `findByCompanyIdAndStage` in `DealRepository`; then run the whole build · `src/main/.../deal/`, `DealStageListApiTest`, `ApiDescriptionTest` · check: `./mvnw -B test -Dtest=DealStageListApiTest && ./mvnw -B verify` → AC-5, AC-8, AC-12

## Evidence

| Acceptance criterion | Result | Evidence (test, command output, manual check) |
| --- | --- | --- |
| AC-1 | | |
| AC-2 | | |
| AC-3 | | |
| AC-4 | | |
| AC-5 | | |
| AC-6 | | |
| AC-7 | | |
| AC-8 | | |
| AC-9 | | |
| AC-10 | | |
| AC-11 | | |
| AC-12 | | |
| AC-13 | | |

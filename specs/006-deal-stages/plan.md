# 006 · Plan: Deal stages

## Approach

Add a `stage` column to `deals` with a Flyway migration: `open`, `won`, or `lost`, not null, default `open`, so every existing deal becomes open (AS-07). The deal request and response gain a `stage` field. Create stores the stage sent, or `open` when none is sent (AS-02, AS-04). Update stores the stage sent, or keeps the current one when none is sent (AS-05); any valid stage may follow any other (AS-03). The controller rejects any other text as bad input naming `stage`, like the other deal fields. `GET /deals` gains an optional `stage` parameter, combined with `companyId`, and rejects an unknown stage the same way (AS-06).

## Constitution check

| Article | How the plan complies |
| --- | --- |
| 1 API only | The stage is set, read, and filtered through the deal endpoints, and shows in `/v3/api-docs`. Reopening a deal (AS-03) needs no database access. |
| 2 Clear errors | An unknown stage, on a deal body or on the list, is a 400 problem naming `stage`, checked in the controller with the other fields before anything is stored. |
| 3 No dangling references | Not touched: the stage is a value, not a reference. |
| 4 Easy to run | API tests and a migration test against PostgreSQL in a container, in `./mvnw verify`. |
| 5 Small and conventional | No new dependency. A Java enum with a JPA `AttributeConverter`, a Flyway migration, Spring Data derived queries. |
| 6 Happy path first | Only stages and the stage filter. No reporting (intent, not now). |
| 7 Tests prove ACs | Every AC maps to a test (see Test strategy). |
| 8 Green main | Each task's check ends with the whole build (`./mvnw -B verify`). |
| 9 Small increments | Two tasks, one commit each. T1 is the larger: the migration, the enum, and the deal classes cannot be committed green separately. |
| 10 Specs are living | R3 records that the stage is the one field an update keeps when left out, an exception to spec 001 R5. `ApiDescriptionTest.listsEveryOperation` (spec 001 AC-21) is updated in T2, because the deal list now takes `stage` as well as `companyId`. |
| 11 Decisions written down | D1 to D6 below; the product choices are AS-02 to AS-07 in `docs/decisions.md`. |
| 12 Verify current docs | No new library. Flyway's Java API (`Flyway.configure().dataSource(...).schemas(...).target(...)`) and JPA `@Converter` are used as in the versions Spring Boot 4.1.1 manages (resolved: Flyway 12.4.0, Hibernate ORM 7.4.5.Final). |

Intent "Consistent API": stage validation and the list parameter work like the existing fields and `companyId`. The one difference, an update keeping a stage it was not sent (AS-05), keeps existing update clients from silently reopening deals.

Architecture checklist: the controller validates and calls `DealService`; queries stay in `DealRepository`; errors go through `BadInputException` and `ApiExceptionHandler`. The deal list is not paged, so the paging rule does not apply.

## Design

- **Migration** `V4__deal_stage.sql`: `ALTER TABLE deals ADD COLUMN stage varchar(10) NOT NULL DEFAULT 'open' CHECK (stage IN ('open', 'won', 'lost'))`, with no index (D6). PostgreSQL fills existing rows with the default.
- **`DealStage`** enum (`OPEN`, `WON`, `LOST`) with `value()` giving the lower-case text and `static Optional<DealStage> parse(String)` matching it exactly. `DealStageConverter` (`@Converter(autoApply = true)`) stores the lower-case text.
- **`DealRequest`** gains `String stage` (text, so the controller can name a wrong value instead of failing JSON parsing).
- **`DealController`**: on create and update, a non-null stage that `DealStage.parse` rejects adds `FieldErrorItem("stage", "must be one of open, won, lost")`. `list(companyId, stage)` rejects an unknown stage with `BadInputException` naming `stage`, then calls `service.list(companyId, parsedStage)`.
- **`Deal`**: `stage` field (default `OPEN`). The constructor sets the stage sent or `OPEN`; `update` sets the stage only when one is sent. Both take the parsed stage from the service.
- **`DealService`**: `create` and `update` parse the request's stage (already validated); `list(companyId, stage)` picks `findAll`, `findByCompanyId`, `findByStage`, or `findByCompanyIdAndStage`, all sorted by id.
- **`DealResponse`** gains `String stage` (the lower-case value).

## Requirement coverage

| Requirement | Covered by |
| --- | --- |
| R1 | `stage` column not null with default `open` (V4); `DealResponse.stage` on every response |
| R2 | `Deal` constructor: the stage sent, or `OPEN`; null counts as none |
| R3 | `Deal.update`: sets the stage only when one is sent |
| R4 | `DealController` create and update validation, before the service is called |
| R5 | No transition rule: `Deal.update` stores any valid stage |
| R6 | `DealController.list` validation; `DealService.list(companyId, stage)` and the derived queries |

## Decisions

| # | Decision | Alternatives | Why |
| --- | --- | --- | --- |
| D1 | Store the stage as lower-case text with a CHECK constraint | A PostgreSQL enum type; a lookup table | Text with a CHECK is simple to migrate and to extend with Flyway; the API value and the stored value are the same |
| D2 | Take the stage as text in `DealRequest` and validate it in the controller | Bind it to the enum in JSON | A failed enum binding is an unreadable body with no field name; Article 2 and NFR-API-01 need the field named, as the other deal fields are |
| D3 | Existing deals get `open` from the column default in the same migration | A separate data migration | AS-07; one statement, run once by Flyway |
| D4 | The list filter uses derived queries | A JPA `Specification` | Two optional filters only; matches the existing `findByCompanyId` |
| D5 | Test AC-9 with Flyway's Java API on a schema used only by this test (`stage_migration_test`): `DROP SCHEMA IF EXISTS … CASCADE`, migrate to V3, insert a deal, migrate to the latest version, read its stage, and drop the schema again | Only trust the column default | Proves the migration on real data without touching the application's schema; dropping first means a long-lived `DB_URL` database cannot leave the schema at V4 and make the test pass without migrating (Flyway's `clean()` is disabled) |
| D6 | No index on `stage` | An index | Three values only; the deal list is small and not paged. Can be added with paging later |

## Test strategy

| Acceptance criterion | Verified by (unit, integration, e2e, manual) |
| --- | --- |
| AC-1 | Integration: `DealStageApiTest.newDealStartsOpen` |
| AC-2 | Integration: `DealStageApiTest.updateToWon` |
| AC-3 | Integration: `DealStageApiTest.unknownStageOnUpdateIsBadInput` (400 naming `stage`; the deal is unchanged) |
| AC-4 | Integration: `DealStageApiTest.wonDealCanBeReopened` |
| AC-5 | Integration: `DealStageListApiTest.listByStageAndCompany` |
| AC-6 | Integration: `DealStageApiTest.createWithStage` |
| AC-7 | Integration: `DealStageApiTest.updateWithoutStageKeepsIt` |
| AC-8 | Integration: `DealStageListApiTest.unknownStageOnListIsBadInput` |
| AC-9 | Integration: `DealStageMigrationTest.existingDealsBecomeOpen` (D5) |
| AC-10 | Integration: `DealStageApiTest.unknownStageOnCreateIsBadInput` (400 naming `stage`; no deal stored) |
| AC-11 | Integration: `DealStageApiTest.updateWithNullStageKeepsIt` |
| AC-12 | Integration: `DealStageListApiTest.noMatchIsEmpty` |
| AC-13 | Integration: `DealStageApiTest.everyResponseShowsStage` (create, update, and list responses) |

Also: `ApiDescriptionTest.listsEveryOperation` checks the deal list parameters `companyId` and `stage` (NFR-API-02), and the existing `DealApiTest` keeps passing.

## Risks and unknowns

- The migration test needs permission to create a schema. The Testcontainers user is a superuser; in CI the `postgres` service user owns the database. If not, the test fails loudly rather than passing.
- Adding a NOT NULL column with a constant default is a metadata-only change in PostgreSQL 11+, so it does not rewrite the table.

## Deviations

## Amendments

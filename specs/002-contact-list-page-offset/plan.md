# 002 · Plan: Contact list page offset

## Approach

`ContactService.list` takes one row off the offset of every page after the first. Use the offset `(page - 1) * size`. Add two tests that read past page 1. No other code changes.

## Constitution check

| Article | How the plan complies |
| --- | --- |
| Engineering 2 (tests prove ACs) | Both ACs get an integration test against PostgreSQL. |
| Engineering 3 (small increments) | One task, one commit. |
| Engineering 4 (specs are living) | The spec is approved; the plan adds no requirement. |

## Design

In `ContactService.list`, replace the two offset lines with `long offset = (long) (page - 1) * size;`. `OffsetPage`, the controller, and the response shape stay as they are. The filters apply in the same query, so the fix covers filtered lists too.

## Requirement coverage

| Requirement | Covered by |
| --- | --- |
| R1 | The offset line in `ContactService.list` (T1) |

## Decisions

| # | Decision | Alternatives | Why |
| --- | --- | --- | --- |
| D1 | Fix the offset in the service. | Change `OffsetPage`. | `OffsetPage` is correct; the bug is the value passed to it. |

## Test strategy

| Acceptance criterion | Verified by (unit, integration, e2e, manual) |
| --- | --- |
| AC-1 | Integration: `ContactListApiTest.pagesDoNotOverlap` |
| AC-2 | Integration: `ContactListApiTest.laterPageWithCompanyFilter`. Contacts of two companies are interleaved by id; the one result must be the target company's third contact by name. |

## Risks and unknowns

Low. The tests need Docker or `DB_URL`.

## Deviations

## Amendments

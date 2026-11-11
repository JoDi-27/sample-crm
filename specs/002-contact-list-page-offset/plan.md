# 002 · Plan: Contact list page offset

## Approach

`ContactService.list` computes the row offset as `max(0, (page - 1) * size - 1)`, which starts one row early after page 1. Use `(page - 1) * size` instead. Add tests that read past page 1, with and without the company filter.

## Constitution check

| Article | How the plan complies |
| --- | --- |
| Engineering: tests with the change | The fix comes with the tests for AC-1 and AC-2, in the existing `ContactListApiTest`. |
| Architecture checklist: service roles | The change stays inside `ContactService.list`, which keeps its read-only transaction. |

## Design

In `src/main/java/com/example/samplecrm/contact/ContactService.java`, `list` sets `offset = (long) (page - 1) * size`. `OffsetPage`, `PageResponse`, the controller, and the API shape are unchanged. Page 1 gives offset 0 as before. A page past the end still returns no items and the right total.

## Requirement coverage

| Requirement | Covered by |
| --- | --- |
| R1 | The offset line in `ContactService.list` |

## Decisions

| # | Decision | Alternatives | Why |
| --- | --- | --- | --- |
| 1 | Fix the arithmetic in the service | Change `OffsetPage` | `OffsetPage` takes a row offset and is correct. The bug is the value passed in. |

## Test strategy

| Acceptance criterion | Verified by (unit, integration, e2e, manual) |
| --- | --- |
| AC-1 | Integration: `ContactListApiTest.pagesDoNotOverlap` (five contacts, size 2, pages 1 to 3) |
| AC-2 | Integration: `ContactListApiTest.secondPageOfCompanyFilter` (five contacts, three at one company, page 2, size 2) |

## Risks and unknowns

Low. The tests need Docker or `DB_URL`.

## Deviations

## Amendments

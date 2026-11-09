# 005 · Verify report: Company search

**Round:** 1 · **Date:** 2026-11-09 · **Git range:** autonomous-test..83d98cf (the loop's base branch; `main` lacks the Phase 2 specs)
**Outcome:** gaps found
**Decision:** pending
**Planned as:**

## Acceptance criteria

| AC | Result | Evidence | Gap |
| --- | --- | --- | --- |
| AC-1 | pass | `CompanyApiTest.searchByNameIgnoringCase`; `./mvnw -B verify` at `83d98cf`, 91 tests, 0 failures | |
| AC-2 | pass | `CompanyApiTest.listWithoutNameReturnsAll`, `CompanyApiTest.listAll`; reads the body as a plain JSON array | |
| AC-3 | pass | `CompanyApiTest.searchWithNoMatchIsEmpty`: 200 and an empty array | |
| AC-4 | pass | `CompanyApiTest.nameTextMatchesWildcardsLiterally`: `a_b`, `50%`, `a\b`, sent as encoded URI variables | |
| AC-5 | pass | `CompanyApiTest.listsAreNotPaged`: exactly 25 of 26 with `name=acme`; 25 without a name text (above the default page size of 20) | |
| AC-6 | pass | `CompanyApiTest.emptyNameTextNarrowsNothing` | |

## Requirements

| R | Verdict | Where | Gap |
| --- | --- | --- | --- |
| R1 | delivered | `CompanyController.java:47` (optional `name`); `CompanyService.java:47-54` (lower-cased `LIKE` with `\` escape); `CompanyService.java:58-60` (escapes `\`, `%`, `_`) | |
| R2 | delivered | `CompanyService.java:50-52` (`conjunction()` when `name` is null or empty); `CompanyService.java:31-32` (a `List` in id order, not paged) | |

## Unrequested behavior

None. `ApiTestBase.getList(String, Object...)` is a test helper named in T1's files.

## Plan drift

- plan.md Constitution check, article 6: "Paging the company and deal lists is left for a BACKLOG item (see AS-01)." `BACKLOG.md` has no such item.
- plan.md Design says `nameContains` "returns no predicate" for an empty text; the code returns `cb.conjunction()`. Same behavior; the reviewer judged it not worth a Deviation.

## Proposed actions

- **G1** Move to BACKLOG.md: add a "Next" item "Page the company and deal lists (DEC-04)", citing AS-01 and spec 005 Scope, as plan.md article 6 says · `BACKLOG.md` · check: `grep -n -i "page the company" BACKLOG.md` shows the item. Parked (auto: verify.gaps): a gap whose only action is moving it to BACKLOG.md waits for a person.

## History

| Round | Date | Outcome | Decision |
| --- | --- | --- | --- |
| 1 | 2026-11-09 | gaps found | pending (parked at verify.gaps) |

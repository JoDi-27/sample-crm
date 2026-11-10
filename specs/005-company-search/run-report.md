# 005 · Run report: Company search

<!--
Written by /jsdd:auto (autopilot or autonomous), one per run (a later run on the same spec adds a "## Run 2" section with the same parts).
Everything here was done without a person. Read "Product decisions", "Decisions made without a person", and "Parked" first.
-->

**Mode:** autonomous, preset standard, loop specs/runs/2026-11-09-1.md · **Started by:** JoDi27, by running /jsdd:auto · **Branch:** autonomous/005-company-search (from `autonomous-test`)
**Started:** 2026-11-09 22:58 · **Ended:** 2026-11-09 23:09
**Outcome:** draft pull request: parked at verify.gaps

## Timeline

Times are commit times (corrected after the run; the first version had estimates).

- 22:59 Clarify: the one open marker (R2, paging) sent to sdd-product; answer inferred, within the charter; written as AS-01. Commit `[005] Clarify (auto)`.
- 23:00 Spec review: sdd-reviewer spec mode round 1, 9 findings, none blocking, all clarifications (AC-3 to AC-6 added, R1 sharpened, Scope says what is out); round 2, no blocking finding, one clarification (literal `%` in AC-4). Spec approved.
- 23:05 Plan: one task. sdd-reviewer plan mode round 1, 2 blocking (a second task whose check proved no AC; an AC-5 test that passed on today's code), both fixed; round 2, no blocking finding. Checks frozen. Commit `[005] Plan`.
- 23:07 T1: tests first (5 failed, as expected), then the filter. `jsdd done 005 T1` passed on attempt 1: `./mvnw -B verify`, 91 tests, 0 failures. Commit `[005] T1`.
- 23:09 Verify round 1: every AC passes, R1 and R2 delivered, no unrequested behavior. One gap (G1): the plan says paging is left to a BACKLOG item, and none was written. Parked at verify.gaps.

## Decisions made without a person

- R2 clarified: the company list stays unpaged (auto: spec.clarify; auto: product, sdd-product, inferred, AS-01)
- Spec approved: no blocking findings in sdd-reviewer spec mode round 2 (auto: spec.approve)
- Plan approved: no blocking findings in sdd-reviewer plan mode round 2 (auto: plan.approve)
- One task instead of two: T2's check proved no AC, so it was merged into T1 before the checks were frozen (plan review round 1)
- Base branch `autonomous-test`, not `main`: `main` holds neither the charter nor the Phase 2 specs

## Product decisions

| AS | Question | Answer | Class | Basis | Reversal |
| --- | --- | --- | --- | --- | --- |
| AS-01 | Is the company list paged now, like the contact list? | No: every company, filtered or not, as a plain array | inferred | spec 001 R8/AC-8; spec 005 Scope; BACKLOG.md § Next "Company search"; the constitution's paging rule is conditional. Weighed against: intent "Consistent API" | cheap (paging later changes the response shape, whatever this spec decides) |

## Parked

At `verify.gaps`. Decide G1 in `verify-report.md`: add a BACKLOG.md "Next" item "Page the company and deal lists (DEC-04)", or drop the gap. Everything else has converged: all six ACs pass and both Rs are delivered. Then `jsdd unpark 005` and `/jsdd:auto 005`, which closes the spec on this branch.

## For the reviewer

- **AS-01** in `docs/decisions.md`: the company list stays unpaged. The intent's "Consistent API" principle points the other way; confirm or reject the row.
- **G1** in `verify-report.md`: the only thing keeping this spec from `done`.
- `ApiTestBase.getList(String, Object...)`: a new test helper that sends query values as encoded URI variables.

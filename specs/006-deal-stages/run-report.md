# 006 · Run report: Deal stages

<!--
Written by /jsdd:auto (autopilot or autonomous), one per run (a later run on the same spec adds a "## Run 2" section with the same parts).
Everything here was done without a person. Read "Product decisions", "Decisions made without a person", and "Parked" first.
-->

**Mode:** autonomous, preset standard, loop specs/runs/2026-11-09-1.md · **Started by:** JoDi27, by running /jsdd:auto · **Branch:** autonomous/006-deal-stages (from `autonomous-test`)
**Started:** 2026-11-09 23:38 · **Ended:** <date time>
**Outcome:** <outcome>

## Timeline

- 23:40 Clarify: the two open markers, plus four questions the spec needed (create with a stage, update without one, unknown list stage, stage of existing deals), sent to sdd-product; all six answers inferred, within the charter; AS-02 to AS-07. Commit `[006] Clarify (auto)`.
- 23:50 Spec review: sdd-reviewer spec mode round 1, no blocking finding; 4 should-fix and 5 notes, all clarifications (null stage, create validation, empty list, access control out, AC-10 to AC-13). Spec approved.
- 00:00 Plan: two tasks. sdd-reviewer plan mode round 1 (52 min), 1 blocking (T1's check passed without the migration test), fixed with one class per command; also T1 runs the whole build, AC-13 moved to T1, the migration test drops its own schema, no index on stage. Round 2, no blocking finding. Checks frozen. Commit `[006] Plan`.
- 01:05 T1: tests first (10 failed, as expected), then the migration, enum, converter, and deal changes. `jsdd done 006 T1` passed on attempt 1 (94 tests). Commit `[006] T1`.
- 01:15 T2: tests first (4 failed), then the list filter. `jsdd done 006 T2` passed on attempt 1 (97 tests). Commit `[006] T2`.

## Decisions made without a person

- Open markers and four follow-up questions settled (auto: spec.clarify; auto: product, sdd-product, inferred, AS-02 to AS-07)
- Spec approved: no blocking findings in sdd-reviewer spec mode round 1 (auto: spec.approve)
- Plan approved: no blocking findings in sdd-reviewer plan mode round 2 (auto: plan.approve)
- `ApiDescriptionTest.listsEveryOperation` (spec 001 AC-21) now expects `companyId` and `stage` on the deal list (plan, Article 10)
- Base branch `autonomous-test`, not `main`; AS rows numbered from AS-02 because AS-01 is on 005's branch

## Product decisions

| AS | Question | Answer | Class | Basis | Reversal |
| --- | --- | --- | --- | --- | --- |
| AS-03 | Can a won or lost deal be reopened? | Yes; no stage is final | inferred | spec 001 R5 (no field is write-once); spec 006 R3; constitution Art. 1. The weakest: nothing states a sales rule, and "reversible" alone would favor final | moderate |
| AS-05 | An update that leaves the stage out | keeps the current stage, unlike every other field (spec 001 R5) | inferred | intent § What success looks like; rule: reversible | moderate |
| AS-07 | Stage of deals that exist before stages | open (migration default) | inferred | as AS-02 | expensive |
| AS-02 | Stage of a new deal | open | inferred | BACKLOG.md § Next "Deal stages"; intent; spec 006 S2 | moderate |
| AS-04 | Can a deal be created won or lost? | yes | inferred | spec 006 R2; one request shape for create and update | moderate |
| AS-06 | Unknown stage on the deal list | 400 naming `stage` | inferred | intent § Clear errors, Consistent API; NFR-API-01; spec 001 R12 | cheap |

## Parked

## For the reviewer

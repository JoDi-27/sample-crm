# 004 · Run report: Company name filter

<!--
Written by /jsdd:auto (autopilot or autonomous), one per run (a later run on the same spec adds a "## Run 2" section with the same parts).
Everything here was done without a person. Read "Product decisions", "Decisions made without a person", and "Parked" first.
-->

**Mode:** autopilot · **Started by:** sdd-runtime-bot, by running /jsdd:auto · **Branch:** autopilot/004-company-name-filter
**Started:** 2026-11-10 21:10 · **Ended:** 2026-11-10 21:15
**Outcome:** pull request ready

## Timeline

- Plan: sdd-reviewer round 1 had one blocking finding (AC-3 passes on old code); recorded as a regression guard. Round 2: none blocking.
- T1: tests and code in one commit. The first check run failed on bad Maven syntax (`+`); fixed to `,`, reviewed, and re-frozen. Passed on attempt 2.
- Verify round 1: converged. `./mvnw verify`: 87 tests, 0 failures.

## Decisions made without a person

- Spec approved by sdd-runtime-bot, by starting the run (spec.approve).
- Plan approved (auto: plan.approve, sdd-reviewer round 2).
- T1 check changed from `-Dtest=CompanyListApiTest+CompanyApiTest` to `...,...`: Deviation 1, no blocking finding from sdd-reviewer (auto: implement.stuck).
- Verify converged, spec closed (auto: verify, sdd-reviewer).

## Product decisions

None.

## Parked

Nothing.

## For the reviewer

- Deviation 1 in plan.md: the frozen check was corrected (syntax only, same classes).
- AC-3 is a guard that passes on the old code; AC-1 and AC-2 are the tests that fail first.
- A whitespace-only `name` filters by that text; the spec does not cover it.
- New `docs/capabilities/comp.md`.

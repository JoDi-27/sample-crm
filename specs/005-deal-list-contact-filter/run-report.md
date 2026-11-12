# 005 · Run report: Deal list contact filter


**Mode:** autopilot · **Started by:** sdd-runtime-bot, by running /jsdd:auto 005 · **Branch:** autopilot/005-deal-list-contact-filter
**Started:** 2026-11-12 15:48 · **Ended:** 2026-11-12 16:00
**Outcome:** pull request ready

## Timeline

- Plan: sdd-reviewer round 1, one blocking finding (the AC-4 guard passes on old code), stated as deliberate in the plan. Checks frozen.
- T1 (filter and DealListApiTest): check failed before the code, passed after, 1 attempt.
- Verify round 1: the first `jsdd suite` failed 26 tests because the reviewer ran tests on the shared database at the same time (not a gap). A rerun alone found one real gap: `ApiDescriptionTest` pinned the deal list parameters.
- Amendment A1 (reviewer round 1, no blocking finding) and T2 (test update, 1 attempt).
- Verify round 2: converged. Full suite 95 tests, 0 failures, 25 s.


## Decisions made without a person

- Spec approved by sdd-runtime-bot, by starting the run (spec.approve).
- Plan approved: sdd-reviewer round 1 (auto: plan.approve).
- G1 approved, with option (a), update the test (auto: verify.gaps, auto: verify.gap_option, the recommended option).
- Amendment A1 approved: sdd-reviewer round 1 (auto: plan.approve).
- Capability entry for FR-DEAL-01 written in a new `docs/capabilities/deal.md` (the Deals area had none).


## Product decisions

None.


## Parked

Nothing.


## For the reviewer

- The parameter is named `mainContactId`, like the deal field (plan decision 1). The spec did not name it.
- Deals use derived queries, not Specifications as DEC-05 says for contacts (plan decision 2).
- AC-4's test passes on the old code on purpose, as a guard.
- Do not run two test runs against one `DB_URL` database at once: they truncate each other's tables.


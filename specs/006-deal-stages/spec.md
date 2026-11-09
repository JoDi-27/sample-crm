---
id: 006
title: "Deal stages"
type: feature
status: draft
phase: 2
depends_on: []
refs: ["PLAN Phase 2 / Deal stages", "FR-DEAL-04"]
updated: 2026-11-09
---

# 006 · Deal stages

## Problem

A deal has no outcome: the team cannot tell which deals are still being pursued and which were won or lost, so it cannot follow a deal to its end. This delivers the PLAN.md Phase 2 item "Deal stages" and row FR-DEAL-04.

## Scope

**In:**

- A stage on every deal: open, won, or lost.
- Setting a deal's stage when it is created, and changing it later.
- Listing deals by stage.

**Out:**

- Reporting on won and lost amounts: reporting is not now (intent).

## User scenarios

### S1 · Close a deal

A client wants to mark a deal as won or lost, so that the team knows its outcome.

1. The client creates a deal.
2. Later, the client sets its stage to won or lost.

### S2 · See what is still open

A client wants to list only the open deals, so that the team knows what it is still pursuing.

## Requirements

- **R1** (FR-DEAL-04) The service shall keep a stage on every deal: open, won, or lost. Deals that exist before stages are added shall have the stage open. (assumed, auto: product, sdd-product, inferred (same reasoning as R2), AS-07)
- **R2** (FR-DEAL-04) When a client creates a deal without a stage, the service shall give it the stage open. When a client creates a deal with a stage, the service shall store that stage. (assumed, auto: product, sdd-product, inferred (BACKLOG.md § Next "Deal stages"; intent § What success looks like; spec 006 R2), AS-02, AS-04)
- **R3** (FR-DEAL-04) When a client updates a deal with a stage, the service shall store it. When a client updates a deal without a stage, the service shall keep the deal's current stage; this is the one field an update does not clear when it is left out (spec 001 R5). (assumed, auto: product, sdd-product, inferred (intent § What success looks like; rule: reversible), AS-05)
- **R4** (FR-DEAL-04) If a client sends a stage that is not open, won, or lost, then the service shall reject the request as bad input.
- **R5** (FR-DEAL-04) When a client changes the stage of a deal that is won or lost, the service shall store the new stage, including open. (assumed, auto: product, sdd-product, inferred (spec 001 R5; spec 006 R3; constitution Art. 1), AS-03)
- **R6** (FR-DEAL-04) When a client lists deals with a stage, the service shall return only deals in that stage, optionally for one company. If the stage is not open, won, or lost, then the service shall reject the request as bad input naming `stage`. (assumed, auto: product, sdd-product, inferred (intent § Clear errors, Consistent API; NFR-API-01; spec 001 R12), AS-06)

## Acceptance criteria

- **AC-1** (R1, R2) Given a company, when a client creates a deal without a stage and reads it, then it has the stage open.
- **AC-2** (R3) Given an open deal, when a client updates it with the stage won, then a later read shows won.
- **AC-3** (R4) Given a deal, when a client updates it with the stage "maybe", then the request is rejected as bad input naming the stage, and the deal is unchanged.
- **AC-4** (R5) Given a won deal, when a client sets its stage to open, then a later read shows open.
- **AC-5** (R6) Given open, won, and lost deals at two companies, when a client lists deals with the stage open, then only open deals are returned; and with a company too, only that company's open deals.
- **AC-6** (R2) Given a company, when a client creates a deal with the stage lost, then a later read shows lost.
- **AC-7** (R3) Given a won deal, when a client updates its amount without a stage, then a later read shows won and the new amount.
- **AC-8** (R6) Given any deals, when a client lists deals with the stage "maybe", then the request is rejected as bad input naming `stage`.
- **AC-9** (R1) Given a deal stored before stages were added, when the stages are added and a client reads it, then it has the stage open.

## Capability changes

| Row | Change | Requirements |
| --- | --- | --- |
| FR-DEAL-04 | adds | R1–R6 |

## Open questions

- Q: What stage does a new deal start in? A: Open. Won and lost are outcomes, and a new deal has none yet. (assumed, auto: product, sdd-product, inferred (BACKLOG.md § Next "Deal stages"; intent § What success looks like; spec 006 S2), 2026-11-09, AS-02)
- Q: Can a won or lost deal be reopened, or is its stage final? A: It can be reopened: any valid stage is stored. No field is write-once today (spec 001 R5), and a stage set by mistake could otherwise only be fixed in the database (Art. 1). The weakest answer here; the owner should confirm it. (assumed, auto: product, sdd-product, inferred (spec 001 R5; spec 006 R3; constitution Art. 1), 2026-11-09, AS-03)
- Q: Can a client create a deal that is already won or lost? A: Yes; create and update share one request shape, and R4 applies to both. (assumed, auto: product, sdd-product, inferred (spec 006 R2; spec 001 R3/R5), 2026-11-09, AS-04)
- Q: When an update leaves the stage out, does the stage stay or reset? A: It stays. A reset would silently reopen won or lost deals; rejecting it would break every existing update client. (assumed, auto: product, sdd-product, inferred (intent § What success looks like; rule: reversible), 2026-11-09, AS-05)
- Q: Is an unknown stage on the deal list bad input? A: Yes, a 400 naming `stage`, like other malformed list parameters. (assumed, auto: product, sdd-product, inferred (intent § Clear errors, Consistent API; NFR-API-01; spec 001 R12), 2026-11-09, AS-06)
- Q: What stage do existing deals get? A: Open. (assumed, auto: product, sdd-product, inferred (as AS-02), 2026-11-09, AS-07)

## Changelog

- 2026-11-09 Created.
- 2026-11-09 Clarified by sdd-product, all inferred (auto: spec.clarify; auto: product): R2 new deals start open (AS-02) and may be created with a stage (AS-04); R5 won or lost deals can be reopened (AS-03); R3 an update without a stage keeps it (AS-05); R6 an unknown list stage is bad input (AS-06); R1 existing deals become open (AS-07). AC-4 completed; AC-6 to AC-9 added for the new statements.

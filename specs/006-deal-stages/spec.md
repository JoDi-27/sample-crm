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
- Changing a deal's stage.
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

- **R1** (FR-DEAL-04) The service shall keep a stage on every deal: open, won, or lost.
- **R2** (FR-DEAL-04) When a client creates a deal without a stage, the service shall give it a starting stage. [NEEDS CLARIFICATION: what stage does a new deal start in?]
- **R3** (FR-DEAL-04) When a client updates a deal with a stage, the service shall store it.
- **R4** (FR-DEAL-04) If a client sends a stage that is not open, won, or lost, then the service shall reject the request as bad input.
- **R5** (FR-DEAL-04) When a client changes the stage of a deal that is won or lost, the service shall [NEEDS CLARIFICATION: can a won or lost deal be reopened, or is its stage final?]
- **R6** (FR-DEAL-04) When a client lists deals with a stage, the service shall return only deals in that stage, optionally for one company.

## Acceptance criteria

- **AC-1** (R1, R2) Given a company, when a client creates a deal without a stage and reads it, then it has the starting stage.
- **AC-2** (R3) Given an open deal, when a client updates it with the stage won, then a later read shows won.
- **AC-3** (R4) Given a deal, when a client updates it with the stage "maybe", then the request is rejected as bad input naming the stage, and the deal is unchanged.
- **AC-4** (R5) Given a won deal, when a client sets its stage to open, then the service behaves as R5 says.
- **AC-5** (R6) Given open, won, and lost deals at two companies, when a client lists deals with the stage open, then only open deals are returned; and with a company too, only that company's open deals.

## Capability changes

| Row | Change | Requirements |
| --- | --- | --- |
| FR-DEAL-04 | adds | R1–R6 |

## Open questions

## Changelog

- 2026-11-09 Created.

---
id: 005
title: "Deal list contact filter"
type: feature
status: done
phase: 2
depends_on: []
refs: ["FR-DEAL-01"]
updated: 2026-11-12
---

# 005 · Deal list contact filter

## Problem

The deal list can be narrowed by company, but not by contact. A client that wants the deals a given person is the main contact for has to fetch every deal and search on its own side. This delivers row FR-DEAL-01. It is also a draft spec for testing the sdd runtime.

## Scope

**In:** an optional main contact filter on the deal list, alone or together with the existing company filter.

**Out:** paging the deal list, and filtering it by title or amount; these go to BACKLOG.md "Next" if wanted.

## User scenarios

### S1 · Find the deals of one contact

A client wants the deals whose main contact is a given contact, so that it can show what that person is working on without loading every deal.

1. The client lists deals with a contact filter.
2. The client gets only the deals whose main contact is that contact, in id order.

### S2 · Narrow by company and contact

A client wants the deals at one company that one contact leads.

1. The client lists deals with both a company filter and a contact filter.
2. The client gets only the deals that match both.

## Requirements

- **R1** (FR-DEAL-01) When a client lists deals with a contact filter, the service shall return only the deals whose main contact is that contact, sorted by id.
- **R2** (FR-DEAL-01) When a client lists deals with both a company filter and a contact filter, the service shall return only the deals that match both.
- **R3** (FR-DEAL-01) When a client lists deals with a contact filter that names no existing contact, the service shall return an empty list, as the company filter does for an unknown company.
- **R4** (FR-DEAL-01) When a client lists deals without a contact filter, the service shall return the same deals it does now.
- **R5** (FR-DEAL-01) If the contact filter is not a whole number, then the service shall reject the request with a client error that names the filter.

## Acceptance criteria

- **AC-1** (R1) Given contacts A and B, and deals D1 and D3 with main contact A and D2 with main contact B, when a client lists deals filtered by contact A, then exactly D1 and D3 are returned, in that order.
- **AC-2** (R2) Given companies X and Y, and deals with main contact A at X, with main contact A at Y, and with main contact B at X, when a client lists deals filtered by company X and contact A, then only the deal with main contact A at X is returned.
- **AC-3** (R3) Given the deals of AC-1, when a client lists deals filtered by a contact id that no contact has, then an empty list is returned.
- **AC-4** (R4) Given the deals of AC-1, when a client lists deals with no contact filter, then all three are returned; and when filtered by company only, the result is the same as before this change.
- **AC-5** (R5) Given the deals of AC-1, when a client lists deals with the contact filter `abc`, then the answer is a client error that names the filter.

## Capability changes

| Row | Change | Requirements |
| --- | --- | --- |
| FR-DEAL-01 | adds | R1, R2, R3, R4, R5 |

## Open questions

None.

## Changelog

- 2026-11-12 Created. FR-DEAL-01 added to `docs/requirements.md`; no baseline of the Deals area was taken first, as for spec 004.
- 2026-11-12 Approved (by sdd-runtime-bot, by starting /jsdd:auto).
- 2026-11-12 Plan approved (auto: plan.approve, sdd-reviewer round 1; the blocking AC-4 guard finding is now stated as deliberate).
- 2026-11-12 Implementing (auto: plan.approve).
- 2026-11-12 Verification round 1 found gaps; fixes approved, see verify-report.md (auto: verify.gaps).
- 2026-11-12 Amendment A1 approved (auto: plan.approve, sdd-reviewer round 1).
- 2026-11-12 Implementing A1 (auto: plan.approve).
- 2026-11-12 Verification round 2 converged; closed. Capability merged into docs/capabilities/deal.md.

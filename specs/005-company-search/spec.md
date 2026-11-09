---
id: 005
title: "Company search"
type: feature
status: in-progress
phase: 2
depends_on: []
refs: ["PLAN Phase 2 / Company search", "FR-COMP-03"]
updated: 2026-11-09
parked: "verify.gaps: Approve or drop G1 in verify-report.md: add a BACKLOG.md Next item to page the company and deal lists (the follow-up AS-01 defers); every AC passes and both Rs are delivered."
---

# 005 · Company search

## Problem

A client can list every company, but cannot find one by name; with more than a handful of companies the full list is hard to use. This delivers the PLAN.md Phase 2 item "Company search" and row FR-COMP-03.

## Scope

**In:**

- Narrowing the company list by part of the company name.
- No name text is rejected as bad input. An empty name text narrows nothing, because every name contains it, as with the contact list.

**Out:**

- Searching companies by fields other than name, and changes to contact or deal search: BACKLOG.md "Next" if wanted.
- Paging the company list (AS-01).
- Access control: any client may list companies, as for every operation today. API keys are spec 007 (FR-AUTH-01).

## User scenarios

### S1 · Find a company

A client wants to find a company by part of its name, so that it can open deals with it without scanning every company.

1. The client lists companies with a name text.
2. The service returns only the companies whose name contains it.

## Requirements

- **R1** (FR-COMP-03) When a client lists companies with a name text, the service shall return only companies whose name contains that text anywhere, ignoring case, matching every character of the text literally. If no company's name contains the text, then the service shall return an empty list and succeed.
- **R2** (FR-COMP-03) When a client lists companies without a name text, the service shall return every company, as it does today (spec 001 R8). The service shall not page the company list, with or without a name text. (assumed, auto: product, sdd-product, inferred (spec 001 R8; BACKLOG.md § Next), AS-01)

## Acceptance criteria

- **AC-1** (R1) Given companies named "Acme Corp", "ACME Labs", "Big Acme Ltd", and "Globex", when a client lists companies with the name text "acme", then Acme Corp, ACME Labs, and Big Acme Ltd are returned, and Globex is not.
- **AC-2** (R2) Given three companies, when a client lists companies without a name text, then all three are returned.
- **AC-3** (R1) Given companies "Acme Corp" and "Globex", when a client lists companies with the name text "zzz", then the request succeeds and no companies are returned.
- **AC-4** (R1) Given companies "A_B Corp" and "AxB Corp", when a client lists companies with the name text "a_b", then only A_B Corp is returned; and given "50% Off" and "50 Off Ltd", when a client lists companies with the name text "50%", then only 50% Off is returned.
- **AC-5** (R2) Given 25 companies whose names contain "acme", when a client lists companies with the name text "acme", then all 25 are returned; and given 25 companies, when a client lists companies without a name text, then all 25 are returned.
- **AC-6** (R1) Given companies "Acme Corp" and "Globex", when a client lists companies with an empty name text, then both are returned.

## Capability changes

| Row | Change | Requirements |
| --- | --- | --- |
| FR-COMP-03 | adds | R1, R2 |

## Open questions

- Q: Should the company list now be paged like the contact list, or keep returning every company? A: Keep returning every company, without paging, with or without a name text. Paging companies alone would change the response shape and still leave deals unpaged; paging both lists belongs in its own item. (assumed, auto: product, sdd-product, inferred (spec 001 R8/AC-8; spec 005 Scope; BACKLOG.md § Next "Company search"; constitution paging rule is conditional), 2026-11-09, AS-01)

## Changelog

- 2026-11-09 Created.
- 2026-11-09 R2 clarified: the company list stays unpaged (auto: spec.clarify, auto: product, sdd-product, inferred, AS-01).
- 2026-11-09 sdd-reviewer spec mode round 1, clarifications only (auto: spec.approve): AC-1 adds a match inside a name; AC-3 (no match), AC-4 (literal `_`), AC-5 (unpaged, 25 companies), and AC-6 (empty text) added for behavior R1 and R2 state; R1 says "anywhere", "literally", and the empty result; Scope says no name text is rejected, and puts paging and access control out.
- 2026-11-09 sdd-reviewer spec mode round 2, clarifications only (auto: spec.approve): AC-4 adds a literal `%` case; R1 and R2 second sentences in EARS form.
- 2026-11-09 Approved (auto: spec.approve, sdd-reviewer spec mode round 2).
- 2026-11-09 Plan approved (auto: plan.approve, sdd-reviewer round 2).
- 2026-11-09 Implementing (auto: plan.approve).
- 2026-11-09 Parked at gate verify.gaps: Approve or drop G1 in verify-report.md: add a BACKLOG.md Next item to page the company and deal lists (the follow-up AS-01 defers); every AC passes and both Rs are delivered. (auto: autonomous)

---
id: 005
title: "Company search"
type: feature
status: draft
phase: 2
depends_on: []
refs: ["PLAN Phase 2 / Company search", "FR-COMP-03"]
updated: 2026-11-09
---

# 005 · Company search

## Problem

A client can list every company, but cannot find one by name; with more than a handful of companies the full list is hard to use. This delivers the PLAN.md Phase 2 item "Company search" and row FR-COMP-03.

## Scope

**In:**

- Narrowing the company list by part of the company name.

**Out:**

- Searching contacts or deals by other fields: BACKLOG.md "Next" if wanted.

## User scenarios

### S1 · Find a company

A client wants to find a company by part of its name, so that it can open deals with it without scanning every company.

1. The client lists companies with a name text.
2. The service returns only the companies whose name contains it.

## Requirements

- **R1** (FR-COMP-03) When a client lists companies with a name text, the service shall return only companies whose name contains that text, ignoring case.
- **R2** (FR-COMP-03) When a client lists companies without a name text, the service shall return companies as it does today. [NEEDS CLARIFICATION: should the company list now be paged like the contact list, or keep returning every company?]

## Acceptance criteria

- **AC-1** (R1) Given companies named "Acme Corp", "ACME Labs", and "Globex", when a client lists companies with the name text "acme", then Acme Corp and ACME Labs are returned, and Globex is not.
- **AC-2** (R2) Given three companies, when a client lists companies without a name text, then all three are returned.

## Capability changes

| Row | Change | Requirements |
| --- | --- | --- |
| FR-COMP-03 | adds | R1, R2 |

## Open questions

## Changelog

- 2026-11-09 Created.

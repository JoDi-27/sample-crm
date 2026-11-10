---
id: 004
title: "Company name filter"
type: feature
status: in-progress
phase: 2
depends_on: []
refs: ["FR-COMP-01"]
updated: 2026-11-10
---

# 004 · Company name filter

## Problem

The company list returns every company, so a client looking for one company by name has to fetch them all and search on its own side. This delivers row FR-COMP-01. sdd-runtime spike 004 also uses it to test what an unattended run leaves behind when it is stopped part way.

## Scope

**In:** an optional name filter on the company list.

**Out:** paging the company list, and filtering it by anything else; these go to BACKLOG.md "Next" if wanted.

## User scenarios

### S1 · Find a company by part of its name

A client wants the companies whose name contains some text, so that it can pick one without loading the whole list.

1. The client lists companies with a name filter.
2. The client gets only the companies whose name contains that text, in any case.

## Requirements

- **R1** (FR-COMP-01) When a client lists companies with a `name` filter, the service shall return only the companies whose name contains that text, ignoring case.
- **R2** (FR-COMP-01) When a client lists companies without a `name` filter, or with an empty one, the service shall return every company, as it does now.

## Acceptance criteria

- **AC-1** (R1) Given the companies "Acme Ltd", "ACME Labs", and "Globex", when a client lists companies with `name=acme`, then exactly "Acme Ltd" and "ACME Labs" are returned.
- **AC-2** (R1) Given the same companies, when a client lists companies with `name=zzz`, then an empty list is returned.
- **AC-3** (R2) Given the same companies, when a client lists companies with no `name` filter, and again with `name=` empty, then all three are returned each time.

## Capability changes

| Row | Change | Requirements |
| --- | --- | --- |
| FR-COMP-01 | adds | R1, R2 |


## Open questions

None.

## Changelog

- 2026-11-10 Created.
- 2026-11-10 Approved (by sdd-runtime-bot, by starting /jsdd:auto).
- 2026-11-10 Plan approved (auto: plan.approve, sdd-reviewer round 2).
- 2026-11-10 Implementing (auto: plan.approve).

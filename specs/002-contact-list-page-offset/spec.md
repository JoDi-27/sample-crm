---
id: 002
title: "Contact list page offset"
type: feature
status: draft
phase: 2
depends_on: []
refs: ["FR-LIST-01", "BACKLOG: Next / Contact list repeats a row on page 2 and later"]
updated: 2026-11-10
---

# 002 · Contact list page offset

## Problem

Page 2 and every later page of the contact list start one row early, so each repeats the last contact of the page before it. A client walking the list page by page sees duplicates, and the last contact of the list can fall off the end. No test reads past page 1, which is why it was missed. This delivers row FR-LIST-01 and the BACKLOG "Next" item about it. It is also the first spec run headless by sdd-runtime (its spike 004): one small fix with one test.

## Scope

**In:** the starting row of page 2 and later pages of the contact list, with and without the company and name filters.

**Out:** page size limits, defaults, sort order, and the response shape, which spec 001 settled (R10–R12) and which stay as they are. A full baseline of the contact list area stays in BACKLOG.md "Next" for later.

## User scenarios

### S1 · Walk the contact list page by page

A client wants to read every contact a page at a time, so that it sees each contact exactly once.

1. The client lists page 1 with a page size.
2. The client lists page 2, 3, … with the same page size until a page comes back with fewer contacts.
3. Together the pages hold every matching contact once, in id order.

## Requirements

- **R1** (FR-LIST-01) When a client lists page *n* of the contacts with page size *s*, the service shall return the matching contacts in id order starting with the one right after the first (*n* − 1) × *s* of them.

## Acceptance criteria

- **AC-1** (R1) Given five contacts, when a client lists page 1, page 2, and page 3 with page size 2, then the pages hold 2, 2, and 1 contacts, no contact appears on two pages, and together they hold all five in id order.
- **AC-2** (R1) Given five contacts of which three are at one company, when a client lists page 2 with page size 2 filtered by that company, then exactly one contact is returned: the third of that company's contacts in id order.

## Capability changes

| Row | Change | Requirements |
| --- | --- | --- |
| FR-LIST-01 | adds | R1 |

## Open questions

None.

## Changelog

- 2026-11-10 Created.

---
id: 007
title: "API keys"
type: feature
status: draft
phase: 2
depends_on: []
refs: ["PLAN Phase 2 / API keys", "FR-AUTH-01"]
updated: 2026-11-09
---

# 007 · API keys

## Problem

Anyone who can reach the service can call it. This delivers the PLAN.md Phase 2 item "API keys" and row FR-AUTH-01.

## Scope

**In:**

- Rejecting calls without a valid API key.

**Out:**

- Users, roles, and sign-in.

## User scenarios

### S1 · A known client calls the API

A client with a key calls the API, and an unknown caller is turned away.

## Requirements

- **R1** (FR-AUTH-01) If a call has no valid API key, then the service shall reject it as unauthorized and change nothing.

## Acceptance criteria

- **AC-1** (R1) Given the running service, when a client calls any operation without a key, then the call is rejected as unauthorized.

## Capability changes

| Row | Change | Requirements |
| --- | --- | --- |
| FR-AUTH-01 | adds | R1 |

## Open questions

## Changelog

- 2026-11-09 Created.

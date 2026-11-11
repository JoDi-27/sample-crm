---
id: 003
title: "Health check"
type: feature
status: planned
phase: 2
depends_on: []
refs: ["FR-OPS-01"]
updated: 2026-11-11
parked: "task.manual_check: AC-2 (README health check section is clear to a new developer) can only be confirmed by the owner reading it; decide, then unpark and rerun."
---

# 003 · Health check

## Problem

Someone running the service, or a container platform, has no simple way to ask whether it is up. This delivers row FR-OPS-01. It is also sdd-runtime spike 004's park test: one criterion can be checked only by a person, so an unattended run must stop there and wait.

## Scope

**In:** a health check request that answers that the service is up, and a README section on calling it.

**Out:** checks of the database or other dependencies, metrics, and readiness versus liveness; these go to BACKLOG.md "Next" if wanted.

## User scenarios

### S1 · Check the service is up

A developer wants to know whether a running service is up, so that they can tell a broken deploy from a broken client.

1. The developer reads the README section on the health check.
2. The developer calls the health check and sees that the service is up.

## Requirements

- **R1** (FR-OPS-01) When a client calls `GET /health`, the service shall answer 200 with a JSON body whose `status` is `UP`.
- **R2** (FR-OPS-01) The README shall have a short section that tells a developer how to call the health check and what an up answer looks like.

## Acceptance criteria

- **AC-1** (R1) Given a running service, when a client calls `GET /health`, then the answer is 200 with `{"status": "UP"}`.
- **AC-2** (R2) Given the README, when the owner reads its health check section, then the owner confirms it is clear enough for a new developer to follow. Only the owner can check this: `check: manual: the owner reads the README health check section and confirms it is clear`.

## Capability changes

| Row | Change | Requirements |
| --- | --- | --- |
| FR-OPS-01 | adds | R1, R2 |


## Open questions

None.

## Changelog

- 2026-11-10 Created.
- 2026-11-11 Approved (by sdd-runtime-bot, by starting /jsdd:auto).
- 2026-11-11 Plan approved (auto: plan.approve, sdd-reviewer round 1). Findings 1-3 applied.
- 2026-11-11 Parked at gate task.manual_check: AC-2 (README health check section is clear to a new developer) can only be confirmed by the owner reading it; decide, then unpark and rerun. (auto: autopilot)

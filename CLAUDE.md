# sample-crm

A small CRM back end for companies, contacts, and deals, with an HTTP JSON API. Java, Spring Boot, PostgreSQL, tests with Testcontainers, CI on GitHub Actions. See `PRODUCT_INTENT.md`.

## Current state

Phase 1 (baseline), not started. There is no code yet. When the build lands, add the build, test, and run commands to this file.

## Where things live

| File | Read it for |
| --- | --- |
| `PRODUCT_INTENT.md` | What the product is, its concepts, guardrails, and technology |
| `PLAN.md` | Phases, their checklists, and exit criteria |
| `BACKLOG.md` | The happy path, open decisions with suggested defaults, and "Next" |
| `specs/constitution.md` | The rules every spec and plan must respect |
| `specs/README.md` | How the spec kit works |
| `specs/NNN-slug/` | One work item: `spec.md`, `plan.md`, `tasks.md` |
| `specs/STATUS.md` | Generated progress board; never edit by hand |
| `.claude/skills/sdd-*` | The six spec-kit skills |

## How work is done

Changes to `main` made outside `task/*` and `base/*` branches go through the spec-driven kit in `specs/`. Work on a `task/*` or `base/*` branch follows the instructions given for that task instead of this kit, and does not need a spec here.

- Do not implement anything that has no spec in `specs/`. Create one with `/sdd-specify` first, even for small items.
- Features follow `/sdd-specify` → `/sdd-plan` → `/sdd-implement` → `/sdd-verify`. Stop at the end of each step for the user's review; running the next command is the approval.
- Spikes follow `/sdd-spike` → `/sdd-verify`.
- Claim a spec or task with `specs/_kit/sdd claim` before working on it. The skills do this.
- Keep the spec's status, its task checkboxes, and `specs/STATUS.md` up to date (`specs/_kit/sdd status --write`).
- When code and spec disagree, update the spec or plan first, then the code (Article 10).
- When a spec settles a `BACKLOG.md` item or finishes a `PLAN.md` checklist item, update those files in place.

Shell helpers:

```sh
specs/_kit/sdd new feature "Title"    # or: new spike "Title"
specs/_kit/sdd status [--write]       # board and next up
specs/_kit/sdd next                   # only what can be worked on now
specs/_kit/sdd claim <ID> [next|Tn]   # claim a spec, or its next free task
specs/_kit/sdd release <ID> [Tn] [--done]
```

## Conventions

- Specs: `spec.md` holds what and why with no technology; `plan.md` holds how with no new requirements. Requirements use EARS, every requirement maps to an acceptance criterion, and every criterion to a test or recorded manual check.
- Unknowns are written as `[NEEDS CLARIFICATION: …]`, never guessed.
- Commit only when asked. Messages start with the spec and task ID, for example `[001] T2 Add company endpoints`.
- Docs are written in plain, short sentences.

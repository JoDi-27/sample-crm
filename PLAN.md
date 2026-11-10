# Plan

Phased plan for `sample-crm`, as described in `docs/intent.md`. Open decisions and their suggested defaults live in `BACKLOG.md`; happy path step numbers below refer to the happy path defined there.

Each checklist item is delivered through a spec in `specs/` (see `specs/README.md`); `specs/STATUS.md` shows progress.

| Phase | Goal | Status |
| --- | --- | --- |
| 0. Kit | Spec-driven development kit in place | Done |
| 1. Baseline | Happy path steps 1–6: a working service for companies, contacts, and deals, tested and run in CI, and tagged | Done |
| 2. Improvements | Find companies by name, follow deals to their outcome | Not started |

## Phase 0: Kit

- [x] **Spec-driven development kit** (the jsdd plugin; `specs/`)

## Phase 1: Baseline

- [x] **CRM service baseline:** companies, contacts, and deals over the API, with tests and CI
- [x] **Baseline tagged** once the service baseline is done (`baseline`, spec 001)

### Exit criteria

- Every happy path step works through the API.
- One build command runs every test locally, and CI runs the same tests on every push and pull request.
- The baseline commit on `main` is tagged.

## Phase 2: Improvements

- [ ] **Company search:** find companies by part of their name · FR-COMP-03
- [x] **Deal stages:** follow each deal to won or lost, and list deals by stage · FR-DEAL-04
- [ ] **API keys:** only known clients can call the API · FR-AUTH-01

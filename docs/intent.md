# Intent

> What we are building, for whom, and why: the short version. When a requirement, design, or decision conflicts with this file, either this file is wrong or the other thing is; resolve it, don't ignore it.

## The product in one sentence

`sample-crm` is a small CRM back end for companies, contacts, and deals, with an HTTP JSON API, kept as a test bed for trying out AI coding agents on realistic work.

## Who it is for

The people trying out AI coding agents, and the agents themselves. It has no real users. Its clients are its own tests and the people who call the API directly.

## Why it should exist

Agents need a codebase that looks like real work but stays small, conventional, and easy to run on a laptop. This is that codebase.

## What we are building

A back end with an HTTP JSON API and no user interface. It keeps track of:

- **Company:** an organization the team sells to.
- **Contact:** a person the team talks to. A contact may work at one company.
- **Deal:** a sales opportunity with a company, optionally with one contact as the main person on it, and an amount.

## Principles

<!-- specs/constitution.md turns these into checkable product articles (P1 to P5). -->

- **API only.** Every capability is reachable through the HTTP JSON API. There is no UI.
- **Clear errors.** Bad input is rejected with an error that says what is wrong. A rejected request changes nothing.
- **No dangling references.** A record never points to a company, contact, or deal that no longer exists.
- **Easy to run.** One build command runs every test on a laptop with Docker, and the same tests run in CI on every push.
- **Small and conventional.** Few dependencies, mainstream frameworks, no clever infrastructure.

## What success looks like

Not tracked. This is a test bed. It only needs to stay small, green, and easy to run.

## Not now

Authentication and users, multi-tenancy, a UI, integrations with other systems, and reporting.

## Next milestone

None planned. Work arrives as tasks on `task/*` branches, or as items in `BACKLOG.md` "Next". See [PLAN.md](../PLAN.md).

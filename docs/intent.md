# Intent

> What we are building, for whom, and why: the short version. When a requirement, design, or decision conflicts with this file, either this file is wrong or the other thing is; resolve it, don't ignore it.

## The product in one sentence

A small CRM back end, with an HTTP JSON API, that keeps track of the companies a small sales team sells to, the contacts who work at them, and the deals the team is pursuing.

## Who it is for

A small sales team, through the programs and people that call the API directly. It is also a sample project for trying out AI coding agents on realistic work.

## Why it should exist

The team needs one place to record companies, contacts, and deals, reachable by other programs. As a sample, it must stay small, conventional, and easy to run on a laptop.

## What we are building

- **Company:** an organization the team sells to.
- **Contact:** a person the team talks to. A contact may work at one company.
- **Deal:** a sales opportunity with a company, optionally with one contact as the main person on it, and an amount.

## Principles

- **API only.** Every capability is reachable through the HTTP JSON API. There is no UI.
- **Clear errors.** Bad input is rejected with an error that says what is wrong. A rejected request changes nothing.
- **No dangling references.** A record never points to a company, contact, or deal that no longer exists.
- **Easy to run.** One build command runs every test on a laptop with Docker, and the same tests run in CI on every push.
- **Small and conventional.** Few dependencies, mainstream frameworks, no clever infrastructure.
- **Consistent API.** Lists, filters, and errors work the same way for every record type.

## What success looks like

A client can run the whole sales flow through the API: record a company and its contacts, find them again, and follow each deal from first contact to its outcome.

## Not now

Authentication and users, multi-tenancy, a UI, integrations with other systems, and reporting.

## First version

See [PLAN.md](../PLAN.md) Phases 0 and 1 (done). Phase 2 improves it.

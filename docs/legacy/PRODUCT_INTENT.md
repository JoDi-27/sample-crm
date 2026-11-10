# Product intent

## What it is

`sample-crm` is a small customer relationship management (CRM) back end for a small sales team. It keeps track of companies, the contacts who work at them, and the deals the team is pursuing. It has an HTTP JSON API and no user interface; its clients are other programs and the people who call the API directly.

It is also a sample project for trying out AI coding agents on realistic work. That is why it stays small, conventional, and easy to run on a laptop.

## Concepts

- **Company:** an organization the team sells to.
- **Contact:** a person the team talks to. A contact may work at one company.
- **Deal:** a sales opportunity with a company, optionally with one contact as the main person on it, and an amount.

## Guardrails

- **API only.** Every capability is reachable through the HTTP JSON API. There is no UI.
- **Clear errors.** Bad input is rejected with an error that says what is wrong. A rejected request changes nothing.
- **No dangling references.** A record never points to a company, contact, or deal that no longer exists.
- **Easy to run.** One build command runs every test on a laptop with Docker, and the same tests run in CI on every push.
- **Small and conventional.** Few dependencies, mainstream frameworks, no clever infrastructure.

## Out of scope for now

Authentication and users, multi-tenancy, a UI, integrations with other systems, and reporting.

## Technology

Decided by the owner on 2026-10-18:

- Java with Spring Boot, exposing a REST API.
- PostgreSQL for storage.
- Tests run against a real PostgreSQL started by Testcontainers, so Docker is needed locally and in CI.
- GitHub Actions for CI.

Versions are chosen and recorded in each spec's plan.

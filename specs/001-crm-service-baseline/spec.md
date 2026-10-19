---
id: 001
title: "CRM service baseline"
type: feature
status: done
phase: 1
depends_on: []
refs: ["PLAN Phase 1 / CRM service baseline", "PLAN Phase 1 / Baseline tagged", "BACKLOG MVP / Records", "BACKLOG MVP / Error responses", "BACKLOG MVP / Deleting referenced records", "BACKLOG MVP / Contact list", "BACKLOG MVP / API description", "BACKLOG MVP / CI"]
updated: 2026-10-19
---

# 001 · CRM service baseline

## Problem

There is no service yet. A small sales team needs one place to record the companies it sells to, the people it talks to, and the deals it is pursuing, reachable through an API. This spec delivers the whole happy path in `BACKLOG.md` (steps 1–6): the three kinds of records, a contact list that can be paged and narrowed, clear errors, valid references after deletes, tests that run locally and in CI, and a tagged baseline that later work starts from.

## Scope

**In:**

- Companies, contacts, and deals: create, read one, update, delete, and list.
- A contact list that is paged and can be narrowed by company and by name.
- Rejection of bad input with errors that name each problem.
- What happens to contacts and deals when the company or contact they refer to is deleted.
- A machine-readable description of the API.
- Automated tests for every acceptance criterion, run by one build command and by CI on every push and pull request.
- A tag on the baseline commit.

**Out:**

- Authentication, users, and permissions: on record in `BACKLOG.md` as not a priority.
- A user interface: on record in `BACKLOG.md` as not a priority.
- Integrations with other systems: out of scope in `PRODUCT_INTENT.md`.
- Anything else: proposed in `BACKLOG.md` "Next" and specified separately.

## User scenarios

### S1 · Record a new customer

A salesperson's tool wants to record a new customer, so that the team can work with it.

1. It creates a company with a name.
2. It adds two contacts at that company and one contact with no company.
3. It reads each record back and sees what it sent, plus the identifier the service assigned.

### S2 · Find contacts

A salesperson's tool wants to show contacts a screen at a time, so that it never loads everything at once.

1. It asks for the first page of contacts with a page size and gets that many contacts at most, plus the total number of matching contacts.
2. It narrows the list to the contacts of one company, or to contacts whose name contains some text.

### S3 · Track a deal

A salesperson's tool wants to track a deal, so that the team knows what it is pursuing and with whom.

1. It opens a deal with a title, an amount, the company, and one of the company's contacts as the main contact.
2. It lists the deals of that company.
3. It updates the deal's amount.

### S4 · Clean up

A salesperson's tool wants to delete records that are no longer needed, without breaking the records that refer to them.

1. It deletes a contact who is the main contact of a deal. The deal stays, with no main contact.
2. It deletes a company. Its contacts stay, with no company. Its deals stay, with no company.

### S5 · Change the code safely

A developer wants to change the service, so that it keeps working afterwards.

1. They run one build command, which runs every test against a real database.
2. They push a branch and open a pull request. CI runs the same tests and reports the result on the commit.

## Requirements

Records:

- **R1** When a client creates a company with a name and, optionally, a website, the service shall store it, assign it an identifier, and return it.
- **R2** When a client creates a contact with a first name, a last name, an email address, and, optionally, a phone number and a company, the service shall store it, assign it an identifier, and return it.
- **R3** When a client creates a deal with a title, an amount, a company, and, optionally, a main contact, the service shall store it, assign it an identifier, and return it.
- **R4** When a client reads a company, contact, or deal by its identifier, the service shall return it.
- **R5** When a client updates a company, contact, or deal, the service shall replace its fields with the ones sent and return the result. A deal update may leave the company empty.
- **R6** When a client deletes a company, contact, or deal, the service shall remove it.
- **R7** If a client reads, updates, or deletes a record that does not exist, then the service shall answer that it was not found and change nothing.
- **R8** When a client lists companies, the service shall return every company.
- **R9** When a client lists deals, optionally for one company, the service shall return every deal, or every deal of that company.

Contact list:

- **R10** When a client lists contacts with a page number and a page size, the service shall return at most that many contacts in a stable order, together with the page number, the page size, and the total number of matching contacts. Pages are numbered from 1.
- **R11** When a client lists contacts without a page number or page size, the service shall use page 1 and a page size of 20.
- **R12** If a client asks for a page number below 1, or a page size below 1 or above 100, then the service shall reject the request as bad input.
- **R13** When a client lists contacts for one company, the service shall return only that company's contacts.
- **R14** When a client lists contacts with a name text, the service shall return only contacts whose first or last name contains that text, ignoring case.
- **R15** When a client asks for a page after the last page, the service shall return no contacts and the total number of matching contacts.

Bad input:

- **R16** If a create or update request misses a required field, has a field that is too long, has an email address that is not well formed, has a negative amount, or refers to a company or contact that does not exist, then the service shall reject it as bad input, name every field that is wrong and why, and change nothing.

References after deletes:

- **R17** When a client deletes a company, the service shall keep that company's contacts and clear their company.
- **R18** When a client deletes a company that has deals, the service shall keep those deals and clear their company. A deal's company is required only when the deal is created.
- **R19** When a client deletes a contact who is the main contact of deals, the service shall keep those deals and clear their main contact.

API description and build:

- **R20** The service shall publish a machine-readable description of its API that covers every operation in this spec.
- **R21** The project shall have one build command that runs every automated test against a real instance of the database the service uses. Where a database is given by environment variables, the tests shall use it and start none; otherwise they shall start one themselves.
- **R22** When a commit is pushed to the hosted repository or a pull request is opened or updated, CI shall run every automated test against a database it provides to the tests through environment variables, and report the result on that commit.
- **R23** When this spec is verified, the baseline commit on `main` shall carry the tag `baseline`.

## Acceptance criteria

- **AC-1** (R1, R4) Given no data, when a client creates a company with a name and a website and then reads it by the returned identifier, then the response has the same name and website.
- **AC-2** (R2, R4) Given a company, when a client creates a contact at that company with a first name, last name, email address, and phone number and then reads it, then the response has the same fields and the company's identifier.
- **AC-3** (R2) Given no data, when a client creates a contact without a company, then it is stored with no company.
- **AC-4** (R3, R4) Given a company and one of its contacts, when a client creates a deal with a title, amount, the company, and the contact as main contact, and then reads it, then the response has the same fields.
- **AC-5** (R5) Given a company, a contact, and a deal, when a client updates each with new field values, then each response and each later read show the new values.
- **AC-6** (R6, R7) Given a company, a contact, and a deal, when a client deletes each and then reads it, then each read answers not found.
- **AC-7** (R7) Given no record with a given identifier, when a client reads, updates, or deletes a company, contact, or deal with that identifier, then the service answers not found and the data is unchanged.
- **AC-8** (R8) Given three companies, when a client lists companies, then all three are returned.
- **AC-9** (R9) Given two companies with deals, when a client lists all deals and then the deals of one company, then the first list has every deal and the second only that company's deals.
- **AC-10** (R10) Given five contacts, when a client lists page 1 with page size 2, then two contacts are returned with page 1, page size 2, and total 5, and the same request made again returns the same two contacts in the same order.
- **AC-11** (R11) Given 25 contacts, when a client lists contacts without paging parameters, then 20 contacts are returned with page 1, page size 20, and total 25.
- **AC-12** (R12) Given any data, when a client asks for page 0, page size 0, or page size 101, then each request is rejected as bad input naming the parameter.
- **AC-13** (R13) Given contacts at two companies and contacts with no company, when a client lists the contacts of one company, then only that company's contacts are returned, and the total counts only them.
- **AC-14** (R14) Given contacts named "Ana Silva", "Bruno Silvano", and "Carla Mendes", when a client lists contacts with the name text "silv", then Ana Silva and Bruno Silvano are returned, and Carla Mendes is not.
- **AC-15** (R15) Given three contacts, when a client lists page 5 with page size 2, then no contacts are returned and the total is 3.
- **AC-16** (R16) Given a valid request for each kind of record, when a client sends it with a required field missing, a field too long, a malformed email address (contacts), a negative amount (deals), or a company or contact identifier that does not exist, then each request is rejected as bad input, the error names the wrong fields with a reason, and nothing is created or changed.
- **AC-17** (R16) Given a contact create request with both a missing first name and a malformed email address, when a client sends it, then the error names both fields.
- **AC-18** (R17) Given a company with two contacts, when a client deletes the company, then both contacts can still be read and have no company.
- **AC-19** (R18) Given a company with a deal, when a client deletes the company, then the deal can still be read and has no company.
- **AC-20** (R19) Given a deal whose main contact is a given contact, when a client deletes that contact, then the deal can still be read and has no main contact.
- **AC-21** (R20) Given the running service, when a client fetches the API description, then it lists every operation of R1–R15.
- **AC-22** (R21) Given a clean checkout on a laptop with Docker, when the developer runs the build command, then every test runs against a real database started for the tests and the build passes. Recorded as a manual check.
- **AC-23** (R22) Given the repository on GitHub, when a commit is pushed and a pull request is opened, then CI runs every test and its result appears on the commit. Recorded as a manual check with links to the CI runs.
- **AC-24** (R23) Given this spec verified, when the tags of the repository are listed, then the baseline tag exists on `main` at the commit that records this verification, whose parent is the verified commit. Recorded as a manual check.
- **AC-25** (R5, R18) Given a deal whose company was deleted, when a client updates its amount and sends no company, then the update is accepted and the deal still has no company.
- **AC-26** (R21, R22) Given a running PostgreSQL given by environment variables, when the build command runs on a machine with no container runtime, then every test passes against that database; and given no such variables on a machine with Docker, the same command passes with a database started for the tests. Recorded as a manual check.

## Open questions

- Q: When a company with deals is deleted, what happens to the deals? A: They stay, with the company cleared, the same rule as contacts (user, 2026-10-18).
- Q: What is the baseline tag called? A: `baseline` (user, 2026-10-18).

## Changelog

- 2026-10-18 Created.
- 2026-10-18 Clarified R18 (deals kept with the company cleared) and R23 (tag `baseline`).
- 2026-10-18 Approved by running /sdd-plan.
- 2026-10-18 Planned: plan.md and tasks.md written.
- 2026-10-18 In progress: plan approved by running /sdd-implement.
- 2026-10-19 R21 and R22: tests use a database given by environment variables when there is one, and CI provides one that way; added AC-26.
- 2026-10-19 AC-24: the tag goes on the commit that records the verification, since a commit cannot name its own SHA.
- 2026-10-19 Verified and closed: every acceptance criterion passes at `5390797`.

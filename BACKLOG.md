# Backlog

The baseline goal is one working happy path through the API. When it works end to end, is tested, and runs in CI, the baseline is ready; everything after that is an improvement.

MVP items come with a suggested default to confirm or replace.

## The happy path

1. A client creates a company.
2. A client adds contacts, some of them at that company.
3. A client lists contacts a page at a time, and narrows the list by company or by name.
4. A client opens a deal with the company, with one of its contacts as the main person.
5. A client updates and deletes records, and the references between them stay valid.
6. Every step is covered by tests that run locally and in CI.

## MVP: decide to build the happy path

- [ ] **Records:** companies, contacts, and deals, each with create, read, update, delete, and list. *Suggested:* the fields named in spec 001.
- [ ] **Error responses:** one error format for bad input, naming each problem. *Suggested:* the HTTP status plus a list of field errors.
- [ ] **Deleting referenced records:** what happens to contacts and deals when their company or contact is deleted. *Suggested:* they stay, with the reference cleared.
- [ ] **Contact list:** contacts are listed a page at a time, filtered by company or by name. *Suggested:* pages numbered from 1, with a default and a maximum page size.
- [ ] **API description:** a machine-readable description of the API. *Suggested:* generated from the code.
- [ ] **CI:** a workflow that runs every test on each push and pull request. *Suggested:* GitHub Actions on `main` and pull requests.

## Spikes

None open.

## Next: improvements after the baseline

<!-- Ideas that are not part of the baseline. Each becomes a spec when it is picked up. -->

## On record: not a priority

- Authentication and users.
- A user interface.

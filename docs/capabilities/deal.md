# Deals (FR-DEAL)

> How this area of the product works now. `docs/requirements.md` lists its rows in one line each; this document holds the detail. `/jsdd:verify` keeps it current: when a spec or baseline closes, its Capability changes are merged here. Edit it only through a spec.

## FR-DEAL-01 · A client can narrow the deal list to deals whose main contact is a given contact, alone or together with the company filter

Last changed: 005 (2026-11-12)

- When a client lists deals with a contact filter (`mainContactId`), the service shall return only the deals whose main contact is that contact, sorted by id.
- When a client lists deals with both a company filter and a contact filter, the service shall return only the deals that match both.
- When a client lists deals with a contact filter that names no existing contact, the service shall return an empty list, as the company filter does for an unknown company.
- When a client lists deals without a contact filter, the service shall return the same deals it does now.
- If the contact filter is not a whole number, then the service shall reject the request with a client error that names the filter.

**Proven by:** `./mvnw test -Dtest=DealListApiTest` (DealListApiTest: filterByContact, filterByCompanyAndContact, unknownContactIsEmpty, noContactFilterUnchanged, invalidContactFilter), and `ApiDescriptionTest.listsEveryOperation` for the API description.

## History

| Date | Spec | Row | Change |
| --- | --- | --- | --- |
| 2026-11-12 | 005 | FR-DEAL-01 | adds |

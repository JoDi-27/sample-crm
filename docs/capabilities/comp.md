# Companies (FR-COMP)

> How this area of the product works now. `docs/requirements.md` lists its rows in one line each; this document holds the detail. `/jsdd:verify` keeps it current: when a spec or baseline closes, its Capability changes are merged here. Edit it only through a spec.


## FR-COMP-01 · A client can narrow the company list to companies whose name contains a given text, ignoring case

Last changed: 004 (2026-11-10)

- When a client lists companies with a `name` filter, the service shall return only the companies whose name contains that text, ignoring case.
- When a client lists companies without a `name` filter, or with an empty one, the service shall return every company, as it does now.

**Proven by:** `./mvnw test -Dtest=CompanyListApiTest` (CompanyListApiTest: filterIgnoresCase, filterWithNoMatch, noFilterReturnsAll)

## History

| Date | Spec | Row | Change |
| --- | --- | --- | --- |
| 2026-11-10 | 004 | FR-COMP-01 | adds |

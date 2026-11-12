# Contact list, paging, and search (FR-LIST)

> How this area of the product works now. `docs/requirements.md` lists its rows in one line each; this document holds the detail. `/jsdd:verify` keeps it current: when a spec or baseline closes, its Capability changes are merged here. Edit it only through a spec.

## FR-LIST-01 · Page *n* of the contact list, in id order, starts with the contact right after the first (*n* − 1) × size matching contacts, so consecutive pages neither repeat nor skip a contact

Last changed: 002 (2026-11-11)

- When a client lists page *n* of the contacts with page size *s*, the service shall return the matching contacts in id order starting with the one right after the first (*n* − 1) × *s* of them.

**Proven by:** `./mvnw -q test -Dtest=ContactListApiTest` (`ContactListApiTest.pagesDoNotOverlap` for AC-1, `ContactListApiTest.secondPageOfCompanyFilter` for AC-2)

## History

| Date | Spec | Row | Change |
| --- | --- | --- | --- |
| 2026-11-11 | 002 | FR-LIST-01 | adds |

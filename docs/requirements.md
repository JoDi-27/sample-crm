# Requirements

> The product-level list of what the product must do. Specs in `specs/` make these rows testable and link back by ID; progress comes from `jsdd coverage`, never from a status column here.

**Format.** `jsdd coverage` reads every table row whose first cell is an ID like `FR-AUTH-01` or `NFR-SEC-01`. Functional rows have four cells (ID, requirement, source, priority); the priority is `M` Must, `S` Should, `C` Could, or `W` Won't. NFR rows may leave out source and priority. IDs never change once a spec cites them: reword a row in place, or add a new one (`FR-AUTH-01b`) for a split.

## 1. Functional requirements

The service already exists (spec 001). Its current behavior is not listed here yet. Each area gets its rows from a baseline (`/jsdd:baseline <area>`) when work first touches it, and new behavior is added as rows in the same area.

| Area | Group | Code | Tests |
|---|---|---|---|
| Companies | `FR-COMP` | `company/`, `V1__companies.sql` | `CompanyApiTest`, `CompanyListApiTest` |
| Contacts (create, read, update, delete) | `FR-CONT` | `contact/Contact*.java`, `V2__contacts.sql` | `ContactApiTest` |
| Contact list, paging, and search | `FR-LIST` | `ContactController.list`, `ContactService.list`, `contact/OffsetPage.java`, `api/PageResponse.java` | `ContactListApiTest` |
| Deals | `FR-DEAL` | `deal/`, `V3__deals.sql` | `DealApiTest` |
| Errors and input validation | `FR-ERR` | `api/`, validation in each `*Controller` | `badInput*` and `unknownId` in each `*ApiTest` |
| Deleting referenced records | `FR-REF` | `ON DELETE SET NULL` in `V2`, `V3`; `CompanyService.delete`, `ContactService.delete` | `*Keeps*` tests in `ContactApiTest`, `DealApiTest` |
| API description | `FR-APIDOC` | springdoc in `pom.xml` (`/v3/api-docs`) | `ApiDescriptionTest` |

### Companies (FR-COMP)

| ID | Requirement | Source | Priority |
|---|---|---|---|
| FR-COMP-01 | A client can narrow the company list to companies whose name contains a given text, ignoring case | sdd-runtime spike 004 | S |

### Operations (FR-OPS)

| ID | Requirement | Source | Priority |
|---|---|---|---|
| FR-OPS-01 | The service answers a health check that says it is up, and the README tells a developer how to call it | sdd-runtime spike 004 | S |

### Contact list, paging, and search (FR-LIST)

| ID | Requirement | Source | Priority |
|---|---|---|---|
| FR-LIST-01 | Page *n* of the contact list, in id order, starts with the contact right after the first (*n* − 1) × size matching contacts, so consecutive pages neither repeat nor skip a contact | Spec 001 R10; BACKLOG "Next" | M |

## 2. Non-functional requirements

| Area | Group | Code | Tests |
|---|---|---|---|
| Build, local run, and CI | `NFR-OPS` | `pom.xml`, `mvnw`, `.github/workflows/ci.yml`, `application.yml`, `TestcontainersConfiguration.java` | `SmokeTest`, manual checks AC-22 to AC-26 in spec 001 |

## 3. Open questions

None. Decisions go to `docs/decisions.md` "Still open" with a `BACKLOG.md` line; research goes to `BACKLOG.md` as a spike.

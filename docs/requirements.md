# Requirements

> The product-level list of what the product must do. Specs in `specs/` make these rows testable and link back by ID; progress comes from `jsdd coverage`, never from a status column here.

## 1. Functional requirements

### 1.1 Companies

| ID | Requirement | Source | Pri |
|---|---|---|---|
| FR-COMP-01 | A client can create, read, update, and delete a company with a name and an optional website | spec 001 R1, R4–R7 | M |
| FR-COMP-02 | A client can list companies | spec 001 R8 | M |
| FR-COMP-03 | A client can find companies by part of their name, ignoring case | owner, 2026-11-09 | S |

### 1.2 Contacts

| ID | Requirement | Source | Pri |
|---|---|---|---|
| FR-CONT-01 | A client can create, read, update, and delete a contact, optionally at one company | spec 001 R2, R4–R7 | M |
| FR-CONT-02 | A client can list contacts a page at a time, narrowed by company or by name | spec 001 R10–R15 | M |
| FR-CONT-03 | Deleting a company keeps its contacts and clears their company | spec 001 R17 | M |

### 1.3 Deals

| ID | Requirement | Source | Pri |
|---|---|---|---|
| FR-DEAL-01 | A client can create, read, update, and delete a deal with a title, an amount, a company, and an optional main contact | spec 001 R3–R7 | M |
| FR-DEAL-02 | A client can list deals, optionally for one company | spec 001 R9 | M |
| FR-DEAL-03 | Deleting a company or a contact keeps its deals and clears the reference | spec 001 R18–R19 | M |
| FR-DEAL-04 | A deal has a stage that shows whether it is still open, won, or lost, and a client can list deals by stage | owner, 2026-11-09 | S |

### 1.4 Access

| ID | Requirement | Source | Pri |
|---|---|---|---|
| FR-AUTH-01 | Only clients with a valid API key can call the API | owner, 2026-11-09 | C |

## 2. Non-functional requirements

### 2.1 Errors and API

| ID | Requirement |
|---|---|
| NFR-API-01 | Bad input is rejected with RFC 9457 problem details naming each field, and changes nothing |
| NFR-API-02 | The API is described at `/v3/api-docs`, covering every operation |

## 3. Open questions

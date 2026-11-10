# Constitution

The non-negotiable rules every spec and plan is checked against. Each plan has a "Constitution check" that names the articles it touches.

`docs/intent.md` stays the source of product intent; the product articles distill the parts of it that constrain day-to-day design. To change an article, record the decision in the amendments log at the bottom, and update the intent doc if the product intent changed too.

## Product articles

- **P1 API only.** Every capability is reachable through the HTTP JSON API. There is no UI, and no behavior that only a script or the database can reach.
- **P2 Clear errors.** Bad input is rejected with a client error that names each problem. A rejected request changes nothing.
- **P3 No dangling references.** A record never points to a company, contact, or deal that no longer exists. Every spec that deletes or changes references says what happens to the records that point at it.
- **P4 Easy to run.** One build command runs every test on a laptop with Docker. Tests use a real PostgreSQL, never an in-memory substitute.
- **P5 Small and conventional.** Prefer mainstream frameworks and few dependencies. A new dependency or piece of infrastructure needs a row in the plan's Decisions table.
- **P6 Green main.** Every commit on `main` builds and passes its tests, locally and in CI.

## Engineering articles

1. **Happy path first.** Build the simplest version that moves the current roadmap item forward. Anything else goes to the backlog, not into the current spec.
2. **Tests prove acceptance criteria.** Every acceptance criterion maps to an automated test or a recorded manual check. A task is done only when its check passes.
3. **Small, traceable increments.** One spec is one reviewable slice (a few days at most; split it otherwise). One task is about one commit, and commit messages start with the spec and task ID, for example `[003] T2 Add invite table`.
4. **Specs are living.** When implementation shows the spec or plan is wrong, update them first, then the code. Code never silently diverges from its spec.
5. **Decisions are written down.** Every choice between real alternatives gets a row in the plan's Decisions table (or a spike's Recommendation). A choice that settles an open decision moves its row in `docs/decisions.md` from Still open to Decided, and ticks the backlog item.
6. **Verify current docs.** Before using a library, SDK, or tool API, check its current documentation rather than relying on memory, and note the version in the plan.

## Project specifics

What the kit's skills and the reviewer need to know about this project.

- **Test command:** `./mvnw verify` (one class: `./mvnw test -Dtest=<Class>`). It needs Java 25 and a running Docker, or a PostgreSQL given by `DB_URL`.

### Architecture checklist

All code is under `src/main/java/com/example/samplecrm/`, migrations under `src/main/resources/db/migration/`.

- **Controller and service roles.** Controllers are package-private and services are public. Services are `@Transactional` at class level, with `@Transactional(readOnly = true)` on reads.
- **Reference checks in the controller.** A controller checks each referenced id with the other package's repository (`existsById`) and reports an unknown one as a field error (400), not a 404.
- **Not found comes from the service.** Each service has a private `find(id)` that throws `NotFoundException`; `get`, `update`, and `delete` go through it.
- **Plain id references.** Entities hold references to other records as `Long` ids, never JPA associations. PUT is a full replacement through the entity's `update(request)`, and responses map through `*Response.from(entity)`.
- **HTTP and list shape.** POST returns 201 with a `Location` header and the body, PUT returns 200 with the body, DELETE returns 204. Every list is sorted by id. A paged list returns `PageResponse{items, page, size, total}` with `page` starting at 1.
- **Flyway owns the schema.** Migrations are named `V<n>__<table>.sql`. Ids are `bigint GENERATED ALWAYS AS IDENTITY`, every foreign key column has an index, and Hibernate runs with `ddl-auto: validate`.
- **Bad input changes nothing, and tests prove it.** Bad-input tests are parameterized and check that row counts and stored values did not change.

## Amendments

| Date | Article | Change | Why |
| --- | --- | --- | --- |
| 2026-11-10 | All | Moved to the jsdd plugin's format. Product articles 1–5 became P1–P5, and old article 8 (Green main) became P6. Old engineering articles 6, 7, 9, 10, 11, and 12 became engineering articles 1 to 6, in the plugin's wording. Added Project specifics. | Migration from the install.sh kit to the plugin. Spec 001 cites the old numbers and is left as written. |

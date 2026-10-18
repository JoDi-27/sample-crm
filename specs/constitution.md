# Constitution

The non-negotiable rules every spec and plan is checked against. Each plan has a "Constitution check" that names the articles it touches.

`PRODUCT_INTENT.md` stays the source of product intent; the product articles below distill the parts of it that constrain day-to-day design. To change an article, record the decision in the amendments log at the bottom, and update the intent doc if the product intent changed too.

## Product articles

1. **API only.** Every capability is reachable through the HTTP JSON API. There is no UI, and no behavior that only a script or the database can reach.
2. **Clear errors.** Bad input is rejected with a client error that names each problem. A rejected request changes nothing.
3. **No dangling references.** A record never points to a company, contact, or deal that no longer exists. Every spec that deletes or changes references says what happens to the records that point at it.
4. **Easy to run.** One build command runs every test on a laptop with Docker. Tests use a real PostgreSQL, never an in-memory substitute.
5. **Small and conventional.** Prefer mainstream frameworks and few dependencies. A new dependency or piece of infrastructure needs a row in the plan's Decisions table.

## Engineering articles

6. **Happy path first.** Build the simplest version that moves a `BACKLOG.md` happy path step forward. Anything else goes to `BACKLOG.md` "Next", not into the current spec.
7. **Tests prove acceptance criteria.** Every acceptance criterion maps to an automated test or a recorded manual check. A task is done only when its check passes.
8. **Green main.** Every commit on `main` builds and passes its tests, locally and in CI.
9. **Small, traceable increments.** One spec is one reviewable slice (a few days at most; split it otherwise). One task is about one commit, and commit messages start with the spec and task ID, for example `[001] T2 Add company endpoints`.
10. **Specs are living.** When implementation shows the spec or plan is wrong, update them first, then the code. Code never silently diverges from its spec.
11. **Decisions are written down.** Every choice between real alternatives gets a row in the plan's Decisions table (or a spike's Recommendation). Choices that settle a `BACKLOG.md` item also update it in place.
12. **Verify current docs.** Before using a library, SDK, or tool API, check its current documentation rather than relying on memory, and note the version in the plan.

## Amendments

| Date | Article | Change | Why |
| --- | --- | --- | --- |

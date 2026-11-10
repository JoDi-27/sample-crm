# Deals (FR-DEAL)

> How this area of the product works now. `docs/requirements.md` lists its rows in one line each; this document holds the detail. `/jsdd:verify` keeps it current: when a spec or baseline closes, its Capability changes are merged here. Edit it only through a spec.

## FR-DEAL-04 · A deal has a stage that shows whether it is still open, won, or lost, and a client can list deals by stage

Last changed: 006 (2026-11-10)

- The service shall keep a stage on every deal: open, won, or lost, and shall show it on every deal it returns. Deals that existed before stages were added have the stage open.
- When a client creates a deal without a stage, or with an empty (null) stage, the service shall give it the stage open. When a client creates a deal with a stage, the service shall store that stage.
- When a client updates a deal with a stage, the service shall store it. When a client updates a deal without a stage, or with an empty (null) stage, the service shall keep the deal's current stage: unlike the other fields an update replaces, a stage left out keeps its value.
- If a client creates or updates a deal with a stage that is not exactly open, won, or lost, then the service shall reject the request as bad input naming the stage, and change nothing.
- When a client changes the stage of a deal that is won or lost, the service shall store the new stage, including open.
- When a client lists deals with a stage, the service shall return only deals in that stage, optionally for one company, and an empty list when no deal matches. If the stage is not exactly open, won, or lost, then the service shall reject the request as bad input naming `stage`.

**Proven by:** `./mvnw -B verify` (`DealStageApiTest`, `DealStageListApiTest`, `DealStageMigrationTest`)

## History

| Date | Spec | Row | Change |
| --- | --- | --- | --- |
| 2026-11-10 | 006 | FR-DEAL-04 | adds |

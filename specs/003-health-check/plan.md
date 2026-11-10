# 003 · Plan: Health check

## Approach

Add a small package-private controller in a new `health` package. `GET /health` returns 200 and `{"status":"UP"}`. Add a README section. One test class proves AC-1. AC-2 is a reading judgement and no command can prove it.

## Constitution check

| Article | How the plan complies |
| --- | --- |
| P1 API only | The check is an HTTP JSON endpoint. |
| P4 Easy to run | The test uses the existing `ApiTestBase` with PostgreSQL in a container. |
| P5 Small and conventional | No new dependency. No Actuator. See decision 1. |
| P6 Green main | Each task leaves `./mvnw verify` green. |
| Art. 1 Happy path first | No dependency checks, metrics, or readiness. These stay out of scope. |
| Art. 2 Tests prove ACs | AC-1 has an automated test. AC-2 is a recorded manual check, as the spec says. |
| Art. 3 Small increments | Two tasks, one commit each. |
| Art. 5 Decisions written down | Decision 1 below. |
| Architecture checklist | Controller is package-private. No entity, no service, no schema change. |

## Design

- `src/main/java/com/example/samplecrm/health/HealthController.java`: `@RestController`, package-private, `@GetMapping("/health")` returning `Map.of("status", "UP")`.
- `README.md`: a "Health check" section with a `curl` example and the up answer, and a row in the API table.
- No database access, so the answer does not depend on PostgreSQL.

## Requirement coverage

| Requirement | Covered by |
| --- | --- |
| R1 | `HealthController`, T1 |
| R2 | README section, T2 |

## Decisions

| # | Decision | Alternatives | Why |
| --- | --- | --- | --- |
| 1 | Hand-written controller | Spring Boot Actuator `/actuator/health` | The spec fixes the path `/health`. Actuator adds a dependency and a different path (P5). |

## Test strategy

| Acceptance criterion | Verified by (unit, integration, e2e, manual) |
| --- | --- |
| AC-1 | Integration: `HealthApiTest` calls `GET /health` and checks status 200 and body `{"status":"UP"}`. |
| AC-2 | Manual: the owner reads the README section. T2 checks the section exists; T3 is the owner's reading. |

## Risks and unknowns

- AC-2 cannot be proven by a command, so an unattended run parks at `task.manual_check` once the work is planned.

## Deviations

## Amendments

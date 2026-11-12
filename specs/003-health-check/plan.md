# 003 · Plan: Health check

## Approach

Add a small `health` package with a package-private controller that answers `GET /health` with `{"status":"UP"}`. It checks nothing else (spec scope). Add a short README section. Test the endpoint through the existing `ApiTestBase`.

## Constitution check

| Article | How the plan complies |
| --- | --- |
| Engineering 4 (spec and plan agree) | No requirement is added here; each R maps to a design part below. |
| Project specifics, architecture checklist | Controller is package-private. No entity, service, or schema is needed, as there is no data. |
| Tests prove behavior | One API test covers AC-1. |

## Design

- `com.example.samplecrm.health.HealthController`: `@RestController`, package-private, `@GetMapping("/health")` returns `Map.of("status", "UP")` with 200.
- No new dependency. Spring Boot Actuator is not added: it is out of scope (no dependency checks, metrics, or probes) and would expose `/actuator/health`, not `/health`.
- `README.md`: a "Health check" section with a `curl` example and the up answer.

## Requirement coverage

| Requirement | Covered by |
| --- | --- |
| R1 | `HealthController`, T1 |
| R2 | README section, T2 |

## Decisions

| # | Decision | Alternatives | Why |
| --- | --- | --- | --- |
| 1 | Hand-written controller | Spring Boot Actuator | Actuator serves another path and adds scope the spec leaves out. |

## Test strategy

| Acceptance criterion | Verified by (unit, integration, e2e, manual) |
| --- | --- |
| AC-1 | Integration: `HealthApiTest` calls `GET /health`, expects 200 and `status` `UP`. |
| AC-2 | The README section's presence and example are checked by T2's command. Whether it is clear to a new developer is a person's judgment: no command proves it. The run parks at `task.manual_check` naming AC-2. |

## Risks and unknowns

- AC-2 needs the owner. The unattended run cannot pass it.

## Deviations

## Amendments

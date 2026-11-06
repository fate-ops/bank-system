# New Bank - M320 Objektorientiertes Design

New Bank is a Java 21 exercise that models banking at the counter. A bank employee can open
savings, salary, and promotional youth accounts, record deposits and withdrawals, and read
balances or account statements. Accounts and bookings are stored in memory for the lifetime of
the application. Customer records, authentication, and IBANs in the use cases describe the
business scenario and are outside the current implementation.

## Build and run

Use JDK 21 and set `JAVA_HOME` to the JDK directory. The Maven wrapper downloads Maven and
dependencies on first use.

| Task | macOS / Linux | Windows CMD |
| --- | --- | --- |
| Clean build and tests | `./mvnw clean test` | `mvnw.cmd clean test` |
| Run the application | `./mvnw spring-boot:run` | `mvnw.cmd spring-boot:run` |

The full suite should pass. Spring Boot exposes [the health endpoint](http://localhost:8080/actuator/health).
There are currently no banking REST endpoints or frontend. Domain behavior can be demonstrated
with the JUnit tests.

## Domain and project layout

Amounts are stored as integer **millirappen**: `100_000` equals CHF 1. Deposits produce positive
bookings and withdrawals negative ones. Each booking stores the date in Unix milliseconds, the
amount and a description. The balance is derived from the booking history. A monthly statement
uses the Europe/Zurich calendar and shows the net sum of that month's bookings.

A savings account cannot go below zero. A salary account can reach its configured credit limit,
which is zero or negative. A youth account must retain CHF 10 after any withdrawal; new youth
accounts can still start with a zero balance.

- `src/main/java/ch/bbw`: bank, factory, account types, and booking model.
- `src/test/java`: JUnit tests for the domain, factory, shared bank and test isolation.
- [Architecture and decisions](docs/architecture.md) and [class diagram](docs/bank_class_diagram.png).
- [Counter use cases](docs/use-cases/) and [exercise briefs](docs/exercises/start.md).

## Sprint status

| Sprint | Scope | Status |
| --- | --- | --- |
| 1 | Bank and basic account operations | Implemented |
| 2 | Booking descriptions, derived balances, and statements | Implemented |
| 3 | Account inheritance and withdrawal rules | Implemented |
| 4 | Account factory, type enum and ID generation | Implemented |
| 5 | Lazy shared bank and isolated tests | Implemented |
| 6 onward | Spring Boot banking API and optional frontend | Not implemented |

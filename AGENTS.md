# AGENTS.md — jhand

> Guidance for AI agents (and human contributors) working on **jhand**, a Java poker hand-history analyser.
> Read this file **before** writing or changing any code. Follow the conventions described here so that
> generated code is consistent with the existing codebase.

---

## 1. What this project is

`jhand` (**J**ava **hand** analyser) is a Spring Boot backend that **imports PokerStars hand-history text files**,
**parses** each hand, computes **hero-centric poker statistics**, **evaluates hand strength** with its own
lookup-table evaluator, and **persists** the result so it can be queried through paginated REST APIs.

The typical flow is:

1. A client uploads one or more `.txt` hand-history files (`POST /v1/hands/import/batch`).
2. Each file becomes a **poker session** (one file = one session; `sessionId` = file name without `.txt`).
3. Each file is split into individual hands and parsed line-by-line into `Action`s.
4. For every hand a `HandResult` is built (hero cards, position, VPIP/PFR, invested/collected, net profit,
   made hand rank, board) and stored in PostgreSQL.
5. A `PokerSession` is stored with its type (cash game / tournament) and its result: the **sum of the hands' net
   profit** for a cash game, **prize − buy-in** for a tournament (see "Tournament session result").
6. The client lists/filters stored hands (`GET /v1/hands`) and sessions (`GET /v1/sessions`).

There is currently **no authentication** and a single **hero** (the tracked player) is passed explicitly on import.
Re-importing the same file overwrites its hands and its session (ids are natural keys).

An **equity calculator** (heads-up, exact hand vs exact hand, all streets) lives in the `equity` package and is
exposed through `POST /v1/equity`. Villain **ranges** are not supported yet.

---

## 2. Tech stack & tooling

| Concern            | Choice                                                                 |
|--------------------|-----------------------------------------------------------------------|
| Language           | **Java 25** (`<java.version>25</java.version>`)                        |
| Framework          | **Spring Boot 4.1.0** (`spring-boot-starter-parent`)                   |
| Web                | `spring-boot-starter-webmvc` (Spring MVC, servlet stack — **not** WebFlux) |
| Persistence        | `spring-boot-starter-data-jpa` + **PostgreSQL** (Hibernate)           |
| Boilerplate        | **Lombok** (annotation processor configured in `maven-compiler-plugin`) |
| Logging            | SLF4J via Lombok `@Slf4j`                                              |
| Build              | Maven (`./mvnw`)                                                       |
| Code formatting    | **`spring-javaformat-maven-plugin`** (runs in the `validate` phase)    |
| Tests              | JUnit 5, **AssertJ**, Mockito, **Instancio**, Spring MVC Test (`MockMvc`) |
| CI                 | GitHub Actions (`.github/workflows/pull-request.yml`) runs `./mvnw clean install` on PRs |

### Build & run commands

```bash
# Build + run all tests (formatting is validated automatically in the `validate` phase)
./mvnw clean verify

# Run only the tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=HandResultBuilderTest

# Apply the Spring code format (fixes formatting instead of only validating)
./mvnw spring-javaformat:apply

# Run the application
./mvnw spring-boot:run
```

> **Important:** `spring-javaformat` runs during `validate`. A build **fails** if code is not formatted.
> Always keep new/changed code formatted (run `spring-javaformat:apply` if unsure).

### Infrastructure

- A PostgreSQL instance is required to **run** the app (see `docker-compose.yaml`); the test suite does **not**
  need a database (unit tests + `@WebMvcTest` slices only). Default datasource in `application.yml`:
  `jdbc:postgresql://localhost:5432/JHand`, user/pass `postgres/postgres`.
- `spring.jpa.hibernate.ddl-auto: update` — schema is auto-managed by Hibernate (no migration tool yet).
  `update` **adds** columns but never alters/drops them: changing a column type, renaming an enum constant
  stored as `STRING`, or adding a primitive `boolean` column to a table with data requires manual SQL or a
  re-import.
- File uploads are configured for large multipart payloads (100 MB) both in Spring and Tomcat.
- CORS is open only to the frontend dev server at `http://localhost:5173` (`WebConfiguration`).
- `poker.monte-carlo.iterations` (default `100000`) configures the pre-flop equity Monte Carlo.

---

## 3. Poker / business domain (glossary)

Agents must understand the poker vocabulary to reason about the code correctly.

### Core poker entities (`com.gui.jhand.core.poker`)

- **Card** — an immutable `record Card(Rank rank, Suit suit)`. Parsed from hand-history notation like `As`
  (Ace of spades) via `Card.fromHandHistory(...)`; renders back to the same 2-char notation via `toString()`.
- **Rank** — enum `TWO..ACE` with a display `symbol` (`2..T,J,Q,K,A`) and a numeric `value` (**0..12**, i.e.
  `TWO = 0`, `ACE = 12`; the value is used as an index by the evaluator). Lookup via `Rank.fromSymbol(...)`.
- **Suit** — enum `CLUBS, DIAMONDS, HEARTS, SPADES` with single-char symbols (`c,d,h,s`) and a bit `value`
  (`1, 2, 4, 8`) used by the evaluator's card encoding.
- **PocketCards** — the hero's hole cards (`record PocketCards(List<Card> cards)`). Cards are **sorted by rank
  descending** in the compact constructor, so equality/`toString` are canonical. Built from history or DB
  strings (`fromHandHistory` / `fromDb`).
- **Board** — the community cards (`record Board(List<Card> cards)`). The compact constructor makes a
  defensive, **unmodifiable** copy (`List.copyOf`), so `equals`/`hashCode` are the record defaults. Build with
  `Board.fromString("As Kd 7c")` or `Board.of(card1, card2, ...)`.
- **HandRank** — the made-hand category: `HIGH_CARD, PAIR, TWO_PAIR, THREE_OF_A_KIND, STRAIGHT, FLUSH,
  FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH, ROYAL_FLUSH`. Produced from an evaluator score with
  `HandRank.fromScore(int)`. (`HandRank.fromRawHand(String)` parses the PokerStars summary text; it is
  currently unused in production code.)

### Poker gameplay concepts

- **Hero** — the tracked player. Passed explicitly on import (`heroName`). **All statistics are computed only
  for the hero**; every hero-specific `ActionHandler` filters actions with
  `state.getHeroName().equals(action.getPlayerName())` (always in this order).
- **Street** (`ActionStreet`) — the phases of a hand: `PRE_FLOP, FLOP, TURN, RIVER, SHOWDOWN, SUMMARY`.
- **Position** (`Position` in `com.gui.jhand.hand`) — the hero's seat relative to the button:
  `BTN, SB, BB, CO, MP, UTG`. Blinds/button positions are read explicitly from the summary line; other
  positions are inferred from the distance to the button (`PositionUtils.resolvePosition`). This inference is
  **only accurate for 6-max**.
- **Session** (`PokerSession` in `com.gui.jhand.pokersession`) — one imported file. `SessionType`
  (`CASH_GAME`, `TOURNAMENT`) is inferred from the file name (cash names contain stakes like `$0.01-$0.02`,
  tournament names contain `T<number>`). A name matching neither makes the import fail with
  `IllegalArgumentException` (→ HTTP 400).
- **Cash game vs. Tournament** — both are PokerStars hand-history formats that this app parses. The header
  line distinguishes them (e.g. a tournament header contains `Tournament #...` and blind *Levels*, while a
  cash game header contains stake info such as `$0.01/$0.02`). See the sample files under
  `src/test/resources/GuiRodri2013/`. The parser handles both; downstream logic is format-agnostic and works
  off parsed `Action`s. ⚠️ Amounts are **dollars** in cash games and **tournament chips** in tournaments, and
  are stored in the same columns — never aggregate money across sessions of different types.
- **Tournament session result** — a tournament's hands are in chips, but its session `netProfit` is
  **prize − buy-in**, in the buy-in currency. Both come from the history, as `Action`s:
  - The buy-in comes from the header (`Tournament #…, $0.45+$0.05 USD` / play money `21250+3750` / bounty
    `10625+10625+3750`) as `TOURNAMENT_BUY_IN`, the sum of all its parts (prize pool + bounty + rake). Absent
    means freeroll (0).
  - The finish line comes as `TOURNAMENT_FINISH` (`X wins the tournament [and receives $5.67] - congratulations!`,
    `X finished the tournament [in 2nd place] [and received $3.70.]`), with the placement and the prize.
  - `TournamentSessionCollector` (in `hand`) folds them per file into a `TournamentResult`, from which
    `TournamentPokerSession.of(...)` derives the result.
  - `realMoney` = the buy-in or the prize has `$`; play-money results are shown without `$`.
  - No hero finish line (a tournament still running) → `netProfit` and `prize` are `null`.
  - Re-entries and bounties won are not accounted for yet.
- **Ante / Small Blind / Big Blind** — forced bets posted before the deal; tracked as hero investment.
  The big blind **of the level** is stored as `blindValue`; it is read from the hand header (`Level II (30/60)` /
  `($0.01/$0.02 USD)`, `BIG_BLIND_LEVEL` action → `BigBlindLevelHandler`), **not** from the posted BB, which can
  be a partial all-in.
- **Equity** — the share of the pot a hand wins on average against a known opponent hand, given the known
  board (`EquityResult`: hero/villain equity, tie probability, scenario counts).

### Hero statistics (computed per hand into `HandResult`)

- **VPIP** (*Voluntarily Put money In Pot*) — `true` if the hero voluntarily called or raised pre-flop
  (blinds/antes do **not** count). Set by `InvestmentHandler` / `RaiseHandler`.
- **PFR** (*Pre-Flop Raise*) — `true` if the hero raised pre-flop. Set by `RaiseHandler`.
- **totalInvested** — sum of chips the hero put in the pot (raises count only the incremental cost over the
  current street investment; uncalled bets returned to the hero are subtracted via `UncalledBetHandler`).
- **totalCollected** — chips the hero won from pots (`CollectedHandler`).
- **netProfit** — `totalCollected - totalInvested` (computed in `HandState.getNetProfit()`).
- **netProfitInBb** — `netProfit / blindValue` (scale 2, `HALF_UP`). It is the unit comparable across cash games
  and tournaments. A `null`, zero or negative `blindValue` is impossible in a real poker game, so
  `HandState.getNetProfitInBb()` throws `BusinessException` (→ HTTP 422).
- **totalPot** — the `Total pot` of the summary (before rake, side pots included), `TotalPotHandler`.
- **startingStack** — the hero's stack at the start of the hand, read from the seat line
  (`Seat 1: Hero (1500 in chips)` / `($2.24 in chips)`, `SEAT_INFO` → `SeatInfoHandler`). `null` for hands
  imported before this column existed (re-import to fill it).
- **handRank** — computed by `HandRankResolver` with the **hero's pocket cards + the final board**, using the
  `evaluator`. Pre-flop-only hands are classified as `PAIR` or `HIGH_CARD`. It is **always set** — including
  when the hero folded (it then means "what the hero would have made").
- **board** — community cards (may be `null` if the hand ended pre-flop).

---

## 4. Architecture & package layout

Root package: `com.gui.jhand`. Code is **organised by domain/feature**, not by technical layer.

```
com.gui.jhand
├── JhandApplication            # Spring Boot entry point
│
├── core                        # cross-cutting, domain-agnostic building blocks
│   ├── poker                   # pure poker value objects (Card, Rank, Suit, Board, PocketCards, HandRank)
│   ├── utils                   # ChipsUtils (money/BigDecimal helpers)
│   ├── exception               # BusinessException (violated poker/business rule), NotFoundException
│   ├── web                     # GlobalExceptionHandler (@RestControllerAdvice, ProblemDetail)
│   └── configuration           # WebConfiguration (CORS, MVC)
│
├── action                      # the "Action" domain: a single parsed event in a hand
│   ├── Action, ActionType, ActionStreet
│   └── handler                 # Strategy handlers that fold Actions into HandState
│       ├── ActionHandler (interface) + ActionHandlerRegistry
│       └── *Handler (one per concern: Header, Time, BigBlindLevel, PostAnte, Investment, Raise,
│                       DealtToHero, Board, Collected, UncalledBet, TotalPot, SeatInfo, SeatSummary)
│
├── parser                      # turns raw hand-history text into a List<Action>
│   ├── PokerStarsParser, ParserContext
│   ├── ParsingRule, ActionMapper (functional), PokerStarsParsingRuleFactory
│
├── evaluator                   # 5/6/7-card hand evaluator (Cactus Kev + Senzee perfect hash)
│   ├── HandEvaluator           # public entry point: eval(Card[]) -> HandEvaluationResult
│   ├── HandEvaluationResult    # record(score, handRank); lower score = stronger hand
│   └── HandEvaluatorTables, HandEvaluatorUtils, SenzeeHash  # package-private internals
│
├── equity                      # heads-up equity calculator
│   ├── web                     # EquityRestService (POST /v1/equity), EquityRequest, EquityResponse
│   ├── EquityCalculator        # @Service; picks a strategy by board length
│   ├── EquityCalculationStrategy (sealed) + PreFlop/Flop/Turn/River*EquityCalculation
│   ├── ShowdownEvaluator       # evaluates one board+runout scenario (shared by all strategies)
│   ├── Deck, EquityCounter, EquityEvaluationWinner, EquityResult
│
├── hand                        # the "HandResult" aggregate (the analysed hand)
│   ├── HandResult              # JPA @Entity (persisted aggregate)
│   ├── HandState               # mutable accumulator used while building a HandResult
│   ├── HandResultBuilder       # orchestrates handlers to build a HandResult
│   ├── HandRankResolver        # hero cards + board -> HandRank (uses evaluator)
│   ├── SessionCollector        # folds a file's hands into its PokerSession; one impl per SessionType:
│   │                           #   CashSessionCollector, TournamentSessionCollector
│   ├── HandImportService       # import use case (write side)
│   ├── HandResultQuery         # read use case (read side, returns Page<HandResult>)
│   ├── HandResultFilter        # query criteria record (bound from request params)
│   ├── HandResultRepository    # Spring Data JPA repository
│   ├── HandResultSpecification # dynamic query predicates
│   ├── Position, PositionUtils
│   ├── BoardConverter, PocketCardsConverter  # JPA AttributeConverters
│   └── web                     # REST layer for this domain
│       ├── HandImportRestService, HandResultRestService  # @RestController(s)
│       └── HandResultSummaryResponse (response record)
│
└── pokersession                # the "PokerSession" aggregate (one imported file)
    ├── PokerSession            # abstract JPA @Entity (sessionId, type, handCount, startTime, endTime,
    │                           #   netProfit); SINGLE_TABLE inheritance, discriminator = the `type` column.
    │                           #   startTime/endTime = time of the first/last hand of the file
    ├── CashPokerSession        # netProfit = sum of the hands (dollars)
    ├── TournamentPokerSession  # netProfit = prize - buyIn; + buyIn, prize, finishPosition, realMoney
    ├── SessionType             # CASH_GAME / TOURNAMENT, inferred from the file name
    ├── TournamentResult        # record: buy-in, prize, placement, realMoney, finished; netProfit()
    ├── PokerSessionService     # write use case (create/overwrite)
    ├── PokerSessionQuery       # read use case (Page<PokerSession>; findById → NotFoundException)
    ├── PokerSessionRepository
    ├── PokerSessionUtils       # normalizeSessionId (strip ".txt")
    └── web
        ├── PokerSessionRestService  # GET /v1/sessions, GET /v1/sessions/{sessionId}
        └── PokerSessionResponse     # response record
```

### Dependency rules (who knows whom)

- **`core.poker` is the innermost domain** — it depends on nothing else in the project and must stay pure
  (no Spring, no JPA, no framework imports). Everything may depend on it.
- **`evaluator` depends only on `core.poker`.** Plain Java, static API, no Spring.
- **`equity` depends on `core.poker` and `evaluator`.** It must not know `hand`, `action` or `parser`.
- **`parser` depends only on `action` and `core`.** It produces `Action`s and never touches `HandState`,
  JPA, or Spring. It is a plain-Java engine (`PokerStarsParser` is instantiated with `new`, not injected).
- **`action.handler` depends on `action`, `hand` (HandState), and `core`.** Handlers mutate `HandState`.
- **`hand` depends on `action`, `parser`, `evaluator`, `pokersession` (service, `SessionType`, `normalizeSessionId`, and the session factories
  `CashPokerSession.of` / `TournamentPokerSession.of` + `TournamentResult`), and
  `core`.** It is the application/persistence layer for hands.
- **`pokersession` depends on nothing but `core`.** It must not know `hand`.
- **`*.web` (controllers) depend only on their domain's services/queries and domain objects**, never directly
  on repositories, the parser, or handlers.
- Direction of flow: `web → service (Import/Query) → builder/handlers/parser → domain (core.poker)`.

**Normalize at the boundary.** Inputs are normalized once, at the entry of the use case, and passed down
already clean. Example: `HandImportService` turns the file name into a `sessionId` with
`PokerSessionUtils.normalizeSessionId`, derives the `SessionType` from it once, and passes both to
`HandResultBuilder` and `SessionCollector`; none of those re-normalize or re-derive.

**Polymorphism over type checks.** Behaviour that differs between cash games and tournaments lives in a
per-type class (`CashPokerSession`/`TournamentPokerSession`, `CashSessionCollector`/`TournamentSessionCollector`).
The only place that branches on `SessionType` is the exhaustive `switch` in `SessionCollector.of`; do not add
`if (type == TOURNAMENT)` checks or type-specific nullable parameters to shared methods.

**Known dependency debts** (do not copy these patterns; fix them when touching the area):

- `hand ↔ action.handler` cycle: handlers mutate `hand.HandState` while `hand.HandResultBuilder` uses the
  handler registry.
- `HandRank.fromScore` (in `core.poker`) encodes the Cactus Kev score ranges, which are an `evaluator`
  implementation detail.
- `ChipsUtils` (in `core`) uses Spring's `StringUtils`.

### CQRS-flavoured separation

Write and read use cases are separated:

- **Write side:** `HandImportRestService` → `HandImportService` (`@Transactional`) → `HandResultBuilder` (+ the
  file's `SessionCollector`, fed every hand) → `HandResultRepository.saveAll(...)` →
  `PokerSessionService.save(collector.toSession(sessionId, handCount, startTime))`.
- **Read side (hands):** `HandResultRestService` → `HandResultQuery` (`@Transactional(readOnly = true)`) →
  `HandResultRepository.findAll(spec, pageable)` (returns `Page<HandResult>`) → the controller maps with
  `HandResultSummaryResponse::of`.
- **Read side (sessions):** `PokerSessionRestService` → `PokerSessionQuery.findAllSessions(type, pageable)`
  (returns `Page<PokerSession>`) → the controller maps with `PokerSessionResponse::of`.

### REST API

| Method & path | Description | Notes |
|---|---|---|
| `POST /v1/hands/import/batch` | multipart `files[]` + `heroName` | `204 No Content`; one transaction per file |
| `GET /v1/hands` | paginated hands | filters: `handId`, `sessionId`, `sessionType` (`CASH_GAME`/`TOURNAMENT`; invalid → 400), `heroCards` (space-separated cards), `handRank`; default sort `time,desc`, size 20 |
| `GET /v1/sessions` | paginated sessions | optional filter `type` (`CASH_GAME`/`TOURNAMENT`; invalid → 400); returns `{sessionId, type, handCount, startTime, endTime, netProfit, buyIn, prize, finishPosition, realMoney}` (the last four are tournament only; `netProfit` is `null` for an unfinished tournament; `endTime` = start of the last hand, `null` for sessions imported before it existed); default sort `startTime,desc`, size 20 |
| `GET /v1/sessions/{sessionId}` | one session | same body as an item of `GET /v1/sessions`; unknown id → 404 (`ProblemDetail`). The id contains spaces/`$`/`'`: the client must URL-encode it |
| `POST /v1/equity` | heads-up equity | JSON `{heroCards, villainCards, board}` (space-separated cards; `board` empty/absent = pre-flop, else 3/4/5 cards). Returns `{heroEquity, villainEquity, tieProbability, totalScenarios}`. Invalid/duplicated cards → 400. Pre-flop is a Monte Carlo estimate |

Dates are returned as UTC `Instant`s (ISO-8601 with `Z`); time-zone conversion is the client's job.

### Error handling

`GlobalExceptionHandler` extends `ResponseEntityExceptionHandler` and returns RFC-7807 `ProblemDetail`:

- Standard Spring MVC exceptions (missing parameter/part, type mismatch, upload too large, ...) → their proper
  4xx status (handled by the superclass).
- `IllegalArgumentException` → **400**, with the exception message as `detail` (logged at `WARN`). Throw it
  for invalid **client input** only (bad filter value, unparseable card, unknown session file name).
- `BusinessException` (`core.exception`) → **422 Unprocessable Content**, with the message as `detail` (logged at
  `WARN`). Throw it when the input is well-formed but violates a poker/business invariant (e.g. a hand whose big
  blind is missing or zero). Its message must be safe to expose.
- `ResourceNotFoundException` (`core.exception`) → **404 Not Found**, with the message as `detail` (logged at `WARN`).
  Throw it from a `*Query` when a resource looked up by id does not exist. Its message must be safe to expose.
- Any other `Exception` → **500** with a **generic** `detail` (the message is never exposed) and the stack
  trace logged at `ERROR`.

---

## 5. Technical building blocks (what each stereotype means here)

Naming conventions in this codebase differ slightly from the Spring defaults — follow them:

- **`*RestService`** = the REST controller. Annotated with `@RestController` + `@RequestMapping("/v1/...")`.
  (This project calls controllers "RestService"; do **not** rename to `*Controller`.) Controllers are thin:
  they only adapt HTTP ↔ use case and return `ResponseEntity` / `Page`.
- **`*Service`** = an application use case / business service (`@Service`). Examples: `HandImportService`
  (write), `PokerSessionService` (write), `HandResultBuilder` (build orchestration). Transaction boundaries
  live here.
- **`*Query`** = a read-only use case (`@Service` + `@Transactional(readOnly = true)`). Prefer returning
  domain objects (`Page<PokerSession>`) and letting the web layer map to response records; see the
  dependency debts above for the legacy `HandResultQuery`.
- **`*Repository`** = Spring Data JPA repository interface (`extends JpaRepository<...>,
  JpaSpecificationExecutor<...>`). Repositories are **package-private** and only used by services/queries in
  the same package.
- **`*Specification`** = dynamic JPA `Specification` builders (Criteria API) for filtering
  (`HandResultSpecification.withFilters(...)`). Prefer specifications over ad-hoc query methods for optional
  filters.
- **`*Handler` (`ActionHandler`)** = a **Strategy** that consumes one or more `ActionType`s and folds them
  into the mutable `HandState`. Each implementation is a `@Component` (the interface itself is **not**
  annotated), declares its supported types via `getSupportedTypes()`, and is auto-registered into the
  `ActionHandlerRegistry` (an `EnumMap` populated from the injected `List<ActionHandler>`). **To support a new
  event: add an `ActionType`, add a parsing rule, and add a handler — no central switch to edit.**
- **`*EquityCalculation` (`EquityCalculationStrategy`)** = a **Strategy** per board length (0, 3, 4, 5),
  auto-registered into `EquityCalculator` by `getSupportedBoardLength()`. The interface is `sealed`: a new
  strategy must be added to its `permits` clause. Scenario evaluation is delegated to `ShowdownEvaluator`;
  strategies must not call back into `EquityCalculator`.
- **`ParsingRule` + `ActionMapper`** = the parser engine. A `ParsingRule` pairs a regex `Pattern` with a
  functional `ActionMapper` (`(Matcher, ParserContext, String) -> List<Action>`). Rules are declared in
  `PokerStarsParsingRuleFactory`; `PokerStarsParser` applies them line-by-line, **first match wins**.
- **`*Converter` (`AttributeConverter`)** = JPA converters (`@Converter(autoApply = true)`) that persist rich
  value objects as strings (`Board`, `PocketCards`). This keeps the DB schema simple and the domain rich.
- **`HandState`** = a **mutable** working accumulator (`@Data`) used only during building; it is **not**
  persisted. `heroName` and `sessionId` are `final` (set in the constructor, already normalized).
  `HandResult` is the **immutable** persisted aggregate (built via Lombok `@Builder`).
- **DTOs** = Java `record`s in the `web` package with a static factory `of(entity)` for mapping
  (`HandResultSummaryResponse.of(...)`, `PokerSessionResponse.of(...)`). They must be **null-safe** (use
  `Objects.toString(x, null)` / ternaries). Request filters are bound via `@ModelAttribute`.

---

## 6. Coding conventions & idioms

Follow the patterns already present. Key rules:

### General Java

- **Prefer immutability.** Use `record`s for value objects and DTOs (`Card`, `Board`, `PocketCards`,
  `HandResultFilter`, `HandResultSummaryResponse`). Records must not expose mutable state: use `List` +
  `List.copyOf` in the compact constructor, **never arrays** as record components (arrays break
  `equals`/`hashCode` and leak mutability). Keep persisted aggregates effectively immutable (built via
  `@Builder`, only getters exposed).
- **Static factory methods** are the standard construction idiom: `fromHandHistory`, `fromString`,
  `fromSymbol`, `fromDb`, `fromScore`, `fromFileName`, `of(...)`. Name factories by their source/intent.
- **No nulls leaking as control flow** — use `Optional` where a value may be absent
  (`Position.getExplicitPosition` returns `Optional<Position>`). Guard against `null` when mapping entities to
  DTOs.
- **Fail fast on bad input** with `IllegalArgumentException` (see `Rank.fromSymbol`, `HandEvaluator.eval`).
  Remember it becomes an HTTP 400.
- Use `static` imports the way the codebase does (e.g. `import static ...ActionType.*`,
  `import static org.springframework.util.StringUtils.hasText`).
- **Money is always `BigDecimal`.** Never use `double`/`float` for chips. Create amounts through
  `ChipsUtils.of(...)` (scale 2, `HALF_UP`). Entity money columns use `@Column(precision = 12, scale = 2)`.
  (Equity/probabilities are not money and use `double`.)
- Enums are stored as strings in the DB (`@Enumerated(EnumType.STRING)`). Renaming a constant is a data
  migration (see Infrastructure).
- Hot paths (`evaluator`, `equity`) may use arrays and primitive loops for performance; keep that confined
  to those packages.

### Lombok

- Use Lombok consistently: `@Getter`, `@Builder`, `@AllArgsConstructor(staticName = "of")`, `@Data`
  (only for mutable helpers like `HandState`), `@NoArgsConstructor(access = PRIVATE)` for utility/JPA classes,
  `@EqualsAndHashCode` (value-like classes only — not on Spring beans such as handlers), `@Slf4j` for logging.
- Utility classes have a **private no-args constructor** (`@NoArgsConstructor(access = PRIVATE)`) and only
  `static` methods (`PositionUtils`, `PokerSessionUtils`, `HandEvaluator`, `ShowdownEvaluator`, `Deck`,
  `PokerStarsParsingRuleFactory`).

### Spring

- Inject dependencies via **constructor injection** using Lombok `@AllArgsConstructor` on `final` fields.
  Do **not** use `@Autowired` on fields.
- Keep **visibility tight**: classes that are internal to a package (repositories, builders, specifications,
  converters, read controllers (`HandResultRestService`, `PokerSessionRestService`), `ParsingRule`, `ParserContext`, `GlobalExceptionHandler`, evaluator/equity
  internals) are **package-private**, and so are their helper methods. Only expose classes that genuinely
  need to be public.
- Transactions: **always** `org.springframework.transaction.annotation.Transactional` (never
  `jakarta.transaction.Transactional`). Writes use `@Transactional(rollbackFor = Exception.class)`, queries
  `@Transactional(readOnly = true)`.
- REST endpoints are versioned under `/v1/...`.

### Formatting

- The project uses **Spring's Java format** (tabs for indentation, specific import ordering). Do not fight it.
  Run `./mvnw spring-javaformat:apply` before committing.

---

## 7. Testing conventions

Tests live under `src/test/java` mirroring the main package structure. **Match these conventions exactly.**

### Libraries (use these, and only these)

- **JUnit 5** (`org.junit.jupiter`) — `@Test`, `@BeforeEach`, `@ParameterizedTest` + `@ValueSource`.
- **AssertJ** for **all** assertions: `assertThat(...)`, `assertThatCode(...).doesNotThrowAnyException()`,
  `assertThatThrownBy(...)`, `within(...)` for floating-point equity, etc.
  - ❌ **Never** use JUnit's `Assertions.assert*`, Hamcrest, or plain `assert`. Assertions must come from
    **AssertJ** only. (E.g. do not `assertEquals(...)`; use `assertThat(x).isEqualTo(...)`.)
- **Mockito** for mocking: `@Mock`, `@MockitoBean` (Spring context mocks), `when/verify/inOrder/argThat`.
  - Note: `@MockitoBean` (not the removed `@MockBean`) is used in Spring MVC slice tests.
- **Instancio** for test data generation: `Instancio.of(Type.class).create()`, `.set(field(...), value)`.
- **Spring MVC Test** for controllers: `@WebMvcTest(SomeRestService.class)` + `MockMvc` (+ `jsonPath` to check
  the body).

### Base class & helpers

- Extend **`TestBase`** for tests that need Mockito field initialisation. It:
  - runs `MockitoAnnotations.openMocks(this)` in `@BeforeEach`,
  - calls an abstract `init()` you implement to set up the subject under test,
  - provides `valid(Class<T>)` = `Instancio.of(type).create()` for quick valid instances.
- Pure unit tests that construct the subject with `new` and don't need the Mockito lifecycle may define their
  own `@BeforeEach setUp()` without extending `TestBase` (see `InvestmentHandlerTest`).
- Monte Carlo tests construct `PreFlopEquityCalculation` with fewer iterations (e.g. `10000`) and assert with
  `within(...)`; exact strategies (flop/turn/river) assert exact `EquityResult`s.

### Test data: TemplateLoaders

- Reusable fixtures live in **`*TemplateLoader`** classes with **static factory methods** returning
  domain-meaningful, named instances, e.g.:
  - `ActionTemplateLoader.validGuiCall()`, `validGuiPostBigBlind()`, `validGuiSeatResultAtBtn()`, ...
  - `HandStateTemplateLoader.validGuiPreFlop()`, ...
  - `CardTemplateLoader` (card constants),
  - `HandTemplateLoader.heroAtBtn()`, `heroAtSB()`, `validWonWithShowdown()`, `validWithSidePot()`, ...
    (these return **full raw PokerStars hand-history strings** as text blocks, used to test the parser and
    builder end-to-end).
- Prefer adding a new named factory to the appropriate `*TemplateLoader` over inlining large fixtures in a
  test.

### Naming & style

- Test method names use **`snake_case` starting with `should_`**:
  `should_save_all_hand_results_from_file`, `should_sort_cards_by_rank_descending`,
  `should_handle_with_vpip`.
- Follow the **Arrange / Act / Assert** structure seen in existing tests. Use `InOrder` to assert interaction
  ordering when relevant (`HandImportServiceTest`).
- Controller tests use `@WebMvcTest` with `MockMvc.perform(...)` and `@MockitoBean` collaborators, asserting
  `status()` (including 400/500 error paths through `GlobalExceptionHandler`) and verifying the delegated call.

### Real hand-history samples

- End-to-end/regression samples live in `src/test/resources/GuiRodri2013/` (real exported PokerStars hand
  histories: 3-max, 4-max and 6-max cash games and tournaments, including sitting-out players). Use them when
  adding parser coverage.

---

## 8. How to add a new feature (recipes)

### Add support for a new hand-history event / new statistic

1. Add a new value to **`ActionType`** (and `ActionStreet` if a new street/phase is needed).
2. Add a **parsing rule** in `PokerStarsParsingRuleFactory` (a regex `Pattern` + an `ActionMapper` lambda
   producing one or more `Action`s). Remember: rules are evaluated top-to-bottom, **first match wins** — order
   matters (put more specific patterns before more general ones).
3. If the event affects hero stats, add fields to **`HandState`** (with mutation helpers) and, if it must be
   persisted, to **`HandResult`** (+ builder wiring in `HandResultBuilder`). New boolean columns on an
   existing table: use `Boolean` or a column default (see Infrastructure).
4. Create a new **`ActionHandler` `@Component`** returning the new `ActionType`(s) from `getSupportedTypes()`
   and mutating `HandState` in `handle(...)`. It auto-registers — no registry edits needed. Filter by
   `heroName` when the stat is hero-specific.
5. Add unit tests: a `*HandlerTest`, parser coverage in `PokerStarsParserTest`, and (if applicable)
   `HandResultBuilderTest` using a `HandTemplateLoader` fixture.

### Add a new query filter

1. Add the field to the **`HandResultFilter`** record.
2. Add the predicate in **`HandResultSpecification.withFilters(...)`** (guard optional values with `hasText` /
   `!= null`). Typed fields (enums) are bound by Spring; an invalid value becomes a 400.
3. Cover it in `HandResultQueryTest` / the specification test.

### Add a new endpoint

1. Add the method to the appropriate **`*RestService`** under `/v1/...`, delegating to a `*Service`/`*Query`.
2. Use `record` DTOs with null-safe `of(...)` factories for requests/responses, mapped **in the web layer**.
3. Add a `@WebMvcTest` slice test (happy path + at least one error path).

### Add a session-level aggregate

1. Collect it on import in the `SessionCollector` of the session type(s) it applies to, from the built
   `HandResult`s (like `CashSessionCollector`) or from the parsed `Action`s (like `TournamentSessionCollector`),
   and hand it to the session factory in `toSession(...)`; or compute it on read in `PokerSessionQuery`.
2. Add the column to `PokerSession` if it applies to every type, or to the subclass if it is type-specific; add
   the field to `PokerSessionResponse` (read it with `instanceof` when it is type-specific).
3. Remember that cash sessions are in dollars, tournament sessions in the buy-in currency, and tournament hands
   in chips.
4. A new `SessionType` needs a `PokerSession` subclass (`@DiscriminatorValue`) and a `SessionCollector`; the
   `switch` in `SessionCollector.of` will not compile until it is handled.

---

## 9. Do & Don't (quick checklist for agents)

**Do**
- Keep `core.poker` framework-free and pure; keep `evaluator` and `equity` free of `hand`/`action`/`parser`.
- Use `record`s (with `List`, not arrays) + static factories + constructor injection + Lombok as the
  surrounding code does.
- Use `BigDecimal` via `ChipsUtils` for money; store enums as `STRING`.
- Keep classes package-private unless they must be public.
- Normalize inputs once at the use-case boundary.
- Use **AssertJ** for every assertion and **Instancio**/`*TemplateLoader` for fixtures.
- Name tests `should_...`; controllers `*RestService`; run `spring-javaformat:apply`.

**Don't**
- ❌ Introduce assertion libraries other than AssertJ (no JUnit `assertEquals`, no Hamcrest, no bare `assert`).
- ❌ Add a dependency that isn't already in `pom.xml` without a clear reason.
- ❌ Put a central `switch`/`if` over `ActionType` — extend via a new `ActionHandler` + parsing rule instead.
- ❌ Let controllers touch repositories/parser/handlers directly — go through a `*Service`/`*Query`.
- ❌ Make application services/queries depend on `*.web` DTOs (new code); map in the controller.
- ❌ Use `double`/`float` for chip amounts, or aggregate cash and tournament amounts together.
- ❌ Leak framework/JPA concerns into `core.poker`.
- ❌ Use `jakarta.transaction.Transactional`; use Spring's.
- ❌ Expose exception messages of unexpected errors in HTTP responses.
- ❌ Break Spring Java formatting (build fails in the `validate` phase).

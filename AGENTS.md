# AGENTS.md — jhand

> Guidance for AI agents (and human contributors) working on **jhand**, a Java poker hand-history analyser.
> Read this file **before** writing or changing any code. Follow the conventions described here so that
> generated code is consistent with the existing codebase.

---

## 1. What this project is

`jhand` (**J**ava **hand** analyser) is a Spring Boot backend that **imports PokerStars hand-history text files**,
**parses** each hand, computes **hero-centric poker statistics**, and **persists** the result so it can be
queried through a paginated REST API.

The typical flow is:

1. A client uploads one or more `.txt` hand-history files (`POST /v1/hands/import/batch`).
2. Each file is split into individual hands and parsed line-by-line.
3. For every hand a `HandResult` is built (hero cards, position, VPIP/PFR, invested/collected, net profit,
   made hand rank, board) and stored in PostgreSQL.
4. The client lists/filters stored hands (`GET /v1/hands`).

There is currently **no authentication** and a single **hero** (the tracked player) is passed explicitly on import.

---

## 2. Tech stack & tooling

| Concern            | Choice                                                                 |
|--------------------|-----------------------------------------------------------------------|
| Language           | **Java 25** (`<java.version>25</java.version>`)                        |
| Framework          | **Spring Boot 4.1.0** (`spring-boot-starter-parent`)                   |
| Web                | `spring-boot-starter-webmvc` (Spring MVC, servlet stack — **not** WebFlux) |
| Persistence        | `spring-boot-starter-data-jpa` + **PostgreSQL** (Hibernate)           |
| Boilerplate        | **Lombok** (annotation processor configured in `maven-compiler-plugin`) |
| Build              | Maven (`./mvnw`)                                                       |
| Code formatting    | **`spring-javaformat-maven-plugin`** (runs in the `validate` phase)    |
| Tests              | JUnit 5, **AssertJ**, Mockito, **Instancio**, Spring MVC Test (`MockMvc`) |

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

- A PostgreSQL instance is required (see `docker-compose.yaml`). Default datasource in `application.yml`:
  `jdbc:postgresql://localhost:5432/JHand`, user/pass `postgres/postgres`.
- `spring.jpa.hibernate.ddl-auto: update` — schema is auto-managed by Hibernate (no migration tool yet).
- File uploads are configured for large multipart payloads (100 MB) both in Spring and Tomcat.
- CORS is open only to the frontend dev server at `http://localhost:5173` (`WebConfiguration`).

---

## 3. Poker / business domain (glossary)

Agents must understand the poker vocabulary to reason about the code correctly.

### Core poker entities (`com.gui.jhand.core.poker`)

- **Card** — an immutable `record Card(Rank rank, Suit suit)`. Parsed from hand-history notation like `As`
  (Ace of spades) via `Card.fromHandHistory(...)`; renders back to the same 2-char notation via `toString()`.
- **Rank** — enum `TWO..ACE` with a display `symbol` (`2..T,J,Q,K,A`) and a numeric `value` (2..14).
  Lookup via `Rank.fromSymbol(...)`.
- **Suit** — enum of the four suits with single-char symbols (`s,h,d,c`).
- **PocketCards** — the hero's hole cards (a `record` wrapping a `List<Card>`). Cards are **sorted by rank
  descending** in the compact constructor, so equality/`toString` are canonical. Built from history or DB
  strings (`fromHandHistory` / `fromDb`).
- **Board** — the community cards (`record Board(Card... cards)`), parsed from a space-separated string.
- **HandRank** — the made-hand category at showdown: `HIGH_CARD, PAIR, TWO_PAIR, THREE_OF_A_KIND, STRAIGHT,
  FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH, ROYAL_FLUSH`. Derived from the summary text with
  `HandRank.fromRawHand(...)`.

### Poker gameplay concepts

- **Hero** — the tracked player. Passed explicitly on import (`heroName`). **All statistics are computed only
  for the hero**; every `ActionHandler` filters actions by `state.getHeroName().equals(action.getPlayerName())`.
- **Street** (`ActionStreet`) — the betting rounds of a hand: `PRE_FLOP, FLOP, TURN, RIVER, SUMMARY`.
- **Position** (`Position` in `com.gui.jhand.hand`) — the hero's seat relative to the button:
  `BTN, SB, BB, CO, MP, UTG`. Blinds/button positions are read explicitly from the summary line; other
  positions are inferred from the distance to the button (`PositionUtils.resolvePosition`).
- **Cash game vs. Tournament** — both are PokerStars hand-history formats that this app parses. The header
  line distinguishes them (e.g. a tournament header contains `Tournament #...` and blind *Levels*, while a
  cash game header contains stake info such as `$0.01-$0.02`). See the sample files under
  `src/test/resources/GuiRodri2013/`. The parser handles both; downstream logic is format-agnostic and works
  off parsed `Action`s.
- **Ante / Small Blind / Big Blind** — forced bets posted before the deal; tracked as hero investment.
  The big blind amount is stored as `blindValue`.

### Hero statistics (computed per hand into `HandResult`)

- **VPIP** (*Voluntarily Put money In Pot*) — `true` if the hero voluntarily called or raised pre-flop
  (blinds/antes do **not** count). Set by `InvestmentHandler` / `RaiseHandler`.
- **PFR** (*Pre-Flop Raise*) — `true` if the hero raised pre-flop. Set by `RaiseHandler`.
- **totalInvested** — sum of chips the hero put in the pot (raises count only the incremental cost over the
  current street investment; uncalled bets returned to the hero are subtracted via `UncalledBetHandler`).
- **totalCollected** — chips the hero won from pots (`CollectedHandler`).
- **netProfit** — `totalCollected - totalInvested` (computed in `HandState.getNetProfit()`).
- **handRank** — the hero's made hand at showdown (may be `null` if the hand never reached showdown).
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
│   ├── web                     # GlobalExceptionHandler (@RestControllerAdvice)
│   └── configuration           # WebConfiguration (CORS, MVC)
│
├── action                      # the "Action" domain: a single parsed event in a hand
│   ├── Action, ActionType, ActionStreet
│   └── handler                 # Strategy handlers that fold Actions into HandState
│       ├── ActionHandler (interface) + ActionHandlerRegistry
│       └── *Handler (one per concern: Header, Time, PostAnte, Investment, Raise,
│                       DealtToHero, Board, Collected, UncalledBet, SeatSummary, HandRank)
│
├── parser                      # turns raw hand-history text into a List<Action>
│   ├── PokerStarsParser, ParserContext
│   ├── ParsingRule, ActionMapper (functional), PokerStarsParsingRuleFactory
│
└── hand                        # the "HandResult" aggregate (the analysed hand)
    ├── HandResult              # JPA @Entity (persisted aggregate)
    ├── HandState               # mutable accumulator used while building a HandResult
    ├── HandResultBuilder       # orchestrates handlers to build a HandResult
    ├── HandImportService       # import use case (write side)
    ├── HandResultQuery         # read use case (read side)
    ├── HandResultRepository    # Spring Data JPA repository
    ├── HandResultSpecification # dynamic query predicates
    ├── Position, PositionUtils
    ├── BoardConverter, PocketCardsConverter  # JPA AttributeConverters
    └── web                     # REST layer for this domain
        ├── HandImportRestService, HandResultRestService  # @RestController(s)
        ├── HandResultFilter (request record)
        └── HandResultSummaryResponse (response record)
```

### Dependency rules (who knows whom)

- **`core.poker` is the innermost domain** — it depends on nothing else in the project and must stay pure
  (no Spring, no JPA, no framework imports). Everything may depend on it.
- **`parser` depends only on `action` and `core`.** It produces `Action`s and never touches `HandState`,
  JPA, or Spring. It is a plain-Java engine (`PokerStarsParser` is instantiated with `new`, not injected).
- **`action.handler` depends on `action`, `hand` (HandState), and `core`.** Handlers mutate `HandState`.
- **`hand` depends on `action`, `parser`, and `core`.** It is the application/persistence layer.
- **`hand.web` (controllers) depend only on `hand` services**, never directly on repositories, the parser,
  or handlers.
- Direction of flow: `web → service (Import/Query) → builder/handlers/parser → domain (core.poker)`.

### CQRS-flavoured separation

Write and read use cases are separated:

- **Write side:** `HandImportRestService` → `HandImportService` (`@Transactional`) → `HandResultBuilder` →
  `HandResultRepository.saveAll(...)`.
- **Read side:** `HandResultRestService` → `HandResultQuery` (`@Transactional(readOnly = true)`) →
  `HandResultRepository.findAll(spec, pageable)` → mapped to `HandResultSummaryResponse`.

---

## 5. Technical building blocks (what each stereotype means here)

Naming conventions in this codebase differ slightly from the Spring defaults — follow them:

- **`*RestService`** = the REST controller. Annotated with `@RestController` + `@RequestMapping("/v1/...")`.
  (This project calls controllers "RestService"; do **not** rename to `*Controller`.) Controllers are thin:
  they only adapt HTTP ↔ use case and return `ResponseEntity` / `Page`.
- **`*Service`** = an application use case / business service (`@Service`). Examples: `HandImportService`
  (write), `HandResultBuilder` (build orchestration). Transaction boundaries live here.
- **`*Query`** = a read-only use case (`@Service` + `@Transactional(readOnly = true)`) returning DTOs/`Page`.
  This is the "Query" side of the read/write split. There is no separate `*Command` class today; the write
  command role is played by `HandImportService`. If you add explicit command objects, mirror the `*Query`
  style.
- **`*Repository`** = Spring Data JPA repository interface. `HandResultRepository extends JpaRepository<...>,
  JpaSpecificationExecutor<...>`. Repositories are **package-private** and only used by services in the same
  package.
- **`*Specification`** = dynamic JPA `Specification` builders (Criteria API) for filtering
  (`HandResultSpecification.withFilters(...)`). Prefer specifications over ad-hoc query methods for optional
  filters.
- **`*Handler` (`ActionHandler`)** = a **Strategy** that consumes one or more `ActionType`s and folds them
  into the mutable `HandState`. Each handler is a `@Component`, declares its supported types via
  `getSupportedTypes()`, and is auto-registered into the `ActionHandlerRegistry` (an `EnumMap` populated from
  the injected `List<ActionHandler>`). **To support a new event: add an `ActionType`, add a parsing rule, and
  add a handler — no central switch to edit.**
- **`ParsingRule` + `ActionMapper`** = the parser engine. A `ParsingRule` pairs a regex `Pattern` with a
  functional `ActionMapper` (`(Matcher, ParserContext, String) -> List<Action>`). Rules are declared in
  `PokerStarsParsingRuleFactory`; `PokerStarsParser` applies them line-by-line, **first match wins**.
- **`*Converter` (`AttributeConverter`)** = JPA converters (`@Converter(autoApply = true)`) that persist rich
  value objects as strings (`Board`, `PocketCards`). This keeps the DB schema simple and the domain rich.
- **`HandState`** = a **mutable** working accumulator (`@Data`) used only during building; it is **not**
  persisted. `HandResult` is the **immutable** persisted aggregate (built via Lombok `@Builder`).
- **DTOs** = Java `record`s in the `web` package with a static factory `of(entity)` for mapping
  (`HandResultSummaryResponse.of(...)`, `HandResultFilter`). Request filters are bound via `@ModelAttribute`.
- **`GlobalExceptionHandler`** = `@RestControllerAdvice` returning RFC-7807 `ProblemDetail` responses.

---

## 6. Coding conventions & idioms

Follow the patterns already present. Key rules:

### General Java

- **Prefer immutability.** Use `record`s for value objects and DTOs (`Card`, `Board`, `PocketCards`,
  `HandResultFilter`, `HandResultSummaryResponse`). Keep persisted aggregates effectively immutable (built via
  `@Builder`, only getters exposed).
- **Static factory methods** are the standard construction idiom: `fromHandHistory`, `fromString`,
  `fromSymbol`, `fromDb`, `fromRawHand`, `of(...)`. Name factories by their source/intent.
- **No nulls leaking as control flow** — use `Optional` where a value may be absent
  (`Position.getExplicitPosition` returns `Optional<Position>`). Guard against `null` when mapping entities to
  DTOs.
- **Fail fast on bad input** with `IllegalArgumentException` (see `Rank.fromSymbol`).
- Use `static` imports the way the codebase does (e.g. `import static ...ActionType.*`,
  `import static org.springframework.util.StringUtils.hasText`).
- **Money is always `BigDecimal`.** Never use `double`/`float` for chips. Create amounts through
  `ChipsUtils.of(...)` (scale 2, `HALF_UP`). Entity money columns use `@Column(precision = 12, scale = 2)`.
- Enums are stored as strings in the DB (`@Enumerated(EnumType.STRING)`).

### Lombok

- Use Lombok consistently: `@Getter`, `@Builder`, `@AllArgsConstructor(staticName = "of")`, `@Data`
  (only for mutable helpers like `HandState`), `@NoArgsConstructor(access = PRIVATE)` for utility/JPA classes,
  `@EqualsAndHashCode`.
- Utility classes have a **private no-args constructor** (`@NoArgsConstructor(access = PRIVATE)`) and only
  `static` methods (`PositionUtils`, `ChipsUtils`, `PokerStarsParsingRuleFactory`).

### Spring

- Inject dependencies via **constructor injection** using Lombok `@AllArgsConstructor` on `final` fields.
  Do **not** use `@Autowired` on fields.
- Keep **visibility tight**: classes that are internal to a package (repositories, builders, specifications,
  converters, the read controller, `ParsingRule`, `ParserContext`, `GlobalExceptionHandler`) are
  **package-private**. Only expose classes that genuinely need to be public.
- Transaction boundaries live in services (`@Transactional` on writes, `@Transactional(readOnly = true)` on
  queries).
- REST endpoints are versioned under `/v1/...`.

### Formatting

- The project uses **Spring's Java format** (tabs for indentation, specific import ordering). Do not fight it.
  Run `./mvnw spring-javaformat:apply` before committing.

---

## 7. Testing conventions

Tests live under `src/test/java` mirroring the main package structure. **Match these conventions exactly.**

### Libraries (use these, and only these)

- **JUnit 5** (`org.junit.jupiter`) — `@Test`, `@BeforeEach`.
- **AssertJ** for **all** assertions: `assertThat(...)`, `assertThatCode(...).doesNotThrowAnyException()`,
  `assertThatThrownBy(...)`, etc.
  - ❌ **Never** use JUnit's `Assertions.assert*`, Hamcrest, or plain `assert`. Assertions must come from
    **AssertJ** only. (E.g. do not `assertEquals(...)`; use `assertThat(x).isEqualTo(...)`.)
- **Mockito** for mocking: `@Mock`, `@MockitoBean` (Spring context mocks), `when/verify/inOrder`.
  - Note: `@MockitoBean` (not the removed `@MockBean`) is used in Spring MVC slice tests.
- **Instancio** for test data generation: `Instancio.of(Type.class).create()`, `.set(field(...), value)`.
- **Spring MVC Test** for controllers: `@WebMvcTest(SomeRestService.class)` + `MockMvc`.

### Base class & helpers

- Extend **`TestBase`** for tests that need Mockito field initialisation. It:
  - runs `MockitoAnnotations.openMocks(this)` in `@BeforeEach`,
  - calls an abstract `init()` you implement to set up the subject under test,
  - provides `valid(Class<T>)` = `Instancio.of(type).create()` for quick valid instances.
- Pure unit tests that construct the subject with `new` and don't need the Mockito lifecycle may define their
  own `@BeforeEach setUp()` without extending `TestBase` (see `InvestmentHandlerTest`).

### Test data: TemplateLoaders

- Reusable fixtures live in **`*TemplateLoader`** classes with **static factory methods** returning
  domain-meaningful, named instances, e.g.:
  - `ActionTemplateLoader.validGuiCall()`, `validGuiPostBigBlind()`, `validGuiSeatResultAtBtn()`, ...
  - `HandStateTemplateLoader.validGuiPreFlop()`, ...
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
  `status()` and verifying the delegated call.

### Real hand-history samples

- End-to-end/regression samples live in `src/test/resources/GuiRodri2013/` (real exported PokerStars hand
  histories: cash games and tournaments). Use them when adding parser coverage.

---

## 8. How to add a new feature (recipes)

### Add support for a new hand-history event / new statistic

1. Add a new value to **`ActionType`** (and `ActionStreet` if a new street/phase is needed).
2. Add a **parsing rule** in `PokerStarsParsingRuleFactory` (a regex `Pattern` + an `ActionMapper` lambda
   producing one or more `Action`s). Remember: rules are evaluated top-to-bottom, **first match wins** — order
   matters (put more specific patterns before more general ones).
3. If the event affects hero stats, add fields to **`HandState`** (with mutation helpers) and, if it must be
   persisted, to **`HandResult`** (+ builder wiring in `HandResultBuilder`).
4. Create a new **`ActionHandler` `@Component`** returning the new `ActionType`(s) from `getSupportedTypes()`
   and mutating `HandState` in `handle(...)`. It auto-registers — no registry edits needed. Filter by
   `heroName` when the stat is hero-specific.
5. Add unit tests: a `*HandlerTest`, parser coverage in `PokerStarsParserTest`, and (if applicable)
   `HandResultBuilderTest` using a `HandTemplateLoader` fixture.

### Add a new query filter

1. Add the field to the **`HandResultFilter`** record.
2. Add the predicate in **`HandResultSpecification.withFilters(...)`** (guard optional values with `hasText`).
3. Cover it in `HandResultQueryTest` / the specification test.

### Add a new endpoint

1. Add the method to the appropriate **`*RestService`** under `/v1/...`, delegating to a `*Service`/`*Query`.
2. Use `record` DTOs with `of(...)` factories for requests/responses.
3. Add a `@WebMvcTest` slice test.

---

## 9. Do & Don't (quick checklist for agents)

**Do**
- Keep `core.poker` framework-free and pure.
- Use `record`s + static factories + constructor injection + Lombok as the surrounding code does.
- Use `BigDecimal` via `ChipsUtils` for money; store enums as `STRING`.
- Keep classes package-private unless they must be public.
- Use **AssertJ** for every assertion and **Instancio**/`*TemplateLoader` for fixtures.
- Name tests `should_...`; controllers `*RestService`; run `spring-javaformat:apply`.

**Don't**
- ❌ Introduce assertion libraries other than AssertJ (no JUnit `assertEquals`, no Hamcrest, no bare `assert`).
- ❌ Add a dependency that isn't already in `pom.xml` without a clear reason.
- ❌ Put a central `switch`/`if` over `ActionType` — extend via a new `ActionHandler` + parsing rule instead.
- ❌ Let controllers touch repositories/parser/handlers directly — go through a `*Service`/`*Query`.
- ❌ Use `double`/`float` for chip amounts.
- ❌ Leak framework/JPA concerns into `core.poker`.
- ❌ Break Spring Java formatting (build fails in the `validate` phase).

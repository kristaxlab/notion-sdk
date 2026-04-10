# Notion SDK for Java

The Notion SDK for Java is a library that allows developers to interact with the
Notion API using Java. It provides a convenient way to access and manipulate Notion
data, such as pages, databases, and blocks.

## Priority #1: Lightweight, Intuitive API

The SDK's top priority is **convenience of library usage and an intuitive API**. Building and reading
complex Notion models should feel lightweight and effortless. Every design decision should be
evaluated against the question: *"Will an SDK user immediately understand how to use this?"*

Key principles:

- **`of(...)` static factories** for simple, common construction (single value, list of values, no
  optional parameters). Named after Java conventions (`List.of(...)`, `Optional.of(...)`).
- **`builder()`** for complex construction with optional fields, conditional logic, or incremental
  accumulation. The builder is the single path for advanced use-cases.
- **Never offer both for the same purpose.** Factories are shortcuts for the 80% case. Builders
  handle the remaining 20%. A user should never be confused about which mechanism to use.
- **Params classes.** Request-only objects (e.g., `AppendBlockChildrenParams`) use
  `@Getter'/'@Setter'` and a public no-arg constructor for serialization compatibility, but expose
  `of(...)` and `builder()` as the primary construction API.
- **Model classes.** Objects that are both returned by the API and sent back in updates
  (e.g., `Block`, `ParagraphBlock`) use `@Getter`/`@Setter` and a public no-arg constructor. This
  enables the retrieve → modify → update workflow that SDK users expect.
- **Endpoint overloads** should accept the most natural type directly (e.g., `Block`, `List<Block>`)
  so users rarely need to construct params objects at all.
- prefer new ArrayList<>() and new HashMap<>() over fluent builders for immutable collections where immutability is not
  explicitly requested.

## Code Quality

The code should be organized in a way that is easy to understand and maintain. It should follow best
practices for Java development, such as using appropriate design patterns and adhering to the
principles of object-oriented programming. Code should be well-documented, with clear Javadoc
explaining the purpose and functionality of each class and method. It should also include error
handling to ensure that any issues that arise during API interactions are properly managed.

All code and design approaches should be at a senior engineer level or higher.

Notion SDK should be designed as a production-grade library, with a focus on reliability,
performance, and security. It should be thoroughly tested to ensure that it works correctly and
efficiently in a variety of scenarios. Additionally, it should be regularly updated to keep up with
changes to the Notion API and to address any bugs or issues that may arise.

## Serialization

Where possible, avoid binding model and params classes to Jackson-specific annotations. Prefer
plain Java constructs (public no-arg constructors, getters/setters, standard naming conventions)
that work with any JSON library. Use Jackson annotations only when there is no reasonable
alternative (`@JsonTypeInfo` for polymorphic deserialization). This keeps the door open for
supporting alternative serialization libraries in the future.

## Code Comments

Comments should not be used as separators between methods. Do not use comment blocks that imitate
dividers. Example to avoid:

```java
// ── Convenience factories: ──
```

## Model Builders

Key principles for builders:

- **Fluent API.** Builder methods should return `this` to allow chaining.
- **Nested objects** For nested objects builders should have methods that accept the nested object directly (e.g.,
  `builder.caption(List<RichText> text)`) as well as builder methods that accept the nested builder (e.g.,
  `paragraphBlock.caption(Consumer<RichText.Builder> textBuilder)`).
- **Methods naming.** Builders should reflect strictly the field they are building ('.color(...)' instead of '
  .withColor(...)' or '.setColor(...)')
- **build() vs buildList().** Some models are used quite often as lists (e.g., `RichText`, 'ColumnBlock') and it is
  common for users to want to build a single instance and wrap it in a list.
  In such cases, builders should provide both `build()` (returns the single instance) and `buildList()` (returns a list
  containing the single instance).
- **build() and buildList()** should always return new instances (never cached or static instances) to avoid unintended
  side effects from mutability.
- **Exceptional cases** Sometimes builder methods should implement additional methods to support the most common use
  cases. For example, `ParagraphBlock.Builder` has a `text(String)` method that is a shortcut for setting a single text
  item in the content list even though the underlying model is `List<RichText>`. This is an example of a builder method
  that is not strictly named after the field it is building, but it provides a convenient shortcut for a common use
  case. We should assess such cases for every builder method and implement them when they significantly improve the
  developer experience without introducing confusion.

### Builder API Design

When implementing a builder, think at two levels:

#### 1. Structure (what you usually get right)
- Accumulate entries in a list when the model is used as a list.
- Provide `build()` (single entry, throws if 0 or >1) and `buildList()` (all entries).
- Both always return new instances (defensive copy).

#### 2. API / DSL (what to focus on more)
- **Design for readability at the call site, not for structural symmetry with the model.**
  Ask: "How would a developer naturally describe this operation in plain English?"
  Then name the methods after that description.
  Example: `from("old").to("new")` reads as a sentence; `update("old", "new")` reads as a constructor.

- **Split multi-field entries into one method per field** so each step can be chained individually.
  Prefer `from(x).to(y)` over `update(x, y)`.

- **Prefer named intent methods over boolean parameters.**
  Prefer `replaceAll()` / `replaceFirst()` over `replaceAllMatches(boolean)`.
  Boolean parameters are harder to read at the call site: `.replaceAllMatches(false)` vs `.replaceFirst()`.

- **Provide an explicit transition method (`and()`, `next()`, etc.) to start a new entry.**
    - Validate the previous entry is complete before starting the next one.
    - This makes the separator visible and self-documenting in the chain.

- **Pre-initialize the builder with one entry** so the user can start setting fields immediately
  without needing a separate "start" method.


## Java doc principles

### 1. Start with a summary

- Begin with a one-sentence summary describing what the class or method does.
- End the summary sentence with a period.

### 2. Describe parameters, return values, and exceptions

- Use `@param` for each parameter (what it represents).
- Use `@return` for methods that return a value.
- Use `@throws` (or `@exception`) for thrown exceptions that callers should know about.

### 3. Favor complete, clear, and concise language

- Use correct grammar, punctuation, and terminology.
- Use present tense and third person (e.g., “Returns…”, “Computes…”).
- Write for an international audience; avoid idioms and slang.
- Prefer concise descriptions without losing meaning.
- Prefer clear terms and consistent naming in prose and examples.
- Stick to the main points and avoid overcomplicating explanations.

### 4. Use tags appropriately

- Use `@deprecated` with clear migration guidance and an alternative API suggestion.
- Use `@see`, `{@link ...}`, and `{@linkplain ...}` to connect related API elements.
- Avoid `@author` (version control provides authorship).

### 5. Explain the “why,” not just the “what”

- Capture intent, constraints, tradeoffs, and important side effects.
- Call out key behavioral contracts (e.g., thread-safety, immutability, synchronization expectations).

### 6. Avoid redundancy

- Don’t restate the method name or repeat what is obvious from the signature.
- Don’t duplicate implementation details that are likely to change.

### 7. Use HTML markup sparingly and correctly

- Use simple HTML when it improves readability (`<p>`, `<ul>`, `<li>`, `<code>`, `<pre>`).
- Avoid excessive formatting that reduces maintainability.

### 8. Include code examples where they add real value

- For non-trivial APIs, include short, focused usage examples (often best in `<pre><code>...</code></pre>`).
- Keep examples correct, minimal, and representative.

### 9. Don’t expose internals or sensitive information

- Avoid documenting secrets, credentials, internal-only endpoints, or security-sensitive details.
- Don’t leak implementation details that could become attack surfaces or lock you into internals.
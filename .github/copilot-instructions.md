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

## Comments

Comments should not be used as separators between methods. Do not use comment blocks that imitate
dividers. Example to avoid:

```java
// ── Convenience factories: ──
```

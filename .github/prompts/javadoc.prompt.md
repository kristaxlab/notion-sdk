---
agent: 'agent'
description: 'Generate Javadoc comments and update existing ones if needed.'
---

Generate Javadoc comments and update existing ones if needed for  ${input:classOrMethod}.

Ask me about ${input:classOrMethod} if it is not clear to you.

# Javadoc best practices

## 1. Start with a summary
- Begin with a one-sentence summary describing what the class or method does.
- End the summary sentence with a period.

## 2. Describe parameters, return values, and exceptions
- Use `@param` for each parameter (what it represents).
- Use `@return` for methods that return a value.
- Use `@throws` (or `@exception`) for thrown exceptions that callers should know about.

## 3. Favor complete, clear, and concise language
- Use correct grammar, punctuation, and terminology.
- Use present tense and third person (e.g., “Returns…”, “Computes…”).
- Write for an international audience; avoid idioms and slang.
- Prefer concise descriptions without losing meaning.
- Prefer clear terms and consistent naming in prose and examples.
- Stick to the main points and avoid overcomplicating explanations.

## 4. Use tags appropriately
- Use `@deprecated` with clear migration guidance and an alternative API suggestion.
- Use `@see`, `{@link ...}`, and `{@linkplain ...}` to connect related API elements.
- Avoid `@author` (version control provides authorship).

## 5. Explain the “why,” not just the “what”
- Capture intent, constraints, tradeoffs, and important side effects.
- Call out key behavioral contracts (e.g., thread-safety, immutability, synchronization expectations).

## 6. Avoid redundancy
- Don’t restate the method name or repeat what is obvious from the signature.
- Don’t duplicate implementation details that are likely to change.

## 7. Use HTML markup sparingly and correctly
- Use simple HTML when it improves readability (`<p>`, `<ul>`, `<li>`, `<code>`, `<pre>`).
- Avoid excessive formatting that reduces maintainability.

## 8. Include code examples where they add real value
- For non-trivial APIs, include short, focused usage examples (often best in `<pre><code>...</code></pre>`).
- Keep examples correct, minimal, and representative.

## 9. Don’t expose internals or sensitive information
- Avoid documenting secrets, credentials, internal-only endpoints, or security-sensitive details.
- Don’t leak implementation details that could become attack surfaces or lock you into internals.
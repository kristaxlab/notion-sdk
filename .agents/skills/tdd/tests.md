# Good and Bad Tests

## Good Tests

**Integration-style**: Test through real interfaces, not mocks of internal parts.

```java
// GOOD: Tests observable behavior
@Test
@DisplayName("user can checkout with valid cart")
void userCanCheckoutWithValidCart() {
  Cart cart = createCart();
  cart.add(product);
  CheckoutResult result = checkout(cart, paymentMethod);
  assertEquals("confirmed", result.status());
}
```

Characteristics:

- Tests behavior users/callers care about
- Uses public API only
- Survives internal refactors
- Describes WHAT, not HOW
- One logical assertion per test

## Bad Tests

**Implementation-detail tests**: Coupled to internal structure.

```java
// BAD: Tests implementation details
@Test
@DisplayName("checkout calls paymentService.process")
void checkoutCallsPaymentServiceProcess() {
  PaymentService paymentService = mock(PaymentService.class);
  checkout(cart, paymentService);
  verify(paymentService).process(cart.total());
}
```

Red flags:

- Mocking internal collaborators
- Testing private methods
- Asserting on call counts/order
- Test breaks when refactoring without behavior change
- Test name describes HOW not WHAT
- Verifying through external means instead of interface

```java
// BAD: Bypasses interface to verify
@Test
@DisplayName("createUser saves to database")
void createUserSavesToDatabase() throws SQLException {
  createUser(new UserCreate("Alice"));
  try (ResultSet row = db.query("SELECT * FROM users WHERE name = ?", "Alice")) {
    assertTrue(row.next());
  }
}

// GOOD: Verifies through interface
@Test
@DisplayName("createUser makes user retrievable")
void createUserMakesUserRetrievable() {
  User user = createUser(new UserCreate("Alice"));
  User retrieved = getUser(user.id());
  assertEquals("Alice", retrieved.name());
}
```

**Tautological tests**: Expected value restates the implementation, so the test passes by construction.

```java
// BAD: Expected value is recomputed the way the code computes it
@Test
@DisplayName("calculateTotal sums line items")
void calculateTotalSumsLineItems() {
  List<LineItem> items = List.of(new LineItem(10), new LineItem(5));
  int expected = items.stream().mapToInt(LineItem::price).sum();
  assertEquals(expected, calculateTotal(items));
}

// GOOD: Expected value is an independent, known literal
@Test
@DisplayName("calculateTotal sums line items")
void calculateTotalSumsLineItems() {
  assertEquals(15, calculateTotal(List.of(new LineItem(10), new LineItem(5))));
}
```

# Unit Tests Documentation (Observer Pattern)

## Overview

This project includes unit tests for the Observer pattern implementation in the Domain core events module:

1. `com.piedrazul.Domain.core.events.ClsClinicEvent`
2. `com.piedrazul.Domain.core.events.ClinicEventType`
3. `com.piedrazul.Domain.core.events.ClsClinicEventBus`
4. `com.piedrazul.Domain.core.events.IClinicObserver`

The tests are written with:
- **JUnit 5**
- **Mockito** (for observer mocks)
- **AAA pattern** (Arrange / Act / Assert)
- Clear, descriptive test method names
- Test classes prefixed with `UTest...`

## Test Run

Command used:

```sh
mvn test -Dtest="UTestClsClinicEvent,UTestClinicEventType,UTestClsClinicEventBus"
```

Or run all tests:

```sh
mvn test
```

Result:
- All 22 observer pattern unit tests execute successfully (exit code 0).

## Where the Tests Are

- `src/test/java/com/piedrazul/Domain/core/events/UTestClsClinicEvent.java`
- `src/test/java/com/piedrazul/Domain/core/events/UTestClinicEventType.java`
- `src/test/java/com/piedrazul/Domain/core/events/UTestClsClinicEventBus.java`

## Components Under Test

| Component         | Role                          |
|-------------------|-------------------------------|
| `ClinicEventType` | Enum: event types (USER_CHANGE, STATUS_MESSAGE) |
| `ClsClinicEvent`  | Immutable event payload (type + message)       |
| `IClinicObserver` | Interface: `void onEvent(ClsClinicEvent event)` |
| `ClsClinicEventBus` | Subject: subscribe, unsubscribe, publish     |

---

## What Was Tested

### 1) UTestClsClinicEvent

**Location:** `src/test/java/com/piedrazul/Domain/core/events/UTestClsClinicEvent.java`

**Coverage:**

| Category   | Test | Description |
|------------|------|-------------|
| **Normal** | `constructorStoresUserChangeTypeAndMessageCorrectly` | Constructor stores `USER_CHANGE` type and message; getters return correct values. |
| **Normal** | `constructorStoresStatusMessageTypeAndMessageCorrectly` | Constructor stores `STATUS_MESSAGE` type and message; getters return correct values. |
| **Normal** | `eventIsImmutableGettersReturnConstructorValues` | Event is immutable; getters return same values as constructor args. |
| **Borderline** | `constructorAcceptsEmptyMessage` | Constructor accepts empty string message. |
| **Borderline** | `constructorAcceptsNullMessage` | Constructor accepts `null` message. |
| **Borderline** | `constructorAcceptsLongMessage` | Constructor accepts very long message (10,000 chars). |

**Total:** 6 tests

---

### 2) UTestClinicEventType

**Location:** `src/test/java/com/piedrazul/Domain/core/events/UTestClinicEventType.java`

**Coverage:**

| Category   | Test | Description |
|------------|------|-------------|
| **Normal** | `userChangeEnumValueExists` | `USER_CHANGE` enum value exists and has correct `name()`. |
| **Normal** | `statusMessageEnumValueExists` | `STATUS_MESSAGE` enum value exists and has correct `name()`. |
| **Normal** | `valueOfReturnsUserChangeForValidString` | `valueOf("USER_CHANGE")` returns correct enum. |
| **Normal** | `valueOfReturnsStatusMessageForValidString` | `valueOf("STATUS_MESSAGE")` returns correct enum. |
| **Borderline** | `valuesReturnsAllEnumConstants` | `values()` returns array with exactly two elements in expected order. |
| **Error** | `valueOfThrowsForInvalidName` | `valueOf("INVALID_TYPE")` throws `IllegalArgumentException`. |

**Total:** 6 tests

---

### 3) UTestClsClinicEventBus

**Location:** `src/test/java/com/piedrazul/Domain/core/events/UTestClsClinicEventBus.java`

**Dependency mocking:**
- Mocks `IClinicObserver` for verification of `onEvent()` calls.

**Coverage:**

| Category   | Test | Description |
|------------|------|-------------|
| **Normal** | `subscribeAndPublishSingleObserverReceivesEvent` | Single subscribed observer receives published event. |
| **Normal** | `subscribeMultipleObserversAllReceiveEvent` | Multiple observers all receive the same event. |
| **Normal** | `unsubscribeObserverNoLongerReceivesEvents` | Unsubscribed observer does not receive events. |
| **Normal** | `clinicObserverImplementationReceivesEvent` | Concrete `IClinicObserver` implementation receives event via `onEvent`. |
| **Borderline** | `subscribeNullObserverDoesNotAdd` | `subscribe(null)` is ignored; valid observers still receive events. |
| **Borderline** | `publishWithZeroObserversDoesNotThrow` | `publish()` with no observers does not throw. |
| **Borderline** | `unsubscribeNonSubscribedObserverDoesNotThrow` | `unsubscribe()` on never-subscribed observer does not throw. |
| **Borderline** | `subscribeSameObserverTwiceReceivesEventTwice` | Same observer subscribed twice receives the event twice. |
| **Borderline** | `publishNullEventPassesNullToObserver` | `publish(null)` passes `null` to observer's `onEvent`. |
| **Error** | `observerThrowingExceptionPropagates` | When an observer throws, the exception propagates; subsequent observers are not notified. |

**Mockito verification:**
- Verifies `observer.onEvent(event)` is called with expected event (or `null`).
- Verifies `never()` when observer is unsubscribed or when a prior observer throws.

**Total:** 10 tests

---

## Coverage Summary

| Component        | Normal | Borderline | Error | Total |
|------------------|--------|------------|-------|-------|
| ClsClinicEvent   | 3      | 3          | 0     | 6     |
| ClinicEventType  | 4      | 1          | 1     | 6     |
| ClsClinicEventBus| 4      | 5          | 1     | 10    |
| **Total**        | **11** | **9**      | **2** | **22**|

---

## Related Source Files

- `src/main/java/com/piedrazul/Domain/core/events/ClinicEventType.java`
- `src/main/java/com/piedrazul/Domain/core/events/ClsClinicEvent.java`
- `src/main/java/com/piedrazul/Domain/core/events/IClinicObserver.java`
- `src/main/java/com/piedrazul/Domain/core/events/ClsClinicEventBus.java`

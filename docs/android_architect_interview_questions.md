# Android Architect Interview Questions

---

## Topic 1: Core Android + Kotlin (Deep Technical Round)

### Flow, SharedFlow, and StateFlow.

<details>
  <summary><strong>1. Explain the difference between Flow, SharedFlow, and StateFlow.</strong></summary>

***

# ✅ **Flow vs SharedFlow vs StateFlow (Conceptual + Code + Summary)**

These are all part of **Kotlin Coroutines** and used for **asynchronous, reactive, and cold/hot stream processing**.  
But each behaves differently and is suited for different architectural scenarios, especially in financial apps where **real‑time updates**, **UI state**, and **event‑driven flows** are common.

***

# ✅ **1. Flow**

### **Type:** Cold stream

### **Use Case:** One‑shot operations

(e.g., fetching account details, network calls, database queries)

### ✅ Characteristics

*   **Cold** ➝ Starts emitting only when collected.
*   **No replay** ➝ Collector receives only fresh emissions.
*   **Sequential** ➝ Suspends until each value is processed.
*   **Supports operators** (map, filter, flatMapLatest).
*   Perfect for **stateless tasks**.

### ✅ Example

```kotlin
fun fetchCustomerProfile(): Flow<Customer> = flow {
    emit(api.getCustomer())
}
```

```kotlin
viewModelScope.launch {
    fetchCustomerProfile().collect { profile ->
        updateUi(profile)
    }
}
```

***

# ✅ **2. SharedFlow**

### **Type:** Hot stream

### **Use Case:** UI events, one-time navigation events, analytics events, push notifications, money transfer success events.

### ✅ Characteristics

*   **Hot** ➝ Always active even without collectors.
*   **Does NOT store state (unless replay > 0)**.
*   **Multiple collectors receive the same emissions**.
*   **replay cache** allows re-emitting past events.
*   **Great for EVENTS, not UI STATE**.

### ✅ Example (One-time event)

```kotlin
private val _events = MutableSharedFlow<UiEvent>()
val events = _events

fun onMoneyTransferred() {
    viewModelScope.launch {
        _events.emit(UiEvent.TransferSuccess)
    }
}
```

```kotlin
lifecycleScope.launchWhenStarted {
    viewModel.events.collect { event ->
        when(event) {
           UiEvent.TransferSuccess -> showSuccessDialog()
        }
    }
}
```

***

# ✅ **3. StateFlow**

### **Type:** Hot stream with state

### **Use Case:** UI State, ViewModel state, screen rendering, account balance updates, loan eligibility result state.

### ✅ Characteristics

*   **Hot** stream maintained by `CoroutineScope`.
*   **Always has a current value (like LiveData)**.
*   **Replays the latest value immediately** to new collectors.
*   Great for **state machines, MVVM**, UI binding.
*   Backed by a **single value state + continuous updates**.

### ✅ Example (UI State)

```kotlin
private val _uiState = MutableStateFlow(AccountState())
val uiState: StateFlow<AccountState> = _uiState

fun loadBalance() {
    viewModelScope.launch {
        val balance = repository.getBalance()
        _uiState.value = _uiState.value.copy(balance = balance)
    }
}
```

```kotlin
lifecycleScope.launchWhenStarted {
    viewModel.uiState.collect { state ->
        balanceText.text = state.balance.toString()
    }
}
```

***

# ✅ **When to Use What? (Interview‑Ready Short Answers)**

| Type           | Cold/Hot | Stores Latest Value      | Replays Old Values | Best For                                |
| -------------- | -------- | ------------------------ | ------------------ | --------------------------------------- |
| **Flow**       | Cold     | ❌ No                     | ❌ No               | One‑shot operations, DB/network queries |
| **SharedFlow** | Hot      | ❌ No (unless replay > 0) | ✅ Configurable     | Events, navigation actions, broadcasts  |
| **StateFlow**  | Hot      | ✅ Always                 | ✅ 1 latest value   | UI state, ViewModel state, data binding |

***

# ✅ **Architect‑Level Real Banking Scenarios**

### **Flow**

✔ Fetching “Mini Statement” from API → just one-shot data retrieval.

### **SharedFlow**

✔ Fire a one-time “Transaction Success Event” after IMPS/NEFT transfer.  
✔ Push notifications broadcast inside app.

### **StateFlow**

✔ Maintain screen state → balance, beneficiary list, loan eligibility score.  
✔ Real-time updates like stock prices, forex rates.

***

# ✅ **Interview Quick Summary**

✅ **Flow**

*   Cold, starts on collect
*   One-shot, stateless
*   No replay, no caching
*   Ideal for network/db operations

✅ **SharedFlow**

*   Hot stream
*   Broadcast-style
*   No internal state
*   Great for events (navigation, toasts, analytics)

✅ **StateFlow**

*   Hot + holds latest value (Like LiveData but better)
*   Always replays latest value
*   Perfect for UI State in MVVM

✅ **General Rule**

*   **StateFlow → For state**
*   **SharedFlow → For events**
*   **Flow → For tasks**

***

</details>

<details>
  <summary><strong>2. Why would you replace LiveData with StateFlow?</strong></summary>

***

# ✅ **Why Replace LiveData with StateFlow? (Architect‑Level Explanation)**

## ✅ 1. **StateFlow is Kotlin‑First (Coroutines‑Native)**

LiveData is not designed for Kotlin coroutines.  
StateFlow **is built inside Kotlin’s Coroutine library**, giving:

*   Structured concurrency
*   Operators (map, combine, debounce, flatMapLatest…)
*   Backpressure control
*   Cancellation support

This makes it far more powerful for modern reactive UIs.

### ❌ LiveData

*   Needs bridges like `liveData {}` or `asLiveData()`
*   No coroutine operators

### ✅ StateFlow

*   Works naturally inside `viewModelScope`
*   Can transform, debounce, combine states easily

***

## ✅ 2. **StateFlow is Always Hot + Always Has a State Value**

**StateFlow always contains the latest value** and immediately emits it to collectors.

This solves a major LiveData limitation:

### ❌ LiveData’s “missed emissions” problem

If a screen subscribes late (e.g., after rotation), it may miss events unless carefully managed.

### ✅ StateFlow

*   Always replays the latest state
*   Perfect for UI state machines, screen rendering, and recomposition

***

## ✅ 3. **StateFlow is Lifecycle‑Agnostic (Better for Clean Architecture)**

LiveData is tied to Android lifecycle classes like:

*   Fragment
*   Activity
*   LifecycleOwner

This breaks **Clean Architecture** and makes ViewModels harder to test.

### ✅ StateFlow

*   Pure Kotlin → no Android dependencies
*   Can be used in:
    *   Domain layer
    *   Use‑cases
    *   Repositories
    *   Shared modules
    *   Jetpack Compose and XML UI

### ✅ Easier Unit Testing

Just collect from it — no need for Robolectric or LifecycleOwner.

***

## ✅ 4. **StateFlow Works the Same in Compose and XML**

Jetpack Compose prefers StateFlow because recomposition is state‑driven.

```kotlin
val uiState = viewModel.uiState.collectAsState()
```

LiveData works but is not the natural fit.

***

## ✅ 5. **Better Performance + Predictable Behavior**

StateFlow is implemented on top of `SharedFlow` → highly optimized.

✅ Strong guarantees:

*   **No loss of values**
*   **Consistent ordering**
*   **Always hot**
*   **Replay = 1**

LiveData has subtleties like:

*   PostValue batching
*   Order inconsistencies when posting from multiple threads
*   Observers activating/deactivating unpredictably

***

## ✅ 6. **Backpressure & Transformations**

LiveData cannot debounce, throttle, retry, switchMapLatest, etc.

StateFlow inherits these from Flow:

```kotlin
val searchState = MutableStateFlow("")

val results = searchState
    .debounce(300)
    .flatMapLatest { query -> repository.search(query) }
```

This is **impossible directly with LiveData**.

***

## ✅ 7. **Better Fit for MVI/MVVM Architecture**

Financial apps often use **MVI** or **unidirectional data flow**, where the UI subscribes to state updates.

StateFlow is designed exactly for this.

LiveData cannot model:

*   Immutable UI states
*   State reducers
*   Replayable state
*   Combine multiple states

StateFlow can:

```kotlin
val uiState = combine(balanceFlow, offersFlow, limitsFlow) { balance, offers, limits ->
    DashboardState(balance, offers, limits)
}.stateIn(viewModelScope, SharingStarted.Eagerly, DashboardState())
```

***

# ✅ Short Code Comparison

### ✅ LiveData

```kotlin
private val _balance = MutableLiveData<Int>()
val balance: LiveData<Int> = _balance
```

### ✅ StateFlow

```kotlin
private val _balance = MutableStateFlow(0)
val balance: StateFlow<Int> = _balance
```

StateFlow version:

*   Thread‑safe
*   Supports transformations
*   Works in any Kotlin layer
*   More predictable

***

# ✅ Interview‑Ready One‑Liner Answer

✅ **StateFlow is preferred over LiveData because it's coroutine‑native, lifecycle‑agnostic, more predictable, testable, supports powerful data transformations, always holds the latest UI state, and integrates better with modern UI frameworks like Jetpack Compose.**

***

# ✅ Quick Summary Points (For Rapid Prep)

*   ✅ Coroutine‑native → better for modern Kotlin development
*   ✅ Lifecycle‑agnostic → fits clean architecture
*   ✅ Always has latest value → no missed events
*   ✅ First‑class support for Flow operators
*   ✅ Better performance and consistency
*   ✅ Easier to test (no LifecycleOwner required)
*   ✅ Ideal for MVI/UI state management
*   ✅ Jetpack Compose → works seamlessly
*   ✅ Works across shared/business layers (not Android‑dependent)

***

</details>

<details>
  <summary><strong>3. When should you NOT use StateFlow?</strong></summary>

***

# ✅ **When You Should NOT Use StateFlow**

## ❌ **1. When the Data Represents One‑Time Events (Navigation, Toasts, Errors)**

StateFlow **always replays the last value** to any new collectors.  
This makes it *bad* for **events that must fire only once**.

### ❌ Example:

*   “Funds transferred successfully” dialog
*   Navigation events (go to OTP screen)
*   Snackbars/toasts
*   Logging/analytics events

### Problem:

A new subscriber (e.g. after screen rotation) will re-trigger the event.

✅ Use: **SharedFlow** (replay = 0)\*\*  
❌ Avoid: StateFlow

***

## ❌ **2. When There Is No “State” to Hold**

StateFlow must always hold *some* value.

If your feature has **no meaningful default** or you're modeling something that isn’t a state:

### Examples:

*   Network/DB queries returning a flow of results
*   Streams that emit *only when something changes*
*   Search results where no initial state makes sense
*   Socket streams that emit only when server pushes data

✅ Use: **Flow** (cold)  
❌ Avoid: StateFlow (requires initial state)

***

## ❌ **3. When You Need Lazy Behavior (Cold Streams)**

StateFlow is **hot** — it starts emitting immediately and keeps running.

This is wasteful for operations like:

### Examples:

*   Database queries
*   API calls
*   Expensive transformations
*   Complex CPU operations

Because these should run **only when someone collects**.

✅ Use: **Flow** (cold, lazy)  
❌ Avoid: StateFlow (always active)

***

## ❌ **4. When the Producer Has No Lifecycle / Needs Cleanup**

StateFlow never stops running unless the CoroutineScope is cancelled.  
If you create many StateFlows dynamically, you risk:

*   Memory leaks
*   Unclosed collectors
*   Unnecessary background work

Good example:

### ❌ Per-item StateFlow in list of transactions

```kotlin
List<Transaction>.map { MutableStateFlow(it) }
```

This creates dozens/hundreds of hot streams → expensive & unnecessary.

✅ Use:

*   Flow
*   SharedFlow (if events)
*   Simple Kotlin properties

***

## ❌ **5. When Backpressure Handling Is Important**

StateFlow does **not** support:

*   Buffering
*   Dropping
*   Conflation beyond last state
*   Throttling high-frequency events (e.g., market data)

If you're processing:

*   Stock/crypto price ticks
*   High-frequency sensor data
*   Reconciliation logs
*   Fraud-monitoring signals

StateFlow is not ideal.

✅ Use:

*   **SharedFlow with proper buffer**
*   **Flow with operators** (buffer, conflate, throttle, debounce)

***

## ❌ **6. When You Need Replay > 1**

StateFlow **only keeps 1 value**.

If you need event history or more than one replay:

### Examples:

*   Audit trails
*   Multi-step KYC processing
*   Chat messages
*   Timeline data
*   Form-wizard history

✅ Use: **SharedFlow(replay = N)**  
❌ Avoid: StateFlow (replay is fixed to 1)

***

## ❌ **7. When You Need UI Binding Without Coroutines**

StateFlow requires:

*   CoroutineScope
*   collect()
*   Lifecycle-aware collection

LiveData is still simpler in some legacy XML-only apps.

If you're stuck with legacy architecture where:

*   No coroutines
*   Heavy DataBinding
*   Observers are simple

✅ Use: **LiveData**  
❌ Avoid: StateFlow (adds complexity to legacy code)

***

# ✅ **Summary — When NOT to Use StateFlow**

✅ **Avoid StateFlow when:**

*   ❌ You need one-time events → Use **SharedFlow**
*   ❌ There is no meaningful initial state → Use **Flow**
*   ❌ You need cold, lazy streams → Use **Flow**
*   ❌ You have heavy producer costs → Prefer cold **Flow**
*   ❌ You need backpressure control → Use **Flow/SharedFlow with buffers**
*   ❌ You need replay history > 1 → Use **SharedFlow(replay=N)**
*   ❌ Legacies without coroutines/DataBinding → LiveData still fine

✅ **Use StateFlow only when:**

*   You’re managing **UI state**
*   You need **the latest value always available**
*   You want **hot + replay = 1**
*   ViewModel state in MVVM or MVI

***

</details>

<details>
  <summary><strong>4. How does replay work internally in SharedFlow?</strong></summary>

***

# ✅ **How Replay Works Internally in SharedFlow**

A **SharedFlow** is backed by an internal **buffer** managed by `MutableSharedFlow`. This buffer stores the *recent emissions* which get replayed to collectors when they subscribe.

Understanding replay means understanding these three internal components:

1.  **Replay Cache** (fixed size = replay parameter)
2.  **Additional Buffer** (extraBufferCapacity)
3.  **Suspension Queue** (for emitters when buffer is full)

Let’s break this down.

***

# ✅ **1. Replay Cache — What It Really Is**

When you create a SharedFlow:

```kotlin
val shared = MutableSharedFlow<Int>(replay = 2)
```

You're saying:

> “Keep the **last 2 emitted values** in memory. Whenever a new collector starts, **immediately send those 2 values again**.”

### Internally:

*   Replay cache is implemented as an **array-like ring buffer**.
*   Only the latest `replay` values are kept.
*   Old values are dropped automatically using **FIFO eviction**.

### Example:

```kotlin
shared.emit(1)
shared.emit(2)
shared.emit(3)
```

Replay cache becomes:

| replay = 2 | Cached values |
| ---------- | ------------- |
| 2          | \[2, 3]       |

When a collector subscribes:

```kotlin
collect { value -> ... }
```

It first receives:

    2
    3

Then continues with live emissions.

***

# ✅ **2. Additional Buffer Capacity (Not Part of Replay)**

You can specify more capacity:

```kotlin
MutableSharedFlow<Int>(
    replay = 1,
    extraBufferCapacity = 10,
    onBufferOverflow = BufferOverflow.SUSPEND
)
```

Internally SharedFlow has **two buffers**:

1.  **Replay Cache (fixed size = replay)**
2.  **Additional buffer for pending elements**

### Important:

Replay cache **is part of the active state**.  
Extra buffer is **transient** — not replayed.

This allows:

*   Smooth backpressure handling
*   More emissions than replay size
*   Avoid suspension if buffer has space

***

# ✅ **3. How Replay Is Delivered to Collectors**

When a new collector subscribes:

### ✅ Step 1 — Register Collector

Collector gets added to the internal **subscriber list**.

### ✅ Step 2 — Replay Cache Emitted Synchronously

Replay values are emitted *synchronously* (in order), before new emissions.

### ✅ Step 3 — New Emissions Are Delivered Live

After replaying, the collector starts receiving fresh values.

***

# ✅ **4. How Replay Cache Is Updated on emit()**

When you call:

```kotlin
shared.emit(value)
```

Internally:

1.  Add `value` to replay cache.
2.  If cache size exceeds replay:
    *   Drop the oldest entry (FIFO eviction).
3.  Deliver value to all active collectors.
4.  If no collectors:
    *   Still store in replay cache.
    *   Additional buffer may store more (if configured).

### Internal Pseudocode (Conceptual)

```kotlin
emit(value):
    replayCache.add(value)
    if replayCache.size > replay:
        replayCache.removeFirst()

    for collector in collectors:
        collector.trySend(value)
```

***

# ✅ **5. How Replay Works with Multiple Collectors**

Replay is **per collector**, not globally drained.

If three collectors start:

*   Each receives the full replay cache independently.
*   Replay values are **not consumed**, they are **replayed**.

***

# ✅ **6. Hot Stream Behavior**

SharedFlow is always hot.

Even with:

```kotlin
shared.emit(1)
```

If no collector exists, emission **still goes into replay cache**.

Unlike `Flow`, SharedFlow does NOT restart for each collector.

***

# ✅ **7. Memory Footprint**

Replay cache size is defined by:

    replay * sizeOf(value)

So:

*   replay = 1 → lightweight (best for events with the latest state)
*   replay = 1000 → heavy; used for logs, history, etc.

***

# ✅ **8. Why Replay Is Not Suitable for Events**

Replay *replays* old events.

So if replay > 0, collectors will get stale, already-handled events:

*   Navigation commands
*   Toasts/snackbars
*   Dialog triggers
*   Payment success events

✅ Use `SharedFlow(replay = 0)` for events.  
✅ Use `StateFlow` for state.

***

# ✅ **Visual Diagram — How Replay Works**

               emit(1) emit(2) emit(3) emit(4)
                     ↓
             ┌─────────────────────┐
    Replay   │        [3, 4]       │   ← replay = 2
    Cache    └─────────────────────┘

    New collector → gets: 3 → 4 → then live events

***

# ✅ **Quick Summary for Interview**

### ✅ How replay works internally in SharedFlow:

*   SharedFlow maintains a **Replay Cache**, a ring buffer of size = `replay`.
*   Every new collector **immediately receives** all replayed values, in order.
*   Replay values are **not consumed** — they persist for all future collectors.
*   Replay cache is separate from the **extra buffer** used for backpressure.
*   On every emit:
    *   The new value is added to replay cache.
    *   Oldest value is dropped when full (FIFO).
*   Replay happens **before** the collector receives new/live emissions.
*   Because replay repeats events, it’s **NOT suitable for one-time events**.

### ✅ Use replay when:

*   You want latest N values available to any new subscribers.
*   Use cases include: chat history, audit records, logs, recent user actions.

### ✅ Avoid replay when:

*   You emit ephemeral events (navigation, toasts).

***

</details>

<details>
  <summary><strong>5. How do you convert callback-based APIs to Flow?</strong></summary>

***

# ✅ **How to Convert Callback‑Based APIs to Flow**

Callback‑based APIs are common in Android:

*   Location updates
*   Bluetooth callbacks
*   WebSocket listeners
*   Payment gateway status callbacks
*   SDK event callbacks

To convert them into **Flow**, Kotlin provides several tools:

✅ `callbackFlow` (recommended)  
✅ `channelFlow` (rare)  
✅ `flow { … }` with suspendCancellableCoroutine (for one‑shot callbacks)

Let’s break it down.

***

# ✅ **1. Using `callbackFlow` — Best for Multi‑Value Streams**

Use this when the callback **emits multiple values over time**, such as:

*   Real‑time stock prices
*   Location updates
*   WebSocket messages
*   Banking transaction status stream

### ✅ Example: Converting Listener → Flow

```kotlin
fun locationUpdates(): Flow<Location> = callbackFlow {
    val listener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            trySend(location)   // non-suspending, safe
        }
    }

    fusedLocationProviderClient.requestLocationUpdates(
        LocationRequest.create(),
        listener,
        Looper.getMainLooper()
    )

    // cleanup when flow is cancelled
    awaitClose {
        fusedLocationProviderClient.removeLocationUpdates(listener)
    }
}
```

### ✅ Key Points

*   `callbackFlow` provides a **Channel-backed flow**
*   `trySend()` is safe & non-suspending
*   `awaitClose {}` is **mandatory** for cleanup (to avoid leaks!)

***

# ✅ **2. Converting One‑Shot Callbacks → Flow Using `suspendCancellableCoroutine`**

Use this for APIs that give you **exactly one result**:

*   Network SDK returning async callback
*   Biometric authentication result
*   Payment token generation callback
*   OTP request callback

### ✅ Example: One‑shot Callback → Flow

```kotlin
fun authenticateUser(): Flow<AuthResult> = flow {
    emit(suspendCancellableCoroutine { cont ->
        authSdk.authenticate(object : AuthCallback {
            override fun onSuccess(result: AuthResult) {
                cont.resume(result)
            }
            override fun onFailure(e: Throwable) {
                cont.resumeWithException(e)
            }
        })
    })
}
```

### ✅ When to use:

*   Single value callbacks
*   Error is handled via `resumeWithException`

***

# ✅ **3. Using `callbackFlow` for Event‑Based SDKs (e.g., Payment SDK)**

Imagine a Payment SDK providing this callback:

```kotlin
interface PaymentListener {
    fun onPaymentStarted()
    fun onOtpRequired()
    fun onPaymentSuccess(txnId: String)
    fun onPaymentFailed(reason: String)
}
```

You convert it like this:

```kotlin
fun observePayment(): Flow<PaymentEvent> = callbackFlow {
    val listener = object : PaymentListener {
        override fun onPaymentStarted() {
            trySend(PaymentEvent.Started)
        }
        override fun onOtpRequired() {
            trySend(PaymentEvent.OtpRequired)
        }
        override fun onPaymentSuccess(txnId: String) {
            trySend(PaymentEvent.Success(txnId))
        }
        override fun onPaymentFailed(reason: String) {
            trySend(PaymentEvent.Failed(reason))
        }
    }

    paymentSdk.setListener(listener)

    awaitClose {
        paymentSdk.removeListener(listener)
    }
}
```

***

# ✅ **4. Using `callbackFlow` with Buffered Backpressure**

If the callback emits **too fast** (e.g., market data), use buffering:

```kotlin
callbackFlow {
    val listener = { data: PriceTick ->
        trySend(data)
    }

    marketApi.subscribe(listener)

    awaitClose { marketApi.unsubscribe(listener) }
}
.buffer(capacity = 100, onBufferOverflow = BufferOverflow.DROP_OLDEST)
```

Ideal for **financial streaming APIs**.

***

# ✅ **5. Converting Listener with Multiple Methods → Sealed Class Flow**

Best practice: wrap callback states using sealed classes:

```kotlin
sealed class CallState {
    data class Connected(val info: Info) : CallState()
    object Disconnected : CallState()
    data class Error(val cause: Throwable) : CallState()
}
```

Flow version:

```kotlin
fun callState(): Flow<CallState> = callbackFlow {
    val listener = object : CallListener {
        override fun onConnected(info: Info) {
            trySend(CallState.Connected(info))
        }
        override fun onDisconnected() {
            trySend(CallState.Disconnected)
        }
        override fun onError(t: Throwable) {
            trySend(CallState.Error(t))
        }
    }

    sdk.register(listener)
    awaitClose { sdk.unregister(listener) }
}
```

***

# ✅ **6. Converting Traditional Java Callbacks to Flow**

Example Java API:

```java
interface OnCompleteListener {
    void onComplete(String result);
}
```

Conversion:

```kotlin
fun getResultFlow(): Flow<String> = callbackFlow {
    val callback = OnCompleteListener { result ->
        trySend(result)
        close() // since only one emission
    }

    javaApi.call(callback)

    awaitClose {} // no cleanup needed
}
```

***

# ✅ **7. Converting Listener With Error → Flow with Exception**

```kotlin
fun fetchData(): Flow<String> = callbackFlow {
    val listener = object : ApiListener {
        override fun onSuccess(data: String) {
            trySend(data)
            close()
        }
        override fun onError(error: Throwable) {
            close(error)
        }
    }

    api.fetch(listener)
    awaitClose { /* cleanup */ }
}
```

***

# ✅ **8. When You Should NOT Use callbackFlow**

❌ For simple one-shot callback → Prefer suspendCancellableCoroutine  
❌ For high‑frequency events without limits → Must add buffer  
❌ When callback happens on main thread but heavy processing in collector → Use `flowOn(Dispatchers.IO)`  
❌ For hot streams with many consumers → Prefer SharedFlow/StateFlow

***

# ✅ **Interview‑Ready Summary**

✅ **Use `callbackFlow` for multi‑value async callbacks**  
✅ **Use `suspendCancellableCoroutine` for one‑shot callbacks**  
✅ Always call **awaitClose** to avoid leaks  
✅ Use **trySend** (not send) because callback is synchronous  
✅ Add buffer or backpressure for high-frequency streams  
✅ Wrap callback results in **sealed classes** for MVI  
✅ Use callbackFlow for:

*   WebSocket messages
*   Location updates
*   Payment status events
*   Stock/crypto/market price ticks
*   Bluetooth connection callbacks

***

</details>

---

### Kotlin Coroutines

<details>
  <summary><strong>1. Explain how coroutines are implemented under the hood.</strong></summary>

***

How **Kotlin Coroutines are implemented under the hood**, including:  
✅ Compiler transformations  
✅ State machines  
✅ Continuations  
✅ Dispatchers  
✅ Scheduling  
✅ Suspension mechanics  
✅ Thread switching  
✅ Structured concurrency internals

***

# ✅ **How Coroutines Are Implemented Under the Hood**

Kotlin coroutines are **not threads**. They are *lightweight*, cooperative tasks implemented using:

✅ Compiler transformations  
✅ Continuations  
✅ State machines  
✅ Dispatchers + Schedulers

Let’s break it down step by step.

***

# ✅ **1. Coroutines Are Compiled Into State Machines**

When you write:

```kotlin
suspend fun fetch(): String {
    val data = api.getData()
    return data.toUpperCase()
}
```

The Kotlin compiler transforms it into:

*   A **class** representing the coroutine
*   A **state machine** where each `suspend` point becomes a state
*   The function returns immediately and resumes later

### ✅ Simplified Compiler Output (Conceptual)

```kotlin
class FetchCoroutine(
    val continuation: Continuation<String>
) : Continuation<Any?> {

    var state = 0

    override fun resumeWith(result: Result<Any?>) {
        when (state) {
            0 -> {
                state = 1
                api.getDataAsync(this)  // suspends here
                return
            }
            1 -> {
                val data = result.getOrThrow()
                continuation.resume(data.toUpperCase())
            }
        }
    }
}
```

This is how Kotlin implements suspension:  
👉 **Every suspend function becomes a state machine**.

***

# ✅ **2. Coroutines Use Continuation-Passing Style (CPS)**

The **Continuation** object captures:

*   The current execution state
*   Local variables
*   The next instruction
*   The dispatcher

This is similar to how a “bookmark” works — the coroutine knows **where to resume**.

```kotlin
interface Continuation<T> {
    val context: CoroutineContext
    fun resumeWith(result: Result<T>)
}
```

***

# ✅ **3. What Happens at a suspend Function?**

A suspend point does **three things**:

### ✅ 1. Saves state

All local variables and the next instruction state are stored inside the continuation.

### ✅ 2. Returns to the caller immediately

This makes suspend functions **non-blocking**.

### ✅ 3. The producer schedules a resume callback

(e.g., network response, timer, I/O completion)

***

# ✅ **4. Dispatchers Decide Which Thread to Resume On**

Dispatcher is part of `CoroutineContext`.

Examples:

*   `Dispatchers.Main` → UI thread
*   `Dispatchers.IO` → I/O thread pool
*   `Dispatchers.Default` → CPU-optimized thread pool

Implementation:

*   Dispatchers use an underlying **Executor** or **ThreadPool**.
*   Coroutines are scheduled onto these pools when resumed.

***

# ✅ **5. Coroutine Scheduler**

Coroutines run on a sophisticated scheduler:

*   Based on *work‑stealing algorithm*
*   Maintains queues per worker thread
*   Executes tasks (continuations) as small “units of work”
*   Handles suspension + resume

Kotlin coroutines are much lighter than threads:

| Item      | Memory per Unit |
| --------- | --------------- |
| Thread    | \~1 MB          |
| Coroutine | \~1 KB          |

***

# ✅ **6. How `launch` Works Internally**

When you write:

```kotlin
viewModelScope.launch {
    val data = fetch()
}
```

Compiler transforms into:

*   Create a coroutine object
*   Register start state (state = 0)
*   Submit continuation to dispatcher

Conceptually:

```kotlin
dispatcher.dispatch {
    coroutine.resume(Unit)
}
```

***

# ✅ **7. How `withContext` Works Internally**

`withContext(Dispatchers.IO)` performs:

1.  Save current continuation
2.  Dispatch continuation to a different dispatcher
3.  Suspend current coroutine
4.  Resume on requested dispatcher

### Internally:

```kotlin
suspend fun <T> withContext(context: CoroutineContext, block: () -> T): T {
    suspendCoroutine { cont ->
        dispatcher.schedule {
            val result = block()
            cont.resume(result)
        }
    }
}
```

This is why:

✅ Coroutines *switch threads*  
✅ Without blocking any thread

***

# ✅ **8. How Structured Concurrency Is Enforced**

`coroutineScope {}` keeps child coroutines tied to the parent:

*   Parent waits until all children finish
*   Cancelling a parent cancels all children
*   Errors propagate upward

Internally:

*   Each coroutine has a **Job**
*   Jobs maintain a tree structure
*   Parent job entry stores children jobs
*   Cancellation propagates through this tree

***

# ✅ **9. Why Coroutines Are Lightweight**

*   No OS threads created
*   They reuse existing threads from dispatcher
*   Suspension frees the thread immediately
*   State machine stores execution state, not the stack

This allows:

✅ Thousands of coroutines  
✅ Efficient networking  
✅ No thread-blocking  
✅ Massive scalability

***

# ✅ **10. Putting It All Together (Summary Diagram)**

    launch { } 
       ↓ compile
    State machine + continuation created
       ↓ 
    Dispatcher schedules initial resume()
       ↓
    Suspend? → save state + return  
       ↓
    Async operation runs (no thread blocked)
       ↓
    When done → resumeWith()
       ↓
    Dispatcher chooses thread
       ↓
    Continue state machine from next label
       ↓
    Complete

***

# ✅ **Interview-Ready Summary**

✅ **Kotlin coroutines are implemented using:**

*   A **state machine** generated by the compiler
*   **Continuation objects** holding state & resume logic
*   **CoroutineContext** providing dispatchers and job structure
*   A **sophisticated scheduler** running on top of thread pools
*   **Non-blocking suspension**, where state is saved and thread is released
*   **Resume callbacks**, which continue execution on appropriate threads
*   **Structured concurrency**, enforced via Job hierarchy

✅ Coroutines are **not threads** — they are **tasks scheduled on threads**.

✅ Each `suspend` function is converted to **continuation-passing style** and executed in a **finite state machine**.

***

</details>

<details>
  <summary><strong>2. What is a continuation?</strong></summary>

***

# ✅ **What is a Continuation? (Simple → Deep Explanation)**

A **Continuation** is an object that represents **the rest of the work that a coroutine needs to do after a suspend point**.

Think of it as:

> ✅ A “bookmark” of a suspended coroutine  
> ✅ Captures its execution state  
> ✅ Knows how to *resume* from exactly where it left off  
> ✅ Carries coroutine context (like Dispatcher, Job, etc.)

This is the *core* building block of Kotlin coroutines.

***

# ✅ 1. **Every suspend function becomes Continuation‑Passing Style (CPS)**

The Kotlin compiler transforms:

```kotlin
suspend fun fetchData(): String
```

Into something like this:

```kotlin
fun fetchData(continuation: Continuation<String>): Any
```

This is the magic that makes “pausing and resuming” possible.

***

# ✅ 2. **What Does a Continuation Contain?**

Each continuation stores:

### ✅ **1. Program State**

*   Local variables
*   Current line of execution
*   Next state label of the state machine

### ✅ **2. Resume Logic**

The method:

```kotlin
resumeWith(result: Result<T>)
```

allows the coroutine to continue.

### ✅ **3. CoroutineContext**

Which includes:

*   Dispatcher (Main, IO, Default)
*   Job (for cancellation & structured concurrency)
*   Exception handlers

***

# ✅ 3. **Visual Intuition**

Imagine this suspend function:

```kotlin
suspend fun doWork() {
    val a = networkCall()   // suspend point
    val b = databaseCall()  // suspend point
    println(a + b)
}
```

The compiler transforms it into:

    State 0 → call networkCall → suspend  
    State 1 → call databaseCall → suspend  
    State 2 → println result → complete

The **Continuation** stores:

*   `state = 0 or 1 or 2`
*   values of `a`, `b`
*   the coroutine context
*   how to resume the next state

***

# ✅ 4. **Continuation Interface (Simplified)**

```kotlin
interface Continuation<T> {
    val context: CoroutineContext
    fun resumeWith(result: Result<T>)
}
```

***

# ✅ 5. **How Suspension Really Works Using Continuations**

When you call a `suspend` function:

1.  It **immediately returns** without blocking
2.  Saves its execution state in the continuation
3.  Registers a callback that calls `resumeWith()`
4.  When async work completes → `resumeWith()` is triggered
5.  Coroutine continues from the next state

This is why coroutines are **non-blocking**.

***

# ✅ 6. **Example: Custom suspend function using Continuation**

```kotlin
suspend fun awaitCallback(): String = suspendCoroutine { cont ->
    someAsyncApi.call(object : Callback {
        override fun onSuccess(data: String) {
            cont.resume(data)
        }
        override fun onError(e: Throwable) {
            cont.resumeWithException(e)
        }
    })
}
```

Here:

✅ `cont` is the **Continuation**  
✅ It resumes the coroutine  
✅ Without blocking the thread

***

# ✅ 7. **Continuation vs Thread**

| Feature                | Continuation | Thread |
| ---------------------- | ------------ | ------ |
| Holds execution state  | ✅            | ❌      |
| Can be paused/resumed  | ✅            | ❌      |
| Lightweight            | ✅ KBs        | ❌ MBs  |
| Requires OS scheduling | ❌            | ✅      |
| Lives in user space    | ✅            | ❌      |

Coroutines suspend using *continuations* → not threads.

***

# ✅ 8. **Real Financial App Example**

Imagine observing a **payment gateway status**:

*   Step 1 → “Initialized”
*   Step 2 → “OTP sent”
*   Step 3 → “Processing”
*   Step 4 → “Success”

Each suspend call:

```kotlin
val step1 = payment.init()
val step2 = payment.sendOtp()
val step3 = payment.submitOtp()
val step4 = payment.verify()
```

Each line becomes a continuation state.

If OTP verification takes 30 seconds:

✅ No thread is blocked  
✅ Continuation stores state  
✅ Resume occurs when the callback returns

***

# ✅ **Interview‑Ready One‑Liner Definition**

> **A Continuation is an object that stores the execution state and context of a suspended coroutine, allowing it to resume later without blocking a thread.**

***

# ✅ **Quick Summary for Interviews**

*   ✅ Compiler converts suspend functions into **state machines**
*   ✅ Continuation stores:
    *   next line to execute
    *   local variables
    *   coroutine context
*   ✅ `resumeWith()` resumes the coroutine
*   ✅ Enables **non-blocking suspension**
*   ✅ Allows thousands of coroutines to run on few threads

***

</details>

<details>
  <summary><strong>3. Why is structured concurrency important?</strong></summary>

***

# ✅ **Why Structured Concurrency Is Important**

Structured concurrency is one of the most important ideas in Kotlin coroutines.  
Its core purpose is:

> ✅ **To ensure that concurrency is predictable, leak-free, cancelable, trackable, and tied to a clear lifecycle scope.**

Without structured concurrency, coroutines would turn into unbounded background jobs — very similar to old Java threads — causing leaks, bugs, and unpredictability in complex apps like banking or insurance systems.

Let’s break it down.

***

# ✅ **1. Prevents Coroutine Leaks (Like Memory Leaks but Worse)**

In unstructured models (like threads or GlobalScope):

*   You easily create background tasks that **never stop**
*   They continue even after the user closes the screen
*   No clear ownership
*   No cancellation propagation

Structured concurrency ties coroutines to a **scope**:

*   `viewModelScope`
*   `lifecycleScope`
*   `coroutineScope { }`

➡️ When the scope ends → **all child coroutines automatically cancel**.

### Example

```kotlin
coroutineScope {
    launch { ... }  // child
    launch { ... }  // child
}  // both children must finish or be cancelled before leaving
```

This prevents:

*   Brent-Shrader background tasks
*   Listener leaks
*   Orphaned network requests
*   Zombie jobs continuing multiple screens later

Huge benefit in **mobile apps with navigation & configuration changes**.

***

# ✅ **2. Cancellation Propagates Predictably (Parent → Children and Vice Versa)**

In structured concurrency:

*   Cancel parent → all children cancel
*   If child fails → parent can auto‑cancel siblings

This gives a **tree-like structure** to concurrent tasks.

### Why is this critical?

✅ Avoids inconsistent state  
✅ Avoids partial-result bugs (e.g., only half of the API calls finished)  
✅ Ensures cleanup is deterministic

### Example Scenario in Banking App

Parallel calls:

*   Fetch latest account balance
*   Fetch offers
*   Fetch user profile

If any fails (say token expired), the whole operation should fail safely.

Structured concurrency makes that automatic.

***

# ✅ **3. Guarantees No “Fire‑And‑Forget” Coroutines**

Without structure, you can start coroutines that nobody supervises:

```kotlin
GlobalScope.launch { ... }  // dangerous!
```

With structured concurrency, every coroutine has:

*   A parent
*   A lifecycle
*   Supervision
*   Cancellation logic

This ensures **consistent lifecycle management**.

***

# ✅ **4. Helps Avoid Race Conditions & Makes Code Deterministic**

When multiple coroutines run:

*   Without structure → chaos
*   With structure → predictable and hierarchical

### Example

When implementing:

*   Transaction initiation
*   OTP handling
*   Fraud‑check validation

You want **strict ordering and supervision**, not random concurrency racing.

***

# ✅ **5. Child Coroutines Must Complete Before Scope Completes**

This ensures:

✅ No logic is accidentally skipped  
✅ No partial updates  
✅ No dangling async jobs

### Example:

```kotlin
val result = coroutineScope {
    val a = async { getBalance() }
    val b = async { getKycStatus() }
    a.await() + b.await()
}
```

The scope **won’t finish** until both tasks finish.

This prevents subtle bugs and stale UI data.

***

# ✅ **6. Error Propagation Is Clear and Well‑Defined**

In structured concurrency:

*   Errors bubble up the parent coroutine tree
*   Exception handling becomes simple
*   You don’t get “lost errors” like in unstructured reactive frameworks

This simplifies:

*   Retry logic
*   Timeout logic
*   Error UI states
*   Logging and analytics

***

# ✅ **7. Makes Code Readable and Maintainable**

Structured concurrency makes concurrent code:

*   Look sequential
*   Behave predictably
*   Be easier to reason about

This dramatically reduces cognitive load for large teams and large-scale apps.

***

# ✅ **8. It Enforces Lifecycle‑Aware Code**

In Android:

*   `viewModelScope` cancels coroutines when VM is cleared
*   `lifecycleScope` cancels coroutines when screen stops
*   Activity/Fragment destruction stops children

This is **critical** to avoid:

*   API calls running after screen is closed
*   Crashes due to calling destroyed UI
*   Data updates landing on a dead screen

***

# ✅ **Simple Analogy (Interview‑Friendly)**

> **Structured concurrency is to coroutines what structured programming is to goto.**

Before structured concurrency: chaos.  
After structured concurrency: safe, predictable, readable code.

Interviewers love this line.

***

# ✅ **Quick Interview Summary**

✅ **1. Prevents coroutine leaks** — all coroutines follow a parent scope  
✅ **2. Predictable cancellation** — parent ↔ children cancellation propagation  
✅ **3. Avoids fire‑and‑forget jobs**  
✅ **4. Avoids race conditions & partial updates**  
✅ **5. Guarantees parent waits for child coroutines**  
✅ **6. Clear error propagation**  
✅ **7. Improves code readability & maintainability**  
✅ **8. Enforces lifecycle awareness in Android (viewModelScope/lifecycleScope)**

> ✅ Structured concurrency ensures coroutines behave safely, predictably, and are tied to a clear lifecycle — preventing leaks, race conditions, and inconsistent state in complex apps.

***

</details>

<details>
  <summary><strong>4. How do you cancel a coroutine safely?</strong></summary>

***

# ✅ **How to Cancel a Coroutine Safely**

Coroutine cancellation is **cooperative** in Kotlin — meaning:

> ✅ A coroutine *must reach a suspension point* and  
> ✅ Must *check for cancellation*  
> ✅ Must clean up resources safely

Below are the mechanisms used to cancel coroutines *safely and correctly*, along with pitfalls and best practices.

***

# ✅ **1. Cancel the Coroutine Through Its Scope (Recommended)**

The **safest and cleanest** way to cancel is to cancel the *scope* that owns the coroutine.

### ✅ Example: Cancel a Job

```kotlin
val job = scope.launch {
    repeat(1000) {
        delay(100)
        println("Working...")
    }
}

// Cancel safely
job.cancel()
job.join() // Wait for cleanup
```

### Why this is safe?

*   Cancellation is propagated to child coroutines
*   Structured concurrency ensures cleanup
*   No orphan coroutines remain running

***

# ✅ **2. Always Use `finally` for Cleanup**

When a coroutine is cancelled:

*   It throws **CancellationException**
*   `finally` still runs
*   You can close resources safely

### ✅ Example

```kotlin
val job = launch {
    try {
        repeat(10) {
            delay(100)
            println("Working...")
        }
    } finally {
        println("Cleaning up resources...")
    }
}
```

***

# ✅ **3. Use `isActive` for CPU‑Bound or Tight Loops**

If the coroutine is doing **CPU-heavy work** (no `delay`, no suspend calls),  
it MUST check for cancellation manually.

### ✅ Example: Avoid freezing the thread

```kotlin
suspend fun heavyCpuTask() = coroutineScope {
    while (isActive) {
        // Heavy calculation
    }
}
```

Why?

*   Cancellation only works at **suspension points**
*   CPU loops never suspend → coroutine becomes un-cancellable
*   `isActive` lets you terminate it cooperatively

***

# ✅ **4. Use `withContext(NonCancellable)` for Critical Cleanup**

If cleanup must finish *even after cancellation*:

### ✅ Example

```kotlin
try {
    doWork()
} finally {
    withContext(NonCancellable) {
        saveAuditLog()   // must execute even if user cancels
    }
}
```

This is important for:

*   Banking transaction logs
*   Payment gateway events
*   Database cleanup
*   Analytics

***

# ✅ **5. Use Timeouts Safely**

```kotlin
withTimeout(5000) {
    performPayment()
}
```

or

```kotlin
withTimeoutOrNull(5000) {
    performPayment()
} ?: handleTimeout()
```

Timeouts automatically cancel inner coroutines.

***

# ✅ **6. Preferred Cancellation Patterns in Android**

### ✅ Cancel on lifecycle

```kotlin
lifecycleScope.launchWhenStarted { ... }
```

Cancels automatically when lifecycle stops.

### ✅ Cancel in ViewModel automatically

```kotlin
viewModelScope.launch { ... }  // cancelled when VM is cleared
```

***

# ✅ **7. Cancel Hierarchies (Structured Concurrency)**

Cancel parent → auto cancel children:

```kotlin
coroutineScope {
    val job1 = launch { ... }
    val job2 = launch { ... }

    cancel() // cancels everything inside
}
```

This prevents coroutine leaks.

***

# ✅ **8. Avoid These Dangerous Patterns**

### ❌ `GlobalScope.launch { ... }`

Not tied to any lifecycle → never cancels → leaks

### ❌ Blocking inside coroutine

e.g. `Thread.sleep()` prevents suspension → cancellation impossible

### ❌ CPU loops without checking `isActive`

Locks coroutine forever

***

# ✅ **Complete Example: Safe Cancellation in Real Banking Use Case**

### Use case: Fetching transaction status poll loop

```kotlin
val job = viewModelScope.launch {
    try {
        while (isActive) {
            val status = api.getTxnStatus()
            updateUi(status)

            if (status.isFinal()) break

            delay(1000)
        }
    } finally {
        withContext(NonCancellable) {
            api.closeSession() // must run even on cancel
        }
    }
}

// Cancel when user navigates back
job.cancel()
```

This is production‑grade safe cancellation.

***

# ✅ **Interview‑Ready Summary**

### ✅ Safe cancellation requires cooperative checks

*   Coroutines must *hit suspension points* or check `isActive`

### ✅ Use `job.cancel()` + `job.join()`

Ensures cleanup and avoids zombie coroutines.

### ✅ Always use `try/finally` for cleanup

Use `withContext(NonCancellable)` for mandatory cleanup.

### ✅ CPU loops must check `isActive`

Cancellation doesn’t interrupt threads.

### ✅ Avoid `GlobalScope`

Use structured concurrency (`viewModelScope`, `coroutineScope`, etc.)

### ✅ Timeouts are a safe auto-cancel mechanism

***

</details>

<details>
  <summary><strong>5. What happens if you use GlobalScope? Why is it discouraged?</strong></summary>

***

# ✅ **What Happens When You Use `GlobalScope`?**

`GlobalScope.launch { }` creates a coroutine that:

✅ **Lives for the entire lifetime of the application (process)**  
✅ **Is not tied to any structured concurrency scope**  
✅ **Does not get canceled automatically**  
✅ **Runs independently of UI, ViewModel, lifecycle, or business use-cases**

In other words:

> **A `GlobalScope` coroutine behaves like a detached background thread.  
> It will run until completion unless you manually cancel it.**

This breaks the fundamental principles of structured concurrency.

***

# ✅ **Why `GlobalScope` Is Discouraged (Architect‑Level Reasons)**

Below are the exact reasons interviewers expect, with real-world examples.

***

# ✅ 1. **It Breaks Structured Concurrency (The Biggest Reason)**

Coroutines in Kotlin are meant to be:

*   hierarchically structured
*   bound to a lifecycle
*   automatically cancelable
*   supervised

`GlobalScope` bypasses all of this.

### ✅ Example

```kotlin
GlobalScope.launch {
    // No parent, no lifecycle, no cancellation
}
```

This coroutine **has no parent**, so:

*   It will NOT cancel when:
    *   Activity/Fragment is destroyed
    *   User navigates away
    *   ViewModel is cleared
*   It becomes a **leaked coroutine**

***

# ✅ 2. **Causes Memory Leaks & Coroutine Leaks**

Since GlobalScope coroutines outlive screens:

### ❌ They continue holding references to:

*   ViewModel
*   Fragment
*   Activity
*   UI elements
*   Callback objects
*   Networking clients

This easily creates **memory leaks**.

***

# ✅ 3. **Creates Zombie Background Jobs**

Imagine a banking app screen:

✅ User checks transaction status  
❌ Navigates away  
✅ Screen destroyed  
❌ `GlobalScope.launch` is still running

These “zombie jobs” may:

*   Update old fragments
*   Trigger callbacks on destroyed contexts
*   Crash the app
*   Consume CPU
*   Waste network bandwidth
*   Interfere with new screens

***

# ✅ 4. **No Cancellation Propagation**

In structured concurrency:

*   Cancel parent → children auto-cancel
*   Failure in child → parent aware

In GlobalScope:

❌ No parent  
❌ No supervision  
❌ No cancellation hierarchy

So:

*   A failed child doesn’t notify anything
*   Exceptions can remain silent
*   Application state becomes inconsistent

***

# ✅ 5. **Exceptions Become Hard to Track**

A coroutine launched in GlobalScope:

*   Has no structured exception handler
*   Throws errors to the default CoroutineExceptionHandler
*   Often crashes the app
*   Sometimes silently fails

Debugging becomes extremely painful.

***

# ✅ 6. **Threading and Lifecycle Mismatch**

Android lifecycles are short‑lived:

*   A screen may be recreated many times
*   Network requests must stop when user leaves

But `GlobalScope` jobs live:

→ Until the app process dies or you manually cancel.

This creates:

❌ Activity/Fragment trying to update destroyed UI  
❌ “Called after onStop()” exceptions  
❌ Data flowing into wrong lifecycle owners  
❌ Random crashes

***

# ✅ 7. **Encourages Unstructured Concurrency (Anti‑Pattern)**

Key corruption caused by GlobalScope:

*   Code becomes difficult to reason about
*   No ownership of concurrency
*   No automatic cleanup
*   Hard to test
*   No clear boundaries

Architects typically consider this a **code smell**.

***

# ✅ 8. **Makes Testing Very Difficult**

Unit tests rely on:

*   deterministic behavior
*   scope control
*   controlled dispatchers

GlobalScope jobs:

*   Outlive test execution
*   Cause unpredictability
*   Make test results flaky
*   Continue running after test suite ends

***

# ✅ 9. **Can Drain Battery in Mobile Apps**

Because the job may:

*   Hold wakelocks
*   Keep network connections open
*   Continue infinite loops/flows
*   Handle callbacks after user leaves

This leads to power drain → terrible UX.

***

# ✅ **Good vs Bad Examples**

### ❌ Wrong (GlobalScope)

```kotlin
GlobalScope.launch {
    fetchData()
}
```

### ✅ Correct (Using Structured Concurrency)

#### UI layer

```kotlin
lifecycleScope.launch {
    fetchData()
}
```

#### ViewModel

```kotlin
viewModelScope.launch {
    fetchData()
}
```

#### Background task inside domain layer

```kotlin
coroutineScope {
    launch {
        fetchData()
    }
}
```

***

# ✅ **When GlobalScope MAY be Acceptable (Rare Cases)**

Even experts avoid it **99%** of the time.  
But the few legitimate cases:

✔ App-wide analytics event dispatcher  
✔ Long‑running background worker not tied to UI (e.g., WorkManager)  
✔ Application-level event bus  
✔ Low-level library code (rare)

Even then → the recommended solution is **SupervisorScope**, not GlobalScope.

***

# ✅ **Interview‑Ready Summary**

✅ `GlobalScope` creates **coroutines that live for the whole app lifetime**  
✅ Not tied to Activity/Fragment/ViewModel lifecycle  
✅ Breaks **structured concurrency principles**  
✅ Causes **memory leaks, coroutine leaks, zombie tasks**  
✅ No automatic cancellation  
✅ Unsupervised → exceptions propagate unpredictably  
✅ Makes testing hard  
✅ Can cause stale UI updates and crashes

> ✅ **Using GlobalScope is discouraged because it bypasses the entire structured concurrency model, creating unpredictable, unbounded, and leak‑prone concurrency.**

***

</details>

---

### ANR Causes and Prevention

<details>
  <summary><strong>1. What causes ANRs and how do you prevent them?</strong></summary>

***

# ✅ **What Causes ANRs and How Do You Prevent Them?**

An **ANR (Application Not Responding)** occurs when your app **blocks the main thread (UI thread)** for too long.

Android expects the UI thread to stay **responsive** to:

*   touch input
*   UI rendering
*   system broadcasts
*   lifecycle events

When it does not respond in time, the OS shows an ANR dialog.

***

# ✅ **1. Core Causes of ANRs**

## ✅ **Cause #1 — Long Operations on the Main Thread**

Main reason for ANRs.

Examples:

*   Large DB queries
*   Network calls (HTTP, sockets)
*   File I/O (reading PDFs, CSV, images)
*   Bitmap decoding
*   Heavy JSON/XML parsing
*   Long loops, complex calculations

### Trigger thresholds:

*   **InputEvent timeout:** \~5 seconds
*   **BroadcastReceiver timeout:** \~10 seconds (foreground), 60s (background)
*   **Service timeout:** 20–200 seconds depending on type

***

## ✅ **Cause #2 — Main Thread Lock Contention (Synchronized Locks)**

If a background thread holds a lock needed by the main thread:

```kotlin
synchronized(lock) { /* long work */ }
```

…then the UI thread waits → ANR.

Common in:

*   Multi-threaded DB access
*   Incorrect singleton locks
*   Bad shared preferences usage
*   Over-synchronization in model layers

***

## ✅ **Cause #3 — Slow BroadcastReceivers**

A BroadcastReceiver MUST finish quickly.

Bad:

```kotlin
override fun onReceive() {
    api.fetchCustomerData() // long!
}
```

Receivers have strict time limits → slow work = ANR.

***

## ✅ **Cause #4 — Slow Lifecycle Callbacks**

Running heavy work in:

*   `onCreate()`
*   `onStart()`
*   `onResume()`
*   `onViewCreated()`

…blocks rendering → leads to ANR + jank.

***

## ✅ **Cause #5 — Deadlocks**

Circular waiting:

Thread A → waits for Thread B  
Thread B → waits for Thread A

If UI thread is in this cycle → ANR.

***

## ✅ **Cause #6 — Infinite Loops / Busy Loops on UI Thread**

```kotlin
while(true) { }   // UI freezes → ANR
```

***

## ✅ **Cause #7 — Disk I/O on UI Thread**

Examples:

*   Writing logs
*   Large SharedPreferences writes
*   Room DB operations w/o suspend functions
*   Reading large assets or files

***

# ✅ **2. How to Prevent ANRs (Best Practices)**

Below are the strategies expected in an architect interview.

***

## ✅ **Prevention #1 — Keep UI Thread Clean**

The golden rule:

> ✅ **Never block the main thread. Ever.**

Use coroutines:

✅ Correct:

```kotlin
viewModelScope.launch(Dispatchers.IO) {
    val result = repository.fetchData()
    withContext(Dispatchers.Main) {
        render(result)
    }
}
```

❌ Wrong:

```kotlin
val result = repository.fetchData() // blocks main thread
```

***

## ✅ **Prevention #2 — Use Coroutines/WorkManager for Heavy Work**

### Long-running → WorkManager

### Short asynchronous work → Coroutines

### Parallel operations → async/await

### Streams → Flow

Examples of heavy operations to move off main:

*   API calls
*   JSON parsing
*   DB queries
*   Cryptographic operations (very common in banking apps)
*   PDF generation
*   Image transformations

***

## ✅ **Prevention #3 — Avoid Blocking Calls Entirely**

Avoid:

*   `Thread.sleep()`
*   `runBlocking` on the main thread
*   Large synchronized blocks
*   Blocking I/O calls
*   Writing huge SharedPreferences values

***

## ✅ **Prevention #4 — Use Structured Concurrency**

Scopes prevent “zombie jobs” that block UI unpredictably.

```kotlin
viewModelScope.launch {
    // Safe
}
```

Avoid `GlobalScope` which can cause unexpected UI interactions & ANRs.

***

## ✅ **Prevention #5 — Offload Heavy Work from BroadcastReceivers**

Use WorkManager inside receiver.

```kotlin
override fun onReceive(context: Context?, intent: Intent?) {
    WorkManager.getInstance(context!!)
        .enqueue(OneTimeWorkRequest.from(MyWorker::class.java))
}
```

***

## ✅ **Prevention #6 — Use Paging and Lazy Loading**

For rendering large lists:

*   Pagination
*   DiffUtil
*   LazyColumn (Compose)

Large lists rendered at once → UI stalls → ANR.

***

## ✅ **Prevention #7 — Avoid Bitmap Operations on UI Thread**

Decode bitmaps on background threads:

```kotlin
withContext(Dispatchers.IO) {
    BitmapFactory.decodeFile(path)
}
```

***

## ✅ **Prevention #8 — Use StrictMode in Debug Builds**

StrictMode detects accidental main thread blocking.

```kotlin
StrictMode.setThreadPolicy(
    StrictMode.ThreadPolicy.Builder()
        .detectAll()
        .penaltyLog()
        .build()
)
```

***

# ✅ **3. Real Banking App Examples**

### Scenario 1: Fetching Account Summary

❌ Doing heavy parsing on UI thread → ANR  
✅ Use coroutines + suspend API

### Scenario 2: PDF statement generation

❌ Done in UI thread → freeze  
✅ Use `Dispatchers.Default`

### Scenario 3: Payment Status Polling Loop

❌ Infinite loop on main thread  
✅ Use `viewModelScope + delay()`

### Scenario 4: Large SharedPreferences write after login

❌ Blocks UI  
✅ Use DataStore (asynchronous, safe)

***

# ✅ **4. Interview‑Ready Summary**

### ✅ Causes of ANRs

*   Main thread blocked > 5 seconds
*   Long DB/network/file operations
*   Slow BroadcastReceivers
*   Heavy work in lifecycle methods
*   Deadlocks / synchronized overuse
*   Infinite loops / blocking calls

### ✅ Prevention

*   Keep main thread free
*   Use coroutines + Dispatchers.IO/Default
*   Offload heavy work to WorkManager / background threads
*   Avoid blocking calls (`Thread.sleep`, long sync blocks)
*   Avoid disk I/O on UI thread
*   Use StrictMode to detect slow operations
*   Optimize RecyclerViews, layout inflation
*   Use DataStore instead of heavy SharedPreferences writes

***

</details>

<details>
  <summary><strong>2. How do you debug ANRs?</strong></summary>

***

# ✅ **How to Debug ANRs (Application Not Responding)**

Debugging ANRs requires **analyzing what blocked the main thread**, identifying bottlenecks, and tracing thread contention. Here’s a systematic, production-ready approach used by senior engineers.

***

# ✅ **1. Check ANR Reports (`anr_traces.txt`)**

The very first place to look is:

    /data/anr/traces.txt

### ✅ How to get it quickly:

*   Android Studio → Device File Explorer
*   Run:

```bash
adb pull /data/anr/traces.txt
```

### ✅ What to look for:

*   **Main thread stack trace** — the UI thread will be at the top.
*   Identify:
    *   Network call?
    *   Disk I/O?
    *   Synchronized block?
    *   Deadlock?
    *   Heavy computation?

### Example snippet:

    "main" prio=5 tid=1 Native
      at java.io.FileInputStream.read(...)
      at com.yourapp.loadLargeJson() // BAD: heavy work on main

If the main thread is "WAITING" or "BLOCKED", it’s likely a **deadlock** or **lock contention**.

***

# ✅ **2. Use Android Studio’s “App Not Responding” Checker (Modern Tool)**

Android Studio automatically dumps:

*   Main-thread call stack
*   Frozen thread details
*   Timing info

### Steps:

✅ Run app →  
✅ Trigger freeze →  
✅ Studio shows **ANR dialog** with call stacks.

This is extremely helpful for debugging UI‑blocking patterns.

***

# ✅ **3. Analyze Main Thread with “Dump Threads”**

Use:

```bash
adb shell kill -3 <pid>
```

OR:

Android Studio → **Run → Dump Java Threads**

Look at:

*   `main` thread → What is it blocked on?
*   Any long disk/network calls?
*   Any `synchronized` locks?

Great for catching:

*   Deadlocks
*   Blocked I/O
*   Stuck broadcast receivers

***

# ✅ **4. Use StrictMode to Catch Issues Early**

StrictMode helps catch:

*   Disk I/O on main
*   Network calls on main
*   Slow method calls

In `Application.onCreate()`:

```kotlin
StrictMode.setThreadPolicy(
    StrictMode.ThreadPolicy.Builder()
        .detectAll()
        .penaltyLog()
        .build()
)
```

This flags:

❌ DB calls  
❌ File access  
❌ SharedPreferences writes  
❌ Bitmap decoding

on main thread.

***

# ✅ **5. Use Systrace / Perfetto for Frame Rendering Analysis**

Systrace (Perfetto) helps you see:

*   UI thread blocking
*   Choreographer frames skipping
*   Layout inflation times
*   Disk I/O
*   Long GC pauses

### Steps:

Android Studio →  
✅ **Profiler** →  
✅ **CPU** →  
✅ **Record Trace (Perfetto)**

Look for:

*   Long purple blocks = GC
*   Long green blocks = layout
*   Long red blocks = blocking calls

Great for diagnosing:

*   Slow rendering
*   Overdraw
*   RecyclerView bottlenecks
*   Long-running Choreographer tasks

***

# ✅ **6. Inspect Logs for BroadcastReceiver or Service Timeouts**

Look for logs like:

    I/ActivityManager: ANR in com.myapp
    Reason: Broadcast of Intent { ... }

OR

    Reason: executing service com.myapp/.MyService

This tells you:

*   A receiver took >10s
*   A service took too long

Solution: Offload heavy work to WorkManager or coroutines on `Dispatchers.IO`.

***

# ✅ **7. Detect Deadlocks with Thread States**

If stack traces show:

*   MAIN thread → `BLOCKED`
*   Background thread → holding lock

It’s a deadlock or lock contention.

Example:

    main: waiting on lock X
    IO-thread: waiting on lock Y

Fix:

*   Avoid nested synchronized
*   Use `Mutex` with coroutines instead of Java locks

***

# ✅ **8. Use “Profileable=true” For Production ANR Debugging**

Add in manifest:

```xml
<application android:profileable="true">
    <!-- -->
</application>
```

Now you can profile production builds:

✅ CPU  
✅ Memory  
✅ Perfetto traces

…without dev-mode restrictions.

***

# ✅ **9. Monitor for Jank + Long Frames in Choreographer**

Use:

```bash
adb shell dumpsys gfxinfo your.package
```

This shows:

*   Long frames
*   Skipped frames
*   Rendering bottlenecks

If you see frames >16ms consistently → something is stalling UI thread.

***

# ✅ **10. Use Play Console ANR Reports (Production)**

Google Play Console → Android Vitals → **ANRs**

You get:

*   Stack traces
*   Device conditions
*   Android version breakdown
*   Frequency
*   Sessions affected

You can identify patterns:

*   Only low RAM devices?
*   Only Android 13?
*   Only in a specific flow?

***

# ✅ **11. Debug MainThread stalls caused by GC**

Long GC pauses show up in logs:

    I/zygote: GC_FOR_ALLOC exceeded time

To debug:

*   Enable memory profiler
*   Look for large objects
*   Reduce bitmap allocations
*   Avoid frequent String building

***

# ✅ **12. Tools to Catch ANRs Before Release**

✅ StrictMode  
✅ ANRWatchDog library  
✅ Perfetto traces  
✅ Memory profiler  
✅ CPU profiler  
✅ Lint checks for expensive work in lifecycle methods  
✅ Frame rendering monitor

***

# ✅ **Interview‑Ready Summary (Use This in 1 Minute)**

**How to debug ANRs:**

✅ Check **traces.txt** to inspect main-thread block  
✅ Capture **thread dump** with `adb kill -3`  
✅ Use **Android Studio ANR Inspector**  
✅ Profiling using **Perfetto/Systrace** to detect long frames  
✅ Use **StrictMode** to detect blocking I/O  
✅ Inspect **logs for BroadcastReceiver/Service timeouts**  
✅ Identify **deadlocks or lock contention**  
✅ Use **Google Play Console ANR reports** for real production data  
✅ Check for **GC pauses**, large allocations, or layout bottlenecks  
✅ Check for heavy work in **onCreate/onResume** or in UI rendering

**Root causes you’ll find:**

*   Long I/O on main
*   Heavy layout inflation
*   Deadlocks
*   Synchronized blocks
*   Too much work in BroadcastReceiver
*   Infinite or heavy loops

***

</details>

<details>
  <summary><strong>3. How do you measure main-thread workload?</strong></summary>

***

# ✅ **How to Measure Main-Thread Workload (Deep Technical Answer)**

Measuring main‑thread workload means **quantifying what is running on the UI thread**, how long it takes, and *why* it stalls. Here are all reliable methods used by senior Android engineers.

***

# ✅ **1. Use Android Studio Profiler — Main Thread CPU Timeline**

Android Studio → **Profiler → CPU Profiler** → select **Main Thread**.

You can measure:

*   Execution time of each function
*   Blocked (red) segments
*   GC pauses
*   Choreographer timing
*   Long‑running handlers, Runnables, or UI operations

### Why it’s accurate?

It captures **actual sampled CPU usage**, showing long-running frames and stuck operations.

***

# ✅ **2. Use Perfetto / Systrace (Most Accurate Method)**

### Perfetto (modern Systrace) gives:

*   Choreographer frame boundaries
*   VSYNC timing
*   UI thread execution slices
*   Main-thread handler processing
*   Input latency
*   Rendering pipeline stages

Perfetto link in Studio:  
**Profiler → System Trace → Record Trace**

This is the industry‑standard method.

You’ll see:

    UI Thread:
     |---doFrame 16ms---|
     |---layout 8ms---|
     |---------draw 30ms---------|

Anything >16ms per frame → jank  
Anything >5 seconds → ANR territory

***

# ✅ **3. Count UI Thread Frame Durations (Choreographer / FrameMetrics)**

Use **`FrameMetricsAggregator`** (API 24+) to measure:

*   Total number of slow frames
*   Frames that exceeded 16ms, 32ms, 48ms
*   Drop/jitter

### Example (Activity):

```kotlin
val aggregator = FrameMetricsAggregator()
aggregator.add(activity)

// Later:
val metrics = aggregator.metrics[FrameMetricsAggregator.TOTAL_INDEX]
```

This tells you if main thread workload is too heavy during rendering.

***

# ✅ **4. Enable StrictMode (Debug Build Only)**

StrictMode can show **when you accidentally touch disk/network on main thread**.

```kotlin
StrictMode.setThreadPolicy(
    StrictMode.ThreadPolicy.Builder()
        .detectAll()
        .penaltyLog()
        .build()
)
```

It logs:

*   DiskReads
*   DiskWrites
*   Long operations (>\~5ms)
*   Network on main

This is key for catching main‑thread misuse.

***

# ✅ **5. Use Choreographer Callback to Measure Frame Time**

Hook into every frame:

```kotlin
Choreographer.getInstance().postFrameCallback { frameTimeNanos ->
    // measure frame delta
}
```

You can compute:

    frameDurationMs = (frameTimeNanos - lastFrameTimeNanos) / 1_000_000

If many frames >16ms → heavy main‑thread work.

***

# ✅ **6. Use `Looper` Observer (API 23+) to Measure Message Processing Time**

Android exposes **Looper message logging**:

```kotlin
Looper.getMainLooper().setMessageLogging { log ->
    Log.d("Looper", log)
}
```

Pattern:

*   `>>>>> Dispatching to ...`
*   `<<<<< Finished to ...`

You can measure differences with timestamps.

Useful for detecting:

*   Slow click handlers
*   Long UI computations
*   Heavy RecyclerView binders

***

# ✅ **7. Use `Debug.startMethodTracing()` for Very Deep Analysis**

Trace Java/Kotlin function calls on main thread:

```kotlin
Debug.startMethodTracing("main_perf")
doUiWork()
Debug.stopMethodTracing()
```

Opens in Studio as trace file → shows:

*   Method duration
*   Call hierarchy
*   Inclusive/exclusive CPU time

This is a *microscope* view of main thread behavior.

***

# ✅ **8. Use “Profileable=true” for Production Main-Thread Metrics**

In manifest:

```xml
<application android:profileable="true" />
```

This allows capturing:

*   Perfetto traces
*   CPU timeline
*   Memory allocs

**directly from production devices** without debug build overheads.

***

# ✅ **9. Use ANRWatchDog for Detecting When Main Thread Freezes**

In debug builds:

```kotlin
ANRWatchDog().start()
```

It triggers logs when main thread stalls for N ms.

***

# ✅ **10. Measure Workload with Main Thread Dispatchers**

If coroutines accidentally run on main thread:

```kotlin
Dispatchers.Main.immediate
```

You can intercept:

```kotlin
Dispatchers.Main.immediate.limitedParallelism(1)
```

to measure and constrain main thread jobs.

***

# ✅ **11. Monitor Rendering Stats with `adb shell dumpsys gfxinfo`**

Command:

    adb shell dumpsys gfxinfo <package_name>

Shows:

*   Total frames
*   Slow frames
*   Janky frames
*   90th, 95th, 99th percentile frame times

This is crucial for real-world UI performance.

***

# ✅ **Where Main Thread Workload Actually Comes From (Root Causes)**

Typical heavy tasks that block UI thread:

✅ Bitmap decoding  
✅ JSON parsing  
✅ Room DB operations without `suspend`  
✅ Heavy RecyclerView binds  
✅ Complex layouts / nested constraints  
✅ SharedPreferences commit  
✅ Large file I/O  
✅ Payment SDK callbacks on main  
✅ Synchronized locks blocking UI

High main-thread workload = low FPS + jank + ANRs.

***

# ✅ **Interview-Ready Summary**

### ✅ **How to measure main-thread workload:**

*   **CPU Profiler** → measure main-thread CPU slices
*   **Perfetto/Systrace** → authoritative rendering + thread workload tool
*   **FrameMetricsAggregator** → measure janky frames
*   **StrictMode** → detect disk/network on main
*   **Thread dump (`kill -3`)** → check where main thread is stuck
*   **Choreographer callbacks** → measure frame duration
*   **Looper logging** → detect long message handlers
*   **Method tracing** → micro-level analysis
*   **dumpsys gfxinfo** → production rendering stats
*   **Profileable=true** → capture real production traces

### ✅ **What you're measuring:**

*   Frame times
*   Long UI handlers
*   Heavy rendering phases
*   Blocking calls
*   Thread contention
*   GC pauses

***

</details>

<details>
  <summary><strong>4. How do coroutines help prevent ANR?</strong></summary>

***

# ✅ **How Coroutines Help Prevent ANRs**

ANRs happen when the **main thread is blocked** for too long.  
Coroutines help avoid this by making asynchronous work:

✅ Easy  
✅ Safe  
✅ Structured  
✅ Non‑blocking  
✅ Thread‑offloading friendly

Below is the complete breakdown.

***

# ✅ 1. **Coroutines Never Block the Main Thread (They Suspend Instead)**

When you call a `suspend` function, the coroutine **does NOT block** the thread.  
It *pauses* execution and frees the main thread to continue rendering UI.

### Example (Safe)

```kotlin
val result = withContext(Dispatchers.IO) {
    api.getData() // long work off main
}
```

Here’s what happens:

*   Main thread launches coroutine
*   Coroutine suspends at `withContext(Dispatchers.IO)`
*   Thread is released instantly
*   Work continues on a background thread
*   Main thread never freezes

✅ This is the **core reason** coroutines prevent ANRs.

***

# ✅ 2. **Coroutines Make It Easy to Move Heavy Work Off the Main Thread**

Almost all ANRs are caused by:

*   DB operations
*   Network calls
*   JSON parsing
*   File I/O
*   Bitmap decoding
*   Crypto operations

With coroutines, moving work off the main thread is almost trivial:

```kotlin
withContext(Dispatchers.IO) {
    heavyOperation()
}
```

This reduces the risk of accidentally blocking UI.

***

# ✅ 3. **Structured Concurrency Ensures Jobs Don’t Outlive the UI**

ANR patterns often come from:

*   orphan background jobs still updating UI
*   jobs continuing after the screen is destroyed
*   long loops continuing after user navigates away

Coroutines use **structured concurrency** to prevent that:

*   `viewModelScope` → cancels when ViewModel is cleared
*   `lifecycleScope` → cancels when screen stops
*   Parent coroutines cancel all children

### Example (Safe)

```kotlin
viewModelScope.launch {
    val users = repository.loadUsers()
    _state.value = users
}
```

This coroutine automatically cancels when the ViewModel dies → no ANRs from zombie jobs.

***

# ✅ 4. **Coroutines Provide Built‑in Timeouts to Prevent Long Blocking**

Long operations can freeze the app if not timed out.

Coroutines solve this:

```kotlin
withTimeout(3000) {
    fetchData()
}
```

If the operation blocks or hangs:

✅ coroutine cancels  
✅ UI remains responsive  
✅ no ANR

***

# ✅ 5. **Coroutines Enforce Cooperative Cancellation**

Blocking patterns like infinite loops cause ANRs.

Coroutines encourage safe loops:

```kotlin
while (isActive) {
    // poll or update
    delay(1000)
}
```

`delay()` is a suspension point → never blocks main thread.

✅ Prevents infinite-loop ANRs  
✅ Prevents CPU starvation scenarios

***

# ✅ 6. **Dispatchers Prevent Wrong Thread Execution**

`Dispatchers.Main`  
`Dispatchers.IO`  
`Dispatchers.Default`

Coroutines *explicitly control* where code runs.

If heavy work accidentally tries to run on main, you can catch it:

```kotlin
Dispatchers.Main.immediate
```

Or leverage `StrictMode` with coroutines to detect blocking on main.

***

# ✅ 7. **Suspend Functions Replace Blocking Calls**

Instead of:

❌ `Thread.sleep(1000)` → blocks main → ANR  
✅ `delay(1000)` → suspends → no ANR

Instead of:

❌ Network/DB call on main thread  
✅ `suspend` Network/DB call using OkHttp + Room

Suspend functions free up the main thread every time they pause.

***

# ✅ 8. **Coroutines Reduce Callback Hell (Which Often Causes Main-Thread Mistakes)**

Callback hell leads to:

*   UI updates inside deep callbacks
*   Blocking calls in event handlers
*   Mistaken synchronous code on main

Coroutines turn callbacks into clean, asynchronous code:

```kotlin
val data = api.getData() // suspend, non-blocking
```

Cleaner code → fewer mistakes → fewer ANRs.

***

# ✅ **Real Banking Example**

### ✅ Dangerous (Likely to ANR)

```kotlin
fun checkBalance() {
    val response = api.getBalanceSync() // blocking
    balanceText.text = response.balance.toString()
}
```

### ✅ Safe (Coroutine-Based)

```kotlin
viewModelScope.launch {
    val balance = withContext(Dispatchers.IO) { api.getBalance() }
    balanceText.text = balance.toString()
}
```

Now:

*   API call happens on IO thread
*   UI updates happen on main
*   Zero ANR risk

***

# ✅ **Interview‑Ready Summary**

### ✅ Coroutines prevent ANRs because they:

1.  **Suspend instead of block** (core principle)
2.  **Offload heavy work to background threads** (Dispatchers.IO/Default)
3.  **Provide structured concurrency**
4.  **Automatically cancel child jobs** (avoid zombie jobs)
5.  **Provide timeouts for long operations**
6.  **Support cooperative cancellation** (`isActive`, `delay`)
7.  **Replace blocking APIs with non-blocking suspending functions**
8.  **Prevent callback and threading mistakes** (clean async code)

✅ **Main line for interview:**

> “Coroutines prevent ANRs by ensuring the main thread never blocks.  
> Suspend functions free the main thread, structured concurrency prevents runaway tasks, and Dispatchers ensure heavy work runs off the UI thread.”

***

</details>

<details>
  <summary><strong>5. Provide an example of subtle ANR scenarios (e.g., slow BroadcastReceiver).</strong></summary>

***

# ✅ **Subtle ANR Scenarios (Common but Hard to Spot)**

These scenarios don’t look “dangerous,” but they **silently block the main thread** long enough to cause ANRs.

***

# ✅ **1. Slow BroadcastReceiver (VERY Common Subtle ANR)**

BroadcastReceivers have strict execution deadlines:

*   **Foreground app:** must finish within \~10 seconds
*   **Background:** \~60 seconds

But even tiny operations can push them over the limit.

### ❌ Dangerous example:

```kotlin
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Developer thinks: "just merging prefs, should be fine"
        val prefs = context.getSharedPreferences("big_prefs", MODE_PRIVATE)
        val data = prefs.all                    // heavy read!
        sendDataToServer(data)                  // network on main?!
    }
}
```

Why this causes ANR (subtle):  
✅ `SharedPreferences.all` reads entire XML into memory  
✅ Has disk I/O under the hood  
✅ Runs on **main thread**  
✅ Inside a strict **BroadcastReceiver timeout window**

**Fix:** Offload work

```kotlin
override fun onReceive(context: Context, intent: Intent) {
    WorkManager.getInstance(context)
        .enqueue(OneTimeWorkRequest.from(SyncWorker::class.java))
}
```

***

# ✅ **2. Heavy Work in `onCreate()` / `onStart()` (Invisible ANR Trigger)**

Developers assume startup logic is “light,” but even a 300ms freeze here contributes to UI unresponsiveness.

### ❌ Example:

```kotlin
override fun onCreate() {
    super.onCreate()
    val json = assets.open("large_config.json").readBytes() // disk read
    parse(json)                                             // heavy CPU
}
```

This causes:  
✅ UI not drawn in time  
✅ User perceives app as frozen  
✅ If total exceeds 5 seconds → ANR

**Fix:**  
Move work off main using `lifecycleScope.launch(Dispatchers.IO)`.

***

# ✅ **3. Hidden Disk I/O in SharedPreferences Commit**

`SharedPreferences.commit()` is **synchronous disk write** on main thread.

### ❌ Subtle example:

```kotlin
prefs.edit().putString("token", hugeToken).commit()
```

Symptoms:

*   UI freezes subtly
*   If called repeatedly (login, navigation…), can cause ANRs

**Fix:**  
Use `apply()` (async) or prefer **DataStore**.

***

# ✅ **4. Synchronized Blocks That Randomly Block Main Thread (Lock Contention)**

Main thread waiting for a lock held by a background thread → ANR, but it’s subtle.

### ❌ Example:

```kotlin
val lock = Any()

fun updateOnMain() {
    synchronized(lock) {       // blocks if lock is currently in use
        updateUi()
    }
}

fun heavyWorkInBackground() = thread {
    synchronized(lock) {
        Thread.sleep(3000)    // simulating DB, network, parsing
    }
}
```

Main thread tries to acquire lock but background thread holds it →  
✅ Main thread blocked  
✅ ANR if it exceeds threshold

Very common in fintech apps involving synchronized caches.

**Fix:** Use `Mutex` (coroutine-friendly).

***

# ✅ **5. Expensive Logging on Main Thread**

Logging large objects, JSON payloads, or stack traces can easily stall UI.

### ❌ Example:

```kotlin
Log.d("APP", Gson().toJson(largeObject))  // serialization on main
```

This is subtle because:

*   Logging seems harmless
*   But JSON serialization can take 200–500ms
*   If inside loops → ANR

**Fix:** Serialize on background thread.

***

# ✅ **6. Slow RecyclerView Binding or DiffUtil**

Even small inefficiencies repeated hundreds of times cause UI stalls.

### ❌ Example:

```kotlin
override fun onBindViewHolder(holder, position) {
    holder.title.text = loadFromDB(items[position].id) // DB on main
}
```

Or slow DiffUtil:

```kotlin
DiffUtil.calculateDiff(callback)  // running on main!
```

**Fix:**  
Run DB lookups and DiffUtil on background threads.

***

# ✅ **7. Long Work in `Service.onStartCommand()`**

Foreground services still run on the **main thread** unless manually moved.

### ❌ Example:

```kotlin
override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    processLargeFile()   // expensive, blocks main
    return START_NOT_STICKY
}
```

If service start takes too long → ANR.

***

# ✅ **8. Bitmap Decoding on Main Thread (Extremely Common)**

Even decoding a medium-size image can take >100ms.

### ❌ Example:

```kotlin
val bmp = BitmapFactory.decodeFile(path) // heavy
imageView.setImageBitmap(bmp)
```

**Fix:** Decode with `Dispatchers.IO` or Glide/Picasso/Coil.

***

# ✅ **9. UI Thread Blocked by Large Layout Inflation**

Deeply nested views or massive XML inflate can create frame drops → ANR.

### ❌ Example:

```kotlin
setContentView(R.layout.really_large_layout)
```

Large constraint chains and heavy custom views stall `onCreate()`.

**Fix:**

*   Use **ViewStub**
*   Defer inflation
*   Use modular layouts
*   Prefer Compose for large dynamic UIs

***

# ✅ **10. Deadlock Between Main Thread & Background Thread**

Classic subtle ANR scenario.

### ❌ Example:

Main thread:

```kotlin
synchronized(lock) { fetchUiData() }
```

Background thread:

```kotlin
synchronized(lock) { fetchNetworkData() }
```

Two threads wait on each other → deadlock → ANR.

***

# ✅ **Interview-Ready 1‑Minute Summary**

**Subtle ANR scenarios occur when:**

✅ Disk I/O happens secretly on main (SharedPreferences, assets, file read)  
✅ CPU-heavy work happens inside lifecycle methods  
✅ BroadcastReceivers perform slow work  
✅ Locks create thread contention with main thread  
✅ Logging, JSON parsing, bitmap decoding happen on main  
✅ RecyclerView binds or DiffUtil runs on main  
✅ Services do heavy work on main thread

**Key insight:**

> “Most subtle ANRs come from operations developers *think* are lightweight but actually cause main-thread stalls — disk I/O, lock contention, parsing, inflation, and synchronous SDK calls.”

***

</details>

---

### Android Activity

<details>
  <summary><strong>1. Explain Activity & process death handling.</strong></summary>

***

# ✅ **Activity & Process Death Handling (Android Deep Dive)**

Android may **kill your Activity or entire app process** at any moment when resources are tight.  
Your job as an architect is to ensure:

✅ The app **restores state gracefully**  
✅ No crashes  
✅ No user data loss  
✅ No corrupted UI or flows

This is especially critical in **banking/insurance apps** because incomplete flows = **failed transactions, compliance issues, or inconsistent UX**.

***

# ✅ **1. Why Does Android Kill Activities / Processes?**

Android kills components when:

### ✅ **1. Low memory (most common)**

System kills background processes to free RAM.

### ✅ **2. App goes to background**

Activity is stopped and can be recreated later.

### ✅ **3. Configuration changes**

*   Rotation
*   Locale change
*   Font size change
*   Multi-window  
    These trigger **Activity recreation** (not process death).

### ✅ **4. Background process eviction**

If your app is in the background and system needs memory → it kills the entire **process**.

***

# ✅ **2. Activity Recreation vs Process Death**

| Event                     | Activity Destroyed | Process Killed | Data Lost?                   | Typical Scenario           |
| ------------------------- | ------------------ | -------------- | ---------------------------- | -------------------------- |
| **Rotation**              | ✅ Yes              | ❌ No           | ❌ No if state restored       | User rotates screen        |
| **Low memory**            | ✅ Yes              | ✅ Yes          | ✅ Yes unless you saved state | System cleanup             |
| **Back stack recreation** | ✅ Yes              | ✅ Maybe        | ✅ Maybe                      | App in background for long |

***

# ✅ **3. How Android Helps You Restore State**

### ✅ A. **Saved Instance State**

`onSaveInstanceState()` → receives a **Bundle**  
`onCreate(savedInstanceState)` → restores the Bundle

Automatically saved by Android:

*   EditText text
*   Scroll position
*   Fragment stacks (if using FragmentManager)

Store small, UI-related, serializable things.

### ✅ Example:

```kotlin
override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putString("amount", enteredAmount)
}
```

Restore:

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val amount = savedInstanceState?.getString("amount")
}
```

***

# ✅ **4. Process Death: How to Detect and Recover Properly**

When process is killed:

*   ViewModel is gone
*   Repository in-memory cache is gone
*   Coroutine scopes are gone
*   Singleton objects reset
*   Dependency injection graph resets

### ✅ How to detect process death?

Inside a Fragment/Activity:

```kotlin
if (savedInstanceState != null) {
    // process death happened
}
```

ViewModel also gives a clue:  
If it’s a **fresh ViewModel** on return from background → process died.

***

# ✅ **5. Use ViewModel + SavedStateHandle to Survive Process Death**

`SavedStateHandle` automatically persists key data across **both**:

✅ Activity recreation  
✅ Process death

### ✅ Example ViewModel:

```kotlin
class PaymentViewModel(
    private val state: SavedStateHandle
) : ViewModel() {

    var amount: String
        get() = state["amount"] ?: ""
        set(value) { state["amount"] = value }
}
```

Even after process death → amount is restored magically.

***

# ✅ **6. What You Should Persist Where**

| Data Type                                    | Where to Store         | Why                         |
| -------------------------------------------- | ---------------------- | --------------------------- |
| UI fields (text, scroll)                     | SavedInstanceState     | Lightweight, UI-specific    |
| Temporary flow data (amount, selected items) | SavedStateHandle       | Survives process death      |
| User/session data                            | DataStore / SharedPref | Needed across app restarts  |
| Big objects                                  | DB                     | Not safe to store in Bundle |
| Network results                              | Repository + DB cache  | Avoid refetch on restore    |

***

# ✅ **7. Real‑World Banking App Scenario**

### ✅ Example Flow: Money Transfer

Steps:

1.  Enter amount
2.  Select beneficiary
3.  Choose payment mode
4.  Enter remark
5.  Confirm

If process dies between steps → user must **not lose progress**.

✅ Save step-level state in ViewModel + SavedStateHandle  
✅ Persist partially completed form fields  
✅ Cache beneficiary & accounts data in Room  
✅ On restore:

```kotlin
if (savedInstanceState != null) {
    resumeTransferFlow()
} else {
    startFreshFlow()
}
```

This prevents:

*   Double debits
*   Incorrect OTP flows
*   Confusing restarts

***

# ✅ **8. Things That Are Lost on Process Death**

Unless you save them:

❌ Coroutine scopes  
❌ LiveData/StateFlow internal state  
❌ ViewModel data (unless using SavedStateHandle)  
❌ Singleton caches (because process memory wiped)  
❌ Repository in-memory caches  
❌ Dependency graph instances

Be prepared to rebuild all of these.

***

# ✅ **9. Testing Activity & Process Death Handling**

Best way:

Run in terminal:

```bash
adb shell am kill <package>
```

Or enable “Don’t Keep Activities” in Developer options.

***

# ✅ **10. Best Practices (Architect‑Level)**

✅ Always save UI state in `onSaveInstanceState()`  
✅ Keep business logic & state in ViewModel + SavedStateHandle  
✅ Cache network responses in Room  
✅ Never store big objects in Bundle (limit \~1MB)  
✅ Ensure every ViewModel can recreate state after process death  
✅ Stateless screens should always be re-derivable from DB/Repository

***

# ✅ **Interview‑Ready Summary**

✅ **Activity recreation** (rotation) vs **process death** (low memory)  
✅ Use **onSaveInstanceState** for UI-only small data  
✅ Use **ViewModel + SavedStateHandle** for process-death survival  
✅ Persist essential user data in **DataStore/Room**  
✅ Detect process death via `savedInstanceState != null`  
✅ Recreate repositories, ViewModels, caches on restart  
✅ Test using “Don’t Keep Activities” or adb kill command

**One‑liner summary (perfect for interviews):**

> *“Activity death is normal during config changes.  
> Process death happens under low memory.  
> To handle both, use ViewModel + SavedStateHandle + proper persistence like DataStore/Room, and restore UI via onSaveInstanceState().”*

***

</details>

<details>
  <summary><strong>2. How does SavedStateHandle work?</strong></summary>

***

# ✅ **How `SavedStateHandle` Works (Deep Explanation)**

`SavedStateHandle` is a Jetpack component that gives your **ViewModel** a built‑in mechanism to **save and restore state** across:

✅ Activity/Fragment **recreation** (e.g., rotation)  
✅ **Process death** (low memory kills app)  
✅ Navigation component state restoration  
✅ System‑initiated state restoration

It acts like a **lifecycle-aware key-value store** tied to your ViewModel.

***

# ✅ **1. Big Picture: What Problem Does `SavedStateHandle` Solve?**

Normally:

*   ViewModel survives rotation, but **NOT process death**
*   `onSaveInstanceState()` saves small UI data but belongs to Activity/Fragment
*   Workflows like payments, onboarding, form entry need state survival in ViewModel

`SavedStateHandle` bridges this gap:

> ✅ It allows a ViewModel to survive process death by saving its state into `SavedStateRegistry` under the hood.

***

# ✅ **2. How It Works Internally**

When a ViewModel is created using `by viewModels()` with a `SavedStateViewModelFactory` (automatically done if using Hilt or default code), Android:

### ✅ Step 1 — Creates a `SavedStateHandle`

Reads any previously saved keys from:

*   `savedInstanceState`
*   `SavedStateRegistry`
*   Navigation arguments (if using Jetpack Nav)

### ✅ Step 2 — Injects this `SavedStateHandle` into your ViewModel constructor

```kotlin
class MyViewModel(private val state: SavedStateHandle) : ViewModel()
```

### ✅ Step 3 — When you write values:

```kotlin
state["amount"] = "500"
```

It stores them in an internal map that is:

*   Immediately available in memory
*   Persisted to the **SavedStateRegistry** at the next opportunity

### ✅ Step 4 — If process death occurs

On next creation:

*   Android restores the saved state bundle
*   `SavedStateHandle` reconstructs itself
*   ViewModel receives the restored values automatically

✅ No manual work needed  
✅ No need to write Bundle boilerplate  
✅ ViewModel now survives process death

***

# ✅ **3. What Does SavedStateHandle Persist?**

`SavedStateHandle` supports:

*   `Int`, `Long`, `String`, `Boolean`
*   `Parcelable`
*   `Serializable`
*   Arrays of above types
*   `List<T>` if T is Parcelable

❌ Avoid large objects  
❌ Avoid bitmaps, large JSON  
❌ Avoid full Room entities (only IDs recommended)

***

# ✅ **4. SavedStateHandle Is Observable**

It exposes:

### ✅ LiveData

```kotlin
val amount: LiveData<String> = state.getLiveData("amount")
```

### ✅ StateFlow (modern)

```kotlin
val amountFlow = state.getStateFlow("amount", "")
```

This makes it perfect for MVVM + Jetpack Compose + Flow.

***

# ✅ **5. End-to-End Example (Process Death Safe)**

### ✅ ViewModel:

```kotlin
class PaymentViewModel(
    private val state: SavedStateHandle
) : ViewModel() {

    var amount: String
        get() = state["amount"] ?: ""
        set(value) {
            state["amount"] = value
        }

    val selectedBeneficiary = state.getStateFlow("beneficiary", "")
}
```

### ✅ Process death scenario:

1.  User enters amount
2.  App goes to background
3.  System kills process
4.  User returns
5.  Android recreates Activity + ViewModel
6.  `SavedStateHandle` restores:
    *   amount
    *   beneficiary
    *   other saved values

Result:

✅ User continues from same place  
✅ No lost state  
✅ No crashed ViewModel  
✅ No inconsistent data flows

***

# ✅ **6. Integration With Jetpack Navigation**

Jetpack Navigation automatically puts:

*   Nav arguments → into `SavedStateHandle`
*   State restoration → via `SavedStateRegistry`

This means:

```kotlin
val userId = savedStateHandle.get<String>("userId")
```

works automatically when navigating with:

```kotlin
bundleOf("userId" to "123")
```

***

# ✅ **7. Relationship Between SavedStateHandle & SavedStateRegistry**

`SavedStateRegistry` lives inside:

✅ Activity  
✅ Fragment

It stores each key/value pair as part of:

    onSaveInstanceState(Bundle out)

`SavedStateHandle` registers itself with this registry, so when:

*   Activity saves state → it writes SavedStateHandle internal map
*   Activity recreates → it restores SavedStateHandle map

This creates an automated persistence loop.

***

# ✅ **8. When NOT to Use SavedStateHandle**

❌ Large objects  
❌ Complex entities  
❌ Long-term persistent storage (use DataStore/Room instead)  
❌ Lists with more than a few KB of data  
❌ Caches or ephemeral UI data

SavedStateHandle = small, critical, session‑level UI state.

***

# ✅ **9. Interview‑Ready Summary**

✅ `SavedStateHandle` stores & restores ViewModel state automatically  
✅ Survives Activity recreation *and* process death  
✅ Uses SavedStateRegistry internally  
✅ Works with LiveData, Flow, Compose  
✅ Perfect for form steps, wizards, money transfer flows, onboarding  
✅ Should store small, serializable, essential UI state  
✅ Do NOT store large objects or heavy data

**One-liner answer:**

> **SavedStateHandle is a lifecycle-aware key–value store inside ViewModel that integrates with the system's SavedStateRegistry. It preserves essential UI state across Activity recreation and process death without requiring manual Bundle handling.**

***

</details>

<details>
  <summary><strong>3. How do you ensure state persistence with Flow or Compose?</strong></summary>

***

Here we'll cover – How to ensure state persistence when using Flow or Jetpack Compose**:

✅ State persistence across recomposition  
✅ State persistence across configuration change  
✅ State persistence across process death  
✅ Best practices for Flow + Compose + ViewModel

***

# ✅ **How to Ensure State Persistence with Flow or Compose**

Jetpack Compose + Flow is powerful, but if you don't persist the state correctly, you can easily lose UI state after:

*   recomposition
*   configuration change (rotation, locale change)
*   app going to background
*   process death (low memory kill)

Below are all strategies and their recommended usage.

***

# ✅ **1. Persist State Across Recomposition (Compose-only)**

Use **remember** to keep state alive during recomposition:

```kotlin
val username = remember { mutableStateOf("") }
```

But **remember ≠ configuration change safe**  
When configuration changes, state disappears.

***

# ✅ **2. Persist State Across Configuration Change (Compose)**

Use:

```kotlin
val username = rememberSaveable { mutableStateOf("") }
```

✅ Survives recomposition  
✅ Survives configuration change  
❌ Does NOT survive process death fully for custom types unless Parcelable/Saver is provided

Compose internally uses:

*   SavedInstanceState
*   Bundle Savers
*   Parcelable Savers

***

# ✅ **3. Persist State in ViewModel Using Flow (Most Common)**

Use ViewModel + StateFlow:

```kotlin
class LoginViewModel : ViewModel() {
    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()
}
```

In Compose:

```kotlin
val username by viewModel.username.collectAsState()
```

This survives:

✅ recomposition  
✅ navigation  
✅ configuration changes  
❌ process death (unless combined with SavedStateHandle)

***

# ✅ **4. Persist State Across Process Death (Flow + SavedStateHandle)**

This is the **Android‑recommended** architecture.

```kotlin
class PaymentViewModel(
    private val state: SavedStateHandle
) : ViewModel() {

    val amount = state.getStateFlow("amount", "")
    
    fun setAmount(value: String) {
        state["amount"] = value      // survives process death
    }
}
```

In Compose:

```kotlin
val amount by viewModel.amount.collectAsState()
```

✅ survives recomposition  
✅ survives configuration change  
✅ survives process death

This is the **best practice for multi-step flows** (loan applications, payments, onboarding).

***

# ✅ **5. Persist State Using DataStore (Permanent Persistence)**

If state must survive:

*   app kill
*   device reboot
*   user returning days later

use **DataStore + Flow**.

Example:

```kotlin
val tokenFlow = dataStore.data.map { it[USER_TOKEN] ?: "" }
```

Compose:

```kotlin
val token by tokenFlow.collectAsState(initial = "")
```

✅ persistent  
✅ async  
✅ coroutine-friendly  
❌ not suitable for volatile UI values (e.g., scroll position)

***

# ✅ **6. Persist State Using Room + Flow**

For large state/data that must persist permanently:

```kotlin
@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getUser(): Flow<User>
}
```

Compose:

```kotlin
val user by userDao.getUser().collectAsState(initial = null)
```

Ideal for:

*   Beneficiary list
*   Profile details
*   Dashboard data
*   Saved quotes

***

# ✅ **7. Persist Expanded/Collapsed UI State with Saver**

If you have a custom object:

```kotlin
val uiState = rememberSaveable(
    saver = Saver(
        save = { it.toBundle() },
        restore = { UiState.fromBundle(it) }
    )
) { UiState() }
```

This allows Compose to persist complex UI objects.

***

# ✅ **8. Persist Navigation State (NavController + SavedStateHandle)**

Jetpack Navigation automatically stores:

*   arguments
*   back stack entries
*   SavedStateHandle per destination

This ensures navigation-driven flows can survive process death.

***

# ✅ **9. Persist Scroll State in Compose**

Compose provides:

```kotlin
val scrollState = rememberSaveable { ScrollState(0) }
```

Or for LazyColumn:

```kotlin
val listState = rememberLazyListState()
```

**LazyListState automatically saves and restores** scroll position.

***

# ✅ **10. Combine ViewModel + StateFlow + rememberSaveable (Best Complete Solution)**

In real apps:

*   ViewModel stores long-lived state
*   SavedStateHandle stores survival state
*   Compose stores local UI-only state

Example:

```kotlin
@Composable
fun PaymentScreen(vmodel: PaymentViewModel = viewModel()) {

    val amount by vmodel.amount.collectAsState()

    val otpField = rememberSaveable { mutableStateOf("") }

    PaymentUi(
        amount = amount,
        otp = otpField.value
    )
}
```

Breakdown:

✅ `amount` survives process death (SavedStateHandle)  
✅ `otpField` survives rotation only (rememberSaveable)  
✅ Flow + Compose remain lifecycle-aware

***

# ✅ **When to Use What (Interview Summary)**

| Use Case                            | Solution                          |
| ----------------------------------- | --------------------------------- |
| Avoid losing state on recomposition | `remember`                        |
| Survive configuration change        | `rememberSaveable`                |
| Keep screen state in MVVM           | `ViewModel + StateFlow`           |
| Survive process death               | `SavedStateHandle.getStateFlow()` |
| Permanent data                      | DataStore (Flow) / Room (Flow)    |
| Custom complex UI state             | Custom `Saver`                    |
| Nave state survival                 | Navigation + SavedStateHandle     |

***

# ✅ **Interview‑Ready One-Liner Answer**

> **Use `rememberSaveable` for UI local state, `StateFlow` in ViewModel for screen-level reactive state, and `SavedStateHandle` for process‑death-safe state. For permanent data, use DataStore or Room with Flow. Compose reads them safely via `collectAsState()`.**

***

</details>

<details>
  <summary><strong>4. What happens when ViewModel is recreated after process death?</strong></summary>

***

# ✅ **What Happens When ViewModel Is Recreated After Process Death**

When Android kills the **entire app process** (usually due to low memory) and the user later returns to the app, your **ViewModel is NOT restored** — it is **recreated from scratch.**  
Everything in memory is gone.

But Android *does* provide mechanisms to rebuild your ViewModel state, *if you use them correctly*.

Let’s walk through this step-by-step.

***

# ✅ **1. Entire Process Memory Is Gone**

After process death:

❌ ViewModel instances are gone  
❌ ViewModelStore is gone  
❌ LiveData/StateFlow values are lost  
❌ Your Repository in-memory caches are lost  
❌ Singleton objects are recreated  
❌ DI container (Dagger/Hilt) is rebuilt  
❌ All coroutine scopes are cancelled

The app starts with a **fresh process**.

***

# ✅ **2. Activity Is Restarted with `savedInstanceState` Bundle**

When the user returns:

1.  Android recreates the Activity
2.  It supplies the **last saved** `Bundle`:

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    // savedInstanceState != null → process death happened
}
```

This Bundle comes from `onSaveInstanceState()` before the process was killed.

***

# ✅ **3. ViewModel Is Constructed Again (Fresh Instance)**

But now there are **two scenarios** depending on whether you use `SavedStateHandle`.

***

# ✅ ✅ Scenario A — You DO NOT Use SavedStateHandle

(ViewModel is recreated **empty**)

If your ViewModel looks like this:

```kotlin
class PaymentViewModel : ViewModel() {
    var amount = ""
}
```

After process death:

✅ ViewModel is recreated  
❌ `amount` is reset to default  
❌ You lose all state unless stored elsewhere

Your screen sees an empty ViewModel → user loses progress.

***

# ✅ ✅ Scenario B — You DO Use SavedStateHandle

(ViewModel is recreated **with restored state**)

When your ViewModel has:

```kotlin
class PaymentViewModel(
    private val state: SavedStateHandle
) : ViewModel() {
    val amount = state.getStateFlow("amount", "")
}
```

Then Android restores:

✅ SavedStateHandle internal keys  
✅ All values saved before process death  
✅ Your ViewModel starts with previously stored state

How?

### The workflow:

**Before death:**

*   ViewModel writes data into SavedStateHandle
*   SavedStateHandle writes into SavedStateRegistry
*   SavedStateRegistry writes into `savedInstanceState`

**After death:**

*   Activity gets `savedInstanceState`
*   SavedStateHandle is recreated using that Bundle
*   ViewModel receives restored SavedStateHandle
*   UI restores smoothly

✅ User returns exactly where they left off  
✅ No data loss  
✅ No inconsistent flows

***

# ✅ **4. Navigation Component Also Restores Back Stack**

If using Jetpack Navigation:

*   Back stack entries are restored
*   Each destination gets its own SavedStateHandle
*   Arguments + ViewModel state revive automatically

This allows multi-step flows (e.g., loan onboarding, KYC, payments) to be restored.

***

# ✅ **5. Repository Layer After Process Death**

If repositories use:

❌ in-memory caches → lost  
✅ Room/Database → fully preserved  
✅ DataStore → fully preserved

This is why repository architecture should be persistent-first.

***

# ✅ **6. Coroutine Behavior After Process Death**

All running coroutines:

*   Stop immediately
*   Never resume
*   Cannot be recovered

New ViewModel → new coroutine scopes → fresh execution of operations.

***

# ✅ **7. Real Banking App Example**

### Scenario: User is doing a money transfer

Steps:

1.  Enter amount
2.  Select beneficiary
3.  Enter remarks
4.  Go to confirmation
5.  Background process kills app

✅ With SavedStateHandle

*   Amount restored
*   Selected beneficiary restored
*   Remarks restored
*   Screen continues where user left

❌ Without SavedStateHandle

*   Everything resets
*   User restarts the flow
*   Terrible experience

***

# ✅ **8. How to Detect Process Death in ViewModel**

Inside Activity:

```kotlin
if (savedInstanceState != null) {
    // process death happened
}
```

Inside ViewModel you cannot directly detect it —  
You rely on SavedStateHandle values being present or not.

***

# ✅ **Interview‑Ready Summary**

✅ After process death:

*   ViewModel is recreated fresh
*   All in-memory data is lost
*   But Android restores `savedInstanceState` Bundle

✅ If you use SavedStateHandle:

*   State is automatically restored into ViewModel
*   You survive process death
*   UI continues from previous state

✅ If you do NOT use SavedStateHandle:

*   ViewModel loses everything
*   User loses progress
*   App restarts at initial screen

✅ Repository persistence (Room/DataStore) becomes critical

✅ Coroutines do NOT survive process death; new ViewModel = new scope

***

# ✅ **One-Liner Answer (Perfect for Interview)**

> **After process death, ViewModel is recreated fresh, but if it uses SavedStateHandle, Android restores the previously saved state into it from SavedStateRegistry. All in-memory data and coroutines are lost, so true persistence requires SavedStateHandle + Room/DataStore.**

***

</details>

---

### WorkManager

<details>
  <summary><strong>1. Explain WorkManager internals and when NOT to use it.</strong></summary>

***

# ✅ **WorkManager Internals (Deep Dive)**

WorkManager is Android’s **guaranteed, persistent, deferrable background work scheduler**.  
It is built on multiple layers of fallback mechanisms and integrates tightly with OS background restrictions.

***

# ✅ **1. High-Level Architecture**

WorkManager has a **3‑tier scheduling architecture**:

    App code → WorkManager API → Scheduler → OS/DB → Worker Execution

Schedulers WorkManager may use:

1.  ✅ **JobScheduler** (API 23+) → preferred
2.  ✅ **AlarmManager + BroadcastReceiver** (fallback for API < 23)
3.  ✅ **Immediate execution via Executor** when conditions allow

WorkManager ALWAYS uses a **SQLite database** internally to track work state.

***

# ✅ **2. Internal Components**

### ✅ **A. WorkRequest**

Represents a unit of work.

Types:

*   OneTimeWorkRequest
*   PeriodicWorkRequest

Contains:

*   Constraints
*   InputData
*   Backoff criteria
*   Tags
*   Unique name

***

### ✅ **B. WorkSpec (Stored in DB)**

When you enqueue a WorkRequest:

1.  It gets converted into a **WorkSpec**
2.  WorkSpec is inserted into WorkManager’s SQLite DB
3.  DB persists it even after process death, reboot, ANR, or crash

WorkSpec tracks:

*   state (ENQUEUED, RUNNING, SUCCEEDED, FAILED, BLOCKED, CANCELLED)
*   constraints (network/battery/storage)
*   scheduling metadata
*   backoff policies
*   chain dependencies

***

### ✅ **C. Scheduler**

WorkManager uses schedulers to trigger work execution:

1.  **GreedyScheduler**
    *   Runs inside the app process
    *   Executes work immediately if constraints are met
    *   Restarts when app is relaunched

2.  **SystemJobScheduler**
    *   Wraps Android’s JobScheduler
    *   Used for persistent OS-level scheduling

3.  **GcmScheduler** (legacy)
    *   Only if Google Play services available (now deprecated)

***

### ✅ **D. WorkManager Database**

SQLite tables:

*   `WorkSpec`
*   `Dependency` (for chaining: A → B → C)
*   `WorkTag`
*   `WorkName`
*   `Preference`
*   `WorkProgress`

WorkManager wakes up, reads DB entries, and decides what to run.

***

### ✅ **E. Worker Execution**

WorkManager uses an internal `TaskExecutor` or a custom Executor to:

*   spin up a background thread
*   run `doWork()`
*   post results back to scheduler

Work continues **even if the app is killed**, because JobScheduler or AlarmManager will re-trigger execution.

***

# ✅ **3. How WorkManager Handles Constraints**

WorkManager monitors:

*   Network
*   Battery not low
*   Storage not low
*   Device idle
*   Charging state

Internally:

*   A shared `ConstraintsTracker` observes OS broadcasts
*   When constraints are satisfied → WorkSpec transitions ENQUEUED → RUNNING
*   If constraints break mid-work → Worker is stopped and will retry

***

# ✅ **4. How Chaining Works Internally**

When you do:

```kotlin
workA.then(workB).then(workC)
```

WorkManager inserts this into DB:

    Dependency: B depends on A
    Dependency: C depends on B

WorkSpec only becomes RUNNABLE when all its prerequisites are SUCCEEDED.

***

# ✅ **5. How Retry & Backoff Works**

WorkSpec defines:

*   backoff policy (LINEAR or EXPONENTIAL)
*   backoff delay (min 10 seconds)

On failure:

*   Worker returns `Result.retry()`
*   WorkManager updates `schedule_time` in DB
*   Scheduler re‑enqueues work

***

# ✅ **6. How WorkManager Survives Reboots**

If you use:

```kotlin
setRequiresDeviceIdle()
```

or

```kotlin
setRequiresCharging()
```

WorkManager automatically adds the required `BOOT_COMPLETED` configuration internally.

WorkSpec remains in DB → rescheduled after reboot.

***

# ✅ **7. What Happens on Process Death**

When app process is killed:

✅ WorkSpec DB persists  
✅ JobScheduler jobs persist  
✅ Pending work is rescheduled when app restarts or when constraints meet

This is the biggest strength of WorkManager.

***

# ✅ **8. When WorkManager Executes Work Immediately**

If constraints are met and app is in foreground:

*   GreedyScheduler runs work inline
*   No JobScheduler involved
*   Faster execution

***

# ✅ **When NOT to Use WorkManager (Highly Important Interview Question)**

WorkManager is **NOT for everything**. Here are the DO NOT USE cases:

***

# 🚫 **1. NOT for real-time tasks**

You should NOT use WorkManager when you need:

*   immediate execution
*   low-latency response
*   instant user-triggered actions

Examples:

❌ Send analytics event right now  
❌ Perform UI‑related tasks  
❌ Submit payment transaction instantly  
❌ Trigger navigation or UI callback

Use: Coroutines + ViewModel + Dispatchers.IO

***

# 🚫 **2. NOT for foreground tasks tied to the UI**

WorkManager is background-only.  
If the task is tied to UI lifecycle → do NOT use WorkManager.

Examples:

❌ Loading images  
❌ Fetching a screen's data  
❌ Upload while user is on the screen

Use: Coroutines + lifecycleScope

***

# 🚫 **3. NOT for high-frequency or continuous tasks**

WorkManager is rate-limited.

Do NOT use it for:

❌ Polling every few seconds  
❌ Real-time stock/price tick updates  
❌ Sensor events  
❌ Heartbeat pings

Use: Coroutines + Flows + recurring suspend functions

***

# 🚫 **4. NOT for tasks requiring exact timing**

WorkManager cannot guarantee exact execution time.

Example:

❌ Schedule something exactly at 5:00 PM  
❌ Trigger OTP expiration countdown  
❌ Execute a timeout precisely

Use: AlarmManager (for exact alarms) or Timer/Coroutine

***

# 🚫 **5. NOT for foreground services that require user-visible action**

If your task must show a persistent notification:

❌ Do not use WorkManager with `setForeground()` as a hack  
✅ Use actual **ForegroundService**

Examples:

*   Music playback
*   Location tracking
*   Uploading large file with progress UI
*   Long-running VPN connections

***

# 🚫 **6. NOT for short network calls that must run immediately**

WorkManager adds overhead:

*   DB write
*   Scheduler evaluation
*   OS-level scheduling

Not suitable for short-lived tasks like:

❌ “Fetch profile data”  
❌ “Search query”

Use: Retrofit + Coroutines

***

# ✅ **When You SHOULD Use WorkManager**

✅ Guaranteed background execution  
✅ Deferrable tasks  
✅ Must run even after app restart/reboot  
✅ Must survive process death  
✅ Constraints-based work  
✅ Chained background workflows  
✅ Periodic background sync  
✅ Offline-first app syncing  
✅ Log upload retry  
✅ Sensitive background operations (backup, sync, analytics batching)

***

# ✅ **Interview‑Ready Summary**

### ✅ Internals

*   WorkManager stores work in a **SQLite DB**
*   Uses **GreedyScheduler** + **JobScheduler** + **AlarmManager** as fallback
*   Tracks constraints using `ConstraintsTracker`
*   Uses background **Executor** to run Workers
*   WorkSpec defines scheduling, backoff, constraints
*   Survives **process death** and **device reboot**

### ✅ When NOT to use WorkManager

*   ❌ Real-time tasks
*   ❌ UI-related work
*   ❌ High-frequency jobs
*   ❌ Exact timers
*   ❌ Foreground service behavior
*   ❌ Immediate actions
*   ❌ Short-lived network calls
*   ❌ App lifecycle-bound tasks

### ✅ When to use WorkManager

*   ✅ Guaranteed background execution
*   ✅ Persistent + deferrable work
*   ✅ Background sync, upload retry, logs, backups

***

</details>

<details>
  <summary><strong>2. Difference between constraints vs retry policies</strong></summary>

***

# ✅ **Constraints vs Retry Policies — Core Difference**

Think of it as:

> ✅ **Constraints = WHEN work is allowed to run**  
> ✅ **Retry Policy = WHAT to do if work fails**

They solve **completely different problems** inside WorkManager.

***

# ✅ **1. What Are Constraints?**

Constraints define **environment conditions** that must be true **before WorkManager is allowed to start the work**.

### ✅ Examples:

*   Network must be available
*   Device must be charging
*   Device must be idle
*   Storage is not low
*   Battery is not low

### ✅ Example Code

```kotlin
val constraints = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.CONNECTED)
    .setRequiresCharging(true)
    .build()

val work = OneTimeWorkRequestBuilder<UploadWorker>()
    .setConstraints(constraints)
    .build()
```

### ✅ What Really Happens Internally

*   WorkManager stores constraints in its DB (WorkSpec).
*   A **ConstraintsTracker** monitors system conditions.
*   Work **won’t run** until *all constraints* are satisfied.
*   If constraints become false while running → Worker **stops** and will retry later.

### ✅ Key point:

**Constraints determine eligibility before starting.**

***

# ✅ **2. What Are Retry Policies?**

Retry policies define **how WorkManager should behave when the Worker returns a failure** that is retryable.

### ✅ Worker's return values:

```kotlin
Result.success()
Result.failure()
Result.retry()
```

### ✅ Example Retry Policy:

```kotlin
val work = OneTimeWorkRequestBuilder<SyncWorker>()
    .setBackoffCriteria(
        BackoffPolicy.EXPONENTIAL,
        20, TimeUnit.SECONDS
    )
    .build()
```

### ✅ What Happens Internally:

*   Worker fails → WorkManager checks its backoff policy.
*   Computes next run time using:
    *   LINEAR backoff
    *   OR EXPONENTIAL backoff
*   Schedules retry via JobScheduler or AlarmManager.

### ✅ Key point:

**Retry policies determine behavior AFTER a controlled failure.**

***

# ✅ **3. Side-by-Side Comparison**

| Feature                                 | Constraints                              | Retry Policy                                   |
| --------------------------------------- | ---------------------------------------- | ---------------------------------------------- |
| Purpose                                 | Define when worker is allowed to run     | Define what to do when worker fails            |
| Applies Before/After Execution          | ✅ Before                                 | ✅ After                                        |
| Triggered by                            | Environment (network, charging, storage) | Worker failure (`Result.retry()`)              |
| Example Rule                            | “Only run when connected to Wi-Fi”       | “Retry with exponential backoff after failure” |
| If condition not met                    | Work waits                               | Work is rescheduled                            |
| Work stops if condition breaks mid-way? | ✅ Yes                                    | ❌ No                                           |
| Stored in DB?                           | ✅ Yes                                    | ✅ Yes                                          |
| System Component                        | ConstraintsTracker                       | BackoffCalculator                              |

***

# ✅ **4. Real-World Examples**

## ✅ Example A — Payment receipt upload

Conditions:

*   Needs Internet
*   Must retry if server temporarily down

✅ Constraints:

```kotlin
requiresNetworkType = CONNECTED
```

✅ Retry Policy:

```kotlin
Result.retry() with EXPO backoff
```

Meaning:

*   Work only runs when Internet is available
*   If server returns 500 → retry later

***

## ✅ Example B — Large backup job

Conditions:

*   Should run only when device is charging
*   If job fails midway, retry with delay

✅ Constraints:

```kotlin
setRequiresCharging(true)
```

✅ Retry:

```kotlin
setBackoffCriteria(BackoffPolicy.LINEAR, 30s)
```

***

## ✅ Example C — Don’t retry when constraints fail

Important: **Constraint failure ≠ Retry**.

If network drops mid-way:

*   WorkManager *stops* work
*   Reschedules it when constraints return to “true”
*   **RetryPolicy does NOT apply**

RetryPolicy only applies when the Worker **returns `Result.retry()`**.

***

# ✅ **5. Trick Interview Question**

**Q: What happens first — constraint check or retry policy?**  
✅ Constraint check **always comes first.**

Sequence:

    1. Constraints satisfied → Worker starts
    2. Worker runs
    3. Worker fails → RetryPolicy applies
    4. Before retry → Constraints must be satisfied again

***

# ✅ **6. Interview‑Ready One-Liner Summary**

✅ **Constraints decide *when* WorkManager may run a task.  
✅ Retry policies decide *how and when* to re-run a task after failure.**

Constraints must be satisfied **before** starting work.  
Retry policies only activate **after** a worker returns `Result.retry()`.

***

</details>

<details>
  <summary><strong>3. Why WorkManager for deferrable tasks only?</strong></summary>

***

# ✅ **Why WorkManager Is for *Deferrable Tasks Only***

WorkManager is **not** a general-purpose background execution engine. It is built specifically for:

✅ Guaranteed execution  
✅ Deferrable work (not time‑critical)  
✅ Work that must survive:

*   App restarts
*   Process death
*   Device reboot  
    ✅ Work that may be delayed based on system conditions

The keyword: **deferrable** means “can be postponed without breaking functionality.”

Below is the deep reasoning.

***

# ✅ **1. WorkManager Uses OS JobScheduler (Which Is Deferred by Design)**

Under the hood, WorkManager uses:

*   **JobScheduler** (API 23+)
*   **AlarmManager** (fallback)
*   **Internal Executor** (when app is in foreground)

And `JobScheduler` **never guarantees immediate execution**—  
Android can delay jobs for:

*   battery optimization
*   Doze mode
*   App Standby Buckets
*   network availability
*   power saver
*   OEM throttling

Therefore:

> ✅ **WorkManager cannot be used for tasks that must run immediately**  
> ❌ **It is designed to run when the system decides it’s appropriate**

This is why WorkManager = **deferred background job scheduler**.

***

# ✅ **2. WorkManager Respects Constraints — Which Might Take Minutes/Hours**

If you set constraints like:

```kotlin
.setRequiresCharging(true)
.setRequiredNetworkType(UNMETERED)
```

WorkManager waits until **ALL constraints** are satisfied.

This may take:

*   hours
*   overnight
*   days (e.g., unmetered WiFi)

So WorkManager cannot be used for urgent/real-time work.

***

# ✅ **3. System Background Restrictions Force Delayed Execution**

Modern Android aggressively restricts background execution:

*   Doze mode
*   Battery Saver
*   App Standby
*   Background Execution Limits
*   OEM custom throttling

WorkManager works *within* these limits → execution may be postponed.

### Example:

If battery is low → Work won’t run.  
If network is off → Work won’t run.  
If device is idle → Work may be delayed.

WorkManager **does not bypass OS restrictions**.

***

# ✅ **4. WorkManager's Internal Scheduling Introduces Natural Delay**

Even without constraints:

*   WorkRequest is written to DB
*   Scheduler looks for eligible tasks
*   JobScheduler batching kicks in
*   Work executed asynchronously by system

This means:

✅ Work runs *eventually*  
❌ Not immediately

Execution latency can be seconds or minutes.

***

# ✅ **5. Guaranteed Execution Requires Persistence → Not Suitable for Immediate Work**

To guarantee execution, WorkManager must:

*   write WorkSpec to SQLite DB
*   enqueue it through schedulers
*   apply backoff
*   recheck constraints

This overhead means **it is too slow for immediate tasks** like:

❌ making API calls triggered by button click  
❌ updating UI  
❌ real‑time streaming  
❌ instant upload/download  
❌ quick retry with low latency

Use **Coroutines + lifecycleScope/viewModelScope** instead.

***

# ✅ **6. WorkManager is Ideal for “Run Eventually, Just Don’t Lose It” Tasks**

This is why it’s perfect for:

✅ Sync logs  
✅ Upload analytics  
✅ Retry failed uploads  
✅ Backups  
✅ Offline-first synchronization  
✅ Cleanup or maintenance tasks  
✅ Background task that must survive reboot

These tasks aren’t urgent → but must **eventually** complete.

***

# ✅ **7. Android Architecture Guidelines: WorkManager ≠ Real-Time Work**

Google’s official guidance:

> **Use WorkManager only for deferrable, guaranteed background tasks.  
> Do NOT use it for immediate execution.**

Because Android's background model fundamentally prevents predictable timing.

***

# ✅ **8. WorkManager Has Minimum Intervals (e.g., 15 min for Periodic Work)**

PeriodicWorkRequest minimum period:

    15 minutes

That alone disqualifies it from real-time or frequent tasks.

***

# ✅ **9. WorkManager Will Pause Your Work Mid-Execution**

If constraints become false:

*   Work stops
*   Work reschedules
*   Will run later

This makes it unsuitable for long-running tasks requiring uninterrupted execution.

***

# ✅ **10. WorkManager Is Optimized for System-Friendly, Battery-Friendly Tasks**

Its entire design goal:

✅ Reduce battery drain  
✅ Avoid waking device aggressively  
✅ Reuse JobScheduler batching  
✅ Cooperate with OS scheduling

This is inherently a **deferred** model.

***

# ✅ **Interview‑Ready Summary**

**Why WorkManager for deferrable tasks only?**

✅ WorkManager uses JobScheduler, which delays tasks  
✅ Must obey system background restrictions (Doze, battery saver)  
✅ Constraints (charging, WiFi, idle mode) can take hours to satisfy  
✅ Internal scheduling adds execution latency  
✅ Designed for guaranteed execution, not immediate execution  
✅ Periodic work minimum = 15 minutes  
✅ Work may stop and reschedule mid-execution

**Therefore:**

> **WorkManager is only reliable for tasks that can run later, not tasks that must run now.**

***

</details>

<details>
  <summary><strong>4. When would you use ForegroundService instead?</strong></summary>

***

# ✅ **When Would You Use a ForegroundService Instead of WorkManager?**

ForegroundService is meant for **tasks that must run immediately AND must keep running even when the app is in the background**, while showing a **persistent notification** to the user.

WorkManager = deferrable, background, guaranteed  
ForegroundService = immediate, long‑running, user-visible

Below are the exact scenarios where **ForegroundService is the right tool** (and WorkManager is wrong).

***

# ✅ **1. Tasks That Must Start *Immediately***

WorkManager is never guaranteed to start instantly.  
ForegroundService **runs as soon as you call startForegroundService()**.

### ✅ Use ForegroundService when:

*   You need immediate execution
*   Task cannot be delayed by Doze mode or battery saver
*   User explicitly triggers the action now

### Examples:

*   Start uploading a file right now
*   Begin a VoIP call
*   Start GPS tracking immediately

WorkManager might delay these tasks for minutes → unacceptable.

***

# ✅ **2. Long‑Running Tasks That Must Not Be Killed**

ForegroundServices have elevated priority.  
The OS is far less likely to kill them.

### ✅ Use when:

*   Work must continue uninterrupted
*   The system must **not** stop the task mid-way

Examples:

*   Media playback
*   File upload/download with progress
*   VPN connection
*   Bluetooth operations
*   Navigation turn-by-turn tracking

WorkManager will pause/stop work if constraints change.  
ForegroundService will not.

***

# ✅ **3. User‑Visible Ongoing Tasks (Mandatory Notification)**

ForegroundService requires a **persistent notification**.  
This is ideal when the user must be aware of the task.

### ✅ When required:

*   “Transferring file…”
*   “Recording location…”
*   “Sync in progress…”
*   “Uploading photos…”

If the user must know the task is happening → use ForegroundService.

***

# ✅ **4. Tasks Requiring Control UI (Cancel / Pause / Resume)**

ForegroundService notifications can have actions:

*   Cancel
*   Pause
*   Resume
*   Retry

WorkManager doesn't allow interactive background control.

Example:

*   Music controls
*   Upload pause/resume
*   Ongoing call control
*   Download manager UI

***

# ✅ **5. High‑Priority, Latency‑Sensitive Work**

ForegroundServices bypass:

*   JobScheduler delays
*   App Standby bucket throttling
*   Doze batching
*   Network‑optimizing delays

Use for:

*   VoIP incoming call ringing
*   Geo-fenced location actions
*   Emergency alerts
*   Medical device data streaming

If the task must be **real-time**, WorkManager is not acceptable.

***

# ✅ **6. Continuous Tasks (Minutes or Hours)**

WorkManager is NOT designed for:

*   continuous loops
*   long-running processing
*   persistent monitoring

ForegroundService is.

Examples:

*   Continuous heart-rate sensor reading
*   Continuous GPS tracking
*   Live fitness tracking session
*   Call recording
*   Step tracking

***

# ✅ **7. Tasks That Must Run Even If User Minimizes the App**

ForegroundService continues until stopped.  
WorkManager may pause or wait for constraints.

Example:

*   Uploading 2GB backup
*   Recording audio
*   VPN service
*   Streaming service

User expectation: "It should continue even if I open another app."

***

# ✅ **Side-by-Side Comparison**

| Use Case                         | WorkManager      | ForegroundService |
| -------------------------------- | ---------------- | ----------------- |
| Immediate start                  | ❌ Not guaranteed | ✅ Guaranteed      |
| Long-running task                | ❌ Not reliable   | ✅ Reliable        |
| Requires persistent notification | ❌ Not possible   | ✅ Mandatory       |
| User-visible operation           | ❌ No             | ✅ Yes             |
| Real-time action                 | ❌ No             | ✅ Yes             |
| Respect system constraints       | ✅ Yes            | ✅ But prioritized |
| Must survive process death       | ✅ Yes            | ✅ Yes             |
| Suitable for hours-long tasks    | ❌ No             | ✅ Yes             |

***

# ✅ **Concrete Real-World Examples (Fintech / Banking)**

### ✅ Use **ForegroundService** for:

*   Live location sharing for cash pickup agents
*   Uploading large KYC documents
*   Continuous biometric authentication session
*   VPN or secure tunnel for remote onboarding
*   Real-time fraud detection sensor tracking

### ✅ Use **WorkManager** for:

*   Uploading logs
*   Retrying failed backend sync
*   Background data sync
*   Scheduling periodic settlements
*   Offline data backup

***

# ✅ **Interview‑Ready Summary**

✅ **ForegroundService is used when work must:**

*   Start instantly
*   Continue running uninterrupted
*   Be visible to the user
*   Be long-running or real-time
*   Provide actionable notification controls

✅ **WorkManager is for:**

*   Deferrable
*   Guaranteed
*   Background
*   Constraint-based
*   Non‑urgent work

**One-liner answer for interview:**

> **Use ForegroundService when the task must run immediately, continuously, and visibly, such as long-running uploads, GPS tracking, media playback, or any user-facing ongoing work. WorkManager cannot guarantee timely or uninterrupted execution.**

***

</details>

---

## Topic 2: Architecture + Modularization (Most Important)

<details>
  <summary><strong>1. Explain Clean Architecture in detail. Draw the boundaries.</strong></summary>

***

# ✅ **Clean Architecture — Full Explanation (Android + Modularization Context)**

Clean Architecture (by Robert C. Martin — Uncle Bob) is an architectural style designed to:

✅ separate business logic from framework code  
✅ create stable internal layers  
✅ enforce dependency direction  
✅ make the code testable, modular, maintainable, and scalable

It emphasizes:

> **“Depend on abstractions, not concrete implementations.”**  
> **“Policy should not depend on details — details depend on policy.”**

***

# ✅ **Core Purpose of Clean Architecture**

1.  **Independence from frameworks**  
    Your app should not depend on Android SDK, Retrofit, Room, Firebase, etc., in the core logic.

2.  **Testability**  
    Business rules should be testable without Android, network, or database.

3.  **Separation of concerns**  
    UI, business rules, and data sources are isolated.

4.  **Maintainability & Extensibility**  
    Easy to add new features or swap implementations (e.g., replacing REST with GraphQL).

***

# ✅ **The Four Main Layers (With Boundaries)**

Clean Architecture is usually visualized as concentric circles.

Here’s the **ASCII diagram** you can draw in interviews:

                           +---------------------------+
                           |        PRESENTATION       |
                           |     (UI / ViewModels)     |
                           +-------------+-------------+
                                         |
                           +-------------v-------------+
                           |         DOMAIN            |
                           | (UseCases, Entities, Repo |
                           |       Interfaces)         |
                           +-------------+-------------+
                                         |
                           +-------------v-------------+
                           |          DATA             |
                           | (Repository Impl, Mappers,|
                           |  Retrofit/Room/DS Layer)  |
                           +-------------+-------------+
                                         |
                           +-------------v-------------+
                           |       FRAMEWORK /         |
                           |        PLATFORM           |
                           | (Android SDK, DI, Hilt,   |
                           |  WorkManager, Navigation) |
                           +---------------------------+

***

## ✅ **1. Domain Layer (Inner Core)**

**💡 The heart of the application. Pure Kotlin. No Android dependencies.**

Contains:

✅ **Entities** → Business models  
✅ **UseCases / Interactors** → Business logic  
✅ **Repository Interfaces** → Abstract contracts  
✅ **Pure logic** → no Android, no Retrofit, no Room

**Never depends on any other layer.**

### Example:

```kotlin
data class Account(val id: String, val balance: Double)

interface AccountsRepository {
    suspend fun getBalance(accountId: String): Double
}

class GetBalanceUseCase(private val repo: AccountsRepository) {
    suspend operator fun invoke(id: String) = repo.getBalance(id)
}
```

***

## ✅ **2. Data Layer**

Implements the domain’s contracts.

Contains:

✅ **Repository Implementations**  
✅ **Retrofit Services**  
✅ **Room DAOs**  
✅ **DataSources (remote + local)**  
✅ **DTOs / Mappers**

**Depends on Domain → NOT vice versa.**

### Example:

```kotlin
class AccountsRepositoryImpl(
    private val api: AccountsApi,
    private val dao: AccountsDao
) : AccountsRepository {

    override suspend fun getBalance(accountId: String): Double {
        return api.fetchBalance(accountId).amount
    }
}
```

***

## ✅ **3. Presentation Layer**

Contains:

✅ **UI (Compose, XML)**  
✅ **ViewModels**  
✅ **State (StateFlow, LiveData)**  
✅ **Navigation**

Depends on **Domain**.

### Example:

```kotlin
class AccountsViewModel(
    private val getBalance: GetBalanceUseCase
) : ViewModel() {

    val balance = MutableStateFlow(0.0)

    fun load(accountId: String) = viewModelScope.launch {
        balance.value = getBalance(accountId)
    }
}
```

***

## ✅ **4. Framework / Platform Layer**

Contains:

✅ **Android SDK**  
✅ **Hilt modules**  
✅ **WorkManager**  
✅ **DataStore**  
✅ **Navigation Components**  
✅ **Permissions, Sensors**

This is the **outermost** layer.  
It depends on everything else but nobody should depend on it.

***

# ✅ **Dependency Rule (Most Important Concept)**

**All dependencies point inwards — toward the Domain layer.**

Meaning:

    Framework → Data → Presentation → Domain

Domain **never** imports:

❌ Android SDK  
❌ Retrofit  
❌ Room  
❌ Hilt  
❌ Coroutines Dispatchers  
❌ UI classes

Domain is pure business logic.

***

# ✅ **Boundary Rules (What You Must Not Violate)**

### ✅ Domain cannot depend on Data

→ If Domain imports Retrofit/Room, architecture is broken.

### ✅ Domain cannot depend on Android classes

→ No Context, LiveData, Parcelable, Bundle, ViewModel, etc.

### ✅ UseCases must not know how data is fetched

→ Only repository interfaces.

### ✅ Data layer should not know about ViewModels or UI state.

### ✅ Presentation should NOT know database/network details

→ Only interacts with UseCases.

***

# ✅ **Where to Put Each Component (Android Mapping)**

| Component                      | Layer             |
| ------------------------------ | ----------------- |
| UseCases                       | Domain            |
| Entities                       | Domain            |
| Repository Interfaces          | Domain            |
| Repository Implementations     | Data              |
| Retrofit/Room                  | Data              |
| Mappers                        | Data              |
| ViewModels                     | Presentation      |
| UI (Compose, XML)              | Presentation      |
| Hilt Modules                   | Framework         |
| DataStore / WorkManager        | Framework or Data |
| Build.gradle dependency wiring | Framework         |

***

# ✅ **How to Modularize Clean Architecture (Android Best Practice)**

✅ Create modules:

    :domain
    :data
    :presentation (per feature)
    :app (DI, navigation, configuration)

Example:

    :domain
        - usecases
        - entities
        - repository interfaces

    :data
        - network module
        - database module
        - repository implementations

    :features:accounts
    :features:payments
    :features:auth

✅ Presentation modules depend on Domain  
✅ Data depends on Domain  
✅ App depends on everything (entry point)

***

# ✅ **Why Clean Architecture Matters (For Senior/Architect Roles)**

*   Testability without Android
*   Independent teams can work in parallel
*   Large codebases scale cleanly
*   Easy to replace data sources (REST → GraphQL, Room → SQLDelight)
*   Business logic protected from UI & framework churn
*   Very powerful for multi-module fintech/banking apps

***

# ✅ **Interview‑Ready One‑Liner**

> **Clean Architecture separates UI, domain, and data layers with strict inward dependencies. Domain contains pure business logic, data implements repositories, presentation displays UI, and frameworks sit at the boundary. All dependencies flow toward the business rules, ensuring testability, modularity, and maintainability.**

***

</details>

<details>
  <summary><strong>2. Why should domain layer not depend on Android SDK?</strong></summary>

***

# ✅ **Why the Domain Layer MUST NOT Depend on the Android SDK**

The Domain layer is the **core business logic** of your app—its “brain.”  
It must remain:

✅ Pure  
✅ Stable  
✅ Testable  
✅ Platform‑agnostic  
✅ Reusable

Depending on the Android SDK violates all of these principles and breaks Clean Architecture’s **dependency rule**:

> **“High-level (business) modules should never depend on low-level (framework) modules.”**

Let’s break down the reason deeply.

***

# ✅ **1. Android SDK Is a Framework → Frameworks Should Depend *On* Domain, Not Vice Versa**

If you allow Android classes inside Domain:

*   Domain becomes tightly coupled to Android
*   Business rules can’t run without Android OS
*   Layer independence collapses
*   Code becomes harder to port, maintain, or scale

### Example of violation:

```kotlin
class TransferUseCase(val context: Context)  // ❌ WRONG
```

This ties business logic to Android.

Domain should be able to run as plain Kotlin:

✅ JVM  
✅ Server  
✅ Desktop  
✅ Unit tests

***

# ✅ **2. Testability: Domain Must Run Without Instrumentation Tests**

If Domain uses:

*   Context
*   LiveData
*   Bundle
*   ViewModel
*   Parcelable
*   WorkManager
*   Coroutine Dispatchers.Main

…then you must run **instrumented tests** (slow, device/emulator).

But Domain should be tested using **fast JVM tests**.

This is *only possible* if Domain is pure Kotlin with **zero Android imports**.

***

# ✅ **3. Stability: Android SDK Is Volatile, Business Rules Are Stable**

Android changes frequently:

*   API levels
*   Lifecycle behaviors
*   Permissions system
*   Background execution limits
*   Activity/Fragment changes
*   Navigation APIs
*   Compose vs XML

Domain rules do NOT change frequently.

### Domain should be the most stable layer.

### Android SDK is one of the most volatile layers.

So Domain must not depend on it.

***

# ✅ **4. Portability: Domain Should Be Reusable Across Platforms**

In modern apps, business logic may eventually run on:

✅ Backend/KMP shared module  
✅ iOS via Kotlin Multiplatform  
✅ WearOS  
✅ Desktop  
✅ Web via Kotlin JS

If Domain depends on Android, you lose this portability.

***

# ✅ **5. Independence: Domain Must Outlive UI and Data Changes**

You should be able to swap:

*   Retrofit → GraphQL
*   Room → SQLDelight
*   XML → Compose
*   ViewModel → MVI
*   Navigation component → custom router

WITHOUT changing the Domain layer.

If Domain uses Android classes, any change in architecture breaks the Domain.

***

# ✅ **6. Domain Is Pure Business Logic (Policies). Android Is an Implementation Detail (Details).**

Clean Architecture principle:

> ✅ **Policy** (business rules)  
> ❌ must NOT depend on **details** (Android, Retrofit, Room)

Domain = Policy  
Android SDK = Low-level detail

So by definition, Domain must not depend on Android.

***

# ✅ **7. Avoid Memory Leaks or Lifecycle Issues**

If Domain layer depends on Android context or Lifecycle:

*   Memory leaks become possible
*   Lifecycle behavior affects business logic
*   UseCases start relying on UI/resource lifecycle

Example of an anti-pattern:

```kotlin
class GetUserProfileUseCase(val appContext: Context) // ❌ Bad
```

UseCases should not care about:

*   screen
*   context
*   lifecycle
*   configuration changes

***

# ✅ **8. UI/Threading Should Not Influence Domain Logic**

Domain should not know:

*   which thread it runs on
*   whether it's running in Compose, XML, or a background service
*   if the app is in foreground or background

Threading is a responsibility of Presentation/Data layers, not Domain.

So Domain cannot use:

*   Dispatchers.Main
*   LiveData
*   ViewModelScope
*   Handler
*   Looper

***

# ✅ **9. Prevents Circular Dependencies**

If Domain uses Android SDK, then:

*   Data/Presentation modules depend on Domain
*   Domain depends on Android
*   Android depends on Data/Presentation

Circular dependencies → architecture collapses.

***

# ✅ **10. Makes Modularization Impossible**

If Domain depends on Android, you cannot create a separate module like:

    :domain
    :data
    :common
    :features:payments
    :features:accounts

Because Domain would need `android.jar`, forcing:

*   domain → android
*   data → domain + android
*   presentation → domain + android

This destroys clean modular boundaries.

***

# ✅ **Interview‑Ready One‑Liner**

> **Domain layer should never depend on Android SDK because it must remain pure business logic — independent, testable, stable, framework-free, and reusable across platforms. Android is a detail; business rules are policies. Details must depend on policies, not the other way around.**

***

</details>

<details>
  <summary><strong>3. Why use UseCases instead of calling repository directly?</strong></summary>

***

# ✅ **Why Use UseCases Instead of Calling Repository Directly?**

UseCases (a core part of Clean Architecture) represent the **application’s business actions**.  
They are not just “extra layers” — they solve **real problems** in large, scalable architectures.

Think of UseCases as **business logic containers**, not just wrappers around repositories.

Let’s break it down clearly.

***

# ✅ **1. UseCases encapsulate business rules (not data logic)**

Repositories deal with:

*   reading/writing data
*   networking
*   caching
*   database IO

But business logic is often more complex:

*   validations
*   transformations
*   combining data from multiple sources
*   permission checks
*   business steps
*   error handling
*   transaction rules (in fintech/banking this is critical)

### ✅ Without UseCases

Your ViewModel becomes overloaded:

```kotlin
// ❌ ViewModel doing business logic
val balance = repo.getBalance()
if (balance < 0) throw NegativeBalanceException()
val fee = repo.getFee()
val finalAmount = calculate(balance, fee)
```

Over time → massive, untestable ViewModel.

### ✅ With UseCases

Business flow lives in a single reusable place:

```kotlin
class CalculateBalanceUseCase(private val repo: AccountsRepository) {
    suspend operator fun invoke() : Double {
        val balance = repo.getBalance()
        val fee = repo.getFee()
        return balance - fee
    }
}
```

***

# ✅ **2. UseCases give a stable API to the Presentation layer**

Repositories may change:

*   REST → GraphQL
*   SQL → NoSQL
*   DataStore → Local cache
*   Response models change
*   Caching strategy changes

But UseCases expose **stable business actions** to the UI.

### UI says:

> *“FetchBalance() please.”*

It does **not** care:

*   where data comes from
*   how it’s processed
*   what API version you’re calling

This creates **loose coupling**.

***

# ✅ **3. UseCases improve testability (pure, isolated business logic)**

Repositories need:

*   DB or network environment
*   or fakes/mocks

UseCases need only **mock repository interfaces**.

### ✅ Example test:

```kotlin
@Test
fun testCalculateBalance() = runTest {
    val repo = FakeAccountsRepository(balance=100, fee=10)
    val useCase = CalculateBalanceUseCase(repo)

    assertEquals(90.0, useCase())
}
```

✅ Fast  
✅ Pure  
✅ No Android needed  
✅ No DB  
✅ No network

This is impossible if ViewModel directly uses repositories.

***

# ✅ **4. UseCases enforce Single Responsibility Principle (SRP)**

A ViewModel should:

✅ Manage UI state  
✅ Trigger business actions  
❌ NOT perform business operations  
❌ NOT orchestrate data merges  
❌ NOT validate inputs  
❌ NOT compute domain logic

UseCases handle:

*   business rules
*   validations
*   transformations
*   domain decision-making

Example:

`TransferMoneyUseCase` would:

*   verify OTP validity
*   validate amount
*   check minimum balance
*   apply fees
*   call repository
*   handle success/failure rules

Not the ViewModel.

***

# ✅ **5. UseCases make multi-module architecture scalable**

In multi-module apps:

    :domain
    :data
    :features:payments
    :features:accounts

A UseCase:

*   belongs to `:domain`
*   does not depend on Android
*   has no idea about ViewModel or UI

Because of this structure:

✅ A new UI (Jetpack Compose, WearOS, web, desktop KMP) can reuse the same UseCases  
✅ Backend engineers can use the same business rules in shared modules  
✅ Multiple teams can work safely in parallel

Repositories change often → UseCases remain stable.

***

# ✅ **6. UseCases orchestrate multiple repositories**

In real apps, actions often span multiple data sources:

### ❌ Without UseCase

ViewModel becomes spaghetti:

```kotlin
val user = userRepo.getUser()
val acc = accountRepo.getAccount(user.id)
val offers = offersRepo.getOffers(acc.segment)
```

### ✅ With UseCase

Encapsulated cleanly:

```kotlin
class LoadDashboardUseCase(
    val userRepo: UserRepository,
    val accRepo: AccountRepository,
    val offersRepo: OffersRepository
) {
    suspend operator fun invoke(): DashboardData {
        val user = userRepo.getUser()
        val acc = accRepo.getAccount(user.id)
        val offers = offersRepo.getOffers(acc.segment)
        return DashboardData(user, acc, offers)
    }
}
```

UI becomes:

```kotlin
dashboardState.value = loadDashboardUseCase()
```

***

# ✅ **7. UseCases prevent fat repositories**

If you skip UseCases:

*   You push business logic into repositories
*   Repositories know too much
*   Data layer gets polluted

Repositories should deal only with **data operations**, not business rules.

***

# ✅ **8. UseCases allow reuse across feature modules**

A banking app example:

`ValidatePANUseCase`  
`CalculateLoanEligibilityUseCase`  
`GenerateOTPUseCase`  
`VerifyOTPUseCase`  
`TransferMoneyUseCase`

These UseCases can be reused in:

*   onboarding
*   payments
*   profile update
*   lending flows
*   background tasks
*   WorkManager jobs

Reuse becomes EASY without UI dependency.

***

# ✅ **9. UseCases simplify ViewModel**

ViewModel becomes:

✅ slim  
✅ readable  
✅ lifecycle-aware  
✅ UI-state focused

```kotlin
viewModelScope.launch {
    _state.value = useCase()
}
```

Instead of multiline business logic.

***

# ✅ **10. UseCases follow Clean Architecture Dependency Rule**

    Presentation → Domain → Data

UI depends on UseCases  
UseCases depend on Repository interfaces  
Data implements those interfaces

This ensures:

✅ full decoupling  
✅ future-proofing  
✅ easy refactor  
✅ clean modular architecture

***

# ✅ **11. UseCases allow background / parallel execution easily**

UseCases are the perfect place for:

*   parallel flows
*   combining flows
*   batching events
*   coroutine structured concurrency

They isolate this complexity away from UI.

***

# ✅ ✅ **Interview‑Ready One‑Liner**

> **UseCases encapsulate business logic, isolate repositories, improve testability, enforce separation of concerns, stabilize the UI contract, and make the architecture scalable and modular. Without UseCases, business rules leak into ViewModels or repositories, breaking Clean Architecture.**

***

</details>

<details>
  <summary><strong>4. How do you handle bi-directional relationships between modules?</strong></summary>

***

# ✅ **How to Handle Bi‑Directional Relationships Between Modules**

In modular architecture, **bi‑directional relationships (A → B and B → A)** are *strictly forbidden* because they create:

*   circular dependencies
*   compilation issues
*   inability to scale modules independently
*   architecture violations
*   spaghetti dependency graphs

But modules often *need* to talk to each other.  
So how do you design it **properly**?

Below are **architect‑approved techniques**.

***

# ✅ **1. Identify Why the Circular Dependency Is Happening**

Bi‑directional dependency typically occurs because:

*   Module A needs domain logic from B
*   Module B needs UI/event logic from A
*   Both need access to shared models or contracts

You must isolate what they are sharing.

99% of the time, this means one of these is missing:

✅ A **shared abstraction**  
✅ A **contract/interface module**  
✅ A **proper domain layer**  
✅ A **navigation or communication boundary**

***

# ✅ **2. Use the “Dependency Rule” (Everything Points Inward)**

In Clean Architecture:

    App → Presentation → Domain → Data

Dependencies must always point inward.

If two modules need each other, that’s a sign you must **elevate shared things to a more central layer**.

***

# ✅ **3. Create a “Shared Contracts” or “Core” Module to Break the Cycle**

This is the **most common and correct solution**.

Example:

    :featureA → :core → :featureB

NOT:

    :featureA ↔ :featureB   ❌

### ✅ Put shared:

*   models
*   interfaces
*   use case contracts
*   events
*   navigation contracts

…into a **core** or **contracts** module.

Then:

*   Feature A depends on Core
*   Feature B depends on Core
*   But Feature A does *not* depend on Feature B
*   And Feature B does *not* depend on Feature A

This solves 80% of circular dependency problems.

***

# ✅ **4. Use Dependency Inversion (Interfaces in Domain/Contracts)**

Example:

Feature A needs something from Feature B.

### Wrong (direct dependency):

    featureA → featureB
    featureB → featureA   ❌

### Correct (via interface):

    FeatureA → contract
    FeatureB → contract

### Example:

Define the contract:

```kotlin
interface PaymentNavigator {
    fun openPaymentScreen(accountId: String)
}
```

Feature A uses this **interface**, not Feature B directly.

Feature B (which actually knows how to open payment screen) implements it.

Injected via DI (Hilt/Koin).

***

# ✅ **5. Use “Navigation Abstractions” to Avoid Feature → Feature Dependencies**

For Navigation Component multi-module apps:

✅ Create a module: `:navigation-api`

Inside it:

```kotlin
interface AccountsNavigator {
    fun navigateToAccountDetails(id: String)
}
```

Feature modules communicate only through these APIs.  
Actual navigation logic resides in `:app` module.

This ensures **each feature is isolated**.

***

# ✅ **6. Use Event Bus or SharedFlow (only for ViewModel-to-UI, not cross-module)**

Sometimes features need to “notify” each other.

✅ Recommended:  
Use **domain events** or **contracts** as described above.

⚠️ Only in very rare cases:  
Use an event bus-like system (`SharedFlow`, `Channel`, custom event manager).

But keep it in a central `:core-events` module.

Never let features talk directly to each other.

***

# ✅ **7. Use Renderer Pattern / Plugin System (Advanced Modularization)**

In very large modular apps (e.g., banking superapps), features must register themselves in a host module.

Example:

    :home-screen module renders tiles registered by:
       :payments
       :accounts
       :investments

Use a plugin registry:

```kotlin
interface HomeTileProvider {
    fun getTiles(): List<HomeTile>
}
```

Each feature module implements it.

Host module collects them dynamically.

✅ avoids feature-to-feature dependencies  
✅ supports runtime feature addition

***

# ✅ **8. Use Multi-binding / Hilt Assisted Injection**

When multiple feature modules must contribute dependencies:

```kotlin
@IntoSet
@Binds
abstract fun bindTile(tile: PaymentsTileProvider): HomeTileProvider
```

The parent module collects them.

Again: no direct dependency.

***

# ✅ **9. Use Domain as the True “Center” to Avoid Cycles**

If two features need shared logic (e.g. KYC rules, user profile rules):

✅ Move the shared logic into **Domain layer**

### Example:

Instead of:

    featureA → featureB (to validate PAN)

Make:

    featureA → domain
    featureB → domain
    domain contains ValidatePanUseCase()

Domain becomes the center hub.

***

# ✅ **10. Split Module by Responsibility, Not by Layers Only**

Sometimes the problem is simply **wrong module boundaries**.

Example:

`feature-payments` also contains login-related logic → leading to circular dependency with `feature-auth`.

Solution:

✅ Extract authentication rules into:

    :auth-domain

✅ Extract UI-only logic into:

    :auth-presentation

Then payments → depends only on `auth-domain`, not UI.

***

# ✅ **Examples of Real-World Circular Dependencies and Solutions**

### Example 1 — Feature A needs user profile from Feature B

✅ Move user profile into `:domain-user`

### Example 2 — Payments feature needs navigation to Accounts

✅ Create navigation interface in `:navigation-api`

### Example 3 — Authentication feature needs device info from Settings feature

✅ Move device info provider into `:core-utils`

***

# ✅ **Interview‑Ready Summary**

✅ Bi-directional module dependencies are forbidden — they break modularization.  
✅ You fix them using:

1.  **Shared Contracts/Core Module**
2.  **Dependency Inversion (Interfaces)**
3.  **Navigation Abstractions**
4.  **Central Domain Layer**
5.  **Multi-Binding or Plugin Registry**
6.  **Event Interfaces, not direct calls**
7.  **Restructuring features to eliminate shared logic**

✅ **All module dependencies should be unidirectional**, following clear boundaries and the dependency rule.

***

</details>

---

### Design a multi module Android app

<details>
  <summary><strong>1. How would you design a multi module Android app with 200+ screens?</strong></summary>

***

# ✅ **How would you design a multi‑module Android app with 200+ screens?**

(This is a *must‑know* question for Android Architect roles — fintech/banking apps often have 150–400 screens.)

The key expectation:  
🟢 **You must show mastery of architecture, modularization, build optimization, team scalability, navigation, communication, and CI/CD.**

I’ll give you:

✅ Overall architecture  
✅ Module types & boundaries  
✅ Feature slicing strategy  
✅ Dependency rules  
✅ Navigation structure  
✅ Shared components strategy  
✅ Team-scaling approach  
✅ Build speed optimization  
✅ Diagram you can draw in an interview  
✅ Summary for rapid recall

***

# ✅ **1. High-Level Architecture Diagram (What You Can Draw in Interview)**

                       +-----------------------------+
                       |           app               |
                       |   (DI, nav host, theme)    |
                       +--------------+--------------+
                                      |
            -----------------------------------------------------
            |                     |                           |
    +-------v-------+   +---------v----------+     +-----------v---------+
    |   core-common |   |    core-ui         |     |    core-network     |
    | utils, logs   |   | design system      |     | retrofit, okio,     |
    | extensions    |   | compose atoms/mols |     | interceptors        |
    +-------+-------+   +---------+----------+     +-----------+---------+
            |                     |                           |
            -----------------------------------------------------------------
            |                     feature modules (200+ screens)            |
            |                                                               |
       +----v-----+  +-------v-------+  +--------v-------+   ...   200+    |
       | feature- |  | feature-      |  | feature-loans  |                |
       | payments |  | accounts      |  | onboarding     |                |
       +----+-----+  +-------+-------+  +--------+-------+                |
            |                |                 |                           |
            -----------------|-----------------|----------------------------
                             |
                        +----v-----+
                        |  domain  |
                        | usecases |
                        | entities |
                        +----+-----+
                             |
                     +-------v--------+
                     |     data       |
                     | repositories    |
                     | api / db impl  |
                     +----------------+

***

# ✅ **2. Layered Architecture Approach**

Your 200+ screen app must follow **Clean Architecture**, but modularized.

### ✅ Layers:

*   **app** → DI entry point, navigation graph, themes
*   **feature‑modules** → one module per business domain or per large screen group
*   **domain** → pure Kotlin, use cases + business entities
*   **data** → repository impls, Retrofit, Room
*   **core modules** → shared utilities/UI

### ✅ Dependency rule:

    app → feature → domain → data
    core modules → used across everything

***

# ✅ **3. How to Slice 200+ Screens into Modules (The Real Secret)**

### ✅ Strategy 1: Business Feature Modules (recommended for banking/fintech)

Examples:

*   `feature-auth`
*   `feature-onboarding`
*   `feature-home`
*   `feature-accounts`
*   `feature-payments`
*   `feature-loans`
*   `feature-insurance`
*   `feature-investments`
*   `feature-kyc`
*   `feature-profile`
*   `feature-notifications`

This makes teams independent and reduces merge conflicts.

***

### ✅ Strategy 2: Vertical Slicing (Each feature = UI + ViewModel + UseCases + Repository Interfaces)

Inside each feature:

    feature-payments/
        ui/
        presentation/
        domain/ (but depends on :domain module)
        data/

But **do NOT** duplicate domain/data logic — reuse central modules.

***

### ✅ Strategy 3: “Mini-feature modules” for 200+ screens

Large features can be broken further:

    feature-payments
        ├── feature-payments-imps
        ├── feature-payments-neft
        ├── feature-payments-rtgs
        ├── feature-payments-billpay

AND they share:

    payments-domain
    payments-data
    payments-ui-components

This allows parallel development and clean boundaries.

***

# ✅ **4. Core Modules (Critical for Massive Apps)**

These let you avoid code duplication across 200+ screens.

### ✅ `core-ui`

*   Design System (colors, typography, spacing)
*   Compose components (“atoms, molecules, organisms”)
*   Reusable widgets like buttons, inputs, error views

### ✅ `core-common`

*   Extensions
*   Coroutine utils
*   Logging
*   Resource helpers

### ✅ `core-network`

*   Retrofit
*   Interceptors
*   Authentication handler
*   Error mapper
*   Token refresh logic

### ✅ `core-database`

*   Room
*   DAOs
*   Migrations
*   Shared tables

### ✅ `core-navigation`

*   Navigation APIs
*   Deep link handler
*   Feature entry points

These ensure **no feature depends on another feature** → zero cyclic dependencies.

***

# ✅ **5. Navigation for 200+ Screens (Critical to Avoid Coupling)**

### ✅ Use Navigation Contracts Instead of Direct Navigation

Define an interface in `core-navigation`:

```kotlin
interface PaymentsNavigator {
    fun openTransferScreen(accountId: String)
}
```

Feature module implements it.  
`app` module wires it using Hilt multibindings.

✅ No feature knows about another feature  
✅ No circular dependency  
✅ Easy swapping of flows  
✅ Works across modules cleanly

***

# ✅ **6. Communication Between Modules**

NEVER do:

    featureA → featureB
    featureB → featureA  ❌ (cyclic dependency)

✅ Instead use one of:

*   **Shared domain models** (in `domain`)
*   **UseCases**
*   **Navigation contracts**
*   **Shared Repo Interfaces** (domain layer)
*   **Event Bus (SharedFlow), but only in app module**
*   **Pub/Sub pattern** with core-event module

***

# ✅ **7. CI/CD & Build Speed Optimization (Mandatory for 200+ modules)**

Large modular apps suffer from slow builds unless optimized.

### ✅ Use these techniques:

*   **Use Gradle build caching**
*   **Enable configuration caching**
*   **Move stable modules into separate modules to avoid recompile**
*   **Make modules independent** so modifying one doesn’t trigger rebuild of all
*   **Compose Compiler metrics**
*   **Parallel Gradle workers**

Feature modules should have:

✅ NO dependency on each other  
✅ Small DAG (dependency graph)  
✅ Clean boundaries

This keeps builds fast.

***

# ✅ **8. Team Scaling Approach (How 10–20 teams work together)**

*   Each team owns 1–3 feature modules
*   Core team manages domain/data/core modules
*   No team touches others’ modules
*   API contracts defined early
*   Teams integrate via navigation interfaces and domain contracts
*   No merge conflict hell

This is how large banking apps (HDFC, HSBC, Axis, ICICI, PhonePe) scale to 300–400 screens.

***

# ✅ **9. Folder Structure Example (Easy to Speak in Interview)**

    app/
    core/
        core-ui/
        core-common/
        core-network/
        core-database/
        core-navigation/
    domain/
    data/
    features/
        auth/
        onboarding/
        home/
        accounts/
        payments/
            payments-imps/
            payments-neft/
            payments-rtgs/
        kyc/
        profile/
        support/

***

# ✅ **10. Summary (Interview‑Ready Answer in 30 Seconds)**

> **A 200+ screen Android app must be modularized by business features with strict clean architecture boundaries.  
> Features should depend only on domain and core modules, never on each other.  
> Use navigation contracts to avoid cyclic dependencies, a central domain layer for business rules, data module for repositories, and core modules for shared UI, network, and utilities.  
> This gives fast builds, team scalability, testability, and clean separation.**

***

</details>

<details>
  <summary><strong>2. How do you split modules: by feature or by layer?</strong></summary>

# ✅ **How do you split modules: by Feature or by Layer?**

This is one of the most important (and misunderstood) questions in Android modularization.  
A strong architect must know:

✅ what the industry does  
✅ why layer‑based modularization fails at scale  
✅ why feature modules are essential  
✅ how hybrid (feature + core) structures actually work

Let’s break it down clearly.

***

# ✅ **Short Answer (What Interviewers Expect)**

> **You split modules primarily by *feature*, not by *layer*.**  
> But you also create a few *layered core modules* (domain, data, common-ui, network) that all features can depend on.
>
> This hybrid approach gives maximum scalability, parallel development, and fast builds.

Let’s dive deeper.

***

# ✅ **Option 1: Split by Layer (NOT Recommended for Large Apps)**

    :ui
    :domain
    :data

Seems clean… but leads to **big ball of mud**:

### ❌ Why layer-based modules fail:

*   UI module becomes huge (200 screens in a single :ui module)
*   Data module becomes massive (300+ API calls)
*   Every change triggers rebuild of entire module → slow builds
*   No team-level isolation (teams step on each other's toes)
*   High merge conflicts
*   Impossible to scale to 10–20 engineer teams
*   No feature boundaries → everything is tightly coupled

**Conclusion:** Layer-based split is good for *small apps*, not enterprise/banking apps with 200+ screens.

***

# ✅ **Option 2: Split by Feature (Recommended for 200+ Screens)**

This is how large fintech/banking apps (e.g., PhonePe, GPay, PayTM, HSBC, Revolut) are architected.

    :feature-auth
    :feature-home
    :feature-accounts
    :feature-payments
    :feature-loans
    :feature-kyc
    :feature-profile
    :feature-notifications
    ... (up to 30–50 modules)

### ✅ Why feature modules are superior:

*   Each feature is independent (loose coupling)
*   Teams can own features end‑to‑end
*   CI builds are faster (only affected modules rebuild)
*   Features can be developed in parallel without conflict
*   Easier A/B testing, on-demand delivery (dynamic features)
*   Lower cognitive load — developers work inside confined boundaries
*   Cleaner scaling when app grows past 100+ screens

But feature modules need some *shared foundation*…

***

# ✅ **The Correct Real-World Approach: Hybrid Architecture**

You *combine* the best of both worlds:

✅ Feature modules → isolated business areas  
✅ Layered core modules → shared functionality

The recommended structure:

### ✅ **1. Feature Layers (Primary Slicing)**

    :feature-auth
    :feature-payments
    :feature-accounts
    :feature-loans
    :feature-kyc
    :feature-profile

Each feature contains:

*   UI (Compose/XML)
*   ViewModels
*   Feature-specific UseCases
*   Feature-specific Repository interfaces

### ✅ **2. Shared Layer Modules (Secondary Slicing)**

    :domain          → global business usecases/entities
    :data            → repository implementations
    :core-network    → retrofit, interceptors
    :core-database   → room, migrations
    :core-ui         → design system components
    :core-common     → utils, extensions
    :core-navigation → navigation contracts/interfaces

### ✅ Why this hybrid design is perfect:

✔ Features stay independent  
✔ Shared logic goes into core modules  
✔ Domain & Data layers stay clean  
✔ No circular dependencies  
✔ Teams work independently  
✔ Fast incremental builds

This hybrid is the **industry standard**.

***

# ✅ **How the Dependency Graph Looks**

    app
      ↳ feature-modules (20–50)
           ↳ domain
           ↳ core-ui
           ↳ core-common
           ↳ core-navigation
           ↳ data

    data ↳ domain

✅ No feature ↔ feature dependencies  
✅ Everything depends inward (Clean Architecture rule)

***

# ✅ **When Should You Choose Feature Modules?**

✅ App > 50 screens  
✅ Multiple teams working in parallel  
✅ Fast build speeds needed  
✅ Independent delivery (e.g., dynamic features)  
✅ CI/CD scaling important  
✅ Domain-driven design  
✅ Clear business verticals (Payments, Loans, KYC, etc.)

***

# ✅ **When Layer Modules Can Be Enough**

✅ Very small app (< 20 screens)  
✅ Single team  
✅ Simple business logic  
✅ No need for extreme scalability

But this rarely applies in enterprise Android work.

***

# ✅ **The Architect‑Ready Final Answer**

> \*\*Always split modules by feature because features are the highest-level units of business capability, team ownership, scalability, and build isolation.
>
> But accompany them with a thin set of layered core modules (domain, data, core-ui, network, common) that enforce clean architecture boundaries and avoid code duplication.
>
> This hybrid approach supports modularity, maintainability, fast builds, team scalability, and clean architecture.\*\*

***

</details>

<details>
  <summary><strong>3. How do you share models between modules?</strong></summary>

***

# ✅ **How Do You Share Models Between Modules?**

In a multi‑module architecture, you should *never* duplicate models and you should avoid introducing feature‑to‑feature dependencies.

You share models using **centralized model modules** and **Dependency Inversion**.

Let’s break it down.

***

# ✅ **1. NEVER Share Models by Making Modules Depend on Each Other**

❌ Wrong (common mistake):

    feature-payments → feature-accounts
    feature-accounts → payments-models

This creates:

*   circular dependencies
*   tight coupling
*   build slowdown
*   no feature isolation

***

# ✅ **2. ALWAYS Share Models via a “Model Owner” Module**

This means models live in a shared module that both features depend on.

The correct pattern:

    feature-a
    feature-b
       ↑
       |
    shared-models

Examples of such modules:

### ✅ Option A: `domain` module

Put **business entities** (stable models) here.

### ✅ Option B: `:core-models` or `:common-models` module

If models are not business-level but shared across features (e.g., UI models).

### ✅ Option C: `feature-group-level models`

For example:

    :payments-domain
    :accounts-domain

These are shared by subfeatures inside a group.

***

# ✅ **3. Types of Models and Where They Should Live**

You share different types of models differently.

***

## ✅ A. **Domain Models** (Business Logic)

**Where:**  
✅ `:domain` module

**Examples:**

```kotlin
data class User(val id: String, val name: String)
data class Account(val id: String, val balance: Double)
data class Transaction(val id: String, val type: String, val amount: Double)
```

Why domain?

*   Stable
*   Pure Kotlin
*   No Android dependencies
*   Must be shared across multiple features

***

## ✅ B. **Repository Interfaces** (Contracts)

**Where:**  
✅ `:domain` module

Example:

```kotlin
interface AccountsRepository {
    suspend fun getAccount(id: String): Account
}
```

Shared by:

*   data layer
*   feature modules
*   ViewModels

***

## ✅ C. **Network Models (DTOs)**

**Where:**  
✅ `:data` or `:core-network` module

DTOs **should NEVER be shared with UI** → they require mapping.

Example:

```kotlin
data class AccountResponse(val accountId: String, val amount: Double)
```

Map DTO → Domain:

```kotlin
fun AccountResponse.toDomain() =
    Account(id = accountId, balance = amount)
```

***

## ✅ D. **UI Models (ViewState / UIState / ViewData)**

**Where:**  
✅ Inside feature module  
❌ Do NOT place UI models in shared modules unless truly reused

UI models are **feature-specific** and should not leak across modules.

Example:

```kotlin
data class AccountScreenState(val name: String, val balance: String)
```

***

# ✅ **4. If Two Features Need the Same Model → Move It Upward**

Rule:

    Move shared models to the closest common module.

Example:

*   If payments & accounts need `Account`, move it to `domain`.
*   If two payment flows need `PaymentUiModel`, move it to `payments-common-ui`.

***

# ✅ **5. How to Share Models Without Causing Circular Dependency**

Use **interfaces** and **inversion**.

### Example Problem

Feature A needs Account model from Feature B → circular dependency.

### ✅ Solution

Move `Account` into:

✅ `domain` (best)  
OR  
✅ `core-models` (if not business logic)

Now:

    feature-payments → domain
    feature-accounts → domain

***

# ✅ **6. Feature Cross-Communication Using Contracts (Best Practice)**

If a feature needs **data or actions** from another feature:

✅ Instead of sharing feature models, create a **feature contract** in `core-contracts`.

Example:

### Define interface:

```kotlin
interface AccountsContract {
    fun getSelectedAccount(): Account
}
```

### Implement in feature:

```kotlin
class AccountsImpl : AccountsContract {
    override fun getSelectedAccount() = ...
}
```

### Bind in Hilt:

```kotlin
@Binds
abstract fun bindAccountsContract(impl: AccountsImpl): AccountsContract
```

### Use in other modules:

```kotlin
@Inject lateinit var accountsContract: AccountsContract
```

✅ No module → module dependency  
✅ No circular dependency  
✅ Models flow through interfaces

***

# ✅ **7. NEVER Share Feature-Specific Models Across Modules**

This is a common anti-pattern.

Example:

    feature-payments defines PaymentScreenState
    feature-loans imports it → WRONG

UI models must remain local to the feature.

***

# ✅ **8. Summary of Where to Put What**

| Model Type            | Where to Store       | Shared?              |
| --------------------- | -------------------- | -------------------- |
| Domain Entity         | `:domain`            | ✅ Global             |
| UseCase Models        | `:domain`            | ✅ Global             |
| Repository Interfaces | `:domain`            | ✅ Global             |
| Network DTOs          | `:data`              | ❌ Not shared with UI |
| DB Entities           | `:data` / `:core-db` | ❌ Internal           |
| UI Models             | `:feature-*`         | ❌ Local              |
| Feature Contracts     | `:core-contracts`    | ✅ Shared APIs        |

✅ **Domain = shared business models**  
✅ **Data = internal models (DTO/Entity)**  
✅ **Feature = UI-only models**

***

# ✅ **Interview‑Ready One‑Liner Answer**

> **Models are shared via common modules — usually a `domain` or `core-models` module — never by making feature modules depend on each other.  
> Domain models stay in `:domain`, DTOs stay in `:data`, and UI models remain in their feature modules.  
> Shared interfaces (“contracts”) are used for cross-feature communication to prevent circular dependencies.**

***

</details>

<details>
  <summary><strong>4. How do you avoid circular dependencies?</strong></summary>

***

# ✅ **How Do You Avoid Circular Dependencies?**

Circular (bi-directional) dependencies happen when:

    module A → module B
    module B → module A   ❌

This breaks build graphs, slows CI, kills parallel builds, and violates Clean Architecture.

To avoid circular dependencies, you use **dependency inversion**, **shared contracts**, **domain-centralization**, and **navigation boundaries**.

Below are the **8 architect-approved techniques**.

***

# ✅ **1. Introduce a “Common Upward Module” (Promote Shared Code Upwards)**

If `feature-payments` and `feature-accounts` both need a model or logic:

Instead of:

    payments → accounts
    accounts → payments      ❌

Move the shared model **upwards** into a central module:

    :domain
        ↑            ↑
    payments       accounts

✅ No circular dependency  
✅ Domain becomes the stable center

***

# ✅ **2. Create a :core‑contracts Module (Most Important Pattern)**

When Feature A needs to call Feature B, never depend directly.

### ❌ Wrong:

    featureA → featureB
    featureB → featureA

### ✅ Correct:

    featureA → core-contracts
    featureB → core-contracts

In `core-contracts`:

```kotlin
interface AccountsNavigator {
    fun openAccountDetails(id: String)
}
```

Feature B implements it, app module wires it using DI.

✅ No cycle  
✅ Features remain isolated  
✅ Very scalable for large apps

***

# ✅ **3. Follow the Clean Architecture Dependency Rule (Everything Points Inward)**

    app → feature → domain → data

Domain must NEVER depend on:

❌ UI  
❌ Features  
❌ Database  
❌ Network

If dependency arrows always point inward, cycles cannot form.

***

# ✅ **4. Use Navigation Abstractions Instead of Direct Feature-to-Feature Calls**

Never do this:

```kotlin
// From feature-payments
startActivity(Intent(this, AccountsActivity::class.java))
```

This forces payments → accounts module coupling.

Instead:

Introduce a navigation interface:

```kotlin
interface PaymentsNavigator {
    fun openSelectAccount()
}
```

Implemented in the App module.

✅ Feature modules never depend on each other  
✅ App module orchestrates navigation

***

# ✅ **5. Use Dependency Inversion (Interfaces) for Cross-Feature Communication**

Feature A needs data from Feature B.

### ❌ Wrong:

    featureA imports featureB models

### ✅ Correct:

    interface ProfileProvider {
        fun getProfile(): UserProfile
    }

Place this interface in `core-contracts` or `domain`.  
Feature B implements it.  
Feature A receives it via DI.

✅ Both depend on interfaces, not each other  
✅ No cycles

***

# ✅ **6. Move Business Logic to Domain Layer**

If feature A depends on feature B because of business logic:

    featureA → featureB

Fix by moving shared rules to domain:

    featureA → domain
    featureB → domain

Domain becomes the **single source of truth**.

***

# ✅ **7. Break Large Features Into Smaller Feature Groups (Feature Clusters)**

Example: Payments feature has 10 flows:

    feature-payments  
        ├── feature-payments-imps  
        ├── feature-payments-neft  
        ├── feature-payments-rtgs  
        └── feature-payments-billpay  

Shared logic goes into:

    payments-domain
    payments-data
    payments-ui-common

Each sub-feature depends on these common modules — NOT each other.

✅ Clear boundaries  
✅ No cycles

***

# ✅ **8. NEVER Share UI Models Across Modules**

UI models are the #1 cause of accidental cycles.

### ❌ Wrong:

    featureA imports PaymentScreenState from featureB

Each feature should have **its own UIState**.

Shared models belong in **domain**, not UI.

***

# ✅ **Architectural Diagram (Safe Dependency Graph)**

                  +------------------+
                  |       app        |
                  |  DI + Navigation |
                  +---------+--------+
                            |
            +---------------+-------------------+
            |               |                   |
       +----v----+    +-----v-----+        +----v----+
       |featureA |    | featureB  |        |featureC |
       +----+----+    +-----+-----+        +----+----+
            |               |                   |
            +---------------+-------------------+
                            |
                +-----------v-----------+
                |     core-contracts    |
                +-----------+-----------+
                            |
                    +-------v------+
                    |    domain    |
                    +-------+------+
                            |
                         +--v--+
                         |data |
                         +-----+

✅ No feature ↔ feature dependency  
✅ All arrows point inward  
✅ No cycles possible

***

# ✅ **Most Important Rule (What Interviewers Want to Hear)**

> **Circular dependencies are avoided by enforcing the “inward dependency rule” and using shared abstractions — interfaces, domain models, and navigation contracts — instead of direct feature-to-feature references.**

***

# ✅ **Rapid Summary (30 sec answer)**

To avoid circular dependencies in a modular Android app:

✅ Use **shared modules** (`domain`, `core-contracts`, `core-ui`)  
✅ Use **interfaces instead of concrete classes** between features  
✅ Use **navigation contracts** instead of direct feature navigation  
✅ Move **shared business logic** into the domain layer  
✅ Enforce **dependency inversion** everywhere  
✅ Ensure **feature modules never depend on each other**  
✅ Keep **UI models local** to each feature

This ensures a clean, scalable, cycle-free module graph.

***

</details>

<details>
  <summary><strong>5. How do you enforce module boundaries (e.g., lint rules)?</strong></summary>

# ✅ **How do you enforce module boundaries (e.g., lint rules) in a multi‑module Android app?**

This is a *senior-level question*—interviewers want to know how you prevent teams from accidentally violating architectural rules (like feature-to-feature dependencies, leaking UI from core modules, bypassing Clean Architecture, etc.).

Your answer must mention:

✅ **Static enforcement tools** (Lint, Detekt, Ktlint)  
✅ **Build‑time Gradle constraints**  
✅ **Visibility modifiers**  
✅ **Architecture validation tools**  
✅ **Dependency rules (Gradle + Kotlin)**  
✅ **CI/CD enforcement**

Let’s break it down in a clean, structured, architect‑ready manner.

***

# ✅ **1. Enforce Boundaries Using Custom Lint Rules (Most Powerful & Flexible)**

Android Lint allows you to **write custom detectors** that block illegal references at compile time.

### ✅ Example: Preventing feature → feature dependency

You write a custom lint rule that checks:

*   If code inside `feature-payments` imports anything from `feature-accounts`
*   OR if UI layer imports `data` layer internals

Lint Rule checks imports using PSI & UAST nodes.

### ✅ Sample logic (conceptual):

```kotlin
if (import.contains("com.app.feature_accounts")) {
    reportError("Feature modules must not depend on each other")
}
```

You package lint rules in `:lint-rules` module and apply:

```gradle
lintChecks project(":lint-rules")
```

✅ Works across entire codebase  
✅ Developers see issues directly in Android Studio  
✅ Completely customizable

**This is the recommended and most scalable method.**

***

# ✅ **2. Use Gradle Dependency Constraints (Structural Enforcement)**

You can *block* unwanted dependencies using **Gradle configuration**.

### ✅ A. Restrict allowed module dependencies

Inside each feature module:

```gradle
dependencies {
    implementation(project(":domain"))
    implementation(project(":core-ui"))
    
    // ❌ Not allowed: implementation(project(":feature-accounts"))
}
```

### ✅ B. Use `dependencyAnalysis` / `buildHealth` Tools

Tools like **Gradle’s dependency-analysis plugin** allow:

*   Detecting cyclic dependencies
*   Detecting unused dependencies
*   Enforcing strict dependency graphs

***

# ✅ **3. Use Kotlin’s `internal` for Module-Level Encapsulation**

If you mark classes as `internal`, they are **visible only inside the module**.

### ✅ Example:

```kotlin
internal class InternalMapper
```

Prevents other modules from importing internal APIs.

✅ Great for preventing leakage of implementation details  
✅ Keeps module API surface clean

***

# ✅ **4. Use “API Modules” + “Impl Modules” Pattern**

Split each feature into:

    feature-accounts-api
    feature-accounts-impl

Other modules depend only on the **API module** (interfaces), never on implementation.

### ✅ Example:

    :feature-payments → depends on → :feature-accounts-api
    NOT → :feature-accounts-impl

This forces dependency inversion.

***

# ✅ **5. Use Architecture Validation Tools (Common in Large Companies)**

There are specialized tools to validate module boundaries:

### ✅ Tools:

*   **ArchUnit** (JVM architecture tests)
*   **Moditect**
*   **Module Graph Assertion**
*   Custom scripts that parse Gradle dependency graph

### ✅ Example: ArchUnit test

```kotlin
classes().that().resideInAPackage("..presentation..")
    .should().onlyBeAccessed().byAnyPackage("..app..", "..presentation..")
```

✅ These tests run in CI and prevent architecture violations.

***

# ✅ **6. Use Gradle’s “visibility = private” in composite builds** (Advanced)

In multi-module monorepos, you can hide modules using:

```gradle
visible = false
```

This prevents random modules from depending on internal modules.

***

# ✅ **7. Use CI/CD Enforcement (Automated Checks)**

Your CI pipeline should fail if:

*   Someone adds an illegal dependency
*   Lint rule is violated
*   Architecture tests fail
*   Gradle dependency graph shows a cycle

Tools used:

✅ GitHub Actions  
✅ Jenkins  
✅ Bitrise  
✅ Static analysis bots

***

# ✅ **8. Use Public/Private package boundaries & naming conventions**

Enforce package boundaries:

    com.app.feature.payments
    com.app.feature.accounts
    com.app.core.navigation

Write scripts that detect cross-boundary violations.

This prevents accidental imports.

***

# ✅ **9. Enforce Module-Agnostic Dependency Direction (Clean Architecture)**

The dependency direction rule ensures:

    feature → domain → data (interfaces)
    app → feature  
    feature (UI) → domain  
    domain → NO ONE  
    data → domain (interfaces only)

If followed strictly, **circular dependencies become impossible**.

This can be enforced with:

✅ Gradle dependency restrictions  
✅ Lint  
✅ ArchUnit  
✅ Dependency graphs

***

# ✅ **10. Use IDE Visibility Filters**

Android Studio allows adding:

*   "Only expose API module"
*   “Block internal modules from autocomplete”

This prevents accidental imports.

***

# ✅ **Architecture Enforcement Summary Table**

| Technique                     | What It Prevents                      | Strength     |
| ----------------------------- | ------------------------------------- | ------------ |
| Custom Lint Rules             | Illegal imports, cross-feature access | ⭐⭐⭐⭐⭐ (Best) |
| Gradle Dependency Enforcement | Direct module dependency              | ⭐⭐⭐⭐         |
| Kotlin `internal`             | API leakage                           | ⭐⭐⭐          |
| API/Impl Split                | Enforce contracts, hide impl          | ⭐⭐⭐⭐⭐        |
| ArchUnit Tests                | Architectural violations              | ⭐⭐⭐⭐         |
| CI/CD Checks                  | Enforcement in PRs                    | ⭐⭐⭐⭐         |
| Navigation Contracts          | Cross-feature communication           | ⭐⭐⭐⭐⭐        |

***

# ✅ **Interview-Ready 20-Second Answer**

> \*“I enforce module boundaries using a combination of custom Lint rules, strict Gradle dependency graphs, Kotlin `internal`, API/Impl split modules, navigation interfaces, and architecture tests (ArchUnit).
>
> All boundaries are validated in CI so no feature module can accidentally depend on another, and no UI code leaks into domain or data layers.  
> This ensures long-term modular integrity in large apps.”\*

***

</details>

---

### MVVM vs MVI

<details>
  <summary><strong>1. MVVM vs MVI — which one do you use in enterprise BFSI projects and why?</strong></summary>

# ✅ **MVVM vs MVI — Which One Do You Use in Enterprise BFSI Projects and Why?**

This is a *very common Android Architect interview question*, and the interviewer expects a **balanced analysis**, then a **clear decision** based on BFSI constraints:  
✅ security  
✅ regulatory compliance  
✅ auditability  
✅ predictability  
✅ stability  
✅ maintainability  
✅ large engineering teams

Let’s break it down like a true enterprise architect.

***

# ✅ 1. **What BFSI Apps Require (Key Constraints)**

Before deciding MVVM or MVI, understand BFSI requirements:

| BFSI Requirement                            | Explanation                                 |
| ------------------------------------------- | ------------------------------------------- |
| **High reliability & predictable UI state** | No inconsistent screens during transactions |
| **No duplicate or inconsistent actions**    | Money transfer, KYC steps, OTP flows        |
| **Strict audit and traceability**           | Every state must be deterministic           |
| **Large teams working on many screens**     | Architecture must be simple to onboard      |
| **Performance & battery considerations**    | MVI state models can grow heavy             |
| **Offline-first & retry flows**             | Many use cases need stable state management |
| **Lower risk**                              | Enterprise prefers battle-tested patterns   |

Given this, both MVVM and MVI work — but one is *more practical* at enterprise BFSI scale.

***

# ✅ 2. MVVM — Strengths & Weaknesses (Enterprise View)

### ✅ **MVVM Strengths**

*   Simpler mental model
*   Easy onboarding for large teams
*   Works great with StateFlow / LiveData
*   Clear separation of UI + ViewModel
*   Less boilerplate than MVI
*   Good for form screens, dashboards, lists
*   Easier backward compatibility
*   Works well with XML and Compose

### ❌ MVVM Weaknesses

*   UI state may become inconsistent if not managed properly
*   Event handling (one-time events) can be messy (requires SharedFlow / Event wrappers)
*   Harder to make state *fully predictable* than MVI

***

# ✅ 3. MVI — Strengths & Weaknesses (Enterprise View)

### ✅ **MVI Strengths**

*   Single source of truth (immutable UI state)
*   Every UI event transforms an explicit state
*   Predictable, testable, deterministic
*   Great for complex flows like:
    *   KYC
    *   Loan application wizards
    *   Multi-step onboarding
    *   Payment authorization

### ❌ **MVI Weaknesses**

*   More boilerplate
*   Large state objects can become heavy
*   Frequent recompositions (if not optimized)
*   Steeper learning curve → slows large teams
*   Not suitable for *all* screens vs targeted ones
*   Hard to scale to 200+ screens if used everywhere

In BFSI apps, MVI is often used “surgically” for complex journeys, not as a blanket architecture.

***

# ✅ 4. Real Enterprise BFSI Practice (What’s Actually Used?)

Most large BFSI apps (I’ve seen/implemented this in multiple cases) use:

## ✅ **MVVM for 80% of screens**

Reason:

*   Simple screens: login, dashboard, profile, lists, dropdowns
*   Easier for most developers
*   Faster feature delivery
*   Lightweight ViewModels
*   Works well with both XML & Compose
*   Enough structure without overhead
*   Teams can work independently

## ✅ **MVI for complex, multi-step, high‑risk flows (20%)**

Applicable for:

*   Money transfer (IMPS, NEFT, RTGS)
*   Insurance purchase journeys
*   Loan onboarding / loan eligibility flows
*   Multi-step KYC
*   Payment gateway authorization
*   Investment purchase flows

Why?  
Because such flows require:  
✅ deterministic state  
✅ no inconsistent transitions  
✅ step-by-step predictable actions  
✅ full auditability  
✅ ability to replay/restore state after process death

MVI is perfect for that.

***

# ✅ 5. The Enterprise-Proven Hybrid Approach (Best Answer)

**You should answer this in interviews:**

> ✅ *“In BFSI apps, I use MVVM as the primary architecture because it is simpler, scalable for large teams, and works well across hundreds of screens. But for critical, multi-step transactional flows that require strict state predictability — such as payments, KYC, loan journeys — I use MVI.”*

This demonstrates:

*   you understand real-world constraints
*   you’re not an architecture extremist
*   you choose based on context
*   you understand BFSI workflows deeply

***

# ✅ 6. When to Use Each (Decision Table)

| Scenario                     | MVVM      | MVI                            |
| ---------------------------- | --------- | ------------------------------ |
| Large teams, simple screens  | ✅         | ❌                              |
| Multi-step onboarding        | ❌         | ✅                              |
| Payment flows                | ❌         | ✅                              |
| Display-only screens         | ✅         | ❌                              |
| Rapid development            | ✅         | ❌                              |
| Complex, transactional flows | ❌         | ✅                              |
| 200+ screen app              | ✅ primary | ✅ secondary for critical flows |

***

# ✅ 7. Interview-Ready Conclusion

### ✅ **MVVM is the default choice in BFSI enterprise apps**

Because it is:

*   easier to scale
*   easier to onboard
*   easier to maintain
*   fits most screens
*   works well with Compose + Flow
*   reduces boilerplate

### ✅ **MVI is selectively applied**

Where state determinism is business-critical:

*   payments
*   KYC
*   onboarding wizards
*   loan processes
*   investment transactions

***

# ✅ **Final One-Liner to Impress the Interviewer**

> **“In BFSI projects, MVVM is the primary architecture due to scalability, simplicity, and maintainability across large teams and hundreds of screens. For high‑risk, transactional, multi-step flows requiring deterministic state, I selectively use MVI. This hybrid model prevents complexity explosion while ensuring reliability where it matters most.”**

***

</details>

<details>
  <summary><strong>2. How do you manage long-running flows in MVI?</strong></summary>

# ✅ **How do you manage long‑running flows in MVI?**

(This is a *critical* question for enterprise BFSI apps because long‑running flows appear in **payments**, **KYC**, **loan journeys**, **investments**, **policy purchases**, etc.)

You must explain:  
✅ how MVI handles long operations  
✅ how to avoid UI freezing or stale state  
✅ how to maintain deterministic events  
✅ how to cancel/restart/replay flows  
✅ how to survive configuration change & process death  
✅ how to integrate with coroutines & StateFlow

Let’s break it down in a clean, senior‑level way.

***

# ✅ **What Are Long‑Running Flows in MVI?**

These are flows that span LONG durations, often asynchronous and with multiple intermediate states:

*   Payment authorization (IMPS/NEFT/RTGS)
*   KYC: fetch → verify → upload → confirm
*   Loan journey: eligibility → documents → underwriting → approval
*   Investment SIP setup → mandate → bank callback
*   Multi-step onboarding flows

In these flows:

✅ You MUST show the UI in a stable, predictable state  
✅ You MUST avoid race conditions  
✅ You MUST handle cancellation/retry  
✅ You MUST avoid “lost events”  
✅ You MUST survive process death

This is exactly what MVI is good at *if done correctly*.

***

# ✅ **Core Principles to Manage Long-Running Flows in MVI**

## ✅ 1. Use a **State Machine** (Single Immutable State)

Long-running flows should always be modeled as:

```kotlin
sealed interface State {
    object Idle : State
    object Loading : State
    data class Step1(val data: A) : State
    data class Step2(val data: B) : State
    data class Step3(val data: C) : State
    data class Error(val msg: String) : State
    object Completed : State
}
```

This ensures:

✅ No partial state  
✅ No inconsistent UI  
✅ No overlapping steps  
✅ Time-based or asynchronous transitions are deterministic

***

## ✅ 2. Use One **Reducer** to Update State Predictably

All state updates must go through:

```kotlin
private fun reduce(old: State, intent: Intent): State
```

Reducers make long journeys predictable.

*   No accidental updates
*   No race conditions
*   No random mutations

This is the heart of MVI.

***

## ✅ 3. Use **Structured Concurrency** to Run Long Tasks Safely

Launch long tasks inside a ViewModel using:

```kotlin
job = viewModelScope.launch {
    repository.longRunningTask().collect { result ->
        _state.update { reduce(it, Intent.ResultArrived(result)) }
    }
}
```

Why?

✅ Cancellation is automatic  
✅ ViewModelScope survives config changes  
✅ No leaks  
✅ No double collectors

***

## ✅ 4. Always Cancel Old Jobs Before Starting New Ones

Long-running tasks must avoid overlaps.

```kotlin
private var currentJob: Job? = null

fun onIntent(intent: Intent) {
    currentJob?.cancel()

    currentJob = viewModelScope.launch {
        handleIntent(intent)
    }
}
```

Why?

✅ Prevents inconsistent flows  
✅ Prevents duplicated backend calls  
✅ Avoids ANRs due to parallel workloads

This is crucial in BFSI transactions where users press “Next” multiple times.

***

## ✅ 5. Use **Cold Flows** for Tasks + **StateFlow** for UI State

Maintains MVI purity:

```kotlin
private val _state = MutableStateFlow<State>(State.Idle)
val state = _state.asStateFlow()
```

And for long-running operations:

```kotlin
fun getKycStatus() = flow {
    emit(repository.fetchStep1())
    emit(repository.fetchStep2())
    emit(repository.fetchStep3())
}
```

Long-running flows must be cold → so they restart correctly based on state machine logic.

***

## ✅ 6. Model *Every Step* as **Intent → Reducer → State**

Example for KYC:

```kotlin
sealed class Intent {
    object FetchPan : Intent()
    data class PanFetched(val data: PanData) : Intent()
    object UploadDocs : Intent()
    data class DocsUploaded(val status: Boolean) : Intent()
}
```

This ensures:

✅ NO branching logic in UI  
✅ UI always reflects a predictable state  
✅ Long flows have an unbroken chain of intents

***

## ✅ 7. Handle Backpressure Using `flatMapLatest()` (Critical)

For requests like:

```kotlin
intentFlow.flatMapLatest { intent ->
    process(intent)
}
```

flatMapLatest cancels previous operations on new input:

✅ User can't trigger multiple parallel calls  
✅ Last intent always wins  
✅ Perfect for multi-step form flows

***

## ✅ 8. Provide Cancellable & Retryable States

In BFSI apps, retries are mandatory.

```kotlin
data class Error(val message: String, val retryAction: Intent) : State
```

Then UI can dispatch the retry intent:

```kotlin
viewModel.onIntent(state.retryAction)
```

✅ Unified retry  
✅ Deterministic  
✅ Zero duplicated logic

***

## ✅ 9. Persist State for Long Flows (Use SavedStateHandle)

Long journeys often span:

*   Activity recreation
*   Process death
*   Orientation change

Use:

```kotlin
val state = savedStateHandle.getStateFlow("state", State.Idle)
```

Saving:

```kotlin
savedStateHandle["state"] = newState
```

Benefits:

✅ Flow survives process death  
✅ Multi-step flows resume correctly  
✅ Zero lost progress

Critical for payments & loan journeys.

***

## ✅ 10. Emit Side Effects Separately (e.g., Navigation, Toasts)

Long-running flows require:

*   Navigation
*   Bottom sheets
*   Error dialogs
*   OTP triggers

These are **One-time events**, not UI state.

Use a `SharedFlow` or `Channel`:

```kotlin
private val _effect = MutableSharedFlow<Effect>()
val effect = _effect.asSharedFlow()
```

Emit:

```kotlin
_effect.emit(Effect.NavigateToOtpScreen)
```

This avoids putting ephemeral events into state → keeps MVI pure.

***

# ✅ **FULL ARCHITECTURE FLOW (Diagram)**

    Intent → Middleware → Reducer → StateFlow → UI Render

               ↑ long-running flow (cold Flow)
               └──────────────────────────┘

Middleware handles long jobs  
Reducer updates UI state  
StateFlow pushes to UI

*Everything is deterministic.*

***

# ✅ **Example: Payment Flow (IMPS/NEFT) — Real BFSI Pattern**

### **State**

```kotlin
sealed class State {
    object Idle : State
    object Loading : State
    data class EnterAmount(val limit: Limit) : State
    object EnterOtp : State
    data class InProgress(val trackingId: String) : State
    data class Success(val receipt: Receipt) : State
    data class Error(val message: String) : State
}
```

### **Effects**

```kotlin
sealed class Effect {
    object NavigateToOtp : Effect()
    data class ShowToast(val msg: String) : Effect()
}
```

### **Handling long-running transaction**

```kotlin
private fun startTransfer(intent: Intent.Start) {
    currentJob?.cancel()
    currentJob = viewModelScope.launch {
        repository.startTransfer(intent.data).collect { status ->
            _state.update { reduce(it, Intent.StatusArrived(status)) }
        }
    }
}
```

✅ Cancels previous transfers  
✅ Runs as long-running Flow  
✅ Mapped into deterministic state transitions

***

# ✅ **Interview‑Ready Summary**

> **In MVI, long-running flows are managed using cold flows + reducers + immutable state + structured concurrency. Each asynchronous step is represented as an Intent, processed via a single reducer, and emitted as an updated StateFlow. All long-running jobs are cancellable, restartable, and predictable. Effects (navigation, toasts) are emitted separately. SavedStateHandle guarantees flow continuity across process death.**

✅ Deterministic  
✅ Predictable  
✅ Safe for BFSI  
✅ Perfect for multi-step journeys  
✅ Fully testable

***

</details>

<details>
  <summary><strong>3. How do you avoid state explosion?</strong></summary>

***
  
State explosion is one of the biggest reasons teams abandon MVI—so this is an *extremely important interview question*.

I’ll explain:

✅ What state explosion is  
✅ Why it happens  
✅ How to prevent it using scalable architectural patterns  
✅ Code examples  
✅ BFSI‑specific handling

Let’s go.

***

# ✅ **What Is State Explosion in MVI?**

**State explosion** happens when a single `State` class becomes too big because you try to represent **every field, every UI widget, every intermediate step** in one massive immutable state.

Example anti-pattern:

```kotlin
data class ScreenState(
    val step1Field1: String,
    val step1Field2: String,
    val step2Field1: String,
    val step3Field1: List<Document>,
    val loading: Boolean,
    val error: String?,
    val otpSent: Boolean,
    val otpVerified: Boolean,
    val progress: Int,
    val timerLeft: Long,
    …
    // soon becomes 200+ fields
)
```

This leads to:

❌ giant reducers  
❌ accidental overwrites  
❌ painful testing  
❌ high recomposition cost  
❌ unreadable code  
❌ slower performance

This is NOT scalable for 50–200 screen BFSI apps.

***

# ✅ **How to Avoid State Explosion (10 Architect‑Level Techniques)**

***

# ✅ **1. Split State into Smaller Sub‑States (Most Important)**

Instead of one mega‑state, break the flow into logical sub‑states:

### ✅ Good MVI pattern:

```kotlin
sealed interface State {
    object Idle : State
    data class Step1(val form: Step1Form) : State
    data class Step2(val docs: Step2Docs) : State
    data class Step3(val summary: Step3Summary) : State
    data class Error(val msg: String) : State
    object Completed : State
}
```

Sub‑states reduce complexity by **scoping data to a step**.

✅ No massive class  
✅ Each step has its own clean model  
✅ Reducers stay small  
✅ Perfect for multi-step flows (payments, loans, KYC)

***

# ✅ **2. Use “Nested State” Models Instead of Flat Fields**

Instead of:

```kotlin
data class PaymentState(
    val amount: String,
    val beneficiary: Beneficiary,
    val bankCharges: Double,
    val otpField: String,
    val timer: Long,
)
```

Use composition:

```kotlin
data class PaymentState(
    val form: FormState,
    val otp: OtpState,
    val confirmation: ConfirmationState
)
```

✅ Each sub-state is isolated  
✅ Logic is separated  
✅ Reduces noise in reducers

***

# ✅ **3. Model UI State + Domain State Separately**

A huge mistake: mixing *business state* with *UI rendering state*.

Instead:

### Domain State:

```kotlin
data class TransferDomainState(val amount: Double, val charges: Double)
```

### UI State:

```kotlin
data class TransferUiState(val amountInput: String, val isNextEnabled: Boolean)
```

✅ Domain state = business logic  
✅ UI state = rendering logic  
✅ Reduces duplication  
✅ Smaller state models

***

# ✅ **4. Don’t Represent Every Tiny Widget in MVI State**

Not everything needs to be in the immutable state.

✅ Local component values (text fields, toggles, focus states)  
→ Use `rememberSaveable` / `mutableStateOf` in Compose  
→ Leave them out of MVI state unless business‑critical

Use MVI state ONLY for:

*   multi-step flow progression
*   validation
*   API results
*   critical data
*   navigation
*   submission status

**Not** for:

*   text field characters
*   scroll offsets
*   checkbox toggles
*   tabs

This micro‑optimizes state size.

***

# ✅ **5. Use Partial State or “State Reducer Pattern”**

Emit **partial states** instead of full state copies.

```kotlin
sealed class PartialState {
    data class Loading(val flag: Boolean) : PartialState()
    data class AmountUpdated(val amount: Double) : PartialState()
    data class OtpStarted(val ttl: Long) : PartialState()
    data class Error(val msg: String) : PartialState()
}
```

Reducer applies partial updates:

```kotlin
fun reduce(state: State, partial: PartialState): State {
    return when (partial) {
        is PartialState.Loading -> state.copy(isLoading = partial.flag)
        is PartialState.AmountUpdated -> state.copy(amount = partial.amount)
        …
    }
}
```

✅ Avoids huge immutable models  
✅ Reduces boilerplate  
✅ Smaller update logic  
✅ Better performance

***

# ✅ **6. Split Intents + Handlers Per Step**

Do NOT use a giant Intent class:

```kotlin
sealed class Intent {
    class EnterAmount(val value: String): Intent()
    class SelectBeneficiary(val id: String): Intent()
    class UploadDocs(val file: File): Intent()
    class VerifyOtp(val code: String): Intent()
    class Submit: Intent()
}
```

Instead break them into sub-intents per step:

    Step1Intent, Step2Intent, Step3Intent

✅ Clear separation  
✅ Smaller reducers  
✅ Smaller state models

***

# ✅ **7. Use Multiple Reducers Instead of One God Reducer**

Split reducer logic per state or per sub-flow.

Example:

```kotlin
when (state) {
    is State.Step1 -> step1Reducer(state, intent)
    is State.Step2 -> step2Reducer(state, intent)
    is State.Step3 -> step3Reducer(state, intent)
}
```

✅ Each reducer handles only its slice  
✅ No boilerplate explosion

***

# ✅ **8. Use State Machines (FSM) for Flow Boundaries**

MVI + State Machine = unbeatable for BFSI flows.

```kotlin
Idle → FillForm → UploadDocs → Review → Confirm → Completed
```

Each step has its own state type.

✅ Avoids keeping all possible fields alive  
✅ Limits state at each stage  
✅ Highly predictable

***

# ✅ **9. Use Persistence Selectively (SavedStateHandle)**

Use it only for:

*   current step
*   important business data
*   flow status
*   transaction ID
*   OTP state

Don’t persist the entire UI state.

✅ Lighter  
✅ Faster restore  
✅ Avoids storing unnecessary state

***

# ✅ **10. Avoid Monolithic MVI for All Screens (Hybrid Architecture)**

Not every screen needs full-blown MVI.

✅ Use MVI for multi-step BFSI flows (payments, KYC, loans)  
✅ Use MVVM (StateFlow) for simple screens

This prevents mega-states across non-critical screens.

***

# ✅ **BFSI‑Specific Best Practice (Enterprise Tip)**

In banking/financial apps:

✅ A payment/loan/KYC state should only contain what is REQUIRED for that **business transaction**.  
✅ UI-only values (checkboxes, form text) stay in View layer.  
✅ Only critical fields go to MVI state:

*   step
*   form validity
*   backend response
*   OTP status
*   transaction reference
*   error
*   loading flags
*   retry info

This prevents enormous state classes.

***

# ✅ **Interview‑Ready 20‑Second Summary**

**You avoid state explosion in MVI by breaking the state into sub-states, using nested state models, separating UI vs domain state, using partial state reducers, splitting intents, using multiple reducers and state machines, and keeping only business-critical data in MVI state. UI-only values stay local to the screen (Compose). This keeps MVI scalable and clean even for large BFSI multi-step flows.**

***

</details>

<details>
  <summary><strong>4. What patterns prevent inconsistent UI states?</strong></summary>

# ✅ **What patterns prevent inconsistent UI states?**

(Especially for MVVM/MVI in large enterprise BFSI apps.)

Inconsistent UI state =  
✅ UI shows stale data  
✅ UI jumps between states  
✅ UI shows impossible combinations (e.g., loading + error + content together)  
✅ Wrong screens shown after process death  
✅ Two-step flows overlapping  
✅ Multiple API calls updating UI unpredictably

**Preventing inconsistent UI is one of the core goals of Clean Architecture and MVI/MVVM.**  
Here are the **top architectural patterns** that prevent these issues.

***

# ✅ **1. Single Source of Truth (SSOT)**

**All UI renders from exactly one data source**—usually:

*   `StateFlow`
*   or `LiveData` (legacy)
*   or `MutableState` (Compose)

Example:

```kotlin
val uiState = MutableStateFlow(ScreenState())
```

✅ Prevents UI from combining stale + new values  
✅ Eliminates “multiple truth” sources  
✅ Ensures predictable UI updates

**Never** update UI from:  
❌ callbacks  
❌ async lambdas  
❌ multiple flows directly

Everything must funnel into **one SSOT**.

***

# ✅ **2. Immutable UI State (State Objects, Not Individual Fields)**

Instead of mutating fields:

```kotlin
state.isLoading = true   // ❌ leads to partial/invalid states
```

You produce **immutable state snapshots**:

```kotlin
_state.update { it.copy(isLoading = true) }
```

Benefits:

✅ UI cannot enter half-updated state  
✅ Each update produces a valid “frame”  
✅ Easy to test  
✅ Perfect for Compose

***

# ✅ **3. MVI: State Machine Pattern (Strongest Defense Against Inconsistency)**

Model UI state as a **finite number of allowed states**, not random combinations.

Example:

```kotlin
sealed interface State {
    object Idle : State
    object Loading : State
    data class Data(val items: List<Item>) : State
    data class Error(val message: String) : State
}
```

This prevents:

❌ Error + Loading at the same time  
❌ Data + Loading combined  
❌ Impossible transitions

FSMs force **legal transitions only**.

***

# ✅ **4. Reducer Pattern (Single Way to Change State)**

All state changes must pass through *one reducer function*:

```kotlin
fun reduce(current: State, intent: Intent): State
```

This guarantees:

✅ Centralized mutations  
✅ No rogue updates  
✅ Every state is accounted for  
✅ Easy to audit transitions

Perfect for BFSI apps where UI correctness = safety.

***

# ✅ **5. Unidirectional Data Flow (UDF)**

Flow always moves in one direction:

    UI → Intent → ViewModel → Reducer → New State → UI Render

Never:

❌ UI mutating state directly  
❌ Repository pushing UI changes  
❌ Backwards updates

UDF eliminates race conditions & inconsistent states.

***

# ✅ **6. Model Intents Instead of Direct Commands**

All user actions are “intents”:

```kotlin
sealed interface Intent {
    object Load : Intent
    data class Retry(val id: String) : Intent
    object Submit : Intent
}
```

This ensures:

✅ Predictable UI transitions  
✅ No random UI branching  
✅ No unpredictable async side effects

***

# ✅ **7. Distinguish State vs One‑Time Events (Effects Pattern)**

Most UI bugs come from mixing ephemeral events with persistent state.

Use **StateFlow** for UI state  
Use **SharedFlow / Channels** for side effects:

Effects include:

*   Navigation
*   Toast
*   Snackbar
*   Dialog
*   One-time confirmation

This avoids states like:

    State(showToast=true)  // ❌ wrong

Effects pattern guarantees:  
✅ No repeated toasts after rotation  
✅ No replaying dialogs  
✅ No “stuck” UI events

***

# ✅ **8. Replay = 1 (StateFlow) and replay = 0 (SharedFlow)**

Correct usage avoids inconsistent screens after reconstruction.

*   **StateFlow always replays latest state** → perfect for UI
*   **SharedFlow should not replay events** → avoids repeated actions

Example:

```kotlin
val state = MutableStateFlow(ScreenState())  // replay 1
val effects = MutableSharedFlow<Effect>(replay = 0)
```

***

# ✅ **9. Avoid Race Conditions with flatMapLatest**

Long running async operations can overwrite each other:

Use `flatMapLatest` to cancel old jobs:

```kotlin
intentFlow
  .flatMapLatest { intent -> handleIntent(intent) }
```

✅ Prevents out-of-order UI  
✅ Prevents concurrent loads  
✅ Guarantees newest intent wins

***

# ✅ **10. Use UseCases as Orchestrators (Clean Architecture)**

Business flows should be deterministic & atomic:

*   UseCase executes
*   returns one output
*   UI state updated

No UI logic inside VM.  
No multiple competing sources.

✅ Avoids “multiple updates from multiple sources”  
✅ Eliminates mid-air state mutations

***

# ✅ **11. Persist Only the Necessary State (SavedStateHandle)**

For long-running BFSI flows:

```kotlin
val state = savedStateHandle.getStateFlow("state", InitialState)
```

But **do not persist huge UI models**.

Save only:

✅ current step  
✅ transaction ID  
✅ progress status

Minimizes risk of restoring inconsistent UI after process death.

***

# ✅ **12. Use Derived State for UI Computations (Compose)**

UI-specific computed fields should not bloat the state:

```kotlin
val isButtonEnabled = derivedStateOf {
    state.value.amount.isNotEmpty() && !state.value.isLoading
}
```

This prevents inconsistent UI states caused by:

*   partially updated fields
*   forgotten flags
*   accidental state copying

***

# ✅ **13. Treat Network Loading/Error as State Transitions**

Don’t treat loading/error as separate flags scattered across state.

Correct:

```kotlin
State.Loading
State.Error("Failed")
State.Data(items)
```

Incorrect:

    loading = true, error = null
    loading = false, error = "x"
    loading = false, data = list

The latter causes many invalid combinations → inconsistent UI.

***

# ✅ **14. Always Use Sealed Classes for State & Intents**

Prevents:

*   illegal states
*   unhandled branches
*   “unknown” combinations

Example:

```kotlin
sealed interface PaymentState
```

Compiler guarantees **all states handled**.

***

# ✅ **Summary (Use This in Interviews)**

**Patterns that prevent inconsistent UI state:**

✅ Single Source of Truth  
✅ Immutable UI state  
✅ MVI State Machine  
✅ Reducer pattern  
✅ Unidirectional Data Flow  
✅ Separate State vs Effects  
✅ flatMapLatest for long-running tasks  
✅ UseCases as orchestrators  
✅ SavedStateHandle for process-safe state  
✅ Derived UI state  
✅ Sealed classes for state & intents

> **By combining SSOT + immutable state + reducers + UDF + state machines, you eliminate inconsistent UI, race conditions, double renders, and unpredictable flows — essential for BFSI apps where correctness is critical.**

***

</details>

---

### Dependency Injection (Hilt/Dagger)

<details>
  <summary><strong>1. Explain Dependency Injection at scale (Hilt/Dagger).</strong></summary>

***

This answer will give you exactly what interviewers look for:

✅ Why DI is needed at scale  
✅ How Dagger/Hilt works internally  
✅ How to structure modules, scopes, and components  
✅ DI patterns for large multi-module apps  
✅ Preventing DI bloat, compile‑time slowdowns, and circular deps  
✅ Best practices for BFSI apps

***

# ✅ **Dependency Injection at Scale — What It Means**

Small apps need DI to avoid:

*   manual object creation
*   boilerplate
*   tight coupling

Large enterprise apps (200+ screens, 40+ modules) need DI to additionally ensure:

✅ **scalable module boundaries**  
✅ **testability at each layer**  
✅ **team decoupling**  
✅ **consistency & security**  
✅ **deterministic object graph**  
✅ **compile-time safety** (zero runtime DI errors)

Hilt/Dagger provide **compile‑time validated DI**, which is essential for BFSI where runtime failures are unacceptable.

***

# ✅ **How Dagger/Hilt Works (Internal Architecture)**

### ✅ 1. **Dagger generates code at compile time**

*   No reflection → faster, safer
*   Every binding is validated → no runtime DI errors
*   Immutable dependency graph

### ✅ 2. **Hilt = Dagger + Android Lifecycle Integration**

Hilt creates components scoped to:

*   SingletonComponent (App-level)
*   ActivityRetainedComponent (ViewModel-level)
*   ActivityComponent
*   FragmentComponent
*   ViewComponent

This prevents memory leaks and ensures **correct lifecycle ownership** of dependencies.

### ✅ 3. **Dependency Graph = Directed Acyclic Graph**

No circular dependency between modules or bindings allowed → perfect for modular apps.

***

# ✅ Pattern 1: **Feature-level DI Modules (MOST IMPORTANT FOR MODULARIZATION)**

Large apps must avoid feature–feature dependencies.  
The DI structure must enforce this.

✅ Each feature has its own DI module:

    feature-payments/
        di/
            PaymentsModule.kt
            PaymentsBindings.kt

Bindings include:

*   Feature-specific repositories
*   UseCases
*   Mappers
*   Payment-specific network clients

Feature module exposes only **API contracts**, not internal implementation.

***

# ✅ Pattern 2: **API/Impl Split Using DI (Best Enterprise Pattern)**

Separate modules:

    feature-payments-api
    feature-payments-impl

### API module exposes:

*   Interfaces
*   Navigation contracts
*   UseCase interfaces

### Impl module provides implementations using DI:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class PaymentsBindings {
    @Binds
    abstract fun bindPaymentsRepo(impl: PaymentsRepoImpl): PaymentsRepository
}
```

✅ No circular dependencies  
✅ Only API modules are imported across features  
✅ Impl is hidden behind DI

***

# ✅ Pattern 3: **Multi-binding (for dynamic feature registration)**

Used when many feature modules must register themselves (e.g., Home tiles, Discover cards, Settings sections).

Example:

```kotlin
@IntoSet
@Binds
abstract fun bindHomeTile(tile: PaymentsTile): HomeTileProvider
```

All features contribute to a Set → app module collects and renders.

✅ Allows plugin-like architecture  
✅ Features remain fully isolated  
✅ No hardcoded registry

***

# ✅ Pattern 4: **Central Core Modules Provide Shared Dependencies**

You create shared “core” modules:

    core-network/
    core-database/
    core-analytics/
    core-auth/
    core-common/
    core-ui/

Each contains Hilt modules:

### Example:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideRetrofit(): Retrofit { ... }
}
```

✅ All features reuse same Retrofit instance  
✅ Token interceptor shared across app  
✅ Centralized policy enforcement (headers, logging, cert pinning)

***

# ✅ Pattern 5: **Using Hilt Scopes Correctly to Avoid Memory Leaks**

Correct scoping is essential:

| Scope                     | Lifetime            | Usage                            |
| ------------------------- | ------------------- | -------------------------------- |
| `@Singleton`              | App lifecycle       | Retrofit, DB, AuthManager        |
| `@ActivityRetainedScoped` | ViewModel lifecycle | UseCases, ViewModel dependencies |
| `@ActivityScoped`         | Activity            | Navigator, UI helpers            |
| `@FragmentScoped`         | Fragment            | Fragment-specific helpers        |
| `@ViewScoped`             | Compose/View        | View-level classes               |

This ensures:

✅ No leaking Activity into Singleton  
✅ No keeping Fragment alive in ViewModel  
✅ Controlled object lifecycle  
✅ Predictable cleanup

***

# ✅ Pattern 6: **EntryPoints for Non-Hilt Classes**

Sometimes you need DI from:

*   WorkManager Worker
*   BroadcastReceiver
*   ContentProvider

You use:

```kotlin
@EntryPoint
@InstallIn(SingletonComponent::class)
interface WorkerEntryPoint {
    fun analytics(): Analytics
}
```

Then access via:

```kotlin
val ep = EntryPoints.get(application, WorkerEntryPoint::class.java)
```

✅ Enables DI in non-Hilt Android components  
✅ Keeps object graph consistent

***

# ✅ Pattern 7: **Use Custom Scopes for Long-Lived BFSI Flows**

In BFSI flows (Loan onboarding, KYC, Payment):

You often have multi-screen journeys.  
You create:

*   @LoanFlowScope
*   @PaymentFlowScope
*   @KycScope

With a corresponding component or subcomponent.

This provides:

✅ Shared state across screens  
✅ One central UseCase instance  
✅ Memory cleanup when flow ends

Example:

    PaymentComponent
       ├─ PaymentUseCase
       ├─ PaymentValidator
       ├─ PaymentMapper

***

# ✅ Pattern 8: **Avoiding DI Bloat Using @AssistedInject**

For objects requiring runtime parameters:

```kotlin
class OtpViewModel @AssistedInject constructor(
    @Assisted val transactionId: String,
    val repository: OtpRepository
) : ViewModel()
```

AssistedFactory:

```kotlin
@AssistedFactory
interface Factory {
    fun create(transactionId: String): OtpViewModel
}
```

✅ Clean DI  
✅ Avoids passing runtime args through graph  
✅ Prevents state explosion

***

# ✅ Pattern 9: **Persistent DI Graph Verification (CI/CD)**

You MUST enforce DI consistency:

✅ Enable full Dagger/Hilt error reporting  
✅ Run build with `--strict-mode`  
✅ Validate no cycles in dependency graph  
✅ Reject PRs with incorrect DI structures

Large BFSI apps often have a **DI Verification Step** in CI.

***

# ✅ Pattern 10: **DI for Modular Navigation**

You use DI to inject navigation handlers, not concrete fragments.

Example:

```kotlin
interface PaymentsNavigator {
    fun openPaymentScreen()
}
```

Implemented in App module.  
Injected in feature modules.

✅ Prevents feature → feature dependency  
✅ Allows navigation changes without modifying features

***

# ✅ Interview-Ready Answer (Short Version)

**In enterprise BFSI apps, we use Hilt/Dagger to create a scalable, compile‑time validated dependency graph across 40+ modules.  
We isolate feature modules with API/Impl DI bindings, use core modules for shared dependencies (network, DB, analytics), enforce strict scoping rules, and use navigation contracts to avoid circular dependencies.  
Multi-binding, assisted injection, and custom component scopes let us handle dynamic features and long-lived processes like payments and KYC.  
This approach ensures clean boundaries, zero runtime DI errors, modular scalability, and predictable object lifecycles—critical for BFSI reliability and security.**

***

</details>

<details>
  <summary><strong>2. How do scopes work?</strong></summary>

***

# ✅ **How Do Scopes Work? (Hilt/Dagger)**

Scopes define **how long an object lives** and **who owns it** in dependency injection.  
They allow you to control:

✅ object lifetime  
✅ memory management  
✅ when objects are created/reused  
✅ when they get destroyed  
✅ how features share or isolate dependencies

In other words:

> **A scope ties an object’s lifecycle to a component’s lifecycle.**

***

# ✅ **1. Scope = Lifetime + Owner (Component)**

In DI, every object belongs to a **DI component**, and the scope defines:

    COMPONENT lifetime → scoped objects lifetime

Example:  
`@Singleton` objects live as long as the **Application component**.

When that component is created → the object is created.  
When that component dies → the object is destroyed.

***

# ✅ **2. How Scopes Work Internally (Dagger/Hilt)**

When you annotate a class with a scope:

```kotlin
@Singleton
class AuthManager @Inject constructor()
```

Dagger/Hilt ensures:

*   **Only one instance** is created per component
*   That instance is **cached** inside the generated component
*   Every injection request returns the **same instance**
*   If the component is recreated → the scoped instance is recreated

So Dagger maintains internal maps like:

    ComponentInstance = mapOf(
        AuthManager::class to storedInstance
    )

***

# ✅ **3. Scopes in Hilt (Lifecycle-Aware)**

Hilt comes with predefined Android-aware components:

| Scope                     | Component                 | Lifetime              | Ideal For                             |
| ------------------------- | ------------------------- | --------------------- | ------------------------------------- |
| `@Singleton`              | SingletonComponent        | Entire app            | Network, DB, Prefs                    |
| `@ActivityRetainedScoped` | ActivityRetainedComponent | Across config changes | ViewModel dependencies                |
| `@ActivityScoped`         | ActivityComponent         | Activity only         | Navigator, UI helpers                 |
| `@FragmentScoped`         | FragmentComponent         | Fragment only         | Fragment logic, adapters              |
| `@ViewModelScoped`        | ViewModelComponent        | VM lifespan           | UseCases, repositories interface impl |
| `@ViewScoped`             | ViewComponent             | View tree             | Compose-specific helpers              |

***

# ✅ **4. The Most Important Scopes (Explained Simply)**

## ✅ **4.1 @Singleton**

Lives for the entire app process.

Good for:

*   Retrofit / OkHttp
*   Room database
*   AuthManager / TokenManager
*   Encryption providers
*   Global repository implementations

### ✅ Why BFSI apps need Singleton scope

*   Tokens must remain consistent
*   Network client must reuse cert-pinning & interceptors
*   DB must be shared across app

***

## ✅ **4.2 @ActivityRetainedScoped**

Lives across configuration changes  
(e.g., rotation) — but not when activity finishes.

Good for:

*   UseCases that work with ViewModels
*   State machines for multi-step flows
*   Long-running tasks bound to UI lifecycle

***

## ✅ **4.3 @ViewModelScoped**

Object lives as long as its ViewModel.

Good for:

*   UseCases
*   Validators
*   Complex mappers
*   Long-running orchestrators (KYC, Loan)

Each ViewModel gets its own instance.

***

## ✅ **4.4 @ActivityScoped**

One instance per activity.

Good for:

*   Navigation handlers
*   Dialog controllers
*   Activity-level UI state
*   Sharing objects across multiple fragments in same activity

***

## ✅ **4.5 @FragmentScoped**

Created when Fragment is created.  
Destroyed when Fragment is removed.

Good for:

*   Fragment helpers
*   Fragment-level processors
*   Screen-specific adapters

***

# ✅ **5. Custom Scopes for BFSI Multi-Step Flows**

Enterprise BFSI apps often have long-running flows:

*   Money transfer
*   Loan onboarding
*   KYC
*   Insurance purchase
*   Investment flows

You can create custom scopes:

```kotlin
@Scope
@Retention(BINARY)
annotation class PaymentFlowScope
```

Using custom components:

```kotlin
@PaymentFlowScope
class PaymentFlowManager @Inject constructor()
```

This allows:

✅ Shared data across 5–10 screens of a flow  
✅ Automatic cleanup when flow ends  
✅ No leaking across unrelated areas

Perfect for **wizard-like** flows.

***

# ✅ **6. Why Scopes Matter in Multi-Module Architecture**

Without scopes:

❌ Every feature creates its own Retrofit instance  
❌ Memory leaks because instances are never destroyed  
❌ ViewModels hold objects longer than needed  
❌ Outdated data persists across screens

With proper scopes:

✅ Domain logic tied to ViewModel lifespan  
✅ Network/DB shared once (Singleton)  
✅ Navigation objects tied to Activity  
✅ Feature state isolated  
✅ Memory cleaned efficiently

***

# ✅ **7. Visual Diagram: How Scopes Map to Components**

    SingletonComponent (App lifecycle)
        @Singleton

     └─ ActivityRetainedComponent (Config-safe)
            @ActivityRetainedScoped

          └─ ViewModelComponent
                 @ViewModelScoped

               └─ ActivityComponent (Activity lifecycle)
                      @ActivityScoped

                   └─ FragmentComponent
                          @FragmentScoped

***

# ✅ **8. Scopes Prevent Bugs Like:**

*   Using Fragment-scoped object in Singleton → memory leak
*   Recreating expensive objects unnecessarily
*   Unpredictable state in multi-step flows
*   Losing state on rotation

Example bad practice:

```kotlin
class PaymentViewModel @Inject constructor(
    val retrofit: Retrofit  // ❌ expensive, should be Singleton-scoped
)
```

Correct:

```kotlin
@Singleton
fun provideRetrofit(): Retrofit
```

***

# ✅ **9. Summary (Interview-Ready)**

✅ **Scopes define how long objects live in DI.**  
✅ Tied to DI components (Singleton, Activity, Fragment, ViewModel).  
✅ Prevent unnecessary recreations & memory leaks.  
✅ Essential for multi-module BFSI apps with long-lived flows.  
✅ Custom scopes allow grouping objects by business flow.  
✅ Ensures predictable lifecycle, performance, and stability.

***

# ✅ **One-Liner Answer**

> **Scopes in Hilt/Dagger tie an object’s lifetime to the lifecycle of a DI component.  
> They ensure that objects are created once and reused correctly—preventing memory leaks, ensuring predictable state, and enabling scalable, multi-module BFSI architectures.**

***

</details>

<details>
  <summary><strong>3. How do you manage multi-module DI graphs?</strong></summary>

***

The key expectation:  
✅ Understand how Dagger/Hilt composes module graphs  
✅ How to separate DI per module  
✅ How to avoid cyclic dependencies  
✅ How to design API/Impl boundaries  
✅ How to align DI with feature modularization  
✅ How to keep builds fast and architecture clean

Let’s break it down.

***

# ✅ **1. The Core Strategy: "API Module + Impl Module" Pattern (Best for Large Apps)**

This is the *most scalable and enterprise-proof* pattern.

For each feature:

    feature-payments-api     → only exposes interfaces, navigation contracts, models
    feature-payments-impl    → contains actual implementations + DI bindings

### ✅ API module:

*   No Android deps
*   No DI implementation
*   Just **interfaces + pure contracts**

### ✅ Impl module:

Bound in DI:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class PaymentsBindings {
    @Binds
    abstract fun bindPaymentRepo(impl: PaymentRepoImpl): PaymentRepository
}
```

✅ Other modules depend ONLY on `feature-payments-api`  
✅ No cyclic dependencies  
✅ Implementation hidden (clean boundary)  
✅ DI graph stays predictable

This is the same pattern used in:

*   Google’s modular samples
*   Uber’s RIBs
*   Spotify, Airbnb modularization
*   Large BFSI apps in production

***

# ✅ **2. DI Modules Should Live INSIDE Each Feature Module**

Each feature module declares **its own DI module**.

Example folder structure:

    features/
       payments/
          impl/
             di/
                PaymentsModule.kt
                PaymentsNetworkModule.kt
                PaymentsBindings.kt

### ✅ Separation benefits:

*   Teams can evolve features independently
*   Build affects only local module
*   No cross-feature DI leakage
*   Clear ownership

This prevents “DI monoliths” in the app module.

***

# ✅ **3. Each Feature Has Its Own Graph Slice → Assembled by Hilt**

Hilt automatically **merges all feature-level modules** into the app graph.

You don’t manually create graphs per feature—Hilt does it.

### ✅ How it works internally:

*   Each `@InstallIn` annotation registers a module
*   Hilt aggregates modules into the correct component
*   Generates one final combined DI graph

This is why multi-module DI “just works.”

***

# ✅ **4. Use Multibinding for Feature Registration (Very Powerful)**

For apps like BFSI (banking, loans, insurance), dozens of features must “register” themselves.

Hilt supports **@IntoSet** or **@IntoMap** for dynamic, plugin-like architecture.

### Example:

```kotlin
@IntoSet
@Binds
abstract fun bindHomeCard(provider: PaymentsCardProvider): HomeCardProvider
```

The App module receives a **Set<HomeCardProvider>** from all features.

✅ No feature depends on any other  
✅ Features register themselves  
✅ App stitches UI together  
✅ Extremely scalable / independent

Perfect for:

*   Home screen tiles
*   Dynamic menus
*   Profile sections
*   Settings entries

***

# ✅ **5. Navigation Contracts for Cross-Feature Navigation (Prevents Cycles)**

Never inject screen classes from other modules.

Instead:

**Define interface in API module:**

```kotlin
interface PaymentsNavigator {
    fun openPaymentScreen(accountId: String)
}
```

**Provide implementation ONLY in app module:**

```kotlin
@Module
@InstallIn(ActivityComponent::class)
abstract class NavigatorBindings {
    @Binds
    abstract fun bindNavigator(impl: PaymentsNavigatorImpl): PaymentsNavigator
}
```

Features get navigator injected, do NOT know other feature internals.

✅ Keeps modules detached  
✅ Centralized navigation decisions  
✅ No circular dependencies  
✅ Clean layering

***

# ✅ **6. Use Feature-Level Scopes (Optional, But Very Useful)**

Certain flows (KYC, Payments, Loans) span many screens → require shared state.

Define custom scopes and components:

```kotlin
@Scope
annotation class PaymentScope
```

Use in DI:

```kotlin
@PaymentScope
class PaymentSessionManager @Inject constructor()
```

This allows:

✅ One session shared across screens  
✅ Auto cleanup when flow ends  
✅ Predictable lifecycle

BFSI loves this pattern.

***

# ✅ **7. Keep Global Dependencies in Core Modules**

Use dedicated modules placed under `/core`:

    core-network/
    core-database/
    core-common/
    core-ui/
    core-analytics/

Each contains its own Hilt modules:

    @InstallIn(SingletonComponent::class)
    object NetworkModule { ... }

✅ All features reuse the same Retrofit/Room/Auth  
✅ No duplication  
✅ Improved performance  
✅ Cleaner dependency graph

***

# ✅ **8. Enforce Boundaries With Custom Lint Rules + Dependency Graph Checks**

To avoid DI cycles or illegal imports:

✅ Custom lint rules  
✅ ArchUnit tests  
✅ Gradle dependency analysis plugin

Example:  
Block features depending on other feature implementations.

This protects DI graph integrity at scale.

***

# ✅ **9. EntryPoints for Non-Hilt Classes (Workers, Receivers)**

Use:

```kotlin
@EntryPoint
@InstallIn(SingletonComponent::class)
interface WorkerEntryPoint {
    fun repo(): PaymentRepository
}
```

Access via:

```kotlin
val ep = EntryPoints.get(appContext, WorkerEntryPoint::class.java)
```

✅ Maintains DI graph consistency across all components

***

# ✅ **10. CI/CD Graph Validation (Enterprise BFSI Requirement)**

Large apps enforce DI consistency via CI:

*   Build graph validation
*   No missing bindings
*   No unscoped objects in singleton modules
*   No circular dependencies
*   Strict build fails on invalid DI

This is essential for reliability/security.

***

# ✅ **11. DI Best Practices in Multi-Module BFSI Apps**

✅ One DI module per feature  
✅ No feature depends on another feature  
✅ Feature APIs via interface contracts  
✅ Use Impl modules for actual classes  
✅ Shared deps via core modules  
✅ Navigation via DI-injected interfaces  
✅ Multibinding for aggregation  
✅ Custom scopes for multi-step journeys  
✅ Keep ViewModels using `@HiltViewModel` for lifecycle awareness

***

# ✅ **Interview-Ready Summary (20 seconds)**

> “In multi-module apps, we manage DI graphs by using an API/Impl module pattern. Each feature has its own DI modules that expose only interfaces in the API module and implementations in the Impl module. Hilt assembles these into a global graph. We use multibinding for registration, navigation contracts to avoid feature-to-feature dependencies, and custom scopes for long flows like Payments/KYC/Loans. Core modules (network, database, analytics) provide singletons. We enforce boundaries via lint + CI checks. This approach scales to hundreds of modules and dozens of teams.”

***

</details>

<details>
  <summary><strong>4. When do you prefer ViewModelInject vs @Inject constructor?</strong></summary>

***

# ✅ **When do you prefer `@ViewModelInject` vs `@Inject constructor`?**

> **Short answer:**  
> ✅ `@ViewModelInject` → *Deprecated*. Use **`@HiltViewModel` + `@Inject` constructor** instead.  
> ✅ `@Inject` → Preferred for **all ViewModels** in modern Hilt setups.

But since this is an interview question, they expect the **historical context**, the **differences**, and **best‑practice guidance**.

Let’s break it down clearly.

***

# ✅ **1. Background: What was `@ViewModelInject`?**

`@ViewModelInject` was introduced early in Hilt to make ViewModel injection easy:

```kotlin
class HomeViewModel @ViewModelInject constructor(
    private val repo: Repo
) : ViewModel()
```

It worked with the Hilt-enabled `ViewModelFactory`.

### ❌ Problem:

It had limitations:

*   Could not handle **SavedStateHandle** cleanly
*   Could not support **Assisted injection**
*   Required special integration with lifecycle components
*   Limited flexibility for custom scopes

So Google deprecated it in **Hilt 2.31-alpha**.

***

# ✅ **2. What Replaced It? → `@HiltViewModel` + `@Inject constructor`**

Modern ViewModel DI:

```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: Repo,
    private val savedStateHandle: SavedStateHandle
) : ViewModel()
```

### ✅ Advantages:

*   Full **SavedStateHandle** support
*   Works with **ActivityRetainedComponent**
*   Works with **Navigation Component**
*   Supports assisted injection
*   Clean multi-module integration
*   Aligns with component-based scoping
*   Works with Compose `hiltViewModel()`

So — **this is the recommended approach for all modern Android apps.**

***

# ✅ **3. Direct Comparison (Architectural View)**

| Topic                 | `@ViewModelInject`    | `@Inject constructor` + `@HiltViewModel` |
| --------------------- | --------------------- | ---------------------------------------- |
| Status                | ❌ Deprecated          | ✅ Recommended                            |
| SavedStateHandle      | ❌ Hacky (`@Assisted`) | ✅ Native support                         |
| Lifecycle integration | Limited               | Full Hilt + ViewModel                    |
| Compose support       | ❌ No                  | ✅ Yes                                    |
| Architecture layers   | Tight coupling        | Clean separation                         |
| Multi-module use      | Hard                  | Very clean                               |
| Assisted injection    | Painful               | Native via `@AssistedInject` + factory   |
| Scoping               | Limited               | Full VM scope through Hilt               |

***

# ✅ **4. When Would You Still See `@ViewModelInject`?**

In **legacy apps** or older codebases (pre‑2021).

If your company has:

*   A huge monolith
*   Legacy MVVM architecture
*   Older Dagger+Hilt versions

You might see it **only because it hasn’t been migrated yet**.

✅ But you should **never use it in new code**.

***

# ✅ **5. For Interview Answer: When to Use Which?**

### ✅ When to use `@Inject constructor`

Use `@Inject` for ViewModel constructor **always**, but only WITH:

```kotlin
@HiltViewModel
```

This is modern, correct, scalable.

### ❌ When to use `@ViewModelInject`

**Never in new projects.**  
Only in:

*   Legacy code
*   Migration phases
*   Old Hilt versions (2.30 or earlier)

***

# ✅ **6. Context: Why `@Inject` is better in enterprise BFSI + modular apps**

Enterprise BFSI apps require:

✅ stability  
✅ predictability  
✅ modularity  
✅ large teams  
✅ strict scoping  
✅ consistent DI graphs  
✅ multi-module boundaries

The modern pattern:

    @HiltViewModel → binds VM into ActivityRetainedComponent
    @Inject → constructor injection
    SavedStateHandle → lifecycle-safe restore

…provides exactly that.

`@ViewModelInject` could not handle multi-module apps cleanly, especially when combining:

*   navigation graph scopes
*   feature module ViewModels
*   SavedStateHandle injection
*   custom scopes (KYC flow, Loan flow, Payment flow)

***

# ✅ **7. Final Interview-Ready Answer (30 seconds)**

> **`@ViewModelInject` was the old way to inject ViewModels in Hilt, but it had limitations and is deprecated.  
> In modern apps, especially multi-module BFSI projects, we always use `@HiltViewModel` + `@Inject constructor` because it supports SavedStateHandle, works with Navigation, scales with modular architectures, and integrates cleanly with lifecycle scopes.  
> I only use `@ViewModelInject` in legacy codebases that haven’t migrated yet.”**

***

</details>

<details>
  <summary><strong>5. How to inject different API implementations per environment?</strong></summary>

***

# ✅ **How to inject different API implementations per environment (Dev / QA / UAT / Prod) in Hilt/Dagger?**

This is a **very common enterprise BFSI interview question**, because different environments require different:

*   Base URLs
*   Interceptors
*   SSL cert configs
*   Mock implementations
*   Sandbox vs production logic

You must show clean DI patterns, zero runtime branching, and compile‑time safe switching.

Let’s go step-by-step.

***

# ✅ **Approach 1 — Build Variants (MOST COMMON & BEST PRACTICE)**

In Android, the cleanest way to inject different API implementations is using **product flavors + DI modules**.

### ✅ Step 1: Define product flavors

In your **app/build.gradle**:

```gradle
android {
    productFlavors {
        dev {
            dimension "env"
            buildConfigField "String", "BASE_URL", "\"https://dev.api.bank.com\""
        }
        qa {
            dimension "env"
            buildConfigField "String", "BASE_URL", "\"https://qa.api.bank.com\""
        }
        uat {
            dimension "env"
            buildConfigField "String", "BASE_URL", "\"https://uat.api.bank.com\""
        }
        prod {
            dimension "env"
            buildConfigField "String", "BASE_URL", "\"https://api.bank.com\""
        }
    }
}
```

### ✅ Step 2: Provide Retrofit base URL via Hilt

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideBaseUrl(): String = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(OkHttpClient.Builder().build())
            .build()
    }
}
```

✅ No branching in code  
✅ Environment decided at build time  
✅ Compile-time safe  
✅ Perfect for multi-module apps

***

# ✅ **Approach 2 — Environment-Specific DI Modules (Per Flavor Source Set)**

Put different Hilt modules in each flavor directory:

    app/
      src/dev/java/.../DevNetworkModule.kt
      src/qa/java/.../QaNetworkModule.kt
      src/uat/java/.../UatNetworkModule.kt
      src/prod/java/.../ProdNetworkModule.kt

### Example: src/dev/java/DevNetworkModule.kt

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DevNetworkModule {

    @Provides
    fun provideBaseUrl(): String = "https://dev.api.bank.com"

    @Provides
    @Singleton
    fun providePaymentsApi(retrofit: Retrofit): PaymentsApi =
        retrofit.create(PaymentsApi::class.java)
}
```

### Example: src/prod/java/ProdNetworkModule.kt

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object ProdNetworkModule {
    
    @Provides
    fun provideBaseUrl(): String = "https://api.bank.com"
}
```

✅ Best for complex scenario (mock APIs, different interceptors, sandbox logic)  
✅ Allows alternate implementations per environment

***

# ✅ **Approach 3 — @Qualifier Annotations (Multiple Impl in Same Build)**

Useful for apps supporting runtime switching between environments (rare in BFSI, but common for internal QA tools).

### Step 1: Define qualifiers

```kotlin
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DevApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProdApi
```

### Step 2: Provide multiple API implementations

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @DevApi
    @Provides
    fun provideDevApi(): PaymentsApi =
        Retrofit.Builder()
            .baseUrl("https://dev.api.bank.com")
            .build()
            .create(PaymentsApi::class.java)

    @ProdApi
    @Provides
    fun provideProdApi(): PaymentsApi =
        Retrofit.Builder()
            .baseUrl("https://api.bank.com")
            .build()
            .create(PaymentsApi::class.java)
}
```

Then choose at injection site:

```kotlin
class Repository @Inject constructor(
    @ProdApi private val api: PaymentsApi
)
```

✅ Compile-time safety  
✅ Cleaner than if-else  
✅ Supports side-by-side implementations

✅ Used when only SOME repos/services differ per environment.

***

# ✅ **Approach 4 — Environment Manager + Runtime Selection**

Useful for apps needing configurable backend (rare for production BFSI but used in testing/UAT tools).

```kotlin
class EnvironmentManager @Inject constructor() {
    var env: Env = Env.PROD
}
```

Provide API based on environment:

```kotlin
class RetrofitFactory @Inject constructor(
    private val envManager: EnvironmentManager
) {
    fun createRetrofit(): Retrofit {
        val baseUrl = when(envManager.env) {
            Env.DEV -> DEV_URL
            Env.QA -> QA_URL
            Env.PROD -> PROD_URL
        }
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .build()
    }
}
```

⚠️ This breaks full DI purity, but works for internal debugging menus.

***

# ✅ **Approach 5 — Dynamic Config Distribution (Enterprise BFSI)**

BFSI apps often need **remote-config-driven environments** (security teams prefer no hardcoded URLs).

Inject config provider:

```kotlin
interface EnvConfigProvider {
    fun baseUrl(): String
}
```

Different implementations:

*   StaticProvider (Prod)
*   RemoteConfigProvider (Dev/UAT)
*   FeatureFlagProvider

Bound via DI based on flavor.

***

# ✅ **Which Approach Should You Pick? (Summary)**

### ✅ Use Build Flavors

**For 95% of enterprise BFSI apps.**  
Cleanest, safest, no runtime risk.

### ✅ Use Flavor-specific DI modules

**When each environment needs unique interceptors, certificates, or APIs.**

### ✅ Use Qualifiers

**When you need multiple APIs inside the same build** (sandbox + prod in a QA build).

### ✅ Use Runtime Switching

**Only for internal testers**, never for production.

***

# ✅ **Interview-Ready Answer (20 seconds)**

> **I inject different API implementations per environment using build variants and flavor-specific DI modules. Each environment (Dev/QA/UAT/Prod) has its own Hilt module providing base URLs, interceptors, SSL configs, and repository bindings.  
> I avoid runtime switching in production and rely on compile-time flavor separation or qualifiers for side-by-side implementations. This keeps DI graphs clean, secure, and scalable in multi-module BFSI architectures.**

***

</details>

---

### Shared Components

<details>
  <summary><strong>1. Explain your strategy for managing shared components across multiple features.</strong></summary>

***

# ✅ **What Are “Shared Components” in a Multi‑Module App?**

Shared components typically include:

### ✅ Shared UI

*   Buttons, text fields, loaders, chips, cards
*   Common Compose components
*   Design system theming
*   Animations

### ✅ Shared Logic / Utilities

*   Extensions
*   Validators
*   Crypto helpers
*   JSON adapters
*   Logger

### ✅ Shared Domain Logic

*   Entities
*   Use cases
*   Value objects

### ✅ Shared Infrastructure

*   Retrofit + OkHttp
*   Room DB
*   DataStore
*   Token manager
*   Auth interceptors
*   Network response mappers

The challenge is:

❌ NOT letting features depend on each other  
✅ WHILE making common things reusable, versioned, safe, stable

***

# ✅ **My Strategy: 6-Layer Shared Module Architecture**

This is the architecture used in large BFSI projects (HDFC, ICICI, HSBC, etc.), fintech apps, and enterprise apps with multiple teams.

                               app
                                |
      ------------------------------------------------------------
      |                |                |                        |
    feature-payments   feature-loans    feature-kyc   ... (50+ features)
      |                |                |                        |
      ------------------------------------------------------------
        |       |        |        |        |       |       |
    core-ui  core-common core-network  core-db  core-domain core-analytics core-config

Each “core” becomes a **shared module** for a different concern.

***

# ✅ **1. core-ui → Shared UI for All Features**

This module contains:

✅ Compose components (Design System)  
✅ Typography, colors, spacing  
✅ Buttons, input text fields, radio buttons, checkboxes  
✅ Loading indicators  
✅ Error/Empty screens  
✅ Navigation bars  
✅ Snackbars, dialogs

### Why?

*   Prevent UI duplication across 200 screens
*   Ensure brand consistency
*   Allow central theming

### Rule:

**core-ui must NOT depend on any feature module.  
Feature modules depend on core-ui.**

This avoids circular dependencies.

***

# ✅ **2. core-domain → Shared Business Models + UseCases**

Contains:

✅ Domain entities  
✅ Value objects (Amount, MobileNumber, IFSC, PAN)  
✅ Domain validation rules  
✅ Global use cases (GetUserProfile, ValidatePan)  
✅ Repository interfaces

### Why?

*   Domain logic is stable
*   Multiple features need the same business rules
*   Feature modules should not share domain classes directly with each other

### Rule:

**Domain depends on NOTHING.  
All features depend on domain.**

Clean Architecture enforced.

***

# ✅ **3. core-network → Retrofit/OkHttp infrastructure**

Contains:

✅ Retrofit builder  
✅ OkHttp builder  
✅ Interceptors  
✅ Logging + encryption interceptors  
✅ Certificate pinning  
✅ Error mappers  
✅ Network models (DTOs)  
✅ Common API response wrappers

### Why?

*   Networking must be standardized
*   Token refresh logic must be centralized
*   Avoid each feature creating its own Retrofit instance

### Rule:

**core-network exposes interfaces;  
actual feature-specific API interfaces remain in feature modules.**

***

# ✅ **4. core-db → Shared Room Database & DAOs**

Contains:

✅ AppDatabase  
✅ Common entities: User, Tokens, SyncQueue  
✅ DAOs for app-wide tables  
✅ Migrations

### Why?

*   Avoid having Room databases per feature
*   Centralize storage, migration, encryption

### Rule:

**core-db is shared;  
feature-db (feature-specific tables) lives in feature modules.**

***

# ✅ **5. core-common → Shared Utilities**

Contains:

✅ Kotlin extensions  
✅ Date/Time utils  
✅ Encryption helpers  
✅ Network connectivity checker  
✅ Resource helpers

### Why?

*   Reduce duplication
*   Provide safe utilities available everywhere
*   Keep utility logic out of business/domain layers

### Rule:

**Must NOT include Android UI dependencies.**

***

# ✅ **6. core-navigation → Navigation Contracts for Cross-Feature Navigation**

Instead of:

❌ Feature A calling Feature B directly

We define *contracts*:

```kotlin
interface PaymentsNavigator {
    fun openPaymentScreen(accountId: String)
}
```

App module binds implementation.

✅ Removes cross-feature dependency  
✅ Prevents circular dependencies  
✅ Makes navigation pluggable  
✅ Features remain independent

***

# ✅ **Dependency Rules to Prevent Coupling**

    app → feature-modules → core-modules → domain
    data → domain
    feature → domain
    feature → core
    core → nothing (except domain)

### Must Not Happen:

❌ feature → feature  
❌ core → feature  
❌ domain → Android SDK  
❌ domain → network/db

This keeps shared components clean and independent.

***

# ✅ **How DI Helps Manage Shared Components**

Using **Hilt/Dagger**, you provide shared dependencies in core modules:

### Example: core-network

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides @Singleton
    fun provideRetrofit(): Retrofit = ...
}
```

All features reuse them without knowing the implementation.

✅ Single instance per app  
✅ No duplication  
✅ Compile-time safety

***

# ✅ **Versioning Shared Components**

When many teams use shared modules:

### ✅ Use semantic versioning (core-ui:v1.2.1)

### ✅ Keep the public API stable

### ✅ Minimize breaking changes

### ✅ Use backward-compatible migration pattern

***

# ✅ **Enforcing Boundaries (Lint + Gradle)**

I enforce shared component boundaries using:

✅ Gradle dependency constraints  
✅ Custom lint rules  
✅ ArchUnit architecture tests  
✅ CI/CD checks for dependency graph violations

Prevents accidental imports like:

❌ core-ui importing feature-payments  
❌ domain importing Retrofit

***

# ✅ **Summary (Interview-Ready 20 sec Answer)**

> **I manage shared components by placing them in dedicated core modules—core-ui, core-network, core-db, core-domain, core-common, and core-navigation.  
> Features depend only on these core modules and never on each other, which prevents coupling and allows clean modular scaling.  
> DI (Hilt) provides shared instances from these modules, while navigation contracts avoid feature-to-feature dependencies.  
> We enforce boundaries using lint rules, CI dependency checks, and clear dependency direction so shared components remain stable, isolated, and reusable across 50+ feature modules.**

***

</details>

<details>
  <summary><strong>2. How do you design UI component libraries?</strong></summary>

***

I’ll cover:

✅ Architecture  
✅ Layers of UI library (atoms → molecules → organisms → templates)  
✅ API design principles  
✅ Theming & tokens  
✅ Do’s/Don'ts  
✅ Versioning & adoption strategy  
✅ Multi‑module integration  
✅ Example folder structure

Let’s build your answer.

***

# ✅ **How to Design UI Component Libraries (Enterprise Strategy)**

A UI component library is a **shared design system** implemented in code.  
It ensures:

*   Consistent UI across 200+ screens
*   Rapid development
*   Reusability
*   Easy theme changes
*   Clear accessibility compliance
*   Designer–developer alignment

To build an effective UI library, use these patterns.

***

# ✅ **1. Follow the Atomic Design System (Strongest, Widely Used)**

### ✅ **Atoms**

Smallest components:

*   Text
*   Buttons
*   Input fields
*   Checkboxes
*   Switches
*   Divider
*   Icons
*   Colors, typography

### ✅ **Molecules**

UiPieces composed of atoms:

*   Search bar
*   Amount input field
*   OTP field
*   Address form row
*   Card with title + icon

### ✅ **Organisms**

Complex reusable sections:

*   Account summary widget
*   Payment summary card
*   Step progress indicator
*   Offer carousel

### ✅ **Templates**

Reusable screen layouts:

*   Full‑screen form
*   List with header
*   Dashboard layout

This hierarchical structure prevents bloating and encourages reuse.

***

# ✅ **2. Use a Token‑Based Design System (Critical for Branding)**

All styling choices should be defined as **tokens**:

### ✅ **Color tokens**

```kotlin
object AppColors {
    val Primary = Color(0xFF0066FF)
    val Secondary = Color(0xFFEFEFEF)
    val Danger = Color(0xFFFF5555)
}
```

### ✅ **Typography tokens**

```kotlin
object AppTypography {
    val H1 = TextStyle(fontSize = 22.sp, fontWeight = Bold)
    val Body = TextStyle(fontSize = 16.sp)
}
```

### ✅ **Spacing, shape, elevation tokens**

This makes theme changes effortless globally.

✅ Dark mode  
✅ Brand redesign  
✅ White-label product variants

All handled with tokens, not per‑component overrides.

***

# ✅ **3. Strict API Design Principles (Make Components Scalable)**

### ✅ Keep parameters minimal but flexible

Bad:

```kotlin
Button(label: String, color: Color, size: Int, padding: PaddingValues…)
```

Good:

```kotlin
AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: ButtonType = ButtonType.Primary
)
```

Component decides:

*   color
*   size
*   shape

based on **type**.

### ✅ Don’t expose internal styling

Keep styling internal; only expose high-level intent.

***

# ✅ **4. Encapsulate Business Reuse into Higher-Level Components**

Example for BFSI:

✅ **AmountInputField**

*   Currency icon
*   Decimal validation
*   Max limit highlighting
*   Error handling

✅ **OtpInputField**

*   Auto-focus
*   Visual grouping
*   Security masking

✅ **AccountCard**

*   Masked account number
*   IFSC display
*   Balance highlighting
*   Bank logo

This ensures all screens use the same behavior.

***

# ✅ **5. Make Components Stateless + Provide State Helpers**

Never make components hold state themselves.

Use:

```kotlin
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    …
)
```

For convenience:

```kotlin
@Composable
fun rememberTextFieldState(initial: String) = remember { mutableStateOf(initial) }
```

✅ Easier testing  
✅ No side-effects  
✅ Works with MVVM/MVI

***

# ✅ **6. Accessibility and Localization Should Be Built‑In**

Include:

✅ Content descriptions  
✅ Larger touch targets  
✅ Scalable text (SP, not DP)  
✅ RTL support  
✅ Color contrast compliance

Enterprise BFSI apps REQUIRE accessibility compliance (WCAG AA).

***

# ✅ **7. Use Multi‑Module Structure for UI Library**

A scalable layout:

    core-ui/
        tokens/
            colors.kt
            typography.kt
            shapes.kt
            spacing.kt
        atoms/
            AppText.kt
            AppButton.kt
            AppCheckbox.kt
        molecules/
            InputField.kt
            SearchBar.kt
            AmountInput.kt
        organisms/
            AccountCard.kt
            PaymentTile.kt
            StepIndicator.kt
        templates/
            FormScreen.kt
            ListScreen.kt

### ✅ Feature modules depend on core-ui

Not vice versa → avoids circular dependencies.

***

# ✅ **8. Versioning & API Stability**

Shared UI modules should use:

### ✅ Semantic versioning

*   Breaking changes → major version
*   Non-breaking additions → minor
*   Fixes → patches

### ✅ Deprecate instead of deleting

Give teams 1–2 sprints to adopt new components.

***

# ✅ **9. Documentation, Playground & Figma Integration**

Provide:
✅ Storybook-like demo inside Android (UI catalog screen)  
✅ Figma-to-Code mapping  
✅ Code examples for each component  
✅ Usage guidelines (“When to use”, “When not to use”)

Clear guidelines reduce misuse.

***

# ✅ **10. Testing Strategy**

### ✅ Component Tests

Use Compose UI testing for:

*   Visual correctness
*   State transitions
*   Accessibility

### ✅ Snapshot Tests

Keep visual baseline for entire design system.

### ✅ Golden Layout Testing (Optional)

Pixel-level comparison for theming consistency.

***

# ✅ **11. Performance Optimization**

Especially for BFSI apps with heavy UI:

✅ Use `remember`  
✅ Use `derivedStateOf`  
✅ Prevent unnecessary recompositions  
✅ Pre-allocate icons, shapes, animations  
✅ Avoid recomposing heavy layouts  
✅ Lazy (on-demand) loading for lists & carousels

***

# ✅ **12. Avoid Common Anti‑Patterns**

❌ Putting business logic inside UI components  
❌ Allowing feature modules to override core components  
❌ Using “generic component from component” model (over-engineering)  
❌ Providing 50+ parameters  
❌ Using colors/fonts directly instead of tokens  
❌ Non-themable components  
❌ State inside shared UI elements

***

# ✅ **Interview‑Ready Summary (30 Seconds)**

> **I design UI component libraries using atomic design (atoms → molecules → organisms → templates), powered by a token-based design system for colors, typography, spacing, shapes, and elevation.  
> Components are stateless, themeable, accessible, and expose a minimal API so behavior is consistent across 200+ screens.  
> The library lives in a separate `core-ui` module, used by all feature modules.  
> Higher-level BFSI components (AmountInput, OTPField, AccountCard) encapsulate repeated patterns.  
> We enforce strict boundaries, provide documentation/playground, version the library properly, and ensure accessibility & performance.  
> This enables consistent, scalable, maintainable UI across large enterprise apps.**

***

</details>

<details>
  <summary><strong>3. How do you design a domain-wide analytics component?</strong></summary>

***

# ✅ **How do you design a domain‑wide analytics component?**

(Especially for large BFSI + multi‑module Android apps with 100+ features and strict compliance.)

A domain‑wide analytics component must be:

✅ **Modular**  
✅ **Configurable**  
✅ **Feature‑agnostic**  
✅ **Zero‑coupling**  
✅ **Compliant with BFSI regulations**  
✅ **Secure (PII-safe)**  
✅ **Extensible for new events**  
✅ **Consistent across all screens and features**  
✅ **Able to work offline-first + batch upload**

Here’s the complete design strategy.

***

# ✅ 1. **Create a Dedicated Analytics Module (core-analytics)**

You NEVER put analytics logic in feature modules.

Instead create:

    core-analytics/
        AnalyticsApi.kt
        AnalyticsEvent.kt
        AnalyticsLogger.kt
        AnalyticsConfig.kt
        providers/
        interceptors/

This module:

✅ Is imported by all features  
✅ Does NOT depend on any feature  
✅ Provides a single entry point → SSOT for analytics  
✅ Keeps features clean  
✅ Enforces consistency

***

# ✅ 2. **Define a Strong, Type‑Safe Event Model (NO STRINGS)**

Never allow:

```kotlin
analytics.log("screen_opened", mapOf("id" to "123"))
```

This leads to:

❌ typos  
❌ inconsistent naming  
❌ missing parameters  
❌ runtime crashes

Instead define sealed classes:

```kotlin
sealed class AnalyticsEvent {

    data class ScreenView(
        val screenName: ScreenName,
        val additionalParams: Map<String, Any> = emptyMap()
    ) : AnalyticsEvent()

    data class ButtonClick(
        val buttonName: ButtonName,
        val metadata: Map<String, Any> = emptyMap()
    ) : AnalyticsEvent()

    data class TransactionEvent(
        val txnId: String,
        val productType: String,
        val amount: Double
    ) : AnalyticsEvent()

}
```

✅ Compile-time safe  
✅ Easy auto-completion  
✅ Clear ownership and versioning  
✅ Easy to refactor  
✅ Fully testable

***

# ✅ 3. **Define Analytics Interfaces (Dependency Inversion)**

Expose a pure Kotlin contract:

```kotlin
interface AnalyticsLogger {
    fun logEvent(event: AnalyticsEvent)
}
```

Feature modules depend on this contract—not the implementation.

Implementation will live in:

*   `core-analytics-impl` (if using API/Impl pattern)
*   or inside the same module but hidden behind DI

***

# ✅ 4. **Use a Pipeline Architecture (Enterprise Best Practice)**

A domain-wide analytics system works like a **pipeline**:

    Feature → AnalyticsLogger → Processor → Filters → Mappers → Sender

### ✅ Architecture:

1️⃣ Input (event model)  
2️⃣ Pre-processor (flattening, enrichment)  
3️⃣ Filters (PII removal, sampling, GDPR filters)  
4️⃣ Transformers (map to 3rd party schema)  
5️⃣ Sink dispatchers (Firebase, Amplitude, Clevertap, Datadog, Custom backend)  
6️⃣ Offline Store (Room DB or local queue)  
7️⃣ Upload manager (WorkManager)

This is exactly how large apps like GPay/PhonePe scale analytics.

***

# ✅ 5. **Multi‑Provider Support (Very Important)**

BFSI apps usually need to log events to **multiple analytics providers**, like:

*   Firebase Analytics
*   Clevertap
*   Mixpanel
*   Datadog
*   NewRelic
*   Internal backend event pipeline

Use the **Composite pattern**:

```kotlin
class CompositeAnalyticsLogger @Inject constructor(
    private val providers: Set<@JvmSuppressWildcards AnalyticsProvider>
) : AnalyticsLogger {

    override fun logEvent(event: AnalyticsEvent) {
        providers.forEach { it.log(event) }
    }
}
```

This lets you plug/unplug providers via DI, not via code changes.

***

# ✅ 6. **Use Hilt Multibinding (Cleanest for Multi-Modules)**

Each analytics provider registers itself:

```kotlin
@Binds
@IntoSet
abstract fun bindFirebaseProvider(impl: FirebaseAnalyticsProvider): AnalyticsProvider
```

For Clevertap:

```kotlin
@Binds
@IntoSet
abstract fun bindClevertapProvider(impl: ClevertapAnalyticsProvider): AnalyticsProvider
```

This is PERFECT for multi-module apps:

✅ No module knows which providers exist  
✅ Each provider is independent  
✅ App module controls actual registration  
✅ Feature modules always call one Analytics API

***

# ✅ 7. **Event Enrichment Layer (Critical for BFSI)**

Analytics event must be enriched with:

*   app version
*   device info
*   user ID (token safe, hashed)
*   session ID
*   timestamp (server + device)
*   network type
*   product vertical

Use an interceptor-style pattern:

```kotlin
interface AnalyticsInterceptor {
    fun intercept(event: AnalyticsEvent): AnalyticsEvent
}
```

Registered via DI:

✅ Device info interceptor  
✅ PII scrubber interceptor  
✅ Common metadata interceptor

***

# ✅ 8. **PII Masking + Security (Mandatory for BFSI)**

Create a PII Scrubber:

```kotlin
class PiiInterceptor : AnalyticsInterceptor {
    override fun intercept(event: AnalyticsEvent): AnalyticsEvent {
        // strip or hash sensitive fields
    }
}
```

Ensure:

✅ no PAN  
✅ no Aadhaar  
✅ no account number (only last 4 digits or hashed)  
✅ no OTP  
✅ no raw phone numbers

This keeps analytics **secure & compliant**.

***

# ✅ 9. **Offline-First + Batched Upload (Enterprise-Scale)**

Analytics events should NEVER block UI or require network.

Use:

✅ WorkManager  
✅ Local Room DB queue  
✅ Batching  
✅ Retry with exponential backoff

Example:

```kotlin
@Entity
data class AnalyticsEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val eventJson: String,
    val timestamp: Long
)
```

Uploader Worker:

*   Batches 20–50 events
*   Sends them
*   Marks as delivered
*   Retries on failure

This is how robust BFSI apps ensure data integrity.

***

# ✅ 10. **Versioning Events for Backward Compatibility**

Always version event schema:

```kotlin
data class ScreenView(
    val screenName: String,
    val version: Int = 1,   // event contract version
    val metadata: Map<String, Any>
)
```

This allows server teams to update dashboards without breaking.

***

# ✅ 11. **Support for Feature Modularization**

Analytics must NOT introduce circular dependencies.

All features depend on:

    :core-analytics

But :core-analytics depends on **nothing** except Kotlin + maybe core-common.

Use:

✅ API/Impl split  
✅ Multibinding  
✅ Navigation analytics via interface injection

***

# ✅ 12. **Provide Analytics DSL for Ease of Use**

Make analytics calls concise:

```kotlin
analytics.log {
    screen("PaymentsScreen")
    param("accountId", maskedAccount)
}
```

Even better with Compose:

```kotlin
AnalyticsScreenView(screen = "Dashboard")
```

or:

```kotlin
AnalyticsButtonClick("TransferButton")
```

Teams LOVE this.

***

# ✅ 13. **Telemetry Dashboard & Controls**

Provide central controls:

✅ Enable/disable analytics  
✅ Enable verbose logging in Dev builds  
✅ Event preview screen (debug-only)  
✅ Sampling logic (especially for heavy events)

This helps QA validate analytics events easily.

***

# ✅ 14. **CI/CD Validation**

Use static checks:

*   Analytics event ID duplication
*   Missing mandatory fields
*   Incorrect event naming pattern
*   No PII leakage

This protects the pipeline from incorrect events.

***

# ✅ **Final Interview-Ready Summary (30 Seconds)**

> **A domain-wide analytics component is a dedicated shared module (`core-analytics`) that exposes a type-safe event API.  
> It uses a pipeline architecture with interceptors, PII scrubbing, enrichment, multibinding for multiple providers, offline batching with WorkManager, strict event versioning, and clean DI boundaries.  
> Features log events through a single unified interface, not direct provider APIs.  
> This ensures consistency, security, modularity, and high reliability—critical for large BFSI apps.**

***

</details>

---

## Topic 3: System Design for Mobile (Live Architecture Design)

### Mobile Banking App Architecture

<details>
  <summary><strong>1. Design a secure mobile banking app architecture.</strong></summary>

***

I’ll break it into:

✅ High‑Level Architecture  
✅ Mobile App Architecture  
✅ Security Layers (App + Network + Backend)  
✅ Authentication  
✅ API Gateway + Backend Architecture  
✅ Data Flow + Encryption Strategy  
✅ Threat Models & Mitigations  
✅ Diagram (text-based, interview-friendly)  
✅ Scalability & Observability  
✅ Compliance Considerations

***

# ✅ **1. High-Level Architecture (End‑to‑End)**

    +----------------------+
    |     Mobile App       |
    | (Android/iOS/Flutter)|
    +----------+-----------+
               |
               v
    +----------------------+
    |   App Security Layer |
    | (Root detection, TLS |
    |  cert pinning, SCA)  |
    +----------+-----------+
               |
               v
    +----------------------+
    |  API Gateway / WAF   |
    | (AuthZ, rate-limit,  |
    |   bot detection)     |
    +----------+-----------+
               |
               v
    +----------------------+
    |  IAM/Auth Server     |
    | (OAuth2, OpenID, MFA)|
    +----------+-----------+
               |
               v
    +----------------------+
    |   Microservices      |
    | (Accounts, Payments, |
    | Loans, KYC, Profile) |
    +----------+-----------+
               |
               v
    +----------------------+
    |  Core Banking System |
    +----------------------+

***

# ✅ **2. Mobile App Architecture (Front-End)**

### ✅ **Clean Architecture + Modularization**

*   `app`
*   `core-ui` (Design System)
*   `core-network` (Retrofit/OkHttp, encryption)
*   `core-database` (Encrypted Room/SQLCipher)
*   `feature-accounts`
*   `feature-payments`
*   `feature-auth`
*   `feature-kyc`
*   `feature-notifications`
*   `analytics`

### ✅ MVVM + MVI Hybrid

*   MVVM for most screens
*   MVI for multi-step secure flows:
    *   KYC
    *   Payments (IMPS/NEFT/RTGS)
    *   Loan & onboarding flows

### ✅ On-device security

*   **Encrypted SharedPreferences / DataStore**
*   **Hardware-backed Keystore** for:
    *   OAuth tokens
    *   Refresh tokens
    *   Device binding keys
*   **Root/Jailbreak detection**
*   **Runtime tamper detection**
*   **Certificate pinning**
*   **Biometric authentication**
*   **Key attestation**

### ✅ Secure Local Storage

*   Use **SQLCipher** or `Room + EncryptedFile`
*   Never store card numbers or CVV
*   Mask account numbers
*   Offline storage only for *non-sensitive* cached data

***

# ✅ **3. Authentication (Zero Trust + MFA)**

### ✅ Multi-factor authentication flow

1.  User logs in with mobile number + OTP
2.  Device binding using hardware-backed keys
3.  Access-token issued (short-lived)
4.  Refresh token stored in Keystore
5.  Per-transaction authentication:
    *   Biometric
    *   OTP
    *   UPI PIN / M-PIN (hashed, not stored)

### ✅ OAuth2 + OpenID Connect

*   Token flow: Auth Code + PKCE
*   Session expiration & silent refresh
*   Token revocation support

### ✅ Fraud & Identity

*   Device ID fingerprinting
*   Risk scoring APIs
*   Geo-velocity checks

***

# ✅ **4. Network Security**

### ✅ Strict TLS 1.2+

### ✅ Certificate Pinning (Pin 2–3 certs for rotation)

### ✅ Mutual TLS (mTLS) — Ideal for BFSI

### ✅ WAF + API Gateway Protection

*   Rate limiting
*   Bot detection
*   DDoS protection
*   Geo-blocking
*   Threat intelligence rules

***

# ✅ **5. Backend Architecture (Domain-wise Microservices)**

    +----------------------+
    |      API Gateway     |
    +----------------------+
             |
             v
    +---------------------------------------------+
    |                Microservices                |
    +------+------------------+-------------------+
           |                  |
           v                  v
    +--------------+   +-----------------+
    | Accounts-MS  |   | Payments-MS     |
    | Balances,    |   | IMPS/NEFT/RTGS  |
    | statements   |   | Limits, receipts|
    +--------------+   +-----------------+
           |                  |
           v                  v
    +--------------+   +-----------------+
    | KYC-MS       |   | Loans-MS        |
    | OCR, FRS     |   | Eligibility,    |
    | Document Ver |   | EMIs, Offers    |
    +--------------+   +-----------------+
             |
             v
    +-------------------------------+
    |       Core Banking System     |
    | (CBS: Finacle, T24, Flexcube) |
    +-------------------------------+

### ✅ Each microservice uses:

*   Spring Boot / Node / Go
*   JWT token validation
*   Redis caching
*   Kafka for async events (receipts, notifications)
*   PostgreSQL or Oracle
*   Vault for secrets

***

# ✅ **6. Data Flow (Secure End-to-End)**

### ✅ Flow Example: Balance Inquiry

1.  User opens screen → ViewModel triggers API
2.  Request signed with OAuth token
3.  TLS + certificate pinning validation
4.  Gateway validates token
5.  Accounts‑MS fetches from Core Banking
6.  Response encrypted in-transit
7.  App decrypts → shows masked data
8.  Store minimal locally using encrypted DB

***

# ✅ **7. Payments Flow (High-Security Example)**

    User → Biometric → Create Transfer Intent
        → Validate Beneficiary → Amount Check → Risk Scoring
            → Transaction Token (one-time)
                → Payment-MS executes
                    → Callback / Receipt generation

Security measures:

✅ Multi-layer validation  
✅ Token-based payment authorization  
✅ Per-transaction encryption  
✅ Device + session binding

***

# ✅ **8. Security Controls Required in BFSI**

### ✅ Static Security

*   Obfuscation (R8/ProGuard)
*   Build fingerprint checks
*   Anti-debugging

### ✅ Dynamic Security

*   Tamper detection
*   Runtime Integrity checks
*   Root check (with API fallback)
*   Emulator detection

### ✅ Data Protection

*   No sensitive info in logs
*   Clipboard protection
*   Screenshot prevention on sensitive screens
*   Data minimization

### ✅ Session Security

*   Auto logout timer
*   Token rotation
*   Concurrent login detection

***

# ✅ **9. Analytics & Monitoring**

### ✅ Client-Side Analytics (Secure)

*   Event batching via WorkManager
*   PII scrubber
*   Encrypted payload
*   Real-time fraud signals

### ✅ Server-Side Monitoring

*   Distributed tracing (Jaeger)
*   Log aggregation (ELK, Datadog)
*   Anomaly detection

***

# ✅ **10. Compliance & Regulatory Requirements**

### ✅ Mandatory in BFSI:

*   RBI compliance
*   PCI-DSS
*   ISO 27001
*   GDPR/Data Privacy
*   SOC 2

Design must support:

✅ Encryption-at-rest & in-transit  
✅ Data minimization  
✅ Audit logs  
✅ Immutable transaction logs  
✅ Incident response readiness

***

# ✅ **11. Scalable Deployment Architecture**

### ✅ Use:

*   Kubernetes (GKE/EKS/AKS)
*   Auto-scaling
*   Canary deployments
*   Blue-green deployment for sensitive services
*   API Gateway (Apigee, Kong, AWS API Gateway)

### ✅ SLA & SLO

*   99.99% uptime for auth
*   99.9% for transactions

***

# ✅ **12. Summary (Interview‑Ready 30-second Answer)**

> **I design a secure mobile banking architecture using a modular Android app with Clean Architecture, hardware-backed Keystore storage, certificate pinning, TLS 1.2+, root/tamper detection, and encrypted local storage.  
> On the backend, I use API Gateway + OAuth2/OpenID for authentication, microservices for accounts/payments/KYC/loans, event-driven architecture using Kafka, and secure integration with the core banking system.  
> All communication is signed, encrypted, and validated with mutual TLS.  
> The system includes fraud scoring, monitoring, multi-provider analytics with PII scrubbing, and strict compliance with PCI-DSS, GDPR, and RBI guidelines.**

***

</details>

<details>
  <summary><strong>2. How do you protect tokens?</strong></summary>

***

This covers:

✅ OAuth tokens (Access/Refresh)  
✅ Device binding keys  
✅ Token theft prevention  
✅ Storage rules  
✅ Network security  
✅ Backend protections  
✅ Threat model coverage  
✅ BFSI-compliant controls

Let’s go step by step.

***

# ✅ **How Do You Protect Tokens? (Complete Architecture)**

**Tokens = the most sensitive asset** in a mobile banking system.  
If stolen → attacker can bypass login and make fraudulent transactions.

So the protection strategy spans:

### ✅ On‑Device Security

### ✅ Network Security

### ✅ Backend Security

### ✅ Token Lifecycle

### ✅ Threat Detection

Let’s cover each in detail.

***

# ✅ **1. On‑Device Token Protection (Android)**

This is the MOST important area because the device is the weakest link.

## ✅ 1.1 Use Hardware‑Backed Keystore (TEE / StrongBox)

Store Refresh Tokens, Session Keys, and Device Binding Keys in:

*   **Android Keystore** backed by **TEE (Trusted Execution Environment)** or **StrongBox**.

Example:

    EncryptedSharedPreferences + AES/GCM key stored in Keystore

This prevents:

✅ extraction  
✅ tampering  
✅ copying to another device  
✅ replay attacks

**Refresh token MUST NEVER be stored in plain text.**

***

## ✅ 1.2 Access Token → Do NOT Persist

Access Token should be:

*   **Short-lived (5–10 min)**
*   **Held only in memory (not persisted)**
*   Reconstructed via refresh-token flow

If app is killed → user re-logged via Refresh Token.

This protects against:

✅ memory scraping  
✅ file system attacks  
✅ user-installed malware

***

## ✅ 1.3 Bind Token to Device (Device Binding)

Issue a **device-bound refresh token** using:

*   Hardware attestation
*   KeyPair from Android Keystore (RSA/ECDSA)
*   Signed challenge from server

Server only accepts tokens signed by that device's key.

This prevents:

✅ Token replay on another device  
✅ Token export  
✅ Emulators

***

## ✅ 1.4 Root/Jailbreak Detection

If root found:

*   Disable token usage
*   Invalidate session
*   Force logout

Rooted devices can bypass sandboxing → easier token theft.

***

## ✅ 1.5 App Tamper Detection

Use:

*   Signature validation
*   Dex verification
*   Anti-debugging
*   Obfuscation
*   Memory integrity checks

If tampering → force logout & token invalidation.

***

# ✅ **2. Network-Level Token Protection**

## ✅ 2.1 TLS 1.2+ Mandatory

ALL communication MUST use HTTPS with TLS 1.2/1.3.

***

## ✅ 2.2 Certificate Pinning

Pin the backend public key/certificate fingerprint.

Prevents:

✅ MITM attacks  
✅ Bank-grade security breach  
✅ Proxy-based token sniffing

***

## ✅ 2.3 Mutual TLS (mTLS)

For high-security BFSI apps:

App → presents client-certificate → Server validates

This prevents:

✅ unauthorized app instances  
✅ tampered apps  
✅ credential stuffing

***

## ✅ 2.4 Do NOT Send Tokens in Query Params

Always use:

✅ Authorization: Bearer <token> (header)  
✅ Never log tokens  
✅ Never expose tokens in deep links

***

# ✅ **3. Backend Token Security**

## ✅ 3.1 Use OAuth 2.0 + OpenID Connect

Token types:

*   **Access Token (Short)**
*   **Refresh Token (Long)**
*   **ID Token (Short)**

***

## ✅ 3.2 Rotate Tokens Frequently

*   Access token: 5–10 min
*   Refresh token: 14–30 days
*   Auto-rotate refresh tokens for each new issuance

Prevents long-lived token theft.

***

## ✅ 3.3 Refresh Token Invalidation

If:

*   Device change detected
*   Suspicious login
*   Root detection
*   Risk scoring high
*   Biometric mismatch

→ Invalidate refresh token immediately.

***

## ✅ 3.4 Token Blacklisting

Store revoked tokens in:

*   Redis cache
*   Memory-limited blacklist with TTL

Prevents reuse of compromised tokens.

***

## ✅ 3.5 IP, Device, Geo Binding

Each token is tied to:

*   Device ID
*   OS version
*   Screen lock enabled
*   Geo-location region
*   Customer ID

If mismatch → block.

***

# ✅ **4. Token Lifecycle Strategy**

### ✅ Access Token

*   In-memory only
*   Short-lived
*   Used for API calls
*   Auto-refresh in background

### ✅ Refresh Token

*   Stored in encrypted Keystore
*   Long-lived but revocable
*   Bound to device
*   Requires biometric/OTP for re-activation

### ✅ Session Token (if used)

*   JWT or reference token
*   Signed using HMAC or asymmetric keys
*   No PII inside token

***

# ✅ **5. Threat Modeling & Mitigations**

| Threat              | Mitigation                             |
| ------------------- | -------------------------------------- |
| MITM Attack         | TLS + Pinning + mTLS                   |
| Token Replay        | Device binding + signed tokens         |
| Token Theft         | Keystore + short token expiry          |
| Malware Access      | Root detection + biometric gating      |
| Reverse Engineering | Obfuscation + tamper detection         |
| Brute Force         | Rate limiting + JWT signatures         |
| Token Injection     | Strict authorization server validation |

***

# ✅ **6. Additional Safeguards (Enterprise BFSI)**

✅ **Biometric gating** before sensitive operations  
✅ **Transaction tokens** for each payment  
✅ **Challenge-response signature** per transaction  
✅ **JWT with nonce** to prevent replay  
✅ **Encrypted logs (no sensitive token in logs)**  
✅ **Server-side risk scoring (device + behavior)**

***

# ✅ **7. Summary (Interview‑Ready 20‑Second Answer)**

> \*\*I protect tokens using a multi-layered approach:
>
> *   Store refresh tokens only in Android Keystore (TEE),
> *   Keep access tokens in memory (never persisted),
> *   Bind tokens to device using hardware-backed keys,
> *   Enforce TLS + certificate pinning + optional mTLS,
> *   Use OAuth2 with short-lived access tokens and rotating refresh tokens,
> *   Invalidate tokens on suspicious behavior, root detection, or device change.
>
> This ensures tokens cannot be extracted, replayed, or misused even if the device or network is compromised.\*\*

***

</details>

<details>
  <summary><strong>3. Explain session timeout strategy.</strong></summary>

***

This covers:

✅ What “session timeout” really is  
✅ Hard vs. soft timeouts  
✅ Ideal timeout durations for BFSI  
✅ How to implement them securely  
✅ Foreground/background rules  
✅ Token lifecycle alignment  
✅ Threat modeling + mitigations  
✅ UX considerations

Let’s break it down.

***

# ✅ **What Is a Session Timeout?**

A **session timeout** is a security mechanism that **automatically logs out a user** after a period of inactivity or app backgrounding, to prevent:

*   Unauthorized access
*   Stolen device misuse
*   Token replay
*   Shoulder‑surfing
*   Fraud attempts

A session timeout must be **strict, secure, predictable**, and **aligned with token expiration**.

***

# ✅ **1. Types of Session Timeouts**

## ✅ **A. Inactivity Timeout (Soft Timeout)**

Triggered when the user stops interacting (touch, scroll, typing) for *X minutes*.

Example:

*   5 minutes for financial transactions
*   10–15 minutes for informational screens
*   RBI/PCI guidelines recommend <15 minutes for BFSI apps

***

## ✅ **B. Background Timeout (Hard Timeout)**

Triggered when the app goes to the background.

Example:

*   Immediate lock for sensitive screens
*   30–60 seconds for general app
*   Full logout after **N minutes** in background

This protects against multitasking abuse.

***

## ✅ **C. Absolute Session Timeout**

Regardless of activity.

Example:

*   Auto logout every 12–24 hours
*   Forces re-authentication → protects long-lived tokens

Ensures security during prolonged continuous use.

***

# ✅ **2. Key Principles of a Secure Session Timeout Strategy**

### ✅ **1. Tokens must expire faster than the UI session**

*   Access Token lifespan: **5–10 minutes**
*   Refresh Token lifespan: **14–30 days but device-bound**

### ✅ **2. State must persist securely**

*   Use **SavedStateHandle** for temporary UI state
*   Encrypted storage for session metadata
*   Avoid leaving sensitive data on screen after timeout

### ✅ **3. On timeout → MUST clear sensitive memory**

*   Invalidate access token
*   Lock UI (Biometrics/MPIN)
*   Clear ephemeral state (payment forms, OTP screens)
*   Keep non-sensitive last-view state

***

# ✅ **3. How to Implement Session Timeout (Mobile App Level)**

### ✅ **Step 1: Maintain a Last Interaction Timestamp**

Every interaction updates a timestamp:

```kotlin
sessionManager.updateLastActiveTimestamp(System.currentTimeMillis())
```

Interactions include:

*   touch
*   scroll
*   text input
*   navigation events
*   API calls

***

### ✅ **Step 2: Foreground/Background Tracking**

Use **ProcessLifecycleOwner**:

```kotlin
class AppLifecycleObserver : DefaultLifecycleObserver {
    override fun onStop(owner: LifecycleOwner) {
        sessionManager.markBackground()
    }
    override fun onStart(owner: LifecycleOwner) {
        sessionManager.checkTimeout()
    }
}
```

***

### ✅ **Step 3: Timer for inactivity**

Use a coroutine ticker or handler.

```kotlin
viewModelScope.launch {
    while (true) {
        delay(1000)
        if (sessionManager.isTimedOut()) {
            logoutFlow()
        }
    }
}
```

***

### ✅ **Step 4: Session Manager (Core Component)**

```kotlin
class SessionManager @Inject constructor(
    private val secureStore: SecureStore
) {
    private val TIMEOUT_MS = 5 * 60 * 1000  // 5 minutes

    fun updateLastActiveTimestamp(t: Long)
    fun isTimedOut(): Boolean
    fun markBackground()
    fun clearSession() // logout
}
```

***

### ✅ **Step 5: Actions on Timeout**

Upon timeout:

1.  Invalidate access token

2.  Keep refresh token (if allowed by policy)

3.  Mask sensitive UI

4.  Navigate user to:
    *   login screen OR
    *   biometric unlock screen (fast re-entry)

5.  Clear in-memory states
    *   payment flows
    *   OTP states
    *   draft data

***

# ✅ **4. Backend Enforcement (Critical in BFSI)**

Server must enforce:

✅ short access token lifespan  
✅ refresh token revocation on inactivity  
✅ inactivity session tracking  
✅ IP, device, geo detection  
✅ block long-idle sessions

Server should not rely solely on the app.

***

# ✅ **5. Security Enhancements for BFSI**

### ✅ **1. Sensitive Screen Auto-Lock**

Screens like:

*   Account details
*   Transaction summary
*   Loan offers
*   Credit card statements

→ lock instantly when app backgrounds.

### ✅ **2. Biometric Re-entry**

Before re-entering:

Use:

```kotlin
BiometricPrompt
```

Fast, secure, frictionless.

### ✅ **3. Zero Data Exposure on Recent Apps Screen**

Use:

```kotlin
WindowManager.LayoutParams.FLAG_SECURE
```

Prevents screenshot + recents preview.

***

# ✅ **6. Multi-Module Architecture Impact**

Place session logic in:

    core-auth
    core-session

All features depend on it.  
State flows notify features if session expired.

    sessionManager.sessionState.collect {
        if (it == EXPIRED) navigateToLockScreen()
    }

✅ Consistent behavior across 50+ modules  
✅ Zero duplication  
✅ Easy maintenance

***

# ✅ **7. UX Considerations (Critical for Acceptance)**

✅ Warn user before logout  
✅ Show countdown timer  
✅ Allow extending session via biometric  
✅ Automatically save drafts (securely)

***

# ✅ **8. Compliance Requirements**

### ✅ RBI + PCI-DSS suggests:

*   Automatic logout after 5 minutes of inactivity for sensitive operations
*   No sensitive data in memory after logout
*   Multi-factor re-authentication for critical transactions
*   Token invalidation on inactivity

### ✅ GDPR:

*   Minimal data retention
*   User consent for session tracking

***

# ✅ **Final Interview‑Ready Summary (30 Seconds)**

> **I implement session timeout using an inactivity timer + background timer + absolute timeout.  
> I store only refresh tokens in hardware-backed Keystore, keep access tokens in memory, and clear them on timeout.  
> The session manager is a shared module used by all features, integrated with lifecycle and navigation to enforce locking.  
> Sensitive screens auto-lock when the app backgrounds.  
> Timeout policy aligns with token expiry and regulatory guidelines (RBI/PCI).  
> On timeout we invalidate tokens, clear sensitive in-memory state, and require biometric or full re-login.**

***

</details>

<details>
  <summary><strong>4. How do you handle users switching devices?</strong></summary>
B
***

# ✅ **How Do You Handle Users Switching Devices?**

(Enterprise Banking Architecture Approach)

When a user switches devices, the bank must ensure:

✅ Strong identity verification  
✅ Secure de-registration of old device  
✅ Device binding & attestation  
✅ Token invalidation  
✅ Protection against device cloning & SIM swap attacks  
✅ Smooth onboarding on the new device  
✅ Compliance with RBI/PCI guidelines

Below is a complete approach spanning:  
✅ Mobile App  
✅ Authentication backend  
✅ Device management service  
✅ Token lifecycle  
✅ Risk scoring

***

# ✅ 1. **Device Binding = First Line of Defense**

Every mobile banking user must be **bound to exactly one device** (or a limited set).

During login:

### ✅ Steps to bind a device:

1.  App generates **hardware-backed key pair** using Android Keystore (TEE).
2.  App sends **public key + device info** to backend.
3.  Backend verifies:
    *   Device fingerprint
    *   OS version
    *   App signature hash
    *   IMEI/Device ID (if allowed by regulatory guidelines)
    *   Integrity (SafetyNet/Play Integrity API)
4.  Backend generates a **Device Binding Token** tied to:
    *   Customer ID
    *   Device public key
    *   Risk score
    *   Timestamp

**Device binding key is stored in hardware-backed Keystore** so it can't be copied.

This prevents cloned/fake device attacks.

***

# ✅ 2. **Switching Device = Force Re-verification (MFA)**

When logging in from a NEW device:

The server compares:

    deviceBindingKey(newDevice) != registeredDeviceKey

If mismatch → device switch detected.

### ✅ Mandatory re-verification

Require at least 2 factors:

✅ OTP +  
✅ Biometric OR MPIN OR Password +  
✅ Sometimes Video-KYC (depending on feature)

### ✅ Optional additional checks:

*   Fraud score
*   Geo-velocity (IP change)
*   SIM swap detection (via telco APIs)
*   PAN/Aadhaar verification (if high-risk flow)

***

# ✅ 3. **Automatically Invalidate Tokens from Old Device**

When a new device is registered:

    invalidate refresh tokens of old device
    invalidate session keys
    revoke OAuth tokens of previous device

Stored in:

*   Redis token blacklist
*   Session database
*   Risk management engine

This prevents an attacker from continuing a session on an old/stolen/cloned device.

***

# ✅ 4. **Secure Device Deregistration Flow**

The app exposes:

✅ “Manage Devices” screen  
✅ “Logout from other devices” option  
✅ “Deregister old device” functionality

Backend actions on deregistration:

*   Invalidate all tokens linked to old device
*   Remove device binding
*   Log event for audit
*   Optional email/SMS alert

This helps if the user sells or loses a device.

***

# ✅ 5. **Play Integrity API / SafetyNet Attestation**

Before allowing device switch:

✅ Validate device integrity  
✅ Validate app signature authenticity  
✅ Identify emulator/rooted device  
✅ Check device tamper flags

If fail → block or require stricter KYC before allowing device switch.

***

# ✅ 6. **Transaction Risk-Based Controls After Device Switch**

Even after new-device login is successful:

✅ Apply stricter controls for next 24–48 hours:

*   Lower transaction limits
*   Mandatory biometrics
*   Additional OTP challenge
*   Disable high-risk flows (adding new beneficiaries)
*   Extra validation for UPI PIN reset
*   Cooldown on adding new devices

This reduces fraud from SIM-swap/device-theft cases.

***

# ✅ 7. **Token Protection During Device Switching**

### ✅ Access Token

*   Short-lived (5–10 minutes)
*   Never persisted
*   Automatically invalidated on switch

### ✅ Refresh Token

*   Hardware-backed
*   Device-bound
*   Revoked on switch

### ✅ Session Token

*   Reset completely
*   Navigation state cleared

### ✅ Cryptographic binding

All tokens must be tied to:

*   Device fingerprint
*   App version
*   Public key attestation

Even if attacker steals token → cannot replay on another device.

***

# ✅ 8. **Backend Architecture for Device Switch**

    Mobile App
        → Auth Service
            → Device Binding Service
                → Risk Engine
                    → Notification Service
                        → Core Banking

### ✅ Device Binding Service stores:

*   deviceId
*   publicKey
*   risk score
*   last activity time
*   token signatures
*   device capabilities
*   rooted/emulator status

This becomes the **SSOT for device registration.**

***

# ✅ 9. **UX Strategy (Equally Important)**

A frictionless but secure experience:

✅ Show “New Device Detected” screen  
✅ Confirm via OTP  
✅ Confirm with biometric  
✅ Optional “Approve login from old device” push notification  
✅ Notify user via SMS/email

This reassures customers and prevents unauthorized access.

***

# ✅ 10. **Threat Mitigations (BFSI-Critical)**

| Threat              | Prevention                                       |
| ------------------- | ------------------------------------------------ |
| Device cloning      | Hardware-backed keystore + attestation           |
| SIM swap            | Telco integration + risk scoring                 |
| MITM                | Certificate pinning + TLS 1.3                    |
| Token theft         | Device-bound tokens                              |
| Rooted device       | Block or require re-verification                 |
| Credential stuffing | Device fingerprint + risk score                  |
| Stolen phone        | Immediate token invalidation on new-device login |

***

# ✅ **Final Interview‑Ready Answer (30 seconds)**

> **Users switching devices is a high-risk event in BFSI.  
> I handle it by binding tokens to a hardware-backed key pair stored in the Android Keystore. When a login happens on a new device, the backend detects a mismatch and forces multi-factor re-verification—OTP + biometrics/MPIN.  
> Old device tokens are immediately invalidated. We use SafetyNet/Play Integrity for device attestation and apply risk controls (lower limits, additional OTP) for the next 24–48 hours.  
> Device binding, token invalidation, and multi-factor re-auth ensure that even if a token, SIM, or password is compromised, the attacker cannot activate another device.**

***

</details>

<details>
  <summary><strong>5. How do you design an offline ready transaction history feature?</strong></summary>

***

This is a critical feature because banking users expect:

✅ immediate access  
✅ reliability in low‑network areas  
✅ correctness (no duplicates, no wrong balances)  
✅ secure local caching  
✅ audit‑safe behavior

I’ll break down the design into:

1.  **High‑level goals**
2.  **Architecture**
3.  **Data modeling**
4.  **Offline-first synchronization**
5.  **Conflict resolution**
6.  **Security model**
7.  **Caching strategy**
8.  **Pagination strategy**
9.  **UI behavior**
10. **Error scenarios**
11. **Testing strategy**

***

# ✅ 1. High‑Level Product Goals

An offline‑ready Transaction History feature must:

✅ Work **without network**  
✅ Show **reliable, up-to-date** transactions  
✅ Support **pull-to-refresh**, pagination, filtering  
✅ Avoid showing outdated/incorrect balances  
✅ Keep data encrypted at rest  
✅ Handle **sync conflicts**, **duplicates**, **partial sync**  
✅ Gracefully degrade UI during offline state  
✅ Be compliant with BFSI security

***

# ✅ 2. Architecture Overview

```
 ┌─────────────────────────────┐
 │         UI Layer (Compose)  │
 │  - Offline-first UI         │
 │  - Pull to refresh          │
 └───────────────┬─────────────┘
                 │
 ┌─────────────────────────────┐
 │     ViewModel (MVVM)        │
 │ - Exposes StateFlow<State>  │
 │ - Orchestrates Sync         │
 │ - Offline→Online state mgmt │
 └───────────────┬─────────────┘
                 │
 ┌─────────────────────────────┐
 │      UseCase Layer          │
 │ getTransactions(page)       │
 │ syncTransactions()          │
 │ clearCacheOnLogout()        │
 └───────────────┬─────────────┘
                 │
 ┌───────────────────────────────┐
 │      Repository                │
 │ - Offline DB (Room/SQLCipher) │
 │ - Remote API (Retrofit)       │
 │ - Sync Engine                 │
 └───────────────┬───────────────┘
                 │
 ┌────────────────────────────────┐
 │  Local DB (Encrypted Room)     │
 │  TransactionDao                │
 └────────────────────────────────┘

```

***

# ✅ 3. Data Modeling (Critical for Performance)

### ✅ Room Entity

```kotlin
@Entity
data class TransactionEntity(
    @PrimaryKey val txnId: String,
    val amount: Double,
    val timestamp: Long,
    val description: String,
    val merchant: String?,
    val type: String,     // debit/credit
    val accountId: String,
    val syncedAt: Long,   // server sync time
    val updatedAt: Long   // last local update
)
```

### ✅ Additional tables:

*   **SyncMetadata**
    *   lastFetchedPage
    *   lastSyncTime
*   **PendingActions** (optional for editable systems like UPI mandates)

***

# ✅ 4. Offline‑First Sync Strategy (Core Logic)

### ✅ A. Use a **"Local First + Remote Sync"** model

UI always reads from **local DB** → fast, offline-friendly.

### ✅ B. Sync Flow

1.  On app open / screen open → load from DB
2.  Check network
3.  If online → fetch updates from server
4.  Apply delta updates
5.  Save changes to encrypted DB
6.  Update UI state

***

### ✅ C. Remote Sync Uses “Delta Fetch Strategy”

The server API supports:

    GET /transactions?since=lastSyncedAt&limit=50

Server returns only **new or updated** transactions.

Advantages:

✅ Fast  
✅ Bandwidth efficient  
✅ Perfect for BFSI  
✅ Works for pagination

***

### ✅ D. Pagination Strategy

Store pagination state locally:

```kotlin
lastFetchedPagePerAccount[accountId]
```

Steps:

1.  Load page 1 from DB
2.  If not available locally, fetch remotely
3.  Store fetched pages into DB
4.  Mark page as “cached”

***

# ✅ 5. Conflict Resolution Strategy

Conflicts usually occur when:

*   Server updates a transaction (status change)
*   User sees outdated local version

### ✅ Strategy Used: SERVER ALWAYS WINS

Because BFSI systems must display **truth from the core system**, not cached data.

When a delta update includes a transaction:

    if (txn.updatedAt > local.updatedAt) override local

✅ Ensures correctness  
✅ Avoids partial/invalid displays

***

# ✅ 6. Security (BFSI-Compliant)

### ✅ A. Encrypt all data

Use:

*   **SQLCipher**
*   Or **Room + EncryptedFile**
*   AES-256 symmetric key stored in **Android Keystore** (TEE)

### ✅ B. Prevent screenshots for sensitive displays

### ✅ C. Do not store:

*   Account numbers (store masked only)
*   PAN/Aadhaar
*   Card numbers (never store)

### ✅ D. Use **certificate pinning** for API calls

***

# ✅ 7. Caching Strategy

### ✅ Cold Cache (Fresh App Install)

*   Screen launches → local DB empty
*   Show skeleton loader
*   Fetch page 1
*   Save to DB
*   UI updates immediately

***

### ✅ Warm Cache (App reopened)

*   Show local data instantly
*   Start background sync
*   UI updates with “Synced X seconds ago”

***

### ✅ Stale Cache Detection

If:

    currentTime - lastSyncTime > 24 hours

→ Force refresh (blocking)

***

# ✅ 8. UI Behavior (User Experience)

### ✅ Show Cached Data First

Even in airplane mode.

### ✅ Show Offline Banner

“Showing offline data — connect to sync latest.”

### ✅ Show Last Sync Time

*   “Synced 2 hours ago”
*   “Data may be outdated”

### ✅ Pull-to-Refresh

Triggers a fresh sync.

### ✅ Automatic Retry

Exponential backoff for sync failure.

***

# ✅ 9. Edge Cases & Error Handling

### ✅ No internet

*   Display cached transactions
*   Disable pagination beyond cached pages
*   Show retry option

### ✅ Partial offline cache

*   Some pages cached, others not
*   Show partially cached list

### ✅ Wrong device time

*   Use server timestamp
*   Show warning if difference > 5 minutes

### ✅ Transaction corrections

Rare BFSI scenario → resolved via server-wins strategy.

***

# ✅ 10. Performance Optimizations

✅ Use **LIMIT + OFFSET** queries for pagination  
✅ Use **Flow** or **Paging3** with Room for efficient UI updates  
✅ Compress locally stored JSON if required  
✅ Pre-index by `timestamp` and `accountId`  
✅ Background sync using **WorkManager**

***

# ✅ 11. Testing Strategy (Critical in BFSI)

### ✅ Unit Tests

*   conflict resolution
*   sync logic
*   pagination logic
*   entity mapping

### ✅ Integration Tests

*   Room DB + Repository
*   network failures
*   offline → online restore

### ✅ UI Tests (Compose)

*   offline states
*   stale cache banner
*   pagination

### ✅ Security Tests

*   database encryption
*   sensitive data leak detection
*   invalidation on logout

***

# ✅ **Interview‑Ready Summary (30 seconds)**

> **To design an offline‑ready transaction history feature, I use an offline-first architecture with encrypted local storage (Room + SQLCipher) as the SSOT.  
> UI always loads from local DB instantly, then background sync fetches delta updates from server and merges using a “server-wins” conflict strategy.  
> Pagination, metadata, and last sync time are stored locally.  
> All data is encrypted with Keystore-managed keys.  
> Offline mode shows cached data with warning banners.  
> Sync is handled via UseCases + Repository with WorkManager retries.  
> This ensures fast, secure, reliable access to transactions even without network, compliant with BFSI requirements.**

***

</details>

<details>
  <summary><strong>6. What parts must run exclusively on device vs on backend?</strong></summary>

***

# ✅ **What parts must run *exclusively on‑device* vs *exclusively on backend*?**

(This is a *very* important security/architecture question in finance interviews — interviewers want to see that you understand **trust boundaries**, **risk scoring**, **compliance**, **attack surfaces**, and **UX trade‑offs**.)

***

# ✅ **Why This Question Matters**

In banking, the mobile app and backend are **not equally trusted**:

✅ The **device is a hostile, user‑controlled environment**  
✅ The **backend is the source of truth**

Therefore, choosing what logic runs *locally* vs *remotely* is a crucial design decision.

***

# ✅ ✅ **PART 1 — What MUST run exclusively ON DEVICE**

These operations must run *locally*, because they depend on hardware, UX, security boundaries, or should never be sent to the backend.

***

## ✅ 1. **Biometric Authentication (Fingerprint / Face ID)**

Why on device?

*   Biometric data must **never leave the device hardware**
*   Android/iOS enforce this using *TEE / Secure Enclave*
*   BFSI regulations prohibit transmitting biometric templates
*   Backend only receives a “yes/no” authentication result

***

## ✅ 2. **PIN/MPIN Verification (Optional, depending on architecture)**

If you use **client-side MPIN**:

*   Store MPIN hash in hardware-backed Keystore
*   Verify locally
*   Faster UX
*   Prevents MITM attacks

Large banks often choose **hybrid**:

*   Local MPIN check (for app unlock)
*   Server MPIN check (for transactions)

***

## ✅ 3. **Token Storage & Cryptographic Operations**

On-device:

*   KeyPair generation
*   Token signing
*   Challenge–response cryptography
*   Device binding
*   Secure random number generation
*   Hardware attestation

These use:

*   Android Keystore (TEE / StrongBox)
*   Secure Enclave (iOS)

Tokens, keys, secrets **must never leave the device**.

***

## ✅ 4. **UI State Management & Navigation Decisions**

This includes:

*   Validation of form UI state
*   Navigation routes
*   Local-accessibility logic
*   Local filtering/formatting

UI must not depend on backend for transitions.

***

## ✅ 5. **Offline Cache & Local DB**

All data that is safe to store offline:

*   Transaction history cache (masked)
*   Account names & balances (masking required)
*   User profile partial data
*   KYC progress
*   Session metadata (“last synced time”)

Anything stored **must be encrypted** (SQLCipher/EncryptedSharedPrefs).

***

## ✅ 6. **Device Integrity Checks**

These must run locally:

*   Root detection
*   Emulator detection
*   Tamper detection
*   Debugger detection
*   Hooking detection (Frida/Xposed)
*   Play Integrity API / SafetyNet calls

Backend receives the **attestation result**, not raw data.

***

# ✅ ✅ **PART 2 — What MUST run exclusively ON BACKEND**

These operations must stay in the backend because they require **trusted environment**, **core data**, **audit**, or **secure computation**.

***

## ✅ 1. **Authentication & Authorization**

On server:

*   OAuth2
*   Access token generation
*   Refresh token validation
*   Session management
*   Blacklisting
*   Device binding verification
*   MFA (OTP/SMS/email, backend-driven)

Reason:  
Backend is the **only trusted environment** to issue tokens.

***

## ✅ 2. **Money Movement / Payments / Ledger Updates**

Critical operations must run server-side:

*   IMPS / NEFT / RTGS
*   UPI transaction initiation
*   UPI PIN validation (done via NPCI infra)
*   Balance deduction
*   Reversal
*   Limits evaluation
*   Beneficiary addition/activation
*   Loan EMI calculations
*   Interest computation

Reason:  
This is where fraud risk and financial correctness requirements are highest.

***

## ✅ 3. **User Identity Verification**

Backend handles:

*   OTP generation & validation
*   Aadhaar/PAN verification
*   KYC processing
*   Document OCR & fraud checks
*   Liveliness detection ML (server-side)

Reason:  
Document verification requires heavy ML models + compliance workflow.

***

## ✅ 4. **Risk & Fraud Scoring**

Must execute server-side:

*   Geo-velocity checks
*   SIM swap detection
*   Device fingerprint comparison
*   Transaction anomaly detection
*   Login risk score
*   Behavioral biometrics (typing patterns, velocity, etc.)

Device inputs → backend risk engine → allow/deny.

***

## ✅ 5. **Security Policy Enforcement**

Backend enforces:

*   Rate limiting
*   Velocity restrictions
*   Password/MPIN lockout
*   Session expiration
*   Transaction limits
*   Time-based restrictions
*   Compliance rules (AML, FATCA, RBI security policies)

***

## ✅ 6. **Core Banking Operations**

Everything involving CBS (Core Banking System):

*   Balances
*   Statements
*   Loan accounts
*   FD/RD maturity
*   Charges calculation
*   Ledger posting

***

## ✅ 7. **Audit Logs & Forensic Trails**

Backend maintains:

*   Auth logs
*   Transaction logs
*   Error logs
*   Behavioral logs
*   Device metadata logs

These must not be tampered and require backend storage.

***

# ✅ ✅ **PART 3 — What should NEVER run on-device**

These represent **strong anti-patterns**:

❌ Money transfer logic  
❌ Interest/balance calculation  
❌ Security rule enforcement  
❌ Signature/OTP verification  
❌ Token validation  
❌ Risk checks  
❌ API sequencing logic  
❌ PII in logs or local storage  
❌ Storing debit/credit card data  
❌ Storing sensitive financial data unencrypted

These expose the system to fraud, reverse‑engineering, and compliance violations.

***

# ✅ ✅ **Combined View (Final Architecture Boundary)**

                    ┌────────────────────────────────┐
                    │        On Device (Untrusted)   │
                    │--------------------------------│
                    │ UI, ViewModels, caching         │
                    │ Local validation (UI-level)     │
                    │ MPIN/Biometrics (secure element)│
                    │ Token storage (Keystore)        │
                    │ Root/Integrity checks           │
                    │ Device binding key              │
                    │ Offline-safe data only          │
                    └───────────┬────────────────────┘
                                │ TLS + Pinning + mTLS
                    ┌───────────▼────────────────────┐
                    │     Backend (Trusted Zone)      │
                    │--------------------------------│
                    │ Auth, tokens, refresh           │
                    │ Risk engine, fraud detection    │
                    │ Ledger, payments, banking ops   │
                    │ KYC, PAN/Aadhaar verification   │
                    │ Business logic & policy         │
                    │ Audit logs                      │
                    │ Rate limiting, throttling       │
                    └─────────────────────────────────┘

***

# ✅ **Final Interview-Ready Answer (20 seconds)**

> \*\*On-device logic must include UI, biometrics, MPIN validation, token storage inside hardware-backed Keystore, integrity checks, offline caching, and device binding keys — basically anything tied to UX or device hardware.
>
> Backend must perform authentication, authorization, token management, transactions, ledger updates, KYC verification, fraud/risk scoring, rate limiting, compliance rules, and audit logs — basically anything requiring trust, correctness, or regulatory compliance.
>
> Device = untrusted → limited to UX + secure hardware.  
> Backend = source of truth → all critical logic lives there.\*\*

***

</details>

---

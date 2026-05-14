# UBS Interview Questions

<details>
  <summary>1. How are data classes different from normal classes?</summary>

***

# ✅ 1. What is a Data Class in Kotlin?

A **data class** in Kotlin is a class specifically designed to **hold data** (state) and automatically provides common utility methods.

```kotlin
data class Customer(
    val id: Int,
    val name: String
)
```

***

# ✅ 2. Key Differences: Data Class vs Normal Class

| Feature                        | Data Class                           | Normal Class    |
| ------------------------------ | ------------------------------------ | --------------- |
| Purpose                        | Stores data (DTO / Model)            | General-purpose |
| `toString()`                   | Auto-generated                       | Manual          |
| `equals()` / `hashCode()`      | Auto-generated (based on properties) | Manual          |
| `copy()`                       | Available                            | Not available   |
| `componentN()` (Destructuring) | Available                            | Not available   |
| Boilerplate                    | Minimal                              | High            |
| Immutability encouragement     | Strong (with `val`)                  | Not enforced    |

***

# ✅ 3. Auto-Generated Functions (VERY IMPORTANT)

When you declare a data class, Kotlin generates:

### ✔ 1. `equals()` & `hashCode()`

Used heavily in:

*   RecyclerView diffing
*   Caching
*   Collections (Set, Map)

```kotlin
val c1 = Customer(1, "Harshal")
val c2 = Customer(1, "Harshal")

println(c1 == c2) // true ✅ (value-based)
```

👉 Normal class:

```kotlin
class Customer(val id: Int, val name: String)

val c1 = Customer(1, "Harshal")
val c2 = Customer(1, "Harshal")

println(c1 == c2) // false ❌ (reference comparison)
```

***

### ✔ 2. `toString()`

```kotlin
println(Customer(1, "Harshal"))
```

Output:

    Customer(id=1, name=Harshal)

👉 In a normal class, you'll get:

    Customer@6d06d69c  // not useful

***

### ✔ 3. `copy()` (VERY IMPORTANT for immutability)

```kotlin
val original = Customer(1, "Harshal")
val updated = original.copy(name = "Rahul")
```

👉 Used heavily in:

*   State management (MVVM, MVI)
*   Immutable UI state updates

***

### ✔ 4. Destructuring (`componentN()`)

```kotlin
val (id, name) = Customer(1, "Harshal")
println(id)   // 1
println(name) // Harshal
```

***

# ✅ 4. Rules for Data Class (Interview MUST-KNOW)

1.  Must have **at least one parameter** in constructor
2.  All primary constructor parameters must be:
    *   `val` or `var`
3.  Cannot be:
    *   `abstract`
    *   `open`
    *   `sealed`
    *   `inner`

***

# ✅ 5. Real-World Banking App Usage

In financial services apps (UBS, HSBC, etc.), data classes are used for:

### ✔ API Models

```kotlin
data class AccountResponse(
    val accountNumber: String,
    val balance: Double,
    val currency: String
)
```

### ✔ UI State (MVVM / MVI)

```kotlin
data class AccountUiState(
    val isLoading: Boolean,
    val balance: String,
    val error: String?
)
```

### ✔ Local DB Entities (Room)

```kotlin
@Entity
data class TransactionEntity(
    @PrimaryKey val id: String,
    val amount: Double
)
```

***

# ✅ 6. Architect-Level Insight (VERY IMPORTANT)

### ✅ Why Data Classes Matter in Large Banking Apps

1.  **Immutability → Prevents bugs**
2.  **Value equality → critical for UI diffing (RecyclerView, Compose)**
3.  **Cleaner state management → reduces side effects**
4.  **Thread safety → safer in multi-threading (important in fintech apps)**

***

# ✅ 7. When NOT to Use Data Classes

Avoid data classes when:

*   Business logic-heavy classes
*   Classes with behavior more than state
*   Inheritance hierarchy required

```kotlin
class PaymentProcessor {
    fun processPayment() { ... }
}
```

***

# ✅ 8. Common Interview Trick Question ⚠️

### Q: Can a data class have mutable properties?

✅ Yes, but **NOT recommended**

```kotlin
data class User(var name: String)
```

👉 Problem:

*   Breaks immutability
*   Harder to debug state changes

***

# ✅ 9. Quick Summary (Interview Revision 🚀)

👉 Data class = **lightweight model class with auto-generated utility methods**

### ✔ Key Benefits:

*   Auto `equals/hashCode`
*   Auto `toString`
*   `copy()` for immutability
*   Destructuring support

### ✔ Used in:

*   API models
*   UI state
*   DB entities

### ✔ Key Interview Point:

> "Data classes provide value-based equality and immutability, which is essential in modern Android architectures like MVVM and MVI, especially for predictable UI state management in large-scale financial apps."

***

</details>

<details>
  <summary>2. How hashcode and equals work in data classes to compare objects or values?</summary>

***

# ✅ 1. What are `equals()` and `hashCode()`?

### ✔ `equals()`

Determines **logical equality (value equality)** between two objects.

```kotlin
a == b  // calls equals()
```

***

### ✔ `hashCode()`

Returns an **integer hash** used for:

*   HashMap
*   HashSet
*   Caching
*   Efficient lookups

👉 Rule:

> If two objects are equal (`equals()` returns true), their `hashCode()` MUST be the same.

***

# ✅ 2. How Data Classes Implement Them

Kotlin automatically generates both methods using **all properties in the primary constructor**.

***

## ✅ Example

```kotlin
data class Account(
    val id: Int,
    val balance: Double
)
```

### 👉 Generated `equals()` (simplified)

```kotlin
override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Account) return false

    return id == other.id && balance == other.balance
}
```

***

### 👉 Generated `hashCode()` (simplified)

```kotlin
override fun hashCode(): Int {
    var result = id
    result = 31 * result + balance.hashCode()
    return result
}
```

***

# ✅ 3. How Comparison Works in Practice

```kotlin
val acc1 = Account(1, 1000.0)
val acc2 = Account(1, 1000.0)
```

### ✅ Step 1: `equals()`

```kotlin
println(acc1 == acc2) // true ✅
```

👉 Because:

*   id == id
*   balance == balance

***

### ✅ Step 2: `hashCode()`

```kotlin
println(acc1.hashCode() == acc2.hashCode()) // true ✅
```

👉 Ensures correct behavior in hash-based collections.

***

# ✅ 4. Why Both Are Needed Together

### ⚠️ Interview Key Point:

> `equals()` is used for correctness, `hashCode()` is used for performance.

***

## ✅ Example: HashSet

```kotlin
val set = HashSet<Account>()
set.add(Account(1, 1000.0))

println(set.contains(Account(1, 1000.0))) // true ✅
```

👉 Internally:

1.  Uses `hashCode()` → find bucket
2.  Uses `equals()` → confirm match

***

# ✅ 5. Normal Class vs Data Class Behavior

## ❌ Normal Class

```kotlin
class Account(val id: Int, val balance: Double)

val acc1 = Account(1, 1000.0)
val acc2 = Account(1, 1000.0)

println(acc1 == acc2) // false ❌
```

👉 Because:

*   Default `equals()` → compares **memory references**

***

## ✅ Data Class

```kotlin
data class Account(val id: Int, val balance: Double)

println(acc1 == acc2) // true ✅
```

👉 Because:

*   Compares **property values**

***

# ✅ 6. Important Edge Case (VERY IMPORTANT)

### ⚠️ Only primary constructor properties are used!

```kotlin
data class User(val id: Int) {
    var name: String = ""
}
```

```kotlin
val u1 = User(1)
u1.name = "Harshal"

val u2 = User(1)
u2.name = "Rahul"

println(u1 == u2) // true ✅ (DANGEROUS!)
```

👉 Because:

*   `name` is NOT part of constructor
*   Ignored in `equals()` and `hashCode()`

***

# ✅ 7. Architect-Level Insights (UBS Expectation)

### ✔ 1. RecyclerView / DiffUtil

```kotlin
override fun areItemsTheSame(oldItem: Account, newItem: Account) =
    oldItem.id == newItem.id

override fun areContentsTheSame(oldItem: Account, newItem: Account) =
    oldItem == newItem  // uses equals()
```

👉 Efficient UI updates depend on correct equality.

***

### ✔ 2. Jetpack Compose Recomposition

*   Compose uses equality checks to decide recomposition
*   Wrong `equals()` → unnecessary UI redraws

***

### ✔ 3. Caching / Deduplication

```kotlin
val map = HashMap<Account, String>()
```

👉 Requires consistent `hashCode()` + `equals()`

***

### ✔ 4. Thread Safety / Immutability

*   Data classes + `val` → stable equality
*   Mutable fields → inconsistent hash → bugs

***

# ✅ 8. Common Pitfalls (Interview Gold ⚠️)

### ❌ Mutable properties inside data class

```kotlin
data class User(var name: String)
```

👉 If modified after use in HashMap → lookup breaks

***

### ❌ Custom equals without hashCode

👉 Violates contract → unpredictable behavior

***

# ✅ 9. Quick Summary (Interview Revision 🚀)

### ✔ Core Concept:

*   Data class `equals()` → compares **values**
*   Data class `hashCode()` → derived from same values

***

### ✔ Key Rule:

> If `a == b`, then `a.hashCode() == b.hashCode()`

***

### ✔ Flow in Collections:

1.  `hashCode()` → locate bucket
2.  `equals()` → verify object

***

### ✔ Key Advantages:

*   Value-based comparison ✅
*   Correct behavior in collections ✅
*   Essential for UI frameworks ✅

***

### ✔ Critical Gotcha:

> Only **primary constructor properties** are considered in equality.

***

# ✅ Perfect Interview Answer (Short Version)

> "In Kotlin data classes, `equals()` and `hashCode()` are automatically generated based on all properties in the primary constructor. `equals()` compares objects by value, while `hashCode()` ensures efficient lookup in hash-based collections. Both follow the contract where equal objects must have the same hash code. This is crucial in Android for RecyclerView diffing, caching, and state management."

***

</details>

<details>
  <summary>3. Can we place properties within and outside the constructor? What properties are considered?</summary>

***

# ✅ 1. Can we declare properties inside and outside constructor?

👉 **Yes, absolutely.**

Kotlin allows properties:

*   ✅ Inside the **primary constructor**
*   ✅ Outside (inside class body)

***

## ✅ Example

```kotlin
data class User(
    val id: Int,
    val name: String
) {
    var age: Int = 0          // outside constructor
    val country: String = "IN" // outside constructor
}
```

***

# ✅ 2. What properties are considered in `equals()` and `hashCode()`?

👉 **ONLY properties declared in the primary constructor are considered.**

***

## ✅ Example Behavior

```kotlin
val u1 = User(1, "Harshal")
u1.age = 30

val u2 = User(1, "Harshal")
u2.age = 25

println(u1 == u2) // ✅ true
```

***

### ❗ Why is it TRUE?

Because:

*   `id` → same ✅
*   `name` → same ✅
*   `age` → ❌ ignored (not in constructor)
*   `country` → ❌ ignored

***

# ✅ 3. Interview Explanation (What interviewer expects)

> "In Kotlin data classes, only the properties defined in the primary constructor are used for generating `equals()`, `hashCode()`, `copy()`, and `componentN()` functions. Any properties declared inside the class body are ignored for these operations."

***

# ✅ 4. What about `copy()`?

Even `copy()` only considers constructor properties.

```kotlin
val u1 = User(1, "Harshal")
u1.age = 30

val u2 = u1.copy(name = "Rahul")

println(u2.age) // ❌ 0 (default value, not copied!)
```

👉 Why?

*   `age` is NOT part of constructor → NOT copied

***

# ✅ 5. Destructuring (`componentN`) Behavior

```kotlin
val (id, name) = User(1, "Harshal")
```

👉 Only constructor properties are destructured.

***

# ✅ 6. Architect-Level Insight ⚠️ (VERY IMPORTANT)

### 🚨 This can cause serious bugs in banking apps

#### Example: Wrong UI comparison

```kotlin
data class Account(val id: Int) {
    var balance: Double = 0.0
}
```

```kotlin
val old = Account(1).apply { balance = 1000.0 }
val new = Account(1).apply { balance = 2000.0 }

println(old == new) // ✅ true (WRONG logically!)
```

👉 Impact:

*   RecyclerView → UI won't update
*   Compose → recomposition fails
*   Financial data mismatch ❌

***

# ✅ 7. Best Practice (Architect-Level Answer)

👉 **Always put all important state inside constructor**

```kotlin
data class Account(
    val id: Int,
    val balance: Double
)
```

***

# ✅ 8. When to keep properties outside?

✅ Valid use cases:

*   Derived/calculated values
*   Temporary UI flags
*   Non-critical metadata

```kotlin
data class User(val id: Int) {
    val isPremium: Boolean
        get() = id % 2 == 0
}
```

***

# ✅ 9. Quick Summary (Interview Revision 🚀)

### ✔ Can we place properties?

*   ✅ Inside constructor
*   ✅ Outside constructor

***

### ✔ What is considered?

👉 ONLY **constructor properties** are used for:

*   `equals()`
*   `hashCode()`
*   `copy()`
*   `componentN()`

***

### ✔ Ignored:

*   Properties in class body ❌

***

### ✔ Key Risk:

> Ignoring important fields leads to incorrect comparisons and bugs.

***

### ✔ Best Practice:

✅ Keep all business-critical fields in constructor  
✅ Prefer immutability (`val`)

***

# ✅ Perfect Interview Answer (Short Version)

> "Yes, Kotlin allows properties inside and outside the constructor, but in data classes only the primary constructor properties are considered for `equals()`, `hashCode()`, and other generated methods. Properties declared inside the class body are ignored, which can lead to logical bugs if important state is kept outside the constructor."

***

✅ This is a **high-impact answer** — interviewers usually go deeper after this.

***

</details>

<details>
  <summary>4. What happens if a data class contains a mutable property like MutableList? How does it affect copy() and immutability ?</summary>

***

# ✅ 1. Problem Statement

👉 What happens if a data class has a mutable property like `MutableList`?

```kotlin
data class Account(
    val id: Int,
    val transactions: MutableList<String>
)
```

***

# ✅ 2. Key Concept: Data Class is NOT Truly Immutable

Even if you use `val`, the object can still be mutable **internally**.

```kotlin
val acc = Account(1, mutableListOf("T1"))
acc.transactions.add("T2") // ✅ Allowed!
```

👉 Why?

*   `val` → reference is immutable
*   BUT object (`MutableList`) → still mutable

***

# ✅ 3. How `copy()` behaves (VERY IMPORTANT ⚠️)

### 👉 copy() performs a **shallow copy**, NOT deep copy

***

## ✅ Example

```kotlin
val acc1 = Account(1, mutableListOf("T1"))

val acc2 = acc1.copy()

acc2.transactions.add("T2")

println(acc1.transactions) // ❗ [T1, T2]
println(acc2.transactions) // [T1, T2]
```

***

### ❗ Why this happens?

*   `copy()` copies references
*   Both objects point to the **same MutableList**

👉 Internally:

    acc1.transactions ──┐
                        ├──> [T1, T2]
    acc2.transactions ──┘

***

# ✅ 4. Impact on Immutability

### ❌ Breaks immutability guarantee

```kotlin
val original = acc1
val copy = acc1.copy()

copy.transactions.add("Fraud Transaction")
```

👉 Now:

*   **original is also modified ❌**
*   Dangerous in banking apps

***

# ✅ 5. Real-World Risk in Financial Apps 🚨

### ❗ Scenario: Transaction corruption

```kotlin
data class Account(
    val id: Int,
    val transactions: MutableList<Transaction>
)
```

BUG:

*   One module updates list
*   Another screen reflects wrong data
*   Audit logs mismatch
*   Financial inconsistency ❌

***

# ✅ 6. Equality Issue (Hidden Problem)

Equality still works based on reference:

```kotlin
val acc1 = Account(1, mutableListOf("T1"))
val acc2 = acc1.copy()

println(acc1 == acc2) // true ✅
```

But after mutation:

```kotlin
acc2.transactions.add("T2")

println(acc1 == acc2) // still true ❌ (unexpected logically)
```

👉 Because:

*   Both refer to SAME list

***

# ✅ 7. Architect-Level Best Practices ✅

***

## ✅ 1. Prefer Immutable Collections

```kotlin
data class Account(
    val id: Int,
    val transactions: List<String>
)
```

👉 Now:

```kotlin
val acc1 = Account(1, listOf("T1"))

val acc2 = acc1.copy(
    transactions = acc1.transactions + "T2"
)
```

✅ Safe  
✅ No shared state

***

## ✅ 2. If mutation needed → create new list

```kotlin
val updated = acc1.copy(
    transactions = acc1.transactions.toMutableList().apply {
        add("T2")
    }
)
```

***

## ✅ 3. Deep Copy Manually (if required)

```kotlin
fun Account.deepCopy(): Account {
    return Account(
        id,
        transactions.toMutableList()
    )
}
```

***

# ✅ 8. Jetpack Compose / MVVM Impact

### ❗ Mutable list breaks state observation

```kotlin
val state = Account(1, mutableListOf("T1"))
state.transactions.add("T2")
```

👉 Compose:

*   May NOT detect change
*   No recomposition ❌

***

## ✅ Correct approach:

```kotlin
val state = Account(1, listOf("T1"))

val newState = state.copy(
    transactions = state.transactions + "T2"
)
```

✅ Recomposition triggered

***

# ✅ 9. Interview-Level Deep Insight 💡

> "Data classes provide shallow immutability, not deep immutability."

***

# ✅ 10. Common Interview Trap Answer ⚠️

### ❓ Does `copy()` create a deep copy?

❌ No → it's **shallow copy**

👉 Only copies:

*   primitive values ✅
*   references ❌ (shared)

***

# ✅ 11. Quick Summary (Interview Revision 🚀)

### ✔ Key Problem:

*   Mutable objects (`MutableList`) break immutability

***

### ✔ copy() behavior:

*   ✅ Shallow copy
*   ❌ Shared references

***

### ✔ Risks:

*   Shared state bugs
*   Incorrect UI updates
*   Data inconsistency (critical in banking apps)

***

### ✔ Best Practices:

*   ✅ Use `List` instead of `MutableList`
*   ✅ Use `copy()` with new instances
*   ✅ Avoid internal mutation
*   ✅ Prefer immutability

***

# ✅ Perfect Interview Answer (Short Version)

> "If a data class contains a mutable property like `MutableList`, it breaks immutability because the list can be modified even if the reference is `val`. Also, the `copy()` function performs a shallow copy, so the original and copied object share the same list reference, leading to unintended side effects. In production apps, especially in financial systems, we avoid this by using immutable collections and creating new instances when updating state."

***

✅ This is a **very high-value answer** — interviewers often dive deeper into:

*   Deep vs shallow copy
*   Immutability strategies
*   Compose state handling

***

</details>

<details>
  <summary>5. Explain the code, what does it do and print?
```kotlin
data class AnotherData(var somevalue: Int)
fun main() {
    val dataset2 = HashSet()
    val obj2 = AnotherData(10)
    dataset2.add(obj2)
    obj2.somevalue = 100
    println("${obj2 in dataset2}")
}
```
</summary>

***

# ✅ 1. Code Understanding

```kotlin
data class AnotherData(var somevalue: Int)

fun main() {
    val dataset2 = HashSet<AnotherData>()
    val obj2 = AnotherData(10)

    dataset2.add(obj2)        // Step 1
    obj2.somevalue = 100      // Step 2 (mutation)
    println("${obj2 in dataset2}") // Step 3
}
```

***

# ✅ 2. What Happens Step-by-Step

***

## 🔹 Step 1: Add object to HashSet

```kotlin
val obj2 = AnotherData(10)
dataset2.add(obj2)
```

👉 HashSet uses:

1.  `hashCode()` → to determine bucket
2.  `equals()` → for collision check

### At this point:

*   `somevalue = 10`
*   `hashCode()` is calculated based on `10`

👉 Object is stored in a bucket based on `hashCode(10)`

***

## 🔹 Step 2: Mutating the object

```kotlin
obj2.somevalue = 100
```

👉 Now:

*   `somevalue = 100`
*   `hashCode()` changes! ⚠️

***

## 🔹 Step 3: Checking membership

```kotlin
println(obj2 in dataset2)
```

Internally:

```kotlin
dataset2.contains(obj2)
```

### HashSet does:

1.  Compute **new hashCode (based on 100)**
2.  Look in a **different bucket**
3.  Does NOT find the object ❌

***

# ✅ 3. Final Output

    false

***

# ✅ 4. Why This Happens (CORE INTERVIEW POINT)

👉 Because:

*   `hashCode()` depends on `somevalue`
*   You changed `somevalue` AFTER insertion
*   So object is now in the **wrong bucket**

***

### Visual Representation

    Step 1:
    Bucket[hash(10)] → obj2

    Step 2:
    obj2.somevalue = 100

    Step 3:
    Search → Bucket[hash(100)] ❌ (empty)

👉 Object exists, but is **"lost" inside HashSet**

***

# ✅ 5. Key Concept Violated ⚠️

> Objects used in hash-based collections must be **immutable with respect to fields used in hashCode() and equals()**

***

# ✅ 6. Why Data Class Makes It Worse

```kotlin
data class AnotherData(var somevalue: Int)
```

👉 `somevalue` is:

*   Part of `equals()` ✅
*   Part of `hashCode()` ✅
*   AND mutable ❌

***

# ✅ 7. Real-World Banking Impact 🚨

### Example:

```kotlin
HashSet<Transaction>
```

If transaction fields change:

*   Duplicate detection fails ❌
*   Data inconsistency ❌
*   Audit issues ❌

👉 This is **critical in financial systems**

***

# ✅ 8. Correct Approach ✅

***

## ✔ Option 1: Make property immutable

```kotlin
data class AnotherData(val somevalue: Int)
```

***

## ✔ Option 2: Avoid mutation after insertion

```kotlin
val obj2 = AnotherData(10)
dataset2.add(obj2)

// DON'T modify after adding
```

***

## ✔ Option 3: Remove → Modify → Reinsert

```kotlin
dataset2.remove(obj2)
obj2.somevalue = 100
dataset2.add(obj2)
```

***

## ✔ Option 4: Use immutable copy

```kotlin
val updated = obj2.copy(somevalue = 100)
dataset2.add(updated)
```

***

# ✅ 9. Interview-Level Insight 💡

> "Hash-based collections rely on stable hash codes. Mutating a field used in hashCode after insertion leads to undefined behavior, as the object may no longer be retrievable."

***

# ✅ 10. Quick Summary (Interview Revision 🚀)

### ✔ Output:

    false

***

### ✔ Root Cause:

*   Mutable property used in `hashCode()`
*   Value changed → hash changes
*   Object stored in wrong bucket

***

### ✔ Key Rule:

✅ Never mutate fields used in `hashCode()` while object is in HashSet/HashMap

***

### ✔ Best Practice:

*   Use `val` (immutability)
*   Use data classes safely
*   Prefer immutable state

***

# ✅ Perfect Interview Answer (Short Version)

> "The code prints false because after inserting the object into a HashSet, we mutate a property that is used in the data class's hashCode. This changes the hashCode, so when the set tries to find the object, it looks in a different bucket and fails. This highlights why mutable properties should not be used in hash-based collections."

***

✅ This is a **highly rated answer** in interviews — if you explain it like this, you’ll stand out.

***

</details>

<details>
  <summary>6. What is StateFlow and Shared Flow?</summary>

***

# ✅ 1. What is Flow (Context)

Before StateFlow & SharedFlow:

👉 Both are part of **Kotlin Coroutines Flow API**
👉 Used for **asynchronous data streams**

***

# ✅ 2. What is StateFlow?

### ✅ Definition

> **StateFlow is a state holder observable flow that always has a current value and emits updates to collectors.**

***

## ✅ Key Characteristics

*   Always has an **initial value**
*   Holds **latest state only**
*   Emits **only latest + new updates**
*   **Hot flow** (doesn’t stop when no collectors)
*   Works like **LiveData replacement**

***

## ✅ Example

```kotlin
class ViewModel {
    private val _state = MutableStateFlow(0)
    val state: StateFlow<Int> = _state

    fun increment() {
        _state.value += 1
    }
}
```

### Observe:

```kotlin
lifecycleScope.launch {
    viewModel.state.collect {
        println(it)
    }
}
```

***

## ✅ Behavior

    Initial value → Always available ✅
    New collector → Gets latest value immediately ✅

***

# ✅ 3. What is SharedFlow?

### ✅ Definition

> **SharedFlow is a general-purpose hot flow used for broadcasting events to multiple collectors.**

***

## ✅ Key Characteristics

*   No default value ❌
*   Can emit **multiple values**
*   Can replay previous emissions (configurable)
*   Used for **events (not state)**

***

## ✅ Example

```kotlin
class ViewModel {
    private val _events = MutableSharedFlow<String>()
    val events = _events

    suspend fun sendEvent() {
        _events.emit("Transaction Successful")
    }
}
```

***

### Observe:

```kotlin
lifecycleScope.launch {
    viewModel.events.collect {
        println(it)
    }
}
```

***

# ✅ 4. Core Difference (VERY IMPORTANT)

| Feature                | StateFlow         | SharedFlow          |
| ---------------------- | ----------------- | ------------------- |
| Purpose                | State (UI state)  | Events              |
| Initial value          | ✅ Required        | ❌ Not required      |
| Replay behavior        | Always 1 (latest) | Configurable        |
| Emits to new collector | Latest value      | Depends on replay   |
| Use case               | UI state          | One-time events     |
| Equivalent to          | LiveData          | EventBus / Channels |

***

# ✅ 5. Real Android Use Case

***

## ✅ StateFlow → UI State

```kotlin
data class UiState(
    val isLoading: Boolean = false,
    val data: List<String> = emptyList()
)

private val _uiState = MutableStateFlow(UiState())
val uiState: StateFlow<UiState> = _uiState
```

👉 Used for:

*   Screen data
*   Loading state
*   Form values

***

## ✅ SharedFlow → Events

```kotlin
private val _showToast = MutableSharedFlow<String>()
val showToast = _showToast
```

👉 Used for:

*   Navigation
*   Toast messages
*   One-time actions

***

# ✅ 6. Why Not Use StateFlow for Events? ⚠️

Problem:

```kotlin
val state = MutableStateFlow("SUCCESS")
```

👉 New collector:

*   Will receive "SUCCESS" again ❌

➡️ Causes:

*   Duplicate navigation
*   Repeated toast

***

✔ Solution: Use SharedFlow

***

# ✅ 7. Replay Insight (SharedFlow Advanced)

```kotlin
val flow = MutableSharedFlow<Int>(replay = 2)
```

👉 Stores last 2 emissions

***

# ✅ 8. Banking App Architectural Insight 💡

### ✅ StateFlow:

*   Account balance
*   Transaction list
*   UI states

### ✅ SharedFlow:

*   Payment success message
*   Error popups
*   Navigation triggers

***

# ✅ 9. Common Interview Pitfalls ⚠️

### ❌ Using SharedFlow for UI state

→ No guaranteed latest value

### ❌ Using StateFlow for events

→ Re-triggering issues

***

# ✅ 10. Quick Summary (Interview Revision 🚀)

### ✔ StateFlow:

*   Holds **state**
*   Always has value
*   Emits latest value
*   Used for UI state

***

### ✔ SharedFlow:

*   Used for **events**
*   No default value
*   Supports replay
*   Used for one-time actions

***

### ✔ Key Line (Interview Golden Answer):

> "StateFlow is used to represent state and always holds the latest value, whereas SharedFlow is used to represent events and doesn't retain state unless configured with replay."

***

# ✅ Perfect Interview Answer (Short Version)

> "StateFlow and SharedFlow are both hot flows in Kotlin coroutines. StateFlow is used for state management and always holds a current value, making it ideal for UI state in MVVM or MVI. SharedFlow is more flexible and is used for event-based communication like navigation or toasts, where we don't want to retain the last value unless explicitly configured."

***

✅ This question often leads to:

*   LiveData vs StateFlow
*   Flow vs Channel
*   Compose integration

***

</details>

<details>
  <summary>7. What is the difference between LiveData and Stateflow? How can you make Stateflow lifecycle aware?</summary>

***

# ✅ 1. LiveData vs StateFlow (Core Difference)

| Feature          | LiveData               | StateFlow                          |
| ---------------- | ---------------------- | ---------------------------------- |
| Origin           | Android (Jetpack)      | Kotlin Coroutines                  |
| Lifecycle aware  | ✅ Yes (built-in)       | ❌ No (manual handling needed)      |
| Initial value    | ❌ Optional             | ✅ Required                         |
| Null handling    | ✅ Supports null        | ✅ Supports null                    |
| Threading        | Main thread by default | Works with coroutines (any thread) |
| Cold/Hot         | Hot                    | Hot                                |
| Dependency       | Android-only           | Kotlin (platform independent)      |
| Use with Compose | Not ideal              | ✅ Preferred                        |

***

# ✅ 2. Key Difference Explained

***

## ✅ LiveData

👉 Automatically lifecycle-aware

```kotlin
liveData.observe(this) {
    // updates only when lifecycle is active
}
```

✔ Stops observing when:

*   Activity/Fragment is stopped/destroyed

***

## ✅ StateFlow

👉 NOT lifecycle-aware by default

```kotlin
lifecycleScope.launch {
    viewModel.state.collect {
        // keeps collecting even if UI is not visible ❌
    }
}
```

⚠️ This can:

*   Waste resources
*   Cause crashes/memory leaks

***

# ✅ 3. How to Make StateFlow Lifecycle-Aware ✅ (VERY IMPORTANT)

***

## ✅ Approach 1: repeatOnLifecycle (Recommended ✅)

```kotlin
lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.state.collect {
            // Safe collection ✅
        }
    }
}
```

### ✔ What happens:

*   Starts collecting at `STARTED`
*   Stops collecting at `STOPPED`
*   Restarts automatically ✅

👉 This is the **modern best practice**

***

## ✅ Approach 2: flowWithLifecycle (Alternative)

```kotlin
lifecycleScope.launch {
    viewModel.state
        .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
        .collect {
            // lifecycle aware ✅
        }
}
```

***

## ✅ Approach 3: Jetpack Compose (Automatic ✅)

```kotlin
val state by viewModel.state.collectAsState()
```

👉 Compose handles lifecycle automatically ✅

***

# ✅ 4. Real Example (Production Level)

***

## ✅ ViewModel

```kotlin
class MyViewModel : ViewModel() {
    private val _state = MutableStateFlow(0)
    val state: StateFlow<Int> = _state
}
```

***

## ✅ Fragment (Best Practice)

```kotlin
viewLifecycleOwner.lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.state.collect {
            println(it)
        }
    }
}
```

***

# ✅ 5. Architect-Level Insight 💡

### ✅ Why StateFlow is preferred over LiveData now?

*   Works with coroutines ✅
*   Better for **clean architecture**
*   Supports operators (map, filter, combine)
*   Platform-independent (KMM ready)

***

### ✅ Why LiveData still exists?

*   Legacy codebases
*   Simpler lifecycle handling
*   No coroutine knowledge required

***

# ✅ 6. Common Interview Pitfalls ⚠️

### ❌ Collecting StateFlow directly

```kotlin
lifecycleScope.launch {
    viewModel.state.collect { } // ❌ NOT lifecycle-safe
}
```

***

### ❌ Using GlobalScope

👉 Memory leaks risk

***

# ✅ 7. Quick Summary (Interview Revision 🚀)

### ✔ LiveData:

*   Lifecycle-aware ✅
*   Android-specific
*   No coroutine dependency

***

### ✔ StateFlow:

*   Not lifecycle-aware ❌
*   Requires manual lifecycle handling
*   Coroutine-based ✅

***

### ✔ Lifecycle Solution:

✅ `repeatOnLifecycle()` → best practice  
✅ `flowWithLifecycle()` → alternative  
✅ Compose → automatic

***

# ✅ Perfect Interview Answer (Short Version)

> "LiveData is lifecycle-aware and automatically stops observing when the UI is not active, while StateFlow is not lifecycle-aware by default and continues emitting unless handled properly. To make StateFlow lifecycle-aware, we use APIs like `repeatOnLifecycle` or `flowWithLifecycle`, which ensure collection only happens in active lifecycle states. In modern Android development, StateFlow is preferred due to its coroutine support and flexibility."

***

✅ This is a **high-scoring answer** — especially if you mention:

*   `repeatOnLifecycle`
*   Compose integration
*   Coroutine advantage

***

</details>

<details>
  <summary>8. What happens if you update MutableStateFlow with the same object reference?</summary>

***

# ✅ 1. Question Understanding

👉 What happens if we assign the **same object reference** to a `MutableStateFlow`?

***

## ✅ Example

```kotlin
data class UiState(val count: Int)

val stateFlow = MutableStateFlow(UiState(0))

val obj = UiState(1)

stateFlow.value = obj       // First update ✅
stateFlow.value = obj       // Same reference again
```

***

# ✅ 2. Key Behavior (VERY IMPORTANT)

👉 **StateFlow does NOT emit if the new value is equal to the current value**

Internally, it checks:

```kotlin
if (newValue == currentValue) {
    // No emission ❌
}
```

***

# ✅ 3. What Happens with SAME Object Reference?

### ✅ Case 1: Same reference, same values

```kotlin
val obj = UiState(1)

stateFlow.value = obj
stateFlow.value = obj  // same reference
```

👉 Result:

*   `equals()` → true ✅
*   No emission ❌

***

### ✅ Case 2: Same reference, but mutated (very dangerous)

```kotlin
data class UiState(var count: Int)

val obj = UiState(1)

stateFlow.value = obj

obj.count = 2

stateFlow.value = obj
```

👉 What happens?

*   `equals()` compares current value with new value
*   BUT both references point to SAME object

👉 So:

```kotlin
obj == obj  // always true ✅
```

➡️ StateFlow thinks **no change happened** ❌  
➡️ No emission ❌  
➡️ UI NOT updated ❌

***

# ✅ 4. Why This Happens (Core Concept)

👉 StateFlow uses **`equals()` comparison (structural equality)**

*   If same reference → equals returns true
*   If mutated object → still same reference → still true

***

# ✅ 5. Real-World Impact (Critical in Banking Apps 🚨)

### ❗ UI Not Updating

```kotlin
val state = MutableStateFlow(Account(1000))

// mutation
state.value.balance = 2000
state.value = state.value
```

👉 Result:

*   No emission ❌
*   UI shows old balance ❌
*   Financial inconsistency ❌

***

# ✅ 6. Architect-Level Rule ✅

> **Never mutate objects inside StateFlow**

***

# ✅ 7. Correct Approach ✅

***

## ✅ Use Immutable State

```kotlin
data class UiState(val count: Int)

val stateFlow = MutableStateFlow(UiState(1))

stateFlow.value = stateFlow.value.copy(count = 2)
```

✅ New object created  
✅ equals() → false  
✅ emission happens ✅

***

## ✅ Always emit NEW instance

```kotlin
val current = stateFlow.value
val updated = current.copy(count = current.count + 1)

stateFlow.value = updated
```

***

# ✅ 8. Compose / MVVM Impact

### ❌ Wrong

```kotlin
state.value.list.add("item")
state.value = state.value
```

👉 No recomposition ❌

***

### ✅ Correct

```kotlin
state.value = state.value.copy(
    list = state.value.list + "item"
)
```

✅ UI updates ✅

***

# ✅ 9. Special Note: Referential vs Structural Equality

| Type  | Meaning          |
| ----- | ---------------- |
| `===` | Same reference   |
| `==`  | Calls `equals()` |

👉 StateFlow uses:

    == (equals)

***

# ✅ 10. Quick Summary (Interview Revision 🚀)

### ✔ What happens?

*   Same object reference → `equals()` returns true
*   StateFlow does NOT emit

***

### ✔ Dangerous case:

*   Mutating same object → no emission → stale UI

***

### ✔ Root Cause:

*   StateFlow avoids duplicate emissions using equality check

***

### ✔ Best Practice:

✅ Always use immutable data  
✅ Always emit new object (`copy()`)  
✅ Avoid mutable fields

***

# ✅ Perfect Interview Answer (Short Version)

> "StateFlow does not emit a new value if the updated value is equal to the current one based on `equals()`. If you assign the same object reference, it won't emit again. Even more dangerously, if you mutate the same object and reassign it, StateFlow still won't emit because the reference is the same and equality check passes. This can lead to UI not updating. Therefore, we should always use immutable objects and emit new instances using `copy()`."

***

✅ This is a **very high-value answer** — if you explain mutation + equality + UI impact, you’ll stand out at architect level.

***

</details>

<details>
  <summary>9. Is LiveData main thread-bound? if yes why?</summary>

***

# ✅ 1. Short Answer (Interview Ready)

> "Yes, LiveData is main thread-bound because it is designed to be lifecycle-aware and safely update UI components, which must run on the main thread. It ensures thread safety by restricting value updates and observer notifications to the main thread."

***

# ✅ 2. What Does “Main Thread-Bound” Mean?

👉 It means:

*   **Observers are always notified on the main thread**
*   UI updates happen safely
*   You cannot directly update LiveData from background thread using `.value`

***

# ✅ 3. How LiveData Enforces This

***

## ✅ 1. `setValue()` (Main Thread Only)

```kotlin
liveData.value = "Hello"
```

👉 Can ONLY be called on **main thread**

### ❌ If called from background thread:

```kotlin
Thread {
    liveData.value = "Hello" // Crash ❌
}
```

➡️ Throws:

    IllegalStateException

***

## ✅ 2. `postValue()` (Background Safe)

```kotlin
Thread {
    liveData.postValue("Hello") // ✅ OK
}
```

👉 Internally:

*   Schedules update on main thread
*   Uses a handler (message queue)

***

# ✅ 4. Why LiveData is Main Thread-Bound (Core Reason)

***

## ✅ 1. UI Safety

👉 Android rule:

> "UI must be updated on main thread only"

LiveData is designed to:

*   Directly connect with UI (Activity/Fragment)
*   Avoid threading issues

***

## ✅ 2. Lifecycle Awareness

```kotlin
liveData.observe(this) { }
```

👉 Lifecycle events occur on **main thread**

So LiveData ensures:

*   Consistent lifecycle + data updates
*   No race conditions

***

## ✅ 3. Simplicity for Developers

*   No need to manage thread switching
*   Safe by default

***

# ✅ 5. Internal Working (Simplified)

LiveData uses:

*   `Handler` + `Looper.getMainLooper()`

```kotlin
Handler(Looper.getMainLooper()).post {
    // notify observers
}
```

***

# ✅ 6. Comparison with StateFlow

| Feature                | LiveData           | StateFlow             |
| ---------------------- | ------------------ | --------------------- |
| Thread restriction     | Main thread-bound  | Not restricted        |
| Update from background | `postValue()`      | Direct update allowed |
| Observer thread        | Always main thread | Collector decides     |

***

## ✅ StateFlow Example

```kotlin
viewModelScope.launch(Dispatchers.IO) {
    stateFlow.value = "Updated" // ✅ allowed
}
```

👉 More flexible than LiveData

***

# ✅ 7. Real Interview Insight 💡

### ✅ Why LiveData design is limiting today?

*   Hard to use in background threads
*   Less flexible than Flow
*   Not suitable for complex async pipelines

👉 That’s why modern apps prefer:

*   **StateFlow / SharedFlow**

***

# ✅ 8. Common Pitfalls ⚠️

### ❌ Calling `setValue()` from background thread

✅ Use `postValue()` instead

***

### ❌ Assuming LiveData works like Flow

👉 It does NOT:

*   No operators (map, zip, combine)
*   Limited transformation capabilities

***

# ✅ 9. Quick Summary (Interview Revision 🚀)

### ✔ Yes, LiveData is main thread-bound

***

### ✔ Why?

*   UI updates must happen on main thread
*   Lifecycle events occur on main thread
*   Ensures thread safety

***

### ✔ Methods:

*   `setValue()` → Main thread only
*   `postValue()` → Background safe

***

### ✔ Limitation:

*   Less flexible vs StateFlow

***

# ✅ Perfect Interview Answer (Best Version)

> "Yes, LiveData is main thread-bound because it is designed to be lifecycle-aware and interact directly with UI components, which must be updated on the main thread in Android. It enforces this by allowing `setValue()` only on the main thread, while `postValue()` can be used from background threads as it internally switches to the main thread. This design ensures thread safety but also makes LiveData less flexible compared to StateFlow in modern architectures."

***

</details>

<details>
  <summary>10. What is state Hoisting along with its rules?</summary>

***

# ✅ 1. What is State Hoisting?

### ✅ Definition (Interview Ready)

> **State hoisting is the pattern of moving state ownership from a child composable to its parent, making the composable stateless and reusable.**

***

# ✅ 2. Why State Hoisting?

👉 In Compose:

*   UI should be **stateless**
*   State should be **managed at a higher level**

***

## ✅ Without State Hoisting (❌ Bad)

```kotlin
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }

    Button(onClick = { count++ }) {
        Text("Count: $count")
    }
}
```

👉 Problem:

*   State is **inside**
*   Not reusable
*   Hard to test

***

## ✅ With State Hoisting (✅ Good)

```kotlin
@Composable
fun Counter(count: Int, onIncrement: () -> Unit) {
    Button(onClick = onIncrement) {
        Text("Count: $count")
    }
}
```

***

## ✅ Parent owns state

```kotlin
@Composable
fun Screen() {
    var count by remember { mutableStateOf(0) }

    Counter(
        count = count,
        onIncrement = { count++ }
    )
}
```

***

# ✅ 3. Key Idea

👉 Move:

*   **State → UP (parent)**
*   **Events → DOWN (child triggers events upward)**

***

# ✅ 4. Rules of State Hoisting (VERY IMPORTANT)

***

## ✅ Rule 1: Single Source of Truth

👉 State should exist in **one place only**

```kotlin
var count by remember { mutableStateOf(0) }
```

***

## ✅ Rule 2: State goes DOWN

👉 Pass state as parameter

```kotlin
Counter(count = count)
```

***

## ✅ Rule 3: Events go UP

👉 Child emits actions

```kotlin
onIncrement = { count++ }
```

***

## ✅ Rule 4: Make Composables Stateless

👉 Prefer:

```kotlin
@Composable
fun Counter(count: Int, onIncrement: () -> Unit)
```

***

## ✅ Rule 5: Hoist to the Lowest Common Parent

👉 State should be placed where:

*   Multiple composables need it
*   It logically belongs

***

## ✅ Rule 6: Use ViewModel for Screen-Level State

```kotlin
val uiState by viewModel.state.collectAsState()
```

👉 For:

*   Business logic
*   API data
*   Persistence

***

# ✅ 5. Real Banking App Example 💰

***

## ❌ Without Hoisting

```kotlin
@Composable
fun BalanceView() {
    var balance by remember { mutableStateOf(1000.0) }
}
```

***

## ✅ With Hoisting

```kotlin
@Composable
fun BalanceView(balance: Double, onRefresh: () -> Unit) {
    Text("Balance: $balance")
}
```

***

## ✅ ViewModel + Hoisting

```kotlin
@Composable
fun AccountScreen(viewModel: AccountViewModel) {
    val state by viewModel.state.collectAsState()

    BalanceView(
        balance = state.balance,
        onRefresh = viewModel::refreshBalance
    )
}
```

***

# ✅ 6. Stateful vs Stateless Composables

| Type      | Description             |
| --------- | ----------------------- |
| Stateful  | Holds its own state     |
| Stateless | Receives state + events |

👉 Best practice:
✅ Prefer **stateless composables**

***

# ✅ 7. Architect-Level Insights 💡

***

### ✅ Why hoisting is critical in large apps?

*   Predictable state flow ✅
*   Easier debugging ✅
*   Better testing ✅
*   Supports unidirectional data flow ✅ (UDF)

***

### ✅ Works perfectly with:

*   MVVM
*   MVI
*   Redux-like patterns

***

# ✅ 8. Common Interview Pitfalls ⚠️

***

### ❌ Keeping state inside child components

👉 Leads to:

*   State duplication
*   UI inconsistency

***

### ❌ Passing mutable objects

```kotlin
fun UI(state: MutableStateFlow<...>) // ❌ bad design
```

👉 Breaks encapsulation

***

# ✅ 9. Visual Flow (Important for Interview)

            Parent (State Owner)
                   ↓
            Child (UI)
                   ↑
             Events (clicks/actions)

👉 This is **Unidirectional Data Flow**

***

# ✅ 10. Quick Summary (Interview Revision 🚀)

### ✔ Definition:

State hoisting = moving state to parent

***

### ✔ Core Rules:

*   ✅ Single source of truth
*   ✅ State flows DOWN
*   ✅ Events flow UP
*   ✅ Hoist to common parent
*   ✅ Prefer stateless composables

***

### ✔ Benefits:

*   Reusability ✅
*   Testability ✅
*   Maintainability ✅

***

# ✅ Perfect Interview Answer (Short Version)

> "State hoisting is a pattern in Jetpack Compose where state is moved out of a composable and managed by a parent, making the composable stateless and reusable. The key rules are: maintain a single source of truth, pass state down as parameters, and send events up via callbacks. This ensures unidirectional data flow, which is essential for scalable and maintainable applications."

***

✅ This is a **high-weight question** — often followed by:

*   UDF (Unidirectional Data Flow)
*   MVI architecture
*   Compose recomposition behavior

***

</details>

<details>
  <summary>11. How does compose avoid recomposition using skippable functions?</summary>

***

# ✅ 1. What is Recomposition?

👉 Recomposition = re-executing a composable function when state changes

***

# ✅ Problem

Recomposition is **expensive if unnecessary**, so Compose tries to **skip it when nothing changed**.

***

# ✅ 2. What are Skippable Functions?

### ✅ Definition

> A composable is **skippable** if Compose can determine that its inputs haven’t changed, so it can skip recomposition for that function.

***

# ✅ 3. How Compose Decides to Skip Recomposition

Compose uses **three key checks**:

***

## ✅ 1. Parameter Equality Check

Compose compares **current vs previous parameters**

```kotlin
if (oldParam == newParam) {
    skip recomposition ✅
}
```

***

## ✅ 2. Stability of Types (VERY IMPORTANT ⚠️)

Compose categorizes types as:

| Type     | Meaning                 |
| -------- | ----------------------- |
| Stable   | Safe to compare         |
| Unstable | Cannot reliably compare |

***

### ✅ Stable Types

*   Primitive types (Int, Boolean, String)
*   Data classes (if immutable)
*   `@Stable` / `@Immutable`

***

### ❌ Unstable Types

*   Mutable objects
*   Collections like `MutableList`
*   Custom classes (default)

***

👉 If parameter is unstable → Compose **won't skip** safely

***

## ✅ 3. Referential + Structural Equality

Compose uses:

*   `==` → structural equality (calls `equals`)
*   `===` → reference equality (in some cases)

***

# ✅ 4. Example: Skipping Recomposition ✅

```kotlin
@Composable
fun Greeting(name: String) {
    println("Recomposed")
    Text("Hello $name")
}
```

```kotlin
Greeting(name = "Harshal")
Greeting(name = "Harshal")
```

👉 No recomposition ✅  
Because:

*   `name` didn't change

***

# ✅ 5. Example: Recomposition Happens ❌

```kotlin
Greeting(name = "Harshal")
Greeting(name = "Rahul")
```

👉 Recomposition occurs ✅

***

# ✅ 6. Example with Data Class

```kotlin
data class User(val name: String)

@Composable
fun UserView(user: User) {
    println("Recomposed")
}
```

```kotlin
val user = User("Harshal")
UserView(user)
UserView(user)
```

👉 Skipped ✅ (same reference + equal)

***

***

# ✅ 7. Dangerous Case (VERY IMPORTANT ⚠️)

***

## ❌ Mutable Object

```kotlin
data class User(var name: String)

val user = User("Harshal")

UserView(user)

user.name = "Rahul"

UserView(user)
```

👉 What happens?

*   Same reference ✅
*   equals() → true (same object)
*   Compose thinks **no change ❌**
*   UI NOT updated ❌

***

# ✅ 8. How Compose Marks Functions as Skippable

Compose compiler transforms code like:

```kotlin
@Composable
fun MyUI(name: String)
```

Into something like:

```kotlin
if (parametersChanged) {
    run composable
} else {
    skip ✅
}
```

***

# ✅ 9. Important Annotations

***

## ✅ `@Stable`

```kotlin
@Stable
class UiState { ... }
```

👉 Promises:

*   Changes will be observable

***

## ✅ `@Immutable`

```kotlin
@Immutable
data class UiState(val name: String)
```

👉 Fully immutable → safest for skipping

***

# ✅ 10. Architect-Level Insight 💡

### ✅ Why Skipping Matters?

*   Reduces unnecessary recomposition ✅
*   Improves performance ✅
*   Critical for large UI trees (banking dashboards, lists)

***

### ✅ Compose Optimization Strategy

*   Skip recomposition when possible
*   Recompose only affected parts
*   Works like **Diffing engine**

***

# ✅ 11. Real Example (Banking App)

***

## ✅ Good (Immutable State)

```kotlin
data class Account(val balance: Double)

AccountView(account = account.copy(balance = 2000.0))
```

✅ New object → recomposition ✅

***

## ❌ Bad (Mutable)

```kotlin
account.balance = 2000.0
AccountView(account)
```

❌ Same reference → skipped ❌  
❌ UI bug ❌

***

# ✅ 12. Key Rules for Skippable Functions

***

### ✔ Rule 1:

👉 Parameters must be **stable**

***

### ✔ Rule 2:

👉 No change in parameters → skip

***

### ✔ Rule 3:

👉 Prefer immutable data (`val`, data classes)

***

### ✔ Rule 4:

👉 Avoid mutable shared state

***

# ✅ 13. Quick Summary (Interview Revision 🚀)

### ✔ Skippable functions:

*   Compose skips recomposition if inputs unchanged

***

### ✔ How it works:

*   Checks parameter equality (`equals`)
*   Requires stable types

***

### ✔ Important:

*   Immutable data → enables skipping ✅
*   Mutable data → breaks skipping ❌

***

### ✔ Key Insight:

> Compose optimizes by recomposing only when inputs change

***

# ✅ Perfect Interview Answer (Short Version)

> "Compose avoids recomposition using skippable functions by checking whether the inputs to a composable have changed. If the parameters are stable and equal to the previous values, Compose skips recomposition for that function. This relies on parameter equality and type stability. Using immutable data ensures correct skipping, while mutable objects can lead to missed updates."

***

✅ This is a **high-impact answer** — if you mention:

*   stability
*   equality check
*   immutability

you’ll stand out.

***

</details>

<details>
  <summary>12. What is coroutine scope and lifecycle scope?</summary>

***

# ✅ 1. What is a Coroutine Scope?

### ✅ Definition (Interview Ready)

> A **CoroutineScope** defines the lifecycle of coroutines — it controls when coroutines start and when they should be cancelled.

***

## ✅ Key Idea

👉 Coroutine = lightweight thread  
👉 Scope = **boundary / container** for coroutines

***

## ✅ Example

```kotlin
val scope = CoroutineScope(Dispatchers.IO)

scope.launch {
    // background work
}
```

***

### ✅ What scope does:

*   Manages coroutines ✅
*   Cancels them when needed ✅
*   Prevents memory leaks ✅

***

# ✅ 2. Types of CoroutineScope

***

## ✅ 1. GlobalScope ❌ (Avoid)

```kotlin
GlobalScope.launch {
    // runs forever 😬
}
```

👉 Problems:

*   No lifecycle awareness
*   Memory leaks
*   Not cancellable properly

***

## ✅ 2. Custom Scope

```kotlin
val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
```

👉 Used in:

*   Repository layer
*   Background services

***

***

# ✅ 3. What is LifecycleScope?

### ✅ Definition (Interview Ready)

> **lifecycleScope is a lifecycle-aware CoroutineScope tied to an Android component (Activity/Fragment) and automatically cancels coroutines when the lifecycle is destroyed.**

***

## ✅ Example

```kotlin
lifecycleScope.launch {
    // runs until Activity/Fragment is destroyed
}
```

***

# ✅ 4. How LifecycleScope Works

***

## ✅ Activity Lifecycle

```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            delay(5000)
            println("Will not run if activity is destroyed")
        }
    }
}
```

✅ If Activity closes → coroutine cancelled automatically

***

# ✅ 5. ViewLifecycleOwner Scope (VERY IMPORTANT ⚠️)

For Fragment UI:

```kotlin
viewLifecycleOwner.lifecycleScope.launch {
    // tied to view lifecycle (not fragment)
}
```

👉 Prevents:

*   Crashes
*   Memory leaks

***

# ✅ 6. Difference: CoroutineScope vs LifecycleScope

| Feature         | CoroutineScope     | LifecycleScope |
| --------------- | ------------------ | -------------- |
| Lifecycle-aware | ❌ No               | ✅ Yes          |
| Auto-cancel     | ❌ No               | ✅ Yes          |
| Usage           | General coroutines | UI components  |
| Risk            | Memory leaks       | Safe           |

***

# ✅ 7. Real Problem Without LifecycleScope 🚨

```kotlin
CoroutineScope(Dispatchers.IO).launch {
    delay(5000)
    updateUI() // ❌ Activity might be destroyed → crash
}
```

***

## ✅ Fixed with lifecycleScope

```kotlin
lifecycleScope.launch {
    delay(5000)
    updateUI() // ✅ safe
}
```

***

# ✅ 8. Architect-Level Insight 💡

***

### ✅ Why lifecycleScope is critical?

*   Prevents memory leaks ✅
*   Avoids UI crashes ✅
*   Ensures structured concurrency ✅

***

### ✅ Works well with:

*   StateFlow / Flow ✅
*   ViewModel ✅
*   Clean architecture ✅

***

# ✅ 9. Special Scope: viewModelScope ✅

👉 Bonus (very important in interviews)

```kotlin
viewModelScope.launch {
    // survives configuration change ✅
}
```

***

## ✅ Why use it?

*   Tied to ViewModel lifecycle
*   Survives rotation
*   Cancels when ViewModel is cleared

***

# ✅ 10. Quick Summary (Interview Revision 🚀)

### ✔ CoroutineScope:

*   Defines lifecycle of coroutines
*   Needs manual cancellation

***

### ✔ LifecycleScope:

*   Lifecycle-aware
*   Auto-cancelled
*   Safe for UI

***

### ✔ Best Practices:

*   ✅ Use `lifecycleScope` for UI
*   ✅ Use `viewModelScope` for business logic
*   ❌ Avoid `GlobalScope`

***

# ✅ Perfect Interview Answer (Short Version)

> "A CoroutineScope defines the lifecycle of coroutines and controls when they are cancelled. LifecycleScope is a specialized coroutine scope provided by Android that is tied to a component’s lifecycle, such as an Activity or Fragment, and automatically cancels coroutines when the lifecycle is destroyed. This prevents memory leaks and crashes, making it ideal for UI-related asynchronous work."

***

✅ This is a **high-scoring answer** — mentioning:

*   cancellation
*   lifecycle-awareness
*   memory safety

will make you stand out.

***

</details>

<details>
  <summary>13. What are dispatchers? state all the dispatchers along with their usage? why unconfined is not used?</summary>

***

# ✅ 1. What are Dispatchers?

### ✅ Definition (Interview Ready)

> **Dispatchers determine on which thread or thread pool a coroutine runs.**

***

## ✅ Key Idea

👉 Coroutines are lightweight, but they still need threads to execute  
👉 Dispatchers **decide execution context**

***

## ✅ Example

```kotlin
CoroutineScope(Dispatchers.IO).launch {
    // runs on background IO thread
}
```

***

# ✅ 2. Types of Dispatchers

***

# ✅ 1. Dispatchers.Main

### ✅ Used For:

*   UI operations
*   Updating views

***

## ✅ Example

```kotlin
lifecycleScope.launch(Dispatchers.Main) {
    textView.text = "Updated"
}
```

***

### ✅ Key Points:

*   Runs on **main (UI) thread**
*   Needed for:
    *   Compose UI updates
    *   LiveData observers
    *   View operations

***

***

# ✅ 2. Dispatchers.IO

### ✅ Used For:

*   Network calls
*   Database operations
*   File reading/writing

***

## ✅ Example

```kotlin
viewModelScope.launch(Dispatchers.IO) {
    val data = api.getAccounts()
}
```

***

### ✅ Key Points:

*   Optimized for **IO-heavy tasks**
*   Uses thread pool
*   Can have many threads (up to \~64)

***

***

# ✅ 3. Dispatchers.Default

### ✅ Used For:

*   CPU-intensive work
*   Heavy computation

***

## ✅ Example

```kotlin
viewModelScope.launch(Dispatchers.Default) {
    val result = calculateRiskScore()
}
```

***

### ✅ Key Points:

*   Optimized for CPU usage
*   Limited threads (based on CPU cores)

***

***

# ✅ 4. Dispatchers.Unconfined ⚠️

### ✅ Used For:

*   Rare cases (testing / debugging)

***

## ✅ Example

```kotlin
GlobalScope.launch(Dispatchers.Unconfined) {
    println(Thread.currentThread().name)
}
```

***

### ✅ Behavior:

*   Starts in **current thread**
*   Resumes in **different thread after suspension**

***

📌 Example:

```kotlin
GlobalScope.launch(Dispatchers.Unconfined) {
    println("Start: ${Thread.currentThread().name}")

    delay(1000)

    println("Resume: ${Thread.currentThread().name}")
}
```

👉 Output:

    Start: main
    Resume: DefaultExecutor

***

***

# ✅ 5. Why `Dispatchers.Unconfined` is NOT used? ⚠️

### ❗ Core Reason:

> It does NOT guarantee thread consistency

***

## ❌ Problems

***

### ❌ 1. Unpredictable thread switching

*   Starts in one thread
*   Resumes in another

👉 Leads to:

*   Race conditions
*   Debugging nightmare

***

### ❌ 2. Unsafe for UI

```kotlin
launch(Dispatchers.Unconfined) {
    textView.text = "Hello" // ❌ may crash
}
```

👉 Might run on non-main thread

***

### ❌ 3. Breaks structured concurrency expectations

***

### ❌ 4. Hard to test & maintain

***

## ✅ When (rarely) used?

*   Low-level coroutine libraries
*   Immediate execution scenarios
*   Debugging

***

# ✅ 6. Switching Dispatchers (Very Important)

***

## ✅ Example: Best Practice

```kotlin
viewModelScope.launch {
    val data = withContext(Dispatchers.IO) {
        repository.getData()
    }

    textView.text = data
}
```

***

👉 Flow:

1.  Main thread
2.  Switch to IO
3.  Back to Main

***

# ✅ 7. Real Banking App Usage 💰

***

### ✅ Main

*   Show account balance
*   Update UI

***

### ✅ IO

*   API calls
*   Database queries
*   Transaction processing

***

### ✅ Default

*   Risk calculation
*   Fraud detection algorithms

***

### ❌ Unconfined

*   ❌ Never used in production

***

# ✅ 8. Architect-Level Insight 💡

***

### ✅ Why dispatchers matter?

*   Performance ✅
*   Thread safety ✅
*   Avoid ANR ✅

***

### ✅ Golden Rule

> "Use the right dispatcher for the right job."

***

# ✅ 9. Quick Summary (Interview Revision 🚀)

***

### ✔ Dispatchers:

*   Decide thread execution

***

### ✔ Types:

| Dispatcher | Usage      |
| ---------- | ---------- |
| Main       | UI updates |
| IO         | Network/DB |
| Default    | CPU work   |
| Unconfined | Rare/debug |

***

### ✔ Why avoid Unconfined:

*   No thread guarantee ❌
*   Unpredictable execution ❌
*   Unsafe for UI ❌

***

# ✅ Perfect Interview Answer (Short Version)

> "Dispatchers in Kotlin coroutines define the thread on which a coroutine runs. Dispatchers.Main is used for UI operations, Dispatchers.IO for network and disk operations, and Dispatchers.Default for CPU-intensive work. Dispatchers.Unconfined is not recommended because it does not confine execution to any specific thread and may resume on a different thread after suspension, leading to unpredictable behavior and potential crashes, especially in UI operations."

***

✅ This is a **high-scoring answer** — especially if you mention:

*   thread pools
*   IO vs Default distinction
*   unpredictability of Unconfined

***

</details>

<details>
  <summary>14. What is the difference between launch, async and in which scenarios runblocking is used?</summary>

***

# ✅ 1. `launch` vs `async` (Core Difference)

***

## ✅ `launch`

### ✔ Purpose:

> Fire-and-forget coroutine (does NOT return a result)

***

## ✅ Example

```kotlin
viewModelScope.launch {
    val data = api.getData()
    println(data)
}
```

***

### ✔ Returns:

```kotlin
Job
```

👉 Used for:

*   UI updates
*   Logging
*   Background tasks

***

***

## ✅ `async`

### ✔ Purpose:

> Used when you need to return a result from a coroutine

***

## ✅ Example

```kotlin
val deferred = viewModelScope.async {
    api.getData()
}

val result = deferred.await()
```

***

### ✔ Returns:

```kotlin
Deferred<T>
```

👉 Used for:

*   Parallel tasks
*   Returning values

***

# ✅ 2. Key Differences

| Feature            | launch          | async                    |
| ------------------ | --------------- | ------------------------ |
| Return type        | `Job`           | `Deferred<T>`            |
| Returns result     | ❌ No            | ✅ Yes                    |
| Await required     | ❌ No            | ✅ Yes (`await()`)        |
| Use case           | Fire-and-forget | Parallel computation     |
| Exception handling | Immediate       | Deferred until `await()` |

***

# ✅ 3. Example: Parallel API Calls (Important)

***

## ✅ Using async

```kotlin
viewModelScope.launch {
    val user = async { api.getUser() }
    val accounts = async { api.getAccounts() }

    println(user.await())
    println(accounts.await())
}
```

✅ Runs in parallel → faster ✅

***

## ❌ Using launch

```kotlin
viewModelScope.launch {
    val user = launch { api.getUser() }
    val accounts = launch { api.getAccounts() }
}
```

❌ Cannot get result  
❌ Not useful for returning data

***

# ✅ 4. Exception Behavior ⚠️

***

## ✅ launch

```kotlin
launch {
    throw Exception("Error")
}
```

👉 Exception is thrown immediately

***

## ✅ async

```kotlin
val deferred = async {
    throw Exception("Error")
}

deferred.await() // Exception thrown HERE
```

👉 Exception is delayed until `await()`

***

# ✅ 5. What is `runBlocking`?

***

### ✅ Definition (Interview Ready)

> `runBlocking` is a coroutine builder that **blocks the current thread** until all coroutines inside it complete.

***

## ✅ Example

```kotlin
fun main() = runBlocking {
    val data = async {
        delay(1000)
        "Hello"
    }
    println(data.await())
}
```

***

👉 Output after 1 sec  
👉 Thread is blocked until completion

***

# ✅ 6. Why `runBlocking` is Dangerous in Android ⚠️

***

## ❌ Blocks main thread

```kotlin
runBlocking {
    delay(5000)
}
```

👉 UI freezes ❌  
👉 ANR (App Not Responding) ❌

***

# ✅ 7. When to Use `runBlocking` ✅

***

## ✅ 1. Unit Testing

```kotlin
@Test
fun testApi() = runBlocking {
    val result = repository.getData()
    assert(result.isNotEmpty())
}
```

***

## ✅ 2. Main function (Non-Android apps)

```kotlin
fun main() = runBlocking {
    println("Hello")
}
```

***

## ✅ 3. Bridging blocking & suspending code

```kotlin
fun getDataSync(): String = runBlocking {
    repository.getData()
}
```

***

# ✅ 8. When NOT to Use `runBlocking` ❌

***

### ❌ In Android UI

```kotlin
runBlocking {
    api.getData() // ❌ blocks UI
}
```

***

### ❌ Inside coroutines

```kotlin
launch {
    runBlocking { } // ❌ bad practice
}
```

***

# ✅ 9. Architect-Level Insight 💡

***

### ✅ launch vs async Design Philosophy

*   `launch` → side effects (actions)
*   `async` → results (values)

***

### ✅ runBlocking

> "Bridge between blocking world and coroutine world"

But:

*   Should NOT be used in production UI layer

***

# ✅ 10. Real Banking App Use Case 💰

***

### ✅ launch

*   Trigger transaction
*   Log analytics
*   Update UI

***

### ✅ async

*   Fetch user + accounts in parallel
*   Calculate risk score + data aggregation

***

### ✅ runBlocking

*   ✅ Unit testing
*   ❌ Never in UI

***

# ✅ 11. Quick Summary (Interview Revision 🚀)

***

### ✔ launch:

*   Fire-and-forget
*   Returns Job
*   No result

***

### ✔ async:

*   Returns result
*   Returns Deferred
*   Needs `await()`

***

### ✔ runBlocking:

*   Blocks thread
*   Used in testing / main()
*   ❌ Avoid in Android UI

***

# ✅ Perfect Interview Answer (Short Version)

> "The `launch` coroutine builder is used for fire-and-forget tasks and returns a Job, while `async` is used when we need a result and returns a Deferred, which we retrieve using `await()`. `runBlocking` blocks the current thread until its coroutine completes, and is mainly used in testing or main functions. It should not be used in Android UI because it can block the main thread and cause ANRs."

***

✅ If you explain:

*   Job vs Deferred
*   await behavior
*   runBlocking pitfalls

👉 That’s a **top-tier architect answer**

***

</details>

<details>
  <summary>15. What happens if you cancel a coroutine while it is running a blocking operation?</summary>

***

# ✅ 1. Short Answer (Interview Ready)

> "If a coroutine is running a blocking operation, cancellation will not take effect immediately because coroutines support cooperative cancellation. The coroutine will only cancel when it reaches a suspension point or explicitly checks for cancellation."

***

# ✅ 2. Key Concept: Cooperative Cancellation

👉 Coroutines **do NOT stop automatically** when cancelled  
👉 They must **cooperate** and check for cancellation

***

## ✅ Cancellation works when:

*   Suspension functions (`delay`, `yield`) ✅
*   Explicit checks (`isActive`) ✅

***

## ❌ Cancellation does NOT work when:

*   Blocking calls (e.g., `Thread.sleep()`) ❌

***

# ✅ 3. Example: Blocking Operation ❌

```kotlin
val job = CoroutineScope(Dispatchers.Default).launch {
    println("Started")

    Thread.sleep(5000) // ❌ blocking call

    println("Finished")
}

Thread.sleep(1000)
job.cancel()
```

***

## ✅ Output

    Started
    (After 5 seconds)
    Finished

***

👉 Even after `job.cancel()`:

*   Coroutine does NOT stop ❌
*   Continues executing

***

# ✅ 4. Why This Happens?

👉 Because:

*   `Thread.sleep()` is **blocking**
*   It does NOT check coroutine cancellation
*   Coroutine cannot interrupt it

***

# ✅ 5. Correct Way (Using Suspension ✅)

```kotlin
val job = CoroutineScope(Dispatchers.Default).launch {
    println("Started")

    delay(5000) // ✅ cancellable

    println("Finished")
}

Thread.sleep(1000)
job.cancel()
```

***

## ✅ Output

    Started
    (coroutine gets cancelled)
    (no "Finished" printed)

***

👉 `delay()` supports cancellation ✅

***

# ✅ 6. How to Handle Blocking Code Properly ✅

***

## ✅ Option 1: Use `withContext(Dispatchers.IO)`

```kotlin
withContext(Dispatchers.IO) {
    blockingCall()
}
```

👉 Keeps blocking work off main thread  
(but still not cancellable unless managed)

***

***

## ✅ Option 2: Check for cancellation manually

```kotlin
launch {
    while (isActive) {
        // do work
    }
}
```

***

***

## ✅ Option 3: Use `ensureActive()`

```kotlin
launch {
    repeat(1000) {
        ensureActive() // throws cancellation exception
        Thread.sleep(10)
    }
}
```

***

***

## ✅ Option 4: Wrap blocking in cancellable APIs (advanced)

Use:

*   interruptible APIs
*   or `suspendCancellableCoroutine`

***

# ✅ 7. Real-World Banking Impact 🚨

***

### ❗ Problem Scenario

```kotlin
launch {
    processTransaction() // blocking API
}
```

👉 If user navigates away:

*   Coroutine cancelled ✅
*   BUT blocking call continues ❌

***

👉 Risks:

*   Resource leakage ❌
*   Duplicate processing ❌
*   Incorrect transaction state ❌

***

# ✅ 8. Architect-Level Insight 💡

***

### ✅ Golden Rule

> "Coroutine cancellation is cooperative, not preemptive."

***

### ✅ Important Distinction

| Type                | Behavior          |
| ------------------- | ----------------- |
| Suspending function | Cancels ✅         |
| Blocking function   | Does NOT cancel ❌ |

***

***

# ✅ 9. How Kotlin Encourages Safe Design

*   Avoid blocking APIs ✅
*   Use suspend functions ✅
*   Use structured concurrency ✅

***

# ✅ 10. Quick Summary (Interview Revision 🚀)

***

### ✔ Cancellation:

*   Cooperative ✅

***

### ✔ Blocking calls:

*   Ignore cancellation ❌
*   Continue execution ❌

***

### ✔ Safe alternatives:

*   Use `delay()` ✅
*   Use suspend functions ✅
*   Check `isActive` ✅

***

### ✔ Key Risk:

*   Memory leaks
*   Wasted resources
*   Incorrect app state

***

# ✅ Perfect Interview Answer (Short Version)

> "If a coroutine is cancelled while executing a blocking operation like Thread.sleep, it will not stop immediately because coroutine cancellation is cooperative. Blocking operations do not check for cancellation, so the coroutine continues until the operation completes. To handle this properly, we should use cancellable suspension functions like delay or explicitly check for cancellation using isActive or ensureActive."

***

✅ This is a **strong architect-level answer** — especially if you mention:

*   cooperative cancellation
*   blocking vs suspending difference
*   real-world impact

***

</details>

<details>
  <summary>16. What is supervisor job? How is different from normal Job?</summary>

***

# ✅ 1. What is a `Job`?

👉 A `Job` represents a coroutine and its lifecycle.

```kotlin
val job = CoroutineScope(Dispatchers.Default).launch {
    // coroutine work
}
```

***

# ✅ 2. What is a `SupervisorJob`?

### ✅ Definition (Interview Ready)

> **SupervisorJob is a special type of Job where failure of one child coroutine does NOT affect other child coroutines.**

***

# ✅ 3. Default Behavior (Normal Job) ❌

👉 In a regular `Job`, failure **propagates downward and upward**

***

## ❌ Example (Normal Job)

```kotlin
val scope = CoroutineScope(Job())

scope.launch {
    launch {
        delay(1000)
        println("Child 1 completed")
    }

    launch {
        throw Exception("Failure in Child 2")
    }
}
```

***

### ✅ What happens?

*   Child 2 fails ❌
*   Entire parent scope is cancelled ❌
*   Child 1 is also cancelled ❌

👉 Output:

    Exception in Child 2
    (child1 never completes)

***

# ✅ 4. SupervisorJob Behavior ✅

👉 Failure is **isolated**

***

## ✅ Example

```kotlin
val scope = CoroutineScope(SupervisorJob())

scope.launch {
    launch {
        delay(1000)
        println("Child 1 completed")
    }

    launch {
        throw Exception("Failure in Child 2")
    }
}
```

***

### ✅ What happens?

*   Child 2 fails ❌
*   Child 1 continues normally ✅
*   Parent is NOT cancelled ✅

👉 Output:

    Exception in Child 2
    Child 1 completed ✅

***

# ✅ 5. Core Difference

| Feature             | Job                    | SupervisorJob              |
| ------------------- | ---------------------- | -------------------------- |
| Failure propagation | Cancels all children ❌ | Does NOT cancel siblings ✅ |
| Isolation           | ❌ No isolation         | ✅ Yes                      |
| Use case            | Dependent tasks        | Independent tasks          |

***

# ✅ 6. Visual Understanding

***

### ❌ Normal Job

    Parent Job
       ├── Child 1 ✅
       ├── Child 2 ❌ (fails)
       └── Child 3 ❌ (cancelled due to failure)

***

### ✅ SupervisorJob

    Parent SupervisorJob
       ├── Child 1 ✅
       ├── Child 2 ❌ (fails)
       └── Child 3 ✅ (continues unaffected)

***

# ✅ 7. Real Banking App Use Case 💰

***

## ✅ Example: Parallel API Calls

```kotlin
val scope = CoroutineScope(SupervisorJob())

scope.launch {
    launch { fetchUserData() }
    launch { fetchTransactions() }
    launch { fetchOffers() }
}
```

***

👉 If `fetchOffers()` fails:

*   User + Transactions still succeed ✅
*   App remains usable ✅

***

### ❌ With normal Job:

*   One API failure cancels all ❌
*   Entire screen fails ❌

***

# ✅ 8. Alternative: `supervisorScope` ✅

***

## ✅ Example

```kotlin
suspend fun loadData() = supervisorScope {
    launch { fetchUser() }
    launch { fetchAccounts() }
}
```

👉 Same behavior as SupervisorJob  
👉 Used inside suspend functions

***

# ✅ 9. Exception Handling Difference ⚠️

***

## ✅ With Job

*   Exception propagates immediately

***

## ✅ With SupervisorJob

👉 You need to handle exceptions manually per child

```kotlin
launch {
    try {
        fetchData()
    } catch (e: Exception) {
        println("Handled error")
    }
}
```

***

# ✅ 10. Architect-Level Insight 💡

***

### ✅ When to use Job

*   Tasks are **dependent**
*   Failure should stop everything

***

### ✅ When to use SupervisorJob

*   Tasks are **independent**
*   Partial success is acceptable ✅

***

### ✅ Golden Rule

> "Use SupervisorJob when failure isolation is required."

***

# ✅ 11. Common Pitfalls ⚠️

***

### ❌ Not handling exceptions in SupervisorJob

👉 Exceptions won’t cancel siblings, but still crash if unhandled

***

### ❌ Using normal Job for parallel independent calls

👉 Leads to unnecessary failures

***

# ✅ 12. Quick Summary (Interview Revision 🚀)

***

### ✔ Job:

*   Failure cancels all children ❌
*   Use for dependent tasks

***

### ✔ SupervisorJob:

*   Failure isolated ✅
*   Other children continue ✅
*   Use for independent tasks

***

### ✔ Alternative:

*   `supervisorScope {}`

***

# ✅ Perfect Interview Answer (Short Version)

> "A SupervisorJob is a special type of Job where the failure of one child coroutine does not cancel its siblings, allowing independent tasks to continue execution. In contrast, a normal Job propagates failure, meaning if one child fails, all other children are cancelled. SupervisorJob is useful when running independent operations like parallel API calls, where one failure should not affect others."

***

✅ This is a **high-impact answer** — mentioning:

*   failure isolation
*   parallel calls
*   real-world use

👉 will impress interviewers.

***

</details>

<details>
  <summary>17. What happens to the call stack when a coroutine suspends?</summary>

***

# ✅ 1. Short Answer (Interview Ready)

> "When a coroutine suspends, its call stack is not kept on the thread stack. Instead, it is copied and stored as a continuation object on the heap. The thread becomes free to execute other work, and when the coroutine resumes, the saved state is restored and execution continues from where it was suspended."

***

# ✅ 2. Traditional Call Stack (Thread)

👉 In normal code:

```kotlin
fun main() {
    functionA()
}

fun functionA() {
    functionB()
}

fun functionB() {
    Thread.sleep(1000)
}
```

### ✅ What happens:

*   Stack grows:

<!---->

    main → functionA → functionB

*   Thread is **blocked** during sleep ❌
*   Stack remains occupied ❌

***

# ✅ 3. Coroutine Behavior (Key Difference)

```kotlin
suspend fun functionB() {
    delay(1000)
}
```

👉 When `delay()` is called:

### ✅ Steps:

1.  Coroutine **suspends**
2.  Current state (variables + position) is saved
3.  Stack is **cleared**
4.  Thread is **freed** ✅

***

# ✅ 4. Where Does the Stack Go?

👉 The call stack is transformed into a **Continuation object**

***

## ✅ Continuation Contains:

*   Execution point (where to resume)
*   Local variables
*   Next instruction

***

### ✅ Simplified Representation

    Heap:
    Continuation {
        state = functionB paused at delay()
        local variables
    }

### ✅ Thread:

    FREE ✅ (can execute other coroutines)

***

# ✅ 5. When Coroutine Resumes

After delay completes:

1.  Continuation is picked up
2.  Execution resumes from saved point

***

```kotlin
println("Resumed after delay")
```

👉 It feels like:

*   Stack was paused ✅
*   But actually reconstructed ❗

***

# ✅ 6. Visual Flow

***

### Before Suspension

    Thread Stack:
    main → functionA → functionB

***

### At Suspension

    Thread Stack: (cleared)
    FREE ✅

    Heap:
    Continuation(functionB state)

***

### After Resume

    Thread Stack (possibly different thread):
    functionB → continues execution

***

# ✅ 7. Important Insight 💡

👉 Coroutines provide **stackless suspension**

✔ They do NOT preserve actual thread stack  
✔ They store state in objects

***

# ✅ 8. Why This is Powerful

***

### ✅ 1. No Thread Blocking

*   Thread is reused ✅
*   High scalability ✅

***

### ✅ 2. Lightweight

*   Thousands of coroutines possible ✅
*   Threads are expensive ❌

***

### ✅ 3. Thread Switching

*   Resume can happen on **different thread**

```kotlin
withContext(Dispatchers.IO)
```

***

# ✅ 9. Real Banking App Impact 💰

***

### ✅ Scenario: API call

```kotlin
val data = api.getAccounts() // suspend function
```

👉 During network call:

*   Coroutine suspended ✅
*   Thread freed ✅
*   Other requests handled ✅

***

### ❗ Without coroutines:

*   Thread blocked ❌
*   Lower scalability ❌

***

# ✅ 10. Architect-Level Insight 💡

***

### ✅ Key Concept

> "Coroutines do not preserve the call stack — they transform it into a state machine."

***

### ✅ Compiler Role

Kotlin compiler:

*   Converts suspend functions into **state machines**
*   Generates continuation objects

***

# ✅ 11. Common Interview Pitfalls ⚠️

***

### ❌ Thinking coroutine pauses thread

👉 Wrong:

*   Coroutine pauses ✅
*   Thread continues working ✅

***

### ❌ Assuming stack is preserved

👉 Not true:

*   Stack is **recreated** from continuation

***

# ✅ 12. Quick Summary (Interview Revision 🚀)

***

### ✔ On suspension:

*   Call stack is saved in **Continuation (heap)**
*   Thread stack is cleared ✅

***

### ✔ Thread:

*   Freed to do other work ✅

***

### ✔ On resume:

*   Stack reconstructed
*   Execution continues

***

### ✔ Key benefit:

*   Non-blocking
*   Scalable

***

# ✅ Perfect Interview Answer (Best Version)

> "When a coroutine suspends, its call stack is not preserved on the thread. Instead, the Kotlin compiler converts the coroutine into a state machine and stores its execution state in a continuation object on the heap. The thread is then freed to perform other work. When the coroutine resumes, the continuation restores the state and execution continues from where it left off. This is what makes coroutines lightweight and non-blocking compared to traditional threads."

***

✅ This is a **top-tier architect answer** — if you mention:

*   continuation
*   state machine
*   heap vs stack
*   thread reuse

👉 you’ll definitely stand out.

***

</details>

<details>
  <summary>18. What is reified keyword? How does it help in collection?</summary>

***

# ✅ 1. What is `reified` keyword?

### ✅ Definition (Interview Ready)

> `reified` allows you to access the actual type of a generic parameter at runtime, which is normally erased due to type erasure in Kotlin/Java.

***

# ✅ 2. The Problem: Type Erasure ⚠️

👉 In Kotlin/Java generics:

```kotlin
fun <T> printType(value: T) {
    println(T::class) // ❌ Compile error
}
```

👉 Why?

*   Generic type `T` is **erased at runtime**
*   JVM doesn’t know what `T` is

***

# ✅ 3. Solution: `reified` + `inline`

```kotlin
inline fun <reified T> printType(value: T) {
    println(T::class) // ✅ Works
}
```

***

## ✅ Why it works?

*   `inline` → function is copied at call site
*   `reified` → actual type is substituted

***

## ✅ Example

```kotlin
printType<String>("Hello")
printType<Int>(10)
```

👉 Output:

    class kotlin.String
    class kotlin.Int

***

# ✅ 4. Rules for `reified`

*   Must be used with `inline` ✅
*   Works only for **generic type parameters**
*   Available only at **compile-time substitution**

***

# ✅ 5. How it Helps in Collections (VERY IMPORTANT)

***

## ✅ Use Case 1: Filtering collections by type

***

## ❌ Without `reified`

```kotlin
fun <T> filterByType(list: List<Any>): List<T> {
    return list.filter { it is T } // ❌ Not allowed
}
```

***

## ✅ With `reified`

```kotlin
inline fun <reified T> filterByType(list: List<Any>): List<T> {
    return list.filter { it is T }.map { it as T }
}
```

***

## ✅ Usage

```kotlin
val items = listOf(1, "Hello", 2, "World")

val strings = filterByType<String>(items)

println(strings) // [Hello, World]
```

***

# ✅ 6. Real-Life Shortcut (Kotlin provides this!)

```kotlin
val strings = items.filterIsInstance<String>()
```

👉 Internally uses `reified`

***

# ✅ 7. Use Case 2: Safe type casting in collections

```kotlin
inline fun <reified T> castList(list: List<Any>): List<T> {
    return list.mapNotNull { it as? T }
}
```

***

# ✅ 8. Use Case 3: JSON Parsing (Very Common)

***

## ✅ Without reified ❌

```kotlin
fun <T> parse(json: String, clazz: Class<T>)
```

👉 Verbose

***

## ✅ With reified ✅

```kotlin
inline fun <reified T> parse(json: String): T {
    return gson.fromJson(json, T::class.java)
}
```

***

## ✅ Usage

```kotlin
val user: User = parse(json)
```

👉 Cleaner ✅  
👉 No need to pass class manually ✅

***

# ✅ 9. Use Case 4: Checking type in collections

```kotlin
inline fun <reified T> Any.isType(): Boolean {
    return this is T
}
```

***

# ✅ 10. Architect-Level Insight 💡

***

### ✅ Why reified is powerful?

*   Eliminates boilerplate ✅
*   Makes APIs cleaner ✅
*   Enables runtime type checks ✅
*   Improves type safety ✅

***

### ✅ Used heavily in:

*   Retrofit / Gson / Moshi
*   Dependency Injection
*   Navigation
*   Collection filtering

***

# ✅ 11. Limitation ⚠️

***

### ❌ Cannot use without inline

```kotlin
fun <reified T> test() // ❌ Compile error
```

***

### ❌ Not usable in Java

*   Java doesn’t support reified generics

***

# ✅ 12. Common Interview Pitfalls ⚠️

***

### ❌ Thinking generics are always available at runtime

👉 They are erased ❌

***

### ❌ Forgetting inline requirement

👉 Must combine:

    inline + reified

***

# ✅ 13. Quick Summary (Interview Revision 🚀)

***

### ✔ Problem:

*   Generics are erased at runtime

***

### ✔ Solution:

*   `inline + reified`

***

### ✔ Benefits:

*   Access type at runtime ✅
*   Filter collections ✅
*   Simplify APIs ✅

***

### ✔ Common Uses:

*   `filterIsInstance<T>()`
*   JSON parsing
*   Type checking

***

# ✅ Perfect Interview Answer (Short Version)

> "The `reified` keyword in Kotlin allows access to a generic type at runtime, which is otherwise erased due to type erasure. It can only be used with inline functions. It is especially useful in collections for operations like filtering elements by type or safe casting, as it allows us to check and use the actual type of objects without explicitly passing class references."

***

✅ This is a **high-value answer** — if you mention:

*   type erasure
*   inline dependency
*   collection filtering

👉 you'll stand out.

***

</details>

<details>
  <summary>19. Coding Challenge</summary>

***
### Write a code of sample app in Android
#### Instructions for the candidate
- Implement android app that fetches a list of characters from this endpoint
```text
https://rickandmortyapi.com/api/character
```
and displays a single screen with a list of characters.

#### For each character we only want to present 2 texts:
- Name of the character (name) (e.g. "Rick Sanchez")
- Name of the origin (origin.name) (e.g. "Earth (C-137)")

#### Additional instructions
- Ul design of the app is not important.
- Focus on implementing clean and readable code as much as it's possible within 1 hour. If you skip something, that normally you would do, e.g. you skip setting up dependency injection framework, just tell us that you decided to skip implementing dependency injection due to the time constraints.
- Ignore the fact that this endpoint returns paginated results, we only want to show the list of characters from the first page.
- Feel free to use the internet to clarify tech questions like we all do in our work.
- Feel free to copy/paste code from the internet as long as you understand what it does and you can explain it.
- Do not use IDE integrated Al tools Chat Apps
- Don't copy/paste code from your other project(s).

**Note:** App doesn't have to be finished and code doesn't have to compile, for us it's important to observe how you work and we sometimes we may interrupt you and ask to start working on some other part of the app, e.g. we see that implementation of the data layer looks good, so we may ask to leave it as it is and start implementing ViewModel or Compose Screen.

***

</details>

<details>
  <summary>20. Code Solution</summary>

***

# ✅ ✅ Approach (What interviewer expects)

We’ll build:

*   ✅ Retrofit for API
*   ✅ Repository layer
*   ✅ ViewModel with StateFlow
*   ✅ Jetpack Compose UI
*   ✅ Simple architecture (no DI due to time)

***

# ✅ 1. Data Models

```kotlin
data class ApiResponse(
    val results: List<CharacterDto>
)

data class CharacterDto(
    val id: Int,
    val name: String,
    val origin: OriginDto
)

data class OriginDto(
    val name: String
)
```

***

# ✅ Add Domain Layer

```kotlin
data class Character(
    val id: Int,
    val name: String,
    val originName: String
)
```

# ✅ Mapper (DTO → Domain)

```kotlin
fun CharacterDto.toDomain(): Character {
    return Character(
        id = id,
        name = name,
        originName = origin.name
    )
}
```

# ✅ 2. Retrofit API

```kotlin
interface ApiService {

    @GET("character")
    suspend fun getCharacters(): ApiResponse
}
```

***

# ✅ Retrofit Builder (simple, no DI)

```kotlin
object RetrofitInstance {

    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
```

***

# ✅ 3. Repository Layer

```kotlin
class CharacterRepository(private val api: ApiService) {
    suspend fun getCharacters(): List<Character> {
        return api.getCharacters().results.map { it.toDomain() }
    }
}
```

***

# ✅ 4. ViewModel (StateFlow)

```kotlin
data class UiState(
    val isLoading: Boolean = false,
    val characters: List<Character> = emptyList(),
    val error: String? = null
)
```

***

```kotlin
class CharacterViewModel : ViewModel() {

    private val api = RetrofitInstance.api
    private val repository = CharacterRepository(api)

    private val _state = MutableStateFlow(UiState(isLoading = true))
    val state: StateFlow<UiState> = _state

    init {
        fetchCharacters()
    }

    private fun fetchCharacters() {
        viewModelScope.launch {
            try {
                val data = repository.getCharacters()
                _state.value = UiState(characters = data)
            } catch (e: Exception) {
                _state.value = UiState(error = e.message)
            }
        }
    }
}
```

***

# ✅ 5. Compose UI

## ✅ Main Screen

```kotlin
@Composable
fun CharacterScreen(viewModel: CharacterViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    when {
        state.isLoading -> CircularProgressIndicator()
        state.error != null -> Text("Error: ${state.error}")
        else -> CharacterList(state.characters)
    }
}
```

***

## ✅ List UI

```kotlin
@Composable
fun CharacterList(list: List<Character>) {
    LazyColumn {
        items(list, key = { it.id }) {
            CharacterItem(it)
        }
    }
}
```

***

## ✅ Item UI

```kotlin
@Composable
fun CharacterItem(character: Character) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = character.name)
        Text(text = character.originName)
    }
}
```

***

# ✅ 6. Activity Setup

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CharacterScreen()
        }
    }
}
```

***

# ✅ 7. Dependencies (Important)

```gradle
implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2"
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3"
implementation "androidx.compose.ui:ui:1.5.0"
implementation "androidx.compose.material3:material3:1.1.2"
implementation "androidx.activity:activity-compose:1.8.0"

implementation "com.squareup.retrofit2:retrofit:2.9.0"
implementation "com.squareup.retrofit2:converter-gson:2.9.0"
```

***

# ✅ 8. What I intentionally skipped (Important for interview ✅)

👉 You should SAY this explicitly during interview:

***

### ✅ Skipped for simplicity (due to time constraint):

*   ❌ Dependency Injection (Hilt/Dagger)
*   ❌ Domain layer (UseCases)
*   ❌ Error mapping (sealed classes)
*   ❌ Pagination handling
*   ❌ Unit tests
*   ❌ UI styling/Material polish

***

# ✅ 9. Architecture Explanation (IMP for Interview)

👉 This follows:

*   ✅ MVVM
*   ✅ Clean separation:
    *   UI → Compose
    *   State → ViewModel
    *   Data → Repository
*   ✅ Unidirectional data flow

***

# ✅ 10. Improvements (If interviewer asks)

You can suggest:

*   Add Hilt for DI
*   Add domain layer
*   Add sealed UI states
*   Add pagination support
*   Use DTO → Domain mapping
*   Add caching (Room DB)

***

# ✅ ✅ Final Short Summary (Interview Ready)

> "I implemented the app using MVVM architecture with Retrofit for networking, a repository layer for abstraction, and StateFlow in ViewModel for state management. The UI is built using Compose with a unidirectional data flow. Due to time constraints, I skipped dependency injection and pagination, but the structure is easily extendable to production-level code."

***

✅ This answer + code is **exactly what interviewers expect in 1-hour exercise**

***

</details>

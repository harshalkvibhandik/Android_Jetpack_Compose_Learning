# Jetpack Compose Learning

<details>
  <summary><b>📂 Core concepts</b></summary>

  <details style="margin-left: 20px;">
    <summary><i>📄 1. What is Jetpack Compose and how is it different from XML-based UI?</i></summary>

***

# ✅ What is Jetpack Compose and how is it different from XML-based UI?

## ✅ Short Interview Answer (Good Opening)

> **Jetpack Compose** is Android’s **modern declarative UI toolkit** that allows developers to build UI using **Kotlin code instead of XML**.  
> Unlike the traditional XML-based UI (which is imperative), Compose follows a **declarative programming model**, where the UI automatically updates when the underlying state changes.

This answer sets the tone. Now let’s go deeper.

***

## 1️⃣ What is Jetpack Compose? (Conceptual Explanation)

**Jetpack Compose** is:

*   A **UI toolkit**
*   Written completely in **Kotlin**
*   Part of **Android Jetpack**
*   Inspired by modern frameworks like **React, Flutter, SwiftUI**

### Key idea:

> *“UI is a function of state.”*

Instead of telling Android **how** to change the UI step-by-step, you simply describe **what the UI should look like for a given state**.

***

## 2️⃣ XML-based UI (Traditional Way) – How it Works

### Characteristics:

*   UI is defined in **XML layout files**
*   Logic is written in **Kotlin/Java**
*   Developer manually updates UI using:
    *   `findViewById`
    *   `setText()`
    *   `setVisibility()`
    *   `notifyDataSetChanged()` (RecyclerView)

### Imperative approach:

> You tell the system **how to update the UI**

### Example (XML-based UI)

**layout.xml**

```xml
<TextView
    android:id="@+id/tvName"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Hello" />
```

**MainActivity.kt**

```kotlin
val tvName = findViewById<TextView>(R.id.tvName)
tvName.text = "Hello Harshal"
```

👉 You **manually mutate** the UI.

***

## 3️⃣ Jetpack Compose – How it Works

### Characteristics:

*   UI defined using **@Composable functions**
*   No XML
*   Less boilerplate
*   Direct integration with Kotlin features
*   Built-in support for:
    *   State
    *   Animation
    *   Theming
    *   Accessibility

### Declarative approach:

> You describe **what** the UI should be, not **how** to update it.

***

## 4️⃣ Simple Jetpack Compose Example

### Compose UI

```kotlin
@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name")
}
```

### Usage

```kotlin
setContent {
    Greeting(name = "Harshal")
}
```

👉 When `name` changes, the UI automatically **recomposes**.

***

## 5️⃣ Biggest Conceptual Difference (🔥 Interview Highlight)

### XML UI:

> UI is **mutable** and updated **manually**

### Compose:

> UI is **stateless**, driven by **state changes**

### Example with State (Compose)

```kotlin
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }

    Button(onClick = { count++ }) {
        Text("Count: $count")
    }
}
```

✅ When `count` changes → Compose **re-executes the function** → UI updates automatically.

***

## 6️⃣ Key Differences Between Jetpack Compose and XML UI

### 🧠 Programming Model

| Aspect         | XML-based UI | Jetpack Compose |
| -------------- | ------------ | --------------- |
| Approach       | Imperative   | Declarative     |
| UI Definition  | XML + Kotlin | Kotlin only     |
| State Handling | Manual       | Automatic       |
| Boilerplate    | Higher       | Much lower      |

***

### ⚙️ Development Experience

| Feature      | XML      | Compose      |
| ------------ | -------- | ------------ |
| findViewById | Required | ❌ Not needed |
| Preview UI   | Slow     | ✅ @Preview   |
| Reusable UI  | Hard     | ✅ Easy       |
| Animation    | Complex  | ✅ Simple     |

***

## 7️⃣ Recomposition – Important Interview Topic

> **Recomposition** is the process where Compose **re-runs composable functions** when state changes.

✅ Compose is **smart**:

*   Only recomposes affected UI
*   Improves performance
*   Avoids full redraws

### Interview Line:

> *“Compose uses intelligent recomposition to update only the parts of the UI affected by state changes.”*

***

## 8️⃣ Why Google Introduced Jetpack Compose?

You can answer like this:

> Google introduced Jetpack Compose to:
>
> *   Reduce boilerplate code
> *   Improve developer productivity
> *   Enable modern UI patterns
> *   Simplify state management
> *   Align Android UI with modern frameworks

✅ Google officially recommends Compose for **new projects**

***

## 9️⃣ Can XML and Compose Co-exist? (TCS Loves This Question)

✅ **Yes**, they can work together.

*   Use **ComposeView** inside XML
*   Use **AndroidView** inside Compose

### Example (Compose inside XML)

```xml
<androidx.compose.ui.platform.ComposeView
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

```kotlin
composeView.setContent {
    Greeting("Harshal")
}
```

***

## 🔥 Interview-Winning One-Liner Summary

> *“Jetpack Compose is a declarative, Kotlin-first UI toolkit that eliminates XML, reduces boilerplate, and automatically updates the UI based on state changes—making Android UI development faster, safer, and more maintainable compared to the traditional XML-based approach.”*

***

  </details>

  <details style="margin-left: 20px;">
    <summary><i>📄 2. How does Compose work internally (Recomposition, Slot Table – high level)?</i></summary>

***

# ✅ How does Jetpack Compose work internally?

### (Recomposition & Slot Table – High Level)

## ✅ Short Interview Answer (Strong Opening)

> Jetpack Compose works by **tracking state changes**, **re-running composable functions during recomposition**, and efficiently updating the UI using an internal data structure called the **Slot Table**, which allows Compose to update only the affected parts of the UI instead of redrawing everything.

***

## 1️⃣ Big Picture: Compose Rendering Pipeline

At a high level, Compose follows this flow:

    Composable Functions
          ↓
    Initial Composition
          ↓
    Slot Table (UI Structure)
          ↓
    State Change
          ↓
    Recomposition
          ↓
    Minimal UI Update

✅ Compose does **not use View hierarchy like XML UI**  
✅ Compose treats UI as a **tree of composable function calls**

***

## 2️⃣ Composition vs Recomposition (Key Difference)

### 🔹 Composition

*   Happens **first time** a composable is executed
*   Builds UI structure
*   Fills the Slot Table

### 🔹 Recomposition

*   Happens when **state changes**
*   Re-runs only affected composables
*   Updates existing UI efficiently

🎯 **Interview line:**

> *“Composition creates the UI, while recomposition updates it when state changes.”*

***

## 3️⃣ How State Triggers Recomposition

Compose tracks **observable state**:

*   `mutableStateOf`
*   `State<T>`
*   `LiveData`
*   `StateFlow`

### Example

```kotlin
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }

    Text("Count: $count")

    Button(onClick = { count++ }) {
        Text("Increment")
    }
}
```

### What happens internally?

1.  `count` is read → Compose records **dependency**
2.  `count++` changes state
3.  Compose **marks dependent composables as “dirty”**
4.  Recomposition runs only for those composables

✅ This is why Compose is **efficient by default**

***

## 4️⃣ What is Recomposition? (Deep but Interview-friendly)

### 🔹 Definition

> Recomposition is the process where Compose **re-executes composable functions whose state has changed**, compares the result with previous composition, and updates only what’s needed.

### 🔹 Important Clarification:

⚠️ Recomposition does NOT mean:

*   Recreating entire UI
*   Redrawing everything

✅ Instead:

*   Only **affected nodes are updated**
*   Rest of the UI remains untouched

***

## 5️⃣ Slot Table – The Heart of Compose (High Level)

### ✅ What is Slot Table?

> **Slot Table** is an internal data structure where Compose stores information about executed composable functions and their emitted UI nodes.

Think of it as:

*   A **flattened tree**
*   That remembers:
    *   What composables were called
    *   In what order
    *   With what parameters

***

### 🧠 Why Slot Table Exists?

XML UI → View hierarchy  
Compose → **Function execution history**

Compose needs:

*   To **remember previous UI structure**
*   To **compare old vs new composition**
*   To **reuse existing UI nodes**

✅ Slot Table enables **diffing without View IDs**

***

## 6️⃣ Slot Table Conceptual Example

### Composable Code

```kotlin
@Composable
fun Screen(showText: Boolean) {
    Column {
        Text("Header")

        if (showText) {
            Text("Visible")
        }

        Text("Footer")
    }
}
```

### Internally (Conceptual Slots)

    Slot 0 → Column
    Slot 1 → Text("Header")
    Slot 2 → Conditional Group
    Slot 3 → Text("Visible")  <-- Optional
    Slot 4 → Text("Footer")

### When `showText` changes:

*   Compose can **insert or skip Slot 3**
*   Without rebuilding Slots 1 and 4

🔥 **Interview takeaway:**

> *“Slot Table allows Compose to efficiently insert, remove, and reuse UI elements based on composition order.”*

***

## 7️⃣ Compiler Magic: Why Order Matters

Compose **relies on call order**, not IDs.

### 🚫 Problematic Code

```kotlin
if (condition) {
    Text("A")
}
Text("B")
```

If the condition changes frequently, the **slot positions shift**.

✅ Compose compiler:

*   Groups composables
*   Generates stable keys
*   Maintains correct slot mapping

🎯 Interview phrase:

> *“Compose depends on stable call order, which is why keys are important in dynamic lists.”*

***

## 8️⃣ Role of `key()` in Slot Table

### Without key (Problem)

```kotlin
list.forEach {
    Text(it.name)
}
```

### With key (Correct)

```kotlin
list.forEach {
    key(it.id) {
        Text(it.name)
    }
}
```

✅ Helps Compose:

*   Identify which slot belongs to which item
*   Prevent incorrect recomposition
*   Avoid UI glitches

***

## 9️⃣ Skipping & Smart Recomposition

Compose optimizes automatically:

### ✅ Skipping

If parameters haven’t changed:

*   Composable is **skipped**
*   Function is NOT re-executed

```kotlin
@Composable
fun Greeting(name: String) {
    Text("Hello $name")
}
```

✅ If `name` doesn’t change → Greeting is skipped

***

## 10️⃣ remember() and Slot Table

`remember` stores values **inside slots**

```kotlin
val count = remember { mutableStateOf(0) }
```

✅ Stored in Slot Table
✅ Survives recompositions
❌ Reset if composable leaves composition

🎯 Interview line:

> *“remember persists values across recompositions but not across configuration changes.”*

***

## 11️⃣ Compose vs Traditional View System (Internals)

| Aspect    | View System   | Compose                  |
| --------- | ------------- | ------------------------ |
| UI Model  | Mutable Views | Immutable UI description |
| Updates   | Manual        | Automatic                |
| Rendering | invalidate()  | Recomposition            |
| Structure | View Tree     | Slot Table               |

***

## 🔥 Interview-Ready Final Summary

> *“Jetpack Compose works by executing composable functions to build a UI tree stored internally in a Slot Table. When state changes, Compose performs recomposition—re-running only the affected composables—using the Slot Table to efficiently reuse or update UI nodes. This declarative approach avoids manual UI updates and results in better performance and simpler code.”*

***

  </details>

  <details style="margin-left: 20px;">
    <summary><i>📄 3. What is @Composable? Can we call a composable from non-composable?</i></summary>

***

## ✅ What is `@Composable`?

**`@Composable`** is a Kotlin annotation that marks a function as part of the **Compose runtime**—i.e., a function that **describes UI**.  
A composable function:

*   **Declares** what the UI should look like for a given **state**.
*   Can be **re-run** by the Compose runtime during **recomposition** when relevant state changes.
*   **Emits UI** elements (nodes) into the composition instead of returning UI objects.

> **Interview one-liner:**  
> “`@Composable` marks a function that participates in Compose’s composition and recomposition. It declares UI from state and lets the runtime re-execute it when that state changes—no manual view updates needed.”

### Minimal example

```kotlin
@Composable
fun Greeting(name: String) {
    Text(text = "Hello, $name!")
}
```

***

## 🔒 Can we call a composable from a non-composable?

**Directly? No.**  
You **cannot call** a composable from a regular function or scope because a composable must run **inside a composition** managed by the Compose runtime.

**How to do it correctly:**  
You need to **enter a composition** via one of the host entry points or interop APIs and then call your composable **inside** that scope.

### ✅ Correct ways to “invoke” composables from non-composable code

1.  **From an Activity (Compose host)**

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {                      // <-- Enter composition
            MyAppTheme {
                Greeting("Harshal")       // <-- Call composable here
            }
        }
    }
}
```

2.  **From a Fragment/View (interop using `ComposeView`)**

```kotlin
class MyFragment : Fragment(R.layout.fragment_host) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val composeView = view.findViewById<ComposeView>(R.id.compose_view)
        composeView.setContent {          // <-- Enter composition
            Greeting("From Fragment")
        }
    }
}
```

3.  **Compose inside XML (ComposeView in layout)**  
    `fragment_host.xml`

```xml
<androidx.compose.ui.platform.ComposeView
    android:id="@+id/compose_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

4.  **Compose in legacy View trees (`AbstractComposeView`)**  
    Create a reusable View class that hosts a composable:

```kotlin
class GreetingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AbstractComposeView(context, attrs) {

    var name: String by mutableStateOf("World")

    @Composable
    override fun Content() {
        Greeting(name)
    }
}
```

Use this `GreetingView` like a normal View (XML or programmatically).

***

## 🔁 “The other direction”: Can a composable call non-Compose (Views)?

**Yes**—via **Android interop**:

*   **`AndroidView`** to host a traditional View **inside** Compose:

```kotlin
@Composable
fun MapContainer() {
    AndroidView(
        factory = { context -> MapView(context).apply { onCreate(null) } },
        update = { mapView -> /* update props/state */ }
    )
}
```

*   **`AndroidViewBinding`** to inflate and use a **ViewBinding** layout:

```kotlin
@Composable
fun ProfileCard() {
    AndroidViewBinding(ProfileCardBinding::inflate) {
        nameText.text = "Harshal"
        roleText.text = "Manager"
    }
}
```

***

## 📌 Important rules and best practices about `@Composable`

1.  **Composables must be side-effect free** (as much as possible).  
    Use **side-effect APIs** for effects tied to lifecycle:

    *   `LaunchedEffect(key)` for coroutines
    *   `DisposableEffect(key)` for setup/cleanup
    *   `SideEffect`, `DerivedStateOf`, `rememberUpdatedState`, etc.

    ```kotlin
    @Composable
    fun UserLoader(userId: String) {
        var user by remember { mutableStateOf<User?>(null) }
        LaunchedEffect(userId) {
            user = repository.load(userId) // suspend call safely launched
        }
        Text(user?.name ?: "Loading…")
    }
    ```

2.  **Don’t block or do long work** in a composable.  
    Use `LaunchedEffect` + `rememberCoroutineScope()` for async work.

3.  **State should be hoisted** (pass state down, events up).  
    Keep composables reusable and testable.

4.  **`remember` vs `rememberSaveable`**

    *   `remember` survives **recomposition**
    *   `rememberSaveable` also survives **configuration changes/process death** (via `SavedStateHandle`/`Bundle`)

    ```kotlin
    val counter = rememberSaveable { mutableStateOf(0) }
    ```

5.  **You can’t combine `suspend` and `@Composable`** in the same function signature.  
    Launch suspend work from a composable using `LaunchedEffect` or a scope.

6.  **Use stable parameters and keys** for performance (especially lists):
    ```kotlin
    LazyColumn {
        items(items = users, key = { it.id }) { user ->
            UserRow(user)
        }
    }
    ```

***

## ❓FAQ-style clarifications you can say in the interview

*   **Q:** “Why can’t I call a composable from a regular function?”  
    **A:** Because composables require the **Compose runtime and slot table context** to record and manage UI during composition/recomposition. That context only exists **inside** a composition (e.g., `setContent { ... }`).

*   **Q:** “So how do I trigger UI changes from non-UI code?”  
    **A:** Update **state** (e.g., via ViewModel `StateFlow`/`LiveData`). The composable **observes** that state (`collectAsState()` / `observeAsState()`) and recomposes automatically.

    ```kotlin
    @Composable
    fun UsersScreen(vm: UsersViewModel = viewModel()) {
        val users by vm.usersFlow.collectAsState(initial = emptyList())
        UsersList(users)
    }
    ```

*   **Q:** “Can I navigate or show dialogs from a non-composable?”  
    **A:** Trigger **state/events** in your ViewModel; composables react and show dialogs or navigate.

***

## 🧠 Interview-ready summary

> “`@Composable` marks a function that participates in the Compose runtime. It describes UI from state and can be safely re-executed during recomposition. You **can’t** call a composable from non-composable code directly because it must run **inside a composition**. Instead, you enter a composition via `setContent`/`ComposeView` and call your composables there. For interop, use `AndroidView`/`AndroidViewBinding` to host Views inside Compose, and pass observable state from ViewModels to drive recomposition.”

***

  </details>

  <details style="margin-left: 20px;">
    <summary><i>📄 4. What is Recomposition? When does it happen?</i></summary>

***

# ✅ What is Recomposition?

## ✅ Interview‑Strength Definition

> **Recomposition** is the process by which **Jetpack Compose re‑executes composable functions whose observed state has changed**, compares the new UI output with the previous composition, and **updates only the affected parts of the UI**.

✔ Not a full redraw  
✔ Not recreation of entire UI  
✔ Smart, incremental update

***

## 🧠 Key Mental Model

> **UI = f(state)**  
> When *state changes*, Compose **re-runs only the functions that depend on that state**.

***

## 🔁 Composition vs Recomposition (Quick Contrast)

| Phase             | When          | What happens                         |
| ----------------- | ------------- | ------------------------------------ |
| **Composition**   | First time    | Compose builds UI & Slot Table       |
| **Recomposition** | State changes | Compose re‑runs affected composables |

🎯 Interview phrase:

> “Composition builds the UI; recomposition updates it.”

***

# ✅ When Does Recomposition Happen?

Recomposition happens **ONLY when Compose detects a change in state that a composable has read**.

Let’s break this down clearly.

***

## 1️⃣ When `State<T>` Changes (Most Common)

### Example

```kotlin
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }

    Text("Count: $count")

    Button(onClick = { count++ }) {
        Text("Increment")
    }
}
```

### What triggers recomposition?

*   `count` is **read** by `Text`
*   `count++` updates state
*   Compose **marks Text as dirty**
*   Recomposition happens

✅ Only `Text` is recomposed  
✅ `Button` is skipped if unaffected

***

## 2️⃣ When Observed Flow / LiveData Emits a New Value

Compose automatically recomposes when observable streams change.

### Example (StateFlow)

```kotlin
@Composable
fun UserScreen(vm: UserViewModel) {
    val user by vm.userState.collectAsState()

    Text(text = user.name)
}
```

### Recomposition triggers:

*   `userState` emits new `user`
*   `Text` reads updated value
*   UI recomposes

✅ This is why Compose works perfectly with **MVVM + ViewModel**

***

## 3️⃣ When a Composable Parameter Changes

### Example

```kotlin
@Composable
fun Greeting(name: String) {
    Text("Hello $name")
}
```

```kotlin
Greeting(name = userName)
```

If `userName` changes →

*   Greeting recomposes
*   Text updates

✅ Parameter changes are **tracked automatically**

***

## 4️⃣ When `remember` State Changes

```kotlin
val isExpanded = remember { mutableStateOf(false) }
```

Changes to `isExpanded.value` trigger recomposition for:

*   Composables that **read** it

***

## 5️⃣ When Keys of Composables Change

This is important for **lists** and **conditional UI**.

### Example

```kotlin
LazyColumn {
    items(items, key = { it.id }) {
        Text(it.name)
    }
}
```

If:

*   The **key changes**
*   Or item order changes

Compose recomposes affected items

✅ Proper keys → safer recomposition  
❌ Missing keys → incorrect UI reuse

***

# ❌ When Recomposition Does NOT Happen

This is equally important in interviews.

### ❌ State changes but NOT read

```kotlin
val state = remember { mutableStateOf(0) }
// If state.value changes but composable does not read it → no recomposition
```

### ❌ Local variable changes

```kotlin
var x = 10
x++ // ❌ Compose does not track this
```

✅ Only **Compose-aware state** triggers recomposition

***

# 🔍 What Exactly Re-runs During Recomposition?

### Important clarification:

> **Composable functions are re‑executed, not UI nodes recreated**

✅ Compose:

*   Re-runs the function body
*   Uses Slot Table
*   Reuses existing UI where possible

***

## 🧪 Example: Partial Recomposition

```kotlin
@Composable
fun Screen() {
    var name by remember { mutableStateOf("Harshal") }

    Header()
    Text("Name: $name")
    Footer()
}
```

### When `name` changes:

*   ✅ `Text` recomposes
*   ❌ `Header()` skipped
*   ❌ `Footer()` skipped

🎯 Interview gold line:

> “Compose recomposes at function granularity, not screen level.”

***

# 🚀 Skipping Optimization (Compose Performance)

Compose automatically **skips recomposition** if:

*   Parameters are unchanged
*   State hasn’t changed

### Example

```kotlin
@Composable
fun Title(text: String) {
    Text(text)
}
```

If `text` unchanged → Title is skipped

✅ This makes Compose highly performant

***

# ⚠️ Common Recomposition Pitfalls (Interview-Worthy)

### ❌ Creating state incorrectly

```kotlin
@Composable
fun BadExample() {
    val count = mutableStateOf(0) // ❌ created on every recomposition
}
```

### ✅ Correct

```kotlin
val count = remember { mutableStateOf(0) }
```

***

### ❌ Passing unstable lambdas / objects

```kotlin
MyComposable(onClick = { println("Hi") }) // recreated every time
```

✅ Fix using `remember`

***

# 💡 Interview‑Ready Summary (Say This)

> “Recomposition in Jetpack Compose is the process by which composable functions are re‑executed when the state they read changes. It happens when Compose‑observed state like `mutableStateOf`, `LiveData`, or `StateFlow` updates, or when composable parameters change. Compose uses recomposition and smart skipping to update only the affected parts of the UI instead of redrawing everything, making it efficient by design.”

***

## ✅ One‑Line Final Punch (Memorable)

> *“State changes trigger recomposition; recomposition re‑runs only the affected composables.”*

***

  </details>

  <details style="margin-left: 20px;">
    <summary><i>📄 5. How do you avoid unnecessary recompositions?</i></summary>

***

# ✅ How do you avoid unnecessary recompositions in Jetpack Compose?

## ✅ High‑Impact Interview Answer (Start With This)

> To avoid unnecessary recompositions in Jetpack Compose, we need to **manage state correctly**, **use stable parameters**, **avoid recreating objects**, **hoist state properly**, and **let Compose skip recomposition** when inputs haven’t changed. The goal is to ensure that only the composables that depend on changed state are recomposed.

***

## 🧠 Golden Rule to Remember

> **Only composables that *read changed state* should recompose.**

Everything else is noise.

***

# ✅ 1. Hoist State to the Lowest Required Level

### ❌ Problem: State high in the tree

```kotlin
@Composable
fun Screen() {
    var count by remember { mutableStateOf(0) }

    Header()
    Content(count)
    Footer()
}
```

Every time `count` changes → `Screen` recomposes → risk of propagating recomposition.

***

### ✅ Solution: Hoist state down

```kotlin
@Composable
fun Screen() {
    Header()
    Content()
    Footer()
}

@Composable
fun Content() {
    var count by remember { mutableStateOf(0) }
    Text("Count: $count")
}
```

✅ Only `Content` recomposes  
✅ Header and Footer are skipped

🎯 Interview line:

> “State should be hoisted to the lowest common ancestor that needs it.”

***

# ✅ 2. Use `remember` to Avoid Recreating Objects

### ❌ Problem

```kotlin
@Composable
fun BadExample() {
    val list = mutableListOf("A", "B", "C") // recreated every recomposition
}
```

### ✅ Solution

```kotlin
@Composable
fun GoodExample() {
    val list = remember {
        mutableListOf("A", "B", "C")
    }
}
```

✅ Prevents object recreation  
✅ Reduces recomposition cost

***

# ✅ 3. Pass Stable Objects and Parameters

Compose treats **unstable parameters** as changed—even if values are the same.

### ❌ Problem: Unstable parameter

```kotlin
@Composable
fun Parent() {
    Child(user = User("Harshal")) // new object each time
}
```

### ✅ Solution: Remember or use immutable models

```kotlin
val user = remember { User("Harshal") }
Child(user)
```

Or make data classes immutable (recommended).

🎯 Interview phrase:

> “Stable and immutable parameters help Compose safely skip recompositions.”

***

# ✅ 4. Avoid Creating Lambdas During Recomposition

### ❌ Problem

```kotlin
Button(onClick = { viewModel.loadData() })
```

Every recomposition → new lambda → potential recomposition.

***

### ✅ Solution

```kotlin
val onClick = remember {
    { viewModel.loadData() }
}

Button(onClick = onClick)
```

✅ Lambda is stable  
✅ Avoids unnecessary recomposition

***

# ✅ 5. Use `derivedStateOf` for Expensive Calculations

### ❌ Problem

```kotlin
val filteredList = items.filter { it.isActive }
```

Recomputed on every recomposition.

### ✅ Solution

```kotlin
val filteredList by remember(items) {
    derivedStateOf {
        items.filter { it.isActive }
    }
}
```

✅ Re-calculated only when `items` changes  
✅ Improves performance

***

# ✅ 6. Use `key()` and `LazyColumn` Keys Properly

### ❌ Problem

```kotlin
LazyColumn {
    items(items) {
        Text(it.name)
    }
}
```

Compose may:

*   Reuse wrong slots
*   Trigger unnecessary recomposition

***

### ✅ Solution

```kotlin
LazyColumn {
    items(
        items = items,
        key = { it.id }
    ) {
        Text(it.name)
    }
}
```

✅ Stable identity  
✅ Safer recomposition  
✅ Correct UI reuse

***

# ✅ 7. Don’t Read State at Higher Levels Than Needed

### ❌ Problem

```kotlin
@Composable
fun Screen() {
    val user by viewModel.user.collectAsState()

    Header(user.name)
    Content()
    Footer()
}
```

Every user change → whole Screen recomposes.

***

### ✅ Solution

```kotlin
@Composable
fun Screen() {
    Header()
    Content()
}

@Composable
fun Content(vm: VM) {
    val user by vm.user.collectAsState()
    Text(user.name)
}
```

✅ Limits recomposition scope

***

# ✅ 8. Prefer Immutable Collections & Models

### ❌ Problem

```kotlin
MutableList<User>
```

Compose can’t easily tell if it changed.

### ✅ Solution

```kotlin
List<User>
```

✅ Better change detection  
✅ Enables smart skipping

***

# ✅ 9. Use `@Stable` and `@Immutable` When Needed (Advanced)

For custom objects:

```kotlin
@Immutable
data class User(val id: Int, val name: String)
```

Or:

```kotlin
@Stable
class UiState { ... }
```

✅ Helps Compose make recomposition decisions correctly

⚠️ Use carefully—don’t lie to Compose.

***

# ✅ 10. Don’t Trigger Side Effects in Composables

### ❌ Problem

```kotlin
@Composable
fun Screen() {
    viewModel.loadData() // called on every recomposition
}
```

### ✅ Solution

```kotlin
LaunchedEffect(Unit) {
    viewModel.loadData()
}
```

✅ Runs once  
✅ No recomposition loop  
✅ Correct lifecycle handling

***

# ✅ 11. Trust Compose’s Skipping – Don’t Over‑Optimize

Compose already:

*   Skips unchanged composables
*   Avoids redundant work
*   Uses Slot Table efficiently

🎯 Interview wisdom:

> “Measure before optimizing—most recompositions are cheap.”

Use:

*   **Layout Inspector**
*   **Compose Recomposition Counts**
*   **Macrobenchmark**

***

# ✅ Quick Interview Checklist (Memorize This)

✅ Hoist state properly  
✅ Use `remember`  
✅ Pass stable parameters  
✅ Avoid recreating lambdas  
✅ Use `derivedStateOf`  
✅ Use keys in lazy lists  
✅ Observe state only where needed  
✅ Use immutable data models  
✅ Move side effects to `LaunchedEffect`

***

# 🔥 Interview‑Ready Final Answer (Polished)

> “To avoid unnecessary recompositions in Jetpack Compose, we should limit the scope of state, hoist it appropriately, and ensure composables only read the state they need. We should use `remember` to avoid recreating objects and lambdas, pass stable and immutable parameters, use keys for lists, and rely on `derivedStateOf` for expensive calculations. Compose already skips unchanged composables, so the real optimization is designing state and composable boundaries correctly.”

***

  </details>

</details>

<details>
  <summary><b>📂 State Management</b></summary>

  <details style="margin-left: 20px;">
    <summary><i>📄 1. Difference between remember, rememberSaveable, and mutableStateOf</i></summary>

***

# ✅ Difference between `mutableStateOf`, `remember`, and `rememberSaveable`

## ✅ One‑Line High‑Impact Summary (Use This First)

> `mutableStateOf` creates observable state, `remember` keeps that state across recompositions, and `rememberSaveable` keeps it across both recompositions **and configuration changes** like rotation.

***

## 🧠 First Understand the Big Picture (Very Important)

These three are **not alternatives**—they **work together**.

> 🔑 **State = value + lifecycle**

Compose needs to know:

1.  **What** state to track → `mutableStateOf`
2.  **How long** to keep it alive:
    *   Across recomposition → `remember`
    *   Across rotation/process recreation → `rememberSaveable`

***

# ✅ 1️⃣ `mutableStateOf`

## 🔹 What is `mutableStateOf`?

> `mutableStateOf` creates an **observable state holder** that Compose tracks for changes.

```kotlin
val count = mutableStateOf(0)
```

✅ When `count.value` changes → recomposition happens  
❌ But **by itself**, it does **NOT** survive recomposition properly

***

### ❌ Problem if used alone

```kotlin
@Composable
fun Counter() {
    val count = mutableStateOf(0) // ❌ recreated on every recomposition

    Button(onClick = { count.value++ }) {
        Text("Count: ${count.value}")
    }
}
```

🚨 Every recomposition resets `count` to `0`

🎯 Interview line:

> “`mutableStateOf` tracks changes, but does not manage lifecycle by itself.”

***

## ✅ When to use `mutableStateOf`?

*   Inside **ViewModel**
*   With `remember` or `rememberSaveable`
*   For any UI‑observable value

```kotlin
var count by mutableStateOf(0)
```

***

# ✅ 2️⃣ `remember`

## 🔹 What is `remember`?

> `remember` stores an object in the **composition memory** so it survives **recompositions**.

```kotlin
val count = remember { mutableStateOf(0) }
```

✅ State survives recomposition  
❌ State is **lost on configuration change** (rotation)

***

### ✅ Correct usage example

```kotlin
@Composable
fun Counter() {
    val count = remember { mutableStateOf(0) }

    Button(onClick = { count.value++ }) {
        Text("Count: ${count.value}")
    }
}
```

✅ Click button → recomposition → value preserved  
❌ Rotate screen → value resets

***

### ✅ Kotlin‑idiomatic version

```kotlin
var count by remember { mutableStateOf(0) }
```

🎯 Interview line:

> “`remember` keeps state in memory across recompositions but not across activity recreation.”

***

## ✅ When to use `remember`?

*   Temporary UI state
*   Animation state
*   Expand/collapse flags
*   State that doesn’t need to survive rotation

***

# ✅ 3️⃣ `rememberSaveable`

## 🔹 What is `rememberSaveable`?

> `rememberSaveable` works like `remember` **but also saves state** using `SavedStateRegistry`, so it survives **configuration changes and process death**.

```kotlin
var count by rememberSaveable { mutableStateOf(0) }
```

✅ Survives recomposition  
✅ Survives rotation  
✅ Survives process recreation (if system kills app)

***

### ✅ Example

```kotlin
@Composable
fun Counter() {
    var count by rememberSaveable { mutableStateOf(0) }

    Button(onClick = { count++ }) {
        Text("Count: $count")
    }
}
```

Rotate device → ✅ count preserved

***

## 🧠 What data types does `rememberSaveable` support?

By default:

*   Primitive types (`Int`, `Boolean`, `String`)
*   `Parcelable`
*   `Serializable`

### ❌ Custom object?

You must provide a **Saver**

```kotlin
val userSaver = Saver<User, String>(
    save = { it.id },
    restore = { id -> User(id) }
)

val user = rememberSaveable(stateSaver = userSaver) {
    User("123")
}
```

🎯 Interview line:

> “`rememberSaveable` uses Bundle under the hood, so data must be saveable.”

***

# ✅ Comparison Table (Interview Gold)

| Feature                | `mutableStateOf` | `remember`    | `rememberSaveable` |
| ---------------------- | ---------------- | ------------- | ------------------ |
| Tracks state changes   | ✅                | ✅             | ✅                  |
| Survives recomposition | ❌                | ✅             | ✅                  |
| Survives rotation      | ❌                | ❌             | ✅                  |
| Survives process death | ❌                | ❌             | ✅                  |
| Uses Bundle            | ❌                | ❌             | ✅                  |
| Common use             | ViewModel state  | UI temp state | UI important state |

***

# ✅ Typical Usage Patterns (VERY Important)

## ✅ Pattern 1: UI state in Composable

```kotlin
var expanded by remember { mutableStateOf(false) }
```

## ✅ Pattern 2: UI state that should survive rotation

```kotlin
var text by rememberSaveable { mutableStateOf("") }
```

## ✅ Pattern 3: Business state in ViewModel (BEST PRACTICE)

```kotlin
class MyViewModel : ViewModel() {
    var count by mutableStateOf(0)
        private set
}
```

```kotlin
@Composable
fun Screen(vm: MyViewModel = viewModel()) {
    Text("Count: ${vm.count}")
}
```

✅ Survives rotation automatically  
✅ Recommended for most real apps

***

# ❌ Common Interview Pitfalls (Mention These)

### ❌ Using `rememberSaveable` for large objects

*   Bundle size limit
*   Performance issues

### ❌ Storing business data in composables

*   Lost on process death
*   Harder to test

🎯 Interview wisdom:

> “Use composables for UI state, ViewModel for business state.”

***

# ✅ How to Explain This Visually (Nice Tip)

Think of lifetimes:

```text
Recomposition  →  remember  →  rememberSaveable  →  ViewModel
(short)           ↑              ↑                   (long)
```

***

# 🔥 Final Interview‑Ready Answer (Polished)

> “`mutableStateOf` creates observable state that triggers recomposition when changed. `remember` retains that state across recompositions but not configuration changes. `rememberSaveable` extends `remember` by saving state in the SavedStateRegistry, allowing it to survive rotation and process recreation. In practice, we combine `remember` or `rememberSaveable` with `mutableStateOf`, and for longer‑lived or business state we prefer ViewModel.”

***

## ✅ One‑Line Closing (Very Strong)

> *“`mutableStateOf` tracks state, `remember` defines its lifetime, and `rememberSaveable` persists it safely.”*

***

  </details>

  <details style="margin-left: 20px;">
    <summary><i>📄 2. How do you manage state in Compose with ViewModel?</i></summary>

***

## ✅ Goal: Unidirectional Data Flow (UDF)

**ViewModel** is the **single source of truth** for screen state.  
The flow is:

    ViewModel (state) ───> UI (renders)
    UI (events) ────────> ViewModel (handles, updates state)

*   UI **observes** state exposed by the ViewModel (`StateFlow`, `LiveData`, or Compose `SnapshotState`).
*   UI emits **events/intents** to ViewModel (button clicks, text change).
*   ViewModel updates the state; Compose **recomposes** automatically.

***

## ✅ Recommended Building Blocks

*   **`ViewModel`**: owns state; survives configuration changes.
*   **`StateFlow`/`Flow`** (preferred) or **`LiveData`**: expose observable state.
*   **`collectAsStateWithLifecycle()`**: lifecycle-aware collection in Compose.
*   **`SavedStateHandle`**: persist important values across process death.
*   **Hilt**: inject dependencies and get `hiltViewModel()` in Composables.
*   **UI Model**: immutable `UiState` (data class) + `UiEvent` + `UiEffect` (one-off).

***

## 🧩 Minimal Example (MVVM + Compose + Flow)

### 1) Define the UI state

```kotlin
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val loggedIn: Boolean = false
)
```

### 2) ViewModel (StateFlow + Single events)

```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    // One-off effects: navigation, toasts, snackbars
    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect: SharedFlow<LoginEffect> = _effect

    fun onEmailChange(value: String) =
        update { it.copy(email = value, error = null) }

    fun onPasswordChange(value: String) =
        update { it.copy(password = value, error = null) }

    fun onLoginClick() {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            update { it.copy(error = "Email/Password required") }
            return
        }
        viewModelScope.launch {
            update { it.copy(loading = true, error = null) }
            runCatching { repo.login(state.email, state.password) }
                .onSuccess {
                    update { it.copy(loading = false, loggedIn = true) }
                    _effect.emit(LoginEffect.NavigateHome)
                }
                .onFailure { e ->
                    update { it.copy(loading = false, error = e.message ?: "Login failed") }
                }
        }
    }

    private inline fun update(block: (LoginUiState) -> LoginUiState) {
        _uiState.update(block)
    }
}

sealed interface LoginEffect {
    data object NavigateHome : LoginEffect
}
```

### 3) Compose Screen (lifecycle-aware collection)

```kotlin
@Composable
fun LoginScreen(
    vm: LoginViewModel = hiltViewModel(),
    onNavigateHome: () -> Unit
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    // Handle one-off effects
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        vm.effect.collect { effect ->
            when (effect) {
                LoginEffect.NavigateHome -> onNavigateHome()
            }
        }
    }

    LoginContent(
        email = uiState.email,
        password = uiState.password,
        loading = uiState.loading,
        error = uiState.error,
        onEmailChange = vm::onEmailChange,
        onPasswordChange = vm::onPasswordChange,
        onLoginClick = vm::onLoginClick
    )
}
```

### 4) Stateless UI (Pure Composables)

```kotlin
@Composable
fun LoginContent(
    email: String,
    password: String,
    loading: Boolean,
    error: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    Column(Modifier.padding(16.dp)) {
        if (error != null) {
            Text(error, color = Color.Red)
        }
        OutlinedTextField(value = email, onValueChange = onEmailChange, label = { Text("Email") })
        OutlinedTextField(value = password, onValueChange = onPasswordChange, label = { Text("Password") })
        Button(onClick = onLoginClick, enabled = !loading) {
            if (loading) CircularProgressIndicator(Modifier.size(16.dp)) else Text("Login")
        }
    }
}
```

**Why this is good in interviews:**

*   Clear **state hoisting**.
*   **Lifecycle-aware** collection.
*   Proper **one-off effect** handling (navigation).
*   **Stateless UI** + **stateful ViewModel** separation.

***

## 🧭 Patterns & Best Practices (Talk through these)

1.  **Expose immutable state**
    ```kotlin
    val uiState: StateFlow<UiState>
    // not MutableStateFlow from outside
    ```

2.  **Immutable `UiState` data classes**  
    Makes skipping and diffing predictable and efficient.

3.  **Prefer `StateFlow` over `LiveData`**  
    Modern, Kotlin-first, cancellation-aware.  
    If you must use LiveData:
    ```kotlin
    val state by liveData.observeAsState()
    ```

4.  **Use `collectAsStateWithLifecycle()`**  
    Avoids leaks and wasted work when the composable is not visible.

5.  **Handle one-off events separately**
    *   Navigation, snackbars, toasts → `SharedFlow`/`Channel`.
    *   Don’t put them in `UiState` to avoid replay on recomposition.

6.  **Avoid business logic in Composables**  
    Use ViewModel for async work, repositories, validation.

7.  **Use `SavedStateHandle` for critical state**  
    Works across process death (e.g., form progress, IDs).
    ```kotlin
    class VM(savedState: SavedStateHandle) : ViewModel() {
        var text by savedState.getStateFlow("text", "").stateIn(...)
    }
    ```

8.  **Threading with `viewModelScope`**  
    Launch coroutines in ViewModel; UI stays purely declarative.

9.  **Navigation + ViewModel scoping**  
    With Navigation-Compose and Hilt:
    ```kotlin
    val vm: MyViewModel = hiltViewModel()
    ```
    ViewModel is scoped to the nav back stack entry.

10. **Stability & recomposition**  
    Keep parameters stable; avoid recreating lambdas/objects in composables.  
    For lists, use `key = { it.id }`.

***

## 🔄 ViewModel + Compose + remember/rememberSaveable

*   **Prefer ViewModel** for screen/business state.
*   Use **`remember`** for **UI-only ephemeral** flags (e.g., local expansion).
*   Use **`rememberSaveable`** for ephemeral UI that must survive rotation (e.g., text field value) **if not already in ViewModel**.

```kotlin
@Composable
fun SearchBar(vm: SearchViewModel = hiltViewModel()) {
    // Ephemeral local UI (focus state)
    var isFocused by remember { mutableStateOf(false) }

    // Search query is business/UI state → ViewModel
    val query by vm.query.collectAsStateWithLifecycle()

    TextField(
        value = query,
        onValueChange = vm::onQueryChange,
        modifier = Modifier.onFocusChanged { isFocused = it.isFocused }
    )
}
```

***

## ⚠️ Common Pitfalls (call these out)

*   ❌ Collecting flows without lifecycle (`collectAsState()` without the `-WithLifecycle` in screens that move in/out)
*   ❌ Emitting navigation/snackbar as part of `UiState` (it can replay on recomposition)
*   ❌ Keeping large/critical state only in `rememberSaveable` (Bundle limits). Use ViewModel.
*   ❌ Doing work directly in composables (e.g., `repo.load()` in body). Use `LaunchedEffect` or ViewModel.

***

## 🧪 Unit/UI Testing Tips (Nice bonus to mention)

*   **ViewModel**: test with `Turbine` on `StateFlow`/`SharedFlow`.
*   **Compose**: pass fake `UiState` into **stateless** composables and assert semantics.
*   **Navigation/effects**: assert via collected effects in tests rather than inspecting `UiState`.

***

## 💬 Interview-Ready Summary

> “I manage state in Compose by making the ViewModel the single source of truth. The ViewModel exposes an immutable `UiState` via `StateFlow`, which the composable collects using `collectAsStateWithLifecycle()`. The UI remains stateless and sends user events back to the ViewModel. One-off events like navigation are modeled as a `SharedFlow` of effects. I use `SavedStateHandle` for persistence across process death and keep only ephemeral visual state in `remember`/`rememberSaveable`. This keeps unidirectional data flow, lifecycle safety, and minimal recompositions.”

***

  </details>

  <details style="margin-left: 20px;">
    <summary><i>📄 3. How does Compose handle configuration changes?</i></summary>

***

# ✅ How does Jetpack Compose handle configuration changes?

## ✅ Strong Interview Opening (Say This First)

> Jetpack Compose itself does not automatically retain UI state across configuration changes. Instead, it relies on **ViewModel**, **rememberSaveable**, and the **SavedStateRegistry** to preserve state when the system recreates the Activity during configuration changes like screen rotation.

That immediately shows maturity.

***

## 1️⃣ What is a Configuration Change? (Quick Context)

Configuration changes include:

*   Screen rotation
*   Locale change
*   Font size change
*   Dark/Light mode change
*   Keyboard availability

📌 **Important**:  
On configuration change:

*   **Activity is destroyed**
*   **Activity is recreated**
*   Composition is **rebuilt from scratch**

<!---->

    Old Activity → Destroyed
    New Activity → Created → setContent { ... }

***

## 2️⃣ What Happens to Compose on Configuration Change?

### 🔹 High‑level behavior

*   Entire **composition is disposed**
*   All `@Composable` functions are **re-executed**
*   UI is rebuilt **declaratively**
*   Old composition memory is **lost**

✅ This is expected and intentional.

🎯 Interview line:

> “Compose embraces Activity recreation and relies on state holders rather than preventing configuration changes.”

***

## 3️⃣ How Different State Types Behave (MOST IMPORTANT PART)

### 🎯 This table is interview gold—you can verbalize it.

| State Holder                   | Survives Recomposition | Survives Configuration Change | Why                          |
| ------------------------------ | ---------------------- | ----------------------------- | ---------------------------- |
| `mutableStateOf` alone         | ❌                      | ❌                             | Recreated every time         |
| `remember`                     | ✅                      | ❌                             | Stored in composition memory |
| `rememberSaveable`             | ✅                      | ✅                             | Saved via SavedStateRegistry |
| `ViewModel`                    | ✅                      | ✅                             | Lifecycle-aware, retained    |
| `ViewModel + SavedStateHandle` | ✅                      | ✅ + process death             | Stored in Bundle             |

***

## 4️⃣ `remember` vs Configuration Change

```kotlin
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }
    Button(onClick = { count++ }) {
        Text("Count: $count")
    }
}
```

### Behavior:

*   ✅ Survives recomposition
*   ❌ Resets to 0 on rotation

🎯 Interview line:

> “`remember` only survives recomposition, not Activity recreation.”

***

## 5️⃣ `rememberSaveable` – How Compose Handles It

```kotlin
@Composable
fun Counter() {
    var count by rememberSaveable { mutableStateOf(0) }
    Button(onClick = { count++ }) {
        Text("Count: $count")
    }
}
```

### Internally:

*   Compose saves `count` into a **Bundle**
*   Uses **SavedStateRegistry**
*   Restores value when Activity is recreated

✅ Survives rotation  
✅ Survives process death (if Bundle restored)

🎯 Interview line:

> “`rememberSaveable` integrates with SavedStateRegistry, similar to `onSaveInstanceState`.”

***

## 6️⃣ ViewModel – Recommended Way (BEST PRACTICE)

```kotlin
class CounterViewModel : ViewModel() {
    var count by mutableStateOf(0)
        private set

    fun increment() {
        count++
    }
}
```

```kotlin
@Composable
fun CounterScreen(vm: CounterViewModel = viewModel()) {
    Button(onClick = vm::increment) {
        Text("Count: ${vm.count}")
    }
}
```

### Why this works:

*   ViewModel **survives configuration change**
*   Compose automatically observes `mutableStateOf`
*   UI recomposes when state changes

🎯 Interview golden sentence:

> “ViewModel is the preferred solution for configuration changes in Compose.”

***

## 7️⃣ Compose + Navigation = ViewModel Scoping

With **Navigation‑Compose**, ViewModels are scoped to:

*   NavGraph
*   Destination
*   Back stack entry

```kotlin
val vm: MyViewModel = hiltViewModel()
```

✅ Survives rotation  
✅ Cleared only when destination is removed

This is very impressive to mention.

***

## 8️⃣ SavedStateHandle for Process Death (Advanced)

For critical state (e.g., form progress):

```kotlin
class FormViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    var name by savedStateHandle
        .getStateFlow("name", "")
        private set
}
```

✅ Survives:

*   Rotation
*   Process death
*   App being killed in background

🎯 Interview phrase:

> “SavedStateHandle complements ViewModel for full state restoration.”

***

## 9️⃣ When to Use What? (Practical Rule)

✅ Use **remember**

*   Temporary UI state (expand/collapse, animation)

✅ Use **rememberSaveable**

*   UI input state (TextField content)
*   When ViewModel is overkill

✅ Use **ViewModel**

*   Business logic
*   Network/UI state shared across screens

✅ Use **SavedStateHandle**

*   State that must survive process death

***

## 10️⃣ What Compose Does NOT Do (Clarify This)

❌ Compose does **not**:

*   Block configuration changes
*   Retain Activity
*   Automatically persist state

✅ It gives **clear tools** and **predictable behavior**

🎯 Nice interview statement:

> “Compose doesn’t hide configuration changes—it makes them explicit and manageable.”

***

## 🔥 Final Interview‑Ready Summary (Polished Answer)

> “On configuration changes, the Activity is recreated and Jetpack Compose rebuilds the entire composition. Any state stored with `remember` is lost, while `rememberSaveable` persists state through the SavedStateRegistry. The recommended approach is to keep screen and business state in a ViewModel, which survives configuration changes. For critical state that must also survive process death, we use ViewModel with SavedStateHandle. Compose handles configuration changes cleanly by relying on proper state ownership rather than retaining UI objects.”

***

## ✅ One‑Line Closing (Very Strong)

> *“Compose treats configuration changes as rebuilds, not problems—state holders define what survives.”*

***

  </details>

  <details style="margin-left: 20px;">
    <summary><i>📄 4. What is State Hoisting? Why is it important?</i></summary>

***

# ✅ What is State Hoisting?

## ✅ Interview‑Ready Definition (Start With This)

> **State hoisting** is a pattern in Jetpack Compose where **state is moved out of a composable and passed down as parameters**, while **events are passed up**, making the composable **stateless, reusable, and easier to test**.

In short:

> **State goes down, events go up.**

***

## 🧠 What Problem Does State Hoisting Solve?

Without state hoisting:

*   UI logic + state are tightly coupled
*   Composables are hard to reuse
*   Testing UI becomes difficult
*   Multiple sources of truth appear

Compose encourages **Unidirectional Data Flow (UDF)**, and state hoisting is how we achieve that.

***

# ✅ Before & After: Clear Example

## ❌ Without State Hoisting (Bad Practice)

```kotlin
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }

    Button(onClick = { count++ }) {
        Text("Count: $count")
    }
}
```

### Problems

*   `Counter` controls its own state
*   Cannot reuse with external state
*   Hard to test
*   Parent cannot control behavior

***

## ✅ With State Hoisting (Recommended)

### ✅ Stateless UI

```kotlin
@Composable
fun Counter(
    count: Int,
    onIncrement: () -> Unit
) {
    Button(onClick = onIncrement) {
        Text("Count: $count")
    }
}
```

### ✅ Stateful Parent

```kotlin
@Composable
fun CounterScreen() {
    var count by remember { mutableStateOf(0) }

    Counter(
        count = count,
        onIncrement = { count++ }
    )
}
```

🎯 Interview phrase:

> “The composable becomes stateless and fully controlled by its parent.”

***

# ✅ The “Single Source of Truth” Rule

### ❌ Bad (Two sources of truth)

```kotlin
@Composable
fun BadTextField(
    text: String,
    onTextChange: (String) -> Unit
) {
    var localText by remember { mutableStateOf(text) } // ❌
}
```

### ✅ Good (Hoisted state)

```kotlin
@Composable
fun GoodTextField(
    text: String,
    onTextChange: (String) -> Unit
) {
    TextField(
        value = text,
        onValueChange = onTextChange
    )
}
```

✅ One source of truth  
✅ Predictable state flow

***

# ✅ Why Is State Hoisting Important?

## 1️⃣ Enables Reusable & Stateless Composables

Stateless composables:

*   Are easier to reuse
*   Can be used in different screens
*   Work with any state source (remember, ViewModel, Flow)

```kotlin
Counter(count = 5, onIncrement = {})
Counter(count = viewModel.count, onIncrement = viewModel::increment)
```

🎯 Interview line:

> “Hoisted composables don’t care where the state comes from.”

***

## 2️⃣ Improves Recomposition Performance

When state is hoisted:

*   Only composables that **use the state** recompose
*   Parent and unrelated UI can be skipped

✅ Less unnecessary recompositions  
✅ Better performance

***

## 3️⃣ Enables Unidirectional Data Flow (UDF)

Flow becomes:

    ViewModel / Parent
            ↓ (state)
       Composable
            ↑ (events)

🎯 Interview sentence:

> “State hoisting enforces unidirectional data flow, making UI behavior predictable.”

***

## 4️⃣ Makes Composables Easier to Test

Stateless composables are **pure functions**.

```kotlin
@Composable
fun Profile(name: String) {
    Text(name)
}
```

✅ Just pass test data  
✅ No mocking state  
✅ Simple UI testing

***

## 5️⃣ Works Perfectly with ViewModel (Best Practice)

### ViewModel owns state

```kotlin
class CounterViewModel : ViewModel() {
    var count by mutableStateOf(0)
        private set

    fun increment() {
        count++
    }
}
```

### Compose observes state

```kotlin
@Composable
fun Screen(vm: CounterViewModel = viewModel()) {
    Counter(
        count = vm.count,
        onIncrement = vm::increment
    )
}
```

🎯 Interview‑level answer:

> “ViewModel becomes the single source of truth, Compose only renders state.”

***

# ✅ When NOT to Hoist State

Not everything needs hoisting.

✅ Keep state local when:

*   It’s temporary
*   It doesn’t affect other composables
*   It’s purely visual

Examples:

```kotlin
• Animation progress
• Ripple state
• IsPressed flag
• Expanded state of a dropdown used only there
```

🎯 Interview wisdom:

> “Hoist state only as high as necessary—but no higher.”

***

# ✅ Common State Hoisting Example: TextField

### ✅ Correct Pattern

```kotlin
@Composable
fun NameInput(
    name: String,
    onNameChange: (String) -> Unit
) {
    TextField(
        value = name,
        onValueChange = onNameChange
    )
}
```

```kotlin
@Composable
fun Screen(vm: VM) {
    val name by vm.name.collectAsStateWithLifecycle()

    NameInput(
        name = name,
        onNameChange = vm::onNameChange
    )
}
```

***

# ✅ Interview Comparison (Compose vs XML)

| XML View System      | Jetpack Compose             |
| -------------------- | --------------------------- |
| View owns state      | External state ownership    |
| Two-way data binding | Explicit UDF                |
| Hard to test         | Stateless UI = easy testing |

***

# 🔥 Interview‑Ready Final Summary (Say This)

> “State hoisting is the practice of moving state out of a composable and passing it down along with callbacks for state changes. It’s important because it enforces a single source of truth, enables unidirectional data flow, improves reusability and testability, and reduces unnecessary recompositions. In Compose, stateless composables with hoisted state are considered best practice.”

***

## ✅ One‑Line Closing (Very Strong)

> *“State hoisting separates what the UI looks like from who owns the state.”*

***

  </details>

  <details style="margin-left: 20px;">
    <summary><i>📄 5. How would you handle large UI state efficiently?</i></summary>

***

## 🎯 Principles for Large UI State

1.  **Single source of truth** (usually `ViewModel`)
2.  **Immutable, value‑type `UiState`** (data classes)
3.  **Slice state per sub‑tree** (pass only what’s needed)
4.  **Stability** (immutable models, stable params, keys)
5.  **Streams + lifecycle-aware collection** (`StateFlow` + `collectAsStateWithLifecycle`)
6.  **Avoid “giant blobs”** in `rememberSaveable`/`SavedStateHandle`—persist IDs/filters, not full lists.

***

## 1) Shape State for Performance (Immutable + Granular)

### ✅ Use an immutable `UiState` and **split** into smaller sub‑states

```kotlin
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class HeaderState(
    val title: String,
    val subtitle: String?
)

data class FiltersState(
    val query: String = "",
    val sort: Sort = Sort.Recent,
    val selectedTags: PersistentList<String> = persistentListOf()
)

data class ListState(
    val items: PersistentList<ItemUi> = persistentListOf(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class ScreenUiState(
    val header: HeaderState,
    val filters: FiltersState,
    val list: ListState
)
```

**Why:**

*   Immutable state → structural equality → Compose can **skip** recomposition if unchanged.
*   Smaller sub-states allow **leaf recomposition** only.

> Tip: Prefer **persistent immutable collections** (`kotlinx.collections.immutable`) to avoid copying huge lists on minor changes and to keep references stable.

***

## 2) Expose State with `StateFlow` and Update Granularly

```kotlin
@HiltViewModel
class ScreenViewModel @Inject constructor(
    private val repo: ItemsRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(
        ScreenUiState(
            header = HeaderState("Catalog", null),
            filters = FiltersState(),
            list = ListState()
        )
    )
    val ui: StateFlow<ScreenUiState> = _ui

    fun onQueryChanged(query: String) = _ui.update {
        it.copy(filters = it.filters.copy(query = query))
    }

    fun onSortChanged(sort: Sort) = _ui.update {
        it.copy(filters = it.filters.copy(sort = sort))
    }

    fun load() = viewModelScope.launch {
        _ui.update { it.copy(list = it.list.copy(isLoading = true, error = null)) }
        runCatching { repo.load(_ui.value.filters) }
            .onSuccess { items ->
                _ui.update { it.copy(list = it.list.copy(items = items.toPersistentList(), isLoading = false)) }
            }
            .onFailure { e ->
                _ui.update { it.copy(list = it.list.copy(isLoading = false, error = e.message)) }
            }
    }
}
```

**Why:**

*   `update` only changes the **sub-state** that changed.
*   Stable references for unchanged parts → **skips** recompositions of unaffected UI.

***

## 3) Collect Slices in the **Lowest** Composable That Needs Them

```kotlin
@Composable
fun Screen(vm: ScreenViewModel = hiltViewModel()) {
    val ui by vm.ui.collectAsStateWithLifecycle()

    Header(ui.header)                // only header reads header
    FiltersBar(
        state = ui.filters,
        onQueryChange = vm::onQueryChanged,
        onSortChange = vm::onSortChanged
    )
    ItemsList(ui.list)               // only list reads list
}
```

**Why:**  
Only `ItemsList` recomposes when `list` changes; `Header` and `FiltersBar` are **skipped**.

> Avoid collecting the entire `UiState` in a top-level and **passing everything** down if children only need slices—pass just the relevant sub-state.

***

## 4) Use `derivedStateOf` for Expensive Derivations

Compute expensive filtered/mapped views **only when inputs actually change**:

```kotlin
@Composable
fun ItemsList(state: ListState) {
    val activeItems by remember(state.items) {
        derivedStateOf { state.items.filter { it.active } }
    }
    LazyColumn {
        items(
            items = activeItems,
            key = { it.id },
            contentType = { "item" } // helps recycling/layout
        ) { item ->
            ItemRow(item)
        }
    }
}
```

**Why:**

*   Avoid re-filtering on every recomposition.
*   `key` and `contentType` improve `LazyColumn` performance and reuse.

***

## 5) Prefer Paging for Large Datasets

When lists are large, avoid holding thousands of rows in UI state. Use **Paging 3**:

```kotlin
@HiltViewModel
class FeedViewModel @Inject constructor(
    repo: FeedRepository
) : ViewModel() {
    val paging = repo.pager.flow.cachedIn(viewModelScope) // Flow<PagingData<ItemUi>>
}

@Composable
fun Feed(vm: FeedViewModel = hiltViewModel()) {
    val items = vm.paging.collectAsLazyPagingItems()
    LazyColumn {
        items(
            count = items.itemCount,
            key = items.itemKey { it.id },
            contentType = items.itemContentType { "feed" }
        ) { index ->
            items[index]?.let { ItemRow(it) }
        }
    }
}
```

**Why:**

*   Keeps memory bounded
*   Loads pages on demand
*   Integrates cleanly with Compose’s lazy components

***

## 6) Avoid Passing/Recreating Heavy Objects

*   **Remember** lambdas/formatters to avoid new instances each recomposition:
    ```kotlin
    val onClick = remember { { /* ... */ } }
    ```
*   **Map domain → UI models** at the ViewModel/repository boundary so the UI receives **lightweight, immutable** data.
*   Don’t put large bitmaps/byte arrays in `UiState`. Keep heavy caches in repositories (in-memory/disk) and pass only **IDs/URIs**.

***

## 7) Persist Only What’s Needed

*   Use `ViewModel` as the main owner.
*   For process death/rotation, store **small, serializable pieces** in `SavedStateHandle`: IDs, filters, query text—**not entire lists**.
*   For pure UI inputs (e.g., text fields) that aren’t ViewModel-critical, `rememberSaveable` is fine; avoid large payloads due to Bundle limits.

```kotlin
@HiltViewModel
class SearchVM @Inject constructor(
    private val saved: SavedStateHandle
) : ViewModel() {
    private val _query = saved.getStateFlow("q", "")
    val query: StateFlow<String> = _query
    fun setQuery(q: String) { saved["q"] = q }
}
```

***

## 8) Stabilize Parameters

*   Prefer **data classes** (structural equality).
*   Mark **truly immutable** types with `@Immutable`.
*   Use `@Stable` cautiously (only when you guarantee stable semantics).
*   For lists, use immutable persistent lists to **avoid new references** on small edits.

```kotlin
import androidx.compose.runtime.Immutable

@Immutable
data class ItemUi(val id: Long, val title: String, val active: Boolean)
```

***

## 9) Throttle/Distinct Changes to Reduce Recomposition

*   In `ViewModel`, apply `distinctUntilChanged()` / `distinctUntilChangedBy { it.subState }` on flows to prevent noisy updates.
*   In Compose, use `snapshotFlow { text }` with debounce for high-frequency inputs.

```kotlin
@Composable
fun QueryBox(vm: SearchVM = hiltViewModel()) {
    var local by remember { mutableStateOf("") }

    // debounce/high frequency input
    LaunchedEffect(Unit) {
        snapshotFlow { local }
            .distinctUntilChanged()
            .debounce(300)
            .collect { vm.setQuery(it) }
    }

    TextField(value = local, onValueChange = { local = it })
}
```

***

## 10) Split Very Large Screens into Feature Modules

*   Break complex UI into **subcomposables** that take **minimal state slices**.
*   Co-locate small `ViewModel`s per feature when boundaries are clear (e.g., bottom sheet with its own VM).

***

## 11) Measurement & Tooling

*   **Recomposition Counts**: enable tooling to see which composables are recomposing.
*   **Layout Inspector**: inspect the composition and recomposition behavior.
*   **Macrobenchmark** for scroll/jank metrics.

> Optimization without measurement is guesswork—always confirm with tools.

***

## Putting It All Together (Mini Example)

```kotlin
@Composable
fun ProductScreen(vm: ProductVM = hiltViewModel()) {
    val ui by vm.ui.collectAsStateWithLifecycle()

    ProductHeader(ui.header)
    ProductFilters(
        state = ui.filters,
        onQueryChange = vm::onQueryChanged,
        onSortChange = vm::onSortChanged
    )
    ProductList(ui.list) // Uses derivedStateOf + keys + contentType + Paging when large
}
```

This approach:

*   Keeps **large state** in a **stable, immutable** structure
*   **Slices** state per sub-tree to **minimize recomposition**
*   Uses **derivedStateOf**, **keys**, and **Paging** for scale
*   Persists **only necessary** pieces across lifecycle events

***

## Crisp Interview Summary

> “For large UI state in Compose, I model it as an immutable `UiState` owned by a `ViewModel`, split into granular sub-states so only the parts that change recompose. I collect slices at the lowest-level composables, use `derivedStateOf` for expensive derivations, persistent immutable collections for stability, and `LazyColumn` with keys/contentType for list performance. For very large datasets I use Paging 3. I persist only small identifiers/filters via `SavedStateHandle` and keep heavy data in repositories. I stabilize parameters, avoid recreating objects/lambdas, and verify improvements with recomposition counts and the Layout Inspector.”

***

  </details>

</details>

<details>
  <summary><b>📂 Side Effects</b></summary>

  <details style="margin-left: 20px;">
    <summary><i>📄 1. Explain LaunchedEffect, SideEffect, DisposableEffect</i></summary>

***

## TL;DR (Use this as your opening line)

*   **`LaunchedEffect`**: Run **suspend** work tied to the **composition’s lifecycle** (e.g., load data once, debounce, collect flows). Cancels/restarts when its **keys change** or when leaving composition.
*   **`SideEffect`**: Run a **non-suspending** effect **after** a successful composition **commit** (e.g., push Compose state to external objects).
*   **`DisposableEffect`**: Register **callbacks/listeners** or resources that require **cleanup**. Runs setup on enter (or key change) and **disposes** on key change or when leaving composition.

Think:  
**LaunchedEffect = coroutine work**,  
**SideEffect = post-commit nudge**,  
**DisposableEffect = setup + teardown**.

***

## 1) `LaunchedEffect`

### What it does

Starts a **Coroutine** that is **scoped to the composable’s lifecycle**. It **launches when the keys change** (including the first time) and **cancels** when keys change or the composable leaves composition.

### When to use

*   Fire **one‑time** tasks on first composition (fetch, analytics, navigation).
*   **Collect** from Flows/suspend APIs.
*   **Debounce/throttle** user input.
*   Imperative calls that **must not** rerun on every recomposition.

### Key behaviors

*   Runs **after** the composable is first *entered*, not on every recomposition.
*   **Key(s)** determine restart. Use `LaunchedEffect(Unit)` for “run once while in composition”.

### Examples

**Run once while the composable is on screen**

```kotlin
@Composable
fun ProfileScreen(vm: ProfileVM = hiltViewModel()) {
    LaunchedEffect(Unit) {
        vm.loadProfile()          // suspend fun
    }
    // ... UI ...
}
```

**Restart when `userId` changes**

```kotlin
@Composable
fun Profile(userId: String, vm: ProfileVM = hiltViewModel()) {
    LaunchedEffect(userId) {
        vm.load(userId)
    }
}
```

**Debounce text input with snapshotFlow**

```kotlin
@Composable
fun SearchBox(vm: SearchVM = hiltViewModel()) {
    var query by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        snapshotFlow { query }
            .distinctUntilChanged()
            .debounce(300)
            .collect { vm.search(it) }
    }

    TextField(value = query, onValueChange = { query = it })
}
```

**Pitfalls**

*   ❌ Don’t call suspend functions directly in the composable body.
*   ❌ Don’t use `LaunchedEffect` with unstable/changing keys accidentally (causes restarts/jank).
*   ✅ Prefer `collectAsStateWithLifecycle()` for simple UI state observation; use `LaunchedEffect` when you need **imperative reactions** (e.g., navigation after a state change).

***

## 2) `SideEffect`

### What it does

Runs a **non‑suspending** block **after** Compose successfully **commits** the current frame (i.e., after recomposition applies changes). Useful to **push Compose-derived values out** to objects **not managed by Compose**.

### When to use

*   Synchronize Compose state to **imperative, non-Compose** components:
    *   View properties, analytics loggers, remember’d controllers, external caches.
*   Update fields that **must reflect the committed UI state**.

### Key behaviors

*   Called **each successful recomposition** (when the composable participates).
*   **Must not suspend**.
*   Safe point: UI tree for this frame is **committed**.

### Examples

**Update non-Compose object after commit**

```kotlin
@Composable
fun MapContainer(controller: MapController, zoom: Float) {
    // Compose UI...
    SideEffect {
        controller.setZoom(zoom) // non-suspending, external side effect
    }
}
```

**Push analytics screen name after commit**

```kotlin
@Composable
fun Screen(analytics: Analytics, name: String) {
    SideEffect {
        analytics.setCurrentScreen(name)
    }
}
```

**Pitfalls**

*   ❌ Don’t do long/blocked work—this must be quick.
*   ❌ Don’t trigger recomposition loops (only mutate **external** state, not Compose state here).

***

## 3) `DisposableEffect`

### What it does

Provides a **setup + dispose** lifecycle around a key. Great for registering listeners, callbacks, sensors, BroadcastReceivers, and cleaning them up deterministically.

### When to use

*   **Register** a listener, **unregister** on dispose.
*   Start/stop camera, location updates, ambient sensors tied to composition.
*   Bridge to lifecycle‑less SDKs that require **manual cleanup**.

### Key behaviors

*   Runs setup when **entering** composition or when **keys change**.
*   Runs `onDispose` when keys change or leaving composition.
*   Block is **non‑suspending** (you can launch your own coroutine inside if needed).

### Examples

**Register/unregister a listener**

```kotlin
@Composable
fun ConnectivityObserver(listener: ConnectivityListener) {
    DisposableEffect(Unit) {
        listener.start()
        onDispose {
            listener.stop()
        }
    }
}
```

**Add TextWatcher to an Android View**

```kotlin
@Composable
fun LegacyTextField(view: EditText) {
    DisposableEffect(view) {
        val watcher = object : TextWatcher { /* ... */ }
        view.addTextChangedListener(watcher)
        onDispose { view.removeTextChangedListener(watcher) }
    }
}
```

**Lifecycle observer tied to a key**

```kotlin
@Composable
fun TrackLifecycle(tag: String) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, tag) {
        val observer = LifecycleEventObserver { _, event ->
            // log event with tag
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}
```

**Pitfalls**

*   ❌ Forgetting to remove listeners → memory leaks.
*   ❌ Using unstable keys → frequent teardown/setup cycles.

***

## Choosing Between Them (Mental Model)

| Use case                                          | Pick                                     |
| ------------------------------------------------- | ---------------------------------------- |
| Run suspend job tied to composition               | **LaunchedEffect**                       |
| Collect Flow, debounce input                      | **LaunchedEffect**                       |
| Push state to external object after UI commit     | **SideEffect**                           |
| Add/remove listeners, resources that need cleanup | **DisposableEffect**                     |
| Needs a coroutine scope without lifecycle restart | `rememberCoroutineScope()` (alternative) |

**Tip:**

*   If you need a **coroutine scope** for user events (e.g., button click), use `rememberCoroutineScope()` instead of `LaunchedEffect`, because you don’t want it to restart with key changes:
    ```kotlin
    val scope = rememberCoroutineScope()
    Button(onClick = { scope.launch { vm.refresh() } }) { Text("Refresh") }
    ```

***

## Keys: The Hidden Lever

*   All three accept **keys** to control **when they start/stop**.
*   **Stable, meaningful keys** prevent accidental restarts or missed cleanup.
*   Patterns:
    *   `Effect(Unit)` → **run once** while in composition.
    *   `Effect(arg1, arg2)` → **restart** when any arg changes.

***

## Common Pitfalls (and fixes)

*   **Starting work in the composable body**  
    → Move to `LaunchedEffect` or `rememberCoroutineScope()`.
*   **Navigation/snackbar in UI state causing repeats**  
    → Model as **one-off effects** (SharedFlow/Channel) and handle in a `LaunchedEffect`.
*   **Memory leaks with listeners**  
    → Always use `DisposableEffect` and ensure `onDispose` unregisters.
*   **Unstable keys causing jitter**  
    → Use IDs or stable values, avoid passing lambdas/objects that change identity each recomposition.

***

## Quick Examples to Quote

**One-off navigation when logged in**

```kotlin
@Composable
fun LoginGate(vm: AuthVM = hiltViewModel(), onNavigateHome: () -> Unit) {
    val loggedIn by vm.loggedIn.collectAsStateWithLifecycle()
    LaunchedEffect(loggedIn) {
        if (loggedIn) onNavigateHome()
    }
}
```

**Sync Compose state to external controller**

```kotlin
@Composable
fun Player(controller: MediaController, isMuted: Boolean) {
    SideEffect { controller.setMute(isMuted) }
}
```

**Register and clean up a callback**

```kotlin
@Composable
fun SensorReader(sensorManager: SensorManager) {
    DisposableEffect(sensorManager) {
        val listener = MySensorListener()
        sensorManager.registerListener(listener)
        onDispose { sensorManager.unregisterListener(listener) }
    }
}
```

***

## Interview‑Ready Closing

> “Use `LaunchedEffect` for suspend work tied to the composable’s lifecycle, `SideEffect` to push non-suspending updates after composition commits, and `DisposableEffect` for setup/teardown of external resources. Keys control when these effects start/stop. This separation prevents recomposition bugs, avoids leaks, and keeps UI declarative while still handling imperative needs cleanly.”

***

  </details>

  <details style="margin-left: 20px;">
    <summary><i>📄 2. When would you use rememberCoroutineScope vs LaunchedEffect?</i></summary>

***

# ✅ Common Mistakes with Side Effects in Jetpack Compose

## ✅ Strong Interview Opening (Say This First)

> The most common mistakes with side effects in Compose involve **triggering them during recomposition**, **using wrong effect APIs**, **incorrect keys**, and **mixing UI rendering with imperative logic**, which can lead to recomposition loops, memory leaks, or repeated executions.

***

## 1️⃣ Performing Side Effects Directly in the Composable Body ❌

### ❌ Mistake

```kotlin
@Composable
fun Screen(vm: VM) {
    vm.loadData() // ❌ runs on every recomposition
}
```

### ❌ Why it’s bad

*   Composables **re-execute frequently**
*   Causes:
    *   Multiple network calls
    *   Repeated analytics events
    *   Infinite loops

### ✅ Correct

```kotlin
LaunchedEffect(Unit) {
    vm.loadData()
}
```

🎯 Interview line:

> “Composable functions may re-run many times—side effects must not be inside the composable body.”

***

## 2️⃣ Using `LaunchedEffect` with Unstable or Changing Keys ❌

### ❌ Mistake

```kotlin
LaunchedEffect(list) { // list reference changes often
    vm.process(list)
}
```

### ❌ Why it’s bad

*   Effect **restarts frequently**
*   Coroutine is canceled & relaunched
*   Causes:
    *   Performance issues
    *   Jank
    *   Unexpected logic resets

### ✅ Correct

```kotlin
LaunchedEffect(list.size) {
    vm.process(list)
}
```

or

```kotlin
LaunchedEffect(Unit) { ... }
```

when it should run only once.

🎯 Interview line:

> “Keys should represent logical identity, not unstable references.”

***

## 3️⃣ Using `LaunchedEffect` for User‑Triggered Actions ❌

### ❌ Mistake

```kotlin
var clicked by remember { mutableStateOf(false) }

LaunchedEffect(clicked) {
    if (clicked) vm.submit()
}
```

### ❌ Why it’s bad

*   Button click → state change → effect triggers
*   Hard to reason about
*   Easy to repeat accidentally

### ✅ Correct – Use `rememberCoroutineScope`

```kotlin
val scope = rememberCoroutineScope()
Button(onClick = {
    scope.launch { vm.submit() }
}) { Text("Submit") }
```

🎯 Interview line:

> “LaunchedEffect is for reacting to state, not for handling user events.”

***

## 4️⃣ Forgetting to Clean Up in `DisposableEffect` ❌

### ❌ Mistake

```kotlin
DisposableEffect(Unit) {
    listener.register()
    // ❌ missing onDispose
}
```

### ❌ Why it’s bad

*   Memory leaks
*   Duplicate listeners
*   Crashes in long sessions

### ✅ Correct

```kotlin
DisposableEffect(Unit) {
    listener.register()
    onDispose {
        listener.unregister()
    }
}
```

🎯 Interview line:

> “If you allocate resources, you must always clean them up.”

***

## 5️⃣ Using `SideEffect` for Long‑Running or Blocking Work ❌

### ❌ Mistake

```kotlin
SideEffect {
    Thread.sleep(3000) // ❌ blocking
}
```

### ❌ Why it’s bad

*   Runs on the UI thread
*   Causes frame drops
*   SideEffect runs **after every successful recomposition**

### ✅ Correct

*   Use `LaunchedEffect` for suspend work
*   Use ViewModel for heavy logic

🎯 Interview line:

> “SideEffect must be fast, non‑blocking, and external-state only.”

***

## 6️⃣ Triggering State Changes Inside `SideEffect` ❌

### ❌ Mistake

```kotlin
SideEffect {
    count++ // ❌ mutates Compose state
}
```

### ❌ Why it’s bad

*   Triggers recomposition → SideEffect runs again
*   Causes **infinite recomposition loop**

### ✅ Correct

*   Use SideEffect only for **external updates**
*   Update Compose state elsewhere

🎯 Interview phrase:

> “SideEffect should never mutate Compose state.”

***

## 7️⃣ Using Side Effects Instead of ViewModel ❌

### ❌ Mistake

```kotlin
LaunchedEffect(Unit) {
    repository.fetchData() // ❌ business logic in UI
}
```

### ❌ Why it’s bad

*   Violates separation of concerns
*   Hard to test
*   Fragile lifecycle handling

### ✅ Correct

```kotlin
class VM : ViewModel() {
    fun load() = viewModelScope.launch { ... }
}
```

```kotlin
LaunchedEffect(Unit) {
    vm.load()
}
```

🎯 Interview line:

> “Compose should trigger side effects, not contain business logic.”

***

## 8️⃣ Handling Navigation or Toasts Without One‑Off Effect Handling ❌

### ❌ Mistake

```kotlin
if (state.success) {
    navController.navigate("home") // ❌ multiple recompositions
}
```

### ❌ Why it’s bad

*   Recomposition repeats navigation
*   Back stack corruption
*   Crashes or duplicated screens

### ✅ Correct

Use `SharedFlow` + `LaunchedEffect`

```kotlin
LaunchedEffect(Unit) {
    vm.effects.collect {
        if (it is NavigateHome) navController.navigate("home")
    }
}
```

🎯 Interview gold line:

> “One‑off events should never be stored directly in UI state.”

***

## 9️⃣ Forgetting That Side Effects Are Lifecycle‑Scoped ❌

### ❌ Mistake

```kotlin
rememberCoroutineScope().launch {
    longRunningTask()
}
```

### ❌ Why it’s bad

*   Coroutine is canceled when composable leaves
*   Work might stop unexpectedly

### ✅ Correct

*   Use `viewModelScope` for long‑lived tasks

🎯 Interview line:

> “If work must outlive the UI, it belongs in the ViewModel.”

***

## 10️⃣ Overusing Side Effects Instead of Declarative APIs ❌

### ❌ Mistake

```kotlin
LaunchedEffect(state) {
    scrollState.scrollToItem(0)
}
```

When:

*   This could be achieved declaratively

### ✅ Better

Use declarative patterns where possible; reserve side effects for:

*   Navigation
*   Animations
*   External system calls

🎯 Interview wisdom:

> “Side effects are an escape hatch—not the default.”

***

## ✅ Quick Interview Checklist (Memorize This)

❌ Side effects in composable body  
❌ Wrong or unstable keys  
❌ Using LaunchedEffect for button clicks  
❌ Missing cleanup in DisposableEffect  
❌ Blocking work in SideEffect  
❌ Mutating Compose state in SideEffect  
❌ Business logic inside UI  
❌ Repeating navigation/snackbars  
❌ Long‑lived tasks outside ViewModel

***

## 🔥 Interview‑Ready Final Summary

> “Common mistakes with side effects in Compose include running them inside the composable body, using unstable keys in `LaunchedEffect`, forgetting cleanup in `DisposableEffect`, misusing `SideEffect` for heavy work or state mutation, and handling user events or navigation incorrectly. The correct approach is to keep composables pure, use the right side‑effect API for the job, scope long‑running work to the ViewModel, and treat side effects as controlled, lifecycle‑aware imperatives.”

***

## ✅ One‑Line Closing (Very Strong)

> *“Side effects should be deliberate, scoped, and predictable—never accidental.”*

***

  </details>

</details>

<details>
  <summary><b>📂 UI & Performance</b></summary>

  <details style="margin-left: 20px;">
    <summary><i>📄 1. How do you optimize Compose performance?</i></summary>

***

# ✅ How do you optimize Jetpack Compose performance?

## ✅ Strong Interview Opening

> Compose performance optimization is mainly about **controlling recomposition**, **keeping UI state stable and minimal**, **structuring composables correctly**, and **using lazy and derived APIs effectively**, while validating improvements using Compose tooling.

***

## 1️⃣ Control and Minimize Recomposition (MOST IMPORTANT)

### ✅ Hoist State Correctly

Only the composables that depend on state should recompose.

```kotlin
@Composable
fun Screen() {
    Header()
    Content() // owns state
    Footer()
}
```

```kotlin
@Composable
fun Content() {
    var count by remember { mutableStateOf(0) }
    Text("Count: $count")
}
```

✅ Header/Footer won’t recompose when `count` changes  
🎯 Interview line:

> “Compose re-runs functions, so controlling state location controls recomposition.”

***

### ✅ Don’t Read State Too High in the Tree

```kotlin
@Composable
fun BadScreen(vm: VM) {
    val user by vm.user.collectAsState()
    Header(user.name) // causes full screen recomposition
}
```

✅ Instead, collect closer to usage.

***

## 2️⃣ Use Stable & Immutable Data

### ✅ Prefer Immutable Models

```kotlin
@Immutable
data class User(val id: Long, val name: String)
```

✅ Lets Compose safely **skip recomposition**

***

### ✅ Avoid Mutable Collections

❌ `MutableList`, `HashMap`  
✅ `List`, `Map`, or `persistentListOf()`

```kotlin
val items: List<Item>
```

🎯 Interview quote:

> “Stable parameters allow Compose to skip work safely.”

***

## 3️⃣ Avoid Object and Lambda Recreation

### ❌ Bad

```kotlin
Button(onClick = { vm.submit(user) })
```

### ✅ Good

```kotlin
val onSubmit = remember(user) { { vm.submit(user) } }

Button(onClick = onSubmit)
```

✅ Prevents unnecessary recompositions  
✅ Reduces allocations

***

## 4️⃣ Use `derivedStateOf` for Expensive Calculations

### ❌ Bad

```kotlin
val filtered = items.filter { it.active }
```

Recomputes on every recomposition.

### ✅ Good

```kotlin
val filtered by remember(items) {
    derivedStateOf { items.filter { it.active } }
}
```

✅ Recomputes **only when `items` changes**

***

## 5️⃣ Optimize Lists with Lazy Components

### ✅ Always use Lazy layouts for large collections

*   `LazyColumn`
*   `LazyRow`
*   `LazyGrid`

```kotlin
LazyColumn {
    items(
        items = users,
        key = { it.id },
        contentType = { "user" }
    ) { user ->
        UserRow(user)
    }
}
```

✅ `key` → correct slot reuse  
✅ `contentType` → layout reuse  
✅ Prevents unnecessary UI rebinding

***

## 6️⃣ Use Paging for Very Large Data Sets

For large or infinite lists:

```kotlin
val items = vm.pagingFlow.collectAsLazyPagingItems()

LazyColumn {
    items(
        count = items.itemCount,
        key = items.itemKey { it.id },
    ) { idx ->
        items[idx]?.let { ItemRow(it) }
    }
}
```

✅ Data loaded on demand  
✅ Minimal memory & recomposition

***

## 7️⃣ Move Heavy Work Out of Composables

### ❌ Bad

```kotlin
@Composable
fun Screen() {
    val data = loadData() // heavy work
}
```

### ✅ Good

*   Use ViewModel
*   Use `LaunchedEffect`
*   Use repositories

```kotlin
LaunchedEffect(Unit) {
    vm.loadData()
}
```

🎯 Interview phrase:

> “Composable functions must be fast and side‑effect free.”

***

## 8️⃣ Avoid Blocking the Main Thread

### ✅ Use suspend APIs, never block

❌ `Thread.sleep()`  
✅ Suspending APIs via coroutines

Compose relies on the UI thread for frame rendering.

***

## 9️⃣ Use `remember`, `rememberSaveable` Properly

*   `remember` → avoid recreating objects
*   `rememberSaveable` → use sparingly (Bundle size!)

```kotlin
val scrollState = rememberLazyListState()
```

✅ Keeps scroll position stable  
✅ Prevents jank

***

## 🔟 Prefer Declarative APIs Over Side Effects

### ❌ Bad

```kotlin
LaunchedEffect(state) {
    scrollState.scrollToItem(0)
}
```

### ✅ Better

Use derived state or declarative scroll patterns where possible.

🎯 Interview insight:

> “Side effects are an escape hatch—declarative code is faster and safer.”

***

## 1️⃣1️⃣ Reduce Overdraw and Layout Complexity

Compose is still rendered via **Android rendering pipeline**:

*   Avoid deep UI trees
*   Avoid unnecessary nesting
*   Use `Box` + `Modifier` wisely

```kotlin
Box(
    modifier = Modifier
        .background(Color.White)
        .padding(16.dp)
)
```

✅ Fewer layers  
✅ Better rendering performance

***

## 1️⃣2️⃣ Measure & Validate Using Tools (VERY IMPORTANT)

Interviewers love this part.

### ✅ Compose Tooling

*   **Layout Inspector**
*   **Recomposition Counts**
*   **Composition Tracing**
*   **Macrobenchmark**

### ✅ Debug recomposition:

```kotlin
Modifier.recomposeHighlighter() // debug only
```

🎯 Interview line:

> “Most recompositions are cheap—optimize only after measuring.”

***

## 1️⃣3️⃣ Navigation & ViewModel Scoping

Use Navigation‑Compose correctly:

*   ViewModel scoped to destination
*   Avoid reloading on every recomposition

```kotlin
val vm: ScreenVM = hiltViewModel()
```

✅ Stable lifecycle  
✅ Avoids repeated work

***

## ✅ Quick Performance Checklist (Memorize This)

✅ Hoist state correctly  
✅ Use immutable/stable data  
✅ Don’t read state too high  
✅ Avoid recreating lambdas/objects  
✅ Use keys in lists  
✅ Use derivedStateOf  
✅ Use Lazy + Paging  
✅ Move heavy work to ViewModel  
✅ Measure before optimizing

***

## 🔥 Interview‑Ready Final Summary

> “Optimizing Compose performance focuses on minimizing unnecessary recompositions through proper state hoisting, stable and immutable data, and correct composable boundaries. Using Lazy components with keys, `derivedStateOf` for expensive calculations, and Paging for large datasets further improves performance. Heavy operations and business logic should live outside composables, and all optimizations should be validated using Compose tooling such as recomposition counts and the Layout Inspector.”

***

## ✅ One‑Line Closing (Very Strong)

> *“In Compose, performance is about data flow and state design, not manual UI optimization.”*

***

  </details>

  <details style="margin-left: 20px;">
    <summary><i>📄 2. Difference between Column and LazyColumn</i></summary>

***

## 🧠 Mental Model

*   **`Column`**: A simple layout that **places all children vertically**. If you make it scrollable with `verticalScroll`, it will **compose and measure all children upfront** (eager), then draw them within a scrollable container.
*   **`LazyColumn`**: A **virtualized, recycling-like** list that **only composes items that are visible (plus small buffer)** on screen (lazy). Excellent for **large/unknown** lists and supports list-specific optimizations (keys, contentType, sticky headers, item animations, etc.).

***

## 🔍 Key Differences (at a glance)

| Aspect               | `Column`                                                    | `LazyColumn`                                                     |
| -------------------- | ----------------------------------------------------------- | ---------------------------------------------------------------- |
| Composition          | **Eager**: all children composed/laid out                   | **Lazy**: only visible items composed                            |
| Scrolling            | Needs `Modifier.verticalScroll(rememberScrollState())`      | Built‑in scrolling with `rememberLazyListState()`                |
| Use case             | **Small/fixed** content                                     | **Large/long** lists (dynamic)                                   |
| Performance          | Can be expensive for many children (O(n) compose & measure) | Efficient for long lists; lower memory/CPU                       |
| API style            | Direct children via composable calls                        | DSL: `item {}`, `items(list) {}`                                 |
| Keys                 | Not applicable                                              | `key` per item for identity & stable reuse                       |
| Content type         | Not applicable                                              | `contentType` hints layout reuse                                 |
| Item animations      | Manual                                                      | `animateItemPlacement` (per‑item), `LazyListItemScope`           |
| Sticky headers       | Manual                                                      | Supported via `stickyHeader {}` (with accompanist or newer APIs) |
| Nesting              | Easy to nest                                                | Nested lazy lists require care (measure policies)                |
| Placeholders/shimmer | Manual                                                      | Per-item laziness helps with skeletons/loading states            |
| Remembered state     | `rememberScrollState()`                                     | `rememberLazyListState()` (position & offset)                    |

***

## ✅ When to Use Which

**Use `Column` when:**

*   The number of children is **small and fixed** (e.g., a form with 10 fields).
*   You need **simple vertical stacking** without list semantics.
*   You want **all content available in memory** (e.g., measurements/animations across all children).

```kotlin
@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        SectionTitle("Account")
        ProfileRow()
        Divider()
        SectionTitle("Notifications")
        ToggleRow("Email Alerts")
        ToggleRow("Push Notifications")
        // Few more rows—fine with Column
    }
}
```

**Use `LazyColumn` when:**

*   The list is **large/unknown** in length (e.g., 1k+ items).
*   Items are **data-driven** and you only need what’s visible.
*   You need list-specific features: **keys**, **contentType**, **paging**, **sticky headers**, **item animations**.

```kotlin
@Composable
fun UsersList(users: List<UserUi>) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = users,
            key = { it.id },                 // important for stability
            contentType = { "user" }         // helps layout reuse
        ) { user ->
            UserRow(user)
        }
    }
}
```

***

## ⚙️ API Differences (with examples)

### `Column` (with scrolling)

```kotlin
Column(
    modifier = Modifier.verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(8.dp),
    horizontalAlignment = Alignment.Start
) {
    Text("Header")
    repeat(1000) { index ->
        Text("Row $index") // ⚠️ All 1000 are composed & measured
    }
}
```

### `LazyColumn` (lazy DSL)

```kotlin
LazyColumn(
    state = rememberLazyListState(),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    item { Text("Header") }     // single header item
    items(1000, key = { it }) { index ->
        Text("Row $index")      // ✅ Only visible rows are composed
    }
}
```

***

## 🚀 Performance Considerations

*   **Column**:
    *   **Pros**: Simpler; predictable; good for small UIs.
    *   **Cons**: With many children, **initial compose/measure is heavy**; high memory usage; potential jank when content is large.
*   **LazyColumn**:
    *   **Pros**: **Virtualization** (compose only visible); supports **Paging 3**; better memory usage; smooth scrolling.
    *   **Cons**: Slight overhead in lazy machinery; **not ideal for very small, finite content** if you don’t need list semantics.

**Interview tip**:

> “If I see a long list implemented with `Column + verticalScroll`, that’s a red flag. I switch to `LazyColumn` for scalability.”

***

## 🧩 Advanced: Keys and contentType (LazyColumn)

Use **`key`** to provide **stable identity** and prevent incorrect reuse or recomposition:

```kotlin
items(
    items = users,
    key = { it.id },
    contentType = { user -> if (user.isAd) "ad" else "user" }
) { user ->
    if (user.isAd) AdRow(user) else UserRow(user)
}
```

*   `key` → maps items to **stable slots** (crucial for reordering/insertions/deletions).
*   `contentType` → helps Compose **reuse** measure/layout paths for similar item types.

***

## 📌 Common Pitfalls (and fixes)

1.  **Using `Column` for paginated/large lists** → Switch to `LazyColumn` (or `LazyVerticalGrid`).
2.  **Not providing keys in `LazyColumn` with dynamic data** → Provide `key = { id }`.
3.  **Lifting list state too high** → Keep `rememberLazyListState()` **local** to the list composable.
4.  **Heavy per-item work in the item lambda** → Precompute in ViewModel or use `derivedStateOf`.
5.  **Nested `LazyColumn` inside `LazyColumn`** → Prefer grouping items within one list; if nested, constrain heights (e.g., `Modifier.heightIn`) and measure costs.
6.  **Forcing all items to be composed (e.g., measuring all items)** → Avoid; leverage laziness and stable content types.

***

## 🔄 Sticky Headers / Item Animations

*   **Sticky headers**: `LazyColumn` supports patterns like sticky headers (community libs or newer APIs) better than `Column`.
*   **Item placement animations**:
    ```kotlin
    items(items, key = { it.id }) { user ->
        Row(Modifier.animateItemPlacement()) {
            UserRow(user)
        }
    }
    ```
*   Not practical with `Column` for large datasets.

***

## 🧪 Scrolling State & Restore

*   **Column**:
    ```kotlin
    val scroll = rememberScrollState()
    Column(Modifier.verticalScroll(scroll)) { ... }
    ```
*   **LazyColumn**:
    ```kotlin
    val listState = rememberLazyListState()
    LazyColumn(state = listState) { ... }
    // Can also save/restore using rememberSaveable(listState) if needed
    ```

***

## ✅ Interview‑Ready Summary

> **Column** renders **all children** and is great for **small, fixed content** (optionally scrollable with `verticalScroll`).  
> **LazyColumn** composes only **visible items** (lazy/virtualized), supports **keys**, **contentType**, **paging**, and is the right choice for **large or dynamic lists**. For performance, prefer `LazyColumn` for any list‑like content, provide stable `key`s, and keep heavy work out of the item lambdas.

***

  </details>

  <details style="margin-left: 20px;">
    <summary><i>📄 3. How does LazyColumn handle recycling internally?</i></summary>

***

## 🧠 Mental model: Lazy ≠ classic View recycling

*   **RecyclerView** reuses **View** instances by moving them between **view holders** (imperative reuse).
*   **LazyColumn** doesn’t have Views to recycle. Instead, it:
    1.  **Sub-composes** only the items that are visible (plus a small prefetch/buffer window).
    2.  **Disposes** items that go far off-screen (removes them from composition).
    3.  **Reuses subcomposition “slots”** and **placeables** when possible (via `contentType` pooling and internal reuse), minimizing re-composition/re-measure work.
    4.  **Pre-composes / pre-measures** upcoming items off the main path to reduce jank on fast scrolls.

Think of it as **virtualization + subcomposition reuse**, not View recycling.

***

## 🔩 The building blocks under the hood (high-level)

> (No need to quote class names in an interview unless you’re comfortable—this is the gist.)

*   **LazyList (LazyColumn/LazyRow)** is built on top of an internal **LazyLayout** framework that uses **subcomposition** to create only the needed item nodes.
*   A **layout state** computes which item indices are needed for the current scroll position and viewport (with a **lookahead/prefetch** buffer).
*   For each needed index, LazyColumn:
    *   **Subcomposes** the item content (or **reuses** a previously composed slot if compatible).
    *   **Measures** and **places** the result (placeables) in the current frame.
*   Items that scroll out of range are either:
    *   **Kept in a short-lived reusable pool** (when `contentType` matches) for quick reuse; or
    *   **Disposed** (removed from composition) if they’re no longer likely to be reused soon.

This achieves the same *effect* as recycling—keeping memory and CPU usage proportional to viewport size—without View holders.

***

## ♻️ What exactly is “reused”?

Compose can reuse **subcomposition slots** and **measure/layout artifacts** rather than Views:

1.  **Composition slots & nodes**:  
    If a new on-screen item has the **same `contentType`** as a recently off-screen one, Compose can **reuse** the underlying slot/node structure. This reduces “compose from scratch” work.

2.  **Measurement path**:  
    With **consistent item structure** and `contentType`, much of the **measurement/layout path** can be reused, avoiding expensive remeasures.

3.  **Data diffing via keys**:  
    A **stable `key`** allows Compose to **maintain identity** across insertions/removals/reorders. This prevents mismatched reuse and unnecessary recomposition of items that didn’t logically change.

> **Difference from RecyclerView:**  
> RecyclerView recycles **View instances**; LazyColumn reuses **composition/measurement artifacts** and smartly **recomposes** only where needed.

***

## 🔑 The role of `key` and `contentType` (very important)

*   **`key`** provides **stable identity** for each item. It’s critical when your list can **insert/remove/reorder** items:
    ```kotlin
    LazyColumn {
        items(
            items = users,
            key = { it.id },                 // identity for diffing
            contentType = { "user" }         // type pooling
        ) { user ->
            UserRow(user) // stable, immutable UserRow for skip/partial recomposition
        }
    }
    ```

*   **`contentType`** hints the list that certain items share **the same structure**. Items of the same `contentType` can **reuse** composition slots more efficiently. Use different `contentType`s for ads vs content, headers vs rows, etc.

> **Rule of thumb:**  
> Always provide a **stable `key`** for dynamic lists; provide **`contentType`** when you have multiple item types.

***

## 🚀 Prefetching & precomposing

To avoid jank on fast scrolls, LazyColumn does **prefetching**:

*   **Pre-composition**: it can compose items just outside the viewport.
*   **Pre-measure**: it can measure them ahead of time so they’re ready to place.

This is similar in spirit to RecyclerView’s prefetch, but in Compose it’s about **subcomposition and measure** instead of binding views.

> Tip: keeping item content lightweight and stable maximizes the benefit of prefetching.

***

## 🧹 Disposal vs retention

Items that move sufficiently out of bounds are **disposed** (removed from the composition), releasing memory. Thanks to:

*   **Keys**, if the same items come back into view later, Compose can **rebind** them with minimal churn.
*   **contentType pooling**, if another item of the same type scrolls into view, Compose may **reuse** subcomposition slots rather than fully composing a fresh one.

This gives you **bounded memory usage** while keeping scroll smooth.

***

## ⚖️ LazyColumn vs Column (why it matters for “recycling”)

*   **Column + verticalScroll**: *composes and measures everything*, no virtualization → **no recycling**, heavy cost for large lists.
*   **LazyColumn**: *composes/keeps only what’s needed* → **virtualization + reuse**, scalable to very large data sets.

> Interview line:  
> “LazyColumn achieves the effect of recycling by subcomposing only visible items, disposing those that leave the viewport, and reusing composition slots via `contentType` and stable `key`s.”

***

## 👨‍💻 Practical tips to maximize LazyColumn efficiency

1.  **Always supply `key`** for dynamic lists.
2.  **Use `contentType`** to group structurally identical items (content vs ad vs header).
3.  **Keep item composables stable**:
    *   Pass **immutable models** (`data class`, `@Immutable`)
    *   Avoid recreating lambdas/formatters each time (wrap in `remember` as needed).
4.  **Avoid heavy work in item lambdas**:
    *   Precompute in ViewModel or use `remember/derivedStateOf`.
5.  **Paging 3** for very large/infinite lists:
    ```kotlin
    val items = vm.pager.collectAsLazyPagingItems()
    LazyColumn {
        items(
            count = items.itemCount,
            key = items.itemKey { it.id },
            contentType = items.itemContentType { "user" }
        ) { index ->
            items[index]?.let { UserRow(it) }
        }
    }
    ```
6.  **Use `animateItemPlacement()`** judiciously—nice UX but adds work. Ensure keys are stable.

***

## 🧪 How to explain in the interview (concise version)

> “LazyColumn doesn’t recycle Views like RecyclerView. It **sub-composes only visible items**, **disposes** those that move far off-screen, and **reuses subcomposition slots** based on `contentType`, with **identity preserved** by `key`. It also **prefetches** (pre-compose/measure) upcoming items to keep scrolling smooth. This gives you the memory and performance benefits of recycling in a Compose-native way.”

***

  </details>

  <details style="margin-left: 20px;">
    <summary><i>📄 4. How do you create custom layouts in Compose?</i></summary>

***

## 🧠 Mental model of Compose layout

Compose layout has **three phases**:

1.  **Measurement** – Each child is measured with **constraints** (min/max width/height).
2.  **Placement** – You position each child via `place`/`placeRelative` inside the parent’s bounds.
3.  **Drawing** – After layout, the node is drawn.

> You can build custom layouts either by:
>
> *   Writing a **custom `@Composable`** using `Layout` (or `SubcomposeLayout`), or
> *   Writing a **custom `Modifier`** via `Modifier.layout { … }` to tweak a single child.

***

## ✅ Approaches to custom layout

### 1) `Layout` composable (recommended starting point)

Use this when you’re composing **all children in one place** and you control how they are measured and placed.

**Example: A simple `FlowRow` (wrap to next line when width exceeded)**

```kotlin
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalGap: Int = 8,
    verticalGap: Int = 8,
    content: @Composable () -> Unit
) {
    Layout(modifier = modifier, content = content) { measurables, constraints ->
        val placeables = mutableListOf<Placeable>()

        var rowWidth = 0
        var rowHeight = 0
        var layoutWidth = 0
        var layoutHeight = 0

        val rows = mutableListOf<List<Placeable>>()
        var currentRow = mutableListOf<Placeable>()

        // Measure each child with incoming constraints (respecting max width)
        for (measurable in measurables) {
            val placeable = measurable.measure(constraints)
            if (rowWidth > 0 && rowWidth + horizontalGap + placeable.width > constraints.maxWidth) {
                // Commit previous row
                rows += currentRow
                layoutWidth = maxOf(layoutWidth, rowWidth)
                layoutHeight += rowHeight + if (layoutHeight > 0) verticalGap else 0

                // Reset row
                currentRow = mutableListOf(placeable)
                rowWidth = placeable.width
                rowHeight = placeable.height
            } else {
                // Add to current row
                if (rowWidth > 0) rowWidth += horizontalGap
                rowWidth += placeable.width
                rowHeight = maxOf(rowHeight, placeable.height)
                currentRow += placeable
            }
        }
        if (currentRow.isNotEmpty()) {
            rows += currentRow
            layoutWidth = maxOf(layoutWidth, rowWidth)
            layoutHeight += rowHeight
        }

        // Ensure size respects constraints (coerceIn handles min/max)
        val width = layoutWidth.coerceIn(constraints.minWidth, constraints.maxWidth)
        val height = layoutHeight.coerceIn(constraints.minHeight, constraints.maxHeight)

        layout(width, height) {
            var y = 0
            rows.forEachIndexed { rowIndex, row ->
                var x = 0
                val rowMaxHeight = row.maxOf { it.height }
                row.forEachIndexed { i, placeable ->
                    placeable.placeRelative(x, y)
                    x += placeable.width + if (i < row.lastIndex) horizontalGap else 0
                }
                y += rowMaxHeight + if (rowIndex < rows.lastIndex) verticalGap else 0
            }
        }
    }
}
```

**What to say in interview:**

*   “I measure children with the incoming constraints, pack them into rows, compute my own size, then place them. I use `placeRelative` to respect RTL.”

***

### 2) `Modifier.layout` (layout a **single child**)

Use this when you want to tweak layout **without creating a new parent**—e.g., custom padding, alignment, or offset logic.

**Example: Center a child within a fixed square, regardless of its size**

```kotlin
fun Modifier.centerInSquare(side: Int) = this.then(
    Modifier.layout { measurable, _ ->
        val placeable = measurable.measure(
            Constraints.fixed(side, side)
        )
        layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0) // already fixed to square
        }
    }
)
```

**Example: Baseline padding for text (alignment lines)**

```kotlin
fun Modifier.firstBaselineToTop(distance: Int) = this.then(
    Modifier.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        val firstBaseline = placeable[FirstBaseline]
        require(firstBaseline != AlignmentLine.Unspecified)
        val topPadding = distance - firstBaseline
        val height = placeable.height + topPadding
        layout(placeable.width, height) {
            placeable.placeRelative(0, topPadding)
        }
    }
)
```

**What to say:**

*   “`Modifier.layout` lets me intercept measurement/placement for a single child; it’s perfect for custom baselines, insets, or bespoke alignment logic.”

***

### 3) `SubcomposeLayout` (measure after seeing size of other content)

Use when **some children depend on the size of others**, similar to “deferred” composition. Great for **overlay badges**, **text-dependent layouts**, or **list items where the trailing content depends on leading width**.

**Example: Measure title first; place an action only if it fits**

```kotlin
@Composable
fun TitleWithOptionalAction(
    title: @Composable () -> Unit,
    action: @Composable () -> Unit
) {
    SubcomposeLayout { constraints ->
        val titlePlaceables = subcompose("title", title).map { it.measure(constraints) }
        val titleWidth = titlePlaceables.maxOf { it.width }
        val titleHeight = titlePlaceables.maxOf { it.height }

        val remaining = constraints.maxWidth - titleWidth
        val actionPlaceables = subcompose("action") { action() }.map {
            it.measure(constraints.copy(minWidth = 0, maxWidth = remaining.coerceAtLeast(0)))
        }
        val actionWidth = actionPlaceables.maxOfOrNull { it.width } ?: 0
        val actionHeight = actionPlaceables.maxOfOrNull { it.height } ?: 0

        val width = (titleWidth + actionWidth).coerceIn(constraints.minWidth, constraints.maxWidth)
        val height = maxOf(titleHeight, actionHeight).coerceIn(constraints.minHeight, constraints.maxHeight)

        layout(width, height) {
            var x = 0
            titlePlaceables.forEach { it.placeRelative(x, (height - it.height) / 2) }
            x += titleWidth
            actionPlaceables.forEach { it.placeRelative(x, (height - it.height) / 2) }
        }
    }
}
```

**What to say:**

*   “`SubcomposeLayout` allows two-phase composition: measure some content, decide constraints for others, then place them.”

***

## 📏 Intrinsics (optional but senior-level)

Intrinsic measurements help Compose decide sizes during **wrapContent** or when a parent needs a child’s “natural” size.

*   With `Layout`, you can set a custom `MeasurePolicy` that defines `minIntrinsicWidth/Height` and `maxIntrinsicWidth/Height` using children’s `measurable.intrinsicWidth(height)` and `intrinsicHeight(width)`.

**Rule of thumb:**  
Only implement intrinsics if the layout participates in **wrapContent** sizing and the defaults don’t work.

***

## 🧭 Useful APIs and tips

*   **Constraints**: Always respect incoming constraints; don’t exceed `maxWidth`/`maxHeight`.
*   **`placeRelative` vs `place`**: Use `placeRelative` to be RTL-aware.
*   **Alignment lines**: Expose or read baselines for text‑aligned layouts.
*   **`LookaheadScope` / `LookaheadLayout`**: For advanced animated size/position transitions (if you’re doing predictive layout + animation).
*   **Performance**:
    *   Avoid measuring children multiple times unless necessary.
    *   Cache repeated calculations with `remember`.
    *   Keep item structures stable (immutable models) to reduce recomposition.

***

## 🧪 Mini demo: custom staggered grid

```kotlin
@Composable
fun StaggeredGrid(
    columns: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    require(columns > 0)
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val colWidths = IntArray(columns) { 0 }
        val colHeights = IntArray(columns) { 0 }

        val placeables = measurables.map { measurable ->
            val placeable = measurable.measure(constraints)
            // Pick the shortest column so far
            val column = colHeights.withIndex().minBy { it.value }.index
            colWidths[column] = maxOf(colWidths[column], placeable.width)
            colHeights[column] += placeable.height
            column to placeable
        }

        val width = colWidths.sum().coerceIn(constraints.minWidth, constraints.maxWidth)
        val height = colHeights.maxOrNull()?.coerceIn(constraints.minHeight, constraints.maxHeight) ?: constraints.minHeight

        // X offsets for each column
        val xOffsets = IntArray(columns) { 0 }.apply {
            for (i in 1 until columns) this[i] = this[i - 1] + colWidths[i - 1]
        }
        val yOffsets = IntArray(columns) { 0 }

        layout(width, height) {
            placeables.forEach { (col, placeable) ->
                val x = xOffsets[col]
                val y = yOffsets[col]
                placeable.placeRelative(x, y)
                yOffsets[col] += placeable.height
            }
        }
    }
}
```

**What this shows:**

*   Custom packing strategy, constraint handling, and placement.

***

## 🔚 Interview‑ready summary

> “I create custom layouts in Compose using either `Layout` (to measure/place multiple children), `Modifier.layout` (to tweak a single child), or `SubcomposeLayout` (when a child’s constraints depend on another). I respect the parent constraints, measure children appropriately, compute my own size, and place children using `placeRelative`. For advanced cases, I consider intrinsics and lookahead. I keep measurement efficient, use immutable models for stability, and prefer RTL‑aware placement.”

***

  </details>

  <details style="margin-left: 20px;">
    <summary><i>📄 5. How do you handle animations in Compose?</i></summary>

***

## 🧭 Mental model

Compose animations are **state‑driven**:

*   You describe **target state**; the runtime animates values **between current and target**.
*   You pick an **AnimationSpec** (e.g., `spring`, `tween`, `keyframes`, `snap`).
*   The framework drives frames and recomposes only the parts that read the animated value.

***

## 1) **Implicit animations** (simple & powerful)

Use when you just need to animate between two values based on state changes.

### `animate*AsState` (scalar and common types)

```kotlin
@Composable
fun FavoriteIcon(isFavorite: Boolean) {
    val tint by animateColorAsState(
        targetValue = if (isFavorite) Color.Red else Color.Gray,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
    )
    Icon(Icons.Default.Favorite, contentDescription = null, tint = tint)
}
```

Other variants include `animateFloatAsState`, `animateDpAsState`, `animateIntOffsetAsState`, `animateSizeAsState`, etc.

### `animateContentSize()` (size changes without manual measuring)

```kotlin
@Composable
fun ExpandableCard(expanded: Boolean) {
    Column(
        Modifier
            .fillMaxWidth()
            .animateContentSize()     // ⬅️ animates layout size changes
            .background(Color(0xFFEEEEEE))
            .padding(16.dp)
    ) {
        Text("Title")
        if (expanded) {
            Text("More content shown...")
        }
    }
}
```

### `AnimatedVisibility` (show/hide with enter/exit)

```kotlin
@Composable
fun Banner(show: Boolean) {
    AnimatedVisibility(
        visible = show,
        enter = fadeIn() + slideInVertically(),
        exit  = fadeOut() + slideOutVertically()
    ) {
        Text("Network restored", Modifier.background(Color(0xFFDCFCE7)))
    }
}
```

### `Crossfade`

```kotlin
@Composable
fun StatusContent(status: Status) {
    Crossfade(targetState = status, label = "status") { s ->
        when (s) {
            Status.Loading -> CircularProgressIndicator()
            Status.Error   -> Text("Error")
            Status.Ready   -> Text("Ready")
        }
    }
}
```

***

## 2) **Grouped state animations** (`Transition`)

Use when **multiple properties** should animate **together** based on the **same target state**.

```kotlin
@Composable
fun RecordingIndicator(isRecording: Boolean) {
    val transition = updateTransition(targetState = isRecording, label = "record")

    val scale by transition.animateFloat(label = "scale") { rec -> if (rec) 1.2f else 1.0f }
    val color by transition.animateColor(label = "color") { rec -> if (rec) Color.Red else Color.Gray }

    Box(
        Modifier
            .size(24.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .background(color, CircleShape)
    )
}
```

**When to prefer:** If you’d otherwise chain several `animate*AsState` for one state, `updateTransition` is cleaner and ensures synchronization.

***

## 3) **Content transitions** (`AnimatedContent`)

Animate between different content for a single state, with size transforms.

```kotlin
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CounterAnimated(count: Int) {
    AnimatedContent(
        targetState = count,
        transitionSpec = {
            slideInVertically { it } + fadeIn() togetherWith
            slideOutVertically { -it } + fadeOut()
        }, label = "counter"
    ) { value ->
        Text("Count: $value", style = MaterialTheme.typography.headlineMedium)
    }
}
```

***

## 4) **Imperative/low‑level control** (`Animatable`)

Use when you need **manual control** over timing, interruption, velocity, or to **chain** animations. Works with coroutines.

```kotlin
@Composable
fun DragChip() {
    val offset = remember { Animatable(0f) }

    Box(
        Modifier
            .offset { IntOffset(offset.value.roundToInt(), 0) }
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    // Directly update during gesture
                    offset.snapTo((offset.value + delta).coerceIn(0f, 600f))
                },
                onDragStopped = { velocity ->
                    // Fling back using decay
                    val decay = rememberSplineBasedDecay<Float>()
                    val target = decay.calculateTargetValue(offset.value, velocity)
                    // Bound and animate
                    val clampedTarget = target.coerceIn(0f, 600f)
                    LaunchedEffect(Unit) {
                        offset.animateDecay(velocity, decay)   // or animateTo(clampedTarget, spring())
                    }
                }
            )
            .size(64.dp)
            .background(Color(0xFFBBDEFB), CircleShape)
    )
}
```

**Why:** Full control—`snapTo`, `animateTo`, `animateDecay`, interruption, query current velocity, etc.

***

## 5) **Infinite / repeating animations**

```kotlin
@Composable
fun DotsPulsing() {
    val infinite = rememberInfiniteTransition(label = "pulse")
    val alpha by infinite.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    Box(Modifier.size(12.dp).background(Color.Black.copy(alpha), CircleShape))
}
```

***

## 6) **AnimationSpec: pick the right physics or timing**

*   **`spring()`**: physics‑based (dampingRatio, stiffness). Great for material feel & gesture follow‑through.
*   **`tween()`**: duration + easing curve (e.g., `FastOutSlowInEasing`).
*   **`keyframes`**: multi‑stage timings (e.g., bounce or emphasize).
*   **`snap()`**: instant jump (sometimes useful for sync).
*   **`repeatable` / `infiniteRepeatable`**: loops with repeat mode.

Example with spring:

```kotlin
val scale by animateFloatAsState(
    targetValue = if (pressed) 0.96f else 1f,
    animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)
)
```

***

## 7) **Gesture + animation interplay**

Combine gesture states (`draggable`, `swipeable`, `pointerInput`) with `Animatable` or `animate*AsState`:

*   While dragging → update with `snapTo`.
*   On release → `animateTo` or `animateDecay` to settle.
*   For swipe-to-dismiss, consider `swipeableV2` (or libraries) with anchors & thresholds.

***

## 8) **Layout and placement animations**

*   **`animateItemPlacement()`** for re‑ordering items in `LazyColumn`:
    ```kotlin
    LazyColumn {
        items(items, key = { it.id }) { item ->
            Row(Modifier.animateItemPlacement()) { Text(item.title) }
        }
    }
    ```
*   **Lookahead/Animated placement** (advanced): `LookaheadScope` / `animateBounds` (if you need predictive size/position changes with smooth morphs).

***

## 9) **Navigation transitions**

*   With Navigation‑Compose, you can use `AnimatedContent` per destination, or a navigation animator lib (e.g., Accompanist’s navigation‑animation) to add enter/exit transitions between routes.
*   Keep navigation effects **as one‑off events**; trigger transitions declaratively by destination changes.

***

## 10) **Performance tips**

*   Prefer **implicit animations** when possible—they integrate with recomposition efficiently.
*   Keep animated values **local** to the composable using them; avoid lifting them too high.
*   Use **immutable/stable** parameters to help skipping.
*   For lists, **provide keys** and avoid heavy work in item lambdas; precompute or use `derivedStateOf`.
*   Avoid blocking work in animation callbacks; everything runs on the main thread.
*   Measure with **Layout Inspector**, **Recomposition Counts**, and **Macrobenchmark** for jank.

***

## 11) **Common pitfalls (and fixes)**

1.  **Starting animations in the composable body**  
    ✅ Use `LaunchedEffect` or `Animatable` inside a coroutine.

2.  **Using unstable keys in effects**  
    ✅ Keep keys stable; or use `LaunchedEffect(Unit)` when you truly want “run once while mounted”.

3.  **Mixing state reads across the tree** causing broad recompositions  
    ✅ Keep animated state local to the smallest sub-tree.

4.  **Navigation/toasts triggered by animated state repeatedly**  
    ✅ Use `SharedFlow` one‑off effects and collect in `LaunchedEffect`.

***

## Putting it together: Example—FAB that expands into a sheet

```kotlin
@Composable
fun FabToSheet(expanded: Boolean, onToggle: () -> Unit) {
    val corner by animateDpAsState(if (expanded) 0.dp else 28.dp, label = "corner")
    val height by animateDpAsState(if (expanded) 280.dp else 56.dp, label = "height")
    val width  by animateDpAsState(if (expanded) 360.dp else 56.dp, label = "width")

    Box(
        Modifier
            .size(width, height)
            .clip(RoundedCornerShape(corner))
            .background(MaterialTheme.colorScheme.primary)
            .clickable { onToggle() }
            .padding(16.dp)
    ) {
        AnimatedContent(
            targetState = expanded,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "fabContent"
        ) { isExpanded ->
            if (isExpanded) SheetContent() else Icon(Icons.Default.Add, contentDescription = null)
        }
    }
}
```

***

## Interview‑ready summary

> “I handle animations in Compose by driving them from state. For simple transitions I use implicit APIs like `animate*AsState`, `AnimatedVisibility`, and `animateContentSize`. When multiple properties must animate together I use `updateTransition` or `AnimatedContent`. For gesture‑driven or finely controlled sequences I use `Animatable` with coroutines (`animateTo`, `snapTo`, `animateDecay`). I pick an appropriate `AnimationSpec` (spring/tween/keyframes), keep animated state local to minimize recomposition, and validate performance with tooling. For lists, I use `animateItemPlacement`; for navigation, destination‑based transitions or a navigation animation library.”

***

  </details>

</details>

<details>
  <summary><b>📂 Theming</b></summary>

  <details style="margin-left: 20px;">
    <summary><i>📄 1. How does Material3 theming work in Compose?</i></summary>

***

# ✅ How does Material 3 theming work in Jetpack Compose?

## ✅ Strong Interview Opening (Use This)

> In Jetpack Compose, **Material 3 theming** is based on a centralized **theme hierarchy** that provides **colors, typography, and shapes** through **CompositionLocals**. These values are consumed automatically by Material components, ensuring consistent design, dynamic color support, and easy light/dark theming.

That already signals senior understanding.

***

## 1️⃣ What is Material 3 in Compose?

**Material 3 (Material You)** is Google’s latest design system that focuses on:

*   Personalized colors (dynamic color)
*   Better accessibility (contrast, typography)
*   Modern component styling
*   Large color surfaces and tonal palettes

In Compose, Material 3 is provided via:

```kotlin
implementation("androidx.compose.material3:material3")
```

***

## 2️⃣ The Core Structure of Material 3 Theme

At a high level, **MaterialTheme** has **three pillars**:

```kotlin
MaterialTheme(
    colorScheme = ...,
    typography = ...,
    shapes = ...
) {
    // App UI
}
```

### Theme hierarchy:

    MaterialTheme
     ├─ colorScheme
     ├─ typography
     └─ shapes

✅ These values are stored in **CompositionLocals**  
✅ Any composable inside the theme can access them

🎯 Interview line:

> “MaterialTheme works by injecting design tokens into the Compose tree using CompositionLocals.”

***

## 3️⃣ ColorScheme (Material 3 Colors)

Material 3 uses **ColorScheme** instead of Material 2’s `Colors`.

### Example:

```kotlin
val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6750A4),
    onPrimary = Color.White,
    secondary = Color(0xFF625B71),
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    error = Color(0xFFB3261E)
)
```

### Accessing colors:

```kotlin
val color = MaterialTheme.colorScheme.primary
```

### Usage in components:

```kotlin
Button(
    colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary
    )
) { Text("Submit") }
```

***

## 4️⃣ Typography (Material 3)

Material 3 defines **semantic text styles**:

```kotlin
Typography(
    bodyLarge = TextStyle(fontSize = 16.sp),
    titleLarge = TextStyle(fontSize = 22.sp),
    headlineMedium = TextStyle(fontSize = 28.sp)
)
```

### Usage:

```kotlin
Text(
    text = "Hello",
    style = MaterialTheme.typography.titleLarge
)
```

✅ Component defaults automatically pick the correct text style  
✅ Ensures accessibility and consistency

🎯 Interview phrase:

> “Typography in Material 3 is semantic, not visual, which improves consistency and scalability.”

***

## 5️⃣ Shapes (Material 3)

Shapes define **corner styling** for components.

```kotlin
val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(24.dp)
)
```

Used automatically by:

*   Button
*   Card
*   Surface
*   Dialog

```kotlin
Card(
    shape = MaterialTheme.shapes.medium
) { ... }
```

***

## 6️⃣ Dynamic Color (Material You – Android 12+)

One of the **biggest Material 3 features**.

```kotlin
@Composable
fun AppTheme(
    useDynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme =
        if (useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            dynamicLightColorScheme(LocalContext.current)
        } else {
            LightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
```

✅ Automatically adapts to user wallpaper  
✅ System‑aware personalization

🎯 Interview sentence:

> “Material 3 supports dynamic color out of the box using the system’s wallpaper on Android 12+.”

***

## 7️⃣ How MaterialTheme Works Internally (High-Level)

*   `MaterialTheme` sets values in **CompositionLocal**
*   These locals are read by:
    *   Buttons
    *   Text
    *   Surfaces
    *   Icons
*   When theme values change → **recomposition happens automatically**

```kotlin
MaterialTheme.colorScheme.primary
```

✅ No manual propagation  
✅ Declarative theming

***

## 8️⃣ Light / Dark Theme Switching

Compose reacts automatically to system dark mode.

```kotlin
val darkTheme = isSystemInDarkTheme()

MaterialTheme(
    colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
) { ... }
```

✅ Recomposition triggers  
✅ All components update instantly

🎯 Interview quote:

> “Theme changes in Compose are declarative and trigger recomposition, not manual UI updates.”

***

## 9️⃣ Custom Theme Extensions (Advanced / Interview Gold)

You can extend the theme using **custom CompositionLocals**:

```kotlin
data class Spacing(
    val small: Dp = 4.dp,
    val medium: Dp = 8.dp,
    val large: Dp = 16.dp
)

val LocalSpacing = compositionLocalOf { Spacing() }
```

```kotlin
@Composable
fun AppTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalSpacing provides Spacing()
    ) {
        MaterialTheme { content() }
    }
}
```

Usage:

```kotlin
val spacing = LocalSpacing.current.medium
```

🎯 Interview line:

> “We can extend MaterialTheme using CompositionLocals for app‑specific design tokens.”

***

## 10️⃣ The Role of Surface in Theming

`Surface` is a **theme‑aware container**:

```kotlin
Surface(
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 4.dp
) {
    Text("Content")
}
```

✅ Applies:

*   Background color
*   Elevation overlays
*   Content color automatically

🎯 Interview line:

> “Surface is the foundation of Material theming in Compose.”

***

## 11️⃣ Common Mistakes (Mention Briefly)

❌ Hardcoding colors instead of using `colorScheme`  
❌ Not using `Surface` for background  
❌ Mixing Material 2 and Material 3 components  
❌ Ignoring dynamic color support  
❌ Putting theme logic inside screens instead of app root

***

## 🔥 Interview‑Ready Final Summary (Polished)

> “Material 3 theming in Compose works through the `MaterialTheme` composable, which provides a `ColorScheme`, `Typography`, and `Shapes` using CompositionLocals. These values are automatically consumed by Material components and recomposed when changed. Material 3 supports dynamic color on Android 12+, semantic typography, and modern tonal surfaces. Themes are declarative, lifecycle‑aware, and easily extensible using custom CompositionLocals.”

***

## ✅ One‑Line Closing (Very Strong)

> *“Material 3 in Compose turns design tokens into reactive, app‑wide state.”*

***

  </details>

  <details style="margin-left: 20px;">
    <summary><i>📄 2. How do you support dark mode dynamically?</i></summary>

***

## 🎯 Goals

1.  **Follow system theme** (default)
2.  **Let users override** (Light / Dark / System)
3.  **Apply instantly at runtime** (no Activity restart)
4.  **Support Material 3 Dynamic Color** on Android 12+
5.  **Persist preference** (e.g., with DataStore)

***

## 1) Theme scaffold (Material 3 + dynamic color)

Create a single theme entry that can toggle **dark** and **dynamic color** at runtime:

```kotlin
@Composable
fun AppTheme(
    darkTheme: Boolean,                 // controlled by system/user
    dynamicColor: Boolean = true,       // enable/disable dynamic color
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> darkColorScheme()
        else -> lightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
```

> **Tip:** Use your own `lightColorScheme()` / `darkColorScheme()` for brand colors; fall back to them when dynamic color is not available (Android < 12).

***

## 2) Follow system by default

Compose exposes the system dark setting via `isSystemInDarkTheme()`:

```kotlin
@Composable
fun AppRoot() {
    val darkFromSystem = isSystemInDarkTheme()
    AppTheme(darkTheme = darkFromSystem) {
        // App UI
    }
}
```

This alone will switch with the system (e.g., when user toggles Android Dark theme) and recomposes the UI immediately.

***

## 3) Add a user override (Light / Dark / System)

Model a **ThemeSetting** and persist it (e.g., in **DataStore**). Keep the setting in a `ViewModel` for the app root.

```kotlin
enum class ThemeSetting { SYSTEM, LIGHT, DARK }
```

**ViewModel (single source of truth):**

```kotlin
class ThemeViewModel(
    private val repo: ThemeRepository // wraps DataStore
) : ViewModel() {

    val themeSetting: StateFlow<ThemeSetting> = repo.themeSetting // SYSTEM/LIGHT/DARK
        .stateIn(viewModelScope, SharingStarted.Eagerly, ThemeSetting.SYSTEM)

    fun setTheme(setting: ThemeSetting) = viewModelScope.launch {
        repo.setTheme(setting)
    }
}
```

**Apply at the root:**

```kotlin
@Composable
fun AppRoot(themeVm: ThemeViewModel = viewModel()) {
    val setting by themeVm.themeSetting.collectAsStateWithLifecycle()
    val darkFromSystem = isSystemInDarkTheme()
    val darkTheme = when (setting) {
        ThemeSetting.SYSTEM -> darkFromSystem
        ThemeSetting.LIGHT  -> false
        ThemeSetting.DARK   -> true
    }
    AppTheme(darkTheme = darkTheme, dynamicColor = true) {
        // ... UI (provide a toggle UI somewhere)
    }
}
```

**Toggle UI example:**

```kotlin
@Composable
fun ThemeSelector(current: ThemeSetting, onChange: (ThemeSetting) -> Unit) {
    Row(Modifier.padding(16.dp)) {
        FilterChip(selected = current == ThemeSetting.SYSTEM, onClick = { onChange(ThemeSetting.SYSTEM) }, label = { Text("System") })
        Spacer(Modifier.width(8.dp))
        FilterChip(selected = current == ThemeSetting.LIGHT, onClick = { onChange(ThemeSetting.LIGHT) }, label = { Text("Light") })
        Spacer(Modifier.width(8.dp))
        FilterChip(selected = current == ThemeSetting.DARK, onClick = { onChange(ThemeSetting.DARK) }, label = { Text("Dark") })
    }
}
```

Because theming is provided via `MaterialTheme` **CompositionLocals**, updates propagate instantly through recomposition—no app restart needed.

***

## 4) Status bar & system bars (nice polish)

Use Accompanist System UI Controller to sync system bars with theme (optional but interview‑impressive):

```kotlin
@Composable
fun SyncSystemBars(darkTheme: Boolean) {
    // implementation("com.google.accompanist:accompanist-systemuicontroller:<version>")
    val sys = rememberSystemUiController()
    val bg = MaterialTheme.colorScheme.background
    SideEffect {
        sys.setSystemBarsColor(
            color = bg,
            darkIcons = !darkTheme // true when light theme to show dark icons
        )
    }
}
```

Call this from your app root inside `AppTheme`.

***

## 5) AMOLED true black (optional user preference)

Some apps offer **true black** for dark mode to save battery:

```kotlin
@Composable
fun AppTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    trueBlack: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val base = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        darkTheme -> darkColorScheme()
        else -> lightColorScheme()
    }
    val colorScheme = if (darkTheme && trueBlack)
        base.copy(background = Color.Black, surface = Color.Black)
    else base

    MaterialTheme(colorScheme = colorScheme, typography = AppTypography, shapes = AppShapes, content = content)
}
```

***

## 6) Interop with View system

If parts of your app still use Views/AppCompat:

*   Use **DayNight** theme in XML (`Theme.Material3.DayNight`).
*   For programmatic override (Views world):  
    `AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM / MODE_NIGHT_YES / MODE_NIGHT_NO)`  
    Compose will still follow your top‑level decision if you **don’t** use this; prefer a single source of truth to avoid conflicts.

***

## 7) Persistence with DataStore (sketch)

```kotlin
class ThemeRepository(private val dataStore: DataStore<Preferences>) {
    private val KEY = stringPreferencesKey("theme_setting")

    val themeSetting: Flow<ThemeSetting> = dataStore.data
        .map { prefs -> prefs[KEY]?.let { runCatching { ThemeSetting.valueOf(it) }.getOrNull() } ?: ThemeSetting.SYSTEM }

    suspend fun setTheme(setting: ThemeSetting) {
        dataStore.edit { it[KEY] = setting.name }
    }
}
```

This ensures the user override survives app restarts and process death.

***

## 8) Common pitfalls & fixes

*   **Hardcoding colors** → Always pull from `MaterialTheme.colorScheme` (respects dark/dynamic).
*   **Multiple theme sources** → Keep a **single source of truth** (ViewModel + Composition root).
*   **Forgetting system bars** → Sync status/navigation bar icon colors for readability.
*   **Ignoring dynamic color** → On Android 12+, it’s a free win. Provide a toggle to opt‑out if your brand requires strict colors.
*   **Applying theme deep in the tree** → Wrap the entire app (or top nav graph) with one `MaterialTheme` so changes propagate predictably.

***

## 9) Quick checklist (for interviews)

*   ✅ Use `isSystemInDarkTheme()` as default
*   ✅ Allow user override (System / Light / Dark) via `ViewModel + DataStore`
*   ✅ Provide dynamic color on Android 12+ (`dynamicLightColorScheme` / `dynamicDarkColorScheme`)
*   ✅ Update `MaterialTheme` at the root; recompose for instant effect
*   ✅ Polish: System bars via Accompanist; optional **true black** toggle

***

## TL;DR (what to say in one breath)

> “I support dark mode dynamically by centralizing theme state in a ViewModel (System/Light/Dark), defaulting to `isSystemInDarkTheme()`, and applying it at the app root via `MaterialTheme`. On Android 12+ I enable dynamic color with `dynamicLight/DarkColorScheme`. The preference is persisted with DataStore, updates recompose the UI instantly, and I synchronize system bars for readability. For brand or AMOLED needs, I add a ‘true black’ option by overriding surface/background in dark mode.”

***

  </details>

  <details style="margin-left: 20px;">
    <summary><i>📄 3. Tell me something about the manage navigations in Compose screens ?</i></summary>

***

## 🧭 Core Idea

Compose uses **Navigation‑Compose** to declare destinations and transitions in code:

*   A **`NavHost`** defines your navigation graph.
*   A **`NavController`** manages the **back stack** of destinations.
*   You navigate using **string routes** (optionally parameterized), and you can structure graphs into **nested graphs** (tabs, feature flows).

***

## ⚙️ Minimal Setup

```kotlin
@Composable
fun App() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onOpenDetails = { id ->
                    navController.navigate("details/$id")
                }
            )
        }

        composable(
            route = "details/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getLong("id")
            DetailsScreen(id = id, onBack = { navController.popBackStack() })
        }
    }
}
```

**Key pieces to mention in interviews:**

*   `rememberNavController()` creates the controller and persists it across recompositions.
*   `NavHost` declaratively defines destinations.
*   Use `navArgument` for typed params.

***

## 🧱 Navigation Building Blocks

### 1) **Routes & Arguments**

*   Routes can be plain (`"home"`) or **parameterized** (`"details/{id}"`).
*   Use `NavType` for type safety (Int/Long/Bool/String/Parcelable/Enum, custom types via `NavType` or Kotlinx Serialization when using the appropriate integration).

### 2) **Nested Graphs**

Group destinations by feature or flow:

```kotlin
navigation(startDestination = "feed/list", route = "feed") {
    composable("feed/list") { /* ... */ }
    composable("feed/post/{postId}") { /* ... */ }
}
```

Great for **bottom navigation** or **wizard flows**.

### 3) **Back Stack & Pop**

*   `navController.popBackStack()` pops one.
*   `popUpTo(route) { inclusive = true }` clears up to a node (e.g., after login).

```kotlin
navController.navigate("home") {
    popUpTo("login") { inclusive = true }
    launchSingleTop = true
}
```

***

## 🧭 Bottom Navigation (state‑preserving)

A common production requirement is retaining state per tab.

```kotlin
@Composable
fun BottomBarApp() {
    val navController = rememberNavController()
    val items = listOf("home", "search", "profile")

    Scaffold(
        bottomBar = {
            NavigationBar {
                val current by navController.currentBackStackEntryFlow
                    .collectAsState(initial = navController.currentBackStackEntry)
                val currentRoute = current?.destination?.route
                items.forEach { route ->
                    NavigationBarItem(
                        selected = currentRoute == route,
                        onClick = {
                            navController.navigate(route) {
                                // Reuse destination if already on top
                                launchSingleTop = true
                                // Save/restore scroll & state per tab
                                restoreState = true
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                            }
                        },
                        icon = { Icon(Icons.Default.Home, null) },
                        label = { Text(route) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {
            composable("home") { Home() }
            composable("search") { Search() }
            composable("profile") { Profile() }
        }
    }
}
```

**Why this matters:** `saveState` + `restoreState` keeps scroll position / filters per tab—**no reloading**.

***

## 🔄 Passing Data (Args, Results, Shared VM)

### 1) **Arguments in route**

*   Simple/primitive args via `navArgument` (shown above).
*   For complex data: prefer passing **IDs** and re‑loading in the destination (scales better).

### 2) **Returning results**

*   Use `SavedStateHandle` on the **previous back stack entry**.

```kotlin
// From child
navController.previousBackStackEntry
    ?.savedStateHandle?.set("result", true)
navController.popBackStack()

// In parent
val savedStateHandle = navController.currentBackStackEntry!!.savedStateHandle
val result = savedStateHandle.getLiveData<Boolean>("result")
result.observe(lifecycleOwner) { /* handle once, then remove if needed */ }
```

Or use a shared `ViewModel` scoped to the parent graph.

### 3) **Shared ViewModel**

Scope VM to a **navigation graph** so both screens access the same instance:

```kotlin
val parentEntry = remember(navController) {
    navController.getBackStackEntry("checkoutGraph")
}
val sharedVm: CheckoutViewModel = hiltViewModel(parentEntry)
```

***

## 🧬 ViewModel Scoping (very interview‑relevant)

*   `hiltViewModel()` resolves a ViewModel tied to the **current back stack entry**; it survives configuration changes and is cleared when the destination is popped.
*   For nested graphs, use the **graph’s** back stack entry to share VMs across screens within that graph.

**Why this matters:** It keeps **state ownership correct** and avoids memory leaks or accidental re‑creation.

***

## 🔗 Deep Links

Support launching directly into a destination:

```kotlin
composable(
    route = "details/{id}",
    deepLinks = listOf(navDeepLink { uriPattern = "app://item/{id}" })
) { /* ... */ }
```

*   OS or external intents can open your screen.
*   Combine with **app links** for HTTPS links.

***

## 🎭 Transitions & Animations

*   **Basic**: Use animated APIs like `AnimatedContent` **inside** screens for local transitions.
*   **Between destinations**: Use **navigation animation** artifacts (e.g., Accompanist’s `navigation-animation`) to define `enter/exit` transitions on destinations.
*   Remember: Don’t block the UI thread; keep transitions fast.

***

## 🪟 Dialogs, Bottom Sheets as Destinations

You can treat dialogs/sheets as destinations for clean back handling:

*   **Material3 Dialog**: navigate to a dialog route and call `popBackStack()` on dismiss.
*   **ModalBottomSheet**: similar pattern; manage with a `SheetState` and pop on hide.

This keeps **system back** behavior consistent via the **NavController**.

***

## 🔙 Back Handling

*   Use **`BackHandler`** to intercept back presses for custom behavior (e.g., confirm exit, collapse search).
*   Always **fall back** to `navController.popBackStack()` when appropriate.

```kotlin
BackHandler(enabled = isSheetOpen) {
    closeSheet()
}
```

***

## 🧼 Process Death & State

*   Keep business state in **ViewModel**; it is **retained** across config changes and recreated after process death.
*   Use `SavedStateHandle` for small/restorable UI state (filters, query, selected tab).
*   Avoid passing large objects via nav arguments—pass IDs, reload from repository/cache.

***

## 🧪 Testing Navigation

*   Use `createAndroidComposeRule<YourActivity>()` and verify:
    *   Destination text/semantics present after navigation.
    *   Back stack pops correctly.
*   For ViewModels, inject fakes via Hilt test components.

***

## 🧯 Common Pitfalls (and fixes)

1.  **Using `Column + verticalScroll` for long lists instead of `LazyColumn`**  
    → Leads to jank when navigating back (heavy recomposition). Use `LazyColumn`.

2.  **Not providing `launchSingleTop` / `restoreState` for bottom tabs**  
    → Creates multiple copies or loses scroll state.

3.  **Passing large objects in route**  
    → Use IDs and reload; or scope a shared ViewModel to the graph.

4.  **Placing `NavController` inside ViewModel**  
    → Avoid; NavController is a UI concern. Expose **events/effects** and let UI navigate.

5.  **Navigation on recomposition**  
    → Causes multiple navigations. Handle **one‑off events** from ViewModel via `SharedFlow` collected in `LaunchedEffect`.

***

## 🧩 Putting It Together (Pattern You Can Quote)

```kotlin
@Composable
fun AppRoot() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "auth") {

        navigation(startDestination = "login", route = "auth") {
            composable("login") {
                val vm: LoginVM = hiltViewModel()
                val effects = vm.effects.collectAsState(initial = null)
                LaunchedEffect(effects.value) {
                    when (effects.value) {
                        is LoginEffect.NavigateHome -> {
                            navController.navigate("home") {
                                popUpTo("auth") { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                        null -> Unit
                    }
                }
                LoginScreen(onSignup = { navController.navigate("signup") })
            }
            composable("signup") { SignupScreen(onBack = { navController.popBackStack() }) }
        }

        navigation(startDestination = "dashboard", route = "home") {
            composable("dashboard") { Dashboard(/* ... */) }
            composable("details/{id}",
                arguments = listOf(navArgument("id") { type = NavType.LongType })
            ) { backStack ->
                DetailsScreen(id = backStack.arguments!!.getLong("id"))
            }
        }
    }
}
```

**Highlights to narrate:**

*   Auth graph pops itself after login.
*   One‑off navigation via `effects` + `LaunchedEffect`.
*   Typed arg in details.
*   Single‑activity, nested graphs, clean back stack.

***

## 🎯 Interview‑ready Summary

> “In Compose, I manage navigation with Navigation‑Compose: a `NavHost` declares the graph and a `NavController` manages the back stack. I use typed routes with arguments, nested graphs for features and bottom tabs, and control back stack with `popUpTo`, `inclusive`, and `launchSingleTop`. I keep screen state in ViewModels scoped to destinations or graphs, pass IDs instead of heavy objects, return results via `SavedStateHandle`, and handle one‑off navigation events with a `SharedFlow` collected in `LaunchedEffect`. For polish, I support deep links, state‑preserving bottom navigation, and (optionally) destination transitions. All of this stays declarative and lifecycle‑aware.”

***

  </details>

</details>

<details>
  <summary><b>📂 Testing</b></summary>

  <details style="margin-left: 20px;">
    <summary><i>📄 1. How do you test Jetpack Compose UI?</i></summary>

***

## 🧭 Testing strategy (the “pyramid” for Compose)

1.  **Unit tests** – ViewModels, use-cases, reducers (plain JVM, fast).
2.  **UI tests (Compose)** – Verify UI behavior via semantics (JVM via Robolectric) or Instrumentation on device/emulator.
3.  **Integration/E2E** – Navigation flows, DI, network fakes on device.
4.  **Performance** – Macrobenchmark for scroll jank, startup, animations.

> Compose encourages **state‑driven UI**—so most logic should be unit‑tested in ViewModels; UI tests verify **state ↔ UI rendering** and **user interactions**.

***

## ✅ The essentials: Compose UI Test APIs

Add test deps (Gradle):

```kotlin
androidTestImplementation("androidx.compose.ui:ui-test-junit4:<version>")
debugImplementation("androidx.compose.ui:ui-test-manifest:<version>") // needed for instrumentation
testImplementation("androidx.compose.ui:ui-test-junit4:<version>") // for Robolectric JVM tests
```

Choose a rule:

```kotlin
@get:Rule val composeTestRule = createComposeRule()                 // JVM (Robolectric) or host-side
// or
@get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>() // Instrumentation
```

Set content under test:

```kotlin
@Test
fun showsUserName() = composeTestRule.run {
    setContent {
        MyAppTheme { Profile(name = "Harshal") }
    }
    onNodeWithText("Harshal").assertIsDisplayed()
}
```

**Key APIs:**

*   **Finding nodes**: `onNodeWithText`, `onNodeWithTag`, `onAllNodes(...)`, `hasClickAction()`, `hasContentDescription()`, `hasAnySibling(...)`
*   **Actions**: `performClick()`, `performTextInput("...")`, `performScrollTo()`, `performScrollToNode(...)`, `performImeAction()`
*   **Assertions**: `assertIsDisplayed()`, `assertTextEquals()`, `assertHasClickAction()`, `assertContentDescriptionEquals()`
*   **Tree control**: `useUnmergedTree = true` to inspect deeper than merged semantics.

Give components **test tags**:

```kotlin
Box(Modifier.testTag("USER_AVATAR"))
// test
onNodeWithTag("USER_AVATAR").assertExists()
```

***

## 🔁 Interactions, scrolling & lists

**Text input & IME:**

```kotlin
onNodeWithText("Email").performTextInput("harshal@tcs.com")
onNodeWithText("Submit").performClick()
```

**LazyColumn scroll to item:**

```kotlin
onNodeWithTag("USERS_LIST")
    .performScrollToNode(hasText("Kamala Manora"))
    .assertExists()
```

**Scroll to index (compose-foundation 1.6+ helpers exist in some libs)**
If not available, search by node with `performScrollToNode`.

***

## ⏱️ Idling & animation control (MainClock)

Compose tests have a **test clock** to control time and animations.

```kotlin
composeTestRule.mainClock.autoAdvance = false

// Trigger animated change
onNodeWithTag("EXPAND_BTN").performClick()

// Advance frames/time
composeTestRule.mainClock.advanceTimeBy(300) // ms
onNodeWithText("Expanded content").assertIsDisplayed()
```

**Wait for coroutines/compose to idle:**

```kotlin
composeTestRule.waitForIdle()
composeTestRule.waitUntil(timeoutMillis = 5_000) { /* condition */ }
```

Use `runOnIdle { ... }` to read/write state safely on the UI thread.

***

## 🧩 Test structure: stateless UI + VM

**Stateless Composable** (easy to test):

```kotlin
@Composable
fun LoginContent(
    email: String,
    password: String,
    loading: Boolean,
    error: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit
) { /* ... */ }
```

**Test the pure UI**:

```kotlin
@Test fun showsErrorWhenProvided() = composeTestRule.run {
    setContent {
        LoginContent(
            email = "", password = "", loading = false, error = "Invalid",
            onEmailChange = {}, onPasswordChange = {}, onLogin = {}
        )
    }
    onNodeWithText("Invalid").assertIsDisplayed()
}
```

**ViewModel** unit test separately\*\* (JVM, Turbine for flows):

```kotlin
class LoginViewModelTest {
    @Test fun successfulLoginEmitsState() = runTest {
        val repo = FakeAuthRepo(success = true)
        val vm = LoginViewModel(repo)
        vm.onEmailChange("a@b.com"); vm.onPasswordChange("123456")
        vm.onLoginClick()
        assertTrue(vm.uiState.first { it.loggedIn }.loggedIn)
    }
}
```

**Integration test**: Compose screen + real VM with fake repo.

***

## 🧭 Navigation testing

Use `TestNavHostController` to verify navigation:

```kotlin
@get:Rule val composeRule = createAndroidComposeRule<ComponentActivity>()

@Test
fun navigatesToDetails() = with(composeRule) {
    val navController = TestNavHostController(activity)
    setContent {
        navController.navigatorProvider.addNavigator(ComposeNavigator())
        AppNavHost(navController = navController)
    }
    onNodeWithText("Open Details").performClick()
    assertEquals("details/{id}", navController.currentDestination?.route)
}
```

Use **one‑off effects** (SharedFlow) + `LaunchedEffect` for navigation; in tests, assert the destination change or the effect emission.

***

## 🧪 Accessibility & semantics

Compose semantics make a11y testable by default.

*   **Labels**: `contentDescription`
*   **Roles**: `Role.Button`, `Role.Switch`
*   **State**: `ToggleableState`, `ProgressBarRangeInfo`

Assertions:

```kotlin
onNode(hasContentDescription("Profile picture")).assertExists()
onNode(hasClickAction() and hasText("Continue")).assertIsDisplayed()
```

Check **merged vs unmerged tree**:

```kotlin
onNode(hasText("Title"), useUnmergedTree = true).assertExists()
```

***

## 🖼️ Screenshot / pixel assertions (optional)

*   **Compose bitmap capture** (debug/local assertions):
    ```kotlin
    val image = onNodeWithTag("CARD").captureToImage()
    // Assert pixel colors or save for diffing
    ```
*   **Paparazzi** (JVM screenshot tests) – no emulator required (great for theming/diff checks).
*   **Shot** or other screenshot libraries (instrumentation) if your team prefers device-baseline diffs.

***

## ⚡ Performance & stability testing

*   **Macrobenchmark** (module `:benchmark`):
    *   **Startup** (cold/warm), **scroll jank**, **interaction traces**
    *   Works on physical device; integrates with Perfetto/Studio
*   **Recomposition counts** (debug builds): enable highlighters to find hot spots (for development, not in tests).

***

## 🧯 Common pitfalls (and fixes)

1.  **Flaky tests from async work**  
    Use `waitForIdle()`, `waitUntil { condition }`, or control `mainClock`. Ensure your async tasks are bound to test dispatchers.

2.  **Missing test tags / semantics**  
    Add `Modifier.testTag("...")` and set content descriptions/roles.

3.  **Testing only happy paths**  
    Cover loading, empty, error, and edge cases (e.g., long names, RTL).

4.  **Navigation side-effects repeating**  
    Use one-off **effects** (SharedFlow) and collect in UI; in tests, assert once.

5.  **Over-mocking UI**  
    Keep UI stateless and focus tests on user-visible behavior; move logic to VM for unit tests.

***

## 🛠️ Example: End-to-end Compose UI test

```kotlin
@get:Rule val rule = createAndroidComposeRule<ComponentActivity>()

@Test
fun loginFlow_success_navigatesHome() = with(rule) {
    setContent {
        val navController = TestNavHostController(activity).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
        }
        AppNavHost(navController, authRepo = FakeAuthRepo(success = true))
    }

    onNodeWithText("Email").performTextInput("user@bank.com")
    onNodeWithText("Password").performTextInput("pass1234")
    onNodeWithText("Login").performClick()

    // Wait until Home visible
    waitUntil(5_000) {
        onAllNodesWithText("Welcome Home").fetchSemanticsNodes().isNotEmpty()
    }
    onNodeWithText("Welcome Home").assertIsDisplayed()
}
```

***

## 🧩 Robolectric vs Instrumentation: when to use which

*   **Robolectric (JVM)**: fast feedback for pure Compose (no system services or hardware needed); great for most **component tests**.
*   **Instrumentation (device/emulator)**: required for system behaviors (Animations realistically, system UI, camera/storage, performance/jank, real IME).

A healthy suite uses **both**.

***

## 📋 Quick checklist (for interviews)

*   Use `createComposeRule()` / `createAndroidComposeRule<Activity>()`
*   Provide `testTag`s and meaningful semantics
*   Control time with `mainClock` for animations
*   Use `waitForIdle()` / `waitUntil` for async stability
*   Test lists with `performScrollToNode`
*   Test navigation with `TestNavHostController`
*   Unit test ViewModels with `runTest` + **Turbine** for Flows
*   Consider **Paparazzi** for screenshots and **Macrobenchmark** for performance

***

## 🔥 30‑second summary (say this)

> “I test Compose UI by driving it through the semantics tree: I set content with a Compose test rule, find nodes via text/tags/roles, perform user actions, and assert visible state. I control async and animations using `waitForIdle()` and the test `mainClock`. I keep UIs stateless for easy unit tests of ViewModels, use `TestNavHostController` for navigation flows, and add test tags and content descriptions for stability and accessibility. For performance, I use Macrobenchmark; for visuals, Paparazzi or bitmap capture.”

***

  </details>

  <details style="margin-left: 20px;">
    <summary><i>📄 2. Difference between Unit Test and UI Test</i></summary>

***

# ✅ Difference Between Unit Test and UI Test

## ✅ Short Interview Answer (Start With This)

> **Unit tests** verify individual pieces of business logic in isolation without Android UI dependencies, while **UI tests** verify how the app behaves from a user’s perspective by interacting with the UI and asserting visible outcomes.

***

## 1️⃣ Unit Test – What & Why

### ✅ What is a Unit Test?

A **Unit Test**:

*   Tests a **single unit of code** (function, ViewModel, use‑case)
*   Runs in **isolation**
*   Does **not depend on UI or Android framework**
*   Focuses on **logic correctness**

### ✅ Typical Targets in Android / Compose

*   ViewModels
*   UseCases
*   Reducers / State transformations
*   Validation logic
*   Mappers

***

### ✅ Example: Unit Test (ViewModel)

```kotlin
@Test
fun increment_increasesCounter() {
    val vm = CounterViewModel()

    vm.increment()

    assertEquals(1, vm.count)
}
```

### ✅ Tools Used

*   JUnit
*   Kotlin Test
*   Mockito / MockK
*   Turbine (for Flow)
*   Coroutine Test APIs

### ✅ Characteristics

*   ✅ Very fast (milliseconds)
*   ✅ Runs on JVM
*   ✅ Easy to debug
*   ✅ No emulator/device required

🎯 Interview line:

> “Unit tests validate business logic and state changes in isolation.”

***

## 2️⃣ UI Test – What & Why

### ✅ What is a UI Test?

A **UI Test**:

*   Tests the **app from the user’s point of view**
*   Interacts with **actual UI elements**
*   Verifies **rendering, navigation, and interactions**
*   Runs on **emulator/device** (or Robolectric for JVM UI tests)

### ✅ Typical Targets

*   Screen rendering
*   Button clicks
*   Text input
*   Navigation
*   Error/loading states
*   Accessibility

***

### ✅ Example: UI Test (Compose)

```kotlin
@Test
fun clickingButton_updatesText() {
    composeTestRule.setContent {
        CounterScreen()
    }

    composeTestRule
        .onNodeWithText("Increment")
        .performClick()

    composeTestRule
        .onNodeWithText("Count: 1")
        .assertIsDisplayed()
}
```

### ✅ Tools Used

*   Compose UI Test APIs
*   Espresso (legacy)
*   Compose Test Rule
*   Robolectric (optional)
*   TestNavHostController

### ✅ Characteristics

*   ❌ Slower than unit tests
*   ❌ Needs device/emulator (usually)
*   ✅ Verifies real user experience
*   ✅ Catches wiring/layout/navigation bugs

🎯 Interview line:

> “UI tests ensure that the user‑visible behavior of the app works as expected.”

***

## 3️⃣ Key Differences (Interview Gold Table – Explain Verbally)

| Aspect       | Unit Test                     | UI Test               |
| ------------ | ----------------------------- | --------------------- |
| Focus        | Business logic                | User interaction      |
| Scope        | Small (single class/function) | Large (screens/flows) |
| Dependencies | Mocked / none                 | Real UI components    |
| Speed        | Very fast                     | Slower                |
| Environment  | JVM                           | Device / Emulator     |
| Flakiness    | Low                           | Higher                |
| Debugging    | Easy                          | Harder                |
| Cost         | Low                           | High                  |

***

## 4️⃣ When to Use Unit Test vs UI Test

### ✅ Prefer Unit Tests When:

*   Testing logic/state changes
*   Validating edge cases
*   Checking ViewModel behavior
*   Running tests frequently (CI)

```kotlin
// Business logic → Unit Test
fun calculateTotal(price: Int, tax: Int): Int
```

***

### ✅ Prefer UI Tests When:

*   Verifying user flows
*   Testing navigation
*   Checking accessibility
*   Ensuring UI reacts to state correctly

```kotlin
// User flow → UI Test
Login → Home → Details
```

🎯 Interview line:

> “I test logic with unit tests and behavior with UI tests.”

***

## 5️⃣ Compose‑Specific Perspective (Nice Interview Touch)

### ✅ In Jetpack Compose:

*   UI is **state‑driven**
*   ViewModels produce **UiState**
*   Composables render that state

✅ **Best Practice**:

*   Unit test **ViewModel**
*   UI test the **Composable reacts to UiState**

```kotlin
// Unit Test
assertEquals(
    LoginUiState(error = "Invalid"),
    vm.uiState.value
)

// UI Test
onNodeWithText("Invalid").assertIsDisplayed()
```

***

## 6️⃣ What UI Tests Cannot Replace

UI tests:

*   ❌ Should NOT test business logic deeply
*   ❌ Are slow and expensive
*   ❌ Break easily with UI changes

🎯 Interview wisdom:

> “UI tests confirm correctness, not implementation.”

***

## 7️⃣ Testing Pyramid (Mention This)

```text
      UI Tests        (Few)
  Integration Tests
     Unit Tests      (Many)
```

✅ Most tests should be **Unit Tests**  
✅ UI tests should be **minimal but critical**

***

## 8️⃣ Common Interview Mistakes (Avoid These)

❌ “UI tests are enough”  
✅ No — they are slow, fragile, and expensive

❌ “Unit tests verify UI rendering”  
✅ No — they verify logic, not screens

❌ “Compose eliminates UI testing”  
✅ No — UI behavior must still be validated

***

## 🔥 Interview‑Ready Final Summary

> “Unit tests and UI tests serve different purposes. Unit tests verify business logic in isolation and run fast on the JVM, making them ideal for ViewModels and state handling. UI tests validate user‑visible behavior by interacting with real UI elements and ensure navigation, rendering, and interactions work correctly. In Compose, I unit test ViewModels extensively and add focused UI tests for core user flows, following the testing pyramid.”

***

## ✅ One‑Line Closing (Very Strong)

> *“Unit tests protect logic; UI tests protect user experience.”*

***

  </details>

  <details style="margin-left: 20px;">
    <summary><i>📄 3. What is Robolectric?</i></summary>

***

# ✅ What is Robolectric?

## ✅ Interview‑Ready Definition

> **Robolectric** is a testing framework that allows Android tests to run **directly on the JVM** by providing **shadow implementations** of Android framework classes, eliminating the need for an emulator or physical device.

***

## 🧠 Why Robolectric Exists

### The problem with traditional Android tests:

*   Android framework classes (`Activity`, `Context`, `Intent`, `View`) normally require:
    *   Emulator or real device
    *   Slow instrumentation tests
*   Unit tests on JVM **cannot run Android code** by default

### ✅ Robolectric solves this by:

*   Simulating Android framework behavior on the JVM
*   Replacing Android classes with **Shadow classes**

🎯 Interview line:

> “Robolectric bridges the gap between JVM unit tests and Android framework APIs.”

***

## 🔧 How Robolectric Works (High‑Level)

1.  Runs tests on the **JVM**
2.  Intercepts Android SDK calls
3.  Uses **Shadow classes** to mimic Android behavior
4.  Executes lifecycle methods synchronously

Example:

*   `Activity` → `ShadowActivity`
*   `Looper` → `ShadowLooper`
*   `SharedPreferences` → `ShadowSharedPreferences`

✅ Fast  
✅ No device  
✅ Deterministic behavior

***

## 🧪 Example: Robolectric Test

### Activity test **without emulator**

```kotlin
@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @Test
    fun clickingButton_updatesText() {
        val activity = Robolectric.buildActivity(MainActivity::class.java)
            .setup()
            .get()

        val button = activity.findViewById<Button>(R.id.button)
        val text = activity.findViewById<TextView>(R.id.text)

        button.performClick()

        assertEquals("Clicked", text.text.toString())
    }
}
```

✅ No emulator  
✅ No instrumentation  
✅ Runs in milliseconds

***

## 🧱 Robolectric + Jetpack Compose

Robolectric **can run Compose UI tests** on JVM:

```kotlin
@get:Rule
val composeRule = createComposeRule()

@Test
fun showText() {
    composeRule.setContent {
        Text("Hello Robolectric")
    }

    composeRule.onNodeWithText("Hello Robolectric").assertIsDisplayed()
}
```

✅ Useful for:

*   Fast Compose UI checks
*   CI pipelines
*   Stateless UI tests

***

## 🚀 Robolectric vs Instrumentation Tests

| Aspect            | Robolectric | Instrumentation Test |
| ----------------- | ----------- | -------------------- |
| Runs on           | JVM         | Emulator/Device      |
| Speed             | ✅ Very fast | ❌ Slower             |
| Android framework | Simulated   | Real                 |
| CI friendly       | ✅ Yes       | ❌ Slower             |
| UI realism        | ❌ Limited   | ✅ Full               |
| Animations        | ❌ Limited   | ✅ Real               |

🎯 Interview line:

> “Robolectric is ideal for fast feedback; instrumentation is needed for real device behavior.”

***

## ✅ When to Use Robolectric

### ✅ Good use cases:

*   ViewModel + Android framework logic
*   Compose UI rendering tests
*   SharedPreferences, Intents, Resources
*   Activity/Fragment lifecycle tests
*   Rapid feedback during development

```kotlin
Context.getString()
SharedPreferences.getBoolean()
Intent extras
```

***

## ❌ When NOT to Use Robolectric

Avoid Robolectric for:

*   Camera, Bluetooth, GPS
*   Sensors and hardware features
*   Real performance & animations
*   System UI interactions

🎯 Interview insight:

> “Robolectric simulates Android—it doesn’t replace real device testing.”

***

## 🧩 Robolectric Shadows (Key Feature)

```kotlin
ShadowToast.getTextOfLatestToast()
ShadowLooper.runUiThreadTasks()
ShadowApplication.getInstance()
```

✅ Extra control for testing edge cases  
✅ Time manipulation, async tasks, lifecycles

***

## 🔄 Robolectric in Testing Pyramid

```text
UI Tests (Instrumentation)   ← Few
--------------------------
UI Tests (Robolectric)
--------------------------
Unit Tests (JVM)             ← Many
```

✅ Use Robolectric to **reduce instrumentation tests**
✅ Faster CI pipelines

***

## 🧯 Common Interview Mistakes

❌ “Robolectric replaces Espresso”  
✅ No—it complements it

❌ “Robolectric tests real Android UI”  
✅ It simulates, not renders

❌ “Compose doesn’t need Robolectric”  
✅ Compose UI tests can still benefit from JVM speed

***

## 🔥 Interview‑Ready Final Summary

> “Robolectric is a JVM-based Android testing framework that simulates Android framework behavior using shadow classes, allowing Android tests—including Compose UI tests—to run without an emulator or device. It provides fast and deterministic feedback, making it ideal for testing Activities, framework interactions, and simple UI rendering. However, for real hardware behavior and performance validation, instrumentation tests are still required.”

***

## ✅ One‑Line Closing (Very Strong)

> *“Robolectric gives you Android tests at unit-test speed.”*

***

  </details>

  <details style="margin-left: 20px;">
    <summary><i>📄 4. Testing ViewModel with coroutines & Flow ?</i></summary>

***

## ✅ What you need (toolkit)

Add these dependencies:

```kotlin
// Coroutines test
testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:<version>")
// Flow testing
testImplementation("app.cash.turbine:turbine:<version>")
// (Optional, if you still expose LiveData anywhere)
testImplementation("androidx.arch.core:core-testing:<version>")
```

> Tip: Keep your ViewModel logic free of Android types; most tests can run on the JVM (no device/emulator).

***

## ✅ Golden rules for ViewModel coroutine tests

1.  **Use `runTest {}`** to get a virtual test scheduler (deterministic, controllable time).
2.  Install a **Main dispatcher rule** so `viewModelScope` uses a test dispatcher.
3.  **Expose UI as `StateFlow`** (immutable from outside); use `Turbine` to assert emissions in order.
4.  **Fake your data layer** with suspending functions or `Flow`.
5.  Control **virtual time** (`advanceTimeBy`, `advanceUntilIdle`) to test debounce/timeout/retry logic.
6.  Clean up by **cancelling** the scope or relying on JUnit rules.

***

## 🧱 Set up a `MainDispatcherRule`

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val dispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }
    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
```

Use it in tests:

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
@get:Rule val mainRule = MainDispatcherRule()
```

***

## 🧪 Example 1: Simple `StateFlow` UI state

### ViewModel

```kotlin
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val loggedIn: Boolean = false
)

class LoginViewModel(
    private val repo: AuthRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _ui = MutableStateFlow(LoginUiState())
    val ui: StateFlow<LoginUiState> = _ui

    fun onEmailChanged(v: String) = _ui.update { it.copy(email = v, error = null) }
    fun onPasswordChanged(v: String) = _ui.update { it.copy(password = v, error = null) }

    fun login() {
        val s = _ui.value
        if (s.email.isBlank() || s.password.isBlank()) {
            _ui.update { it.copy(error = "Email/Password required") }
            return
        }
        viewModelScope.launch(dispatcher) {
            _ui.update { it.copy(loading = true, error = null) }
            runCatching { repo.login(s.email, s.password) }
                .onSuccess {
                    _ui.update { it.copy(loading = false, loggedIn = true) }
                }
                .onFailure { e ->
                    _ui.update { it.copy(loading = false, error = e.message ?: "Login failed") }
                }
        }
    }
}
```

### Test (using Turbine)

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule val main = MainDispatcherRule()
    
    @Test
    fun `successful login emits loading then loggedIn`() = runTest {
        val fakeRepo = object : AuthRepository {
            override suspend fun login(email: String, password: String) = Unit
        }
        val vm = LoginViewModel(fakeRepo, dispatcher = StandardTestDispatcher(testScheduler))

        vm.onEmailChanged("user@tcs.com")
        vm.onPasswordChanged("secret")

        vm.ui.test {
            // Initial emission
            assertEquals(LoginUiState(email = "user@tcs.com", password = "secret"), awaitItem())

            vm.login()
            // Because we’re using a TestDispatcher, the coroutine hasn’t executed yet:
            // advance until all pending coroutines complete
            advanceUntilIdle()

            // After login() we should see loading true then loggedIn true
            val loading = awaitItem()
            assertTrue(loading.loading)

            val success = awaitItem()
            assertFalse(success.loading)
            assertTrue(success.loggedIn)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `blank credentials instantly set error without suspending`() = runTest {
        val fakeRepo = object : AuthRepository {
            override suspend fun login(email: String, password: String) = Unit
        }
        val vm = LoginViewModel(fakeRepo, dispatcher = StandardTestDispatcher(testScheduler))

        vm.ui.test {
            awaitItem() // initial
            vm.login()
            val error = awaitItem()
            assertEquals("Email/Password required", error.error)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
```

**Why this is good:**

*   Uses **`runTest`** and a **test dispatcher** for determinism.
*   Asserts **ordered emissions** from `StateFlow` with **Turbine**.
*   Uses **`advanceUntilIdle()`** to flush jobs.

***

## 🧪 Example 2: Testing debounce / search with virtual time

### ViewModel with debounce

```kotlin
class SearchViewModel(
    private val repo: SearchRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val scope = viewModelScope
    private val _query = MutableStateFlow("")
    private val _results = MutableStateFlow<List<String>>(emptyList())
    val results: StateFlow<List<String>> = _results

    init {
        _query
            .debounce(300)
            .filter { it.length >= 2 }
            .flatMapLatest { q -> flow { emit(repo.search(q)) } }
            .flowOn(dispatcher)
            .onEach { _results.value = it }
            .launchIn(scope)
    }

    fun setQuery(q: String) { _query.value = q }
}
```

### Test (control time)

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule val main = MainDispatcherRule()

    @Test
    fun `debounce waits 300ms before emitting results`() = runTest {
        val repo = object : SearchRepository {
            override suspend fun search(q: String) = listOf("Result for $q")
        }
        val vm = SearchViewModel(repo, dispatcher = StandardTestDispatcher(testScheduler))

        vm.results.test {
            // initial
            assertEquals(emptyList<String>(), awaitItem())

            vm.setQuery("h")
            // < 300ms, nothing should emit due to debounce filter (and length < 2)
            advanceTimeBy(200)
            expectNoEvents()

            vm.setQuery("ha")
            // countdown restarts; advance to just before 300
            advanceTimeBy(299)
            expectNoEvents()

            // Cross debounce threshold
            advanceTimeBy(1)
            // Let downstream flatMapLatest run
            advanceUntilIdle()

            val res = awaitItem()
            assertEquals(listOf("Result for ha"), res)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
```

**Key idea:** with `runTest` you get **virtual time**—you can **fast‑forward** without sleeping, making debounce/timeout logic **deterministic** and fast.

***

## 🧪 Example 3: Testing `SharedFlow` one‑off effects

One‑off effects (e.g., navigation, toasts) are usually modeled as `SharedFlow` or `Channel`.

### ViewModel

```kotlin
sealed interface LoginEffect { data object NavigateHome : LoginEffect }

class EffectsVM : ViewModel() {
    private val _effects = MutableSharedFlow<LoginEffect>()
    val effects: SharedFlow<LoginEffect> = _effects

    fun success() = viewModelScope.launch { _effects.emit(LoginEffect.NavigateHome) }
}
```

### Test

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class EffectsVMTest {

    @get:Rule val main = MainDispatcherRule()

    @Test
    fun `emits NavigateHome effect once`() = runTest {
        val vm = EffectsVM()
        vm.effects.test {
            vm.success()
            assertEquals(LoginEffect.NavigateHome, awaitItem())
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }
}
```

***

## 🧪 Example 4: `SavedStateHandle` + process recreation

If your ViewModel uses `SavedStateHandle`, you can initialize it in tests:

```kotlin
class SavedVM(private val saved: SavedStateHandle) : ViewModel() {
    val name: StateFlow<String> = saved.getStateFlow("name", "")
    fun setName(n: String) { saved["name"] = n }
}

@Test
fun `SavedStateHandle persists value`() = runTest {
    val handle = SavedStateHandle()
    val vm = SavedVM(handle)

    val values = mutableListOf<String>()
    val job = launch { vm.name.collect { values += it } }

    vm.setName("Harshal")
    advanceUntilIdle()
    assertEquals(listOf("", "Harshal"), values)

    // Simulate recreation
    val vm2 = SavedVM(handle)
    val latest = vm2.name.first()
    assertEquals("Harshal", latest)

    job.cancel()
}
```

***

## 🔍 Tips & gotchas

*   **Prefer `StateFlow`** over `LiveData` for ViewModel state; it’s Kotlin‑first and works great with `runTest`.
*   If you *do* have `LiveData`, add `InstantTaskExecutorRule` to execute architecture components synchronously.
*   Use **`StandardTestDispatcher`** by default; use **`UnconfinedTestDispatcher`** rarely (it runs immediately and can hide ordering issues).
*   **Don’t** use real `Dispatchers.IO` in tests—inject a dispatcher and replace it with a test dispatcher.
*   Keep ViewModel constructors **DI-friendly** (pass repositories, dispatchers). This makes tests trivial.
*   For **error flows / retries**, assert with Turbine’s `awaitError()` or check error states after `advanceUntilIdle()`.

***

## 🧪 Minimal test template you can reuse

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class MyViewModelTest {

    @get:Rule val main = MainDispatcherRule()

    @Test
    fun `happy path`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repo = FakeRepo()
        val vm = MyViewModel(repo, dispatcher)

        vm.state.test {
            assertEquals(MyState(), awaitItem())

            vm.onAction()

            advanceUntilIdle()

            val next = awaitItem()
            // assert next fields
            cancelAndIgnoreRemainingEvents()
        }
    }
}
```

***

## 🧭 What to say in the interview (30‑second summary)

> “I test ViewModels with `kotlinx-coroutines-test` using `runTest` and a `MainDispatcherRule` so `viewModelScope` runs on a test dispatcher. I expose UI as `StateFlow` and assert emissions with Turbine in order. For debounce/timeout logic I control virtual time using `advanceTimeBy`/`advanceUntilIdle`. One‑off events are `SharedFlow` and asserted once. I inject dispatchers and fakes into the ViewModel so tests stay deterministic and fast.”

***

  </details>

</details>

<details>
  <summary><b>📂 Scenario based questions</b></summary>

  <details style="margin-left: 20px;">
    <summary><i>📄 1. If your Compose screen is recomposing too often, how will you debug?</i></summary>

***

## 🎯 Goals when debugging

1.  **See where recompositions happen** (and how often).
2.  **Identify why** they’re happening (unstable params, state reads too high, effects restarting, etc.).
3.  **Confirm fixes** with measurements.

***

## 🛠️ Step 1: Turn on recomposition visualizers

### 1) Recomposition count overlay (Layout Inspector)

*   **Run the app** → **Android Studio > Layout Inspector** → select your process.
*   Enable “**Show recomposition counts**” (or “Highlight recompositions” in recent Studio).
*   Watch which composables flash or show high counts.

**What you’re looking for:**

*   Counts increasing while you aren’t interacting → **unwanted recomposition**.
*   Parent counts rising while only leaf UI should change → **state read too high**.

### 2) Debug modifier: `recomposeHighlighter()`

Use during local runs (debug only):

```kotlin
@Composable
fun DebugBox(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = modifier
            .recomposeHighlighter() // flashes on recomposition (debug impl)
            .padding(8.dp)
    ) { content() }
}
```

Apply around suspicious subtrees to see what’s “hot”.

> Tip: Use this in a feature branch; remove before commit.

***

## 🧭 Step 2: Check the usual *causes* (quick checklist)

### A) **Unstable parameters** (new objects/lambdas every recomposition)

*   **Symptoms:** Child recomposes even when its inputs haven’t “logically” changed.
*   **Fixes:**
    *   Use **immutable data classes** for models; annotate with `@Immutable` when truly immutable.
    *   Avoid creating objects in parameter position; wrap with `remember`.
    *   Memoize lambdas:
        ```kotlin
        val onClick = remember(user) { { vm.submit(user.id) } }
        Button(onClick = onClick) { Text(user.name) }
        ```

### B) **State read too high in the tree**

*   **Symptom:** A whole screen recomposes for a small change.
*   **Fix:** **Collect/observe state as low as possible** (closest to where it’s needed).  
    Split UI into smaller stateless composables and pass **only required slices** of state.

### C) **Mutable collections or reference changes**

*   **Symptom:** Every update looks like “changed” because the list reference changes.
*   **Fixes:**
    *   Prefer **`List`** over `MutableList`.
    *   Consider **persistent immutable collections** (`kotlinx.collections.immutable`) to keep references stable.
    *   Avoid `list = list.toMutableList().apply { … }` patterns on every update.

### D) **Effects restarting due to unstable keys**

*   **Symptom:** `LaunchedEffect` keeps cancel/restart (network thrash, jank).
*   **Fix:** Use **stable, minimal keys**; don’t key on entire lists/objects.  
    Example: `LaunchedEffect(userId)` not `LaunchedEffect(user)`.

### E) **Derivations computed every recomposition**

*   **Symptom:** CPU spikes without UI change.
*   **Fix:** Wrap expensive derivations in **`remember { derivedStateOf { … } }`** with proper keys.

### F) **Navigation / snackbars fired from composable body**

*   **Symptom:** Repeated navigations/snackbars.
*   **Fix:** Emit **one‑off effects** from ViewModel (`SharedFlow`) and handle in `LaunchedEffect`.

### G) **State in wrong owner**

*   **Symptom:** Losing/cascading recompositions across screens.
*   **Fix:** Keep **business state in ViewModel**. Use local `remember` only for **ephemeral UI**.

***

## 🔍 Step 3: Instrument to isolate the hot path

### Add `@ComposableContract` tools

*   Check **parameter stability** by temporarily logging:
    ```kotlin
    @Composable
    fun UserRow(user: UserUi, onClick: () -> Unit) {
        SideEffect {
            // prints when this composable successfully commits a recomposition
            Log.d("UserRow", "Committed for user=${user.id}")
        }
        // ...
    }
    ```
*   Add **test tags** to probe specific nodes with Layout Inspector.

### Narrow the subtree

Wrap potential hotspots with a `DebugBox()` (with `recomposeHighlighter`) and bisect down until the counts stop incrementing.

***

## 🧪 Step 4: Validate with Compose Compiler metrics (advanced)

The Compose compiler can **report stability & skippability** of your composables.

**In your module’s `gradle.properties`** (or Gradle task args):

```properties
kotlin.compiler.plugin.compose.metrics=true
kotlin.compiler.plugin.compose.metricsDestination=/tmp/compose-metrics
kotlin.compiler.plugin.compose.reports=true
kotlin.compiler.plugin.compose.reportsDestination=/tmp/compose-reports
```

**What you get:**

*   Which composables are **skippable** (can be skipped when inputs unchanged).
*   Which parameters are **unstable**.
*   Where **restart groups** are created.

**Use these reports** to spot functions that *should* be skippable but aren’t (typically due to unstable params or reading state inside).

> Interview tip: “I turn on compiler metrics to confirm which composables are skippable and which inputs are driving restarts.”

***

## 📈 Step 5: Profile frames and CPU

*   **CPU Profiler / System Trace (Perfetto)** in Android Studio:
    *   Inspect **main thread** frames during scroll or animation.
    *   Look for **layout/measure spikes** after interactions that shouldn’t cause broad recomposition.
*   **Macrobenchmark** (for regressions):
    *   Create a **scroll** or **interaction** benchmark and ensure **frame time** / **jank** doesn’t regress.

***

## 🔧 Concrete fixes you’ll commonly apply

1.  **Memoize lambdas/formatters**
    ```kotlin
    val formatter = remember(locale) { NumberFormat.getCurrencyInstance(locale) }
    ```

2.  **Use `key` and `contentType` in `LazyColumn`**
    ```kotlin
    LazyColumn {
        items(items, key = { it.id }, contentType = { it.type }) { item -> ItemRow(item) }
    }
    ```

3.  **`derivedStateOf` for derived lists**
    ```kotlin
    val active by remember(items) {
        derivedStateOf { items.filter { it.isActive } }
    }
    ```

4.  **Move state reads down**
    ```kotlin
    // Bad: parent reads everything
    val ui by vm.ui.collectAsState()
    Header(ui.title); List(ui.items)

    // Good: collect where used
    Header(title = vm.title.collectAsState().value)
    ItemsList(items = vm.items.collectAsState().value)
    ```

5.  **Stabilize models**
    ```kotlin
    @Immutable
    data class UserUi(val id: Long, val name: String, val isActive: Boolean)
    ```

6.  **Avoid `LaunchedEffect` restarts**
    ```kotlin
    // Bad
    LaunchedEffect(filters) { vm.search(filters) }

    // Good: key by a minimal, stable representation (e.g., hash or id)
    LaunchedEffect(filters.hashCode()) { vm.search(filters) }
    ```

7.  **Side-effect loops fix**
    ```kotlin
    // Bad: mutating state in SideEffect
    SideEffect { count++ } // loop

    // Good: Only push to external systems here; update Compose state via proper events
    ```

***

## 🧪 Safety net: Write a small recomposition test (debug build)

You can instrument a **debug‑only counter** to detect regressions:

```kotlin
@Composable
fun Counted(tag: String, content: @Composable () -> Unit) {
    var c by remember { mutableStateOf(0) }
    SideEffect { c++ ; Log.d("Recomp", "$tag = $c") }
    content()
}
```

Wrap suspect composables; watch log counts before/after fixes.

***

## 🧭 Putting it all together (my quick routine)

1.  **Layout Inspector → Recomposition counts** to spot hotspots.
2.  **recomposeHighlighter()** around subtrees to isolate.
3.  Inspect **params & state reads** in that subtree: stabilize data, move state reads down, memoize lambdas/objects.
4.  Check **effects** keys (`LaunchedEffect`, `DisposableEffect`) for unnecessary restarts.
5.  Add **`derivedStateOf`** for heavy derivations.
6.  Re‑run inspector → counts should drop.
7.  (Optional) **Compiler reports** to prove skippability.
8.  **Macrobenchmark** to guard against regressions.

***

## 🧠 Interview‑ready one‑liner

> “I debug excessive recompositions by visualizing them with Layout Inspector and `recomposeHighlighter()`, then fix the root cause—usually unstable parameters, state read too high, or effect keys—by stabilizing inputs, hoisting state correctly, and using `derivedStateOf`. I validate with Compose compiler metrics and, if needed, a Macrobenchmark to ensure performance holds.”

***

  </details>

  <details style="margin-left: 20px;">
    <summary><i>📄 2. How would you migrate a large XML-based app to Compose?</i></summary>

***

## 🎯 Migration Goals

1.  **Incremental rollout**—no big-bang rewrite.
2.  **Seamless interop**—Compose + Views co-exist safely.
3.  **Modern architecture**—state flows from ViewModel to UI.
4.  **Theming consistency**—Material 3 baseline from day one.
5.  **Measurable improvements**—performance + code size + velocity.

***

## 🗺️ High-Level Phases

1.  **Preparation (architecture & groundwork)**
2.  **Introduce Compose in islands (interop)**
3.  **Move screens module-by-module**
4.  **Consolidate theming, navigation, and state**
5.  **Decommission XML/View-only code**

Each phase should be shippable.

***

## 1) Preparation: set yourself up for success

### ✅ Dependency & tooling

*   Add Compose BOM + Material3, Navigation-Compose, Activity-Compose.
*   Enable **Compose Compiler** & Kotlin versions compatible across modules.

### ✅ Architecture hardening

*   **MVVM with unidirectional data flow**:
    *   ViewModel exposes **`StateFlow<UiState>`** and **one-off effects** (`SharedFlow`).
    *   Keep business logic and data in **repositories** use-case layer.
*   Decouple UI from XML by pushing all logic into **ViewModel** so both XML and Compose can render the same state.

### ✅ Theming parity

*   Define **Material 3 theme** (`MaterialTheme(colorScheme, typography, shapes)`).
*   Map brand colors from XML theme to **`lightColorScheme()` / `darkColorScheme()`**.
*   If possible, set **dynamic color** for Android 12+ as a progressive enhancement.

### ✅ Test and metrics

*   Turn on **Compose metrics** in a branch to evaluate skippability (optional).
*   Set up **UI testing rule** for Compose alongside Espresso for legacy.

***

## 2) Interop: Compose + Views (safe, incremental)

You don’t have to migrate a whole screen at once—start with **UI islands**.

### A) Use **Compose inside existing XML**

*   Add a **`ComposeView`** to your layout, or wrap with `AbstractComposeView` for reusability.

**XML:**

```xml
<androidx.compose.ui.platform.ComposeView
    android:id="@+id/compose_chip"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

**Fragment/Activity:**

```kotlin
composeView.setContent {
    AppTheme {
        FilterChips(
            selected = viewModel.selected,
            onToggle = viewModel::toggle
        )
    }
}
```

**Great for:** Replacing **RecyclerView rows**, chips, cards, banners, settings rows.

***

### B) Use **Views inside Compose** where needed

Some legacy SDKs are still View-based (Maps, WebView).

```kotlin
@Composable
fun LegacyMap() {
    AndroidView(
        factory = { ctx -> MapView(ctx).apply { onCreate(null) } },
        update = { mapView -> /* bind props */ }
    )
}
```

***

### C) Replace **RecyclerView adapters** first

*   Compose’s `LazyColumn` removes adapter boilerplate and layout inflation cost.
*   Migrate an adapter item by item: first **row composables**, then the whole list container.

***

## 3) Screen-by-Screen Migration Strategy

Prioritize **high-ROI screens**:

*   High change frequency, complex adapters, or where XML constraints are painful.
*   Low-risk screens (e.g., settings) are good first wins.

### Example: Migrate a detail screen

**Before (XML + Fragment):**

*   Fragment inflates XML, uses ViewBinding, manual `setText`, `setOnClickListener`.

**After (Compose screen):**

```kotlin
@Composable
fun ProductScreen(
    vm: ProductViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    ProductContent(ui, onBack, vm::retry)
}

@Composable
fun ProductContent(
    ui: ProductUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(ui.title) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } }) }
    ) { padding ->
        when {
            ui.loading -> Box(Modifier.padding(padding)) { CircularProgressIndicator() }
            ui.error != null -> ErrorCard(ui.error, onRetry)
            else -> LazyColumn(Modifier.padding(padding)) {
                item { Header(ui) }
                items(ui.features, key = { it.id }, contentType = { "feature" }) { FeatureRow(it) }
            }
        }
    }
}
```

**Keep old Fragment** as a thin host if needed:

```kotlin
class ProductFragment : Fragment() {
    override fun onCreateView(...) = ComposeView(requireContext()).apply {
        setContent { AppTheme { ProductScreen(onBack = { findNavController().popBackStack() }) } }
    }
}
```

***

## 4) Navigation, State & Side-effects

### Navigation

*   If you use **Navigation Component** with Fragments, you can:
    *   Keep graph, but screens rendered by Compose via fragments with `ComposeView`.
    *   Or migrate to **Navigation-Compose** route-by-route.
    *   For bottom tabs: use **`launchSingleTop` + `restoreState`** to preserve scroll.

### State ownership

*   ViewModel is **single source of truth** for each screen.
*   Compose observes `StateFlow`/`Flow` with `collectAsStateWithLifecycle()`.
*   UI events go **up** to ViewModel.
*   One-off effects (nav/snackbar) via `SharedFlow`, collected in **`LaunchedEffect`**.

### Side-effects

*   `LaunchedEffect(key)` for loading/collecting within the screen’s lifecycle.
*   `DisposableEffect` for registering/unregistering listeners (interop).
*   `SideEffect` to push post-commit info to external controllers.

***

## 5) Theming & Design System Migration

*   Create a **Theme.kt** that maps your brand tokens:
    *   `ColorScheme` (Material 3), `Typography`, `Shapes`.
*   Wrap entire app (or the root host) with `MaterialTheme`.
*   Build **design tokens** via CompositionLocals for spacing, elevations, radii—so XML and Compose both reflect the same values during transition.

```kotlin
@Composable
fun AppTheme(dark: Boolean, content: @Composable () -> Unit) {
    val scheme = if (dark) DarkColorScheme else LightColorScheme
    CompositionLocalProvider(LocalSpacing provides Spacing()) {
        MaterialTheme(colorScheme = scheme, typography = AppTypography, content = content)
    }
}
```

***

## 6) Testing during migration

*   **Unit tests** for ViewModel logic (Flows, reducers, debounce with `runTest` + Turbine).
*   **Compose UI tests** for new composables:
    *   `onNodeWithText/Tag` + `performClick` + `assert…`
    *   Control animations with `mainClock`.
*   **Robolectric** for fast JVM Compose tests and to keep CI fast.
*   Keep existing Espresso tests for legacy screens until fully migrated.

***

## 7) Performance checks & guardrails

*   Replace `Column + verticalScroll` with **`LazyColumn`** for long content.
*   Provide **`key`** and **`contentType`** in lazy lists.
*   Use **`remember`** and **`derivedStateOf`** for heavy derivations.
*   Keep parameters **stable** (`data class`, `@Immutable`) to enable skipping.
*   Enable **recomposition counts** in Layout Inspector to detect hot spots.
*   Consider a **Macrobenchmark** for critical screens (scrolling, search).

***

## 8) Rollout strategy (risk-managed)

1.  **Start with leaf UI (rows, chips, banners)** embedded via `ComposeView`.
2.  **Migrate a simple screen** (Settings/About) end-to-end to validate patterns (VM, nav, theming).
3.  **Pick a high-impact screen** with RecyclerView → `LazyColumn` migration for visible perf gain.
4.  **Parallelize by feature modules** (if modularized) so teams can migrate independently.
5.  Use feature flags and **A/B shadow screens** if needed for risk control.
6.  Track metrics (startup time, frame time, crash-free sessions, code size delta).

***

## 9) Decommissioning legacy

*   Remove **ViewBinding**/adapter code as screens convert.
*   Collapse **XML themes** into a minimal bridge, ultimately moving to a pure Compose theme.
*   Delete layout resources that are no longer referenced (CI check for unused resources helps).
*   Remove Fragment scaffolding when a graph fully runs on Navigation‑Compose.

***

## 10) Common pitfalls & how to avoid them

*   **Big-bang rewrite** → Don’t. Incremental interop is your friend.
*   **State stored in composables** that needs to survive process death → Move to **ViewModel + SavedStateHandle**.
*   **Creating new objects/lambdas per recomposition** → `remember` or lift/memoize.
*   **No keys in lists** → unstable scrolling and extra recomposition.
*   **Triggering side-effects from composable body** → use `LaunchedEffect`.
*   **Mixing Material 2 & 3** inconsistently → pick M3 and migrate components.
*   **Inconsistent theme usage** → always pull colors/typography from `MaterialTheme`.

***

## 🧩 Minimal migration template you can reuse

**ViewModel (state + effect):**

```kotlin
data class UiState(val loading: Boolean = false, val items: List<ItemUi> = emptyList(), val error: String? = null)
sealed interface UiEffect { data class NavigateTo(val route: String): UiEffect }

@HiltViewModel
class ScreenVM @Inject constructor(
    private val repo: Repo
): ViewModel() {
    private val _ui = MutableStateFlow(UiState(loading = true))
    val ui: StateFlow<UiState> = _ui

    private val _effect = MutableSharedFlow<UiEffect>()
    val effect: SharedFlow<UiEffect> = _effect

    init { load() }

    fun load() = viewModelScope.launch {
        runCatching { repo.items() }
            .onSuccess { _ui.value = UiState(items = it) }
            .onFailure { _ui.value = UiState(error = it.message) }
    }

    fun openDetail(id: Long) = viewModelScope.launch {
        _effect.emit(UiEffect.NavigateTo("detail/$id"))
    }
}
```

**Compose Screen:**

```kotlin
@Composable
fun Screen(
    vm: ScreenVM = hiltViewModel(),
    navigate: (String) -> Unit
) {
    val ui by vm.ui.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.effect.collect { eff ->
            when (eff) { is UiEffect.NavigateTo -> navigate(eff.route) }
        }
    }

    when {
        ui.loading -> Loading()
        ui.error != null -> Error(ui.error) { vm.load() }
        else -> LazyColumn {
            items(ui.items, key = { it.id }, contentType = { "item" }) { item ->
                ItemRow(item, onClick = { vm.openDetail(item.id) })
            }
        }
    }
}
```

***

## ✅ 30‑second interview summary

> “I’d migrate a large XML app to Compose incrementally. First I harden the architecture so ViewModels expose all state and effects. Then I embed Compose islands inside existing screens via `ComposeView`, starting with RecyclerView rows and reusable components. Next, I migrate full screens one by one, using Navigation‑Compose where feasible while interoperating with View-based navigation when needed. I centralize theming in Material 3, keep state in ViewModels, and manage side effects with `LaunchedEffect`. I validate with Compose UI tests and Layout Inspector recomposition counters, and guard key flows with Macrobenchmarks. Finally, I decommission legacy XML/ViewBinding as modules complete.”

***

  </details>

  <details style="margin-left: 20px;">
    <summary><i>📄 3. How do you design a scalable app for multiple markets?</i></summary>

***

# ✅ How do you design a scalable app for multiple markets?

## ✅ High‑Impact Interview Opening

> Designing a scalable app for multiple markets involves **separating global core logic from market‑specific variations**, ensuring **configurability over hard‑coding**, and building for **localization, compliance, feature toggling, and performance at scale**.

***

## 1️⃣ Core Principle: Separate “Core” vs “Market”

### ✅ Mental Model (Very Important)

    Global App Core
     ├─ Business rules
     ├─ Navigation
     ├─ Shared UI components
     ├─ Analytics framework
     ├─ Networking layer
     │
     ├─ Market A (India)
     ├─ Market B (UK)
     ├─ Market C (US)

🎯 Interview line:

> “The app should behave differently per market without branching the core.”

***

## 2️⃣ Architecture Level – Modularization

### ✅ Multi‑module architecture (required)

    :app
    :core
    :core-ui
    :core-network
    :core-analytics
    :feature-login
    :feature-payments
    :market-india
    :market-uk
    :market-us

### ✅ What goes where?

| Module      | Responsibility                         |
| ----------- | -------------------------------------- |
| `core`      | Domain models, use cases               |
| `core-ui`   | Reusable Compose components            |
| `feature-*` | Market‑independent features            |
| `market-*`  | Market rules, configuration, overrides |

🎯 Interview phrase:

> “Market modules override behavior, not core architecture.”

***

## 3️⃣ Localization & Internationalization (Critical)

### ✅ String, layout, and formatting support

*   `strings.xml` per locale
*   Currency formatting
*   Date & number formats
*   RTL support

```kotlin
val formatter = NumberFormat.getCurrencyInstance(locale)
```

✅ **Never hardcode text or formats**

🎯 Interview insight:

> “We localize content and format data based on locale, not assumptions.”

***

## 4️⃣ Feature Toggles & Remote Configuration

### ✅ Why?

*   Markets launch at different times
*   Regulations differ
*   Features may be disabled per region

### ✅ Solution

*   Firebase Remote Config / LaunchDarkly
*   Backend‑driven feature flags

```kotlin
if (featureFlags.isInternationalTransferEnabled) {
    ShowInternationalTransfer()
}
```

🎯 Interview line:

> “Feature flags let us ship once and enable selectively.”

***

## 5️⃣ Configuration‑Driven Behavior (NOT Hardcoded)

### ✅ Avoid this ❌

```kotlin
if (country == "IN") { ... }
```

### ✅ Prefer this ✅

```kotlin
MarketConfig {
  kycRequired = true
  maxTransferLimit = 250000
}
```

Config loaded:

*   From backend
*   Per market
*   Cached locally

🎯 Interview wisdom:

> “Markets change faster than releases — configuration must be dynamic.”

***

## 6️⃣ Compliance & Regulations Layer

Different markets → different compliance:

*   GDPR (EU)
*   RBI / Indian banking norms
*   PCI‑DSS
*   Data residency

### ✅ Strategy

*   Abstract compliance checks
*   Feature gating
*   Per‑market consent screens

```kotlin
interface ConsentProvider {
    fun showConsentIfRequired()
}
```

Implemented per market module.

***

## 7️⃣ Theming & Branding per Market

### ✅ Compose makes this very scalable

Use **Theme as configuration**:

```kotlin
@Composable
fun AppTheme(marketTheme: MarketTheme) {
    MaterialTheme(
        colorScheme = marketTheme.colorScheme,
        typography = marketTheme.typography
    ) { ... }
}
```

Markets may differ in:

*   Primary color
*   Typography
*   Logos
*   Branding guidelines

🎯 Interview line:

> “Branding is data, not code.”

***

## 8️⃣ Navigation Strategy for Multi‑Market Apps

### ✅ Core navigation + market extensions

*   Core destinations are shared
*   Market modules **add/remove routes**

```kotlin
interface MarketNavigation {
    fun register(graph: NavGraphBuilder)
}
```

🎯 Interview phrase:

> “Navigation is extensible, not duplicated.”

***

## 9️⃣ API & Backend Versioning

### ✅ Challenges

*   Market‑specific APIs
*   Different rollout schedules

### ✅ Solution

*   API version negotiation
*   Adapter / Mapper per market

```kotlin
interface PaymentApi {
    suspend fun transfer(...)
}
```

Market‑specific implementations mapped internally.

***

## 🔟 Data Layer & Storage Strategy

### ✅ Market‑aware persistence

*   Encrypted storage per region
*   Market‑specific caching rules
*   Controlled PII access

```kotlin
EncryptedDataStore(marketPolicy)
```

🎯 Interview insight:

> “Data residency rules affect storage design.”

***

## 1️⃣1️⃣ Analytics & Tracking

### ✅ Unified events, market tags

```kotlin
trackEvent(
  name = "payment_success",
  market = marketCode
)
```

*   Same event definitions
*   Different reporting rules

🎯 Interview phrase:

> “Same analytics contract, multiple reporting lenses.”

***

## 1️⃣2️⃣ Testing Strategy (Very Important)

### ✅ Unit Tests

*   Core logic tested once

### ✅ Market tests

*   Market config tests
*   Feature flag scenarios

### ✅ UI Tests

*   Locale switching
*   RTL rendering
*   Feature enable/disable

🎯 Interview phrase:

> “Confidence comes from automation, not assumptions.”

***

## 1️⃣3️⃣ Deployment & Release Strategy

### ✅ Play Store setups:

*   Country‑based rollout
*   Feature flags for rollback
*   Staged releases

✅ Can also support:

*   White‑label builds
*   Enterprise distributions

***

## 1️⃣4️⃣ Common Mistakes to Call Out (Good Interview Point)

❌ Hardcoding market checks  
❌ Forking codebase per country  
❌ Multiple apps per market unnecessarily  
❌ Ignoring localization early  
❌ Manual feature gating

🎯 Senior insight:

> “If adding a new market requires code rewrite — the architecture is wrong.”

***

## ✅ Real‑World Example (Short & Powerful)

> “In a banking app, India might require KYC before payments, while the UK might allow low‑value transfers without KYC. Instead of branching screens, both markets use the same PaymentScreen, but the ViewModel reacts to MarketConfig and feature flags supplied at runtime.”

***

## 🔥 Interview‑Ready Final Summary (Polished)

> “To design a scalable multi‑market app, I separate global core logic from market‑specific configuration using a modular architecture. Market behavior is driven by runtime configuration, feature flags, and localized resources rather than hardcoded checks. I ensure compliance, theming, navigation, and analytics are extensible per market while keeping business logic and UI reusable. This approach allows new markets to be onboarded with minimal code changes and low risk.”

***

## ✅ One‑Line Power Close

> **“Scalable multi‑market apps are configuration‑driven, not country‑coded.”**

***

  </details>

  <details style="margin-left: 20px;">
    <summary><i>📄 4. How would you architect a feature used by multiple apps (host apps)?</i></summary>

***

# ✅ How would you architect a feature used by multiple host apps?

## ✅ High‑impact interview opening

> To architect a feature used by multiple host apps, I treat it as a **productized, versioned SDK or platform module** that is **independent, configurable, and host‑agnostic**, with a clear contract, minimal assumptions about the host, and strong backward compatibility guarantees.

***

## 1️⃣ Guiding principles (very important)

Before tools or code, define principles:

1.  **Host‑agnostic** – The feature must not depend on app‑specific logic.
2.  **Stable API contract** – Changes must not break existing hosts.
3.  **Configuration‑driven** – Different apps behave differently without forks.
4.  **Loose coupling** – Communication via interfaces/events, not direct calls.
5.  **Independent lifecycle & release** – Feature evolves without forcing host upgrades.
6.  **Security & ownership isolation** – Host owns auth, storage, navigation decisions.

🎯 Interview line:

> “A shared feature must behave like a product, not a code snippet.”

***

## 2️⃣ Choose the right packaging model

### ✅ Preferred: **Android SDK / Library (AAR)**

    :feature-sdk
     ├─ api
     ├─ ui
     ├─ domain
     ├─ data
     ├─ contracts

Consumed by:

    BankingApp
    RetailApp
    PartnerApp
    WhiteLabelApp

### ✅ Alternatives (case‑by‑case)

| Option                 | When to use                     |
| ---------------------- | ------------------------------- |
| Android Library (AAR)  | Most common, best control       |
| Dynamic Feature Module | Same app, Play Feature delivery |
| Remote UI (WebView)    | Ultra‑fast updates, weaker UX   |
| Service / App Plugin   | Automotive / Enterprise         |

🎯 Interview insight:

> “If multiple independent apps consume it, it should be an SDK, not a feature module.”

***

## 3️⃣ Define a strict public API (contract first)

### ✅ Public API module (`:feature-api`)

```kotlin
interface TransferFeature {
    @Composable
    fun Screen(
        config: TransferConfig,
        callbacks: TransferCallbacks
    )
}

data class TransferConfig(
    val currency: String,
    val maxLimit: Double,
    val locale: String
)

interface TransferCallbacks {
    fun onSuccess(transactionId: String)
    fun onError(error: TransferError)
    fun onExit()
}
```

✔ Host calls into feature  
✔ Feature calls back via interfaces  
✔ No direct host dependency

🎯 Interview phrase:

> “The contract is more important than the implementation.”

***

## 4️⃣ Internal architecture of the shared feature

### ✅ Clean Architecture inside the SDK

    feature-sdk
     ├─ api            ← Public interfaces
     ├─ ui             ← Compose screens
     ├─ domain         ← Use cases
     ├─ data           ← Repositories
     ├─ internal       ← Hidden implementation

✅ Only `api` is public  
✅ Everything else marked `internal`

```kotlin
internal class TransferViewModel(...)
```

🎯 Interview insight:

> “Host apps should never depend on internal packages.”

***

## 5️⃣ Configuration‑driven behavior (key to reuse)

Never branch by app name:

❌ `if (host == "AppA")`

✅ Use config objects:

```kotlin
TransferConfig(
    currency = "INR",
    maxLimit = 250000.0,
    locale = "en_IN"
)
```

Or remote config:

```kotlin
FeatureFlagConfig(
    kycRequired = true,
    showInternational = false
)
```

🎯 Interview line:

> “Different apps → different config, same binary.”

***

## 6️⃣ UI strategy: Compose as a **black‑box UI**

### ✅ Feature owns its UI

```kotlin
TransferFeature.Screen(
    config = config,
    callbacks = callbacks
)
```

Host:

*   Provides data & navigation callbacks
*   Does NOT touch internal UI logic

### ✅ Host can still customize:

*   Theme
*   Colors
*   Typography
*   Locale
*   Accessibility

```kotlin
AppTheme {
    TransferFeature.Screen(...)
}
```

🎯 Interview phrase:

> “The feature renders UI; the host controls experience boundaries.”

***

## 7️⃣ Navigation ownership (critical)

### ✅ Rule: **Host owns navigation**

Feature never does:

```kotlin
navController.navigate(...)
```

Instead:

```kotlin
callbacks.onSuccess(txId)
```

Host decides:

```kotlin
onSuccess = { navController.navigate("receipt/$it") }
```

🎯 Interview insight:

> “The feature signals intent; the host decides destination.”

***

## 8️⃣ Authentication, storage & networking

### ✅ Auth handled by host

Feature depends on an **AuthProvider**:

```kotlin
interface AuthProvider {
    fun getAccessToken(): String
}
```

Host injects implementation.

### ✅ Storage via abstraction

```kotlin
interface SecureStorage {
    fun save(key: String, value: String)
}
```

### ✅ Networking via adapter

Use:

*   Host‑provided OkHttp client, or
*   Feature‑scoped client with injected interceptors

🎯 Interview quote:

> “Security boundaries must remain with the host.”

***

## 9️⃣ Dependency Injection strategy

### ✅ Constructor‑based DI (preferred)

```kotlin
class TransferFeatureImpl(
    private val authProvider: AuthProvider,
    private val config: TransferConfig
) : TransferFeature
```

Host:

```kotlin
val feature = TransferFeatureImpl(authProvider, config)
```

### ✅ Hilt/DI interop (optional)

Expose entry points, but **don’t force DI framework usage** on hosts.

🎯 Interview line:

> “Shared features must not dictate the host’s DI framework.”

***

## 🔁 Versioning & backward compatibility

### ✅ Semantic versioning

    1.2.0 – new features
    1.2.1 – bugfix
    2.0.0 – breaking changes

### ✅ Deprecate safely

```kotlin
@Deprecated("Use new callback")
fun onComplete()
```

Never:

*   Rename APIs casually
*   Break contracts silently

🎯 Interview insight:

> “Breaking changes cost trust faster than bugs.”

***

## 🧪 Testing strategy

### ✅ In the SDK

*   Unit tests (domain logic)
*   Compose UI tests (Robolectric for speed)
*   Contract tests (API behavior)

### ✅ In host apps

*   Integration test with fake configs
*   Navigation callback verification

🎯 Interview phrase:

> “I test the feature in isolation and in integration.”

***

## 🛡️ Observability, logging & analytics

### ✅ Feature emits structured events

```kotlin
interface FeatureAnalytics {
    fun track(event: FeatureEvent)
}
```

Host plugs its analytics system:

*   Firebase
*   Adobe
*   Internal

🎯 Interview insight:

> “The feature produces signals; the host decides where to send them.”

***

## ⚠️ Common mistakes (call these out)

❌ Tight coupling to host app  
❌ Hardcoded market/app logic  
❌ Feature navigating directly  
❌ Forcing DI or theming  
❌ Unversioned breaking changes

🎯 Senior quote:

> “If one host upgrade breaks others, the architecture failed.”

***

## ✅ Real‑world example (short)

> In a banking ecosystem, the same **Funds Transfer feature** is used by the retail app, SME app, and partner app. Each host injects different limits, currencies, branding, and compliance rules, but the same SDK binary handles the flow, validation, and UI while emitting host‑handled navigation events.

***

## 🔥 Interview‑ready final summary (polished)

> “I architect shared features as independent, versioned SDKs with a strict public API. The feature owns its UI and internal logic, while the host provides configuration, authentication, navigation, and analytics through well‑defined interfaces. Behavior differences are configuration‑driven rather than hardcoded. This approach ensures scalability across host apps, safe evolution over time, and minimal integration risk.”

***

## ✅ One‑line power close

> **“A multi‑host feature should feel like a platform product, not reused code.”**

***

  </details>

</details>

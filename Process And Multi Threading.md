# Process And Multi Threading 

---------------------------

## Process Importance Levels

**• Process with the lowest importance is eliminated first**

• **Foreground Process [Highest Importance]** It has an Activity with its **onResume()** running, user is interacting Service executing its Lifecycle methods, foreground service or user is interacting with the Service bound to the running Activity , 

- **BroadcastReceiver** executing its onReceive

--------------------------

A **BroadcastReceiver** in Android is a component that allows an app to respond to system-wide or app-specific broadcast messages (events) sent by the Android system or other apps. These broadcasts are typically used to notify apps about events like changes in network status, incoming calls, low battery, or custom events defined by an app.

### Key Points about BroadcastReceiver
1. **Purpose**:
   - It listens for **Intents** broadcast by the system or other apps.
   - Examples of system broadcasts include:
     - `ACTION_BOOT_COMPLETED`: Device has finished booting.
     - `ACTION_POWER_CONNECTED`: Device is plugged into power.
     - `ACTION_AIRPLANE_MODE_CHANGED`: Airplane mode is toggled.
   - Apps can also define and send custom broadcasts for internal communication.

2. **How It Works**:
   - A `BroadcastReceiver` is registered to listen for specific `Intent` actions.
   - When a matching `Intent` is broadcast, the receiver's `onReceive(Context, Intent)` method is triggered to handle the event.
   - It runs briefly (typically less than 10 seconds) to process the event and should not perform long-running tasks.

3. **Types of Broadcasts**:
   - **Normal Broadcasts**: Sent to all registered receivers asynchronously (e.g., `sendBroadcast()`).
   - **Ordered Broadcasts**: Delivered to receivers one at a time, in a defined order, with the ability to modify or abort the broadcast (e.g., `sendOrderedBroadcast()`).
   - **Sticky Broadcasts**: Persist after being sent and remain available for later receivers (deprecated since API 21).

4. **Registration Methods**:
   - **Static Registration** (in `AndroidManifest.xml`):
     - Declared in the app's manifest to listen for broadcasts even when the app is not running.
     - Example:
       ```xml
       <receiver android:name=".MyReceiver">
           <intent-filter>
               <action android:name="android.intent.action.BOOT_COMPLETED"/>
           </intent-filter>
       </receiver>
       ```
   - **Dynamic Registration** (in code):
     - Registered programmatically using `registerReceiver()` in an `Activity` or `Service`.
     - Must be unregistered with `unregisterReceiver()` to avoid memory leaks.
     - Example:
       ```java
       BroadcastReceiver receiver = new BroadcastReceiver() {
           @Override
           public void onReceive(Context context, Intent intent) {
               // Handle the broadcast
           }
       };
       IntentFilter filter = new IntentFilter("android.intent.action.BOOT_COMPLETED");
       registerReceiver(receiver, filter);
       ```

5. **Lifecycle**:
   - A `BroadcastReceiver` is active only during the execution of `onReceive()`.
   - After `onReceive()` completes, the receiver is considered inactive unless re-registered.
   - For long-running tasks, the receiver should delegate work to a `Service` or `JobScheduler`.

6. **Use Cases**:
   - Listening for system events (e.g., battery low, network changes).
   - Responding to custom app events (e.g., notifying components about data updates).
   - Triggering actions when the device boots or a call is received.

7. **Security Considerations**:
   - Broadcasts can be intercepted by other apps unless protected with permissions.
   - Use `LocalBroadcastManager` (or modern alternatives like `LiveData` or `Flow`) for in-app broadcasts to avoid exposing sensitive data.
   - Since Android 8.0 (API 26), most implicit broadcasts require explicit registration due to background execution limits.

8. **Relation to Process Importance** (from your provided image):
   - A `BroadcastReceiver` executing its `onReceive()` method runs in a **Foreground Process** with high importance, meaning it is less likely to be killed by the system during execution.

### Example Implementation
Here’s a simple `BroadcastReceiver` that listens for power connection events:
```java
public class PowerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_POWER_CONNECTED.equals(intent.getAction())) {
            Toast.makeText(context, "Device is charging!", Toast.LENGTH_SHORT).show();
        }
    }
}
```

Register it dynamically in an `Activity`:
```java
public class MainActivity extends AppCompatActivity {
    private PowerReceiver powerReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        powerReceiver = new PowerReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_POWER_CONNECTED);
        registerReceiver(powerReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(powerReceiver); // Prevent memory leaks
    }
}
```

Or statically in `AndroidManifest.xml`:
```xml
<receiver android:name=".PowerReceiver">
    <intent-filter>
        <action android:name="android.intent.action.ACTION_POWER_CONNECTED"/>
    </intent-filter>
</receiver>
```

### Best Practices
- Avoid long-running tasks in `onReceive()`. Use a `Service` or `WorkManager` for background work.
- Unregister dynamic receivers in `onPause()` or `onDestroy()` to prevent leaks.
- Use explicit broadcasts for sensitive data (e.g., specify the receiver's package name).
- For modern apps, consider using `LiveData`, `Flow`, or `WorkManager` for event-driven communication instead of broadcasts when possible.

If you have a specific use case or code related to `BroadcastReceiver` you'd like help with, please share it!

--------------

## Process Importance Levels

**• Visible Process [2nd Highest Importance]**
  • It has an Activity with its **onPause()** called
    • Service bound to a visible Activity

• **Service Process [3rd Highest Importance]**
  • User is not directly interacting
  • It has a Service running [ex. playing music] and does not belong to the above 2 categories

**• Background Process [2nd Lowest Importance]**
  • No user interaction whatsoever currently
  • The one most recently seen by the user is the last to be destroyed

**• Empty Process [Lowest Importance]**
  • No Active components are running
  • Still alive for caching purposes

------------------------------------

![image-20250418204449382](/home/awadin/snap/typora/96/.config/Typora/typora-user-images/image-20250418204449382.png)

---------------------------------------------------

## Important Note For Thread:

**LONG RUNNING OPERATIONS**

- Performing long operations such as network access or database queries will block the whole UI.
- WHEN the UI thread is blocked, no events can be dispatched, including drawing events.
- From the user's perspective, the application appears to hang.
- Don’t perform long running operations on main thread
    • No working with files
    • No network operations
    • No complex calculations

- **Use additional threads for tasks longer than 5 seconds**
- Cannot modify UI from background Thread
- Use Services

---------------------------

![image-20250418205718112](/home/awadin/snap/typora/96/.config/Typora/typora-user-images/image-20250418205718112.png)

This slide illustrates the **Android Threading Architecture**, focusing on how threads and processes work within an Android application. Let’s break it down step by step to help you understand the key concepts.

---

### **Title: Android Threading Architecture**

The slide provides a high-level overview of how an Android app's components interact with threads and processes, emphasizing the threading model and communication with external components.

---

### **Diagram Breakdown**

#### **1. Android Process 1 (Blue Box)**
- This represents a single Android app process. In Android, each app typically runs in its own process (a separate instance of the Dalvik/ART runtime).
- The process contains:
  - **Internal Components** (Green Boxes):
    - **Activities**: UI components that the user interacts with (e.g., screens in your app).
    - **BroadcastReceivers**: Components that listen for system or app events (e.g., a `BroadcastReceiver` for detecting a power connection).
    - **Services**: Background components that perform long-running tasks (e.g., playing music).
    - **ContentProviders**: Components that manage shared data between apps (e.g., accessing contacts).
  - **Main Thread/Looper** (Orange Box):
    - Labeled as "Main Thread/Looper" with sub-labels **M1** and **M2**.
    - The **Main Thread** (also called the UI thread) is responsible for handling all UI operations and lifecycle events of the app's components.
    - The **Looper** is a mechanism that runs a message loop for the thread, processing events (like UI updates, clicks, or lifecycle callbacks) in a queue.
    - **M1** and **M2** likely represent messages or tasks in the message queue that the Looper processes (e.g., drawing the UI, handling user input).

#### **2. Thread Pool (Blue Box with T1 and T2)**
- The **Thread Pool** contains additional threads (**T1**, **T2**) that can be used for background tasks.
- These threads are separate from the Main Thread and are used to offload long-running operations (e.g., network calls, database operations) to avoid blocking the UI.
- The slide connects the Thread Pool to the Main Thread with an arrow, indicating that background threads can communicate with the Main Thread (e.g., to update the UI after a task completes).

#### **3. External Components (Purple Box)**
- This represents components outside the app process, such as other apps or system services.
- The slide shows arrows indicating communication:
  - **Call ContentProviders/Services**: The app process can call external `ContentProviders` or `Services` (e.g., accessing a calendar provider or interacting with a system service).
  - **Call Activities/Broadcast Receivers**: External components can trigger the app's `Activities` or `BroadcastReceivers` (e.g., an incoming call might trigger a `BroadcastReceiver` in your app).

---

### **Key Concepts Highlighted**

1. **Main Thread (UI Thread)**:
   - The Main Thread is responsible for all UI-related tasks and lifecycle events of Activities, Services, and BroadcastReceivers.
   - It uses a **Looper** to process a queue of messages (M1, M2), such as drawing the UI, handling clicks, or responding to lifecycle events like `onCreate()` or `onResume()`.

2. **Thread Pool**:
   - Android apps can create additional threads (e.g., T1, T2) to handle long-running tasks.
   - This aligns with the previous slide on "Long Running Operations," which advises against performing tasks like network access or database queries on the Main Thread to prevent UI blocking.
   - The Thread Pool allows these tasks to run in the background, and results can be sent back to the Main Thread for UI updates.

3. **Communication with External Components**:
   - The app process can interact with external components (e.g., other apps or system services) via `ContentProviders` or `Services`.
   - External components can also trigger the app's `Activities` or `BroadcastReceivers` (e.g., a system event like a battery warning might activate a `BroadcastReceiver`).

---

--------------------------------

-----------------

### ** Activity - runOnUiThread**

- **Activity:**
  - `void runOnUiThread(Runnable runnable)`
  - Runs the specified action on the UI thread.
  - If the current thread is the UI thread, then the action is executed immediately.
  - If the current thread is not the UI thread, the action is posted to the event queue of the UI thread.

**Explanation:**

- The `runOnUiThread` method is used to execute a `Runnable` on the UI thread (Main Thread). This is crucial because UI updates (like showing a `Toast` or updating a `TextView`) must happen on the UI thread.
- If you call this method from the UI thread, the `Runnable` runs immediately.
- If you call it from a background thread (e.g., a thread in the Thread Pool), the `Runnable` is added to the UI thread’s event queue (managed by the Looper, as seen in the Android Threading Architecture slide) and executed when the UI thread is free.

---

### **View - post and postDelayed**

- **View:**
  - `boolean post(Runnable runnable)`
    - Causes the Runnable to be added to the message queue. The runnable will be run on the user interface thread.
    - Returns true if the Runnable was successfully placed in to the message queue.
  - `boolean postDelayed(Runnable runnable, long delayMilliseconds)`
    - Causes the Runnable to be added to the message queue. The runnable will be run on the user interface thread.
    - Returns true if the Runnable was successfully placed in to the message queue.
    - Returns false on failure, usually because the looper processing the message queue is exiting.
    - If result = true does not mean the Runnable will be processed – if the looper is quit before the delivery time of the message occurs then the message will be dropped.

**Explanation:**
- These methods are called on a `View` (e.g., a `Button`, `TextView`, etc.) to schedule a `Runnable` to run on the UI thread.
- `post(Runnable)`: Adds the `Runnable` to the UI thread’s message queue immediately.
- `postDelayed(Runnable, long)`: Adds the `Runnable` to the UI thread’s message queue with a delay (in milliseconds).
- Both methods return `true` if the `Runnable` was successfully queued, but this doesn’t guarantee execution—if the app’s `Looper` quits (e.g., the app process is killed), the `Runnable` won’t run.

---

### **Process - setThreadPriority**

- **Process:**
  - `void setThreadPriority(int priority)`
    - Set the priority of the calling thread, based on Linux priorities
    - -20=high, 19=low
  - At the beginning of the run(), set the thread to use background priority by calling Process.setThreadPriority() with THREAD_PRIORITY_BACKGROUND.
  - Reduces resource competition between the Runnable object’s thread and the UI thread.

**Explanation:**
- The `Process.setThreadPriority(int)` method adjusts the priority of a thread, which affects how the system allocates CPU resources to it.
- Priority values range from -20 (highest) to 19 (lowest), based on Linux thread priorities.
- For background tasks, you should set the thread priority to `THREAD_PRIORITY_BACKGROUND` (a constant in `android.os.Process` with a value of 10) to reduce competition with the UI thread, ensuring the UI remains responsive.

---

### **Modern Alternatives (Additional Note)**

While these methods (`runOnUiThread`, `post`, `setThreadPriority`) are valid, modern Android development often uses more structured approaches for threading:
- **Executors (Java)**: Use `Executors` for background tasks and a `Handler` for UI updates:

  ```java
  Handler uiHandler = new Handler(Looper.getMainLooper());
  ExecutorService executor = Executors.newSingleThreadExecutor();
  
  public void addUser(View view) {
      String user = userName.getText().toString();
      String pass = password.getText().toString();
  
      executor.execute(() -> {
          long id = vivzHelper.insertData(user, pass);
          uiHandler.post(() -> {
              if (id < 0) {
                  Message.message(this, "Unsuccessful");
              } else {
                  Message.message(this, "Successfully Inserted A Row");
                  uiHandler.postDelayed(() -> {
                      userName.setText("");
                      password.setText("");
                  }, 2000);
              }
          });
      });
  }
  ```

These modern approaches are more maintainable and less error-prone than raw threads, but the methods in the slides are still useful for understanding the underlying mechanics.

---

-------------

-----------

![](/home/awadin/Pictures/Screenshots/Screenshot from 2025-04-18 22-08-38.png)



---

### **Title: **

The slide provides a visual representation of how a `Handler`, `Looper`, and `Message` work together to facilitate communication between threads, specifically between a background thread (Thread 1) and the Main Thread.

---

### **Diagram Breakdown**

#### **1. Main Thread (Green Box)**
- This represents the **Main Thread** (also called the UI thread), which is responsible for handling UI operations and lifecycle events in an Android app.
- Inside the Main Thread:
  - **Main Thread Queue** (Blue Box):
    - This is the message queue managed by the `Looper`.
    - It contains messages (e.g., **Message M2**) that the Main Thread processes one by one.
  - **Looper** (Green Box):
    - The `Looper` continuously loops through the message queue, pulling out messages (like M2) and dispatching them to the appropriate `Handler` for processing.
  - **Handler** (Yellow Box):
    - The `Handler` is associated with the Main Thread and is responsible for sending and processing messages in the queue.
    - It has a reference back to the Main Thread’s `Looper` and message queue, as shown by the green arrow.

#### **2. Thread 1 (Outside the Main Thread)**
- This represents a **background thread** (e.g., a thread you create to perform long-running tasks like database operations).
- **Send Message** (Red Box):
  - Thread 1 can send a message to the Main Thread’s message queue using a `Handler`.
  - The slide shows Thread 1 sending a message to the Main Thread’s queue via the `Handler`.

#### **3. Flow of Communication**
- **Step 1**: Thread 1 sends a message to the Main Thread’s queue using the `Handler` (red arrow from "Send Message" to the Main Thread Queue).
- **Step 2**: The message (e.g., M2) is added to the Main Thread’s message queue.
- **Step 3**: The `Looper` in the Main Thread pulls the message from the queue.
- **Step 4**: The `Looper` dispatches the message back to the `Handler` (green arrow from the queue back to the `Handler`).
- **Step 5**: The `Handler` processes the message (e.g., updates the UI or performs some action on the Main Thread).

---

### **Key Concepts Highlighted**

1. **Handler**:
   - A `Handler` allows you to send and process `Message` objects or `Runnable` objects associated with a thread’s message queue.
   - It is typically created in the Main Thread and tied to the Main Thread’s `Looper`, so it can enqueue messages to be processed on the Main Thread.

2. **Looper**:
   - The `Looper` is a mechanism that runs a message loop for a thread.
   - In the Main Thread, the `Looper` continuously checks the message queue for new messages and dispatches them to the appropriate `Handler`.

3. **Message Queue**:
   - The message queue holds `Message` objects (e.g., M2) or `Runnable` objects that the `Looper` processes.
   - Messages are processed in the order they are added (FIFO: First In, First Out).

4. **Thread Communication**:
   - A background thread (Thread 1) can send messages to the Main Thread’s queue using a `Handler`.
   - This is a safe way to communicate between threads, especially when you need to update the UI (which must happen on the Main Thread).

---

### **Benefits of Using a Handler**

1. **Structured Communication**:
   - The `Handler` provides a structured way to send data (via `Message` objects) between threads, which is more flexible than `runOnUiThread` or `post`.

2. **Data Passing**:
   - You can pass data in the `Message` object (e.g., `msg.arg1` to indicate success/failure).
   - You can also use `Message.obj` to pass custom objects or use `Message.setData(Bundle)` for more complex data.

3. **Reusability**:
   - The same `Handler` can be used to handle multiple types of messages by checking the `Message.what` field or other properties.

---

### **Modern Alternatives (Additional Note)**

While `Handler` and `Looper` are powerful, modern Android development often uses higher-level abstractions:
- **Executors and Handler (Java)**: Use `Executors` for background tasks and a `Handler` for UI updates, as shown in the previous response.
- **LiveData or Flow**: For reactive updates, you can use `LiveData` to observe changes from a background thread and update the UI automatically on the Main Thread.

However, understanding the `Handler` architecture is crucial because it underpins many of these modern APIs.

---

---------------------

------------------

Let’s break down the slides on the Android Thread, Looper, and Handler architecture to help you understand the concept of a **Handler**.

### What is a Handler?
A **Handler** in Android is a mechanism that allows you to communicate between threads, particularly to send and process **Message** and **Runnable** objects associated with a thread's **MessageQueue**.

#### Key Points from the First Slide:
1. **Purpose of a Handler**:
   - It lets you **send and process** `Message` and `Runnable` objects through a thread’s **MessageQueue**.
   - A Handler is **tied to a single thread** and its associated **MessageQueue**.

2. **Binding to a Thread**:
   - When you create a new Handler, it is **bound to the thread and MessageQueue** of the thread that creates it.
   - The Handler will **deliver messages and runnables** to that MessageQueue and execute them as they are dequeued.

3. **Usage**:
   - You typically need **only one Handler per activity**, and there’s no need to manually register it.

#### Key Points from the Second Slide:
1. **Role of a Handler**:
   - Your **background thread** can use the Handler to communicate with the **UI thread** (main thread), which handles the activity’s UI updates.
   - The Handler ensures that the work (messages or runnables) is executed on the UI thread.

2. **Two Main Uses of a Handler**:
   - **(1)** To **schedule messages and runnables** to be executed at some point in the future (e.g., delayed tasks).
   - **(2)** To **enqueue an action** to be performed on a different thread than the one you’re currently on (e.g., sending a task from a background thread to the UI thread).

3. **Two Options for Communication**:
   - A Handler can handle two types of objects: **Messages** (used to send data) and **Runnables** (used to execute a block of code).

### How It Fits into the Thread/Looper/Handler Architecture
- **Thread**: A thread in Android (like the main/UI thread or a background thread) can have a **Looper**.
- **Looper**: A Looper continuously loops through the **MessageQueue** of a thread, pulling out messages or runnables to process.
- **MessageQueue**: A queue that holds `Message` and `Runnable` objects to be processed by the thread.
- **Handler**: Acts as an interface to interact with the MessageQueue. It allows you to **post** (send) messages or runnables to the queue and process them when they’re dequeued by the Looper.

### Example Scenario
Imagine you’re running a background thread to download data, and you want to update the UI when the download is complete:
1. Create a **Handler** on the **main thread** (UI thread).
2. From the background thread, use the Handler to **post a Runnable** or **send a Message** to the main thread’s MessageQueue.
3. The Looper on the main thread will pick up the message/runnable and execute it, updating the UI safely.

---------

### How to Send Message Objects?
The slide outlines several methods to send `Message` objects to a thread’s **MessageQueue** via a **Handler**, along with how to process them.

#### Methods to Send Messages:
1. **`sendMessage()`**:
   - Adds the `Message` to the **MessageQueue** immediately.
   - The message is placed at the **end** of the queue (default behavior).

2. **`sendMessageAtFrontOfQueue()`**:
   - Adds the `Message` to the **MessageQueue** immediately, but places it at the **front** of the queue.
   - This gives your message **priority** over other messages in the queue, so it’s processed sooner.

3. **`sendMessageAtTime()`**:
   - Adds the `Message` to the queue to be processed at a specific time.
   - The time is specified in **system uptime milliseconds** (using `SystemClock.uptimeMillis()`).
   - Useful for scheduling a message to be handled at an exact future time.

4. **`sendMessageDelayed()`**:
   - Adds the `Message` to the queue with a **delay**.
   - The delay is specified in **milliseconds**.
   - The message will be processed after the delay (relative to the current time).

5. **`sendEmptyMessage()`**:
   - Sends an **empty `Message`** to the queue.
   - Useful when you don’t need to attach data to the message but just want to trigger an action.
   - The empty message is identified by a `what` value (an integer identifier).

#### Processing Messages:
- To handle these messages, the **Handler** must implement the **`handleMessage()`** method.
- When the Looper dequeues a `Message` from the MessageQueue, it calls `handleMessage()` on the associated Handler.
- Inside `handleMessage()`, you can process the message and perform actions, such as updating the UI (if the Handler is tied to the main thread).

### Example Workflow
1. **Create a Message**:
   - Use `Message.obtain()` to get a `Message` object (preferred to reduce object creation overhead).
   - Set data in the `Message` (e.g., `what`, `arg1`, `arg2`, or `obj` for custom objects).

2. **Send the Message**:
   - Use one of the methods above, e.g., `handler.sendMessageDelayed(msg, 1000)` to send a message with a 1-second delay.

3. **Handle the Message**:
   - In the Handler’s `handleMessage()` method, check the `what` field of the `Message` to determine the action, then update the UI or perform other tasks.

### Code Example
Here’s a simple example of sending and handling a message:

```java
Handler handler = new Handler(Looper.getMainLooper()) {
    @Override
    public void handleMessage(Message msg) {
        if (msg.what == 1) {
            // Update UI or perform action
            Log.d("Handler", "Message received!");
        }
    }
};

// Sending a message
Message msg = Message.obtain();
msg.what = 1; // Identifier for the message
handler.sendMessageDelayed(msg, 1000); // Send with 1-second delay
```

--------------------

**LiveData** is a lifecycle-aware, observable data holder class in Android, part of the Android Jetpack libraries (specifically the Architecture Components). It is designed to help manage and observe data changes in a way that respects the lifecycle of Android components like `Activities`, `Fragments`, or `Services`. This ensures that UI updates are performed efficiently and safely, avoiding common issues like memory leaks or crashes due to stopped components.

Below, I’ll explain `LiveData` in detail, covering its purpose, how it works, its benefits, and how you can use it in your app (e.g., to improve the database operations in your `MainActivity` from the previous slides). I’ll also connect it to the threading concepts we’ve discussed.

---

### **What is LiveData?**

- **Definition**: `LiveData` is a data holder class that allows you to observe changes to data. It is lifecycle-aware, meaning it only updates observers (e.g., UI components) when they are in an active state (e.g., `STARTED` or `RESUMED`).
- **Purpose**: It provides a reactive way to update the UI when data changes, while ensuring that updates are lifecycle-safe and thread-safe.
- **Library**: Part of `androidx.lifecycle:lifecycle-livedata` (or `androidx.lifecycle:lifecycle-livedata-ktx` for Kotlin extensions).

---

### **Key Features of LiveData**

1. **Lifecycle Awareness**:
   - `LiveData` only notifies observers (e.g., an `Activity` or `Fragment`) when they are in an active lifecycle state (`STARTED` or `RESUMED`).
   - If the observer’s lifecycle is `STOPPED` or `DESTROYED` (e.g., the `Activity` is in the background or destroyed), `LiveData` won’t send updates, preventing crashes like trying to update a UI element that no longer exists.

2. **Observable**:
   - You can attach observers to a `LiveData` object to be notified when the data changes.
   - When the data held by `LiveData` changes, all active observers are automatically notified, and the UI can be updated.

3. **Thread Safety**:
   - `LiveData` ensures that data updates are delivered to observers on the Main Thread, which is crucial for UI updates.
   - You can update `LiveData` from any thread (e.g., a background thread), and it will safely dispatch the update to the Main Thread.

4. **Sticky Behavior**:
   - `LiveData` holds the latest value and delivers it to new observers as soon as they start observing.
   - This “sticky” behavior ensures that a new observer (e.g., after a screen rotation) immediately receives the most recent data without needing to fetch it again.

5. **Integration with Other Jetpack Components**:
   - Works seamlessly with `ViewModel` to manage UI-related data in a lifecycle-aware way.
   - Can be used with Room (Android’s database library) to observe database changes reactively.

---

### **How Does LiveData Work?**

#### **Core Components**
- **LiveData Object**: Holds a single piece of data (e.g., a `String`, a `List`, or a custom object). It can be of type `LiveData<T>` where `T` is the type of data (e.g., `LiveData<String>`).
- **Observer**: An object (typically an `Activity` or `Fragment`) that observes the `LiveData` for changes. It implements the `Observer<T>` interface and defines what to do when the data changes.
- **LifecycleOwner**: The `LiveData` uses a `LifecycleOwner` (e.g., an `Activity` or `Fragment`) to determine the lifecycle state of the observer and decide whether to deliver updates.

#### **Basic Workflow**
1. **Create a LiveData Object**:
   - You create a `LiveData` instance (e.g., `MutableLiveData`, a subclass of `LiveData` that allows you to modify the data).
   - Example: `MutableLiveData<String> message = new MutableLiveData<>();`

2. **Set or Update the Data**:
   - Use `setValue(T)` (synchronous, must be called on the Main Thread) or `postValue(T)` (asynchronous, can be called from any thread) to update the `LiveData`’s value.
   - Example: `message.setValue("Success");` or `message.postValue("Success");`

3. **Observe the LiveData**:
   - Attach an observer to the `LiveData` using `observe(LifecycleOwner, Observer)`.
   - The observer will be notified whenever the data changes, but only if the `LifecycleOwner` is in an active state.
   - Example:
     ```java
     message.observe(this, new Observer<String>() {
         @Override
         public void onChanged(String newMessage) {
             // Update UI
             Toast.makeText(this, newMessage, Toast.LENGTH_SHORT).show();
         }
     });
     ```

4. **Lifecycle Management**:
   - When the `LifecycleOwner` (e.g., `Activity`) moves to `STOPPED` (e.g., goes to the background), `LiveData` stops sending updates.
   - When the `LifecycleOwner` is `DESTROYED`, the observer is automatically removed, preventing memory leaks.

---

### **Types of LiveData**

1. **LiveData<T>**:
   - The base class, which is read-only. You can observe it but cannot modify its value directly.
   - Typically used when you only need to expose data to observers without allowing external modifications.

2. **MutableLiveData<T>**:
   - A subclass of `LiveData` that allows you to modify the data using `setValue(T)` or `postValue(T)`.
   - Commonly used when you need to update the data (e.g., in a `ViewModel`).

3. **MediatorLiveData<T>**:
   - A subclass of `MutableLiveData` that allows you to merge multiple `LiveData` sources.
   - Useful for combining data from multiple sources (e.g., database and network) and exposing a single `LiveData` to the UI.
   - Example:
     ```java
     MediatorLiveData<String> mediator = new MediatorLiveData<>();
     mediator.addSource(liveData1, value -> mediator.setValue(value + " processed"));
     mediator.addSource(liveData2, value -> mediator.setValue(value + " processed"));
     ```

---

### **LiveData vs. Other Threading Methods**

Let’s connect `LiveData` to the threading concepts we’ve discussed in your slides (e.g., `Handler`, `runOnUiThread`, etc.):

1. **Compared to `runOnUiThread` or `Handler`**:
   - In the "Some Threading Methods" and "Thread Looper Handler Architecture" slides, you used `runOnUiThread` or a `Handler` to update the UI from a background thread.
   - `LiveData` simplifies this by handling the thread switching for you. When you call `postValue` from a background thread, `LiveData` ensures the observer is notified on the Main Thread.

2. **Compared to Manual Threading**:
   - In your `MainActivity`, you used a `Thread` to perform a database operation and then used `runOnUiThread` or a `Handler` to update the UI.
   - With `LiveData`, you can perform the database operation in a background thread, update a `MutableLiveData` with the result, and have the UI automatically update when the `LiveData` changes—no manual thread switching required.

3. **Lifecycle Safety**:
   - Unlike `Handler` or `runOnUiThread`, `LiveData` ensures that UI updates only happen when the `Activity` or `Fragment` is active, preventing crashes if the `Activity` is destroyed.

---

### **Using LiveData in Your Code**

Let’s apply `LiveData` to your `MainActivity` to handle the database operation (`vivzHelper.insertData`) and UI updates (`Message.message`) more efficiently. We’ll use a `ViewModel` to hold the `LiveData`, which is the recommended practice.

#### **Step 1: Add Dependencies**
Ensure you have the `LiveData` and `ViewModel` dependencies in your `build.gradle`:
```gradle
implementation "androidx.lifecycle:lifecycle-livedata:2.8.6"
implementation "androidx.lifecycle:lifecycle-viewmodel:2.8.6"
```

#### **Step 2: Create a ViewModel**
The `ViewModel` will hold the `LiveData` and handle the database operation.

```java
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {
    private final VivzDatabaseAdapter vivzHelper;
    private final MutableLiveData<String> message = new MutableLiveData<>();

    public UserViewModel(Context context) {
        vivzHelper = new VivzDatabaseAdapter(context);
    }

    public LiveData<String> getMessage() {
        return message;
    }

    public void addUser(String user, String pass) {
        new Thread(() -> {
            long id = vivzHelper.insertData(user, pass);
            if (id < 0) {
                message.postValue("Unsuccessful");
            } else {
                message.postValue("Successfully Inserted A Row");
            }
        }).start();
    }
}
```

- `message` is a `MutableLiveData` that holds the result message.
- `getMessage()` exposes the `MutableLiveData` as a read-only `LiveData` to the `Activity`.
- `addUser` performs the database operation in a background thread and updates the `LiveData` with `postValue`.

#### **Step 3: Update MainActivity to Observe the LiveData**
Modify `MainActivity` to use the `ViewModel` and observe the `LiveData`.

```java
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends ComponentActivity {
    EditText userName, password;
    UserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.userNameValue);
        password = findViewById(R.id.passwordValue);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Observe the LiveData for changes
        viewModel.getMessage().observe(this, message -> {
            Message.message(this, message);
            if (message.startsWith("Successfully")) {
                new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                    userName.setText("");
                    password.setText("");
                }, 2000);
            }
        });
    }

    public void addUser(View view) {
        String user = userName.getText().toString();
        String pass = password.getText().toString();
        viewModel.addUser(user, pass);
    }
}
```

- The `ViewModel` is initialized using `ViewModelProvider`.
- The `Activity` observes the `LiveData` with `observe`, and whenever the `LiveData` changes, the UI is updated with `Message.message`.
- If the operation is successful, the `EditText` fields are cleared after a 2-second delay (using a `Handler` for simplicity, though `LiveData` could handle this too).

#### **Step 4: Update VivzDatabaseAdapter**
Ensure `VivzDatabaseAdapter` is properly initialized with the `Context` passed from the `ViewModel`. The code for `VivzDatabaseAdapter` and `VivzHelper` remains the same as in your previous slides.

---

### **How This Improves Your Code**

1. **Thread Safety**:
   - The database operation runs in a background thread, and `LiveData` ensures the UI update (`Message.message`) happens on the Main Thread.

2. **Lifecycle Safety**:
   - If the `Activity` is destroyed (e.g., due to a screen rotation), `LiveData` automatically stops sending updates, preventing crashes.
   - The `ViewModel` survives configuration changes (like screen rotations), so the `LiveData` retains its data, and the `Activity` can re-observe it after recreation.

3. **Cleaner Code**:
   - You no longer need to manually manage threads and UI updates with `runOnUiThread` or a `Handler`.
   - The `ViewModel` encapsulates the business logic, making the `Activity` responsible only for UI updates.

4. **Scalability**:
   - You can easily extend this to handle more complex scenarios, like observing database changes with Room (which natively supports `LiveData`).

---

### **Advanced Use Case: LiveData with Room**

Since you’re working with SQLite, you can use the Room persistence library, which integrates with `LiveData` to observe database changes reactively. Here’s a brief example:

#### **Step 1: Define a Room Entity and DAO**
```java
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "VIVZTABLE")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
}

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.lifecycle.LiveData;
import java.util.List;

@Dao
public interface UserDao {
    @Insert
    long insert(User user);

    @Query("SELECT * FROM VIVZTABLE")
    LiveData<List<User>> getAllUsers();
}
```

#### **Step 2: Update ViewModel to Use Room**
```java
public class UserViewModel extends ViewModel {
    private final UserDao userDao;
    private final LiveData<List<User>> allUsers;
    private final MutableLiveData<String> message = new MutableLiveData<>();

    public UserViewModel(Application application) {
        AppDatabase db = Room.databaseBuilder(application, AppDatabase.class, "vivzdatabase").build();
        userDao = db.userDao();
        allUsers = userDao.getAllUsers();
    }

    public LiveData<String> getMessage() {
        return message;
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public void addUser(String name, String password) {
        new Thread(() -> {
            long id = userDao.insert(new User(name, password));
            if (id < 0) {
                message.postValue("Unsuccessful");
            } else {
                message.postValue("Successfully Inserted A Row");
            }
        }).start();
    }
}
```

- `getAllUsers()` returns a `LiveData<List<User>>` that automatically updates whenever the database changes.

#### **Step 3: Observe Database Changes in MainActivity**
```java
public class MainActivity extends ComponentActivity {
    EditText userName, password;
    UserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.userNameValue);
        password = findViewById(R.id.passwordValue);

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Observe the message
        viewModel.getMessage().observe(this, message -> {
            Message.message(this, message);
            if (message.startsWith("Successfully")) {
                new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                    userName.setText("");
                    password.setText("");
                }, 2000);
            }
        });

        // Observe the list of users
        viewModel.getAllUsers().observe(this, users -> {
            // Update UI with the list of users (e.g., display in a RecyclerView)
            Log.d("MainActivity", "User list updated: " + users.size());
        });
    }

    public void addUser(View view) {
        String user = userName.getText().toString();
        String pass = password.getText().toString();
        viewModel.addUser(user, pass);
    }
}
```

- The `Activity` now observes both the `message` (for success/failure) and the `allUsers` `LiveData` (to react to database changes).
- If another part of the app modifies the database, the `allUsers` `LiveData` will automatically notify the `Activity` to update the UI.

---

### **Benefits of LiveData**

1. **Simplified Threading**:
   - No need to manually switch threads for UI updates—`LiveData` handles it for you.

2. **Lifecycle Awareness**:
   - Prevents memory leaks and crashes by only updating active observers.

3. **Reactive UI Updates**:
   - Automatically updates the UI when data changes, reducing boilerplate code.

4. **Configuration Change Handling**:
   - When used with `ViewModel`, `LiveData` survives configuration changes (e.g., screen rotations), ensuring the UI can restore its state.

5. **Integration with Room**:
   - Room’s `@Query` methods can return `LiveData`, making it easy to build reactive UIs that update when the database changes.


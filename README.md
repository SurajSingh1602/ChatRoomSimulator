# ChatRoomSimulator
Project Title: Java Concurrency-Based Chatroom Simulator

# Overview:
The Chatroom Simulator is a console-based Java application designed to emulate the fundamental behaviors of a multi-user chat environment. It focuses on demonstrating concurrent operations, message flow management, and user presence tracking without needing a full-fledged network or GUI. The simulator generates dynamic user activity, allowing observation of messages being sent and received in a simulated real-time setting.

# Core Features:

1. User Simulation (UserSimulator):

Creates virtual users that autonomously join, send messages, and leave the chatroom.
Simulates various user actions (e.g., typing delays, random message selection) using Java's Random utility.
Each user operates independently within its own thread (Runnable), showcasing concurrent execution.

2. User Status Management (UserStatus):

Tracks the online/offline status of each simulated user using a simple boolean flag.
Provides methods for users to "go online" and "go offline," reflecting their presence in the chat.

3. Message Handling (MessageHandler):

Manages the lifecycle of messages from creation to "broadcast."
Utilizes a ConcurrentLinkedQueue for thread-safe handling of incoming messages from multiple users.
Maintains a historical log of all messages (Collections.synchronizedList), allowing retrieval of recent chat history.
Simulates message distribution by printing messages to the console as they are processed.

4. Chatroom Orchestration (ChatRoom):

The central component that sets up and manages the entire simulation.
Responsible for creating the specified number of users and initiating their respective UserSimulator threads.
Employs a java.util.concurrent.ExecutorService (thread pool) to efficiently manage and execute multiple user simulation threads concurrently.
Controls the overall simulation duration and periodically updates the console with chat status (online users, recent messages, total messages).
Gracefully shuts down all user simulators and the thread pool upon completion.

# Technical Stack:

1. Language: Java
2. Concurrency: java.lang.Runnable, java.util.concurrent.ExecutorService, java.util.concurrent.TimeUnit, java.util.concurrent.atomic.AtomicInteger
3. Collections: java.util.List, java.util.Map, java.util.ArrayList, java.util.concurrent.ConcurrentHashMap, java.util.concurrent.ConcurrentLinkedQueue, java.util.Collections.synchronizedList
4. Time & Date: java.time.LocalDateTime, java.time.format.DateTimeFormatter

#How it Works:

The App class serves as the entry point, prompting the user for the number of participants and the simulation duration. It then initializes a ChatRoom instance. The ChatRoom sets up a MessageHandler and generates multiple UserSimulator instances. Each UserSimulator is assigned a UserStatus object and runs as a separate thread, autonomously performing actions like joining, sending messages (via the MessageHandler), and leaving. The ChatRoom continuously polls the MessageHandler to process and "broadcast" new messages while also providing periodic status updates of the chatroom.

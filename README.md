# QueuesManagement

## Overview
Queues Management With Threads is a Java project designed to simulate and manage queues efficiently using multithreading. This project aims to demonstrate the power of concurrent programming in handling real-world scenarios where tasks can be processed in parallel to improve performance and efficiency.

## Objective
The objective was to develop a system that takes parameters as input and simulates a queue management scenario, demonstrating proficiency with threads and synchronization mechanisms.

## Problem Analysis and Modeling
The system models a scenario with a given number of clients (N) and queues/servers (Q), managing their arrival and processing using multithreading—one thread per queue and a main thread for management.

## Design
The design employs Object-Oriented Programming principles, utilizing a Scheduler to manage Server instances. Data structures such as BlockingQueue and AtomicInteger ensure thread safety.

## Implementation
- **Task**: Represents a client with ID, arrival time, and processing time.
- **Server**: Simulates a server processing tasks in a queue.
- **Scheduler**: Manages task assignment to servers based on selected strategies.
- **SimulationManager**: Manages the simulation, updating GUI with the simulation state.

## Features
- **Multithreading:** Utilizes Java's concurrency framework to manage and process multiple queues in parallel.
- **Efficient Queue Management:** Implements algorithms to distribute tasks across queues effectively, minimizing wait times and optimizing resource utilization.
- **Scalability:** Designed to scale with the number of tasks and threads, allowing for efficient processing of a large number of tasks.
- **Real-world Simulation:** Provides a simulation environment to test and observe the behavior of queues under various conditions.

## Results and Conclusions
The project highlights the importance of thread safety and synchronization in a multithreaded environment. It demonstrates the application of OOP concepts to simplify complex problems.

# 🤖 FSO – Concurrent Control System for LEGO EV3 Robot

> Practical assignment for the course **Fundamentos de Sistemas Operativos (FSO)**  
> BSc in Computer Engineering and Multimedia – **ISEL**  
> Winter semester **2025 / 2026**

---

## 👥 Authors

- **Name:** Miguel Cordeiro — nº 49765 — LEIM32D  
- **Name:** Ricardo Ferreira — nº 51324 — LEIM32D  
- **Instructor:** Eng.º Carlos Carvalho  

---

## 📝 Project Overview

This project consists of the development of a **concurrent and multitasking control system** for a **LEGO EV3 robot**, designed to consolidate core concepts of **Operating Systems**, namely:

- concurrency and multithreading  
- synchronization and mutual exclusion  
- producer–consumer coordination  
- priority handling between concurrent tasks  

Starting from a base system developed in a previous assignment, this second practical work significantly extends the architecture by introducing **new concurrent tasks**, increasing system complexity and realism.

The final system is capable of **coordinating multiple autonomous and manual behaviours**, while ensuring **safe and exclusive access to the robot hardware**.

---

## 🎥 Project Demo

> 👉 **Click the image above** to watch the platform demonstration.
[![Watch the demo on YouTube](https://img.youtube.com/vi/vnhXgVWCtPE/hqdefault.jpg)](https://www.youtube.com/watch?v=vnhXgVWCtPE)

---

## 🎯 Learning Objectives

- Apply **concurrency and synchronization mechanisms** in a real-time system  
- Design and manage **multiple cooperating threads**  
- Implement **mutual exclusion** over shared resources  
- Extend a system incrementally while preserving architectural coherence  
- Experience the practical challenges of concurrency in hardware-interacting systems  

---

## 🧠 System Architecture Overview

The system follows a **producer–consumer architecture**, where:

- Multiple **producer tasks** generate robot commands  
- A single **consumer task** executes commands on the robot  
- A **circular buffer** ensures ordered and synchronized communication  

Core reusable components include:

- `BufferCircular` – synchronized producer–consumer buffer  
- `Servidor` – consumer task responsible for command execution  
- `Tarefa` – abstract base class implementing a task state machine  

The architecture evolved incrementally, allowing new behaviours to be integrated without redesigning the system from scratch.

---

## 🧪 Development

The project was developed in stages, progressively increasing concurrency and task interaction.

### 1. Producer–Consumer Model

- Implementation of a **circular buffer** using semaphores  
- Guarantees sequential execution of commands regardless of origin  
- Supports multiple producers concurrently  

---

### 2. Concurrent Task Execution & Mutual Exclusion

- Multiple tasks execute in parallel:
  - manual commands (GUI)
  - random movement generation
  - command reproduction
- Access to the **LEGO EV3 robot** is protected using **mutual exclusion**, ensuring that only one task controls the robot at a time  

---

### 3. Obstacle Avoidance Task

- Introduction of a high-priority task `EvitarObstaculo`  
- Continuously monitors the robot’s touch sensor  
- Automatically interrupts normal execution to perform corrective manoeuvres  
- Demonstrates **task prioritization** and controlled preemption  

---

### 4. Command Recording & Playback

- Commands executed by the robot can be:
  - **recorded** to a file
  - **replayed** later automatically  
- Integration required careful synchronization to avoid:
  - duplicated commands  
  - buffer overflow  
  - inconsistent system states  

---

### 5. Graphical Interfaces

The system includes multiple interfaces:

- **Main GUI**
  - manual control (forward, turn, stop, etc.)
  - robot connection management  
  - random movement activation  

- **Recorder GUI**
  - record and replay command sequences  
  - manage files and recording state  

- **Simulation Console**
  - simulate robot behaviour without physical hardware  
  - manually trigger obstacle avoidance  

---

## ⚙️ Technologies & Concepts

- **Language:** Java  
- **Concurrency:** Threads, Semaphores, synchronized blocks  
- **Patterns:** Producer–Consumer, State Machine  
- **Hardware:** LEGO EV3 (real or simulated)  
- **GUI:** Java Swing  

---

## 📌 Conclusion

This project successfully consolidates fundamental concepts of **Operating Systems** through the development of a realistic and concurrent robotic control system.

By integrating multiple tasks with different priorities and responsibilities, the system highlights the **complexity of synchronization**, especially in environments that interact with real hardware.

Despite some remaining limitations, the final solution demonstrates a solid and well-structured approach to concurrency, task coordination and safe resource sharing, fulfilling the learning objectives of the course.

---

## 📄 Report

All design decisions, architecture diagrams and implementation details are documented in the final report:

```text
docs/FSO_TP2_RelatórioFinal_grupo10_A49765_A51324_A51639.pdf

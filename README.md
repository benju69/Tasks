# ✅ Simple Tasks

A clean, minimal Android to-do list app built with modern Android technologies.

## Overview

**Simple Tasks** is a lightweight, intuitive task manager that lets you create, organise, and track your daily to-dos without any unnecessary complexity. It supports priority levels, task filtering, and a dark mode — all in a Material3-styled Jetpack Compose UI.

---

## Features

- **Add, edit & delete tasks** — Full CRUD lifecycle for every task
- **Priority levels** — Assign LOW, MEDIUM, or HIGH priority to any task
- **Task filtering** — View All, Active, or Completed tasks at a glance
- **Toggle completion** — Mark tasks done (or undone) with a single tap
- **Dark mode** — Persistent dark/light theme toggle via Settings
- **Offline-first** — All data stored locally with Room (SQLite); no account required

---

## Screenshots

> _Screenshots will be added once the app is running on a device/emulator._

---

## Tech Stack

| Layer | Technology                       |
|---|----------------------------------|
| Language | Kotlin                           |
| UI | Jetpack Compose + Material 3     |
| Architecture | Clean Architecture + MVVM        |
| Dependency Injection | Hilt (Dagger)                    |
| Database | Room (SQLite)                    |
| Preferences | Jetpack DataStore                |
| Async | Kotlin Coroutines + Flow         |
| Navigation | Jetpack Navigation Compose       |
| Testing | JUnit 5 (JUnit Jupiter) + Kotlin Coroutines Test |
| Min SDK | 26 (Android 8.0)                 |
| Target SDK | 36 (Android 16)                  |

---

## Project Structure

```
Tasks/
├── app/            # Application entry point, DI setup
├── core/           # Shared utilities (coroutine dispatchers, test helpers)
├── data/           # Data layer — Room database, repositories, use-case impls
├── domain/         # Business logic — models, repository interfaces, use-cases
├── feature/        # UI features — Task List, Task Editor, Settings
└── ui/             # Shared Compose components and Material3 theme
```

### Architecture Diagram

```
┌─────────────────────────────────────────────┐
│                  feature/                    │
│   TaskListScreen  TaskEditorScreen  Settings │
│        ↕ ViewModel (MVVM)                   │
└─────────────────┬───────────────────────────┘
                  │ Use Cases
┌─────────────────▼───────────────────────────┐
│                  domain/                     │
│      Models · Repository interfaces         │
│      Use-case interfaces                    │
└─────────────────┬───────────────────────────┘
                  │ Implementations
┌─────────────────▼───────────────────────────┐
│                   data/                      │
│   Room DB · TaskDao · TaskMapper             │
│   Repository impls · Use-case impls         │
└─────────────────────────────────────────────┘
```

---

## Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17+
- Android SDK with API level 26+

### Build & Run

```bash
# Clone the repository
git clone https://github.com/benju69/Tasks.git
cd Tasks

# Open in Android Studio, or build from command line:
./gradlew assembleDebug

# Run unit tests
./gradlew test
```

---

## Running Tests

```bash
# All unit tests
./gradlew test

# Specific module
./gradlew :feature:test
./gradlew :data:test
```

---

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/my-feature`)
3. Commit your changes (`git commit -m 'Add my feature'`)
4. Push to the branch (`git push origin feature/my-feature`)
5. Open a Pull Request

---

## License

This project is open source. See [LICENSE](LICENSE) for details.

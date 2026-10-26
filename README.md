# Social Network Application

A Java-based desktop social networking application designed with a layered architecture (Domain – Repository – Service – UI), featuring data persistence, topologic graph algorithms for communities, and dynamic repository switching via design patterns.

## Table of Contents
- Features
- Architecture & Design Patterns
- Project Structure
- Configuration & Persistence (Factory & Exceptions)
- Requirements & Rulings
- Implementation Details

---

## Features
- **User Management (CRUD):** Add, update, delete, and list users with strict validation.
- **Friendship Management:** Establish/remove friendships between existing users with ISO timestamp tracking (`LocalDateTime`).
- **Cascade Deletion:** Deleting a user automatically removes all associated friendships to maintain file consistency.
- **Community Analysis:** Graph algorithms (DFS/BFS) to calculate connected components (number of communities) and the most active community (longest simple path).
- **Extensible Storage:** Seamlessly toggle between Memory, File, and Database persistence layers using external configuration.

---

## Architecture & Design Patterns
```text
┌─────────────────┐
│    ConsoleUI    │  ◄── Console User Interface
└────────┬────────┘
│
┌────────▼────────┐
│  Service Layer  │  ◄── UserService, FriendshipService, ComunityService
└────────┬────────┘
│
┌────────▼────────┐
│ Repository (I)  │  ◄── InMemoryRepository, AbstractFileRepository, DbRepositories
└────────┬────────┘
│
┌────────▼────────┐
│  Domain Layer   │  ◄── User, Friendship, Entity
└─────────────────┘
```

### Design Patterns Used
* **Factory Pattern (Simple Factory):** `RepositoryFactory` centralizes the creation of repository instances (`createUserRepository`, `createFriendshipRepository`) based on `config.properties`.
* **Template Method Pattern:** `AbstractFileRepository` handles the file I/O skeleton (`loadData`, `saveData`), delegating line-to-entity parsing (`extractEntity`, `createEntityAsString`) to subclasses (`UserFileRepository`, `FriendshipFileRepository`).
* **Repository Pattern:** `Repository<ID, E>` interface hides storage implementation details from services.
* **Strategy Pattern:** Used by services to switch between memory/file/database storage transparently.

### SOLID Principles
* **SRP (Single Responsibility Principle):** Validators only validate data; Repositories only handle storage; Services handle business logic; UI handles user interaction.
* **OCP (Open/Closed Principle):** Adding a new storage type (e.g., Database via JDBC) requires creating new Repository implementations without altering existing UI or Service code.
* **LSP (Liskov Substitution Principle):** Any `Repository<ID, E>` implementation (`InMemoryRepository`, `UserFileRepository`, `UserDbRepository`) can replace the abstract interface without breaking service logic.
* **DIP (Dependency Inversion Principle):** High-level modules (`Main`, `Service`, `UI`) depend on abstractions (`Repository<ID, E>`), not on concrete implementations.

---

## Project Structure
```text
├── domain/
│   ├── Entity.java                   # Generic base class for domain models
│   ├── User.java                     # User entity
│   ├── Friendship.java               # Friendship entity (includes LocalDateTime)
│   ├── Exceptions/                   # RepositoryException, ServiceException
│   └── Validation/                   # Validator interface, UserValidator, FriendshipValidator
├── Repository/
│   ├── Repository.java               # Generic Repository interface
│   ├── InMemoryRepository.java       # In-memory storage (HashMap)
│   ├── AbstractFileRepository.java   # Abstract Template Method file storage
│   ├── UserFileRepository.java       # User text-file parser
│   ├── FriendshipFileRepository.java # Friendship text-file parser
│   ├── UserDbRepository.java         # Skeleton for JDBC database repository
│   ├── FriendshipDbRepository.java   # Skeleton for JDBC database repository
│   ├── RepositoryFactory.java        # Simple Factory for repository creation
│   └── RepositoryType.java           # Enum (MEMORY, FILE, DATABASE)
├── Service/
│   ├── UserService.java              # User management & cascading logic
│   ├── FriendshipService.java        # Friendship management
│   └── ComunityService.java          # Graph analysis (DFS/BFS)
├── ui/
│   └── ConsoleUI.java                # Command-line interface
├── config.properties                 # System storage configuration
└── Main.java                         # Application entry point
```

---

## Configuration & Persistence

### 1. Repository Factory & Database Transition
* **How Factory Works:** At startup, `RepositoryFactory` reads `repository.type` and configuration properties from `config.properties` via `java.util.Properties`.
* **Database Transition:** Skeleton classes (`UserDbRepository`, `FriendshipDbRepository`) exist and throw `UnsupportedOperationException`. To switch to database persistence, update `config.properties`:
  ```properties
  repository.type=DATABASE
  db.url=jdbc:postgresql://localhost:5432/socialnetwork
  db.user=postgres
  db.password=postgres
   ```

### 2. Missing Files & Corrupted Lines
* Missing File Handling: If a file specified in config.properties does not exist at startup, the repository starts empty without throwing an exception. The file is created automatically upon the first save operation.

* Corrupted Lines: Corrupted or invalid lines encountered during loadData() are reported to System.err (with line number and error cause) and safely skipped. Loading continues for remaining valid data to ensure application stability.

### 3. Exception Handling
* Custom Unchecked Exceptions: All custom exceptions (ValidationException, RepositoryException) extend RuntimeException.

* Exception Chaining: Low-level exceptions (IOException, DateTimeParseException, NumberFormatException) are wrapped into RepositoryException using super(message, cause).

* UI-Level Catching: Exceptions are propagated up to ConsoleUI where user-friendly messages are displayed.No empty catch blocks or raw e.printStackTrace() are used.

* Implementation Details
* Hashing (hashCode / equals): Overridden in Entity and domain models using ID. Utilized by HashMap/HashSet in InMemoryRepository and ComunityService for 
O(1) fast lookups.

* ISO 8601 Date Format: Friendship creation timestamps are stored as LocalDateTime strings (e.g., 2026-10-19T10:30).

* Resource Safety: All file streams use try-with-resources blocks (BufferedReader, BufferedWriter) to guarantee automatic closure.

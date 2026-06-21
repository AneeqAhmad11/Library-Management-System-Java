# Library Management System — Java OOP

> A full-featured Library Management System built in **Java**, demonstrating core Object-Oriented Programming concepts including inheritance, abstraction, polymorphism, encapsulation, and interfaces.

---

## OOP Concepts Demonstrated

| Concept | Where Used |
|---------|-----------|
| **Encapsulation** | All model classes (`Book`, `Member`, `Person`) use private fields with public getters/setters |
| **Inheritance** | `Member` and `Librarian` both extend the abstract `Person` class |
| **Abstraction** | `Person` is an abstract class with abstract methods `getRole()` and `displayInfo()` |
| **Polymorphism** | `getRole()` and `displayInfo()` are overridden differently in `Member` vs `Librarian` |
| **Interfaces** | `LibraryService` implements `Manageable<Book>` and `Searchable<Book>` |
| **Generics** | `Searchable<T>` and `Manageable<T>` are generic interfaces |
| **Collections** | `ArrayList` used for books, members, and transactions |
| **File I/O** | Java Serialization (`ObjectOutputStream`) for persistent data storage |
| **Exception Handling** | `try-catch` in `DataStorage` for robust file operations |

---

## Project Structure

```
Library-Management-System-Java/
├── src/
│   └── library/
│       ├── LibraryApp.java          ← Main entry point + all menus
│       ├── interfaces/
│       │   ├── Searchable.java      ← Generic search interface
│       │   └── Manageable.java      ← Generic CRUD interface
│       ├── model/
│       │   ├── Person.java          ← Abstract base class
│       │   ├── Member.java          ← Extends Person
│       │   ├── Librarian.java       ← Extends Person
│       │   ├── Book.java            ← Book entity with serialization
│       │   └── Transaction.java     ← Borrow/return record
│       ├── service/
│       │   └── LibraryService.java  ← Core business logic
│       └── util/
│           ├── DataStorage.java     ← File persistence (Serialization)
│           ├── DateUtil.java        ← Date helpers, ID generation
│           └── ConsoleUI.java       ← All console I/O helpers
├── data/                            ← Auto-created; stores .dat files
├── Makefile
└── README.md
```

---

## Features

**Book Management**
- Add, view, update, and remove books
- Search by title, author, or genre
- Track total copies and available copies

**Member Management**
- Register, update, suspend/activate, and remove members
- Standard and Premium membership types
- Borrow limit: 3 books per member

**Borrow & Return**
- Issue books to members with due date (14-day loan period)
- Return books with automatic overdue fine calculation
- View all currently overdue books

**Fine Management**
- PKR 10 per day overdue fine
- Block borrowing if outstanding fines exist
- Fine payment with partial payment support

**Transaction History**
- View all transactions
- Filter by member
- View full details of any transaction

**Reports**
- Library summary (total books, members, issued, overdue)
- Top 5 most borrowed books
- Overdue report
- Members with outstanding fines

**Data Persistence**
- All data saved automatically to `data/` using Java Serialization
- Data survives application restarts
- Sample data loaded on first run (10 books, 5 members)

---

## How to Run

### Prerequisites
- Java JDK 17 or higher
- `make` (optional — or compile manually)

### Option 1: Using Make
```bash
git clone https://github.com/AneeqAhmad11/Library-Management-System-Java.git
cd Library-Management-System-Java
make run
```

### Option 2: Manual Compilation
```bash
git clone https://github.com/AneeqAhmad11/Library-Management-System-Java.git
cd Library-Management-System-Java

# Compile
mkdir -p out
javac -d out -sourcepath src $(find src -name "*.java")

# Run
java -cp out library.LibraryApp
```

### Option 3: IntelliJ IDEA
1. Open IntelliJ → **File → Open** → select this folder
2. Mark `src` as **Sources Root** (right-click → Mark Directory as → Sources Root)
3. Run `library.LibraryApp`

---

## Sample Accounts (Pre-loaded)

The system seeds sample data on first launch:

**Books pre-loaded:** 10 books (fiction, programming, algorithms, history)

**Members pre-loaded:**

| Member ID | Name | Type |
|-----------|------|------|
| MEM-0001 | Ali Hassan | Standard |
| MEM-0002 | Sana Tariq | Premium |
| MEM-0003 | Usman Ghani | Standard |
| MEM-0004 | Ayesha Malik | Premium |
| MEM-0005 | Bilal Ahmed | Standard |

---

## Class Hierarchy

```
Person (abstract)
├── Member      → can borrow books, has fines
└── Librarian   → admin, manages the system

LibraryService
├── implements Manageable<Book>
│       add(), remove(), update(), displayAll()
└── implements Searchable<Book>
        searchByName(), findById()
```

---

## Author

**Aneeq Ahmad** — FA23-BSE-033A
BS Software Engineering, COMSATS University Islamabad, Lahore Campus

- GitHub: [@AneeqAhmad11](https://github.com/AneeqAhmad11)
- LinkedIn: [linkedin.com/in/aneeqahmad11](https://linkedin.com/in/aneeqahmad11)

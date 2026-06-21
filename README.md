# Library Management System — Java OOP

> A console-based Library Management System built in Java demonstrating all four pillars of OOP: **Encapsulation, Inheritance, Polymorphism, and Abstraction**. Developed as an OOP course project — BS Software Engineering, COMSATS University Islamabad, Lahore Campus.

---

## OOP Concepts Demonstrated

| Concept | Where Applied |
|---|---|
| **Encapsulation** | Private fields in all model classes, accessed via getters/setters |
| **Abstraction** | `Person` is abstract with abstract methods `getRole()` and `displayInfo()` |
| **Inheritance** | `Member` and `Librarian` both extend `Person` |
| **Polymorphism** | `getRole()` and `displayInfo()` overridden in each subclass; `LibraryService` referenced via interfaces |
| **Interfaces** | `Manageable<T>` (CRUD) and `Searchable<T>` (search) both implemented by `LibraryService` |
| **Generics** | Interfaces use `<T>` type parameters |
| **Collections** | `ArrayList` for books, members, transactions |
| **File I/O** | Java Object Serialization for persistence across runs |
| **Exception Handling** | `try-catch` in `DataStorage` prevents crashes on missing/corrupt files |

---

## Features

- Add / update / remove books and view full catalog
- Search books by title, author, or genre
- Register / update / remove / suspend members
- Issue books to members (validates availability, borrow limit, outstanding fines)
- Return books with automatic overdue fine calculation (PKR 10/day)
- Transaction history — all or filtered by member
- Fine management — view and pay outstanding fines
- Reports — library summary, top borrowed books, overdue list, members with fines
- Sample data auto-seeded on first run (10 books, 5 members)

---

## Class Hierarchy

```
           Person  (abstract class)
          /       \
       Member   Librarian

LibraryService implements Manageable<Book>, Searchable<Book>
```

---

## Project Structure

```
Library-Management-System-Java/
├── src/
│   └── library/
│       ├── LibraryApp.java          ← Main entry point + all menus
│       ├── interfaces/
│       │   ├── Searchable.java
│       │   └── Manageable.java
│       ├── model/
│       │   ├── Person.java          ← Abstract base class
│       │   ├── Member.java          ← extends Person
│       │   ├── Librarian.java       ← extends Person
│       │   ├── Book.java
│       │   └── Transaction.java
│       ├── service/
│       │   └── LibraryService.java  ← Core business logic
│       └── util/
│           ├── DataStorage.java     ← File I/O (serialization)
│           ├── DateUtil.java
│           └── ConsoleUI.java
├── data/                            ← Auto-created at runtime
├── Makefile
└── README.md
```

---

## Run Instructions

### Requirements
- Java JDK 17+
- `make` (optional)

### With Make
```bash
git clone https://github.com/AneeqAhmad11/Library-Management-System-Java.git
cd Library-Management-System-Java
make run
```

### Without Make
```bash
javac -sourcepath src -d out $(find src -name "*.java")
java -cp out library.LibraryApp
```

### Windows (CMD)
```cmd
xcopy /s /q src\*.java sources.txt
javac -sourcepath src -d out @sources.txt
java -cp out library.LibraryApp
```

---

## Business Rules

| Rule | Value |
|---|---|
| Max books per member | 3 |
| Loan period | 14 days |
| Overdue fine | PKR 10/day |
| Borrowing blocked if | Outstanding fine > 0 |

---

## Author

**Aneeq Ahmad** — FA23-BSE-033A  
BS Software Engineering, COMSATS University Islamabad, Lahore Campus  
GitHub: [@AneeqAhmad11](https://github.com/AneeqAhmad11) · LinkedIn: [aneeqahmad11](https://linkedin.com/in/aneeqahmad11)

package library;

import library.model.Book;
import library.model.Member;
import library.model.Transaction;
import library.service.LibraryService;
import library.util.ConsoleUI;
import library.util.DateUtil;

import java.util.List;

/**
 * ============================================================
 *  LibraryApp.java  —  Main entry point
 *  Library Management System
 *
 *  Author   : Aneeq Ahmad (FA23-BSE-033A)
 *  Course   : Object-Oriented Programming (Java)
 *  Semester : BS Software Engineering
 *  COMSATS University Islamabad, Lahore Campus
 * ============================================================
 *
 *  OOP Concepts Used:
 *    ✔ Encapsulation  — private fields + getters/setters in all model classes
 *    ✔ Inheritance    — Member and Librarian extend abstract Person
 *    ✔ Polymorphism   — overridden getRole() and displayInfo() methods
 *    ✔ Abstraction    — abstract class Person, interfaces Searchable/Manageable
 *    ✔ Interfaces     — LibraryService implements Manageable<Book> & Searchable<Book>
 *    ✔ Collections    — ArrayList for books, members, transactions
 *    ✔ File I/O       — Java Serialization for data persistence
 *    ✔ Exception Handling — try-catch in DataStorage
 */
public class LibraryApp {

    private static final LibraryService library = new LibraryService();

    // ── Entry point ──────────────────────────────────────────
    public static void main(String[] args) {
        seedSampleData(); // load demo data on first run
        showMainMenu();
    }

    // ════════════════════════════════════════════════════════
    //  MAIN MENU
    // ════════════════════════════════════════════════════════
    private static void showMainMenu() {
        while (true) {
            ConsoleUI.printHeader("MAIN MENU");
            System.out.println("     1.  📖  Book Management");
            System.out.println("     2.  👤  Member Management");
            System.out.println("     3.  🔄  Borrow & Return");
            System.out.println("     4.  💰  Fine Management");
            System.out.println("     5.  📋  Transaction History");
            System.out.println("     6.  📊  Reports");
            System.out.println("     0.  ❌  Exit");
            System.out.println();
            int choice = ConsoleUI.readIntInRange("Enter choice", 0, 6);

            switch (choice) {
                case 1 -> bookMenu();
                case 2 -> memberMenu();
                case 3 -> borrowReturnMenu();
                case 4 -> fineMenu();
                case 5 -> transactionMenu();
                case 6 -> reportMenu();
                case 0 -> {
                    ConsoleUI.printHeader("GOODBYE");
                    System.out.println("  Thank you for using the Library Management System.");
                    System.out.println("  — Aneeq Ahmad, BSE COMSATS Lahore\n");
                    System.exit(0);
                }
            }
        }
    }

    // ════════════════════════════════════════════════════════
    //  BOOK MANAGEMENT MENU
    // ════════════════════════════════════════════════════════
    private static void bookMenu() {
        while (true) {
            ConsoleUI.printHeader("BOOK MANAGEMENT");
            System.out.println("     1.  Add New Book");
            System.out.println("     2.  View All Books");
            System.out.println("     3.  Search Books");
            System.out.println("     4.  View Book Details");
            System.out.println("     5.  Update Book");
            System.out.println("     6.  Remove Book");
            System.out.println("     0.  Back to Main Menu");
            System.out.println();
            int choice = ConsoleUI.readIntInRange("Enter choice", 0, 6);
            if (choice == 0) return;

            switch (choice) {
                case 1 -> addBook();
                case 2 -> viewAllBooks();
                case 3 -> searchBooks();
                case 4 -> viewBookDetails();
                case 5 -> updateBook();
                case 6 -> removeBook();
            }
        }
    }

    private static void addBook() {
        ConsoleUI.printHeader("ADD NEW BOOK");

        String isbn    = ConsoleUI.readLine("ISBN (e.g. 978-0-13-468599-1)");
        if (isbn.isEmpty()) { ConsoleUI.error("ISBN cannot be empty."); ConsoleUI.pressEnter(); return; }

        String title   = ConsoleUI.readLine("Title");
        String author  = ConsoleUI.readLine("Author");
        String genre   = ConsoleUI.readLine("Genre (e.g. Fiction, Science, History)");
        String pub     = ConsoleUI.readLine("Publisher");
        int    year    = ConsoleUI.readInt("Publish Year");
        int    copies  = ConsoleUI.readInt("Number of Copies");

        if (copies < 1) { ConsoleUI.error("Copies must be at least 1."); ConsoleUI.pressEnter(); return; }

        Book book = new Book(isbn, title, author, genre, pub, year, copies, DateUtil.today());

        if (library.add(book)) {
            ConsoleUI.success("Book added successfully!");
            System.out.println();
            book.displayInfo();
        } else {
            ConsoleUI.error("A book with ISBN " + isbn + " already exists.");
        }
        ConsoleUI.pressEnter();
    }

    private static void viewAllBooks() {
        ConsoleUI.printHeader("ALL BOOKS");
        library.displayAll();
        ConsoleUI.pressEnter();
    }

    private static void searchBooks() {
        ConsoleUI.printHeader("SEARCH BOOKS");
        System.out.println("  Search by title, author, or genre.");
        String keyword = ConsoleUI.readLine("Keyword");
        List<Book> results = library.searchByName(keyword);

        System.out.println();
        if (results.isEmpty()) {
            ConsoleUI.info("No books found matching \"" + keyword + "\".");
        } else {
            System.out.printf("  %-14s  %-32s  %-20s  %-12s  %s%n",
                    "ISBN", "Title", "Author", "Genre", "Available");
            System.out.println("  " + "─".repeat(97));
            results.forEach(Book::printSummary);
            System.out.println("\n  " + results.size() + " result(s) found.");
        }
        ConsoleUI.pressEnter();
    }

    private static void viewBookDetails() {
        ConsoleUI.printHeader("BOOK DETAILS");
        String isbn = ConsoleUI.readLine("Enter ISBN");
        Book b = library.findById(isbn);
        if (b == null) {
            ConsoleUI.error("No book found with ISBN: " + isbn);
        } else {
            System.out.println();
            b.displayInfo();
        }
        ConsoleUI.pressEnter();
    }

    private static void updateBook() {
        ConsoleUI.printHeader("UPDATE BOOK");
        String isbn = ConsoleUI.readLine("Enter ISBN of book to update");
        Book b = library.findById(isbn);
        if (b == null) { ConsoleUI.error("Book not found."); ConsoleUI.pressEnter(); return; }

        System.out.println("\n  Current details:");
        b.displayInfo();
        System.out.println("\n  Enter new values (press Enter to keep current):\n");

        String title  = ConsoleUI.readLine("Title [" + b.getTitle() + "]");
        String author = ConsoleUI.readLine("Author [" + b.getAuthor() + "]");
        String genre  = ConsoleUI.readLine("Genre [" + b.getGenre() + "]");
        String pub    = ConsoleUI.readLine("Publisher [" + b.getPublisher() + "]");

        if (!title.isEmpty())  b.setTitle(title);
        if (!author.isEmpty()) b.setAuthor(author);
        if (!genre.isEmpty())  b.setGenre(genre);
        if (!pub.isEmpty())    b.setPublisher(pub);

        if (library.update(b)) {
            ConsoleUI.success("Book updated successfully.");
        } else {
            ConsoleUI.error("Update failed.");
        }
        ConsoleUI.pressEnter();
    }

    private static void removeBook() {
        ConsoleUI.printHeader("REMOVE BOOK");
        String isbn = ConsoleUI.readLine("Enter ISBN of book to remove");
        Book b = library.findById(isbn);
        if (b == null) { ConsoleUI.error("Book not found."); ConsoleUI.pressEnter(); return; }

        System.out.println("\n  Book: " + b.getTitle() + " by " + b.getAuthor());
        if (!ConsoleUI.readConfirm("Are you sure you want to remove this book?")) {
            ConsoleUI.info("Removal cancelled.");
            ConsoleUI.pressEnter();
            return;
        }

        if (library.remove(isbn)) {
            ConsoleUI.success("Book removed from catalog.");
        } else {
            ConsoleUI.error("Cannot remove — some copies are currently issued to members.");
        }
        ConsoleUI.pressEnter();
    }

    // ════════════════════════════════════════════════════════
    //  MEMBER MANAGEMENT MENU
    // ════════════════════════════════════════════════════════
    private static void memberMenu() {
        while (true) {
            ConsoleUI.printHeader("MEMBER MANAGEMENT");
            System.out.println("     1.  Register New Member");
            System.out.println("     2.  View All Members");
            System.out.println("     3.  Search Members");
            System.out.println("     4.  View Member Details");
            System.out.println("     5.  Update Member Info");
            System.out.println("     6.  Suspend / Activate Member");
            System.out.println("     7.  Remove Member");
            System.out.println("     0.  Back to Main Menu");
            System.out.println();
            int choice = ConsoleUI.readIntInRange("Enter choice", 0, 7);
            if (choice == 0) return;

            switch (choice) {
                case 1 -> registerMember();
                case 2 -> viewAllMembers();
                case 3 -> searchMembers();
                case 4 -> viewMemberDetails();
                case 5 -> updateMember();
                case 6 -> toggleMemberStatus();
                case 7 -> removeMember();
            }
        }
    }

    private static void registerMember() {
        ConsoleUI.printHeader("REGISTER NEW MEMBER");

        String name    = ConsoleUI.readLine("Full Name");
        String email   = ConsoleUI.readLine("Email");
        String phone   = ConsoleUI.readLine("Phone");
        String address = ConsoleUI.readLine("Address");

        System.out.println("\n  Membership Type:");
        System.out.println("     1. Standard  (borrow up to 3 books, 14-day loan)");
        System.out.println("     2. Premium   (same limits, priority reservations)");
        int typeChoice = ConsoleUI.readIntInRange("Choice", 1, 2);
        String type    = typeChoice == 1 ? "Standard" : "Premium";

        String memberId = library.addMember(name, email, phone, address, type);
        if (memberId == null) {
            ConsoleUI.error("A member with this email already exists.");
        } else {
            ConsoleUI.success("Member registered successfully!");
            System.out.println("\n  Member ID : " + memberId);
            System.out.println("  Name      : " + name);
            System.out.println("  Type      : " + type);
            System.out.println("\n  Please note the Member ID for future transactions.");
        }
        ConsoleUI.pressEnter();
    }

    private static void viewAllMembers() {
        ConsoleUI.printHeader("ALL MEMBERS");
        library.displayAllMembers();
        ConsoleUI.pressEnter();
    }

    private static void searchMembers() {
        ConsoleUI.printHeader("SEARCH MEMBERS");
        String keyword = ConsoleUI.readLine("Search by name or email");
        List<Member> results = library.searchMembersByName(keyword);

        if (results.isEmpty()) {
            ConsoleUI.info("No members found matching \"" + keyword + "\".");
        } else {
            System.out.println();
            for (Member m : results) m.displayInfo();
        }
        ConsoleUI.pressEnter();
    }

    private static void viewMemberDetails() {
        ConsoleUI.printHeader("MEMBER DETAILS");
        String id = ConsoleUI.readLine("Enter Member ID");
        Member m  = library.findMemberById(id);
        if (m == null) {
            ConsoleUI.error("Member not found.");
        } else {
            System.out.println();
            m.displayInfo();
            if (m.getBorrowedCount() > 0) {
                System.out.println("\n  Currently Borrowed ISBNs:");
                m.getBorrowedIsbns().forEach(isbn -> {
                    Book b = library.findById(isbn);
                    System.out.println("    • " + isbn +
                            (b != null ? " — " + b.getTitle() : ""));
                });
            }
        }
        ConsoleUI.pressEnter();
    }

    private static void updateMember() {
        ConsoleUI.printHeader("UPDATE MEMBER");
        String id = ConsoleUI.readLine("Enter Member ID");
        Member m  = library.findMemberById(id);
        if (m == null) { ConsoleUI.error("Member not found."); ConsoleUI.pressEnter(); return; }

        System.out.println();
        m.displayInfo();
        System.out.println("\n  Enter new values (press Enter to keep current):\n");

        String name    = ConsoleUI.readLine("Name [" + m.getName() + "]");
        String email   = ConsoleUI.readLine("Email [" + m.getEmail() + "]");
        String phone   = ConsoleUI.readLine("Phone [" + m.getPhone() + "]");
        String address = ConsoleUI.readLine("Address [" + m.getAddress() + "]");

        library.updateMember(id,
                name.isEmpty()    ? m.getName()    : name,
                email.isEmpty()   ? m.getEmail()   : email,
                phone.isEmpty()   ? m.getPhone()   : phone,
                address.isEmpty() ? m.getAddress() : address);

        ConsoleUI.success("Member information updated.");
        ConsoleUI.pressEnter();
    }

    private static void toggleMemberStatus() {
        ConsoleUI.printHeader("SUSPEND / ACTIVATE MEMBER");
        String id = ConsoleUI.readLine("Enter Member ID");
        Member m  = library.findMemberById(id);
        if (m == null) { ConsoleUI.error("Member not found."); ConsoleUI.pressEnter(); return; }

        System.out.println("\n  Member : " + m.getName());
        System.out.println("  Status : " + (m.isActive() ? "Active" : "Suspended"));
        String action = m.isActive() ? "suspend" : "activate";
        if (ConsoleUI.readConfirm("Do you want to " + action + " this member?")) {
            m.setActive(!m.isActive());
            ConsoleUI.success("Member " + action + "d successfully.");
        } else {
            ConsoleUI.info("No changes made.");
        }
        ConsoleUI.pressEnter();
    }

    private static void removeMember() {
        ConsoleUI.printHeader("REMOVE MEMBER");
        String id = ConsoleUI.readLine("Enter Member ID");
        Member m  = library.findMemberById(id);
        if (m == null) { ConsoleUI.error("Member not found."); ConsoleUI.pressEnter(); return; }

        System.out.println("\n  Member: " + m.getName());
        if (!ConsoleUI.readConfirm("Remove this member?")) {
            ConsoleUI.info("Cancelled."); ConsoleUI.pressEnter(); return;
        }

        if (library.removeMember(id)) {
            ConsoleUI.success("Member removed.");
        } else {
            ConsoleUI.error("Cannot remove — member has books currently issued.");
        }
        ConsoleUI.pressEnter();
    }

    // ════════════════════════════════════════════════════════
    //  BORROW & RETURN MENU
    // ════════════════════════════════════════════════════════
    private static void borrowReturnMenu() {
        while (true) {
            ConsoleUI.printHeader("BORROW & RETURN");
            System.out.println("     1.  Issue Book to Member");
            System.out.println("     2.  Return Book");
            System.out.println("     3.  View Overdue Books");
            System.out.println("     0.  Back to Main Menu");
            System.out.println();
            int choice = ConsoleUI.readIntInRange("Enter choice", 0, 3);
            if (choice == 0) return;

            switch (choice) {
                case 1 -> issueBook();
                case 2 -> returnBook();
                case 3 -> viewOverdue();
            }
        }
    }

    private static void issueBook() {
        ConsoleUI.printHeader("ISSUE BOOK");

        String memberId = ConsoleUI.readLine("Member ID");
        Member m = library.findMemberById(memberId);
        if (m == null) { ConsoleUI.error("Member not found."); ConsoleUI.pressEnter(); return; }

        System.out.println("  Member : " + m.getName() +
                " (" + m.getBorrowedCount() + "/" +
                Member.MAX_BOOKS_ALLOWED + " books)");

        String isbn = ConsoleUI.readLine("Book ISBN");
        Book b = library.findById(isbn);
        if (b != null) System.out.println("  Book   : " + b.getTitle());

        int result = library.issueBook(memberId, isbn);
        switch (result) {
            case 0 -> {
                ConsoleUI.success("Book issued successfully!");
                System.out.println("\n  Member   : " + m.getName());
                System.out.println("  Book     : " + (b != null ? b.getTitle() : isbn));
                System.out.println("  Due Date : " +
                        DateUtil.dueDate(Member.LOAN_PERIOD_DAYS));
            }
            case 1 -> ConsoleUI.error("Member not found or account is suspended.");
            case 2 -> ConsoleUI.error("Book not found in catalog.");
            case 3 -> ConsoleUI.error("No copies available — all issued.");
            case 4 -> ConsoleUI.error("Member has reached the borrow limit (" +
                    Member.MAX_BOOKS_ALLOWED + " books).");
            case 5 -> ConsoleUI.error("Member has outstanding fines. Please pay before borrowing.");
        }
        ConsoleUI.pressEnter();
    }

    private static void returnBook() {
        ConsoleUI.printHeader("RETURN BOOK");

        String memberId = ConsoleUI.readLine("Member ID");
        Member m = library.findMemberById(memberId);
        if (m == null) { ConsoleUI.error("Member not found."); ConsoleUI.pressEnter(); return; }

        System.out.println("  Member : " + m.getName());

        if (m.getBorrowedCount() == 0) {
            ConsoleUI.info("This member has no books to return.");
            ConsoleUI.pressEnter();
            return;
        }

        System.out.println("\n  Currently borrowed:");
        m.getBorrowedIsbns().forEach(isbn -> {
            Book bk = library.findById(isbn);
            System.out.println("    • " + isbn + (bk != null ? " — " + bk.getTitle() : ""));
        });

        String isbn = ConsoleUI.readLine("\n  ISBN to return");
        double fine = library.returnBook(memberId, isbn);

        if (fine == -1) {
            ConsoleUI.error("No active borrow record found for this ISBN.");
        } else if (fine == 0) {
            ConsoleUI.success("Book returned on time. No fine.");
        } else {
            ConsoleUI.success("Book returned.");
            System.out.printf("%n  ⚠  Overdue fine charged: PKR %.2f%n", fine);
            System.out.println("  Please pay the fine before borrowing again.");
        }
        ConsoleUI.pressEnter();
    }

    private static void viewOverdue() {
        ConsoleUI.printHeader("OVERDUE BOOKS");
        List<Transaction> overdue = library.getOverdueTransactions();
        if (overdue.isEmpty()) {
            ConsoleUI.info("No overdue books at this time.");
        } else {
            System.out.printf("  %-10s  %-10s  %-20s  %-14s  %-10s  %s%n",
                    "Txn ID", "Member", "Member Name", "ISBN", "Due Date", "Days Late");
            System.out.println("  " + "─".repeat(80));
            for (Transaction t : overdue) {
                long days = DateUtil.daysOverdue(t.getDueDate());
                System.out.printf("  %-10s  %-10s  %-20s  %-14s  %-10s  %d%n",
                        t.getTransactionId(), t.getMemberId(),
                        t.getMemberName(), t.getBookIsbn(),
                        t.getDueDate(), days);
            }
            System.out.println("\n  Total overdue: " + overdue.size());
        }
        ConsoleUI.pressEnter();
    }

    // ════════════════════════════════════════════════════════
    //  FINE MANAGEMENT MENU
    // ════════════════════════════════════════════════════════
    private static void fineMenu() {
        while (true) {
            ConsoleUI.printHeader("FINE MANAGEMENT");
            System.out.println("     1.  Check Member's Fine");
            System.out.println("     2.  Pay Fine");
            System.out.println("     0.  Back to Main Menu");
            System.out.println();
            int choice = ConsoleUI.readIntInRange("Enter choice", 0, 2);
            if (choice == 0) return;

            switch (choice) {
                case 1 -> checkFine();
                case 2 -> payFine();
            }
        }
    }

    private static void checkFine() {
        ConsoleUI.printHeader("CHECK FINE");
        String id = ConsoleUI.readLine("Enter Member ID");
        Member m  = library.findMemberById(id);
        if (m == null) { ConsoleUI.error("Member not found."); ConsoleUI.pressEnter(); return; }

        System.out.println("\n  Member : " + m.getName());
        System.out.printf("  Outstanding Fine : PKR %.2f%n", m.getOutstandingFine());
        if (m.getOutstandingFine() == 0) {
            ConsoleUI.info("No outstanding fines.");
        }
        ConsoleUI.pressEnter();
    }

    private static void payFine() {
        ConsoleUI.printHeader("PAY FINE");
        String id = ConsoleUI.readLine("Enter Member ID");
        Member m  = library.findMemberById(id);
        if (m == null) { ConsoleUI.error("Member not found."); ConsoleUI.pressEnter(); return; }

        System.out.println("\n  Member           : " + m.getName());
        System.out.printf("  Outstanding Fine : PKR %.2f%n", m.getOutstandingFine());

        if (m.getOutstandingFine() == 0) {
            ConsoleUI.info("No fine to pay.");
            ConsoleUI.pressEnter();
            return;
        }

        double amount = ConsoleUI.readDouble("Amount to pay (PKR)");
        double paid   = library.payFine(id, amount);
        if (paid < 0) {
            ConsoleUI.error("Error processing payment.");
        } else {
            ConsoleUI.success(String.format("PKR %.2f received.", paid));
            System.out.printf("  Remaining Fine : PKR %.2f%n",
                    m.getOutstandingFine());
        }
        ConsoleUI.pressEnter();
    }

    // ════════════════════════════════════════════════════════
    //  TRANSACTION HISTORY MENU
    // ════════════════════════════════════════════════════════
    private static void transactionMenu() {
        while (true) {
            ConsoleUI.printHeader("TRANSACTION HISTORY");
            System.out.println("     1.  View All Transactions");
            System.out.println("     2.  View Transactions by Member");
            System.out.println("     3.  View Transaction Details");
            System.out.println("     0.  Back to Main Menu");
            System.out.println();
            int choice = ConsoleUI.readIntInRange("Enter choice", 0, 3);
            if (choice == 0) return;

            switch (choice) {
                case 1 -> {
                    ConsoleUI.printHeader("ALL TRANSACTIONS");
                    library.displayAllTransactions();
                    ConsoleUI.pressEnter();
                }
                case 2 -> {
                    ConsoleUI.printHeader("MEMBER TRANSACTIONS");
                    String id = ConsoleUI.readLine("Enter Member ID");
                    System.out.println();
                    library.displayMemberTransactions(id);
                    ConsoleUI.pressEnter();
                }
                case 3 -> {
                    ConsoleUI.printHeader("TRANSACTION DETAILS");
                    String txnId = ConsoleUI.readLine("Enter Transaction ID");
                    Transaction found = library.getAllTransactions().stream()
                            .filter(t -> t.getTransactionId().equalsIgnoreCase(txnId))
                            .findFirst().orElse(null);
                    if (found == null) {
                        ConsoleUI.error("Transaction not found.");
                    } else {
                        System.out.println();
                        found.displayInfo();
                    }
                    ConsoleUI.pressEnter();
                }
            }
        }
    }

    // ════════════════════════════════════════════════════════
    //  REPORTS MENU
    // ════════════════════════════════════════════════════════
    private static void reportMenu() {
        while (true) {
            ConsoleUI.printHeader("REPORTS");
            System.out.println("     1.  Library Summary");
            System.out.println("     2.  Top 5 Most Borrowed Books");
            System.out.println("     3.  Overdue Report");
            System.out.println("     4.  Members with Outstanding Fines");
            System.out.println("     0.  Back to Main Menu");
            System.out.println();
            int choice = ConsoleUI.readIntInRange("Enter choice", 0, 4);
            if (choice == 0) return;

            switch (choice) {
                case 1 -> {
                    ConsoleUI.printHeader("LIBRARY SUMMARY");
                    library.showSummaryReport();
                    ConsoleUI.pressEnter();
                }
                case 2 -> {
                    ConsoleUI.printHeader("TOP 5 MOST BORROWED BOOKS");
                    System.out.printf("  %-14s  %-35s  %s%n", "ISBN", "Title", "Borrows");
                    System.out.println("  " + "─".repeat(60));
                    library.showTopBorrowedBooks(5);
                    ConsoleUI.pressEnter();
                }
                case 3 -> {
                    ConsoleUI.printHeader("OVERDUE REPORT");
                    viewOverdue();
                }
                case 4 -> {
                    ConsoleUI.printHeader("MEMBERS WITH OUTSTANDING FINES");
                    List<Member> withFines = library.getAllMembers().stream()
                            .filter(m -> m.getOutstandingFine() > 0)
                            .toList();
                    if (withFines.isEmpty()) {
                        ConsoleUI.info("No members have outstanding fines.");
                    } else {
                        System.out.printf("  %-10s  %-25s  %s%n",
                                "Member ID", "Name", "Fine (PKR)");
                        System.out.println("  " + "─".repeat(50));
                        for (Member m : withFines) {
                            System.out.printf("  %-10s  %-25s  %.2f%n",
                                    m.getId(), m.getName(), m.getOutstandingFine());
                        }
                    }
                    ConsoleUI.pressEnter();
                }
            }
        }
    }

    // ════════════════════════════════════════════════════════
    //  SEED SAMPLE DATA (first run only)
    // ════════════════════════════════════════════════════════
    private static void seedSampleData() {
        // Only seed if the catalog is empty
        if (!library.getAllBooks().isEmpty()) return;

        String today = DateUtil.today();

        // Books
        library.add(new Book("978-0-06-112008-4", "To Kill a Mockingbird",
                "Harper Lee", "Fiction", "J. B. Lippincott", 1960, 3, today));
        library.add(new Book("978-0-7432-7356-5", "1984",
                "George Orwell", "Dystopian Fiction", "Secker & Warburg", 1949, 2, today));
        library.add(new Book("978-0-14-028329-7", "The Great Gatsby",
                "F. Scott Fitzgerald", "Classic Fiction", "Scribner", 1925, 4, today));
        library.add(new Book("978-0-14-303943-3", "Clean Code",
                "Robert C. Martin", "Programming", "Prentice Hall", 2008, 2, today));
        library.add(new Book("978-0-13-468599-1", "The Pragmatic Programmer",
                "Andrew Hunt", "Programming", "Addison-Wesley", 1999, 3, today));
        library.add(new Book("978-0-13-110362-7", "The C Programming Language",
                "Kernighan & Ritchie", "Programming", "Prentice Hall", 1978, 5, today));
        library.add(new Book("978-0-59-651798-1", "Head First Java",
                "Kathy Sierra", "Programming", "O'Reilly Media", 2005, 4, today));
        library.add(new Book("978-0-20-163361-0", "Design Patterns",
                "Gang of Four", "Software Engineering", "Addison-Wesley", 1994, 2, today));
        library.add(new Book("978-0-13-235088-4", "Introduction to Algorithms",
                "Cormen et al.", "Computer Science", "MIT Press", 2009, 3, today));
        library.add(new Book("978-9-69-243018-4", "Pakistan — A Hard Country",
                "Anatol Lieven", "History", "PublicAffairs", 2011, 2, today));

        // Members
        library.addMember("Ali Hassan",     "ali@example.com",    "0300-1234567", "Lahore",    "Standard");
        library.addMember("Sana Tariq",     "sana@example.com",   "0311-2345678", "Lahore",    "Premium");
        library.addMember("Usman Ghani",    "usman@example.com",  "0333-3456789", "Islamabad", "Standard");
        library.addMember("Ayesha Malik",   "ayesha@example.com", "0345-4567890", "Karachi",   "Premium");
        library.addMember("Bilal Ahmed",    "bilal@example.com",  "0321-5678901", "Lahore",    "Standard");
    }
}

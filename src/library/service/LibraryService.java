package library.service;

import library.interfaces.Manageable;
import library.interfaces.Searchable;
import library.model.Book;
import library.model.Member;
import library.model.Transaction;
import library.util.DataStorage;
import library.util.DateUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Core service class — manages books, members, and transactions.
 *
 * OOP concepts demonstrated:
 *  - Interface implementation : implements Manageable and Searchable
 *  - Composition              : contains Book, Member, Transaction lists
 *  - Polymorphism             : Manageable<Book> and Searchable<Book>
 */
public class LibraryService
        implements Manageable<Book>, Searchable<Book> {

    // ── In-memory state (loaded from disk on startup) ────────
    private List<Book>        books;
    private List<Member>      members;
    private List<Transaction> transactions;
    private int               nextMemberNum;
    private int               nextTransNum;

    // ── Constructor ──────────────────────────────────────────
    public LibraryService() {
        books        = DataStorage.loadBooks();
        members      = DataStorage.loadMembers();
        transactions = DataStorage.loadTransactions();
        nextMemberNum = members.size() + 1;
        nextTransNum  = transactions.size() + 1;
    }

    // ══════════════════════════════════════════════════════════
    //  BOOK OPERATIONS  (implements Manageable<Book>)
    // ══════════════════════════════════════════════════════════

    @Override
    public boolean add(Book book) {
        if (findBookByIsbn(book.getIsbn()) != null) return false;
        books.add(book);
        return DataStorage.saveBooks(books);
    }

    @Override
    public boolean remove(String isbn) {
        Book b = findBookByIsbn(isbn);
        if (b == null) return false;
        if (b.getAvailableCopies() < b.getTotalCopies()) return false; // copies out
        books.remove(b);
        return DataStorage.saveBooks(books);
    }

    @Override
    public boolean update(Book updated) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getIsbn().equals(updated.getIsbn())) {
                books.set(i, updated);
                return DataStorage.saveBooks(books);
            }
        }
        return false;
    }

    @Override
    public void displayAll() {
        System.out.printf("  %-14s  %-32s  %-20s  %-12s  %s%n",
                "ISBN", "Title", "Author", "Genre", "Available");
        System.out.println("  " + "─".repeat(97));
        if (books.isEmpty()) {
            System.out.println("  (No books in catalog)");
            return;
        }
        books.forEach(Book::printSummary);
        System.out.println("\n  Total: " + books.size() + " book(s)");
    }

    // ══════════════════════════════════════════════════════════
    //  BOOK SEARCH  (implements Searchable<Book>)
    // ══════════════════════════════════════════════════════════

    @Override
    public List<Book> searchByName(String keyword) {
        String kw = keyword.toLowerCase();
        return books.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(kw)
                          || b.getAuthor().toLowerCase().contains(kw)
                          || b.getGenre().toLowerCase().contains(kw))
                .collect(Collectors.toList());
    }

    @Override
    public Book findById(String isbn) {
        return findBookByIsbn(isbn);
    }

    public Book findBookByIsbn(String isbn) {
        return books.stream()
                .filter(b -> b.getIsbn().equalsIgnoreCase(isbn))
                .findFirst().orElse(null);
    }

    // ══════════════════════════════════════════════════════════
    //  MEMBER OPERATIONS
    // ══════════════════════════════════════════════════════════

    public String addMember(String name, String email,
                            String phone, String address,
                            String membershipType) {
        // Check duplicate email
        boolean emailExists = members.stream()
                .anyMatch(m -> m.getEmail().equalsIgnoreCase(email));
        if (emailExists) return null;

        String memberId = DateUtil.generateMemberId(nextMemberNum++);
        Member m = new Member(memberId, name, email, phone, address,
                              membershipType, DateUtil.today());
        members.add(m);
        DataStorage.saveMembers(members);
        return memberId;
    }

    public boolean removeMember(String memberId) {
        Member m = findMemberById(memberId);
        if (m == null || m.getBorrowedCount() > 0) return false;
        members.remove(m);
        return DataStorage.saveMembers(members);
    }

    public boolean updateMember(String memberId, String name,
                                String email, String phone, String address) {
        Member m = findMemberById(memberId);
        if (m == null) return false;
        m.setName(name);
        m.setEmail(email);
        m.setPhone(phone);
        m.setAddress(address);
        return DataStorage.saveMembers(members);
    }

    public void displayAllMembers() {
        System.out.printf("  %-10s  %-25s  %-25s  %-10s  %-8s  %s%n",
                "ID", "Name", "Email", "Type", "Books", "Status");
        System.out.println("  " + "─".repeat(90));
        if (members.isEmpty()) {
            System.out.println("  (No registered members)");
            return;
        }
        for (Member m : members) {
            System.out.printf("  %-10s  %-25s  %-25s  %-10s  %d/%-6d  %s%n",
                    m.getId(), m.getName(), m.getEmail(),
                    m.getMembershipType(), m.getBorrowedCount(),
                    Member.MAX_BOOKS_ALLOWED,
                    m.isActive() ? "Active" : "Suspended");
        }
        System.out.println("\n  Total: " + members.size() + " member(s)");
    }

    public List<Member> searchMembersByName(String keyword) {
        String kw = keyword.toLowerCase();
        return members.stream()
                .filter(m -> m.getName().toLowerCase().contains(kw)
                          || m.getEmail().toLowerCase().contains(kw))
                .collect(Collectors.toList());
    }

    public Member findMemberById(String memberId) {
        return members.stream()
                .filter(m -> m.getId().equalsIgnoreCase(memberId))
                .findFirst().orElse(null);
    }

    // ══════════════════════════════════════════════════════════
    //  BORROW / RETURN
    // ══════════════════════════════════════════════════════════

    /**
     * Issue a book to a member.
     * Returns a result code:
     *   0 = success
     *   1 = member not found / inactive
     *   2 = book not found
     *   3 = book not available
     *   4 = member borrow limit reached
     *   5 = member has outstanding fines
     */
    public int issueBook(String memberId, String isbn) {
        Member m = findMemberById(memberId);
        if (m == null || !m.isActive()) return 1;

        Book b = findBookByIsbn(isbn);
        if (b == null) return 2;
        if (!b.isAvailable()) return 3;
        if (!m.canBorrowMore()) {
            return m.getOutstandingFine() > 0 ? 5 : 4;
        }

        b.checkOut();
        m.borrowBook(isbn);

        String dueDate = DateUtil.dueDate(Member.LOAN_PERIOD_DAYS);
        String txnId   = DateUtil.generateTransactionId(nextTransNum++);
        Transaction txn = new Transaction(txnId, memberId, m.getName(),
                isbn, b.getTitle(), DateUtil.today(), dueDate);
        transactions.add(txn);

        DataStorage.saveBooks(books);
        DataStorage.saveMembers(members);
        DataStorage.saveTransactions(transactions);

        return 0;
    }

    /**
     * Return a book.
     * Returns calculated fine (0 if on time), or -1 if transaction not found.
     */
    public double returnBook(String memberId, String isbn) {
        // Find the open transaction for this member+book
        Transaction txn = transactions.stream()
                .filter(t -> t.getMemberId().equals(memberId)
                          && t.getBookIsbn().equals(isbn)
                          && !t.isReturned())
                .findFirst().orElse(null);

        if (txn == null) return -1;

        long daysLate = DateUtil.daysOverdue(txn.getDueDate());
        double fine = daysLate > 0 ? daysLate * Member.FINE_PER_DAY : 0;

        txn.completeReturn(DateUtil.today(), fine);

        Book b = findBookByIsbn(isbn);
        if (b != null) b.checkIn();

        Member m = findMemberById(memberId);
        if (m != null) {
            m.returnBook(isbn);
            if (fine > 0) m.addFine(fine);
        }

        DataStorage.saveBooks(books);
        DataStorage.saveMembers(members);
        DataStorage.saveTransactions(transactions);

        return fine;
    }

    // ══════════════════════════════════════════════════════════
    //  FINES
    // ══════════════════════════════════════════════════════════

    /**
     * Pay some or all of a member's outstanding fine.
     * Returns the actual amount paid (capped at outstanding balance).
     */
    public double payFine(String memberId, double amount) {
        Member m = findMemberById(memberId);
        if (m == null) return -1;
        double pay = Math.min(amount, m.getOutstandingFine());
        m.payFine(pay);
        DataStorage.saveMembers(members);
        return pay;
    }

    // ══════════════════════════════════════════════════════════
    //  TRANSACTION QUERIES
    // ══════════════════════════════════════════════════════════

    public void displayAllTransactions() {
        System.out.printf("  %-10s  %-10s  %-14s  %-10s  %-10s  %-10s  %s%n",
                "Txn ID", "Member", "Book", "Issued", "Due", "Returned", "Status");
        System.out.println("  " + "─".repeat(80));
        if (transactions.isEmpty()) {
            System.out.println("  (No transactions yet)");
            return;
        }
        transactions.forEach(Transaction::printSummary);
        System.out.println("\n  Total: " + transactions.size() + " transaction(s)");
    }

    public void displayMemberTransactions(String memberId) {
        List<Transaction> result = transactions.stream()
                .filter(t -> t.getMemberId().equals(memberId))
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            System.out.println("  No transactions found for member " + memberId);
            return;
        }
        System.out.printf("  %-10s  %-14s  %-10s  %-10s  %-10s  %s%n",
                "Txn ID", "Book", "Issued", "Due", "Returned", "Status");
        System.out.println("  " + "─".repeat(72));
        result.forEach(Transaction::printSummary);
    }

    public List<Transaction> getOverdueTransactions() {
        return transactions.stream()
                .filter(t -> !t.isReturned()
                          && DateUtil.daysOverdue(t.getDueDate()) > 0)
                .collect(Collectors.toList());
    }

    // ══════════════════════════════════════════════════════════
    //  REPORTS
    // ══════════════════════════════════════════════════════════

    public void showSummaryReport() {
        System.out.printf("  Total Books in Catalog : %d%n", books.size());
        long available = books.stream().filter(Book::isAvailable).count();
        System.out.printf("  Books with Copies Out  : %d%n",
                books.stream().filter(b -> b.getAvailableCopies() < b.getTotalCopies()).count());
        System.out.printf("  Books Available        : %d%n", available);
        System.out.printf("  Total Members          : %d%n", members.size());
        long activeMembers = members.stream().filter(Member::isActive).count();
        System.out.printf("  Active Members         : %d%n", activeMembers);
        System.out.printf("  Total Transactions     : %d%n", transactions.size());
        long open = transactions.stream().filter(t -> !t.isReturned()).count();
        System.out.printf("  Currently Issued       : %d%n", open);
        System.out.printf("  Overdue                : %d%n", getOverdueTransactions().size());
        double totalFines = members.stream()
                .mapToDouble(Member::getOutstandingFine).sum();
        System.out.printf("  Pending Fines          : PKR %.2f%n", totalFines);
    }

    public void showTopBorrowedBooks(int n) {
        books.stream()
                .filter(b -> b.getTimesBorrowed() > 0)
                .sorted(Comparator.comparingInt(Book::getTimesBorrowed).reversed())
                .limit(n)
                .forEach(b -> System.out.printf("  %-14s  %-35s  %d borrow(s)%n",
                        b.getIsbn(), b.getTitle(), b.getTimesBorrowed()));
    }

    // ── Getters ──────────────────────────────────────────────
    public List<Book>        getAllBooks()        { return new ArrayList<>(books); }
    public List<Member>      getAllMembers()      { return new ArrayList<>(members); }
    public List<Transaction> getAllTransactions() { return new ArrayList<>(transactions); }
}

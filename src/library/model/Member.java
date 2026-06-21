package library.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a library member who can borrow books.
 *
 * OOP concepts demonstrated:
 *  - Inheritance   : extends Person
 *  - Polymorphism  : overrides getRole() and displayInfo()
 *  - Encapsulation : private fields, controlled via methods
 */
public class Member extends Person {

    private static final long serialVersionUID = 1L;

    // ── Business rules (constants) ───────────────────────────
    public static final int    MAX_BOOKS_ALLOWED = 3;
    public static final int    LOAN_PERIOD_DAYS  = 14;
    public static final double FINE_PER_DAY      = 10.0; // PKR per day

    // ── Member-specific fields ───────────────────────────────
    private String       membershipType;   // "Standard" or "Premium"
    private String       joinDate;
    private boolean      isActive;
    private double       outstandingFine;
    private List<String> borrowedIsbns;   // ISBNs of currently borrowed books

    // ── Constructor ──────────────────────────────────────────
    public Member(String id, String name, String email,
                  String phone, String address,
                  String membershipType, String joinDate) {
        super(id, name, email, phone, address);
        this.membershipType = membershipType;
        this.joinDate       = joinDate;
        this.isActive       = true;
        this.outstandingFine = 0.0;
        this.borrowedIsbns  = new ArrayList<>();
    }

    // ── Polymorphic overrides ────────────────────────────────
    @Override
    public String getRole() { return "Member"; }

    @Override
    public void displayInfo() {
        System.out.println("  ┌─────────────────────────────────────────────┐");
        System.out.printf ("  │  Member ID      : %-26s│%n", getId());
        System.out.printf ("  │  Name           : %-26s│%n", getName());
        System.out.printf ("  │  Email          : %-26s│%n", getEmail());
        System.out.printf ("  │  Phone          : %-26s│%n", getPhone());
        System.out.printf ("  │  Membership     : %-26s│%n", membershipType);
        System.out.printf ("  │  Joined         : %-26s│%n", joinDate);
        System.out.printf ("  │  Books Borrowed : %-26s│%n",
                borrowedIsbns.size() + " / " + MAX_BOOKS_ALLOWED);
        System.out.printf ("  │  Outstanding    : PKR %-22.2f│%n", outstandingFine);
        System.out.printf ("  │  Status         : %-26s│%n",
                isActive ? "Active" : "Suspended");
        System.out.println("  └─────────────────────────────────────────────┘");
    }

    // ── Domain logic ─────────────────────────────────────────
    public boolean canBorrowMore() {
        return isActive && borrowedIsbns.size() < MAX_BOOKS_ALLOWED && outstandingFine == 0.0;
    }

    public boolean borrowBook(String isbn) {
        if (!canBorrowMore()) return false;
        borrowedIsbns.add(isbn);
        return true;
    }

    public boolean returnBook(String isbn) {
        return borrowedIsbns.remove(isbn);
    }

    public void addFine(double amount) {
        this.outstandingFine += amount;
    }

    public void payFine(double amount) {
        this.outstandingFine = Math.max(0, this.outstandingFine - amount);
    }

    // ── Getters & Setters ────────────────────────────────────
    public String       getMembershipType()  { return membershipType; }
    public String       getJoinDate()        { return joinDate; }
    public boolean      isActive()           { return isActive; }
    public double       getOutstandingFine() { return outstandingFine; }
    public List<String> getBorrowedIsbns()   { return new ArrayList<>(borrowedIsbns); }
    public int          getBorrowedCount()   { return borrowedIsbns.size(); }

    public void setMembershipType(String type) { this.membershipType = type; }
    public void setActive(boolean active)       { this.isActive = active; }
    public void setOutstandingFine(double fine) { this.outstandingFine = fine; }
}

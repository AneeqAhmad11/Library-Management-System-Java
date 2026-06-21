package library.model;

/**
 * Represents a librarian (admin) who manages the library.
 *
 * OOP concepts demonstrated:
 *  - Inheritance  : extends Person
 *  - Polymorphism : overrides getRole() and displayInfo()
 */
public class Librarian extends Person {

    private static final long serialVersionUID = 1L;

    private String employeeId;
    private String department;
    private String joinDate;

    // ── Constructor ──────────────────────────────────────────
    public Librarian(String id, String name, String email,
                     String phone, String address,
                     String employeeId, String department, String joinDate) {
        super(id, name, email, phone, address);
        this.employeeId = employeeId;
        this.department = department;
        this.joinDate   = joinDate;
    }

    // ── Polymorphic overrides ────────────────────────────────
    @Override
    public String getRole() { return "Librarian"; }

    @Override
    public void displayInfo() {
        System.out.println("  ┌─────────────────────────────────────────────┐");
        System.out.printf ("  │  Librarian ID   : %-26s│%n", getId());
        System.out.printf ("  │  Name           : %-26s│%n", getName());
        System.out.printf ("  │  Employee ID    : %-26s│%n", employeeId);
        System.out.printf ("  │  Department     : %-26s│%n", department);
        System.out.printf ("  │  Email          : %-26s│%n", getEmail());
        System.out.printf ("  │  Phone          : %-26s│%n", getPhone());
        System.out.printf ("  │  Joined         : %-26s│%n", joinDate);
        System.out.println("  └─────────────────────────────────────────────┘");
    }

    // ── Getters ──────────────────────────────────────────────
    public String getEmployeeId() { return employeeId; }
    public String getDepartment() { return department; }
    public String getJoinDate()   { return joinDate; }
}

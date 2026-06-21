package library.model;

import java.io.Serializable;

/**
 * Abstract base class for all people in the library system.
 *
 * OOP concepts demonstrated:
 *  - Abstraction   : abstract class with abstract methods
 *  - Encapsulation : private fields, public getters/setters
 *  - Inheritance   : Member and Librarian extend this class
 */
public abstract class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    // ── Encapsulated fields ──────────────────────────────────
    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;

    // ── Constructor ──────────────────────────────────────────
    public Person(String id, String name, String email,
                  String phone, String address) {
        this.id      = id;
        this.name    = name;
        this.email   = email;
        this.phone   = phone;
        this.address = address;
    }

    // ── Abstract methods (subclasses MUST implement) ─────────
    /** Returns the role label, e.g. "Member" or "Librarian". */
    public abstract String getRole();

    /** Prints a formatted summary of this person to the console. */
    public abstract void displayInfo();

    // ── Getters ──────────────────────────────────────────────
    public String getId()      { return id; }
    public String getName()    { return name; }
    public String getEmail()   { return email; }
    public String getPhone()   { return phone; }
    public String getAddress() { return address; }

    // ── Setters ──────────────────────────────────────────────
    public void setName(String name)       { this.name    = name; }
    public void setEmail(String email)     { this.email   = email; }
    public void setPhone(String phone)     { this.phone   = phone; }
    public void setAddress(String address) { this.address = address; }

    // ── toString (Polymorphism) ──────────────────────────────
    @Override
    public String toString() {
        return String.format("[%s] %s (ID: %s)", getRole(), name, id);
    }
}

package library.model;

import java.io.Serializable;

/**
 * Represents a book in the library catalog.
 *
 * OOP concepts demonstrated:
 *  - Encapsulation : all fields private, accessed via getters/setters
 *  - Serializable  : so Book objects can be saved to disk
 */
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    // ── Fields ───────────────────────────────────────────────
    private String isbn;
    private String title;
    private String author;
    private String genre;
    private String publisher;
    private int    publishYear;
    private int    totalCopies;     // physical copies in library
    private int    availableCopies; // copies currently on shelf
    private int    timesBorrowed;   // lifetime borrow count (for reports)
    private String addedDate;

    // ── Constructor ──────────────────────────────────────────
    public Book(String isbn, String title, String author,
                String genre, String publisher,
                int publishYear, int totalCopies, String addedDate) {
        this.isbn            = isbn;
        this.title           = title;
        this.author          = author;
        this.genre           = genre;
        this.publisher       = publisher;
        this.publishYear     = publishYear;
        this.totalCopies     = totalCopies;
        this.availableCopies = totalCopies;
        this.timesBorrowed   = 0;
        this.addedDate       = addedDate;
    }

    // ── Domain logic ─────────────────────────────────────────
    public boolean isAvailable() {
        return availableCopies > 0;
    }

    /** Called when a member borrows a copy. Returns false if none left. */
    public boolean checkOut() {
        if (availableCopies <= 0) return false;
        availableCopies--;
        timesBorrowed++;
        return true;
    }

    /** Called when a member returns a copy. */
    public void checkIn() {
        if (availableCopies < totalCopies)
            availableCopies++;
    }

    /** One-line summary for table listings. */
    public void printSummary() {
        System.out.printf("  %-14s  %-32s  %-20s  %-12s  %d/%d%n",
                isbn, truncate(title, 32), truncate(author, 20),
                genre, availableCopies, totalCopies);
    }

    /** Full detail card. */
    public void displayInfo() {
        System.out.println("  ┌─────────────────────────────────────────────┐");
        System.out.printf ("  │  ISBN           : %-26s│%n", isbn);
        System.out.printf ("  │  Title          : %-26s│%n", truncate(title, 26));
        System.out.printf ("  │  Author         : %-26s│%n", author);
        System.out.printf ("  │  Genre          : %-26s│%n", genre);
        System.out.printf ("  │  Publisher      : %-26s│%n", publisher);
        System.out.printf ("  │  Year           : %-26d│%n", publishYear);
        System.out.printf ("  │  Copies         : %d total, %d available%14s│%n",
                totalCopies, availableCopies, "");
        System.out.printf ("  │  Times Borrowed : %-26d│%n", timesBorrowed);
        System.out.printf ("  │  Added          : %-26s│%n", addedDate);
        System.out.printf ("  │  Status         : %-26s│%n",
                availableCopies > 0 ? "Available" : "All Copies Issued");
        System.out.println("  └─────────────────────────────────────────────┘");
    }

    private String truncate(String s, int max) {
        return s.length() > max ? s.substring(0, max - 3) + "..." : s;
    }

    // ── Getters ──────────────────────────────────────────────
    public String getIsbn()             { return isbn; }
    public String getTitle()            { return title; }
    public String getAuthor()           { return author; }
    public String getGenre()            { return genre; }
    public String getPublisher()        { return publisher; }
    public int    getPublishYear()      { return publishYear; }
    public int    getTotalCopies()      { return totalCopies; }
    public int    getAvailableCopies()  { return availableCopies; }
    public int    getTimesBorrowed()    { return timesBorrowed; }
    public String getAddedDate()        { return addedDate; }

    // ── Setters ──────────────────────────────────────────────
    public void setTitle(String title)          { this.title       = title; }
    public void setAuthor(String author)        { this.author      = author; }
    public void setGenre(String genre)          { this.genre       = genre; }
    public void setPublisher(String pub)        { this.publisher   = pub; }
    public void setPublishYear(int year)        { this.publishYear = year; }
    public void setTotalCopies(int total) {
        int diff = total - this.totalCopies;
        this.totalCopies     = total;
        this.availableCopies = Math.max(0, this.availableCopies + diff);
    }

    @Override
    public String toString() {
        return String.format("Book[%s] \"%s\" by %s", isbn, title, author);
    }
}

package library.model;

import java.io.Serializable;

/**
 * Records a single borrow or return event.
 * Immutable after creation except for returnDate/fine (set on return).
 */
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Status { ISSUED, RETURNED, OVERDUE }

    // ── Fields ───────────────────────────────────────────────
    private final String transactionId;
    private final String memberId;
    private final String memberName;
    private final String bookIsbn;
    private final String bookTitle;
    private final String issueDate;
    private final String dueDate;
    private String       returnDate;   // null until returned
    private double       fine;
    private Status       status;

    // ── Constructor ──────────────────────────────────────────
    public Transaction(String transactionId,
                       String memberId, String memberName,
                       String bookIsbn, String bookTitle,
                       String issueDate, String dueDate) {
        this.transactionId = transactionId;
        this.memberId      = memberId;
        this.memberName    = memberName;
        this.bookIsbn      = bookIsbn;
        this.bookTitle     = bookTitle;
        this.issueDate     = issueDate;
        this.dueDate       = dueDate;
        this.returnDate    = null;
        this.fine          = 0.0;
        this.status        = Status.ISSUED;
    }

    // ── Complete the return ──────────────────────────────────
    public void completeReturn(String returnDate, double fine) {
        this.returnDate = returnDate;
        this.fine       = fine;
        this.status     = Status.RETURNED;
    }

    public void markOverdue() {
        this.status = Status.OVERDUE;
    }

    // ── Display ──────────────────────────────────────────────
    public void printSummary() {
        System.out.printf("  %-10s  %-10s  %-14s  %-10s  %-10s  %-10s  %s%n",
                transactionId, memberId, truncate(bookTitle, 14),
                issueDate, dueDate,
                returnDate != null ? returnDate : "---",
                status);
    }

    public void displayInfo() {
        System.out.println("  ┌─────────────────────────────────────────────┐");
        System.out.printf ("  │  Transaction ID : %-26s│%n", transactionId);
        System.out.printf ("  │  Member ID      : %-26s│%n", memberId);
        System.out.printf ("  │  Member Name    : %-26s│%n", memberName);
        System.out.printf ("  │  Book ISBN      : %-26s│%n", bookIsbn);
        System.out.printf ("  │  Book Title     : %-26s│%n", truncate(bookTitle, 26));
        System.out.printf ("  │  Issue Date     : %-26s│%n", issueDate);
        System.out.printf ("  │  Due Date       : %-26s│%n", dueDate);
        System.out.printf ("  │  Return Date    : %-26s│%n",
                returnDate != null ? returnDate : "Not returned yet");
        System.out.printf ("  │  Fine           : PKR %-22.2f│%n", fine);
        System.out.printf ("  │  Status         : %-26s│%n", status);
        System.out.println("  └─────────────────────────────────────────────┘");
    }

    private String truncate(String s, int max) {
        return s != null && s.length() > max ? s.substring(0, max - 3) + "..." : s;
    }

    // ── Getters ──────────────────────────────────────────────
    public String getTransactionId() { return transactionId; }
    public String getMemberId()      { return memberId; }
    public String getMemberName()    { return memberName; }
    public String getBookIsbn()      { return bookIsbn; }
    public String getBookTitle()     { return bookTitle; }
    public String getIssueDate()     { return issueDate; }
    public String getDueDate()       { return dueDate; }
    public String getReturnDate()    { return returnDate; }
    public double getFine()          { return fine; }
    public Status getStatus()        { return status; }
    public boolean isReturned()      { return status == Status.RETURNED; }
}

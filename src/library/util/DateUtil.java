package library.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Utility methods for date formatting and due-date calculation.
 */
public class DateUtil {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private DateUtil() {}

    /** Today as a formatted string. */
    public static String today() {
        return LocalDate.now().format(FMT);
    }

    /** Due date = today + loanDays, formatted. */
    public static String dueDate(int loanDays) {
        return LocalDate.now().plusDays(loanDays).format(FMT);
    }

    /**
     * Days between dueDateStr and today.
     * Positive means overdue, negative means still within period.
     */
    public static long daysOverdue(String dueDateStr) {
        LocalDate due = LocalDate.parse(dueDateStr, FMT);
        return ChronoUnit.DAYS.between(due, LocalDate.now());
    }

    /** Generate a simple sequential transaction ID like TXN-0001. */
    public static String generateTransactionId(int nextNumber) {
        return String.format("TXN-%04d", nextNumber);
    }

    /** Generate a member ID like MEM-0012. */
    public static String generateMemberId(int nextNumber) {
        return String.format("MEM-%04d", nextNumber);
    }
}

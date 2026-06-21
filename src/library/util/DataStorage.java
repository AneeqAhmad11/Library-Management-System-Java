package library.util;

import library.model.Book;
import library.model.Member;
import library.model.Transaction;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles loading and saving of all data using Java Object Serialization.
 * Files are written to the /data directory relative to where the app runs.
 */
public class DataStorage {

    private static final String DATA_DIR      = "data/";
    private static final String BOOKS_FILE    = DATA_DIR + "books.dat";
    private static final String MEMBERS_FILE  = DATA_DIR + "members.dat";
    private static final String TRANS_FILE    = DATA_DIR + "transactions.dat";

    // ── Static utility class — no instances needed ───────────
    private DataStorage() {}

    /** Ensure the data directory exists before first write. */
    private static void ensureDataDir() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) dir.mkdirs();
    }

    // ── Books ────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    public static List<Book> loadBooks() {
        return (List<Book>) loadObject(BOOKS_FILE);
    }

    public static boolean saveBooks(List<Book> books) {
        return saveObject(books, BOOKS_FILE);
    }

    // ── Members ──────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    public static List<Member> loadMembers() {
        return (List<Member>) loadObject(MEMBERS_FILE);
    }

    public static boolean saveMembers(List<Member> members) {
        return saveObject(members, MEMBERS_FILE);
    }

    // ── Transactions ─────────────────────────────────────────
    @SuppressWarnings("unchecked")
    public static List<Transaction> loadTransactions() {
        return (List<Transaction>) loadObject(TRANS_FILE);
    }

    public static boolean saveTransactions(List<Transaction> transactions) {
        return saveObject(transactions, TRANS_FILE);
    }

    // ── Generic helpers ──────────────────────────────────────
    private static Object loadObject(String path) {
        File file = new File(path);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(file)))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("  [!] Could not load data from " + path + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private static boolean saveObject(Object data, String path) {
        ensureDataDir();
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(path)))) {
            oos.writeObject(data);
            return true;
        } catch (IOException e) {
            System.err.println("  [!] Could not save data to " + path + ": " + e.getMessage());
            return false;
        }
    }
}

package library.util;

import java.util.Scanner;

/**
 * Centralised console I/O helpers — keeps input/output logic out of
 * service classes and makes it easy to swap to a GUI later.
 */
public class ConsoleUI {

    private static final Scanner sc = new Scanner(System.in);
    private static final int WIDTH = 60;

    private ConsoleUI() {}

    // ── Screen / formatting ──────────────────────────────────
    public static void clearScreen() {
        // ANSI escape to clear — works in most terminals
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void printLine(char c) {
        System.out.println("  " + String.valueOf(c).repeat(WIDTH));
    }

    public static void printHeader(String title) {
        clearScreen();
        printLine('═');
        System.out.printf("  %-" + WIDTH + "s%n",
                "  📚  LIBRARY MANAGEMENT SYSTEM — " + title);
        printLine('═');
        System.out.println();
    }

    public static void printSection(String label) {
        System.out.println();
        System.out.println("  ── " + label + " " + "─".repeat(WIDTH - label.length() - 4));
    }

    public static void pressEnter() {
        System.out.print("\n  Press Enter to continue...");
        sc.nextLine();
    }

    public static void success(String msg) {
        System.out.println("\n  ✓  " + msg);
    }

    public static void error(String msg) {
        System.out.println("\n  ✗  " + msg);
    }

    public static void info(String msg) {
        System.out.println("  ℹ  " + msg);
    }

    // ── Input helpers ────────────────────────────────────────
    public static String readLine(String prompt) {
        System.out.print("  " + prompt + ": ");
        return sc.nextLine().trim();
    }

    public static int readInt(String prompt) {
        while (true) {
            System.out.print("  " + prompt + ": ");
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                error("Please enter a valid integer.");
            }
        }
    }

    public static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int val = readInt(prompt);
            if (val >= min && val <= max) return val;
            error("Please enter a number between " + min + " and " + max + ".");
        }
    }

    public static double readDouble(String prompt) {
        while (true) {
            System.out.print("  " + prompt + ": ");
            String input = sc.nextLine().trim();
            try {
                double val = Double.parseDouble(input);
                if (val >= 0) return val;
                error("Amount cannot be negative.");
            } catch (NumberFormatException e) {
                error("Please enter a valid number.");
            }
        }
    }

    public static boolean readConfirm(String prompt) {
        System.out.print("  " + prompt + " (y/n): ");
        String input = sc.nextLine().trim().toLowerCase();
        return input.equals("y") || input.equals("yes");
    }

    public static void tableHeader(String... cols) {
        System.out.println();
        System.out.print("  ");
        for (String col : cols) System.out.printf("%-20s", col);
        System.out.println();
        printLine('-');
    }
}

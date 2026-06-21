package library.interfaces;

import java.util.List;

/**
 * Generic search interface implemented by service classes.
 * Demonstrates interface usage and generics in Java OOP.
 */
public interface Searchable<T> {
    /** Search items whose name/title contains the keyword (case-insensitive). */
    List<T> searchByName(String keyword);

    /** Find a single item by its unique ID. Returns null if not found. */
    T findById(String id);
}

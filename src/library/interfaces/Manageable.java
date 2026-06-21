package library.interfaces;

/**
 * CRUD contract for any manageable resource (books, members).
 */
public interface Manageable<T> {
    boolean add(T item);
    boolean remove(String id);
    boolean update(T item);
    void    displayAll();
}

// Book.java
import java.util.Objects;

public class Book implements Comparable<Book> {
    private Integer bookId;
    private String title;
    private String author;
    private String category;
    private boolean isIssued;

    public Book(Integer bookId, String title, String author, String category, boolean isIssued) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isIssued = isIssued;
    }

    // Getters & setters
    public Integer getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public boolean isIssued() { return isIssued; }

    public void markAsIssued() { isIssued = true; }
    public void markAsReturned() { isIssued = false; }

    public void displayBookDetails() {
        System.out.printf("ID: %d | Title: %s | Author: %s | Category: %s | Issued: %s%n",
                bookId, title, author, category, isIssued ? "Yes" : "No");
    }

    @Override
    public int compareTo(Book other) {
        // Compare by title lexicographically (case-insensitive)
        return this.title.compareToIgnoreCase(other.title);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;
        return Objects.equals(bookId, book.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId);
    }

    // CSV serialization (simple, escapes commas)
    public String toCSVLine() {
        return bookId + "," + escape(title) + "," + escape(author) + "," + escape(category) + "," + isIssued;
    }

    public static String escape(String s) {
        if (s == null) return "";
        return s.replace(",", "&#44;"); // simple comma escape
    }

    public static String unescape(String s) {
        if (s == null) return "";
        return s.replace("&#44;", ",");
    }

    public static Book fromCSVLine(String line) {
        // Expected: id,title,author,category,isIssued
        String[] parts = line.split(",", 5);
        if (parts.length < 5) return null;
        try {
            Integer id = Integer.parseInt(parts[0]);
            String title = unescape(parts[1]);
            String author = unescape(parts[2]);
            String category = unescape(parts[3]);
            boolean isIssued = Boolean.parseBoolean(parts[4]);
            return new Book(id, title, author, category, isIssued);
        } catch (Exception e) {
            return null;
        }
    }
}

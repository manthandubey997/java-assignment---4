// LibraryManager.java
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LibraryManager {
    private Map<Integer, Book> books = new HashMap<>();
    private Map<Integer, Member> members = new HashMap<>();
    private Set<String> categories = new HashSet<>();
    private final Path booksFile = Paths.get("books.txt");
    private final Path membersFile = Paths.get("members.txt");

    public LibraryManager() {
        try {
            loadFromFile();
        } catch (Exception e) {
            System.out.println("Could not load data: " + e.getMessage());
        }
    }

    // ----------------- ID generation -----------------
    private int nextBookId() {
        return books.keySet().stream().max(Integer::compareTo).orElse(100) + 1;
    }

    private int nextMemberId() {
        return members.keySet().stream().max(Integer::compareTo).orElse(200) + 1;
    }

    // ----------------- Add book/member -----------------
    public Book addBook(String title, String author, String category) throws IOException {
        int id = nextBookId();
        Book b = new Book(id, title, author, category, false);
        books.put(id, b);
        categories.add(category);
        saveBooksToFile();
        return b;
    }

    public Member addMember(String name, String email) throws IOException {
        if (!isValidEmail(email)) throw new IllegalArgumentException("Invalid email format.");
        int id = nextMemberId();
        Member m = new Member(id, name, email, new ArrayList<>());
        members.put(id, m);
        saveMembersToFile();
        return m;
    }

    private boolean isValidEmail(String email) {
        if (email == null) return false;
        String regex = "^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$";
        return Pattern.compile(regex).matcher(email).matches();
    }

    // ----------------- Issue / Return -----------------
    public String issueBook(int bookId, int memberId) throws IOException {
        Book book = books.get(bookId);
        Member member = members.get(memberId);
        if (book == null) return "Book not found.";
        if (member == null) return "Member not found.";
        if (book.isIssued()) return "Book is already issued.";
        book.markAsIssued();
        member.addIssuedBook(bookId);
        saveBooksToFile();
        saveMembersToFile();
        return "Book issued successfully.";
    }

    public String returnBook(int bookId, int memberId) throws IOException {
        Book book = books.get(bookId);
        Member member = members.get(memberId);
        if (book == null) return "Book not found.";
        if (member == null) return "Member not found.";
        if (!book.isIssued()) return "Book is not marked as issued.";
        book.markAsReturned();
        member.returnIssuedBook(bookId);
        saveBooksToFile();
        saveMembersToFile();
        return "Book returned successfully.";
    }

    // ----------------- Search -----------------
    public List<Book> searchBooksByTitle(String q) {
        String lower = q.toLowerCase();
        return books.values().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public List<Book> searchBooksByAuthor(String q) {
        String lower = q.toLowerCase();
        return books.values().stream()
                .filter(b -> b.getAuthor().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public List<Book> searchBooksByCategory(String q) {
        String lower = q.toLowerCase();
        return books.values().stream()
                .filter(b -> b.getCategory().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    // ----------------- Sort -----------------
    public List<Book> sortBooksByTitle() {
        List<Book> list = new ArrayList<>(books.values());
        Collections.sort(list); // uses Comparable (title)
        return list;
    }

    public List<Book> sortBooksByAuthor() {
        List<Book> list = new ArrayList<>(books.values());
        list.sort(Comparator.comparing(Book::getAuthor, String.CASE_INSENSITIVE_ORDER));
        return list;
    }

    public List<Book> sortBooksByCategory() {
        List<Book> list = new ArrayList<>(books.values());
        list.sort(Comparator.comparing(Book::getCategory, String.CASE_INSENSITIVE_ORDER));
        return list;
    }

    // ----------------- Load & Save -----------------
    public void loadFromFile() throws IOException {
        // Ensure files exist
        if (!Files.exists(booksFile)) Files.createFile(booksFile);
        if (!Files.exists(membersFile)) Files.createFile(membersFile);

        // Load books
        try (BufferedReader br = Files.newBufferedReader(booksFile)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                Book b = Book.fromCSVLine(line);
                if (b != null) {
                    books.put(b.getBookId(), b);
                    categories.add(b.getCategory());
                }
            }
        }

        // Load members
        try (BufferedReader br = Files.newBufferedReader(membersFile)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                Member m = Member.fromCSVLine(line);
                if (m != null) members.put(m.getMemberId(), m);
            }
        }
    }

    public void saveBooksToFile() throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(booksFile)) {
            for (Book b : books.values()) {
                bw.write(b.toCSVLine());
                bw.newLine();
            }
        }
    }

    public void saveMembersToFile() throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(membersFile)) {
            for (Member m : members.values()) {
                bw.write(m.toCSVLine());
                bw.newLine();
            }
        }
    }

    // Convenience to save both
    public void saveToFile() {
        try {
            saveBooksToFile();
            saveMembersToFile();
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // ----------------- Utility methods for UI ------------
    public Optional<Book> getBookById(int id) { return Optional.ofNullable(books.get(id)); }
    public Optional<Member> getMemberById(int id) { return Optional.ofNullable(members.get(id)); }

    public void printAllBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }
        books.values().forEach(Book::displayBookDetails);
    }

    public void printAllMembers() {
        if (members.isEmpty()) {
            System.out.println("No members available.");
            return;
        }
        members.values().forEach(Member::displayMemberDetails);
    }

    public Set<String> getCategories() { return categories; }
}

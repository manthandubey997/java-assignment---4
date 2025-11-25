// Main.java
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static LibraryManager lm = new LibraryManager();

    public static void main(String[] args) {
        System.out.println("Welcome to City Library Digital Management System");
        Scanner sc = new Scanner(System.in);

        while (true) {
            printMenu();
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> addBookUI(sc);
                    case "2" -> addMemberUI(sc);
                    case "3" -> issueBookUI(sc);
                    case "4" -> returnBookUI(sc);
                    case "5" -> searchBooksUI(sc);
                    case "6" -> sortBooksUI(sc);
                    case "7" -> {
                        System.out.println("Saving and exiting...");
                        lm.saveToFile();
                        sc.close();
                        return;
                    }
                    case "8" -> {
                        lm.printAllBooks();
                    }
                    case "9" -> {
                        lm.printAllMembers();
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
            System.out.println(); // blank line
        }
    }

    private static void printMenu() {
        System.out.println("1. Add Book");
        System.out.println("2. Add Member");
        System.out.println("3. Issue Book");
        System.out.println("4. Return Book");
        System.out.println("5. Search Books");
        System.out.println("6. Sort Books");
        System.out.println("7. Exit");
        System.out.println("8. Debug: List all Books");
        System.out.println("9. Debug: List all Members");
        System.out.print("Enter your choice: ");
    }

    private static void addBookUI(Scanner sc) throws IOException {
        System.out.print("Enter Book Title: ");
        String title = sc.nextLine().trim();
        System.out.print("Enter Author: ");
        String author = sc.nextLine().trim();
        System.out.print("Enter Category: ");
        String category = sc.nextLine().trim();
        Book b = lm.addBook(title, author, category);
        System.out.println("Book added successfully with ID: " + b.getBookId());
    }

    private static void addMemberUI(Scanner sc) throws IOException {
        System.out.print("Enter Member Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Enter Member Email: ");
        String email = sc.nextLine().trim();
        Member m = lm.addMember(name, email);
        System.out.println("Member added successfully with ID: " + m.getMemberId());
    }

    private static void issueBookUI(Scanner sc) throws IOException {
        System.out.print("Enter Book ID to issue: ");
        int bookId = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Enter Member ID: ");
        int memberId = Integer.parseInt(sc.nextLine().trim());
        System.out.println(lm.issueBook(bookId, memberId));
    }

    private static void returnBookUI(Scanner sc) throws IOException {
        System.out.print("Enter Book ID to return: ");
        int bookId = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Enter Member ID: ");
        int memberId = Integer.parseInt(sc.nextLine().trim());
        System.out.println(lm.returnBook(bookId, memberId));
    }

    private static void searchBooksUI(Scanner sc) {
        System.out.println("Search by: 1) Title 2) Author 3) Category");
        String opt = sc.nextLine().trim();
        System.out.print("Enter search query: ");
        String q = sc.nextLine().trim();
        List<Book> results;
        switch (opt) {
            case "1" -> results = lm.searchBooksByTitle(q);
            case "2" -> results = lm.searchBooksByAuthor(q);
            case "3" -> results = lm.searchBooksByCategory(q);
            default -> {
                System.out.println("Invalid option.");
                return;
            }
        }
        if (results.isEmpty()) System.out.println("No books match your search.");
        else results.forEach(Book::displayBookDetails);
    }

    private static void sortBooksUI(Scanner sc) {
        System.out.println("Sort by: 1) Title 2) Author 3) Category");
        String opt = sc.nextLine().trim();
        List<Book> sorted;
        switch (opt) {
            case "1" -> sorted = lm.sortBooksByTitle();
            case "2" -> sorted = lm.sortBooksByAuthor();
            case "3" -> sorted = lm.sortBooksByCategory();
            default -> {
                System.out.println("Invalid option.");
                return;
            }
        }
        sorted.forEach(Book::displayBookDetails);
    }
}Main.java (menu)

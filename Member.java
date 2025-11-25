// Member.java
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Member {
    private Integer memberId;
    private String name;
    private String email;
    private List<Integer> issuedBooks; // list of book IDs

    public Member(Integer memberId, String name, String email, List<Integer> issuedBooks) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.issuedBooks = issuedBooks == null ? new ArrayList<>() : issuedBooks;
    }

    public Integer getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<Integer> getIssuedBooks() { return issuedBooks; }

    public void displayMemberDetails() {
        System.out.printf("ID: %d | Name: %s | Email: %s | IssuedBooks: %s%n",
                memberId, name, email, issuedBooks.toString());
    }

    public void addIssuedBook(int bookId) {
        if (!issuedBooks.contains(bookId)) issuedBooks.add(bookId);
    }

    public void returnIssuedBook(int bookId) {
        issuedBooks.remove(Integer.valueOf(bookId));
    }

    // CSV serialization: id,name,email,book1|book2|...
    public String toCSVLine() {
        StringBuilder sb = new StringBuilder();
        sb.append(memberId).append(",")
          .append(escape(name)).append(",")
          .append(escape(email)).append(",");
        if (issuedBooks != null && !issuedBooks.isEmpty()) {
            for (int i = 0; i < issuedBooks.size(); i++) {
                if (i > 0) sb.append("|");
                sb.append(issuedBooks.get(i));
            }
        }
        return sb.toString();
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace(",", "&#44;");
    }

    private static String unescape(String s) {
        if (s == null) return "";
        return s.replace("&#44;", ",");
    }

    public static Member fromCSVLine(String line) {
        String[] parts = line.split(",", 4);
        if (parts.length < 3) return null;
        try {
            Integer id = Integer.parseInt(parts[0]);
            String name = unescape(parts[1]);
            String email = unescape(parts[2]);
            List<Integer> issued = new ArrayList<>();
            if (parts.length >= 4 && parts[3].trim().length() > 0) {
                String[] ids = parts[3].split("\\|");
                for (String s : ids) {
                    if (!s.isBlank()) issued.add(Integer.parseInt(s));
                }
            }
            return new Member(id, name, email, issued);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Member member = (Member) o;
        return Objects.equals(memberId, member.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId);
    }
}

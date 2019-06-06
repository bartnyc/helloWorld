import java.util.List;
import java.util.Objects;

/**
 * Class that represents a book.
 * Titles are unique so only title is used for hashing and equals.
 * Authors are represent as a list of Strings.
 * POJO is threadsafe because is immutable.
 */
public class Book {

    private final String bookTitle;

    private final List<String> authors;

    /**
     * Default constructor.
     * @param bookTitle book title
     * @param authors list of authors
     */
    public Book(String bookTitle, List<String> authors) {
        this.bookTitle = bookTitle;
        this.authors = authors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return Objects.equals(bookTitle, book.bookTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookTitle);
    }

    /**
     * Book title getter.
     * @return book title
     */
    public String getBookTitle() {
        return bookTitle;
    }

    /**
     * List of authors setter.
     * @return List of authors.
     */
    public List<String> getAuthors() {
        return authors;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookTitle='" + bookTitle + '\'' +
                ", authors=" + authors +
                '}';
    }
}

import java.util.List;
import java.util.Set;

public interface BookDatabase {

    /**
     * Init data base with two equals list of book titles and list of set of authors
     *
     * @param titlesInitList  titles to init
     * @param authorsInitList authors to init
     */
    void init(List<String> titlesInitList, List<List<String>> authorsInitList);

    /**
     * In shutdown method all data should be cleaned.
     */
    void shutDown();

    /**
     * Removes book from the database for a given title.
     *
     * @param bookTitle input book will be removed
     * @return true if book was removes, if not false
     */
    boolean removeBookByTitle(String bookTitle);

    /**
     * Removes all books for a given author.
     *
     * @param author input author
     * @return returns true if books were removed , false otherwise
     */
    boolean removeBooksByAuthor(String author);

    /**
     * Returns all books form database for a given author.
     *
     * @param author input author
     * @return returns set of Books for a given author, empty set if author does not exist
     */
    Set<Book> queryBookByAuthor(String author);

    /**
     * Return all authors assigned to a given title.
     *
     * @param bookTitle input title
     * @return return list of authors for a given book or empty set if not exists
     */
    List<String> queryAuthorsByBookTitle(String bookTitle);

}

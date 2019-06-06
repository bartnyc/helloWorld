
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Default Database implementation.
 * This class keeps map of all book titles mapped to Books pojos.
 * Strings are immutable, Books pojos also so it is safe.
 * <p>
 * It contains also Multimap "authorToBooksMap" mapping author to multiple Books represented as a set.
 * Set is required for removing book for a given author in constant time O(1)
 */
public class DefaultBookDatabaseImpl implements BookDatabase {

    private final Logger logger = LogManager.getLogger(DefaultBookDatabaseImpl.class.getName());

    private final Map<String, Book> titleToBookMap;

    private final Map<String, Set<Book>> authorToBooksMap;

    /**
     * Default constructor.
     */
    public DefaultBookDatabaseImpl() {
        titleToBookMap = new HashMap<>();
        authorToBooksMap = new HashMap<>();
    }

    /**
     * Init method implementation.
     * Input data in two lists, both lists must have same size.
     * Input data in Strings for simplicity.
     * Single title for a given book must be not null, non empty list of authors must be provided.
     * Duplicates are not allowed, it is assumed that book has unique title.
     *
     * @param titlesInitList  titles to init
     * @param authorsInitList authors init list
     */
    public void init(List<String> titlesInitList, List<List<String>> authorsInitList) {
        logger.info("initializing books database...");
        if (titlesInitList == null || authorsInitList == null) {
            throw new NullPointerException("Lists must not be null.");
        }
        if (titlesInitList.size() != authorsInitList.size()) {
            throw new IllegalArgumentException("Both lists must have same size.");
        }

        for (int i = 0; i < titlesInitList.size(); i++) {
            String title = titlesInitList.get(i);
            List<String> authors = authorsInitList.get(i);
            if (title == null || authors == null || authors.isEmpty()) {
                throw new IllegalArgumentException("Wrong argument for init book at position: " + i);
            }
            Book book = new Book(title, authors);
            logger.info("Adding book : {} to database", book);
            boolean added = this.addBook(book);
            if (!added) {
                logger.warn("Book: {}, was not added !");
            }
        }
        logger.debug("All the titles in database after init: {}", titleToBookMap.keySet());
        logger.debug("All author to book mappings after init: {}", authorToBooksMap);
    }

    @Override
    public void shutDown() {
        logger.debug("Shutdown mode, flushing all data.");
        titleToBookMap.clear();
        authorToBooksMap.clear();
    }

    /**
     * Adds book to database : bookTitle -> book mapping
     * after that adds mapping : author -> book for all authors from the list.
     * Cost of adding one book is O(n) when "n" - number of authors
     *
     * @param book input book
     * @return false when book already on database, return otherwise
     */
    private boolean addBook(Book book) {
        if (titleToBookMap.containsKey(book.getBookTitle())) {
            logger.warn("This book: {} is database already", book);
            return false;
        }
        titleToBookMap.put(book.getBookTitle(), book);
        createAuthorToBookMapping(book);
        return true;
    }

    /**
     * Helper method for populate mapping, author -> input book
     *
     * @param book input book
     */
    private void createAuthorToBookMapping(Book book) {
        List<String> authors = book.getAuthors();
        logger.debug("Following book: '{}',  will be added as a reference for the following list of authors: {}", book.getBookTitle(), authors);
        for (String author : authors) {
            authorToBooksMap.putIfAbsent(author, new HashSet<>());
            authorToBooksMap.get(author).add(book);
        }

    }

    /**
     * Helper method for remove mapping: author -> book, for all authors of a book.
     * If author has no further books assigned, we are removing mapping.
     *
     * @param book input book for removing author references
     */
    private void deleteAuthorToBookMapping(Book book) {
        List<String> authors = book.getAuthors();
        for (String author : authors) {
            if (authorToBooksMap.containsKey(author)) {
                Set<Book> books = authorToBooksMap.get(author);
                books.remove(book);
                if (books.isEmpty()) {
                    logger.warn("Following author: {} has no referenced books, will be removed from database. ", author);
                    authorToBooksMap.remove(author);
                }
            }
        }
    }

    /**
     * This implementation removes all books from a given title and all the references from authors to this book.
     * It is assumes that this is infrequent operation.
     *
     * @param bookTitle input book will be removed
     * @return false for non existing titles, true if success
     */
    @Override
    public boolean removeBookByTitle(String bookTitle) {
        if (!titleToBookMap.containsKey(bookTitle)) {
            logger.warn("No such book title: {} in database", bookTitle);
            return false;
        }
        Book bookToRemove = titleToBookMap.get(bookTitle);
        logger.debug("All authors: {} with reference to the book: {} in database", bookToRemove.getAuthors(), bookTitle);
        titleToBookMap.remove(bookTitle);
        deleteAuthorToBookMapping(bookToRemove);
        return true;
    }

    /**
     * This implementation removes all the books for a given author.
     * It is assumes that this is infrequent operation.
     *
     * @param author input author
     * @return true if removed, false otherwise
     */
    @Override
    public boolean removeBooksByAuthor(String author) {
        if (!authorToBooksMap.containsKey(author)) {
            logger.warn("Unknown author: {}", author);
            return false;
        }
        // temporary copy here to avoid ConcurrentModificationException
        // we must be sure that book won't be removed from this set, passing iterator will complicate existing architecture
        Set<Book> books = new HashSet<>(authorToBooksMap.get(author));
        for (Book book : books) {
            removeBookByTitle(book.getBookTitle());
        }
        return true;
    }

    /**
     * Implementation of Book query by Author, runs in O(1) time.
     *
     * @param author input author
     * @return set of books, empty set for null queries or non existing entries
     */
    @Override
    public Set<Book> queryBookByAuthor(String author) {
        return authorToBooksMap.getOrDefault(author, Collections.emptySet());
    }

    /**
     * Implementation of Authors query by Book which runs in O(1) time.
     *
     * @param bookTitle input title
     * @return list of authors, empty list for null queries or non existing entries.
     */
    @Override
    public List<String> queryAuthorsByBookTitle(String bookTitle) {
        if (!titleToBookMap.containsKey(bookTitle)) {
            logger.warn("Book title: {} not found", bookTitle);
            return Collections.emptyList();
        }
        return titleToBookMap.get(bookTitle).getAuthors();
    }

    /**
     * Return size of books in memory
     *
     * @return number of all unique books
     */
    public int booksSize() {
        return titleToBookMap.keySet().size();
    }

    /**
     * Returns number of all unique authors in whole database
     *
     * @return number of all unique authors
     */
    public int authorsSize() {
        return authorToBooksMap.keySet().size();
    }
}

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DefaultBookDatabaseImplTest {

    private DefaultBookDatabaseImpl bookDatabase;

    private List<String> initTitles;

    private List<List<String>> initAuthors;

    @Before
    public void setUp() {
        initTitles = Arrays.asList("Book A", "Book B", "Book C");
        initAuthors = Arrays.asList(Arrays.asList("Alice", "Bob"),
                Arrays.asList("Audrey", "Bob"),
                Arrays.asList("Peter", "John", "Audrey"));
    }

    @After
    public void tearDown() {
        bookDatabase.shutDown();
    }

    @Test(expected = NullPointerException.class)
    public void testInitNullArguments() {
        bookDatabase = new DefaultBookDatabaseImpl();
        bookDatabase.init(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithDifferentSize() {
        bookDatabase = new DefaultBookDatabaseImpl();
        initTitles = Arrays.asList("Book A", "Book B");
        initAuthors = new ArrayList<>();
        bookDatabase.init(initTitles, initAuthors);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithBlankAuthorsForOneBook() {
        bookDatabase = new DefaultBookDatabaseImpl();
        initAuthors = Arrays.asList((Arrays.asList("Audrey", "Bob")),
                Arrays.asList("Peter", "John", "Audrey"),
                Collections.emptyList());
        bookDatabase.init(initTitles, initAuthors);
    }

    @Test
    public void testBooksAndAuthorsSizesAfterProperInit() {
        bookDatabase = new DefaultBookDatabaseImpl();
        bookDatabase.init(initTitles, initAuthors);
        assertEquals("Expected 3 books but got " + bookDatabase.booksSize() + " instead", 3, bookDatabase.booksSize());
        assertEquals("Expected 5 authors but got " + bookDatabase.authorsSize() + " instead", 5, bookDatabase.authorsSize());
    }

    @Test
    public void testAddBookWithDuplicate() {
        bookDatabase = new DefaultBookDatabaseImpl();
        initTitles = Arrays.asList("Book A", "Book B", "Book C", "Book C");
        initAuthors = Arrays.asList(Arrays.asList("Alice", "Bob"),
                Arrays.asList("Audrey", "Bob"),
                Arrays.asList("Peter", "John", "Audrey"),
                Arrays.asList("Peter", "John", "Audrey"));

        bookDatabase.init(initTitles, initAuthors);
        assertEquals("Expected 3 books but got " + bookDatabase.booksSize() + " instead", 3, bookDatabase.booksSize());
        assertEquals("Expected 5 authors but got " + bookDatabase.authorsSize() + " instead", 5, bookDatabase.authorsSize());
    }

    @Test
    public void testOneBookOneAuthorRemoveBook() {
        bookDatabase = new DefaultBookDatabaseImpl();

        initTitles = Collections.singletonList("Book A");
        initAuthors = Collections.singletonList(Collections.singletonList("Alice"));

        bookDatabase.init(initTitles, initAuthors);
        assertEquals("Expected 1 books but got " + bookDatabase.booksSize() + " instead", 1, bookDatabase.booksSize());
        assertEquals("Expected 1 authors but got " + bookDatabase.authorsSize() + " instead", 1, bookDatabase.authorsSize());

        boolean removed = bookDatabase.removeBookByTitle(initTitles.get(0));
        assertTrue(removed);

        assertEquals("Expected 0 books but got " + bookDatabase.booksSize() + " instead", 0, bookDatabase.booksSize());
        assertEquals("Expected 0 authors but got " + bookDatabase.authorsSize() + " instead", 0, bookDatabase.authorsSize());

    }

    @Test
    public void testOneBookOneAuthorRemoveAuthor() {
        bookDatabase = new DefaultBookDatabaseImpl();

        initTitles = Collections.singletonList("Book A");
        initAuthors = Collections.singletonList(Collections.singletonList("Alice"));

        bookDatabase.init(initTitles, initAuthors);
        assertEquals("Expected 1 books but got " + bookDatabase.booksSize() + " instead", 1, bookDatabase.booksSize());
        assertEquals("Expected 1 authors but got " + bookDatabase.authorsSize() + " instead", 1, bookDatabase.authorsSize());

        boolean removed = bookDatabase.removeBooksByAuthor("Alice");
        assertTrue(removed);

        assertEquals("Expected 0 books but got " + bookDatabase.booksSize() + " instead", 0, bookDatabase.booksSize());
        assertEquals("Expected 0 authors but got " + bookDatabase.authorsSize() + " instead", 0, bookDatabase.authorsSize());

    }

    @Test
    public void testOneBookTwoAuthorsRemoveBook() {
        bookDatabase = new DefaultBookDatabaseImpl();

        initTitles = Collections.singletonList("Book A");
        initAuthors = Collections.singletonList(Arrays.asList("Alice", "Bob"));

        bookDatabase.init(initTitles, initAuthors);
        assertEquals("Expected 1 books but got " + bookDatabase.booksSize() + " instead", 1, bookDatabase.booksSize());
        assertEquals("Expected 2 authors but got " + bookDatabase.authorsSize() + " instead", 2, bookDatabase.authorsSize());

        boolean removed = bookDatabase.removeBookByTitle(initTitles.get(0));
        assertTrue(removed);

        assertEquals("Expected 0 books but got " + bookDatabase.booksSize() + " instead", 0, bookDatabase.booksSize());
        assertEquals("Expected 0 authors but got " + bookDatabase.authorsSize() + " instead", 0, bookDatabase.authorsSize());

    }

    @Test
    public void testOneBookTwoAuthorsRemoveAuthor() {
        bookDatabase = new DefaultBookDatabaseImpl();

        initTitles = Collections.singletonList("Book A");
        initAuthors = Collections.singletonList(Arrays.asList("Alice", "Bob"));

        bookDatabase.init(initTitles, initAuthors);
        assertEquals("Expected 1 books but got " + bookDatabase.booksSize() + " instead", 1, bookDatabase.booksSize());
        assertEquals("Expected 2 authors but got " + bookDatabase.authorsSize() + " instead", 2, bookDatabase.authorsSize());

        boolean removed = bookDatabase.removeBooksByAuthor("Alice");
        assertTrue(removed);

        assertEquals("Expected 0 books but got " + bookDatabase.booksSize() + " instead", 0, bookDatabase.booksSize());
        assertEquals("Expected 0 authors but got " + bookDatabase.authorsSize() + " instead", 0, bookDatabase.authorsSize());

    }

    @Test
    public void testTwoBooksDisjointAuthorSetRemoveOneAuthor() {
        bookDatabase = new DefaultBookDatabaseImpl();

        initTitles = Arrays.asList("Book A", "Book B");
        initAuthors = Arrays.asList(Arrays.asList("Alice", "Bob"), Arrays.asList("Audrey", "Claire"));

        bookDatabase.init(initTitles, initAuthors);
        assertEquals("Expected 2 books but got " + bookDatabase.booksSize() + " instead", 2, bookDatabase.booksSize());
        assertEquals("Expected 4 authors but got " + bookDatabase.authorsSize() + " instead", 4, bookDatabase.authorsSize());

        boolean removed = bookDatabase.removeBooksByAuthor("Alice");
        assertTrue(removed);

        assertEquals("Expected 1 books but got " + bookDatabase.booksSize() + " instead", 1, bookDatabase.booksSize());
        assertEquals("Expected 2 authors but got " + bookDatabase.authorsSize() + " instead", 2, bookDatabase.authorsSize());

    }

    @Test
    public void testTwoBooksDisjointAuthorSetRemoveOneBook() {
        bookDatabase = new DefaultBookDatabaseImpl();

        initTitles = Arrays.asList("Book A", "Book B");
        initAuthors = Arrays.asList(Arrays.asList("Alice", "Bob"), Arrays.asList("Audrey", "Claire"));

        bookDatabase.init(initTitles, initAuthors);
        assertEquals("Expected 2 books but got " + bookDatabase.booksSize() + " instead", 2, bookDatabase.booksSize());
        assertEquals("Expected 4 authors but got " + bookDatabase.authorsSize() + " instead", 4, bookDatabase.authorsSize());

        boolean removed = bookDatabase.removeBookByTitle("Book A");
        assertTrue(removed);

        assertEquals("Expected 1 books but got " + bookDatabase.booksSize() + " instead", 1, bookDatabase.booksSize());
        assertEquals("Expected 2 authors but got " + bookDatabase.authorsSize() + " instead", 2, bookDatabase.authorsSize());

        removed = bookDatabase.removeBookByTitle("Book B");
        assertTrue(removed);

        assertEquals("Expected 0 books but got " + bookDatabase.booksSize() + " instead", 0, bookDatabase.booksSize());
        assertEquals("Expected 0 authors but got " + bookDatabase.authorsSize() + " instead", 0, bookDatabase.authorsSize());

    }

    @Test
    public void testRemoveNonExistingBook() {
        bookDatabase = new DefaultBookDatabaseImpl();

        initTitles = Arrays.asList("Book A", "Book B");
        initAuthors = Arrays.asList(Arrays.asList("Alice", "Bob"), Arrays.asList("Audrey", "Claire"));

        bookDatabase.init(initTitles, initAuthors);
        assertEquals("Expected 2 books but got " + bookDatabase.booksSize() + " instead", 2, bookDatabase.booksSize());
        assertEquals("Expected 4 authors but got " + bookDatabase.authorsSize() + " instead", 4, bookDatabase.authorsSize());

        boolean removed = bookDatabase.removeBookByTitle("Book X");
        assertFalse(removed);

        assertEquals("Expected 2 books but got " + bookDatabase.booksSize() + " instead", 2, bookDatabase.booksSize());
        assertEquals("Expected 4 authors but got " + bookDatabase.authorsSize() + " instead", 4, bookDatabase.authorsSize());

    }

    @Test
    public void testRemoveNonExistingAuthor() {
        bookDatabase = new DefaultBookDatabaseImpl();

        initTitles = Arrays.asList("Book A", "Book B");
        initAuthors = Arrays.asList(Arrays.asList("Alice", "Bob"), Arrays.asList("Audrey", "Claire"));

        bookDatabase.init(initTitles, initAuthors);
        assertEquals("Expected 2 books but got " + bookDatabase.booksSize() + " instead", 2, bookDatabase.booksSize());
        assertEquals("Expected 4 authors but got " + bookDatabase.authorsSize() + " instead", 4, bookDatabase.authorsSize());

        boolean removed = bookDatabase.removeBooksByAuthor("Jeannette");
        assertFalse(removed);

        assertEquals("Expected 2 books but got " + bookDatabase.booksSize() + " instead", 2, bookDatabase.booksSize());
        assertEquals("Expected 4 authors but got " + bookDatabase.authorsSize() + " instead", 4, bookDatabase.authorsSize());

    }

    @Test
    public void testRemoveAllBooksForSharedAuthor() {
        bookDatabase = new DefaultBookDatabaseImpl();

        initTitles = Arrays.asList("Book A", "Book B");
        initAuthors = Arrays.asList(Arrays.asList("Alice", "Bob"), Arrays.asList("Alice", "Claire"));

        bookDatabase.init(initTitles, initAuthors);
        assertEquals("Expected 2 books but got " + bookDatabase.booksSize() + " instead", 2, bookDatabase.booksSize());
        assertEquals("Expected 3 authors but got " + bookDatabase.authorsSize() + " instead", 3, bookDatabase.authorsSize());

        boolean removed = bookDatabase.removeBooksByAuthor("Alice");
        assertTrue(removed);

        assertEquals("Expected 0 books but got " + bookDatabase.booksSize() + " instead", 0, bookDatabase.booksSize());
        assertEquals("Expected 0 authors but got " + bookDatabase.authorsSize() + " instead", 0, bookDatabase.authorsSize());

    }

    @Test
    public void testRemoveBookWithSharedAuthor() {
        bookDatabase = new DefaultBookDatabaseImpl();

        initTitles = Arrays.asList("Book A", "Book B");
        initAuthors = Arrays.asList(Arrays.asList("Alice", "Bob"), Arrays.asList("Alice", "Claire"));

        bookDatabase.init(initTitles, initAuthors);
        assertEquals("Expected 2 books but got " + bookDatabase.booksSize() + " instead", 2, bookDatabase.booksSize());
        assertEquals("Expected 3 authors but got " + bookDatabase.authorsSize() + " instead", 3, bookDatabase.authorsSize());

        boolean removed = bookDatabase.removeBookByTitle("Book A");
        assertTrue(removed);

        assertEquals("Expected 1 books but got " + bookDatabase.booksSize() + " instead", 1, bookDatabase.booksSize());
        assertEquals("Expected 2 authors but got " + bookDatabase.authorsSize() + " instead", 2, bookDatabase.authorsSize());

        removed = bookDatabase.removeBookByTitle("Book B");
        assertTrue(removed);

        assertEquals("Expected 0 books but got " + bookDatabase.booksSize() + " instead", 0, bookDatabase.booksSize());
        assertEquals("Expected 0 authors but got " + bookDatabase.authorsSize() + " instead", 0, bookDatabase.authorsSize());

    }

    @Test
    public void testGetAllBooksForGivenAuthor() {
        bookDatabase = new DefaultBookDatabaseImpl();

        initTitles = Arrays.asList("Book A", "Book B");
        initAuthors = Arrays.asList(Arrays.asList("Alice", "Bob"), Arrays.asList("Alice", "Claire"));

        bookDatabase.init(initTitles, initAuthors);
        assertEquals("Expected 2 books but got " + bookDatabase.booksSize() + " instead", 2, bookDatabase.booksSize());
        assertEquals("Expected 3 authors but got " + bookDatabase.authorsSize() + " instead", 3, bookDatabase.authorsSize());

        Set<Book> expectedBooksByAlice = new HashSet<>();
        expectedBooksByAlice.add(new Book("Book A", Arrays.asList("Alice", "Bob")));
        expectedBooksByAlice.add(new Book("Book B", Arrays.asList("Alice", "Claire")));

        assertEquals(expectedBooksByAlice, bookDatabase.queryBookByAuthor("Alice"));

        Set<Book> expectedBooksByBob = new HashSet<>();
        expectedBooksByBob.add(new Book("Book A", Arrays.asList("Alice", "Bob")));

        assertEquals(expectedBooksByBob, bookDatabase.queryBookByAuthor("Bob"));

        Set<Book> expectedBooksByClaire = new HashSet<>();
        expectedBooksByClaire.add(new Book("Book B", Arrays.asList("Alice", "Claire")));

        assertEquals(expectedBooksByClaire, bookDatabase.queryBookByAuthor("Claire"));

        //now remove Bob, Alice should have 1 book, her books should be equal to Claire books

        boolean removed = bookDatabase.removeBooksByAuthor("Bob");
        assertTrue(removed);

        assertEquals(bookDatabase.queryBookByAuthor("Alice"), bookDatabase.queryBookByAuthor("Claire"));

    }

    @Test
    public void testGetAllBooksForGivenAuthorWhichDoNotExist() {
        bookDatabase = new DefaultBookDatabaseImpl();

        initTitles = Arrays.asList("Book A", "Book B");
        initAuthors = Arrays.asList(Arrays.asList("Alice", "Bob"), Arrays.asList("Alice", "Claire"));

        bookDatabase.init(initTitles, initAuthors);
        assertEquals("Expected 2 books but got " + bookDatabase.booksSize() + " instead", 2, bookDatabase.booksSize());
        assertEquals("Expected 3 authors but got " + bookDatabase.authorsSize() + " instead", 3, bookDatabase.authorsSize());

        assertEquals(Collections.emptySet(), bookDatabase.queryBookByAuthor("Diana"));

    }

    @Test
    public void testGetAllAuthorsForAGivenBook() {
        bookDatabase = new DefaultBookDatabaseImpl();

        initTitles = Arrays.asList("Book A", "Book B");
        initAuthors = Arrays.asList(Arrays.asList("Alice", "Bob"), Arrays.asList("Alice", "Claire"));

        bookDatabase.init(initTitles, initAuthors);
        assertEquals("Expected 2 books but got " + bookDatabase.booksSize() + " instead", 2, bookDatabase.booksSize());
        assertEquals("Expected 3 authors but got " + bookDatabase.authorsSize() + " instead", 3, bookDatabase.authorsSize());

        List<String> expectedAuthorsForBookA = Arrays.asList("Alice", "Bob");

        assertEquals(expectedAuthorsForBookA, bookDatabase.queryAuthorsByBookTitle("Book A"));

        List<String> expectedAuthorsForBookB = Arrays.asList("Alice", "Claire");

        assertEquals(expectedAuthorsForBookB, bookDatabase.queryAuthorsByBookTitle("Book B"));

    }

    @Test
    public void testGetAllAuthorsForAGivenBookWhichNotExist() {
        bookDatabase = new DefaultBookDatabaseImpl();

        initTitles = Arrays.asList("Book A", "Book B");
        initAuthors = Arrays.asList(Arrays.asList("Alice", "Bob"), Arrays.asList("Alice", "Claire"));

        bookDatabase.init(initTitles, initAuthors);
        assertEquals("Expected 2 books but got " + bookDatabase.booksSize() + " instead", 2, bookDatabase.booksSize());
        assertEquals("Expected 3 authors but got " + bookDatabase.authorsSize() + " instead", 3, bookDatabase.authorsSize());

        List<String> expectedAuthorsForBookGhostBook = new ArrayList<>();

        assertEquals(expectedAuthorsForBookGhostBook, bookDatabase.queryAuthorsByBookTitle("Book X"));

    }

    @Test
    public void testSameAuthorsAndDifferentBooks() {
        bookDatabase = new DefaultBookDatabaseImpl();

        initTitles = Arrays.asList("Book A", "Book B");
        initAuthors = Arrays.asList(Arrays.asList("Alice", "John"), Arrays.asList("Alice", "John"));

        bookDatabase.init(initTitles, initAuthors);
        assertEquals("Expected 2 books but got " + bookDatabase.booksSize() + " instead", 2, bookDatabase.booksSize());
        assertEquals("Expected 2 authors but got " + bookDatabase.authorsSize() + " instead", 2, bookDatabase.authorsSize());

        boolean removed = bookDatabase.removeBooksByAuthor("Alice");
        assertTrue(removed);

        assertEquals("Expected 0 books but got " + bookDatabase.booksSize() + " instead", 0, bookDatabase.booksSize());
        assertEquals("Expected 0 authors but got " + bookDatabase.authorsSize() + " instead", 0, bookDatabase.authorsSize());

        // now remove Book

        bookDatabase.init(initTitles, initAuthors);
        assertEquals("Expected 2 books but got " + bookDatabase.booksSize() + " instead", 2, bookDatabase.booksSize());
        assertEquals("Expected 2 authors but got " + bookDatabase.authorsSize() + " instead", 2, bookDatabase.authorsSize());

        removed = bookDatabase.removeBookByTitle("Book B");
        assertTrue(removed);

        assertEquals("Expected 1 books but got " + bookDatabase.booksSize() + " instead", 1, bookDatabase.booksSize());
        assertEquals("Expected 2 authors but got " + bookDatabase.authorsSize() + " instead", 2, bookDatabase.authorsSize());

    }

    @Test
    public void testMultipleScenarios() {
        bookDatabase = new DefaultBookDatabaseImpl();

        initTitles = Arrays.asList("Book A", "Book B", "Book C");
        initAuthors = Arrays.asList(Arrays.asList("Alice", "John"),
                Arrays.asList("Alice", "John"),
                Arrays.asList("Alice", "John"));

        bookDatabase.init(initTitles, initAuthors);
        assertEquals("Expected 3 books but got " + bookDatabase.booksSize() + " instead", 3, bookDatabase.booksSize());
        assertEquals("Expected 2 authors but got " + bookDatabase.authorsSize() + " instead", 2, bookDatabase.authorsSize());

        boolean removed = bookDatabase.removeBooksByAuthor("John");
        assertTrue(removed);

        assertEquals("Expected 0 books but got " + bookDatabase.booksSize() + " instead", 0, bookDatabase.booksSize());
        assertEquals("Expected 0 authors but got " + bookDatabase.authorsSize() + " instead", 0, bookDatabase.authorsSize());

        bookDatabase.init(initTitles, initAuthors);

        removed = bookDatabase.removeBookByTitle("Book A");
        assertTrue(removed);
        // now remove Book

        assertEquals("Expected 2 books but got " + bookDatabase.booksSize() + " instead", 2, bookDatabase.booksSize());
        assertEquals("Expected 2 authors but got " + bookDatabase.authorsSize() + " instead", 2, bookDatabase.authorsSize());

        removed = bookDatabase.removeBookByTitle("Book B");
        assertTrue(removed);
        // now remove Book

        assertEquals("Expected 1 books but got " + bookDatabase.booksSize() + " instead", 1, bookDatabase.booksSize());
        assertEquals("Expected 2 authors but got " + bookDatabase.authorsSize() + " instead", 2, bookDatabase.authorsSize());

        removed = bookDatabase.removeBookByTitle("Book C");
        assertTrue(removed);
        // now remove Book

        assertEquals("Expected 0 books but got " + bookDatabase.booksSize() + " instead", 0, bookDatabase.booksSize());
        assertEquals("Expected 0 authors but got " + bookDatabase.authorsSize() + " instead", 0, bookDatabase.authorsSize());

        initTitles = Arrays.asList("Book A", "Book B", "Book C", "Book D");
        initAuthors = Arrays.asList(Arrays.asList("Alice", "John", "Bob", "Claire"),
                Arrays.asList("Alice", "Claire", "Bob"),
                Arrays.asList("Alice", "Claire", "John"),
                Arrays.asList("Bob", "John", "Claire", "Susan"));

        bookDatabase.init(initTitles, initAuthors);
        assertEquals("Expected 4 books but got " + bookDatabase.booksSize() + " instead", 4, bookDatabase.booksSize());
        assertEquals("Expected 5 authors but got " + bookDatabase.authorsSize() + " instead", 5, bookDatabase.authorsSize());

        removed = bookDatabase.removeBooksByAuthor("Susan");
        assertTrue(removed);

        assertEquals("Expected 3 books but got " + bookDatabase.booksSize() + " instead", 3, bookDatabase.booksSize());
        assertEquals("Expected 4 authors but got " + bookDatabase.authorsSize() + " instead", 4, bookDatabase.authorsSize());

        removed = bookDatabase.removeBooksByAuthor("Bob");
        assertTrue(removed);

        assertEquals("Expected 1 books but got " + bookDatabase.booksSize() + " instead", 1, bookDatabase.booksSize());
        assertEquals("Expected 3 authors but got " + bookDatabase.authorsSize() + " instead", 3, bookDatabase.authorsSize());

        removed = bookDatabase.removeBookByTitle("Book C");
        assertTrue(removed);

        assertEquals("Expected 0 books but got " + bookDatabase.booksSize() + " instead", 0, bookDatabase.booksSize());
        assertEquals("Expected 0 authors but got " + bookDatabase.authorsSize() + " instead", 0, bookDatabase.authorsSize());

    }

    @Test
    public void testNullQueries() {
        bookDatabase = new DefaultBookDatabaseImpl();

        initTitles = Arrays.asList("Book A", "Book B");
        initAuthors = Arrays.asList(Arrays.asList("Alice", "Bob"), Arrays.asList("Alice", "Claire"));

        bookDatabase.init(initTitles, initAuthors);
        assertEquals("Expected 2 books but got " + bookDatabase.booksSize() + " instead", 2, bookDatabase.booksSize());
        assertEquals("Expected 3 authors but got " + bookDatabase.authorsSize() + " instead", 3, bookDatabase.authorsSize());

        Set<Book> resultAsSetOfBooks = bookDatabase.queryBookByAuthor(null);
        assertEquals(Collections.emptySet(), resultAsSetOfBooks);

        List<String> resultsAsListOfAuthors = bookDatabase.queryAuthorsByBookTitle(null);
        assertEquals(Collections.emptyList(), resultsAsListOfAuthors);

    }

    @Test
    public void testShutdown() {
        bookDatabase = new DefaultBookDatabaseImpl();

        initTitles = Arrays.asList("Book A", "Book B");
        initAuthors = Arrays.asList(Arrays.asList("Alice", "Bob"), Arrays.asList("Alice", "Claire"));

        bookDatabase.init(initTitles, initAuthors);
        assertEquals("Expected 2 books but got " + bookDatabase.booksSize() + " instead", 2, bookDatabase.booksSize());
        assertEquals("Expected 3 authors but got " + bookDatabase.authorsSize() + " instead", 3, bookDatabase.authorsSize());

        bookDatabase.shutDown();

        assertEquals("Expected 0 books but got " + bookDatabase.booksSize() + " instead", 0, bookDatabase.booksSize());
        assertEquals("Expected 0 authors but got " + bookDatabase.authorsSize() + " instead", 0, bookDatabase.authorsSize());


    }


}
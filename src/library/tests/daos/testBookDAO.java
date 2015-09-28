package library.tests.daos;

import junit.framework.Assert;
import library.daos.BookDAO;
import library.entities.Book;
import library.helpers.BookHelper;
import library.interfaces.entities.IBook;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jackson on 20/09/15.
 */
public class testBookDAO {

  private BookHelper bookHelper;
  private BookDAO bookDAO;
  private int numTestBooks;



// Before and After ############################################################

  @Before // Also tests addBook()
  public void SetUp() {
    bookHelper = new BookHelper();
    bookDAO = new BookDAO(bookHelper);

    //Create A few Test Books
    bookDAO.addBook("author1", "title1", "callNo1");
    bookDAO.addBook("author1", "title2", "callNo2");
    bookDAO.addBook("author2", "title4", "callNo3");
    bookDAO.addBook("author3", "title7", "callNo4");
    numTestBooks = bookDAO.listBooks().size();
  }

  @After
  public void takeDown(){
    bookHelper = null;
    bookDAO = null;
  }

  @Rule
  public final ExpectedException exception = ExpectedException.none();


  @Test
  public void testAll(){
    testFindBookByAuthor();
    testFindBookByTitle();
    testFindBookByAuthorTitle();
    testFindByID();
  }

  // Tests for Finding Books ###################################################

  @Test
  public void testFindBookByAuthor() {
    List<IBook> returnedBooks = bookDAO.findBooksByAuthor("author1");
    for (IBook book : returnedBooks){
      assert (book.getAuthor() == "author1");
    }
  }

  @Test
  public void testFindBookByTitle() {
    List<IBook> returnedBooks = bookDAO.findBooksByTitle("title1");
    for (IBook book : returnedBooks){
      assert (book.getAuthor() == "author1");
    }
  }

  @Test
  public void testFindBookByAuthorTitle() {
    List<IBook> returnedBooks = bookDAO.findBooksByAuthorTitle("author1", "title1");
    for (IBook book : returnedBooks){
      assert (book.getAuthor() == "author1");
    }
  }


  @Test
  public void testFindByID(){
    IBook book = bookDAO.getBookByID(1);
      assert (book.getID() == 1);
    exception.expect(NullPointerException.class);
    book = bookDAO.getBookByID(5);
    assert (book.getID() == 5);
    }

// Other Tests ################################################################

  @Test
  public void testBookList(){
    assert (bookDAO.listBooks().size() == numTestBooks);
  }

  @Test
  public void testAddBook(){
    bookDAO.addBook("aaa","aaa","aaa");
    assert (bookDAO.listBooks().size() == numTestBooks + 1);
  }
}


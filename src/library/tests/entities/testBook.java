package library.tests.entities;

import library.entities.Book;
import library.interfaces.entities.EBookState;
import library.interfaces.entities.IBook;
import org.junit.*;
import org.junit.rules.ExpectedException;

/**
 * Created by jackson on 20/09/15.
 */
public class testBook {
  private Book book;
  private int id;
  private String author, title, callNumber;

  //Before and After Test Functions ############################################

  @Before
  public void setUp(){
    System.out.println("@Before Running"); //was having issues getting @Before to run, just a confirmation message to ensure it's working
    id = 1;
    author = "Bruce Wayne";
    title = "fighting crime 101";
    callNumber ="ABC 3.111";
    book = new Book(id,author,title,callNumber);
  }

  @After
  public void tearDown(){
    book = null;
  }

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  //Constructor Tests ##########################################################

  @Test
  public void testAllParameters(){
    testIdParameters();
    book = null;
    testAuthorParameters();
    book = null;
    testTitleParameters();
    book = null;
    testCallNumberParameters();
  }



  @Test
  public void testIdParameters(){
    id = 1;
    book = new Book(id,author,title,callNumber);
    book = null;

    id = -1;
    exception.expect(IllegalArgumentException.class);
    book = new Book(id,author,title,callNumber);
    book = null;

    id = 0;
    book = new Book(id,author,title,callNumber);

  }

  @Test
  public void testAuthorParameters(){
    author = "abc";
    book = new Book(id,author,title,callNumber);
    book = null;

    author= null;
    exception.expect(IllegalArgumentException.class);
    book = new Book(id,author,title,callNumber);
    book = null;

    author = "";
    book = new Book(id,author,title,callNumber);
  }

  @Test // Expect a fail, fail, successful
  public void testTitleParameters(){
    System.out.println("#### Testing Title #######################");
    System.out.println("---- title = null ----");

    title = null;
    try {
      book = new Book(id,author,title,callNumber);
      System.out.println(" --> successful");}
    catch(IllegalArgumentException e) {
      System.out.println("!!! --> unsuccessful");
    }
    book = null;

    title = "";
    System.out.println("---- title = nothing ----");
    try {
      book = new Book(id,author,title,callNumber);
      System.out.println("--> successful");}
    catch(IllegalArgumentException e) {
      System.out.println("!!! --> unsuccessful");
    }
    book = null;

    System.out.println("---- title = 'test' ----");
    title = "test";
    try {
      book = new Book(id,author,title,callNumber);
      System.out.println(" --> successful");}
    catch(IllegalArgumentException e) {
      System.out.println("!!! --> unsuccessful");
    }
  }

  @Test // Expect a fail, fail, successful
  public void testCallNumberParameters(){
    callNumber = "abc";
    book = new Book(id,author,title,callNumber);
    book = null;

    callNumber = null;
    exception.expect(IllegalArgumentException.class);
    book = new Book(id,author,title,callNumber);
    book = null;

    callNumber = "";
    book = new Book(id,author,title,callNumber);
  }

  //Repair Tests ###############################################################

  @Test
  public void testRepairBad(){
  book.repair();
  }

  @Test
  public void testRepairGood(){
    book.setState(EBookState.DAMAGED);
    book.repair();
  }

  //Return Tests ###############################################################

  @Test
  public void testReturnUnDamaged(){
    book.setState(EBookState.ON_LOAN);
    book.returnBook(false);
    Assert.assertTrue(book.getState() == EBookState.AVAILABLE);
  }

  @Test
  public void testReturnDamaged(){
    book.setState(EBookState.ON_LOAN);
    book.returnBook(true);
    Assert.assertTrue(book.getState() == EBookState.DAMAGED);
  }

  //Lost Tests #################################################################

  @Test
  public void testLostCorrect(){
    book.setState(EBookState.ON_LOAN);
    book.lose();
    Assert.assertTrue(book.getState() == EBookState.LOST);
  }

  @Test
  public void testLostIncorrect(){
    book.setState(EBookState.AVAILABLE);
    book.lose();
    Assert.assertFalse(book.getState() == EBookState.LOST);
  }





}

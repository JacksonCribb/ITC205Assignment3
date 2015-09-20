package library.tests.entities;

import library.entities.Book;
import library.interfaces.entities.EBookState;
import library.interfaces.entities.IBook;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
  @Test // Expect a fail, fail, successful
  public void testIdParameters(){
    System.out.println("#### Testing Id's #######################");
    System.out.println("---- test id = -1 ----");
    id = -1;
    try {
    book = new Book(id,author,title,callNumber);
      System.out.println(" --> successful");}
    catch(IllegalArgumentException e) {
      System.out.println("!!! --> unsuccessful");
    }
    book = null;

    System.out.println("---- test id = 0 ----");
    id = 0;
    try {
      book = new Book(id,author,title,callNumber);
      System.out.println("--> successful");}
    catch(IllegalArgumentException e) {
      System.out.println("!!! --> unsuccessful");
    }
    book = null;
    System.out.println("---- test id = 1 ----");
    id = 1;
    try {
      book = new Book(id,author,title,callNumber);
      System.out.println(" --> successful");}
    catch(IllegalArgumentException e) {
      System.out.println("!!! --> unsuccessful");

    }
  }
  @Test // Expect a fail, fail, successful
  public void testAuthorParameters(){
    System.out.println("#### Testing author #######################");
    System.out.println("---- author = null ----");

    author = null;
    try {
      book = new Book(id,author,title,callNumber);
      System.out.println(" --> successful");}
    catch(IllegalArgumentException e) {
      System.out.println("!!! --> unsuccessful");
    }
    book = null;

    author = "";
    System.out.println("---- author = nothing ----");
    try {
      book = new Book(id,author,title,callNumber);
      System.out.println("--> successful");}
    catch(IllegalArgumentException e) {
      System.out.println("!!! --> unsuccessful");
    }
    book = null;

    System.out.println("---- author = 'test' ----");
    author = "test";
    try {
      book = new Book(id,author,title,callNumber);
      System.out.println(" --> successful");}
    catch(IllegalArgumentException e) {
      System.out.println("!!! --> unsuccessful");
    }
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
    System.out.println("#### Testing callNumber #######################");
    System.out.println("---- callNumber = null ----");

    callNumber = null;
    try {
      book = new Book(id,author,title,callNumber);
      System.out.println(" --> successful");}
    catch(IllegalArgumentException e) {
      System.out.println("!!! --> unsuccessful");
    }
    book = null;

    callNumber = "";
    System.out.println("---- callNumber = nothing ----");
    try {
      book = new Book(id,author,title,callNumber);
      System.out.println("--> successful");}
    catch(IllegalArgumentException e) {
      System.out.println("!!! --> unsuccessful");
    }
    book = null;

    System.out.println("---- callNumber = 'test' ----");
    callNumber = "test";
    try {
      book = new Book(id,author,title,callNumber);
      System.out.println(" --> successful");}
    catch(IllegalArgumentException e) {
      System.out.println("!!! --> unsuccessful");
    }
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

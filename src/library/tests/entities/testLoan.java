package library.tests.entities;

import library.entities.Book;
import library.entities.Loan;
import library.entities.Member;
import library.interfaces.entities.ELoanState;
import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import java.time.LocalDate;
import java.time.Month;


/**
 * Created by jackson on 20/09/15.
 */
public class testLoan {

  private IBook book;
  private IMember borrower;
  private LocalDate borrowDate, dueDate;
  private int id;
  private Loan loan;

  //Before and After ###########################################################
  @Before
  public void setUp(){
    id = 3;
    borrowDate = LocalDate.of(2015, Month.JANUARY,1);
    dueDate = LocalDate.of(2015, Month.JANUARY,12);
    book = new Book(2,"Terminator", "1001 ways to get to a chopper", "a125");
    borrower = new Member(3, "Gandalf", "the grey", "1231231", "superWizard69@theshire.com");
    loan = new Loan(id,book,borrower,borrowDate,dueDate);
  }
  // Constructor Tests #########################################################
  @Test
  public void testLoanConstructor(){

  }
  // tests for checkOverDueDate ################################################
  @Test
  public void checkOverDueTrue(){
    Assert.assertTrue(loan.checkOverDue(LocalDate.of(2015, Month.JANUARY, 13)));}
  @Test
  public void checkOverDueFalse(){
    Assert.assertFalse(loan.checkOverDue(LocalDate.of(2015, Month.JANUARY, 11)));}
  @Test //Expected, Runtime exception when loan State switches to PENDING
  public void checkInvalidState(){
    loan.setState(ELoanState.CURRENT);
    loan.checkOverDue(LocalDate.of(2015, Month.JANUARY, 13));
    loan.setState(ELoanState.OVERDUE);
    loan.checkOverDue(LocalDate.of(2015, Month.JANUARY, 13));
    loan.setState(ELoanState.PENDING);
    loan.checkOverDue(LocalDate.of(2015, Month.JANUARY, 13));
  }
  //test for isOverdue #########################################################
  @Test
  public void testIsOverdue(){
  loan.setState(ELoanState.OVERDUE);
    Assert.assertTrue(loan.isOverDue());}
  @Test
  public void testIsNotOverdue(){
    loan.setState(ELoanState.PENDING);
    Assert.assertFalse(loan.isOverDue());}

  // test Commit ###############################################################
  @Test
  public void testCommitPending(){
    loan.setState(ELoanState.PENDING);
    loan.commit(1);
  }
  @Test
  public void testCommitNotPending(){
    loan.setState(ELoanState.COMPLETE);
    loan.commit(1);
  }


}

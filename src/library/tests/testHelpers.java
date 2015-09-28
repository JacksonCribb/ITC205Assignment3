package library.tests;

import library.Main;
import library.entities.Book;
import library.entities.Loan;
import library.entities.Member;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

/**
 * Created by jackson on 20/09/15.
 */
public class testHelpers {

  private IBook book;
  private IMember member;
  private ILoan loan;

  @Test
  public void testHelperCreation(){
    book = new Book(1, "test", "test", "test");
    Assert.assertNotNull(book);
    member = new Member(1, "aaa", "aaa", "aaa", "aaa");
    Assert.assertNotNull(member);
    loan = new Loan(1,book, member, LocalDate.now(), LocalDate.of(2019, Month.JANUARY, 5));
    Assert.assertNotNull(loan);
  }

}

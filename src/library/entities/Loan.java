package library.entities;

import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;

import java.util.Date;

/**
 * Created by jackson on 20/09/15.
 */
public class Loan implements ILoan {

  private IBook book;
  private IMember borrower;
  private Date borrowDate, dueDate;
  private int loanId;


  public Loan(int id, IBook loanedBook, IMember borrowMember, Date dateBorrowed, Date dateDue){
  if (id > 0
          && loanedBook != null
          && borrowMember !=null
          && dateDue.after(dateBorrowed)){
    loanId = id;
    book = loanedBook;
    borrower = borrowMember;
    borrowDate = dateBorrowed;
    dueDate = dateDue;
  } else{
    throw new IllegalArgumentException("Constructor: incorrect Parameters");
  }
  }

  @Override
  public void commit(int id) {

  }

  @Override
  public void complete() {

  }

  @Override
  public boolean isOverDue() {
    return false;
  }

  @Override
  public boolean checkOverDue(Date currentDate) {
    return false;
  }

  @Override
  public IMember getBorrower() {
    return null;
  }

  @Override
  public IBook getBook() {
    return null;
  }

  @Override
  public int getID() {
    return 0;
  }
}

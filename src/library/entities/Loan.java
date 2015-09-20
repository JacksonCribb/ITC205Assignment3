package library.entities;

import library.interfaces.entities.ELoanState;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;

import java.time.LocalDate;
import java.util.Date;


/**
 * Created by jackson on 20/09/15.
 */
public class Loan implements ILoan {

  private IBook book;
  private IMember borrower;
  private LocalDate borrowDate, dueDate;
  private int id;
  private ELoanState loanState;


  public Loan(int loanId, IBook loanedBook, IMember borrowMember, LocalDate dateBorrowed, LocalDate dateDue){
  if (loanId > 0
          && loanedBook != null
          && borrowMember !=null
          && dateDue.isAfter(dateBorrowed)){
    id = loanId;
    book = loanedBook;
    borrower = borrowMember;
    borrowDate = dateBorrowed;
    dueDate = dateDue;
    loanState = ELoanState.CURRENT;
  } else{
    throw new IllegalArgumentException("Constructor: incorrect Parameters");
  }
  }

  //For Testing Only!!! do not use otherwise
  public void setState(ELoanState state){
    loanState = state;
  }

  @Override
  public void commit(int id) {
    if (loanState != ELoanState.PENDING){
      throw new RuntimeException("Loan state is not PENDING! loan state = " + loanState);}
    book.borrow(this);
    borrower.addLoan(this);
  }

  @Override
  public void complete() {
    if (loanState != ELoanState.CURRENT || loanState != ELoanState.OVERDUE){
      throw new RuntimeException("Invalid Loan State, cannot be CURRENT or OVERDUE! Current State = " + loanState);
    } else {
      loanState = ELoanState.COMPLETE;
    }
  }

  @Override
  public boolean isOverDue() {
    if (loanState == ELoanState.OVERDUE){
      return true;
    }
    return false;
  }

  @Override
  public boolean checkOverDue(LocalDate currentDate) {
   if (loanState != ELoanState.CURRENT && loanState != ELoanState.OVERDUE) {
     throw new RuntimeException("loan state is invalid! loan state = " + loanState);
   }

    if (currentDate.isAfter(dueDate)){
      return true;
    }
    return false;
  }


  // Getters ###################################################################
  @Override
  public IMember getBorrower() {
    return borrower;
  }
  @Override
  public IBook getBook() {
    return book;
  }
  @Override
  public int getID() {
    return id;
  }
}

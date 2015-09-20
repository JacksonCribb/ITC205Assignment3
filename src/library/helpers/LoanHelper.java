package library.helpers;

import library.entities.Loan;
import library.interfaces.daos.ILoanHelper;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;

import java.time.LocalDate;

/**
 * Created by jackson on 20/09/15.
 */
public class LoanHelper implements ILoanHelper {
  @Override

  public ILoan makeLoan(IBook book, IMember borrower, LocalDate borrowDate, LocalDate
          dueDate) {
    Loan loan = new Loan(0,book,borrower,borrowDate, dueDate);
    return loan;
  }
}

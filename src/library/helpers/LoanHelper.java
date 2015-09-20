package library.helpers;

import library.interfaces.daos.ILoanHelper;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;

import java.util.Date;

/**
 * Created by jackson on 20/09/15.
 */
public class LoanHelper implements ILoanHelper {
  @Override

  public ILoan makeLoan(IBook book, IMember borrower, Date borrowDate, Date
          dueDate) {
    return null;
  }
}

package library.daos;

import library.interfaces.daos.ILoanDAO;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;

import java.util.Date;
import java.util.List;

/**
 * Created by jackson on 20/09/15.
 */
public class LoanDao implements ILoanDAO{
  @Override
  public ILoan createLoan(IMember borrower, IBook book) {
    return null;
  }

  @Override
  public void commitLoan(ILoan loan) {

  }

  @Override
  public ILoan getLoanByID(int id) {
    return null;
  }

  @Override
  public ILoan getLoanByBook(IBook book) {
    return null;
  }

  @Override
  public List<ILoan> listLoans() {
    return null;
  }

  @Override
  public List<ILoan> findLoansByBorrower(IMember borrower) {
    return null;
  }

  @Override
  public List<ILoan> findLoansByBookTitle(String title) {
    return null;
  }

  @Override
  public void updateOverDueStatus(Date currentDate) {

  }

  @Override
  public List<ILoan> findOverDueLoans() {
    return null;
  }
}

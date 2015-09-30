package library.interfaces.daos;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;
import org.mockito.cglib.core.Local;

public interface ILoanDAO {
		
	public ILoan createLoan(IMember borrower, IBook book);

	public void commitLoan(ILoan loan);
	
	public ILoan getLoanByID(int id);
	
	public ILoan getLoanByBook(IBook book);
	
	public List<ILoan> listLoans();
	
	public List<ILoan> findLoansByBorrower(IMember borrower);

	public List<ILoan> findLoansByBookTitle(String title);

	public void updateOverDueStatusDate(Date currentDate);

	public List<ILoan> findOverDueLoans();

}


package library.interfaces.entities;

import java.time.LocalDate;

public interface ILoan {
	
	public static final int LOAN_PERIOD = 14;
	
	
	public void commit(int id);
	
	public void complete();
	
	public boolean isOverDue();
	
	public boolean checkOverDue(LocalDate currentDate);
	
	public IMember getBorrower();
	
	public IBook getBook();
	
	public int getID();
	

}

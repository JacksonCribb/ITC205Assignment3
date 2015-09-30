package library;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import library.interfaces.EBorrowState;
import library.interfaces.IBorrowUI;
import library.interfaces.IBorrowUIListener;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.entities.EBookState;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;
import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.ICardReaderListener;
import library.interfaces.hardware.IDisplay;
import library.interfaces.hardware.IPrinter;
import library.interfaces.hardware.IScanner;
import library.interfaces.hardware.IScannerListener;
import library.panels.borrow.ABorrowPanel;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BorrowUC_CTL implements ICardReaderListener, 
									 IScannerListener, 
									 IBorrowUIListener {
	
	private ICardReader reader;
	private IScanner scanner; 
	private IPrinter printer; 
	private IDisplay display;
	//private String state;
	private int scanCount = 0;
	private IBorrowUI ui;

	private EBorrowState state;
	private IBookDAO bookDAO;
	private IMemberDAO memberDAO;
	private ILoanDAO loanDAO;
	
	private List<IBook> bookList;
	private List<ILoan> loanList;
	private IMember borrower;

	private boolean overdue;
	private boolean atLoanLimit;
	private boolean hasFines;
	private boolean overFineLimit;

	private JPanel previous;



	public BorrowUC_CTL(ICardReader reader, IScanner scanner, 
			IPrinter printer, IDisplay display,
			IBookDAO bookDAO, ILoanDAO loanDAO, IMemberDAO memberDAO ) {
		this.reader = reader;
		this.scanner = scanner;
		this.printer = printer;
		this.display = display;
		this.bookDAO = bookDAO;
		this.loanDAO = loanDAO;
		this.memberDAO = memberDAO;
		this.ui = new BorrowUC_UI(this);
		state = EBorrowState.CREATED;

	}

	public BorrowUC_CTL(ICardReader reader, IScanner scanner,
						IPrinter printer, IDisplay display,
						IBookDAO bookDAO, ILoanDAO loanDAO, IMemberDAO memberDAO, IBorrowUI ui ) {
		this.reader = reader;
		this.scanner = scanner;
		this.printer = printer;
		this.display = display;
		this.bookDAO = bookDAO;
		this.loanDAO = loanDAO;
		this.memberDAO = memberDAO;
		this.ui = ui;
		state = EBorrowState.CREATED;

	}
	
	public void initialise() {
		previous = display.getDisplay();
		display.setDisplay((JPanel) ui, "Borrow UI");
		reader.addListener(this);
		reader.setEnabled(true);
		scanner.addListener(this);
		scanner.setEnabled(false);
		state = EBorrowState.INITIALIZED;
	}
	
	public void close() {
		display.setDisplay(previous, "Main Menu");
	}

	@Override
	public void cardSwiped(int memberID) {
		borrower = memberDAO.getMemberByID(memberID);
		scanCount = borrower.getLoans().size();
				//Has reached Fine Limit
		if (state != EBorrowState.INITIALIZED){
			throw new RuntimeException("BorrowState is not Initialized");
		}

		if (borrower.hasReachedFineLimit()) {
			ui.displayOverFineLimitMessage(borrower.getFineAmount());
			reader.setEnabled(false);
			scanner.setEnabled(false);
		}
				//Has Reached Loan Limit
		else if (borrower.hasReachedLoanLimit()){
		ui.displayAtLoanLimitMessage();
			setState(EBorrowState.BORROWING_RESTRICTED);
			reader.setEnabled(false);
			scanner.setEnabled(false);
		}
				//Has Payable Fines
		else {
			if (borrower.hasFinesPayable()){
				ui.displayOutstandingFineMessage(borrower.getFineAmount());
				setState(EBorrowState.SCANNING_BOOKS);
				reader.setEnabled(false);
				scanner.setEnabled(true);

			}
				//All good
			else {
				ui.displayMemberDetails(borrower.getID(), borrower.getFirstName(),borrower.getContactPhone());
				setState(EBorrowState.SCANNING_BOOKS);
				reader.setEnabled(false);
				scanner.setEnabled(true);
			}
		}


	}


	//TODO Complete Book Scanned Function
	@Override
	public void bookScanned(int barcode) {


		throw new RuntimeException("Not implemented yet");
	}

	
	private void setState(EBorrowState state) {
		this.state =  state;
	}

	@Override
	public void cancelled() {
		close();
	}
	
	@Override
	public void scansCompleted() {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public void loansConfirmed() {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public void loansRejected() {
		throw new RuntimeException("Not implemented yet");
	}

	private String buildLoanListDisplay(List<ILoan> loans) {
		StringBuilder bld = new StringBuilder();
		for (ILoan ln : loans) {
			if (bld.length() > 0) bld.append("\n\n");
			bld.append(ln.toString());
		}
		return bld.toString();		
	}

}

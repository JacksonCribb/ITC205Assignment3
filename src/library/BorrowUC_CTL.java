package library;

import java.util.*;

import javax.swing.JPanel;

import library.daos.BookDAO;
import library.daos.LoanDAO;
import library.entities.Book;
import library.hardware.Scanner;
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
import library.panels.borrow.ScanningPanel;


public class BorrowUC_CTL implements ICardReaderListener, 
									 IScannerListener, 
									 IBorrowUIListener {
	
	private ICardReader reader;
	private IScanner scanner; 
	private IPrinter printer;
	private IDisplay display;


	private int scanCount = 0;
	private int loanLimit = IMember.LOAN_LIMIT;
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
        bookList = new ArrayList<IBook>();
        loanList = new ArrayList<ILoan>();
		state = EBorrowState.INITIALIZED;
	}
	
	public void close() {
		display.setDisplay(previous, "Main Menu");
	}

	@Override
	public void cardSwiped(int memberID) {
		if (state != EBorrowState.INITIALIZED){
			throw new RuntimeException("BorrowState is not Initialized! State = " + state);
		}


		borrower = memberDAO.getMemberByID(memberID);

                // Has over Due Books
        if (borrower.hasOverDueLoans()) {
            setState(EBorrowState.BORROWING_RESTRICTED);
            ui.displayOverDueMessage();
            reader.setEnabled(false);
            scanner.setEnabled(false);
			scanCount = borrower.getLoans().size();
			loanList.addAll(borrower.getLoans());
        }

				//Has reached Fine Limit
		else if (borrower.hasReachedFineLimit()) {
            setState(EBorrowState.BORROWING_RESTRICTED);
			ui.displayOverFineLimitMessage(borrower.getFineAmount());
			reader.setEnabled(false);
			scanner.setEnabled(false);
			scanCount = borrower.getLoans().size();
			loanList.addAll(borrower.getLoans());
		}
				//Has Reached Loan Limit
		else if (borrower.hasReachedLoanLimit()){
            setState(EBorrowState.BORROWING_RESTRICTED);
		    ui.displayAtLoanLimitMessage();
			reader.setEnabled(false);
			scanner.setEnabled(false);
			scanCount = borrower.getLoans().size();
			loanList.addAll(borrower.getLoans());
		}
				//Has Payable Fines
		else if(borrower.hasFinesPayable()) {
            setState(EBorrowState.SCANNING_BOOKS);
            ui.displayOutstandingFineMessage(borrower.getFineAmount());
			reader.setEnabled(false);
			scanner.setEnabled(true);
			scanCount = borrower.getLoans().size();
			loanList.addAll(borrower.getLoans());}
				//All good
        else {
                setState(EBorrowState.SCANNING_BOOKS);
            ui.displayMemberDetails(borrower.getID(), borrower.getFirstName(),borrower.getContactPhone());
            reader.setEnabled(false);
            scanner.setEnabled(true);
            scanCount = borrower.getLoans().size();
           	loanList.addAll(borrower.getLoans());
			ui.displayExistingLoan(buildLoanListDisplay(loanList));
        }
    }


	@Override
	public void bookScanned(int barcode) {

        if (state != EBorrowState.SCANNING_BOOKS) {
            throw new RuntimeException("Borrow state is incorrect! state = " + state);
        }
        IBook book = bookDAO.getBookByID(barcode);

        if (book == null) {
            ui.displayErrorMessage("Book Does not exist!");

        } else if (loanDAO.getLoanByBook(book) != null) {
            ui.displayErrorMessage("Book is currently not available for loan");

        } else if (!loanList.isEmpty()) {
            for (ILoan l : loanList) {
                if (l.getBook().getID() == book.getID()) {
                    ui.displayErrorMessage("You have already Scanned this Book");
                }
            }
        } else {
            if (scanCount < loanLimit) {
                scanCount++;
                loanDAO.createLoan(borrower, book);
                loanList.add(loanDAO.createLoan(borrower, book));

                ui.displayPendingLoan(buildLoanListDisplay(loanList));

            } else {
                ui.displayErrorMessage("You have reached the maximum Number Of loans");
                scanner.setEnabled(false);
                setState(EBorrowState.SCANNING_BOOKS);
            }
        }
    }

	
	public void setState(EBorrowState state) {
		this.state =  state;
        ui.setState(state);
	}

	@Override
	public void cancelled() {
		close();
	}
	
	@Override
	public void scansCompleted() {
		if (state != EBorrowState.SCANNING_BOOKS){
			throw new RuntimeException("Borrower is incorrect State! state = " + state);
        }

        state = EBorrowState.CONFIRMING_LOANS;
        ui.setState(EBorrowState.CONFIRMING_LOANS);
        ui.displayPendingLoan(buildLoanListDisplay(loanList));
        scanner.setEnabled(false);

	}


	@Override
	public void loansConfirmed() {
        if (state != EBorrowState.CONFIRMING_LOANS){
            throw new RuntimeException("Borrower is incorrect State! state = " + state);
        }
        for(ILoan l: loanList){
            loanDAO.commitLoan(l);
            System.out.println(l.getBook().getTitle());
        }
        scanner.setEnabled(false);
        printer.print(buildLoanListDisplay(loanList));
        setState(EBorrowState.COMPLETED);
	}

	@Override
	public void loansRejected() {
        if (state != EBorrowState.CONFIRMING_LOANS){
            throw new RuntimeException("Borrower is incorrect State! state = " + state);
        }

        //Empty and reinitialize book and loan list
        loanList = null;
        bookList = null;
        bookList = new ArrayList<IBook>();
        loanList = new ArrayList<ILoan>();

        state = EBorrowState.CANCELLED;
        ui.setState(EBorrowState.CANCELLED);

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

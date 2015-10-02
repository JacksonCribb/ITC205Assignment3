package library.tests;

import library.BorrowUC_CTL;
import library.BorrowUC_UI;
import library.daos.BookDAO;
import library.daos.LoanDAO;
import library.daos.MemberDAO;
import library.entities.Book;
import library.entities.Loan;
import library.entities.Member;
import library.hardware.CardReader;
import library.hardware.Display;
import library.hardware.Printer;
import library.hardware.Scanner;
import library.helpers.BookHelper;
import library.helpers.LoanHelper;
import library.helpers.MemberHelper;
import library.interfaces.EBorrowState;
import library.interfaces.IBorrowUI;
import library.interfaces.entities.EBookState;
import library.interfaces.entities.ILoan;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by jackson on 2/10/15.
 */
public class testBorrowOtherFunctions {
    Scanner  scanner = mock(Scanner.class);
    CardReader cardReader = mock(CardReader.class);
    Display display = mock(Display.class);
    Printer printer = mock(Printer.class);


    BookDAO bookDAO = mock(BookDAO.class);
    LoanDAO loanDAO = mock(LoanDAO.class);
    MemberDAO memberDao = mock(MemberDAO.class);


    BorrowUC_CTL testBorrower;

    Member testMember = mock(Member.class);
    Book testBook = mock(Book.class);
    Loan testLoan = mock(Loan.class);

    IBorrowUI borrowUI =  mock(BorrowUC_UI.class);;
    List<ILoan> loanList;

    @Before
    public void setUp(){




        testBorrower = new BorrowUC_CTL(cardReader, scanner, printer, display, bookDAO, loanDAO, memberDao, borrowUI);

        testBorrower.setState(EBorrowState.SCANNING_BOOKS);

        //Add a book to the loans to stop pesky null pointer Exceptions
        when (bookDAO.getBookByID(1)).thenReturn(testBook);
        testBorrower.bookScanned(1);
    }

    @Test(expected=RuntimeException.class)
    //Check Scans Complete Verifies State
    public void testScansCompleteWrongState(){
        testBorrower.setState(EBorrowState.CANCELLED);
        testBorrower.scansCompleted();
    }

    @Test // Verify scansComplete() works correctly, also using as a checklist for what needs to be done by the time it finishes
    public void testScanComplete(){
        testBorrower.scansCompleted();
        verify (borrowUI).displayPendingLoan(anyString());
        verify (borrowUI).setState(EBorrowState.CONFIRMING_LOANS);
        verify (scanner).setEnabled(false);
    }

    @Test // verify LoansConfirmed() does everything it should
    public void testLoansConfirmed(){
        testBorrower.setState(EBorrowState.CONFIRMING_LOANS);
        testBorrower.loansConfirmed();
        verify (borrowUI).setState(EBorrowState.COMPLETED);
    }

    @Test // verify rejection does everything it needs to
    public void testRejection(){
        testBorrower.setState(EBorrowState.CONFIRMING_LOANS);
        testBorrower.loansRejected();
        verify (borrowUI).setState(EBorrowState.SCANNING_BOOKS);
    }

}

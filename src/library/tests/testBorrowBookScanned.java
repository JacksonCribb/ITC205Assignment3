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
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Created by jackson on 30/09/15.
 */
public class testBorrowBookScanned {
    Scanner scanner;
    CardReader cardReader;
    Printer printer;
    Display display;


    BookDAO bookDAO = mock(BookDAO.class);
    LoanDAO loanDAO = mock(LoanDAO.class);
    MemberDAO memberDao = mock(MemberDAO.class);

    BookHelper bookHelper;
    LoanHelper loanHelper;
    MemberHelper memberHelper;


    BorrowUC_CTL testBorrower;

    Member testMember = mock(Member.class);
    Book testBook = mock(Book.class);
    Loan testLoan = mock(Loan.class);

    IBorrowUI borrowUI =  mock(BorrowUC_UI.class);;
    List<ILoan> loanList;

    @Before
    public void setUp(){
        scanner = new Scanner();
        cardReader = new CardReader();
        display = mock(Display.class);

        bookHelper = new BookHelper();
        loanHelper = new LoanHelper();
        memberHelper = new MemberHelper();


        //create a list of borrowed Books
        loanList = new ArrayList<ILoan>();
        for (int i=0; i < 7 ; i++) {
            loanList.add(mock(Loan.class));
        }

        testBorrower = new BorrowUC_CTL(cardReader, scanner, printer, display, bookDAO, loanDAO, memberDao, borrowUI);
        testBorrower.setState(EBorrowState.SCANNING_BOOKS);
        when (bookDAO.getBookByID(anyInt())).thenReturn(testBook);
        when(testBook.getState()).thenReturn(EBookState.AVAILABLE);
        when (loanDAO.getLoanByBook(testBook)).thenReturn(testLoan);
    }

    @After
    public void tearDown(){
        scanner = null;
        cardReader = null;
        testBorrower = null;
        bookDAO = null;
    }

        //test Check for Borrow State
    @Test(expected=RuntimeException.class)
    public void testScanBookWrongState(){
        testBorrower.setState(EBorrowState.CANCELLED);
        testBorrower.bookScanned(1);
    }

    @Test //Test book Scanning with a non existent Book
    public void testNonExistingBook(){
        when(bookDAO.getBookByID(anyInt())).thenReturn(null);
        testBorrower.bookScanned(1);
        verify(borrowUI).displayErrorMessage("Book Does not exist!");
    }

    @Test //Test with book In a non Borrowable state
    public void testWithAlreadyBorrowedBook(){
        when (testBook.getState()).thenReturn(EBookState.DAMAGED);
        testBorrower.bookScanned(1);
        verify(borrowUI).displayErrorMessage("Book is currently not available for loan");
    }


    @Test //test Book is already in loan List
    public void testBookInLoanList(){
        testBorrower.bookScanned(1);
        when (testBook.getID()).thenReturn(1);
        testBorrower.bookScanned(2);
        verify (borrowUI).displayErrorMessage("Book has already been Scanned");

    }

    @Test //Test with all good to add new book
    public void testBookFine(){
        testBorrower.bookScanned(1);
       verify(borrowUI).displayPendingLoan(anyString());

    }

    @Test //test at maximumLoans
    public void testAtMaxLoans(){
        testBorrower.bookScanned(1);
        testBorrower.bookScanned(1);
        testBorrower.bookScanned(1);
        testBorrower.bookScanned(1);
        verify (borrowUI).displayErrorMessage("You have reached the maximum Number Of loans");
    }


}

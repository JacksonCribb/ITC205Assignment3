package library.tests;

import library.BorrowUC_CTL;
import library.BorrowUC_UI;
import library.daos.BookDAO;
import library.daos.LoanDAO;
import library.daos.MemberDAO;
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
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;
/**
 * Created by jackson on 2/10/15.
 */
public class fullTest {

    private Scanner     scanner;
    private Printer     printer = mock(Printer.class);
    private CardReader  reader;
    private Display     display = mock(Display.class);

    private LoanHelper  loanHelper;
    private BookHelper  bookHelper;
    private MemberHelper memberHelper;

    private BookDAO     bookDAO;
    private MemberDAO   memberDAO;
    private LoanDAO     loanDAO;

    BorrowUC_CTL testBorrow;
    BorrowUC_UI testUI = mock(BorrowUC_UI.class);
    IBook[] book = new IBook[15];
    IMember[] member = new IMember[6];


    @Before
    public void setUp() {

        loanHelper = new LoanHelper();
        bookHelper = new BookHelper();
        memberHelper = new MemberHelper();

        bookDAO = new BookDAO(bookHelper);
        loanDAO = new LoanDAO(loanHelper);
        memberDAO = new MemberDAO(memberHelper);

        reader = new CardReader();
        scanner = new Scanner();





        // Test Data


        book[0] = bookDAO.addBook("author1", "title1", "callNo1");
        book[1] = bookDAO.addBook("author1", "title2", "callNo2");
        book[2] = bookDAO.addBook("author1", "title3", "callNo3");
        book[3] = bookDAO.addBook("author1", "title4", "callNo4");
        book[4] = bookDAO.addBook("author2", "title5", "callNo5");
        book[5] = bookDAO.addBook("author2", "title6", "callNo6");
        book[6] = bookDAO.addBook("author2", "title7", "callNo7");
        book[7] = bookDAO.addBook("author2", "title8", "callNo8");
        book[8] = bookDAO.addBook("author3", "title9", "callNo9");
        book[9] = bookDAO.addBook("author3", "title10", "callNo10");
        book[10] = bookDAO.addBook("author4", "title11", "callNo11");
        book[11] = bookDAO.addBook("author4", "title12", "callNo12");
        book[12] = bookDAO.addBook("author5", "title13", "callNo13");
        book[13] = bookDAO.addBook("author5", "title14", "callNo14");
        book[14] = bookDAO.addBook("author5", "title15", "callNo15");

        member[0] = memberDAO.addMember("fName0", "lName0", "0001", "email0");
        member[1] = memberDAO.addMember("fName1", "lName1", "0002", "email1");
        member[2] = memberDAO.addMember("fName2", "lName2", "0003", "email2");
        member[3] = memberDAO.addMember("fName3", "lName3", "0004", "email3");
        member[4] = memberDAO.addMember("fName4", "lName4", "0005", "email4");
        member[5] = memberDAO.addMember("fName5", "lName5", "0006", "email5");

        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();

        //create a member with overdue loans
        for (int i = 0; i < 2; i++) {
            ILoan loan = loanDAO.createLoan(member[1], book[i]);
            loanDAO.commitLoan(loan);
        }
        cal.setTime(now);
        cal.add(Calendar.DATE, ILoan.LOAN_PERIOD + 1);
        Date checkDate = cal.getTime();
        loanDAO.updateOverDueStatusDate(checkDate);


        //create a member with maxed out unpaid fines
        member[2].addFine(10.0f);

        //create a member with maxed out loans
        for (int i = 2; i < 7; i++) {
            ILoan loan = loanDAO.createLoan(member[3], book[i]);
            loanDAO.commitLoan(loan);
        }

        //a member with a fine, but not over the limit
        member[4].addFine(5.0f);

        //a member with a couple of loans but not over the limit
        for (int i = 7; i < 9; i++) {
            ILoan loan = loanDAO.createLoan(member[5], book[i]);
            loanDAO.commitLoan(loan);
        }

        // Set everything Else up
        testBorrow = new BorrowUC_CTL(reader,scanner,printer,display,bookDAO,this.loanDAO,memberDAO, testUI);
        testBorrow.initialise();
    }

        // Test member #3 who has maxed out unpaid fines
    @Test(expected=RuntimeException.class)
    public void runFullTest(){
        testBorrow.cardSwiped(3);
        verify (testUI).displayOverFineLimitMessage(anyFloat());
        testBorrow.bookScanned(1);
        verify(scanner).setEnabled(false);
        verify(reader).setEnabled(false);
 }

    @Test(expected = RuntimeException.class)
    public void testWithSomeFines(){
        testBorrow.cardSwiped(5);
        verify (testUI).displayOutstandingFineMessage(anyFloat());
        testBorrow.bookScanned(1);
        verify(scanner).setEnabled(false);
        verify(reader).setEnabled(false);
    }

    @Test
    public void testWithSomeLoans(){
        testBorrow.cardSwiped(6);
        verify (testUI).setState(EBorrowState.SCANNING_BOOKS);
        testBorrow.bookScanned(2);
        verify (testUI).displayErrorMessage("Book is currently not available for loan");
        testBorrow.scansCompleted();
    }

    @Test(expected = RuntimeException.class)
    public void testWithMaxLoans(){
        testBorrow.cardSwiped(2);
        verify(testUI).displayOverDueMessage();
        testBorrow.bookScanned(3);
    }

    @Test
    public void testMemberUnpaidLoans(){
        testBorrow.cardSwiped(4);
        verify(testUI).displayAtLoanLimitMessage();
    }

    @Test
    public void testAllGoodReject(){
         testBorrow.cardSwiped(1);
        verify (testUI).setState(EBorrowState.SCANNING_BOOKS);
        testBorrow.bookScanned(12);
        verify (testUI).displayPendingLoan(any());
        testBorrow.scansCompleted();
        verify (testUI).setState(EBorrowState.CONFIRMING_LOANS);
        testBorrow.loansRejected();
        verify (testUI).setState(EBorrowState.CANCELLED);
        // Check the book Has not been borrowed
        Assert.assertNull(bookDAO.getBookByID(12).getLoan());
    }
    @Test
    public void testAllGoodConfirm(){
        testBorrow.cardSwiped(1);
        verify (testUI).setState(EBorrowState.SCANNING_BOOKS);
        testBorrow.bookScanned(12);
        verify (testUI).displayPendingLoan(any());
        testBorrow.scansCompleted();
        verify (testUI).setState(EBorrowState.CONFIRMING_LOANS);
        testBorrow.loansConfirmed();
        verify (testUI).setState(EBorrowState.COMPLETED);
        System.out.println(bookDAO.getBookByID(12).getAuthor());
        System.out.println(loanDAO.findLoansByBookTitle(bookDAO.getBookByID(12).getTitle()));
        // Check the book Has indeed Been Borrowed
        Assert.assertTrue(loanDAO.getLoanByBook(bookDAO.getBookByID(12)).getBorrower() == memberDAO.getMemberByID(1));
    }

}

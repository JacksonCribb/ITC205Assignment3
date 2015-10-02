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
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Created by jackson on 30/09/15.
 */
public class testBorrowCardSwiped {
    Scanner scanner;
    CardReader cardReader;
    Printer printer;
    Display display;


    BookDAO bookDAO;
    LoanDAO loanDAO;
    MemberDAO memberDao = mock(MemberDAO.class);

    BookHelper bookHelper;
    LoanHelper loanHelper;
    MemberHelper memberHelper;

    BorrowUC_CTL testBorrower;

    Member testMember = mock(Member.class);
    IBorrowUI borrowUI;
    List<ILoan> loanList;

    @Before
    public void setUp(){
        scanner = new Scanner();
        cardReader = new CardReader();
        display = mock(Display.class);

        bookHelper = new BookHelper();
        loanHelper = new LoanHelper();
        memberHelper = new MemberHelper();

        borrowUI = mock(BorrowUC_UI.class);

        bookDAO = new BookDAO(bookHelper);
        loanDAO = new LoanDAO(loanHelper);
        when (testMember.getFirstName()).thenReturn("Amanda");
        when (testMember.getLastName()).thenReturn("Hugankiss");
        when (testMember.getID()).thenReturn(0001);
        when (testMember.getContactPhone()).thenReturn("12313123");
        when (testMember.getEmailAddress()).thenReturn("apples@oranges.com");


        //create a list of borrowed Books
        loanList = new ArrayList<ILoan>();
        for (int i=0; i < 7 ; i++) {
            loanList.add(mock(Loan.class));
        }

        testBorrower = new BorrowUC_CTL(cardReader, scanner, printer, display, bookDAO, loanDAO, memberDao, borrowUI);

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
    public void testScanMemberNoInitialize(){
        testBorrower.cardSwiped(1);
    }

    @Test
    public void testAll(){
        outStandingFines();
        testWithOverFineLimit();
        testAtLoanLimit();
        testAllGood();
    }



    @Test // Member is over fine limit
    public void testWithOverFineLimit(){
        setTestMemberReturns(false, true, false);
        testBorrower.cardSwiped(1);
        verify(borrowUI).displayOverFineLimitMessage(anyFloat());
    }

    @Test // Member has outstanding fines but not at loan limit
    public void outStandingFines(){
        setTestMemberReturns(true, false, false);
        testBorrower.cardSwiped(1);
        verify(borrowUI).displayOutstandingFineMessage(anyFloat());}


    @Test // Member has reached their loan limit
    public void testAtLoanLimit(){
        setTestMemberReturns(false, false, true);
        testBorrower.cardSwiped(1);
        verify (borrowUI).displayAtLoanLimitMessage();
    }
    @Test // All is good for member to scan book for borrowing
    public void testAllGood(){
        setTestMemberReturns(false, false, false);
        testBorrower.cardSwiped(1);
        verify (borrowUI).displayMemberDetails(anyInt(), anyString(), anyString());
    }

        //Sets Returns for above functions to save typing
    private void setTestMemberReturns(boolean hasFines, boolean loanLimit, boolean fineLimit){

        when (testMember.hasFinesPayable()).thenReturn(hasFines);
        when (testMember.hasReachedFineLimit()).thenReturn(loanLimit);
        when (testMember.hasReachedLoanLimit()).thenReturn(fineLimit);

        when (testMember.getLoans()).thenReturn(loanList);
        when (memberDao.getMemberByID(anyInt())).thenReturn(testMember);
        when (testMember.getFineAmount()).thenReturn(1000.0f);

        testBorrower.initialise();
    }
}

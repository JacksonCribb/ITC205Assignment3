package library.entities;

import library.interfaces.entities.EBookState;
import library.interfaces.entities.ELoanState;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;

/**
 * Created by jackson on 19/09/15.
 */
public class Book implements IBook {

  private int id;
  private String author, title, callNumber;
  private ELoanState loan;
  private EBookState bookState;



  public Book(int bookId, String bookAuthor, String bookTitle, String bookCallNumber){
    if(bookId > 0
       && bookAuthor != null && !bookAuthor.isEmpty()
       && bookTitle != null && !bookTitle.isEmpty()
       && bookCallNumber != null && !bookCallNumber.isEmpty()){
          author = bookAuthor;
          id = bookId;
          title = bookTitle;
          callNumber = bookCallNumber;
          bookState = EBookState.AVAILABLE;
          loan = ELoanState.PENDING;
    }
    else {
      throw new IllegalArgumentException("constructor: Bad parameters");
    }
  }

  //TODO: this Function
  @Override
  public void borrow(ILoan loan) {

  }

  //Used for testing to set the state of the book, DO NOT USE OTHERWISE!!!
  public void setState(EBookState state){
  bookState = state;
  }

  @Override
  public ILoan getLoan() {

    if (loan != ELoanState.CURRENT){
    return null;

    }
    return null;
  }

  @Override
  public void returnBook(boolean damaged) {

    if (bookState == EBookState.AVAILABLE){
      throw new IllegalArgumentException("Book is currently not on loan");
    }
    if(damaged == true){
      bookState = EBookState.DAMAGED;
    } else {
      bookState = EBookState.AVAILABLE;
    }


  }

  @Override
  public void lose() {
    if (bookState == EBookState.ON_LOAN){
    bookState = EBookState.LOST;
    } else {
      throw new RuntimeException("Book is not currently on loan! bookState = " + bookState);
    }


  }

  @Override
  public void repair() {
  if (bookState == EBookState.DAMAGED){
    bookState = EBookState.AVAILABLE;
  } else {
    throw new RuntimeException("Book is not Damaged! BookState= " + bookState);
  }
  }

  @Override
  public void dispose() {
  bookState = EBookState.DISPOSED;
  }

  @Override
  public EBookState getState() {
    return bookState;
  }

  @Override
  public String getAuthor() {
    return author;
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public String getCallNumber() {
    return callNumber;
  }

  @Override
  public int getID() {
    return id;
  }
}

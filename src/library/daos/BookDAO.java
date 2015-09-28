package library.daos;

import library.entities.Book;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.IBookHelper;
import library.interfaces.entities.IBook;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jackson on 20/09/15.
 */
public class BookDAO implements IBookDAO {

  private List<IBook> bookList;
  private IBookHelper helper;
  private int bookNo = 1;

  public BookDAO(IBookHelper bookHelper){
  if (bookHelper == null){
    throw new IllegalArgumentException("BookDAO: Helper is Null!");}
    helper = bookHelper;
    bookList = new ArrayList<IBook>();
  }

  @Override
  public IBook addBook(String author, String title, String callNo) {
    IBook newBook = helper.makeBook(author, title,callNo, bookNo);
    bookList.add(newBook);
    bookNo ++;
    return newBook;
  }

  @Override
  public IBook getBookByID(int id) {
    for (IBook currentBook : bookList) {
      if (currentBook.getID() == id) {
        return currentBook;
      }
    }
    return null;
  }

  @Override
  public List<IBook> listBooks() {
    return bookList;
  }

  @Override
  public List<IBook> findBooksByAuthor(String author) {
    List<IBook> returnBooks = new ArrayList<IBook>();
    for (IBook currentBook: bookList){
      if (currentBook.getAuthor().equals(author)){
        returnBooks.add(currentBook);
      }
    }
    return returnBooks;
  }

  @Override
  public List<IBook> findBooksByTitle(String title) {
    List<IBook> returnBooks = new ArrayList<IBook>();
    for (IBook currentBook: bookList){
      if (currentBook.getTitle().equals(title)){
        returnBooks.add(currentBook);
      }
    }
    return returnBooks;
  }

  @Override
  public List<IBook> findBooksByAuthorTitle(String author, String title) {
    List<IBook> returnBooks = new ArrayList<IBook>();
    for (IBook currentBook: bookList){
      if (currentBook.getAuthor().equals(author) && currentBook.getTitle().equals(title)){
        returnBooks.add(currentBook);
      }
    }
    return returnBooks;
  }
}

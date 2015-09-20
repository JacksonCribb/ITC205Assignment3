package library.daos;

import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.IBookHelper;
import library.interfaces.entities.IBook;

import java.util.List;

/**
 * Created by jackson on 20/09/15.
 */
public class BookDAO implements IBookDAO {

  private IBookHelper helper;

  public BookDAO(IBookHelper bookHelper){
      
  }
  @Override
  public IBook addBook(String author, String title, String callNo) {
    return null;
  }

  @Override
  public IBook getBookByID(int id) {
    return null;
  }

  @Override
  public List<IBook> listBooks() {
    return null;
  }

  @Override
  public List<IBook> findBooksByAuthor(String author) {
    return null;
  }

  @Override
  public List<IBook> findBooksByTitle(String title) {
    return null;
  }

  @Override
  public List<IBook> findBooksByAuthorTitle(String author, String title) {
    return null;
  }
}

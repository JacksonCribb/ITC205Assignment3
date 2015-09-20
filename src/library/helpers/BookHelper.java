package library.helpers;

import library.interfaces.daos.IBookHelper;
import library.interfaces.entities.IBook;

/**
 * Created by jackson on 20/09/15.
 */
public class BookHelper implements IBookHelper{
  @Override
  public IBook makeBook(String author, String title, String callNumber, int
          id) {
    return null;
  }
}

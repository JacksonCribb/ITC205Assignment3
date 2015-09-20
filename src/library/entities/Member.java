package library.entities;

import library.interfaces.entities.EMemberState;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;

import java.util.List;

/**
 * Created by jackson on 20/09/15.
 */
public class Member implements IMember{

  private String firstName, lastName, contactPhone, emailAddress;
  private int id;

  public Member(int MemberId, String memberFirstName, String memberLastName, String memberContact, String memberEmail ){

  }

  @Override
  public boolean hasOverDueLoans() {
    return false;
  }

  @Override
  public boolean hasReachedLoanLimit() {
    return false;
  }

  @Override
  public boolean hasFinesPayable() {
    return false;
  }

  @Override
  public boolean hasReachedFineLimit() {
    return false;
  }

  @Override
  public float getFineAmount() {
    return 0;
  }

  @Override
  public void addFine(float fine) {

  }

  @Override
  public void payFine(float payment) {

  }

  @Override
  public void addLoan(ILoan loan) {

  }

  @Override
  public List<ILoan> getLoans() {
    return null;
  }

  @Override
  public void removeLoan(ILoan loan) {

  }

  @Override
  public EMemberState getState() {
    return null;
  }

  @Override
  public String getFirstName() {
    return null;
  }

  @Override
  public String getLastName() {
    return null;
  }

  @Override
  public String getContactPhone() {
    return null;
  }

  @Override
  public String getEmailAddress() {
    return null;
  }

  @Override
  public int getID() {
    return 0;
  }
}

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
  private EMemberState memberState;
  private ILoan loan;

  public Member(int memberId, String memberFirstName, String
          memberLastName, String memberContact, String memberEmail){
    if (    memberId > 0
            && memberFirstName != null  && !memberFirstName.isEmpty()
            && memberLastName != null   && !memberLastName.isEmpty()
            && memberContact != null    && !memberContact.isEmpty()
            && memberEmail != null      && !memberEmail.isEmpty()){
      id = memberId;
      firstName = memberFirstName;
      lastName = memberLastName;
      contactPhone = memberContact;
      emailAddress = memberEmail;
      memberState = EMemberState.BORROWING_ALLOWED;
    } else {
      throw new IllegalArgumentException("Constructor: Incorrect Parameters");
    }
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

  //getters ####################################################################

  @Override
  public EMemberState getState() {
    return memberState;
  }
  @Override
  public String getFirstName() {
    return firstName;
  }
  @Override
  public String getLastName() {
    return lastName;
  }
  @Override
  public String getContactPhone() {
    return contactPhone;
  }
  @Override
  public String getEmailAddress() {
    return emailAddress;
  }
  @Override
  public int getID() {
    return id;
  }
}

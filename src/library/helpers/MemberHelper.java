package library.helpers;

import library.entities.Member;
import library.interfaces.daos.IMemberHelper;
import library.interfaces.entities.IMember;

/**
 * Created by jackson on 20/09/15.
 */
public class MemberHelper implements IMemberHelper {
  @Override
  public IMember makeMember(String firstName, String lastName, String
          contactPhone, String emailAddress, int id) {
    Member member = new Member(id, firstName, lastName, contactPhone,emailAddress);
    return member;
  }
}

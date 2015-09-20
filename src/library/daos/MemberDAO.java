package library.daos;

import library.interfaces.daos.IMemberDAO;
import library.interfaces.entities.IMember;

import java.util.List;

/**
 * Created by jackson on 20/09/15.
 */
public class MemberDAO implements IMemberDAO {
  @Override
  public IMember addMember(String firstName, String lastName, String ContactPhone, String emailAddress) {
    return null;
  }

  @Override
  public IMember getMemberByID(int id) {
    return null;
  }

  @Override
  public List<IMember> listMembers() {
    return null;
  }

  @Override
  public List<IMember> findMembersByLastName(String lastName) {
    return null;
  }

  @Override
  public List<IMember> findMembersByEmailAddress(String emailAddress) {
    return null;
  }

  @Override
  public List<IMember> findMembersByNames(String firstName, String lastName) {
    return null;
  }
}

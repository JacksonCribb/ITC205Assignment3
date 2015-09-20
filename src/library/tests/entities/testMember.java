package library.tests.entities;


import library.entities.Member;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by jackson on 20/09/15.
 */
public class testMember {

  private String firstName, lastName, contactPhone, emailAddress;
  private int id;
  private Member Member;

  // before and after Stuff ####################################################
  @Before
  private void setUp(){
    id = 1;
    firstName = "Gandalf";
    lastName = "the gray";
    contactPhone = "0438556638";
    emailAddress = "superwizard69@theshire.com";
    Member = new Member(id, firstName, lastName, contactPhone, emailAddress);
  }
  @After
  public void tearDown(){
    Member = null;
  }

  //Constructor Tests ##########################################################
  @Test
  public void testAllParameters(){

  }









}



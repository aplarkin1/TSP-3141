package com.gpsworkers.gathr;

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.junit.runners.MethodSorters;
//import org.springframework.test.context.web.WebAppConfiguration;
import com.gpsworkers.gathr.exceptions.UserNotFoundException;
import com.gpsworkers.gathr.exceptions.UnauthorizedUserInteractionException;
// import com.gpsworkers.gathr.controllers.APIService;
import org.springframework.boot.test.context.SpringBootTest;
import com.gpsworkers.gathr.exceptions.NotAdminException;
import java.util.Collection;
import static org.assertj.core.api.Assertions.assertThat;
//import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;
import com.gpsworkers.gathr.mongo.groups.GroupRepository;
import com.gpsworkers.gathr.mongo.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.gpsworkers.gathr.mongo.users.User;
import com.gpsworkers.gathr.mongo.groups.Group;
import org.junit.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class groupTest {

  private static final String user1 = "someBody@gmail.com";
  private static final String user2 = "someoneElse@gmail.com";
  private static final String user3 = "someBodyElse@gmail.com";
  private static final String group = "cool group";



  @Autowired
  UserRepository userRepo;

  @Autowired
  GroupRepository groupRepo;

  @Before
  public void setup() throws UnauthorizedUserInteractionException, UserNotFoundException {
    userRepo.deleteById( user1 );
    userRepo.deleteById( user2 );
    userRepo.deleteById( user3 );
    groupRepo.deleteById( group );

    userRepo.insert(new User( "some", "body", "someBody@gmail.com"));
    userRepo.insert(new User( "someone", "else", "someoneElse@gmail.com"));
    userRepo.insert(new User( "somebody", "else", "someBodyElse@gmail.com"));
    groupRepo.insert(new Group( "cool group", userRepo.findByEmail("someBody@gmail.com")));
  }

  @Test
  public void addUser() {
    Group group1 = groupRepo.findByGroupName( group );
    group1.addUser( userRepo.findByEmail( user2 ));
    Collection<User> coll = group1.getUsers();
    assertThat( coll.contains( userRepo.findByEmail( user2 ))).isEqualTo( true );
  }

  /*@Test
  public void makeAdmin() {
    try {
      Group group1 = groupRepo.findByGroupName( group );
      group1.addUser( userRepo.findByEmail( user2 ) );
      group1.makeAdmin( userRepo.findByEmail( user1 ), userRepo.findByEmail( user2 ) );
      Collection<User> coll = group1.getAdmin();
      assertThat( coll.contains( userRepo.findByEmail( user2) ) ).isEqualTo( true );

    } catch ( NotAdminException ex ) {
      System.out.println( "user is not admin");
    }
  }
  @Test
  public void adminRemoveUser() {
    Group group1 = groupRepo.findByGroupName( group );
    group1.addUser( userRepo.findByEmail( user2 ) );
    try {
      group1.removeUser( userRepo.findByEmail( user1 ), userRepo.findByEmail( user2 ));
    } catch ( NotAdminException e ) {
        e.getMessage();
    }
    Collection<User> coll = group1.getUsers();
    assertThat( coll.contains( userRepo.findByEmail( user2) )).isEqualTo( false );
  }

  @Test
  public void userRemoveSelfe() {
    Group group1 = groupRepo.findByGroupName( group );
    group1.addUser( userRepo.findByEmail( user2 ) );
    try {
      group1.removeUser( userRepo.findByEmail( user2 ), userRepo.findByEmail( user2 ));
    } catch ( NotAdminException e ) {
      e.getMessage();
    }
    Collection<User> coll = group1.getUsers();
    assertThat( coll.contains( userRepo.findByEmail( user2) )).isEqualTo( false );
  }

  @Test( expected = NotAdminException.class )
  public void notAdminNotUserRemoveUser() throws NotAdminException {
    Group group1 = groupRepo.findByGroupName( group );
    group1.addUser( userRepo.findByEmail( user2 ) );
    group1.addUser( userRepo.findByEmail( user3 ) );
    group1.removeUser( userRepo.findByEmail( user3 ), userRepo.findByEmail( user2 ));
    Collection<User> coll = group1.getUsers();
    assertThat( coll.contains( userRepo.findByEmail( user2 ))).isEqualTo( true );
  } */
}

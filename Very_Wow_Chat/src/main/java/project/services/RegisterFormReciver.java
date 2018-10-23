package project.services;

import org.apache.commons.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;

/* Since java is a little bitch we need to make a class
 * that will serve as out json obj when reciving Json data from the client,
 * doe on the upside this is a good place to add all the validation code for 
 * validating client input  */
public class RegisterFormReciver {

  //@Autowired
  //private BCryptPasswordEncoder bCryptPasswordEncoder; // using bcrypt to encode our passwords
  
  // Receiving client data, that needs to be validated
  private final String username; // public data
  private final String password; // private data 
  private final String passwordReapeat; // used for validation
  private final String email;  // public data
  
  public RegisterFormReciver(String usr,String pass,String passRe,String email) {
    this.username = usr;
    this.password = pass;
    this.passwordReapeat = passRe;
    this.email = email;
  }
  
  /* Usage : usr.createTempUser()
   *  For  : usr is RegisterFormReciver class
   *  After: Creates a temporary state of user information that need to be validated by the user */
  public void createTempUser() {
    //String encodedPass = this.bCryptPasswordEncoder.encode(this.password);//Encryp our password
     
  }
  
  /* Usage : usr.validateEmail()
   *  For  : usr is RegisterFormReciver class
   *  After: checks if the email is of valid form */
  public boolean validateEmail() { return EmailValidator.getInstance().isValid(this.email); }
  
  /* Usage : usr.validatePassword()
   *  For  : usr is RegisterFormReciver class
   *  After: checks if both password and passwordReapeat match */
  public boolean validatePassword() { return this.password.contentEquals(this.passwordReapeat); }
  
  /* Usage : usr.userNameExists()
   *  For  : usr is RegisterFormReciver class
   *  After: Validates if the username of urs class is valid by checking services */
  public boolean userNameExists() { 
    return true;//remember call the function that Vilhelm implements}
  }
  
  /* ------------------------ AUTO GEN GETTERS AND SETTERS --------------------------*/
  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getPasswordReapeat() {
    return passwordReapeat;
  }

  public String getEmail() {
    return email;
  }
}

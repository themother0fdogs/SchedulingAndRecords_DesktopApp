package Model;

/** This is the UserInfo class that stores the user information from the mySQL database. */

public class UserInfo {

     int userId;
     String userName;
     String userPassword;

     public UserInfo(int userId, String userName, String userPassword) {
          this.userId = userId;
          this.userName = userName;
          this.userPassword = userPassword;
     }
     /**@return user Id*/
     public int getUserId() {
          return userId;
     }

     /**@param userId userId to set*/
     public void setUserId(int userId) {
          this.userId = userId;
     }

     /**@return username*/
     public String getUserName() {
          return userName;
     }

     /**@param userName username to set*/
     public void setUserName(String userName) {
          this.userName = userName;
     }

     /**@return user password */
     public String getUserPassword() {
          return userPassword;
     }

     /**@param userPassword user password to set*/
     public void setUserPassword(String userPassword) {
          this.userPassword = userPassword;
     }
}

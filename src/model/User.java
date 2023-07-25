package model;
/** Class provided to describe objects within Users.*/
public class User {
    public int userID;
    public String username;
    public String password;

    /**
     * Lists information within User.
     */
    public User() {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }


    /**
     * @return userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @param userID setter for userID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password setter for password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}

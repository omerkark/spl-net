package bgu.spl.net.api.bidi;

import java.util.List;
import java.util.Vector;

public class BGUUser {

    private String userName;
    private List<String> following;
    private String passWord;
    private boolean LogIn = false;
    //TODO: THINK IF THIS SHOULD BE HERE AND BYTE OR STRING.
    private List<String> FutreMessagesToBeSent;

    public BGUUser(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
        following = new Vector<>();
    }

    public String getUserName() {
        return userName;
    }

    public List<String> getFollowing() {
        return following;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setLogIn(boolean logIn) { LogIn = logIn; }
}

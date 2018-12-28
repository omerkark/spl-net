package bgu.spl.net.api.bidi;

import java.util.List;

public class BGUUser {

    private String userName;
    private List<BGUUser> following;
    private String passWord;
    private boolean LogIn = false;
    //TODO: THINK IF THIS SHOULD BE HERE AND BYTE OR STRING.
    private List<String> messagesToBeSent;

    public BGUUser(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    // return true if was able to follow or false otherwise.
    public boolean ToFollow(BGUUser clientToFollw){
        if(!following.contains(clientToFollw)) {
            following.add(clientToFollw);
            return true;
        }
        return false;
    }

    public String getUserName() {
        return userName;
    }

    public List<BGUUser> getFollowing() {
        return following;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setLogIn(boolean logIn) { LogIn = logIn; }
}

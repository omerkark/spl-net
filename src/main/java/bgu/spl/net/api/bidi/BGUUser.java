package bgu.spl.net.api.bidi;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class BGUUser {

    private String userName;
    private List<String> IAmfollowing;
    private List<String> Myfollowers;
    private String passWord;
    private boolean LogIn = false;
    private int numOfPosts = 0;

    //TODO: THINK IF THIS SHOULD BE HERE AND BYTE OR STRING.
    private List<String> FutreMessagesToBeSent;

    public BGUUser(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
        IAmfollowing = new Vector<>();
        Myfollowers = new Vector<>();
    }

    public String getUserName() {
        return userName;
    }

    public List<String> getIAmfollowing() {
        return IAmfollowing;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setLogIn(boolean logIn) { LogIn = logIn; }

    public int getNumUsersIAmFollowing(){
        return IAmfollowing.size();
    }

    public int getNumOfPosts() {
        return numOfPosts;
    }

    public void addFollower(String name){
        this.Myfollowers.add(name);
    }

    public int getNumOfFollowers(){
        return Myfollowers.size();
    }

    public void startFollowing(String userName){
        this.IAmfollowing.add(userName);
    }

    public void removeIAmFollowing(String userName){
        IAmfollowing.remove(userName);
    }

    public void removeFollower(String userName){
        Myfollowers.remove(userName);
    }
}

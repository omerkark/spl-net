package bgu.spl.net.api.bidi;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class BGUUser {

    private String userName;
    private List<String> following;
    private String passWord;
    private boolean LogIn = false;
    private int numOfPosts = 0;
    private AtomicInteger numberOfFollowers;

    //TODO: THINK IF THIS SHOULD BE HERE AND BYTE OR STRING.
    private List<String> FutreMessagesToBeSent;

    public BGUUser(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
        following = new Vector<>();
        numberOfFollowers = new AtomicInteger();
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

    public int usersIAmFollowing(){
        return following.size();
    }

    public int getNumOfPosts() {
        return numOfPosts;
    }

    public void incrmentNumOfFallowers(){ numberOfFollowers.incrementAndGet(); }

    public void decrmentNumOfFallowers(){ numberOfFollowers.decrementAndGet(); }

    public int usersFollowMe() { return numberOfFollowers.get(); }

}

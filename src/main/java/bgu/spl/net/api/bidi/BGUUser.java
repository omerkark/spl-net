package bgu.spl.net.api.bidi;

import bgu.spl.net.api.Messages.Notification;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class BGUUser {

    private String userName;
    private List<String> IAmfollowing;
    private List<String> Myfollowers;
    private String passWord;
    private boolean LogIn = false;
    private int numOfPosts = 0;
    private BlockingQueue<Notification> FutreMessagesToBeSent;

    public BGUUser(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
        IAmfollowing = new Vector<>();
        Myfollowers = new Vector<>();
        FutreMessagesToBeSent = new LinkedBlockingDeque<>();
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

    public void addToFuterMessage(Notification notification){
        this.FutreMessagesToBeSent.add(notification);
    }
}

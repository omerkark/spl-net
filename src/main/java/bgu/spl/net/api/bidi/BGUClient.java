package bgu.spl.net.api.bidi;

import java.util.List;

public class BGUClient {

    private String userName;
    private List<BGUClient> following;
    private String passWord;

    public BGUClient(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    // return true if was able to follow or false otherwise.
    public boolean ToFollow(BGUClient clientToFollw){
        if(!following.contains(clientToFollw)) {
            following.add(clientToFollw);
            return true;
        }
        return false;
    }

    public String getUserName() {
        return userName;
    }

    public List<BGUClient> getFollowing() {
        return following;
    }

    public String getPassWord() {
        return passWord;
    }
}

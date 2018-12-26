package bgu.spl.net.api.Messages;

import java.util.List;

public class Follow_UnFollow extends Message  {

    boolean Follow_true_Unfollow_false;
    private int numOfUsersToFollow;
    private List<String> UserNameList;

    public Follow_UnFollow(boolean follow_true_Unfollow_false, int numOfUsersToFollow, List<String> userNameList) {
        super((short)4);
        Follow_true_Unfollow_false = follow_true_Unfollow_false;
        this.numOfUsersToFollow = numOfUsersToFollow;
        UserNameList = userNameList;
    }
}

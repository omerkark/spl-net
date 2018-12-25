package bgu.spl.net.api.Messages;

import java.util.List;

public class Follow_UnFollow  {

    private short Opcode = 4;
    boolean Follow_true_Unfollow_false;
    private int numOfUsersToFollow;
    private List<String> UserNameList;

    public Follow_UnFollow(boolean follow_true_Unfollow_false, int numOfUsersToFollow, List<String> userNameList) {
        Follow_true_Unfollow_false = follow_true_Unfollow_false;
        this.numOfUsersToFollow = numOfUsersToFollow;
        UserNameList = userNameList;
    }
}

package bgu.spl.net.api.Messages;

public class StatsRequest extends Message {

    private String userNameToGetDataOn;

    public StatsRequest(String userNameToGetDAataOn) {
        super((short)8);
        this.userNameToGetDataOn = userNameToGetDAataOn;
    }

    public String getUserNameToGetDataOn() {
        return userNameToGetDataOn;
    }

}

package bgu.spl.net.api.Messages;

public class StatsRequest extends Message {

    private String userNameToGetDAataOn;

    public StatsRequest(String userNameToGetDAataOn) {
        super((short)8);
        this.userNameToGetDAataOn = userNameToGetDAataOn;
    }
}

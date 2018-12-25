package bgu.spl.net.api.Messages;

public class StatsRequest {

    private short Opcode = 8;
    private String userNameToGetDAataOn;

    public StatsRequest(String userNameToGetDAataOn) {
        this.userNameToGetDAataOn = userNameToGetDAataOn;
    }
}

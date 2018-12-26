package bgu.spl.net.api.Messages;

public class PM extends Message{

    private String UserNameToSendTo ;
    private String content;

    public PM(String userNameToSendTo, String content) {
        super((short)6);
        UserNameToSendTo = userNameToSendTo;
        this.content = content;
    }
}

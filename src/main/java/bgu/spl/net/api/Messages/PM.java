package bgu.spl.net.api.Messages;

public class PM {

    private short Opcode = 6;
    private String UserNameToSendTo ;
    private String content;

    public PM(String userNameToSendTo) {
        UserNameToSendTo = userNameToSendTo;
    }
}

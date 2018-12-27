package bgu.spl.net.api.Messages;

public class ErrorMessage extends Message{

    private short opCodeForMessage;

    public ErrorMessage(short opcode, short opCodeForMessage) {
        super(opcode);
        this.opCodeForMessage = opCodeForMessage;
    }
}

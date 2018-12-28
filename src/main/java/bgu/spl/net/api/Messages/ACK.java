package bgu.spl.net.api.Messages;

public class ACK extends Message {

    private String optional;
    private short opCodeForMessage;

    public ACK(short opcode, short opCodeForMessage) {
        super(opcode);
        this.opCodeForMessage = opCodeForMessage;
    }

    public void setOptional(String optional) {
        this.optional = optional;
    }

    public String getOptional() {
        return optional;
    }

    public short getOpCodeForMessage() {
        return opCodeForMessage;
    }
}

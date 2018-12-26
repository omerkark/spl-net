package bgu.spl.net.api.Messages;

public class Message {
    private short Opcode;

    public Message(short opcode) {
        Opcode = opcode;
    }

    public short getOpcode() {
        return Opcode;
    }
}

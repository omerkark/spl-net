package bgu.spl.net.api.Messages;

public class Logout extends Message{

    private short Opcode = 3;

    public Logout(short opcode) {
        super((short)3);
    }
}

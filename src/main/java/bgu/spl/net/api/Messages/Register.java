package bgu.spl.net.api.Messages;

public class Register {

    private short Opcode = 1;
    private String userName;
    private String PassWord;

    public Register(String userName, String passWord) {
        this.userName = userName;
        PassWord = passWord;
    }
}

package bgu.spl.net.api.Messages;

public class Register extends Message {


    private String userName;
    private String PassWord;

    public Register(String userName, String passWord) {
        super((short)1);
        this.userName = userName;
        PassWord = passWord;
    }
}

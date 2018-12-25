package bgu.spl.net.api.Messages;

import java.util.List;

public class Login  {

    private short Opcode =2;
    private String UserName;
    private String PassWord;

    public Login(String userName, String passWord) {
        UserName = userName;
        PassWord = passWord;
    }
}

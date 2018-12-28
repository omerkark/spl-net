package bgu.spl.net.api.Messages;

public class Login extends Message {

    private String UserName;
    private String PassWord;


    public Login(String userName, String passWord) {
        super((short)2);
        UserName = userName;
        PassWord = passWord;
    }

    public String getUserName() {
        return UserName;
    }

    public String getPassWord() {
        return PassWord;
    }
}

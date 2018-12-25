package bgu.spl.net.api.Messages;

public class Post  {

    private short OpCode = 5;
    private String Content;

    public Post(String content) {
        Content = content;
    }
}

package bgu.spl.net.api.Messages;

public class Post extends Message  {

    private String Content;

    public Post(String content) {
        super((short)5);
        Content = content;
    }

    public String getContent() {
        return Content;
    }
}

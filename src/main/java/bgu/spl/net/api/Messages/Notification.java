package bgu.spl.net.api.Messages;

public class Notification extends Message {

    private char PM_Post;
    private String PostingUserName;
    private String Content;

    public Notification(char pm_post, String postingUserName, String content) {
        super((short)9);
        this.Content = content;
        this.PM_Post = pm_post;
        this.PostingUserName = postingUserName;
    }

    public char getPM_Post() {
        return PM_Post;
    }

    public String getPostingUserName() {
        return PostingUserName;
    }

    public String getContent() {
        return Content;
    }
}

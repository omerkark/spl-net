package bgu.spl.net.api.Messages;

import bgu.spl.net.api.Messages.Message;

public class Notification extends Message {

    private char PM_Post;
    private String PostingUserName;
    private String Content;

    public Notification() {
        super((short)9);
    }
}

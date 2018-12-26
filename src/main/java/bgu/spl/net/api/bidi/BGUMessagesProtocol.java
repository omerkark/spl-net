package bgu.spl.net.api.bidi;

import bgu.spl.net.api.Messages.Message;

public class BGUMessagesProtocol implements BidiMessagingProtocol<Message> {

    @Override
    public void start(int connectionId, Connections<Message> connections) {
        // here if he is registerd proces or register it;
    }

    @Override
    public void process(Message message) {

    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }
}

package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.Messages.Message;
import bgu.spl.net.api.bidi.BGUMessagesProtocol;
import bgu.spl.net.api.bidi.DataBase;
import bgu.spl.net.api.bidi.EncoderDecoder;
import bgu.spl.net.impl.rci.RemoteCommandInvocationProtocol;
import bgu.spl.net.srv.Server;

public class TPCMain {

    public static void main(String[] args) {

        DataBase dataBase = DataBase.getInstance();


        Server.threadPerClient(
                7777, //port
                () -> new BGUMessagesProtocol(dataBase), //protocol factory
                EncoderDecoder::new //message encoder decoder factory
        ).serve();


    }
}

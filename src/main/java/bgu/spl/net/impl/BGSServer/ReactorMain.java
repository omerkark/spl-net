package bgu.spl.net.impl.BGSServer;
import bgu.spl.net.api.bidi.BGUConnections;
import bgu.spl.net.api.bidi.BGUMessagesProtocol;
import bgu.spl.net.api.bidi.DataBase;
import bgu.spl.net.api.bidi.EncoderDecoder;
import bgu.spl.net.srv.Server;

public class ReactorMain {

    public static void main(String[] args) {

        DataBase dataBase = new DataBase();
        BGUConnections bguConnections = new BGUConnections();

        Server.reactor(
                Runtime.getRuntime().availableProcessors(),
                7777, //port
                () ->  new BGUMessagesProtocol(dataBase), //protocol factory
                EncoderDecoder::new, //message encoder decoder factory
                bguConnections
        ).serve();
    }
}

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

        //Runtime.getRuntime().availableProcessors(),
        Server.reactor(
                Integer.parseInt(args[1]),Integer.parseInt(args[0]), //port
                () ->  new BGUMessagesProtocol(dataBase), //protocol factory
                EncoderDecoder::new, //message encoder decoder factory
                bguConnections
        ).serve();
    }
}

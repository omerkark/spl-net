package bgu.spl.net.api.bidi;

import bgu.spl.net.api.Messages.*;
import bgu.spl.net.srv.ConnectionHandler;
import com.sun.xml.internal.bind.v2.TODO;

import java.lang.Error;
import java.sql.Connection;

public class BGUMessagesProtocol implements BidiMessagingProtocol<Message> {

    private short opcode;
    private Connections <Message> Connection;
    private int id;
    private DataBase dataBase;

    @Override
    public void start(int connectionId, Connections<Message> connections) {
        Connection  = connections;
        this.id = connectionId;
    }

    @Override
    public void process(Message message) {

        DataBase dataBase= DataBase.getInstance();


        if(message instanceof Register){
            if(dataBase.coantainsClient(id)){
                ErrorMessage errorMessage = new ErrorMessage((short)11, (short)1);
                Connection.send(id, errorMessage);
            }
            else {
                // register a new client.
                String name = ((Register) message).getUserName();
                String password = ((Register) message).getPassWord();
                BGUClient bguClient = new BGUClient(name, password);
                dataBase.addCustomer(bguClient, id);
                ACK ack = new ACK((short)10 , (short)1);
                Connection.send(id, ack);
            }
        }
        else if(message instanceof Login){
            if(!dataBase.coantainsClient(id)){
                ErrorMessage errorMessage = new ErrorMessage((short)11, (short)2);
                Connection.send(id, errorMessage);
            }
        }
        else if(message instanceof Logout){

        }
        else if(message instanceof Follow_UnFollow){

        }
        else if(message instanceof Post){

        }
        else if(message instanceof PM){

        }
        else if(message instanceof UserListRequest){

        }
        else if(message instanceof StatsRequest){

        }
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }
}

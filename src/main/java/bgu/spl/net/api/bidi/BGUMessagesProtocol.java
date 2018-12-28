package bgu.spl.net.api.bidi;

import bgu.spl.net.api.Messages.*;

import java.util.List;

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

        DataBase dataBase = DataBase.getInstance();

        //~~~~~~~~~~~~~~~~~~~~~~~~~REGISTER~~~~~~~~~~~~~~~~~~~~~
        if(message instanceof Register){
            String name = ((Register) message).getUserName();
            String password = ((Register) message).getPassWord();
            BGUUser bguUser = new BGUUser(name, password);
            // the user is already registered or there is a user with the same name
            if(dataBase.containsUser(bguUser.getUserName())){
                Connection.send(id, new ErrorMessage((short)11, (short)1));
            }
            else {
                // register a new user.
                dataBase.addCustomer(bguUser);
                Connection.send(id, new ACK((short)10 , (short)1));
            }
        }
        //~~~~~~~~~~~~~~~~~~~~~~~~~LOGIN~~~~~~~~~~~~~~~~~~~~~~~~~
        else if(message instanceof Login){
            Login login = (Login)message;
            if(!dataBase.containsUser(login.getUserName()) &&
                    dataBase.getUser(login.getUserName()).getPassWord() != login.getPassWord() &&
                    dataBase.isConnected(id, login.getUserName())){
                ErrorMessage errorMessage = new ErrorMessage((short)11, (short)2);
                Connection.send(id, errorMessage);
            }else{
                dataBase.connectUser(id, login.getUserName());
                Connection.send(id, new ACK((short)10 , (short)2));
            }
        }
        //~~~~~~~~~~~~~~~~~~~~~~~LOGOUT~~~~~~~~~~~~~~~~~~~~~~~~~~~
        else if(message instanceof Logout){
            // TODO: IMPLEMENT

        }
        //~~~~~~~~~~~~~~~~~~~~~~~~FOLLOW\UN-FOLLOW~~~~~~~~~~~~~~~
        else if(message instanceof Follow_UnFollow){
            Follow_UnFollow follow_unFollow = ((Follow_UnFollow)message);
            if(!dataBase.isConnected(id)){
                Connection.send(id, new ErrorMessage((short)11, (short)4));
            }// the client is connected.
            else{
                    if(follow_unFollow.isFollow_true_Unfollow_false()){
                        List<String> succssFollow = dataBase.TryFollow(follow_unFollow.getUserNameList());

                    }
                    else{
                        List<String> successUnFollow = dataBase.TryUnFollow(follow_unFollow.getUserNameList());
                    }
            }

        }
        //~~~~~~~~~~~~~~~~~~~~~~~~POST~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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

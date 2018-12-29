package bgu.spl.net.api.bidi;

import bgu.spl.net.api.Messages.*;

import java.util.List;
import java.util.Vector;

public class BGUMessagesProtocol implements BidiMessagingProtocol<Message> {

    private short opcode;
    private Connections <Message> Connection;
    private int id;
    private DataBase dataBase;
    private boolean ProtocolLogin = false;
    private String ProtocolUserName = null;

    @Override
    public void start(int connectionId, Connections<Message> connections) {
        Connection  = connections;
        this.id = connectionId;
    }

    @Override
    public void process(Message message) {

        DataBase dataBase = DataBase.getInstance();

        //~~~~~~~~~~~~~~~~~~~~~~~~~REGISTER~~~~~~~~~~~~~~~~~~~~~
        if (message instanceof Register) {
            String name = ((Register) message).getUserName();
            String password = ((Register) message).getPassWord();
            BGUUser bguUser = new BGUUser(name, password);
            // the user is already registered or there is a user with the same name
            if (dataBase.containsUser(bguUser.getUserName())) {
                Connection.send(id, new ErrorMessage((short) 11, (short) 1));
            } else {
                // register a new user.
                dataBase.addCustomer(bguUser);
                Connection.send(id, new ACK((short) 10, (short) 1));
            }
        }
        //~~~~~~~~~~~~~~~~~~~~~~~~~LOGIN~~~~~~~~~~~~~~~~~~~~~~~~~
        else if (message instanceof Login) {
            Login login = (Login) message;
            if (!dataBase.containsUser(login.getUserName()) ||
                    dataBase.getUser(login.getUserName()).getPassWord() != login.getPassWord() ||
                    ProtocolLogin == true) {
                Connection.send(id, new ErrorMessage((short) 11, (short) 2));
            } else {
                dataBase.connectUser(id, login.getUserName());
                ProtocolLogin = true;
                ProtocolUserName = login.getUserName();
                Connection.send(id, new ACK((short) 10, (short) 2));
            }
        }
        //~~~~~~~~~~~~~~~~~~~~~~~LOGOUT~~~~~~~~~~~~~~~~~~~~~~~~~~~
        else if (message instanceof Logout) {
            // TODO: IMPLEMENT
        }
        //~~~~~~~~~~~~~~~~~~~~~~~~FOLLOW\UN-FOLLOW~~~~~~~~~~~~~~~
        else if (message instanceof Follow_UnFollow) {
            Follow_UnFollow follow_unFollow = ((Follow_UnFollow) message);
            if (ProtocolLogin) {
                Connection.send(id, new ErrorMessage((short) 11, (short) 4));
            }// the client is connected.
            else {
                List<String> successFollow_UnFollow = new Vector<>();
                if (follow_unFollow.isFollow_true_Unfollow_false())
                    successFollow_UnFollow = dataBase.TryFollow(follow_unFollow.getUserNameList(), ProtocolUserName);
                successFollow_UnFollow = dataBase.TryUnFollow(follow_unFollow.getUserNameList(), ProtocolUserName);

                if (successFollow_UnFollow.size() > 0) {
                    ACK ack = new ACK((short) 10, (short) 4);
                    ack.setOptional(dataBase.toStringLists(successFollow_UnFollow));
                    Connection.send(id, ack);
                }
                // there was no successful follow/ unfollow.
                else Connection.send(id, new ErrorMessage((short) 11, (short) 4));
            }
        }
        //~~~~~~~~~~~~~~~~~~~~~~~~POST~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        else if (message instanceof Post) {
            Post post = (Post)message;
            if (ProtocolLogin)
                Connection.send(id, new ErrorMessage((short) 11, (short) 5));
            else{
                    String content = post.getContent();

                }

            }
        //~~~~~~~~~~~~~~~~~~~~~~PM~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        else if (message instanceof PM) {
        PM pm = (PM)message;
            if (ProtocolLogin || !dataBase.containsUser(pm.getUserNameToSendTo()))
                Connection.send(id, new ErrorMessage((short) 11, (short) 6));
            else{
                Connection.send(id, new ACK((short) 10, (short) 6));


            }

        }
        //~~~~~~~~~~~~~~~~~~~~~~~~USER-LIST~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        else if (message instanceof UserListRequest) {
            if (ProtocolLogin) {
                Connection.send(id, new ErrorMessage((short) 11, (short) 7));
            } else {
                String str = dataBase.toStringLists(dataBase.getListRegistrationOrder());
                ACK ack = new ACK((short) 10, (short) 7);
                ack.setOptional(str);
                Connection.send(id, ack);
            }
        }
            //~~~~~~~~~~~~~~~~~~~~~~~STATS-REQUEST~~~~~~~~~~~~~~~~~~~~~~~~~
        else if (message instanceof StatsRequest) {
            StatsRequest statsrequest = (StatsRequest) message;
            // if the user name we want to get data on did not register
            if (ProtocolLogin || !dataBase.containsUser(statsrequest.getUserNameToGetDataOn()))
                Connection.send(id, new ErrorMessage((short) 11, (short) 8));
            else {
                ACK ack = new ACK((short) 10, (short) 8);
                BGUUser bguUser = dataBase.getUser(statsrequest.getUserNameToGetDataOn());
                String str = "" + bguUser.getNumOfPosts() + "\0" + bguUser.getNumOfFollowers()
                        + "\0" + bguUser.getNumUsersIAmFollowing() + "\0";
                ack.setOptional(str);
                Connection.send(id, ack);
                }
            }
        }

        public void sendNotification (String userName){
            int IdConnectionToSendTo = dataBase.isConnected(userName);
            // the user I want to send to is connected
            if(IdConnectionToSendTo!= -1){

            }
        }


            @Override
            public boolean shouldTerminate () {
                return false;
            }
        }


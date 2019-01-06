package bgu.spl.net.api.bidi;

import bgu.spl.net.api.Messages.*;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BGUMessagesProtocol implements BidiMessagingProtocol<Message> {

    private Connections <Message> Connection;
    private int id;
    private DataBase dataBase;
    private boolean ProtocolLogin = false;
    private String ProtocolUserName = null;

    public BGUMessagesProtocol(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    public void start(int connectionId, Connections<Message> connections) {
        Connection  = connections;
        this.id = connectionId;
    }

    @Override
    public void process(Message message) {

        //~~~~~~~~~~~~~~~~~~~~~~~~~REGISTER~~~~~~~~~~~~~~~~~~~~~
        if (message instanceof Register) {
            String name = ((Register) message).getUserName();
            String password = ((Register) message).getPassWord();
            BGUUser bguUser = new BGUUser(name, password);
            synchronized (dataBase) {
                // the user is already registered or there is a user with the same name
                if (dataBase.containsUser(bguUser.getUserName())) {
                    Connection.send(id, new ErrorMessage((short) 11, (short) 1));
                } else {
                    // register a new user.
                    dataBase.addCustomer(bguUser);
                    Connection.send(id, new ACK((short) 10, (short) 1));
                }
            }
        }
        //~~~~~~~~~~~~~~~~~~~~~~~~~LOGIN~~~~~~~~~~~~~~~~~~~~~~~~~
        else if (message instanceof Login) {
            Login login = (Login) message;
            if (dataBase.containsUser(login.getUserName())) {
                synchronized (dataBase.getUser(login.getUserName())) {
                    if (!dataBase.containsUser(login.getUserName()) ||
                            !dataBase.getUser(login.getUserName()).getPassWord().equals(login.getPassWord()) ||
                            ProtocolLogin == true ||
                            dataBase.isConnected(login.getUserName()) != -1) {
                        Connection.send(id, new ErrorMessage((short) 11, (short) 2));
                    } else {
                        dataBase.connectUser(id, login.getUserName());
                        ProtocolLogin = true;
                        ProtocolUserName = login.getUserName();
                        Connection.send(id, new ACK((short) 10, (short) 2));
                        //check if he has future messages and send them.
                        ConcurrentLinkedQueue<Notification> futureMessages = dataBase.getUser(ProtocolUserName).getFutreMessagesToBeSent();
                        while (futureMessages.size() > 0)
                            Connection.send(id, futureMessages.poll());
                    }
                }
            }
            else
                Connection.send(id, new ErrorMessage((short) 11, (short) 2));
        }

        //~~~~~~~~~~~~~~~~~~~~~~~LOGOUT~~~~~~~~~~~~~~~~~~~~~~~~~~~
        else if (message instanceof Logout) {
            Logout logout = (Logout)message;
            if(!ProtocolLogin){
                Connection.send(id, new ErrorMessage((short) 11, (short) 3));
            }
            else {
                synchronized (dataBase.getUser(ProtocolUserName)) {
                    dataBase.disconnectUser(ProtocolUserName);
                    ProtocolLogin = false;
                    ProtocolUserName = null;
                    Connection.send(id, new ACK((short) 10, (short) 3));
                    Connection.disconnect(id);
                }
            }
        }
        //~~~~~~~~~~~~~~~~~~~~~~~~FOLLOW\UN-FOLLOW~~~~~~~~~~~~~~~
        else if (message instanceof Follow_UnFollow) {
            Follow_UnFollow follow_unFollow = ((Follow_UnFollow) message);
            if (!ProtocolLogin) {
                Connection.send(id, new ErrorMessage((short) 11, (short) 4));
            }// the client is connected.
            else {
                List<String> successFollow_UnFollow;
                if (follow_unFollow.isFollow_true_Unfollow_false())
                    successFollow_UnFollow = dataBase.TryFollow(follow_unFollow.getUserNameList(), ProtocolUserName);
                else
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
            if (!ProtocolLogin)
                Connection.send(id, new ErrorMessage((short) 11, (short) 5));
            else{
                    String content = post.getContent();
                    String [] arr = content.split(" ");
                    Vector<String> userNames = new Vector<String>();
                    for(String s: arr){
                        if(s.indexOf('@')!= -1 & !userNames.contains(s.substring((s.indexOf('@') +1)))){
                            userNames.add(s.substring((s.indexOf('@') +1)));
                        }
                    }
                    // taking care of @<userName>
                    for(String user: userNames){
                        // check if this user is registered to the system send him the post.
                        if(dataBase.containsUser(user) && !dataBase.getUser(ProtocolUserName).getMyfollowers().contains(user)){
                            sendNotification(ProtocolUserName, '1', content, user);
                        }
                    }
                    // send to all my followers
                    BGUUser bguUser = dataBase.getUser(ProtocolUserName);
                    for(String userName: bguUser.getMyfollowers()){
                        sendNotification(ProtocolUserName,'1',content, userName);
                    }
                // add to all post saved in the dataBase
                dataBase.addPostPm(content);
                // increment user num of posts
                dataBase.getUser(ProtocolUserName).incrementNumOfPosts();
                Connection.send(id, new ACK((short) 10, (short) 5));
                }
            }
        //~~~~~~~~~~~~~~~~~~~~~~PM~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        else if (message instanceof PM) {
        PM pm = (PM)message;
            if (!ProtocolLogin || !dataBase.containsUser(pm.getUserNameToSendTo()))
                Connection.send(id, new ErrorMessage((short) 11, (short) 6));
            else{
                Connection.send(id, new ACK((short) 10, (short) 6));
                sendNotification(ProtocolUserName, '0', pm.getContent(), pm.getUserNameToSendTo());
                // add to all post saved in the dataBase
                dataBase.addPostPm(pm.getContent());
            }
        }
        //~~~~~~~~~~~~~~~~~~~~~~~~USER-LIST~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        else if (message instanceof UserListRequest) {
            if (!ProtocolLogin) {
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
            if (!ProtocolLogin || !dataBase.containsUser(statsrequest.getUserNameToGetDataOn()))
                Connection.send(id, new ErrorMessage((short) 11, (short) 8));
            else {
                ACK ack = new ACK((short) 10, (short) 8);
                BGUUser bguUser = dataBase.getUser(statsrequest.getUserNameToGetDataOn());
                String str = "" + bguUser.getNumOfPosts() + " " + bguUser.getNumOfFollowers()
                        + " " + bguUser.getNumUsersIAmFollowing() + " ";
                ack.setOptional(str);
                Connection.send(id, ack);
                }
            }
        }


        public void sendNotification (String userName, char pm_post, String content, String userToSendTo){
            Notification notification = new Notification(pm_post, userName, content);
            int IdConnectionToSendTo = dataBase.isConnected(userToSendTo);
            // the user I want to send to is connected - send NOW
            synchronized (dataBase.getUser(userToSendTo)) {
                if (IdConnectionToSendTo != -1) {
                    Connection.send(IdConnectionToSendTo, notification);
                }// not connected i will save in his messages To send Queue.
                else {
                    BGUUser bguUser = dataBase.getUser(userToSendTo);
                    bguUser.addToFuterMessage(notification);
                }
            }
        }


            @Override
            public boolean shouldTerminate () {
                return false;
            }
        }


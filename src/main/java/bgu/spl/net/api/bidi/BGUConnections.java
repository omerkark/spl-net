package bgu.spl.net.api.bidi;


import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import bgu.spl.net.srv.ConnectionHandler;

public class BGUConnections<T> implements Connections<T> {

    private ConcurrentHashMap<Integer , ConnectionHandler<T>> connectionHandler;

    public BGUConnections() {
        this.connectionHandler = new ConcurrentHashMap<>();
    }

    @Override
    public boolean send(int connectionId, T msg) {
        // mybe we need to sync if you need to send a message and in the same time he unregisters.
        if(connectionHandler.containsKey(connectionId)){
            connectionHandler.get(connectionId).send(msg);
            return true;
        }
        return false;
    }

    @Override
    public void broadcast(T msg) {
        for(int id: connectionHandler.keySet()){
            connectionHandler.get(id).send(msg);
        }
    }

    @Override
    public void disconnect(int connectionId) {
        if (connectionHandler.containsKey(connectionId)){
            try {
                connectionHandler.get(connectionId).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connectionHandler.remove(connectionId);
        }
    }

    public void connect(ConnectionHandler toConnect){
        if(!connectionHandler.contains(toConnect)){
            // add to the hash map the connection handler
        }
    }

    public ConnectionHandler getConectionHandler(int id){
        return connectionHandler.get(id);
    }
}

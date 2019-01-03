package bgu.spl.net.api.bidi;


import java.util.concurrent.ConcurrentHashMap;
import bgu.spl.net.srv.ConnectionHandler;

public class BGUConnections<T> implements Connections<T> {

    private ConcurrentHashMap<Integer , ConnectionHandler<T>> connectionHandlers;

    public BGUConnections() {
        this.connectionHandlers = new ConcurrentHashMap<>();
    }

    @Override
    public boolean send(int connectionId, T msg) {
        synchronized (connectionHandlers.get(connectionId)) {
            if (connectionHandlers.containsKey(connectionId)) {
                connectionHandlers.get(connectionId).send(msg);
                return true;
            }
            return false;
        }
    }

    @Override
    public void broadcast(T msg) {
        for(int id: connectionHandlers.keySet()){
            connectionHandlers.get(id).send(msg);
        }
    }

    @Override
    public void disconnect(int connectionId) {
        if (connectionHandlers.containsKey(connectionId))
            connectionHandlers.remove(connectionId);
    }

    public void addConnection(ConnectionHandler toConnect, int id){
            connectionHandlers.putIfAbsent(id, toConnect);

    }

}

package bgu.spl.net.api.bidi;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class DataBase {


    private static class SingletonDataBase {
		private static DataBase DateBaseInstance = new DataBase();
	}

	// fields
	private List<BGUClient> clients;
    // hash map between an id and a client.
    private HashMap<Integer, String> iDToCustomer;

	public static DataBase getInstance() {
		return SingletonDataBase.DateBaseInstance;
	}

	private DataBase(){
	    clients = new Vector<>();
	    iDToCustomer = new HashMap<>();
	}

	public void addCustomer(BGUClient bguClient, int id){
	    //TODO: think if we need to sync can a few people try to add at the same time ?
	    clients.add(bguClient);
	    iDToCustomer.put(id, bguClient.getUserName());
    }

    public boolean coantainsClient(int id){
	    return iDToCustomer.containsKey(id);
    }



}

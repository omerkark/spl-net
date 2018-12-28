package bgu.spl.net.api.bidi;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class DataBase {


    private static class SingletonDataBase {
		private static DataBase DateBaseInstance = new DataBase();
	}

	// fields

	// all users how registered to the system.
	private List<BGUUser> users;
    // hash map between an id and a client for users conncted to the system.
    private HashMap<Integer, String> iDToCustomer;

	public static DataBase getInstance() {
		return SingletonDataBase.DateBaseInstance;
	}

	private DataBase(){
	    users = new Vector<>();
	    iDToCustomer = new HashMap<>();
	}

	public void addCustomer(BGUUser bguUser){
	    //TODO: think if we need to sync can a few people try to add at the same time ?
	    users.add(bguUser);
    }

    // check if the user is already registered to the sys by user name.
    public boolean containsUser(String bguUser){
	    for(BGUUser user: users){
	    	if(user.getUserName() == bguUser)
	    		return true;
		}
	    return false;
    }

    // returns the user if exists or null otherwise
    public BGUUser getUser(String bguUser){
		for(BGUUser user: users){
			if(user.getUserName() == bguUser)
				return user;
		}
		return null;
	}

	// checks if a user is already connected from this client or a diffrent client
	// mybe we can remove thr id check
    public boolean isConnected(int id, String name){
		return iDToCustomer.containsKey(id) & iDToCustomer.containsValue(name);
	}

	public boolean isConnected(int id){
		return iDToCustomer.containsKey(id);
	}

	public void connectUser(int id, String bguUser){
		iDToCustomer.put(id , bguUser);
		// change the status of the customer to true
		getUser(bguUser).setLogIn(true);
	}

	public List<String> TryFollow(List<String> userNamesToFollow){
		return null;
	}

	public List<String> TryUnFollow(List<String> userNamesToFollow){
		return null;
	}



}

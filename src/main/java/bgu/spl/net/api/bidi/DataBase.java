package bgu.spl.net.api.bidi;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class DataBase {


    private static class SingletonDataBase {
		private static DataBase DateBaseInstance = new DataBase();
	}

	// fields

	// all users how registered to the system.
	private HashMap<String, BGUUser> users;
    // hash map between an id and a client for users conncted to the system.
    private HashMap<Integer, String> iDToCustomer;
    private List<String> listRegistrationOrder;

	public static DataBase getInstance() {
		return SingletonDataBase.DateBaseInstance;
	}

	private DataBase(){
	    users = new HashMap<>();
	    iDToCustomer = new HashMap<>();
	    listRegistrationOrder = new Vector<>();
	}

	public void addCustomer(BGUUser bguUser){
	    //TODO: think if we need to sync can a few people try to add at the same time ?
	    users.put(bguUser.getUserName(), bguUser);
	    listRegistrationOrder.add(bguUser.getUserName());
    }

    // check if the user is already registered to the sys by user name.
    public boolean containsUser(String bguUserName){
	    for(String userName: users.keySet()){
	    	if(userName == bguUserName)
	    		return true;
		}
	    return false;
    }

    // returns the user if exists or null otherwise
    public BGUUser getUser(String bguUser){
		if(users.containsKey(bguUser))
			return users.get(bguUser);
		return null;
	}

	public List<String> getListRegistrationOrder() {
		return listRegistrationOrder;
	}

	// checks if a user is already connected from this client or a diffrent client
	// maybe we can remove thr id check
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

	public List<String> TryFollow(List<String> userNamesToFollow, int id){
		BGUUser bguUser = getUser(iDToCustomer.get(id));
		List<String> sucssesFollowers = new Vector<>();
		// go over the potential users to follow
		for(String userToFollow: userNamesToFollow){
			// check if i am already folloing after them
			if(!bguUser.getFollowing().contains(userToFollow)){
				//checks if the person i want to follow is registered to the system
				if(users.containsKey(userToFollow)){
					bguUser.getFollowing().add(userToFollow);
					sucssesFollowers.add(userToFollow);
				}
			}
		}
		return sucssesFollowers;
	}

	public List<String> TryUnFollow(List<String> userNamesToUnFollow, int id){
		BGUUser bguUser = getUser(iDToCustomer.get(id));
		List<String> sucssesUnFollowers = new Vector<>();
		// go go over the potential users to Unfollow
		for(String userToUnFollow: userNamesToUnFollow){
			// check if we are already following the user we want to unFollow.
			if(bguUser.getFollowing().contains(userToUnFollow)){
				bguUser.getFollowing().remove(userToUnFollow);
				sucssesUnFollowers.add(userToUnFollow);
			}
		}
		return sucssesUnFollowers;
	}

	public String toStringLists(List<String> listToString) {
		Integer i = listToString.size();
		String str = i.toString();
		for(String s: listToString){
			str += "\0" + s;
		}
	}
}

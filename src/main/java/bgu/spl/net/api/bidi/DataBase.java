package bgu.spl.net.api.bidi;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class DataBase {

    private static class SingletonDataBase {
		private static DataBase DateBaseInstance = new DataBase();
	}

	// all users how registered to the system.
	private HashMap<String, BGUUser> users;
    // hash map between an id and a client for users conncted to the system.
    private HashMap<String, Integer> iDToCustomer;
    // all users how wver registered.
    private List<String> listRegistrationOrder;
    private List<String> postsPm;

	public static DataBase getInstance() {
		return SingletonDataBase.DateBaseInstance;
	}

	private DataBase(){
	    users = new HashMap<>();
	    iDToCustomer = new HashMap<>();
	    listRegistrationOrder = new Vector<>();
	    postsPm = new Vector<>();
	}

	public void addCustomer(BGUUser bguUser){
	    //TODO: think if we need to sync can a few people try to add at the same time ?
	    users.put(bguUser.getUserName(), bguUser);
	    listRegistrationOrder.add(bguUser.getUserName());
    }

    // check if the user is already registered to the sys by user name.
    public boolean containsUser(String bguUserName){
	    for(String userName: users.keySet()){
	    	if(userName.equals(bguUserName))
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
    public int isConnected(String name){
		if (iDToCustomer.containsKey(name))
		    return iDToCustomer.get(name);
		return -1;
	}

	public void connectUser(int id, String bguUser){
		iDToCustomer.put(bguUser ,id);
		// change the status of the customer to true
		getUser(bguUser).setLogIn(true);
	}

	public void disconnectUser(String bguUser){
		iDToCustomer.remove(bguUser);
		getUser(bguUser).setLogIn(false);
	}

	public List<String> TryFollow(List<String> userNamesToFollow, String UserName){
		BGUUser bguUser = getUser(UserName);
		List<String> sucssesFollowers = new Vector<>();
		// go over the potential users to follow
		for(String userToFollowName: userNamesToFollow){
			// check if i am already following after them
			if(!bguUser.getIAmfollowing().contains(userToFollowName)){
				//checks if the person i want to follow is registered to the system
				if(users.containsKey(userToFollowName)){
					BGUUser userToFollow = getUser(userToFollowName);
					//add some one to my following list
					bguUser.startFollowing(userToFollowName);
					// add a follower to the user i started following
					userToFollow.addFollower(bguUser.getUserName());
					sucssesFollowers.add(userToFollowName);
				}
			}
		}
		return sucssesFollowers;
	}

	public List<String> TryUnFollow(List<String> userNamesToUnFollow, String UserName){
		BGUUser bguUser = getUser(UserName);
		List<String> sucssesUnFollowers = new Vector<>();
		// go go over the potential users to Unfollow
		for(String userToUnFollowName: userNamesToUnFollow){
			// check if we are already following the user we want to unFollow.
			if(bguUser.getIAmfollowing().contains(userToUnFollowName)){
				BGUUser userToUnFollow = getUser(userToUnFollowName);
				// remove the user from the user i am following
				bguUser.removeIAmFollowing(userToUnFollowName);
				//remove me from that user followers.
				userToUnFollow.removeFollower(bguUser.getUserName());
				sucssesUnFollowers.add(userToUnFollowName);
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
		str += "\0";
		return str;
	}

	public void addPostPm (String content){
	    postsPm.add(content);
    }

}

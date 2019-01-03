package bgu.spl.net.api.bidi;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataBase {

	// all users registered in the system.
	private ConcurrentHashMap<String, BGUUser> users;
    // hash map between an id and a client for users conncted to the system.
    private ConcurrentHashMap<String, Integer> iDToCustomer;
    // all users how wver registered.
    private List<String> listRegistrationOrder;
    private List<String> postsPm;
	private ReadWriteLock userListLock;

	public DataBase(){
	    users = new ConcurrentHashMap<>();
	    iDToCustomer = new ConcurrentHashMap<>();
	    listRegistrationOrder = new Vector<>();
	    postsPm = new Vector<>();
	    userListLock = new ReentrantReadWriteLock(true);
	}

	public void addCustomer(BGUUser bguUser){
		userListLock.readLock().lock();
		users.put(bguUser.getUserName(), bguUser);
		listRegistrationOrder.add(bguUser.getUserName());
		userListLock.readLock().unlock();
    }

    // check if the user is already registered to the sys by user name.
    public boolean containsUser(String bguUserName){
		return users.containsKey(bguUserName);
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
		userListLock.writeLock().lock();
		Integer i = listToString.size();
		String str = i.toString();
		for(String s: listToString){
			str += "\0" + s;
		}
		str += "\0";
		userListLock.writeLock().unlock();
		return str;
	}

	public void addPostPm (String content){
	    postsPm.add(content);
    }

}

package main.remote;

import java.util.HashSet;
import java.util.Set;

import main.configuration.UserSystem;

public class UserServer extends UserSystem
{
    private String _USER_ID, _CONTROLED_USER_ID;
    private Set<String> _REMOTE_USERS_IDS;

    public UserServer() {
        _REMOTE_USERS_IDS = new HashSet<>();
    }

    public void setControledUserId(String controled_user_id) {
		_CONTROLED_USER_ID = controled_user_id;
    }

    public String getControledUserId() {
		return _CONTROLED_USER_ID;
    }

    public void setUserId(String user_id) {
        _USER_ID = user_id;
    }

    public String getUserId() {
        return _USER_ID;
    }

    public Set<String> getRemoteUsersIds() {
        return _REMOTE_USERS_IDS;
    }

    public void addRemoteUserId(String remote_user_id) {
        _REMOTE_USERS_IDS.add(remote_user_id);
    }

    public void removeRemoteUserId(String remote_user_id) {
        _REMOTE_USERS_IDS.remove(remote_user_id);
    }
}

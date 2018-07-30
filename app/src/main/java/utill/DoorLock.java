package utill;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class DoorLock {
    public String itemCode;
    public String psw;
    //public User user;
//    public ArrayList<User> userList;
    public HashMap<String,String> userList;
    public boolean motor;
    public boolean sensor;
    public static boolean isUserHasAccess = false;

    public DoorLock() {
        itemCode = "Item001";
        psw = "1234";
        motor = false;
        sensor = false;
        userList = new HashMap<String,String>();
        userList.put("admin","1234");
    }

    public boolean isUserHasAccess(String userName) {
        for (String userkey:userList.keySet()) {
            if(userList.get(userkey).equals(userName)){
                return true;
            }
        }
        return false;
    }

    public void addUser(String userName, String psw){
        userList.put(userName,psw);
    }

    public void removeUser(String userName){
        userList.remove(userName);
    }

    public void unlock(){
        this.motor = true;
    }
}

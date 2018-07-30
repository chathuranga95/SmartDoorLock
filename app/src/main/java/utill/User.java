package utill;

public class User {
    public String userName;
    public String psw;

    public User(String userName, String psw) {
        this.userName = userName;
        this.psw = psw;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isAuthenticated(String psw){
        return (psw.equals(this.psw));
    }
}

package app.android.scc331.rest_test.Objects;

public class UserData {

    private String username;
    private int id;

    public UserData(String username, int id){
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}

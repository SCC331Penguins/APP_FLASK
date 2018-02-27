package app.android.scc331.rest_test.Objects;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;

import app.android.scc331.rest_test.Util.InternalStorage;

public class LoginDetail implements Serializable {

    private static final String SAVE_KEY = "login_details";

    private String username = null;
    private String password = null;
    private boolean autoLogin;

    public static LoginDetail load(Context context){
        if(InternalStorage.fileExistance(context, SAVE_KEY)) {
            try {
                return (LoginDetail) InternalStorage.readObject(context, SAVE_KEY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new LoginDetail();
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public boolean getAutoLogin(){
        return autoLogin;
    }

    public LoginDetail setLoginDetails(String username, String password, boolean autoLogin){
        this.username = username;
        this.password = password;
        this.autoLogin = autoLogin;
        return this;
    }

    public void save(Context context){
        try {
            InternalStorage.writeObject(context, SAVE_KEY, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

package htlgrieskirchen.net.tawimmer.todo_newtry.Cloud;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

public class User implements Serializable {

    String name;
    String nickname;
    String username;
    String password;
    InternetConnectionHandler internetConnectionHandler;
    int id;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        internetConnectionHandler = new InternetConnectionHandler();
    }

    protected User(Parcel in) {
        username = in.readString();
        password = in.readString();
    }

    public boolean userLogin(InternetConnectionHandler connectionHandler) {
        InternetConnectionHandler.Response response = connectionHandler.post("http://sickinger-solutions.at/notesserver/"+"todolists.php?username="+ username + "&password="+ password,"");

        boolean result = response.startWith(2);
        System.out.println("result:" + result);
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.inputStream));

            JsonObject jsonObject = new JsonParser().parse(bufferedReader.readLine()).getAsJsonObject();
            response.close();
            if (jsonObject.get("message").getAsString().equals("username or password not found")) {
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean userSignIn() {
        JSONObject jSONObject = new JSONObject();

        try {
            jSONObject.put("username", username);
            jSONObject.put("password", password);
            jSONObject.put("name", name);

            InternetConnectionHandler.Response response = internetConnectionHandler.post("http://sickinger-solutions.at/notesserver/register.php?", jSONObject.toString());
            boolean suc = response.startWith(2);
            response.close();
            return suc;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkPassword() {
        if (password.length() > 0) return true;
        return false;
    }

    private boolean checkUsername() {
        if (username.length() > 0) return true;
        return false;
    }




    public String getName() {
        return name;
    }

    public void setName(String lastname) {
        this.name = lastname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

